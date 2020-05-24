package com.shl.crowdfunding.manager.controller;

import com.shl.crowdfunding.util.AjaxResult;
import com.shl.crowdfunding.util.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    RepositoryService repositoryService;

    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }

    @RequestMapping("/showimg")
    public String showimg(){
        return "process/showimg";
    }

    @ResponseBody
    @RequestMapping("/doShowimg")
    public void doShowimg(String id, HttpServletResponse response) throws IOException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(resourceAsStream,outputStream);
    }

    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(String id){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
            String deploymentId = processDefinition.getDeploymentId();
            repositoryService.deleteDeployment(deploymentId,true);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("流程删除失败");
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }


    @ResponseBody
    @RequestMapping("/deploy")
    public Object deploy(HttpServletRequest request){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            MultipartRequest multipartRequest = (MultipartRequest)request;
            MultipartFile processFile = multipartRequest.getFile("processFile");
            repositoryService.createDeployment().addInputStream(processFile.getOriginalFilename(),processFile.getInputStream()).deploy();
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("文件部署失败");
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value = "pageno",required = false,defaultValue = "1")Integer pageno,
    @RequestParam(value = "pagesize",required = false,defaultValue = "10")Integer pagesize){
        AjaxResult ajaxResult = new AjaxResult();
        try {
            Page page = new Page(pageno,pagesize);
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage(page.getStartPage(), pagesize);

            List<Map<String,Object>> myprocessDefinitions = new ArrayList<>();
            for (ProcessDefinition processDefinition : processDefinitions) {
                HashMap<String, Object> processDefinitionMap = new HashMap<>();
                processDefinitionMap.put("name",processDefinition.getName());
                processDefinitionMap.put("id",processDefinition.getId());
                processDefinitionMap.put("key",processDefinition.getKey());
                processDefinitionMap.put("version",processDefinition.getVersion());
                myprocessDefinitions.add(processDefinitionMap);
            }


            page.setDatas(myprocessDefinitions);
            Long count = processDefinitionQuery.count();
            page.setTotalsize(count.intValue());

            ajaxResult.setPage(page);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            ajaxResult.setMessage("查询流程定义失败");
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }
}
