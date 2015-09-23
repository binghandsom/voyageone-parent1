package com.voyageone.cms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.formbean.IRecursion;
import com.voyageone.common.util.StringUtils;


public class DaoHandler<K,T extends IRecursion> {
	private K dao;
	private Method method;
	private Object parameter;
	private List<T> resultList;
	private String fieldnamepara;
	public DaoHandler(K dao,String methodName,Object parameter,String fieldnamepara) throws Exception{
		this.method = dao.getClass().getMethod(methodName,parameter.getClass());
		this.dao = dao;
		this.parameter = parameter;
		this.fieldnamepara = fieldnamepara;
	
	}
	
	@SuppressWarnings("unchecked")
	public T execute() throws Exception{
		return  (T) method.invoke(dao,parameter);
		
	}
	
	public void setParameter(Object parameter){
		this.parameter=parameter;
	}
	
	/**
	 * 检索指定的categoryID找到该ID所对应的分类信息和所有父分类信息
	 * @param categoryId
	 * @param channelId
	 * @param categoryCNList
	 * @throws Exception 
	 */
	public  List<T> getCategoryGroup() throws Exception {
		if(resultList == null){
			resultList=new ArrayList<T>();
		}
		T item=(T)execute();
		if(item == null) {
			return resultList;
		}
		resultList.add(item);
		if(item.getParentId() == 0) {
			return resultList;
		} else {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("id", item.getParentId());
			data.put("channelId", item.getChannelId());
			setParameter(data);
			getCategoryGroup();
		}
		return resultList;
	}
	
	/**
	 * 从一组父子分类信息中找出子的分类信息
	 * @param categoryList 下标为0的那条记录是子的分类 
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public Map<String, T> getCategoryInfoByGroup() throws Exception{
		if(resultList == null){
			resultList=getCategoryGroup();
		}
		if(resultList.size() == 0)
		{
			return null;
		}
		T ret=(T) resultList.get(0);
		Class c= ret.getClass();
		Field[] fields = c.getDeclaredFields();
		Map<String, T> resultMap = new HashMap<String, T>();
		if(!StringUtils.isEmpty(fieldnamepara)) {
			T bean = (T) getFieldForBean(fieldnamepara);
			resultMap.put("fieldBean", bean);
			
		}
		for (Field f : fields) {
			if(null == f.getAnnotation(Extends.class)){
				continue;
			}
			Object value = fieldSearch(f);
			Method m = c.getMethod("set"+StringUtils.uppercaseFirst(f.getName()),f.getType());
			m.invoke(ret,value);
		}
		resultMap.put("resultBean", ret);
		return resultMap;
	}
	
	private Object fieldSearch(Field field) throws Exception{
		for (T categoryBean : resultList) {
			Class c= categoryBean.getClass();
			Method m = c.getMethod("get"+StringUtils.uppercaseFirst(field.getName()));
			Object value =  m.invoke(categoryBean);    //调用getter方法获取属性值
			if(value != null && !StringUtils.isEmpty(value.toString())){
				return value;
			}
		}
		return null;
	}
	private T getFieldForBean(String fieldname) throws Exception {
		for(T categoryBean: resultList) {
			Class c = categoryBean.getClass();
			Field field = c.getDeclaredField(fieldname);
			Method m = c.getMethod("get"+StringUtils.uppercaseFirst(field.getName()));
			Object value =  m.invoke(categoryBean);    //调用getter方法获取属性值
			if(value != null && !StringUtils.isEmpty(value.toString())){
				return categoryBean;
			}
		}
		return null;
	}
}
