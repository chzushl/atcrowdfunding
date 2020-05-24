package com.shl.crowdfunding.potal.controller;

import com.shl.crowdfunding.bean.Cert;
import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.bean.MemberCert;
import com.shl.crowdfunding.bean.Ticket;
import com.shl.crowdfunding.manager.service.CertService;
import com.shl.crowdfunding.potal.listener.PassListener;
import com.shl.crowdfunding.potal.listener.RefuseListener;
import com.shl.crowdfunding.potal.service.MemberService;
import com.shl.crowdfunding.potal.service.TicketService;
import com.shl.crowdfunding.util.AjaxResult;
import com.shl.crowdfunding.util.Const;
import com.shl.crowdfunding.vo.Data;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    TicketService ticketService;

    @Autowired
    CertService certService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @RequestMapping("/apply")
    public String apply(HttpSession session) {
        Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
        Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
        if(ticket==null){
            ticket = new Ticket();
            ticket.setMemberid(loginMember.getId());
            ticket.setStatus("0");
            ticket.setPstep("apply");
            ticketService.saveTicket(ticket);
        }else {
            if("accttype".equals(ticket.getPstep())){
                return "redirect:/member/basicinfo.htm";
            }else if("basicinfo".equals(ticket.getPstep())){
                return "redirect:/member/uploadCert.htm";
            }else if("uploadcert".equals(ticket.getPstep())){
                return "redirect:/member/checkemail.htm";
            }else if("checkemail".equals(ticket.getPstep())){
                return "redirect:/member/checkauthcode.htm";
            }
        }
        return "redirect:/member/accttype.htm";
    }

    @RequestMapping("/accttype")
    public String accttype() {
        return "member/accttype";
    }
    @RequestMapping("/basicinfo")
    public String basicinfo() {
        return "member/basicinfo";
    }
    @RequestMapping("/checkemail")
    public String checkemail() {
        return "member/checkemail";
    }
    @RequestMapping("/checkauthcode")
    public String checkauthcode() {
        return "member/checkauthcode";
    }
    @RequestMapping("/uploadCert")
    public String uploadCert(HttpSession session, Map<String,Object> map) {
        Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
        List<Cert> certs = certService.queryCertByAccttype(loginMember.getAccttype());
        map.put("certs",certs);
        return "member/uploadCert";
    }
    //验证验证码
    @ResponseBody
    @RequestMapping("/finishApply")
    public Object finishApply(HttpSession session,String authcode) {
        AjaxResult result = new AjaxResult();
        try {
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            //获取当前用户的验证码和填写的验证码比对，如果不一样就改变数据库中的验证码
           if(authcode.equals(ticket.getAuthcode())){
               Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(loginMember.getLoginacct()).singleResult();
               taskService.complete(task.getId());

               //设置用户状态
               loginMember.setAuthstatus("1");
               memberService.updateAuthstatus(loginMember);

               //记录流程步骤
               ticket.setPstep("finishapply");
               ticketService.updateTicket(ticket);
               result.setSuccess(true);
           }else{
               result.setSuccess(false);
               result.setMessage("验证码不正确,请重新输入!");
           }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
    //开启流程实例
    @ResponseBody
    @RequestMapping("/startProcess")
    public Object startProcess(HttpSession session,String email) {
        AjaxResult result = new AjaxResult();
        try {
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            //获取当前用户的验证码和填写的验证码比对，如果不一样就改变数据库中的验证码
            if(!email.equals(loginMember.getEmail())){
                loginMember.setEmail(email);
                memberService.updateEmail(loginMember);
            }
            //准备四位随机验证码
            StringBuilder authcode = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                authcode.append(new Random().nextInt(10));
            }

            //准备启动流程实例的变量
            Map<String, Object> varMap = new HashMap<String, Object>();
            varMap.put("toEmail", email);
            varMap.put("authcode", authcode.toString());
            varMap.put("loginacct", loginMember.getLoginacct());
            varMap.put("passListener", new PassListener());
            varMap.put("refuseListener", new RefuseListener());

            //启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("auth", varMap);
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("checkemail");
            ticket.setPiid(processInstance.getId());
            ticket.setAuthcode(authcode.toString());

            ticketService.updateTicket4PI(ticket);
            ticketService.updateTicket(ticket);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/doUploadCert")
    public Object doUploadCert(HttpSession session, Data data) {
        AjaxResult result = new AjaxResult();
        try {
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            List<MemberCert> certimgs = data.getCertimgs();
            String realName = session.getServletContext().getRealPath("/pics");
            for (MemberCert memberCert : certimgs) {
                MultipartFile fileImg = memberCert.getFileImg();
                String tmpName = UUID.randomUUID().toString()+"_"+fileImg.getOriginalFilename();
                String filename = realName+"/cert"+"/"+tmpName;
                fileImg.transferTo(new File(filename));

                memberCert.setIconpath(tmpName);
                memberCert.setMemberid(loginMember.getId());
            }
            certService.saveMemberCert(certimgs);

            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("uploadcert");
            ticketService.updateTicket(ticket);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("/updateAcctType")
    public Object updateAcctType(HttpSession session, String accttype) {
        AjaxResult result = new AjaxResult();
        try {
            // 获取登录会员信息
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginMember.setAccttype(accttype);
            // 更新账户类型
            memberService.updateAcctType(loginMember);
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("accttype");
            ticketService.updateTicket(ticket);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("/updateBasicinfo")
    public Object updateBasicinfo(HttpSession session, Member member) {
        AjaxResult result = new AjaxResult();
        try {
            Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginMember.setCardnum(member.getCardnum());
            loginMember.setRealname(member.getRealname());
            loginMember.setTel(member.getTel());
            memberService.updateBasicinfo(loginMember);
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("basicinfo");
            ticketService.updateTicket(ticket);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}
