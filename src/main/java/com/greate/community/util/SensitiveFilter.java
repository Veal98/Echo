package com.greate.community.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤器
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 将敏感词替换成 ***
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    /**
     * 初始化前缀树
     */
    @PostConstruct // 初始化方法
    public void init() {
        try (
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败" + e.getMessage());
        }


    }

    /**
     * 将一个敏感词添加进前缀树中
     * @param keyword
     */
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i ++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);// 首先判断是否存在相同子节点

            if (subNode == null) {
                subNode = new TrieNode(); // 初始化子节点
                tempNode.addSubNode(c, subNode); // 添加子节点
            }

            // 指向子节点，进入下一层循环
            tempNode = subNode;

            // 设置结束标识（叶子节点），表示这个字符是该敏感词的最后一个字符
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本（即用 *** 替代敏感词）
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针 1：前缀树的工作指针
        TrieNode tempNode = rootNode;
        // 指针 2：指向文本中某个敏感词的第一位
        int begin = 0;
        // 指针 3；指向文本中某个敏感词的最后一位
        int end = 0;

        // 记录过滤后的文本（结果）
        StringBuilder sb = new StringBuilder();

        while (end < text.length()) {
            char c = text.charAt(end);
            // 跳过符号（防止敏感词混合符号，比如 ☆赌☆博）
            if (isSymbol(c)) {
                // 若指针 1 处于根节点，则将此符号计入结果（直接忽略），让指针 2 向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin ++;
                }
                // 无论符号在开头还是在中间，指针 3 都会向下走一步
                end ++;
                continue;
            }

            // 检查子节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以指针 begin 开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一位的判断
                begin ++;
                end = begin;
                // 指针 1 重新指向根节点
                tempNode = rootNode;
            }
            else if (tempNode.isKeywordEnd()) {
                // 发现敏感词，将 begin~end 的字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一位的判断
                end ++;
                begin = end;
                // 指针 1 重新指向根节点
                tempNode = rootNode;
            }
            else {
                // 检查下一个字符
                end ++;
            }
        }

        // 将最后一批字符计入结果（如果最后一次循环的字符串不是敏感词，上述的循环逻辑不会将其加入最终结果）
        sb.append(text.substring(begin));

        return sb.toString();
    }

    // 判断某个字符是否是符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 定义前缀树
     */
    private class TrieNode {
        // 关键词结束标识（叶子节点）
        private boolean isKeywordEnd = false;
        // 子节点(key:子节点字符, value:子节点类型)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}
