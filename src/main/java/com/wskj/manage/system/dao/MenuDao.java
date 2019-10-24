/**

 */
package com.wskj.manage.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wskj.manage.common.persistence.CrudDao;
import com.wskj.manage.system.entity.Menu;

/**
 * 菜单DAO接口
 * @author ganjinhua
 * @version 2014-05-16
 */
@Mapper
public interface MenuDao extends CrudDao<Menu> {

	public List<Menu> findByParentIdsLike(Menu menu);

	public List<Menu> findByUserId(Menu menu);
	
	public int updateParentIds(Menu menu);
	
	public int updateSort(Menu menu);
	
}
