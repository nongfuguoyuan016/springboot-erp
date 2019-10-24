/**

 */
package com.wskj.manage.system.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wskj.manage.common.persistence.CrudDao;
import com.wskj.manage.system.entity.Role;

/**
 * 角色DAO接口
 * @author ganjinhua
 * @version 2013-12-05
 */
@Mapper
public interface RoleDao extends CrudDao<Role> {

	Role getByName(Role role);
	
	Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	int deleteRoleMenu(Role role);

	int insertRoleMenu(Role role);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	int deleteRoleOffice(Role role);

	int insertRoleOffice(Role role);

}
