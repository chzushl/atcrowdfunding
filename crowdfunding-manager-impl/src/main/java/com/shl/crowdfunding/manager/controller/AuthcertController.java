package com.shl.crowdfunding.manager.controller;

import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.potal.service.MemberService;
import com.shl.crowdfunding.potal.service.TicketService;
import com.shl.crowdfunding.util.AjaxResult;
import com.shl.crowdfunding.util.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authcert")
public class AuthcertController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index() {
        return "authcert/index";
    }

    @RequestMapping("/show")
    public String show(Integer memberid, Map map) {

        Member member = memberService.getMemberById(memberid);

        List<Map<String, Object>> list = memberService.queryCertByMemberid(memberid);
        map.put("member", member);
        map.put("certimgs", list);
        return "authcert/show";

    }

    //审核通过
    @ResponseBody
    @RequestMapping("/pass")
    public Object pass(String taskid, Integer memberid) {
        AjaxResult result = new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", true);
            taskService.setVariable(taskid, "memberid", memberid);
            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    //审核拒绝
    @ResponseBody
    @RequestMapping("/refuse")
    public Object refuse(String taskid, Integer memberid) {
        AjaxResult result = new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", false);
            taskService.setVariable(taskid, "memberid", memberid);
            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    //后台审核的数据显示
    @RequestMapping("/pageQuery")
    @ResponseBody
    public Object pageQuery(@RequestParam(value = "pageno", required = false, defaultValue = "1") Integer pageno,
                            @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            Page page = new Page(pageno, pagesize);
            //获取前台的流程数据
            TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey("auth").taskCandidateGroup("backuser");
            List<Task> tasks = taskQuery.listPage(page.getStartPage(), pagesize);

            //创建map集合将数据封装到map中
            List<Map<String, Object>> datas = new ArrayList<>();
            for (Task task : tasks) {
                Map<String, Object> map = new HashMap<>();
                map.put("taskid", task.getId());
                map.put("taskName", task.getName());

                //获取流程定义的属性
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
                map.put("procDefName", processDefinition.getName());
                map.put("procDefVersion", processDefinition.getVersion());

                //获取申请人的姓名
                Member member = ticketService.getMemberByPiid(task.getProcessInstanceId());
                map.put("member", member);
                datas.add(map);
            }
            page.setDatas(datas);
            Long count = taskQuery.count();
            page.setTotalsize(count.intValue());
            ajaxResult.setPage(page);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("审核数据查询失败");
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }
}
