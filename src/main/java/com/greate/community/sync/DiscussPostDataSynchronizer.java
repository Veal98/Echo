package com.greate.community.sync;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.greate.community.entity.Event;
import com.greate.community.event.EventProducer;
import com.greate.community.util.CommunityConstant;

/**
 * Mysql与ES的帖子部分的数据源同步器
 * 引入Canal实现Mysql与ES的数据源同步
 */

@Component
public class DiscussPostDataSynchronizer implements InitializingBean, CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostDataSynchronizer.class);

    @Value("${canal-monitor.host:127.0.0.1}")
    private String canalMonitorHost;

    @Value("${canal-monitor.port:11111}")
    private int canalMonitorPort;

    @Value("${canal-monitor.table-name:discuss_post}")
    private String canalMonitorTableName;

    @Value("${canal-monitor.db-name:echo}")
    private String canalMonitorDBName;

    @Value("${canal-monitor.destination}")
    private String canalMonitorDestination;

    @Value("${canal-monitor.username:}")
    private String canalMonitorUsername;

    @Value("${canal-monitor.password:}")
    private String canalMonitorPassword;

    @Resource
    private EventProducer eventProducer;

    private final static int BATCH_SIZE = 10000;


    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            while (true) {
                CanalConnector connector =
                        CanalConnectors.newSingleConnector(new InetSocketAddress(canalMonitorHost, canalMonitorPort),
                                canalMonitorDestination,
                                canalMonitorUsername, canalMonitorPassword);
                try {
                    //打开连接
                    connector.connect();
                    logger.info("Canal connect success! Listening mysql table:" + canalMonitorTableName);
                connector.subscribe(canalMonitorDBName+"."+canalMonitorTableName);
                    //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
                    connector.rollback();
                    while (true) {
                        // 获取指定数量的数据
                        Message message = connector.getWithoutAck(BATCH_SIZE);
                        long batchId = message.getId();
                        int size = message.getEntries().size();
                        if (batchId == -1 || size == 0) {
                        } else {
                            handleDataChange(message.getEntries());
                        }
                        // 提交确认
                        connector.ack(batchId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Connect wrong! reconnecting...");
                } finally {
                    connector.disconnect();
                    //防止频繁访问数据库链接: 线程睡眠 10秒
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /**
     * 打印canal server解析binlog获得的实体类信息
     */
    private void handleDataChange(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN ||
                    entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            CanalEntry.RowChange rowChage;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                logger.error("Canal parse row has an error, data:{}, exception:{}", entry, e.toString());
                throw new RuntimeException("Canal parse has an error , data:" + entry, e);
            }
            CanalEntry.EventType eventType = rowChage.getEventType();
            logger.info("Canal catching updating binlog:【{}】", entry.getHeader().getTableName());
            switch (eventType) {
                /**
                 * 删除操作
                 */
                case DELETE:

                    deleteDiscussPost(rowChage, entry);
                    break;
                /**
                 * 添加与更新操作
                 */
                case INSERT:
                case UPDATE:
                    addOrUpdateDiscussPost(rowChage, entry);
                    break;
                default:
                    break;
            }

        }
    }

    private void deleteDiscussPost(CanalEntry.RowChange rowChange, CanalEntry.Entry entry) {
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        rowDatasList.forEach(each -> {
            Event event = new Event()
                    .setTopic(TOPIC_DELETE)
                    .setEntityType(ENTITY_TYPE_POST);
            List<CanalEntry.Column> beforeColumnsList = each.getBeforeColumnsList();
            constructEventAndSend(event, beforeColumnsList);
        });


    }

    private void addOrUpdateDiscussPost(CanalEntry.RowChange rowChange, CanalEntry.Entry entry) {
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        rowDatasList.forEach(each -> {
            Event event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setEntityType(ENTITY_TYPE_POST);
            List<CanalEntry.Column> afterColumnsList = each.getAfterColumnsList();
            constructEventAndSend(event, afterColumnsList);
        });
    }

    private void constructEventAndSend(Event event, List<CanalEntry.Column> afterColumnsList) {
        afterColumnsList.forEach(eachColumn -> {
            String columnName = eachColumn.getName();
            String columnValue = eachColumn.getValue();
            if ("user_id".equals(columnName)) {
                event.setUserId(Integer.parseInt(columnValue));
            } else if ("id".equals(columnName)) {
                event.setEntityId(Integer.parseInt(columnValue));
            }
        });
        logger.info("send event message:{}", event);
        eventProducer.fireEvent(event);
    }
}

