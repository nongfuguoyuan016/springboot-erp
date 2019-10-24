/**

 */
package com.wskj.manage.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wskj.manage.common.service.TreeService;
import com.wskj.manage.system.dao.DataDao;
import com.wskj.manage.system.entity.Data;
import com.wskj.manage.system.utils.UserUtils;

/**
 * 数据集Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DataService extends TreeService<DataDao, Data> {

	public List<Data> findAll(){
		return UserUtils.getDataList();
	}

	public List<Data> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getDataAllList();
		}else{
			return UserUtils.getDataList();
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Data> findList(Data data){
		data.setParentIds(data.getParentIds()+"%");
		return dao.findByParentIdsLike(data);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int save(Data data) {
		UserUtils.removeCache(UserUtils.CACHE_DATA_LIST);
		return super.save(data);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int delete(Data data) {
		UserUtils.removeCache(UserUtils.CACHE_DATA_LIST);
		return super.delete(data);
	}
	
}
