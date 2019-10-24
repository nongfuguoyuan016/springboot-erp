/**

 */
package com.wskj.manage.system.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Menu;
import com.wskj.manage.system.service.SystemService;
import com.wskj.manage.system.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 菜单Controller
 * @author ganjinhua
 * @version 2013-3-23
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@ModelAttribute("menu")
	public Menu get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getMenu(id);
		}else{
			return new Menu();
		}
	}
	
	@RequiresPermissions("sys:menu:edit")
	@PostMapping(value = "save")
	public JSONResult save(Menu menu, BindingResult result) {
		if(!UserUtils.getUser().isAdmin()){
			return JSONResult.fail("越权操作，只有超级管理员才能添加或修改数据！");
		}
		if (result.hasErrors()) {
			JSONResult.fail("包含非法信息");
		}
		systemService.saveMenu(menu);
		return JSONResult.ok("保存菜单'" + menu.getName() + "'成功");
	}
	
	@RequiresPermissions("sys:menu:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(Menu menu) {
		systemService.deleteMenu(menu);
		return JSONResult.ok("删除菜单成功");
	}
	
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHide
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@GetMapping(value = "treeData")
	public JSONResult treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = systemService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			Menu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				if(isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return JSONResult.ok(mapList);
	}
}
