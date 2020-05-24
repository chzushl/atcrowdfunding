package com.shl.crowdfunding.potal.listener;

import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.potal.service.MemberService;
import com.shl.crowdfunding.potal.service.TicketService;
import com.shl.crowdfunding.util.ApplicationUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

//实名认证审核拒绝执行的操作
public class RefuseListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        Integer memberid = (Integer) delegateExecution.getVariable("memberid");
        ApplicationContext applicationContext = ApplicationUtils.applicationContext;

        //获取ticketService和memberService对象
        TicketService ticketService = applicationContext.getBean(TicketService.class);
        MemberService memberService = applicationContext.getBean(MemberService.class);
        //更新数据
        Member member = memberService.getMemberById(memberid);
        member.setAuthstatus("0");
        memberService.updateAuthstatus(member);
        ticketService.updateStatus(member);
    }
}
