package com.wskj.manage.system.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wskj.manage.common.service.CrudService;
import com.wskj.manage.common.utils.DateUtils;
import com.wskj.manage.system.dao.LogDao;
import com.wskj.manage.system.entity.Log;

/**
 * 日志Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class LogService extends CrudService<LogDao, Log> {

	@Override
	public Page<Log> findPage(int pageNo,int pageSize, Log log) {
		
		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null){
			log.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (log.getEndDate() == null){
			log.setEndDate(DateUtils.addMonths(log.getBeginDate(), 1));
		}
		
		return super.findPage(pageNo,pageSize, log);
		
	}
	
}
