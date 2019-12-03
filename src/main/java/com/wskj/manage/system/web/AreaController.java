package com.wskj.manage.system.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.FastJsonUtils;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Area;
import com.wskj.manage.system.service.AreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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
	public JSONResult save(@Validated Area area, BindingResult result) {
		if (result.hasErrors()) {
			return JSONResult.fail("包含非法信息");
		}
		area.setParent(new Area(area.getParentId()));
		int res = areaService.save(area);
		return res != 0 ? JSONResult.ok("保存区域'" + area.getName() + "'成功") : JSONResult.fail("保存失败,请稍后再试");
	}
	
	@RequiresPermissions("sys:area:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(Area area) {
		int result = areaService.delete(area);
		return result != 0 ? JSONResult.ok("删除区域成功") : JSONResult.fail("删除失败,请稍后再试");
	}

	@RequiresPermissions("sys:area:view")
	@GetMapping(value = "list/data")
	public JSONResult treeData() {
		List<Area> allList = areaService.findAll();
		// 转为json再转为list,复制原来的集合
		String areaString = FastJsonUtils.toJsonStringIncludeProperties(allList,"id","parentId","parentIds","name","code","type","remarks");
		List<Area> retList = JSONArray.parseArray(areaString,Area.class);
		Map<String,Area> areaMap = new HashMap<>(retList.size());
		retList.forEach(a -> {
			if (a.getParentIds().trim().length() > 2) {
				a.setParentIds(a.getParentIds().substring(2,a.getParentIds().trim().length()-1));
			} else {
				a.setParentIds(a.getParentIds().substring(0,1));
			}
			areaMap.put(a.getId(),a);
		});
		retList.stream().filter(a -> !"1".equals(String.valueOf(a.getType()))).forEach(a -> { areaMap.get(a.getParentId()).getChildren().add(a); });
		Object ret = FastJsonUtils.toJsonArrayIncludeProperties(retList.stream().filter(a -> "1".equals(String.valueOf(a.getType()))).collect(toList()),
				"id","name","parentId","parentIds","code","children","type","remarks");
		return JSONResult.ok(ret);
	}
}
