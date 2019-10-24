/**

 */
package com.wskj.manage.system.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wskj.manage.common.persistence.TreeDao;
import com.wskj.manage.system.entity.Office;

/**
 * 机构DAO接口
 * @author ganjinhua
 * @version 2014-05-16
 */
@Mapper
public interface OfficeDao extends TreeDao<Office> {
	
}
