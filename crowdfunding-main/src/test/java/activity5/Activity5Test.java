package activity5;

import com.shl.crowdfunding.listener.activity.NoListener;
import com.shl.crowdfunding.listener.activity.YesListener;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;

public class Activity5Test {
    ApplicationContext ioc = new ClassPathXmlApplicationContext("classpath:spring/spring-*.xml");
    ProcessEngine processEngine = (ProcessEngine) ioc.getBean("processEngine");

    //使用监听器启动流程实例
    @Test
    public void test09(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        HashMap stringHashMap = new HashMap<String,String>();
        stringHashMap.put("YesListener",new YesListener());
        stringHashMap.put("NoListener",new NoListener());
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(), stringHashMap);
        System.out.println(processInstance);
    }

    @Test
    public void test092(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> zhangsan = taskQuery.taskAssignee("zhangsan").list();
        for (Task task : zhangsan) {
            taskService.setVariable(task.getId(),"flag",true);
            taskService.complete(task.getId());
        }
    }

    //排他网管关
    //给days设置值为4,启动流程实例
    @Test
    public void test081(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        HashMap stringHashMap = new HashMap<String,String>();
        stringHashMap.put("days","2");
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(), stringHashMap);
        System.out.println(processInstance);
    }
    //执行下一步流程
    @Test
    public void test082(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> zhangsan = taskQuery.taskAssignee("zhangsan").list();
        for (Task task : zhangsan) {
            taskService.complete(task.getId());
        }
    }

    //使用流程变量启动流程实例
    @Test
    public void test07() {
        HashMap stringHashMap = new HashMap<String,String>();
        stringHashMap.put("tl","zhangsan");
        stringHashMap.put("pm","lisi");
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId(),stringHashMap);
        System.out.println("processInstance = " + processInstance);
    }

    //给任务指定委托组,由指定的组指派委托人
    @Test
    public void test06(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> tl = taskQuery.taskCandidateGroup("tl").list();
        long count = taskQuery.taskAssignee("zhangsan").count();
        System.out.println("zhangsan任务数量"+count);
        for (Task task : tl) {
            System.out.println(task.getName());
            taskService.claim(task.getId(),"zhangsan");
        }
        taskQuery = taskService.createTaskQuery();
        count = taskQuery.taskAssignee("zhangsan").count();
        System.out.println("zhangsan任务数量"+count);
    }

    //查询任务,完成任务
    @Test
    public void test05(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> list1 = taskQuery.taskAssignee("zhangsan").list();
        for (Task task : list1) {
            System.out.println("Id = " + task.getId());
            System.out.println("task = " + task.getName());
            taskService.complete(task.getId());
        }
        List<Task> list2 = taskQuery.taskAssignee("lisi").list();
        for (Task task : list2) {
            System.out.println("Id = " + task.getId());
            System.out.println("task = " + task.getName());
            taskService.complete(task.getId());
        }
    }

    //启动流程实例
    @Test
    public void test04(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        System.out.println("processInstance = " + processInstance);
    }
    //流程的查询的相关方法
    @Test
    public void test03() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery1 = repositoryService.createProcessDefinitionQuery();
       //根据processDefinitionQuery查询所有prodef对象
        List<ProcessDefinition> lists = processDefinitionQuery1.list();
        for (ProcessDefinition list : lists) {
            System.out.println("Id = " + list.getId());
            System.out.println("Key = " + list.getKey());
            System.out.println("Name = " + list.getName());
            System.out.println("Version = " + list.getVersion());
            System.out.println("---------------------------------");
        }
        //根据processDefinitionQuery查询所有prodef的数量
        long count = processDefinitionQuery1.count();
        System.out.println("数量 = " + count);
        //根据processDefinitionQuery查询最后一次生成的prodef对象
        ProcessDefinition processDefinition1 = processDefinitionQuery1.latestVersion().singleResult();
        System.out.println("Id = " + processDefinition1.getId());
        System.out.println("Key = " + processDefinition1.getKey());
        System.out.println("Name = " + processDefinition1.getName());
        System.out.println("Version = " + processDefinition1.getVersion());
        System.out.println("+++++++++++++++++++++++++++++");
        //根据processDefinitionQuery排序,分页(只能对不同的流程图分页)查询
        ProcessDefinitionQuery processDefinitionQuery2 = processDefinitionQuery1.orderByProcessDefinitionVersion().asc();
        List<ProcessDefinition> processDefinitions = processDefinitionQuery2.listPage(0, 2);
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println("Id = " + processDefinition.getId());
            System.out.println("Key = " + processDefinition.getKey());
            System.out.println("Name = " + processDefinition.getName());
            System.out.println("Version = " + processDefinition.getVersion());
            System.out.println("============================================");
        }
    }

    //将流程图部署
    @Test
    public void test02() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess7.bpmn").deploy();
        System.out.println(deploy);
    }

    //创建23张表
    @Test
    public void test01() {
        System.out.println(processEngine);
    }
}
