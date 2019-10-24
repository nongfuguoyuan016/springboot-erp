/**

 */
package com.wskj.manage.system.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import com.wskj.manage.common.config.Global;
import com.wskj.manage.common.service.BaseService;
import com.wskj.manage.common.utils.CacheUtils;
import com.wskj.manage.common.utils.SpringContextHolder;
import com.wskj.manage.system.dao.*;
import com.wskj.manage.system.entity.*;
import com.wskj.manage.system.security.SystemRealm.Principal;

/**
 * 用户工具类
 * @author ganjinhua
 * @version 2013-12-05
 */
public class UserUtils {

//	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
//	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
//	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
//	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
//	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
//
//	private static DataDao dataDao = SpringContextHolder.getBean(DataDao.class);

	private static UserDao userDao = null;
	private static RoleDao roleDao = null;
	private static MenuDao menuDao = null;
	private static AreaDao areaDao = null;
	private static OfficeDao officeDao = null;

	private static DataDao dataDao = null;

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	public static final String CACHE_DATA_LIST = "dataList";
	public static final String CACHE_DATA_ALL_LIST = "dataAllList";

	private static UserDao userDao(){
		if (userDao == null) {
			userDao = SpringContextHolder.getBean(UserDao.class);
		}
		return userDao;
	}

	private static RoleDao roleDao(){
		if (roleDao == null) {
			roleDao = SpringContextHolder.getBean(RoleDao.class);
		}
		return roleDao;
	}
	private static MenuDao menuDao(){
		if (menuDao == null) {
			menuDao = SpringContextHolder.getBean(MenuDao.class);
		}
		return menuDao;
	}
	private static AreaDao areaDao(){
		if (areaDao == null) {
			areaDao = SpringContextHolder.getBean(AreaDao.class);
		}
		return areaDao;
	}

	private static OfficeDao officeDao(){
		if (officeDao == null) {
			officeDao = SpringContextHolder.getBean(OfficeDao.class);
		}
		return officeDao;
	}

	private static DataDao dataDao(){
		if (dataDao == null) {
			dataDao = SpringContextHolder.getBean(DataDao.class);
		}
		return dataDao;
	}

	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao().get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao().findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null){
			User userLogin=new User();
			userLogin.setLoginName(loginName);
			user = userDao().getByLoginName(userLogin);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao().findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		removeCache(CACHE_DATA_LIST);
		removeCache(CACHE_DATA_ALL_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		if (user.getOffice() != null && user.getOffice().getId() != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取用户登录名
	 */
	public static String getUserLoginName(){
		return getUser().getLoginName();
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			User user = getUser();
			if (user.isAdmin()){
				roleList = roleDao().findAllList(new Role());
			}else{
				Role role = new Role();
				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
				roleList = roleDao().findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao().findAllList(new Menu());
			}else{
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao().findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}

	/**
	 *  获取一级菜单
	 * @return
	 */
	public static List<Menu> getFirstMenuList(){
		List<Menu> menuList = getMenuList() ;
		return menuList.stream().filter(menu -> menu.getParentId().equals(Global.getConfig("parentMenuId"))&&"1".equals(menu.getIsShow())).sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList()) ;
	}

	/**
	 * 通过菜单id获取子菜单
	 * @param parentMenuId
	 * @return
	 */
	public static List<Menu> getSubMenuList(String parentMenuId){
		List<Menu> menuList = getMenuList() ;
		return menuList.stream().filter(menu -> menu.getParentId().equals(parentMenuId)&&"1".equals(menu.getIsShow())).sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList()) ;
	}

	/**
	 * 通过菜单id判断是否有子菜单
	 * @param parentMenuId
	 * @return
	 */
	public static boolean hasSubMenu(String parentMenuId){
		List<Menu> menuList = getMenuList() ;
		for (Menu menu : menuList) {
			if(menu.getParentId().equals(parentMenuId)&&"1".equals(menu.getIsShow())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao().findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao().findAllList(new Office());
			}else{
				Office office = new Office();
				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao().findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			officeList = officeDao().findAllList(new Office());
		}
		return officeList;
	}
	
	
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Data> getDataList(){
		@SuppressWarnings("unchecked")
		List<Data> dataList = (List<Data>)getCache(CACHE_DATA_LIST);
		if (dataList == null){
			User user = getUser();
			if (user.isAdmin()){
				dataList = dataDao().findAllList(new Data());
			}else{
				Data data = new Data();
				data.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				dataList = dataDao().findList(data);
			}
			putCache(CACHE_DATA_LIST, dataList);
		}
		return dataList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Data> getDataAllList(){
		@SuppressWarnings("unchecked")
		List<Data> dataList = (List<Data>)getCache(CACHE_DATA_ALL_LIST);
		if (dataList == null){
			dataList = dataDao().findAllList(new Data());
		}
		return dataList;
	}
	
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}
	
}
