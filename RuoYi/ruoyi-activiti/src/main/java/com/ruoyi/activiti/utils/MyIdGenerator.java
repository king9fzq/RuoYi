package com.ruoyi.activiti.utils;

import com.ruoyi.common.utils.SnowflakeIdWorker;
import org.activiti.engine.impl.cfg.IdGenerator;

/**
 * @Auther: Ace Lee
 * @Date: 2019/3/11 16:05
 */
public class MyIdGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return String.valueOf(SnowflakeIdWorker.nextId());
    }
}