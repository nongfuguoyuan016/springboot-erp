/**

 */
package com.wskj.manage.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wskj.manage.common.service.TreeService;
import com.wskj.manage.system.dao.AreaDao;
import com.wskj.manage.system.entity.Area;
import com.wskj.manage.system.utils.UserUtils;

/**
 * 区域Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Override
	@Transactional(readOnly = false)
	public int save(Area area) {
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		return super.save(area);
	}
	
	@Override
	@Transactional(readOnly = false)
	public int delete(Area area) {
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		return super.delete(area);
	}
	
}
