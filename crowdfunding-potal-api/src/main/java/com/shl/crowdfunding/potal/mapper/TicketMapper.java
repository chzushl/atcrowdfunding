package com.shl.crowdfunding.potal.mapper;

import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.bean.Ticket;

import java.util.List;

public interface TicketMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ticket record);

    Ticket selectByPrimaryKey(Integer id);

    List<Ticket> selectAll();

    Ticket getTicketByMemberId(Integer id);

    int saveTicket(Ticket ticket);

    int updateTicket(Ticket ticket);

    void updateTicket4PI(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}
