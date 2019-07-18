package com.ruoyi.activiti.service;

import com.ruoyi.activiti.mapper.ActHistoryMapper;
import com.ruoyi.activiti.mapper.ActRunMapper;
import com.ruoyi.activiti.utils.RollbackTaskCmd;
import com.ruoyi.activiti.utils.exceptions.TaskNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
public class ActRunServiceImpl implements ActRunService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private ActHistoryMapper actHistoryMapper;

    @Resource
    private ActRunMapper actRunMapper;

    @Override
    public void run(String key, Map<String, Object> variables) {
        //if(null==variables) runtimeService.startProcessInstanceByKey(key);
        runtimeService.startProcessInstanceByKey(key, variables);
    }

    @Override
    public boolean rollBack(String taskId) {

        Object rejectElemKey = ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(new RollbackTaskCmd(taskId));
        log.warn("rejectElemKey:"+rejectElemKey);
        rejected(taskId,rejectElemKey.toString(),"退回到上一步!");
        return true;
    }

    @Override
    @Transactional
    public boolean rejected(String taskId, String rejectElemKey, String dealReason) {
        int res = 0;
        //1.历史表
        //判断是否结束
        Map<String, Object> endEvent = actHistoryMapper.selectEndEventByTaskId(taskId);
        log.info("查询hi_taskinst结束事件的结果，{}", endEvent);

        Map<String, Object> hiTask = actHistoryMapper.selectHiTaskByTaskId(taskId);
        String ruExcutionId = (String) hiTask.get("EXECUTION_ID_");
        String _processId = (String) hiTask.get("PROC_INST_ID_");

        //2.运行表
        //判断是驳回到原点：运行表ru_task，act_ru_identitylink，ru_variable，ru_execution清除节点信息
        if ("S00000".equals(rejectElemKey)) {
            if (null == endEvent || endEvent.isEmpty()) {
                //删variables
                res = actRunMapper.deleteRuVariable(taskId);
                log.info("删ru_variables结束，{}", res);

                //删除当前的任务
                //不能删除当前正在执行的任务，所以要先清除掉关联
                TaskEntity currentTaskEntity = (TaskEntity) taskService.createTaskQuery()
                        .processInstanceId(_processId).singleResult();
                currentTaskEntity.setExecutionId(null);
                taskService.saveTask(currentTaskEntity);
                taskService.deleteTask(currentTaskEntity.getId(), true);
                log.info("删ru_task结束，{}", currentTaskEntity);

                //删execution
                res = actRunMapper.deleteRuExecution(taskId);
                log.info("删ru_execution结束，{}", res);

                //删identitylink
                res = actRunMapper.deleteRuIdentity(taskId);
                log.info("删ru_identitylink结束，{}", res);

            } else {
                //结束了，act_hi_actinst删掉结束event
                res = actHistoryMapper.deleteHiEndEvent(taskId);
                log.info("删掉hi_actinst中endEvent结束，{}", res);
            }
        } else {
            //判断是驳回到节点：运行表ru_task，ru_execution更改节点信息
            jumpEndActivity(ruExcutionId, rejectElemKey, dealReason);

        }
        return true;
    }

    /**
     * 第一种自由跳转的方式:
     * 这种方式是通过 重写命令  ，推荐这种方式进行实现(这种方式的跳转，最后可以通过taskDeleteReason 来进行查询 )
     */

    public void jumpEndActivity(String executionId, String targetActId, String reason) {
/*        //当前节点
        ActivityImpl currentActivityImpl = qureyCurrentTask("ziyouliu:1:4");
        //目标节点
        ActivityImpl targetActivity = queryTargetActivity("shenchajigou");*/

        ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(new Command<Object>() {
            public Object execute(CommandContext commandContext) {
                ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(executionId);
                execution.destroyScope(reason);  //
                ProcessDefinitionImpl processDefinition = execution.getProcessDefinition();
                ActivityImpl findActivity = processDefinition.findActivity(targetActId);
                execution.executeActivity(findActivity);
                return execution;
            }

        });
        log.info("自由跳转至节点：{}-----完成", targetActId);
    }
}