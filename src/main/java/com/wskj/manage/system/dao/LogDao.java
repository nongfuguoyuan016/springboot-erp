/**

 */
package com.wskj.manage.system.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wskj.manage.common.persistence.CrudDao;
import com.wskj.manage.system.entity.Log;

/**
 * 日志DAO接口
 * @author ganjinhua
 * @version 2014-05-16
 */
@Mapper
public interface LogDao extends CrudDao<Log> {

}
