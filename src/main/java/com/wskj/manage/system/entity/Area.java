/**

 */
package com.wskj.manage.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.wskj.manage.common.utils.Collections3;
import com.wskj.manage.common.utils.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.wskj.manage.common.persistence.TreeEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域Entity
 * @author ganjinhua
 * @version 2013-05-15
 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String parentId;	// 父级编号
//	private String parentIds; // 所有父级编号
	@Size(max = 10)
	private String code; 	// 区域编码
//	private String name; 	// 区域名称
//	private Integer sort;		// 排序
	@NotEmpty
	@Min(1)
	@Max(4)
	private Integer type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）

	private List<Area> children;
	
	public Area(){
		super();
		this.sort = 30;
	}

	public Area(String id){
		super(id);
	}

	public List<Area> getChildren() {
		if (Collections3.isEmpty(children)) {
			children = Lists.newArrayList();
		}
		return children;
	}

	public void setChildren(List<Area> children) {
		this.children = children;
	}

	@Override
	public String getParentId() {
		return StringUtils.isNotEmpty(parentId) ? parentId : super.getParentId();
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public Area getParent() {
		return parent;
	}

	@Override
	public void setParent(Area parent) {
		this.parent = parent;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@NotEmpty
	@Size(max = 100)
	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name;
	}
}