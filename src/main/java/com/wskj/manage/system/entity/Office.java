/**

 */
package com.wskj.manage.system.entity;

import javax.validation.constraints.*;

import com.google.common.collect.Lists;
import com.wskj.manage.common.utils.Collections3;
import com.wskj.manage.common.utils.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.wskj.manage.common.persistence.TreeEntity;

import java.util.List;

/**
 * 机构Entity
 */
public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
	@Size(min = 1,max = 100)
	private String code; 	// 机构编码
	@NotNull
	@Min(1)
	@Max(3)
	private Integer type; 	// 机构类型（1：公司；2：一级部门；3：二级部门）
	@Size(min = 1, max = 255)
	private String address; // 联系地址
	@Size(min = 6, max = 10)
	private String zipCode; // 邮政编码
	@Size(max = 100)
	private String master; 	// 负责人
	@Size(max = 200)
	private String phone; 	// 电话
	@Size(max = 200)
	private String fax; 	// 传真
	@Email
	private String email; 	// 邮箱

	private List<Office> children;

	private String parentId;
	
	public Office(){
		super();
		this.type = 2;
	}

	public Office(String id){
		super(id);
	}

	public List<Office> getChildren() {
		if (Collections3.isEmpty(children)) {
			children = Lists.newArrayList();
		}
		return children;
	}

	public void setChildren(List<Office> children) {
		this.children = children;
	}

//	@JsonBackReference
//	@NotNull
	@Override
	public Office getParent() {
		return parent;
	}

	@Override
	public void setParent(Office parent) {
		this.parent = parent;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getParentId() {
		return StringUtils.isNotEmpty(parentId) ? parentId : super.getParentId();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=3)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

//	public String getParentId() {
//		return parent != null && parent.getId() != null ? parent.getId() : "0";
//	}
	
	@Override
	public String toString() {
		return name;
	}
}