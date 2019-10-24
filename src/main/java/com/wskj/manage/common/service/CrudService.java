/**

 */
package com.wskj.manage.common.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.wskj.manage.common.persistence.CrudDao;
import com.wskj.manage.common.persistence.DataEntity;

/**
 * Service基类
 * @author luochaoqun
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> extends BaseService {
	
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	public List<T> findAllList(){return dao.findAllList() ;}

	/**
	 * 查询分页数据
	 * @param pageNo
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(int pageNo, int pageSize, T entity) {
		return PageHelper.startPage(pageNo,pageSize).doSelectPage(()->dao.findList(entity));
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int save(T entity) {
		if (entity.getIsNewRecord()){
			entity.preInsert();
			return dao.insert(entity);
		}else{
			entity.preUpdate();
			return dao.update(entity);
		}
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int delete(T entity) {
		return dao.delete(entity);
	}

	public int count(T entity){
		return dao.count(entity);
	}

}
