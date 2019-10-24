/**

 */
package com.wskj.manage.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Dict;
import com.wskj.manage.system.entity.DictAddress;
import com.wskj.manage.system.service.DictService;
import com.wskj.manage.system.utils.DictUtils;

/**
 * 字典Controller
 * @author ganjinhua
 * @version 2014-05-16
 */
@Controller
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
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
		return "system/dictList";
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
	@RequestMapping(value = "save")//@Valid
	@ResponseBody
	public JSONResult save(Dict dict, Model model) {
//		if (!beanValidator(model, dict)){
//			return JSONResult.fail("包含非法信息");
//		}
		dictService.save(dict);
		return JSONResult.ok("保存字典'" + dict.getLabel() + "'成功");
	}
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONResult delete(Dict dict) {
		dictService.delete(dict);
		return JSONResult.ok("删除字典成功");
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String type, HttpServletResponse response) {
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
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<Dict> listData(@RequestParam(required=false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		return dictService.findList(dict);
	}

	@RequestMapping(value = "/address/all")
	@ResponseBody
	public List<DictAddress> getAllDictAddress() {
		return DictUtils.getAllDictAddress();
	}
}
