/**

 */
package com.wskj.manage.system.web;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Dict;
import com.wskj.manage.system.service.DictService;
import com.wskj.manage.system.utils.DictUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典Controller
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@ModelAttribute
	public Dict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "list/data")
	@ResponseBody
	public Map<String,Object> listData(Dict dict, int pageNo, int pageSize) {
		Map<String,Object> map = Maps.newHashMap();
		Page<Dict> page = dictService.findPage(pageNo, pageSize, dict);
		map.put("total",page.getTotal());
		map.put("rows",page.getResult());
		return map;
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		return "system/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@PostMapping(value = "save")//@Valid
	public JSONResult save(Dict dict, BindingResult result) {
		if (result.hasErrors()) {
			return JSONResult.fail("包含非法信息");
		}
		int res = dictService.save(dict);
		return res != 0 ? JSONResult.ok("保存字典'" + dict.getLabel() + "'成功") : JSONResult.fail("保存失败,请稍后再试");
	}
	
	@RequiresPermissions("sys:dict:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(Dict dict) {
		int result = dictService.delete(dict);
		return result != 0 ? JSONResult.ok("删除字典成功") : JSONResult.fail("删除失败,请稍后再试");
	}
	
	@RequiresPermissions("user")
	@GetMapping(value = "treeData")
	public JSONResult treeData(@RequestParam(required=false) String type) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Dict dict = new Dict();
		dict.setType(type);
		List<Dict> list = dictService.findList(dict);
		for (int i=0; i<list.size(); i++){
			Dict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return JSONResult.ok(mapList);
	}
	
	@RequestMapping(value = "listData")
	public JSONResult listData(@RequestParam(required=false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		return JSONResult.ok(dictService.findList(dict));
	}

	@GetMapping(value = "/address/all")
	public JSONResult getAllDictAddress() {
		return JSONResult.ok(DictUtils.getAllDictAddress());
	}
}
