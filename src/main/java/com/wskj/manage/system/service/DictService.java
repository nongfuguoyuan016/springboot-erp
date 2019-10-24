/**

 */
package com.wskj.manage.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wskj.manage.common.service.CrudService;
import com.wskj.manage.common.utils.CacheUtils;
import com.wskj.manage.system.dao.DictDao;
import com.wskj.manage.system.entity.Dict;
import com.wskj.manage.system.utils.DictUtils;

/**
 * 字典Service
 * @author ganjinhua
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DictService extends CrudService<DictDao, Dict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	@Override
	@Transactional(readOnly = false)
	public int save(Dict dict) {
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
		return super.save(dict);
	}

	@Override
	@Transactional(readOnly = false)
	public int delete(Dict dict) {
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
		return super.delete(dict);
	}

}
