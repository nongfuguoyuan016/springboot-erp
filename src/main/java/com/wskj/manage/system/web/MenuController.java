/**

 */
package com.wskj.manage.system.web;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.FastJsonUtils;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Menu;
import com.wskj.manage.system.service.SystemService;
import com.wskj.manage.system.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	public JSONResult save(@Validated Menu menu, BindingResult result) {
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
	 * 菜单树形数据
	 * @return
	 */
	@RequiresPermissions("sys:menu:view")
	@GetMapping(value = "treeData")
	public JSONResult treeData() {
		List<Menu> allMenu = systemService.findAllMenu();
		// 转为json再转为list,复制原来的集合
		String menuString = FastJsonUtils.toJsonStringIncludeProperties(allMenu,"id","name","parentId","href","permission","children","parent","createDate","type");
		List<Menu> retList = JSONArray.parseArray(menuString,Menu.class);
		Map<String,Menu> menuMap = Maps.newHashMap();
		retList.forEach(a -> menuMap.put(a.getId(),a));
		retList.forEach(a -> {
			if (!Menu.getRootId().equals(a.getParentId()) ) {
				Menu menu = menuMap.get(a.getParentId());
				if (menu != null) {
					menu.getChildren().add(a);
				}
			}
		});
		List<Menu> menus = retList.stream().filter(a -> Menu.getRootId().equals(a.getParentId())).sorted((a, b) -> a.getCreateDate().before(b.getCreateDate()) ? 1 : -1).collect(Collectors.toList());
		Object ret = FastJsonUtils.toJsonArrayIncludeProperties(menus,"id","name","parentId","href","permission","children","type");
		return JSONResult.ok(ret);
	}
}
