package com.wskj.manage.system.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * @author: xuchang
 * @date: 2019/10/8
 */
public class DictAddress implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer code;

    private Integer type;

    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
