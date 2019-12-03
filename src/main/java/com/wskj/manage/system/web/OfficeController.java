package com.wskj.manage.system.web;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.config.Global;
import com.wskj.manage.common.utils.FastJsonUtils;
import com.wskj.manage.common.utils.JSONResult;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.common.web.BaseController;
import com.wskj.manage.system.entity.Office;
import com.wskj.manage.system.service.OfficeService;
import com.wskj.manage.system.utils.DictUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 机构Controller
 */
@RestController
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}
	
	@RequiresPermissions("sys:office:edit")
	@PostMapping(value = "save")
	public JSONResult save(@Validated Office office, BindingResult result) {
		if (result.hasErrors()){
			return JSONResult.fail("包含非法信息");
		}
		officeService.save(office);
		return JSONResult.ok("保存机构'" + office.getName() + "'成功");
	}
	
	@RequiresPermissions("sys:office:edit")
	@GetMapping(value = "delete")
	public JSONResult delete(Office office) {
		int result = officeService.delete(office);
		return 0 != result ? JSONResult.ok("删除机构成功") : JSONResult.ok("删除机构失败,请稍后重试") ;
	}

	/**
	 * 获取机构JSON数据
	 * @return
	 */
	@RequiresPermissions("sys:office:view")
	@GetMapping(value = "list/data")
	public JSONResult treeData() {
		List<Office> list = officeService.findAll();
		// 转为json再转为list,复制原来的集合
		String listStr = FastJsonUtils.toJsonStringIncludeProperties(list,
				"id","parentId","parentIds","name","code","type","remarks","zipCode","master","phone","address");
		List<Office> offices = JSONArray.parseArray(listStr, Office.class);
		Map<String,Office> officeMap = new HashMap<>(offices.size());
		offices.forEach(a -> {
			if (a.getParentIds().trim().length() > 2) {
				a.setParentIds(a.getParentIds().substring(2,a.getParentIds().trim().length()-1));
			} else {
				a.setParentIds(a.getParentIds().substring(0,1));
			}
			officeMap.put(a.getId(),a);
		});
		offices.stream().filter(a -> !"1".equals(String.valueOf(a.getType()))).forEach(a -> { officeMap.get(a.getParentId()).getChildren().add(a); });
		Object ret = FastJsonUtils.toJsonArrayIncludeProperties(offices.stream().filter(a -> "1".equals(String.valueOf(a.getType()))).collect(toList()),
				"id","name","parentId","parentIds","code","children","type","remarks","zipCode","master","phone","address");
		return JSONResult.ok(ret);
	}
}
