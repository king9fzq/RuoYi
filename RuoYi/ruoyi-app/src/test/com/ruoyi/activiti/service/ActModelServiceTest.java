package com.ruoyi.activiti.service;

import com.ruoyi.activiti.mapper.ActHistoryMapper;
import com.ruoyi.app.RuoYiAPPApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiAPPApplication.class)
public class ActModelServiceTest {

    @Resource
    TaskService taskService;

    @Resource
    RuntimeService runtimeService;

    @Resource
    ActProcessService actProcessService;

    @Resource
    HistoryService historyService;

    @Resource
    ActRunService actRunService;

    @Resource
    private ActHistoryMapper actHistoryMapper;

    @Test
    public void a(){

        Map<String,Object> hiTask=actHistoryMapper.selectHiTaskByTaskId("600705353176317952");
        System.out.println(hiTask);
    }

    @Test
    public void start(){

        ProcessInstance ro =  runtimeService.startProcessInstanceByKey("roll");
        System.out.println("流程实例id："+ro.getProcessInstanceId()+"流程部署id："+ro.getDeploymentId());
    }

    @Test
    public void findOwn(){

        System.out.println(taskService.createTaskQuery()
                .list());

    }

    @Test
    public void complete(){
        String taskId = "601081809572200448";

        Map<String,Object> map = new HashMap<>();
        map.put("day",5);
        taskService.complete(taskId,map);
    }

    @Test
    public void delAllDataBydeploymentId(){

        String deploymentId = "12508";
        System.out.println("结果:"+actProcessService.deleteAllDataByDeploymentId(deploymentId));
    }

    @Test
    public void rejected(){

        String taskId = "600693928190017537";
        String rejectElemKey = "qj";
        String dealReason = "排他网关的回退！";
        System.out.println("回退结果:"+actRunService.rejected(taskId, rejectElemKey, dealReason));
    }

    @Test
    public void rollBack(){
        String taskId = "601081267466797057";
        actRunService.rollBack(taskId);
    }
}