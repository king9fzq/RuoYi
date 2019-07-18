package com.ruoyi.activiti.service;

import java.util.Map;

public interface ActRunService {

    void run(String key, Map<String,Object> variables);

    boolean rollBack(String taskId);

    boolean rejected(String taskId, String rejectElemKey, String dealReason);

}
