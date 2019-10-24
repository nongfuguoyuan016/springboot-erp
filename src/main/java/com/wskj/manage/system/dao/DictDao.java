/**

 */
package com.wskj.manage.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wskj.manage.common.persistence.CrudDao;
import com.wskj.manage.system.entity.Dict;
import com.wskj.manage.system.entity.DictAddress;

/**
 * 字典DAO接口
 * @author ganjinhua
 * @version 2014-05-16
 */
@Mapper
public interface DictDao extends CrudDao<Dict> {

	List<String> findTypeList(Dict dict);

	List<DictAddress> getAllDictAddress();

	List<Dict> findAllListByParentId(Dict dict);
}
