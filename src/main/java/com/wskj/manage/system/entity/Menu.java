/**

 */
package com.wskj.manage.system.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.wskj.manage.common.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wskj.manage.common.persistence.DataEntity;

/**
 * 菜单Entity
 * @author ganjinhua
 * @version 2013-05-15
 */
public class Menu extends DataEntity<Menu> {

	private static final long serialVersionUID = 1L;

	private String parentId;	// 父级菜单id
	private String parentIds; // 所有父级编号
    @NotNull
    @Size(max = 20)
	private String name; 	// 名称
    @Size(max = 200)
	private String href; 	// 链接
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
    @Size(max = 200)
	private String permission; // 权限标识
    @NotNull
    @Digits(integer = 1,fraction = 0)
	private Integer type; //层级
	
	private String userId;

	private List<Menu> children;
	
	public Menu(){
		super();
		this.sort = 30;
		this.isShow = "1";
	}
	
	public Menu(String id){
		super(id);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<Menu> getChildren() {
		return children != null ? children : new ArrayList<>(5);
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public String getParentId() {
		return StringUtils.isNotEmpty(parentId) ? parentId : getRootId();
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@JsonIgnore
	public static String getRootId(){
		return "1";
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return name;
	}
}