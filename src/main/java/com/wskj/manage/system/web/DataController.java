package com.wskj.manage.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.wskj.manage.common.utils.JSONResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.config.Global;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Data;
import com.wskj.manage.system.entity.User;
import com.wskj.manage.system.service.DataService;
import com.wskj.manage.system.utils.DictUtils;
import com.wskj.manage.system.utils.UserUtils;

/**
 * 数据集Controller
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/data")
public class DataController extends BaseController {

	@Autowired
	private DataService dataService;
	
	@ModelAttribute("data")
	public Data get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dataService.get(id);
		}else{
			return new Data();
		}
	}
	
	@RequiresPermissions("sys:data:edit")
	@PostMapping(value = "save")
	public JSONResult save(@Valid Data data, BindingResult result) {
		if (result.hasErrors()){
			return JSONResult.fail("包含非法信息");
		}
		dataService.save(data);
		if(data.getChildDeptList()!=null){
			Data childOffice = null;
			for(String id : data.getChildDeptList()){
				childOffice = new Data();
				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_type", "未知"));
				childOffice.setParent(data);
				childOffice.setType("2");
				childOffice.setGrade(String.valueOf(Integer.valueOf(data.getGrade())+1));
				childOffice.setUseable(Global.YES);
				dataService.save(childOffice);
			}
		}
		return JSONResult.ok("保存成功");
	}
	
	@RequiresPermissions("sys:data:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(Data data) {
		int result = dataService.delete(data);
		return result != 0 ? JSONResult.ok("删除成功") : JSONResult.fail("删除失败,请稍后重试");
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@GetMapping(value = "treeData")
	public JSONResult treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
                                              @RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Data> list = dataService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Data e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return JSONResult.ok(mapList);
	}
}
