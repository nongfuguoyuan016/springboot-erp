package com.wskj.manage.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.FastJsonUtils;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Office;
import com.wskj.manage.system.entity.Role;
import com.wskj.manage.system.entity.User;
import com.wskj.manage.system.service.SystemService;
import com.wskj.manage.system.utils.UserUtils;

/**
 * 用户Controller
 * @author ganjinhua
 * @version 2013-8-29
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}

	@RequiresPermissions("sys:user:view")
	@GetMapping("/list/data")
	public JSONResult listData(User user, int pageNo, int pageSize) {
		Map<String,Object> map = Maps.newHashMap();
		Page<User> page = systemService.findUser(pageNo, pageSize, user);
		map.put("total",page.getTotal());
		map.put("rows", FastJsonUtils.toJsonArrayIncludeProperties(page.getResult(),"id","office","company","name","loginName","phone","mobile"));
		return JSONResult.ok(map);
	}

	@RequiresPermissions("sys:user:edit")
	@PostMapping(value = "save")
	public JSONResult save(@Valid User user, HttpServletRequest request, BindingResult result) {
		if (result.hasErrors()) {
			return JSONResult.fail("包含非法信息");
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		if (!checkLoginName(user.getOldLoginName(), user.getLoginName())){
			return JSONResult.fail("保存用户'" + user.getLoginName() + "'失败，登录名已存在");
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// 保存用户信息
		systemService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
			//UserUtils.getCacheMap().clear();
		}
		return JSONResult.ok( "保存用户'" + user.getLoginName() + "'成功");

	}
	
	@RequiresPermissions("sys:user:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(User user, RedirectAttributes redirectAttributes) {
		if (UserUtils.getUser().getId().equals(user.getId())){
			return JSONResult.fail( "删除用户失败, 不允许删除当前用户");
		}else if (User.isAdmin(user.getId())){
			return JSONResult.fail( "删除用户失败, 不允许删除超级管理员用户");
		}
		int result = systemService.deleteUser(user);
		return 0 != result ? JSONResult.ok("删除用户成功") : JSONResult.fail("删除失败,请稍后再试");
	}

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	private boolean checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return true;
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return true;
		}
		return false;
	}

	/**
	 * 用户信息保存
	 * @param user
	 * @return
	 */
	@RequiresPermissions("user")
	@PostMapping(value = "info")
	public JSONResult info(User user) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			return JSONResult.ok("保存用户信息成功");
		}
		return JSONResult.fail("保存用户信息失败,请稍后重试");
	}


	/**
	 * 返回用户信息
	 * @return
	 */
	@RequiresPermissions("user")
	@GetMapping(value = "infoData")
	public JSONResult infoData() {
		return JSONResult.ok(UserUtils.getUser());
	}

	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequiresPermissions("user")
	@PostMapping(value = "modifyPwd")
	public JSONResult modifyPwd(String oldPassword, String newPassword) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				return JSONResult.ok("修改密码成功");
			}else{
				return JSONResult.fail("修改密码失败，旧密码错误");
			}
		}
		return JSONResult.fail("修改密码失败, 密码为空");
	}
	
	@RequiresPermissions("user")
	@GetMapping(value = "treeData")
	public JSONResult treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return JSONResult.ok(mapList);
	}

}
