package com.ruoyi.activiti.mapper;

import java.util.List;
import java.util.Map;

public interface ActHistoryMapper {

    List<Map<String, Object>> selectMyTasksCompleted(String userId);

    List<Map<String, Object>> selectMyProcessStarted(String userId);


    Map<String, Object> selectEndEventByTaskId(String taskId);

    int deleteHiEndEvent(String taskId);

    Map<String, Object> selectHiTaskByTaskId(String taskId);

    List<Map<String, Object>> selectHiTaskByTaskKey(String taskKey);

    List<Map<String, Object>> selectHiVariablesByProInsId(String proInsId);

    Map<String, Object> selectIdentitylinkByTaskId(String taskId);

}
