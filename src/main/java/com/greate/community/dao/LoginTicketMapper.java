package com.greate.community.dao;

import com.greate.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Deprecated
public interface LoginTicketMapper {

    /**
     * 插入一条 LoginTicket
     * @param loginTicket
     * @return
     */
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 根据 ticket 查询 LoginTicket
     * @param ticket
     * @return
     */
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新凭证状态，0：有效，1：无效
     * @param ticket
     * @param status
     * @return
     */
    int updateStatus(String ticket, int status);

}
