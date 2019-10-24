/**

 */
package com.wskj.manage.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wskj.manage.common.service.TreeService;
import com.wskj.manage.system.dao.OfficeDao;
import com.wskj.manage.system.entity.Office;
import com.wskj.manage.system.utils.UserUtils;

/**
 * 机构Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		office.setParentIds(office.getParentIds()+"%");
		return dao.findByParentIdsLike(office);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int save(Office office) {
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		return super.save(office);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int delete(Office office) {
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		return super.delete(office);
	}
	
}
