package com.ruoyi.activiti.utils.exceptions;

import com.ruoyi.common.exception.MyException;

public class TaskNotExistException extends MyException {

    public TaskNotExistException() {
        super("任务不存在!");
    }
}
