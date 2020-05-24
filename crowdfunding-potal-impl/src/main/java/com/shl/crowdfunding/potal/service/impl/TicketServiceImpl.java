package com.shl.crowdfunding.potal.service.impl;

import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.bean.Ticket;
import com.shl.crowdfunding.potal.mapper.TicketMapper;
import com.shl.crowdfunding.potal.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public Ticket getTicketByMemberId(Integer id) {
        return ticketMapper.getTicketByMemberId(id);
    }

    @Override
    public int saveTicket(Ticket ticket) {
        return ticketMapper.saveTicket(ticket);
    }

    @Override
    public int updateTicket(Ticket ticket) {
        return ticketMapper.updateTicket(ticket);
    }

    @Override
    public void updateTicket4PI(Ticket ticket) {
        ticketMapper.updateTicket4PI(ticket);
    }

    @Override
    public Member getMemberByPiid(String processInstanceId) {
        return ticketMapper.getMemberByPiid(processInstanceId);
    }

    @Override
    public void updateStatus(Member member) {
        ticketMapper.updateStatus(member);
    }
}
