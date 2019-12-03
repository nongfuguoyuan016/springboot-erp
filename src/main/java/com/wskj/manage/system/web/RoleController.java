package com.wskj.manage.system.web;

import com.wskj.manage.common.config.Global;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Role;
import com.wskj.manage.system.service.SystemService;
import com.wskj.manage.system.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色Controller
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;

	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("sys:role:view")
	@GetMapping(value = "list/data")
	public JSONResult listData() {
		return JSONResult.ok(systemService.findAllRole());
	}
	
	@RequiresPermissions("sys:role:edit")
	@PostMapping(value = "save")
	public JSONResult save(@Validated Role role, BindingResult error) {
		if(!UserUtils.getUser().isAdmin()&&role.getSysData().equals(Global.YES)){
			return JSONResult.fail("越权操作！");
		}
		if (error.hasErrors()){
			return JSONResult.fail("包含非法信息");
		}
		if (!checkName(role.getOldName(), role.getName())){
			return JSONResult.fail("保存角色'" + role.getName() + "'失败, 角色名已存在");
		}
		if (!checkEnname(role.getOldEnname(), role.getEnname())){
			return JSONResult.fail("保存角色'" + role.getName() + "'失败, 英文名已存在");
		}
		systemService.saveRole(role);
		return JSONResult.ok("保存角色'" + role.getName() + "'成功");
	}
	
	@RequiresPermissions("sys:role:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(Role role) {
		if(!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)){
			return JSONResult.fail("越权操作，只有超级管理员才能修改此数据！");
		}
		systemService.deleteRole(role);
		return JSONResult.ok("删除角色成功");
	}

	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	private boolean checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return true;
		} else if (name!=null && systemService.getRoleByName(name) == null) {
			return true;
		}
		return false;
	}

	/**
	 * 验证角色英文名是否有效
	 * @param oldEnname
	 * @param enname
	 * @return
	 */
	private boolean checkEnname(String oldEnname, String enname) {
		if (enname!=null && enname.equals(oldEnname)) {
			return true;
		} else if (enname!=null && systemService.getRoleByEnname(enname) == null) {
			return true;
		}
		return false;
	}

}
