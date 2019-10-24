/**

 */
package com.wskj.manage.system.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.manage.common.utils.CacheUtils;
import com.wskj.manage.common.utils.SpringContextHolder;
import com.wskj.manage.common.utils.StringUtils;
import com.wskj.manage.system.dao.DictDao;
import com.wskj.manage.system.entity.Dict;
import com.wskj.manage.system.entity.DictAddress;

/**
 * 字典工具类
 * @author ganjinhua
 * @version 2013-5-29
 */
public class DictUtils {
	
	private static DictDao dictDao = null;

	public static final String CACHE_DICT_MAP = "dictMap";

	public static final String CACHE_DICT_ADDRESS_LIST = "dictAddressList";

	public static final String CACHE_ALL_FIRST_DICT = "allFirstDice";
	
	private static DictDao dictDao() {
		if (dictDao == null) {
			dictDao = SpringContextHolder.getBean(DictDao.class);
		}
		return dictDao;
	}
	
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public static String getDictLabels(String values, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")){
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}

	public static String getDictId(String type, String label){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getId();
				}
			}
		}
		return null;
	}

	public static Dict getDict(String type, String id) {
		Dict dict = null;
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(id)) {
			for (Dict d : getDictList(type)){
				if (type.equals(d.getType()) && id.equals(d.getId())){
					return d;
				}
			}
		}
		return dict;
	}

	public static List<Dict> getDictList(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>)CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao().findAllList(new Dict())){
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}

	public static List<Dict> getAllFirstDictList(){
		@SuppressWarnings("unchecked")
		List<Dict> allFirstDictList = (List<Dict>)CacheUtils.get(CACHE_ALL_FIRST_DICT);
		if (allFirstDictList==null){
			Dict dict = new Dict();
			dict.setParentId("0");
			//根据父id为0的查询所有一级目录
			allFirstDictList = dictDao().findAllListByParentId(dict);
			CacheUtils.put(CACHE_ALL_FIRST_DICT,allFirstDictList);
		}
		return allFirstDictList;
	}

	/**
	 * 根据父id查询
	 * @param parentId
	 * @return
	 */
	public static List<Dict> getListByParentId(String parentId){
		List<Dict> list = Lists.newArrayList();
		if(StringUtils.isNotBlank(parentId)){
			Dict dict = new Dict();
			dict.setParentId(parentId);
			list = dictDao().findAllListByParentId(dict);
		}
		return list;
	}


	public static List<DictAddress> getAllDictAddress() {
		@SuppressWarnings("unchecked")
		List<DictAddress> dictAddressList = (List<DictAddress>)CacheUtils.get(CACHE_DICT_ADDRESS_LIST);
		if (dictAddressList==null){
			dictAddressList = dictDao().getAllDictAddress();
			CacheUtils.put(CACHE_DICT_ADDRESS_LIST, dictAddressList);
		}
		return dictAddressList;
	}

	public static DictAddress getDictAddressById(Integer id) {
		DictAddress dictAddress = null;
		Optional<DictAddress> optional = getAllDictAddress().stream().filter(a -> a.getId().equals(id)).findFirst();
		if (optional.isPresent()) {
			dictAddress = optional.get();
		}
		return dictAddress;
	}

	public static List<DictAddress> getDictAddressOfProvince() {
		return getAllDictAddress().stream().filter(a -> a.getType().equals(1)).sorted(Comparator.comparingInt(DictAddress::getId)).collect(Collectors.toList());
	}

	public static List<DictAddress> getDictAddressByParentId(Integer parentId) {
		return getAllDictAddress().stream().filter(a -> a.getParentId().equals(parentId)).sorted(Comparator.comparingInt(DictAddress::getId)).collect(Collectors.toList());
	}

}
