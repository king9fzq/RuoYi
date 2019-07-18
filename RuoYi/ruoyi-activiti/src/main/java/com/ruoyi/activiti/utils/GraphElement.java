package com.ruoyi.activiti.utils;

import lombok.Data;

//节点和连线的父类
@Data
public class GraphElement {

    /**

     * 实例id，历史的id.

     */

    private String id;


    /**

     * 节点名称，bpmn图形中的id.

     */

    private String name;
}
