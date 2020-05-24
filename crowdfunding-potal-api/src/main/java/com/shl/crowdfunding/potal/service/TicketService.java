package com.shl.crowdfunding.potal.service;

import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.bean.Ticket;

public interface TicketService {

    Ticket getTicketByMemberId(Integer id);

    int saveTicket(Ticket ticket);

    int updateTicket(Ticket ticket);

    void updateTicket4PI(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}
