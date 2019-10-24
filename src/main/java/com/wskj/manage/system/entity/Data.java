/**

 */
package com.wskj.manage.system.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.wskj.manage.common.persistence.TreeEntity;

/**
 * 数据集Entity
 * @author ganjihua
 * @version 2015-12-7
 */
public class Data  extends TreeEntity<Data> {

		private static final long serialVersionUID = 1L;
		private String type; 	// 集合类型（1：总数据；2：分区；3：视频；4 相机）
		private String grade; 	// 数据等级（1：一级；2：二级；3：三级；4：四级）
		private String address; // 设备IP地址
		private String intersectionCode; // 路口编号
		private String videoName; 	// 硬盘录像机品牌
		private String videoAddress; 	// 录像机IP地址
		private String enterclose; 	// 通道号
		private String useable;//是否可用
		private User primaryPerson;//主负责人
		private User deputyPerson;//副负责人
		private List<String> childDeptList;//快速添加子部门
		
		public Data(){
			super();
//			this.sort = 30;
			this.type = "2";
		}

		public Data(String id){
			super(id);
		}
		
		public List<String> getChildDeptList() {
			return childDeptList;
		}

		public void setChildDeptList(List<String> childDeptList) {
			this.childDeptList = childDeptList;
		}

		public String getUseable() {
			return useable;
		}

		public void setUseable(String useable) {
			this.useable = useable;
		}

		public User getPrimaryPerson() {
			return primaryPerson;
		}

		public void setPrimaryPerson(User primaryPerson) {
			this.primaryPerson = primaryPerson;
		}

		public User getDeputyPerson() {
			return deputyPerson;
		}

		public void setDeputyPerson(User deputyPerson) {
			this.deputyPerson = deputyPerson;
		}

//		@JsonBackReference
//		@NotNull
		@Override
		public Data getParent() {
			return parent;
		}

		@Override
		public void setParent(Data parent) {
			this.parent = parent;
		}
		
		@Length(min=1, max=1)
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Length(min=1, max=1)
		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		@Length(min=0, max=255)
		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
		@Length(min=0, max=100)
     	public String getIntersectionCode() {
			return intersectionCode;
		}

		public void setIntersectionCode(String intersectionCode) {
			this.intersectionCode = intersectionCode;
		}
		@Length(min=0, max=100)
		public String getVideoName() {
			return videoName;
		}

		public void setVideoName(String videoName) {
			this.videoName = videoName;
		}
		@Length(min=0, max=100)
		public String getVideoAddress() {
			return videoAddress;
		}

		public void setVideoAddress(String videoAddress) {
			this.videoAddress = videoAddress;
		}
		@Length(min=0, max=3)
		public String getEnterclose() {
			return enterclose;
		}

		public void setEnterclose(String enterclose) {
			this.enterclose = enterclose;
		}

		@Override
		public String toString() {
			return name;
		}
	}