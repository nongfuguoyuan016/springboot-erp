package com.wskj.manage.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Area;
import com.wskj.manage.system.service.AreaService;
import com.wskj.manage.system.utils.UserUtils;

/**
 * 区域Controller
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}
	
	@RequiresPermissions("sys:area:edit")
	@PostMapping(value = "save")
	public JSONResult save(@Valid Area area, BindingResult result) {
		if (result.hasErrors()) {
			return JSONResult.fail("包含非法信息");
		}
		int res = areaService.save(area);
		return res != 0 ? JSONResult.ok("保存区域'" + area.getName() + "'成功") : JSONResult.fail("保存失败,请稍后再试");
	}
	
	@RequiresPermissions("sys:area:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(Area area) {
		int result = areaService.delete(area);
		return result != 0 ? JSONResult.ok("删除区域成功") : JSONResult.fail("删除失败,请稍后再试");
	}

	@RequiresPermissions("user")
	@GetMapping(value = "treeData")
	public JSONResult treeData(@RequestParam(required=false) String extId) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
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
