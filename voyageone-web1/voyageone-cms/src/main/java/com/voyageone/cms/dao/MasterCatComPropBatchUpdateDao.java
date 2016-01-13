package com.voyageone.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.modelbean.CmsCategoryModel;
import com.voyageone.cms.modelbean.PropertyValue;
import com.voyageone.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MasterCatComPropBatchUpdateDao extends BaseDao {
	
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterCatComPropBatchUpdateDao.class);

	/**
	 * 获取类目属性已完成类目id.
	 * @param propName
	 * @return
	 */
	public List<Map<String,String>> getComPropOptions(String propName){

		logger.info("查询主类目共通属性选项开始...");

		List<Map<String,String>> resultList = selectList(Constants.DAO_NAME_SPACE_CMS + "get_Com_Prop_Options", propName);

		logger.info("查询主类目共通属性选项结束...");

		return resultList;
	}

	/**
	 * 批量更新model的属性值.
	 * @param modelIdList
	 * @param propName
	 * @param channelId
	 * @param level
	 * @param propValue
	 * @param user
	 * @return
	 */
	public boolean batchUpdate(List<String> modelIdList,String propName,String channelId,int level,String propValue,String user){
		logger.info("["+propName+"]"+"属性值批量更新开始...");
		Map<String, Object> parmMap = new HashMap<String, Object>();

		parmMap.put("modelIdList", modelIdList);
		parmMap.put("propName",propName);
		parmMap.put("channelId",channelId);
		parmMap.put("level",level);
		parmMap.put("propValue",propValue);
		parmMap.put("user", user);

		int updateCount = super.update(Constants.DAO_NAME_SPACE_CMS + "update_batch_cat_com_propValue", parmMap);

		logger.info("["+propName+"]"+"属性值批量更新结束，"+updateCount+"条记录被更新...");

		if(updateCount>1)
			return true;

		return false;
	}

	/**
	 * 验证当前model的属性值是否存在.
	 * @param modelIdList
	 * @param modelId
	 * @param channelId
	 * @param level
	 * @param referencePropId
	 * @return
	 */
	public List<Map<String,String>> verifyPropValueExist(String propName,List<String> modelIdList,String modelId,String channelId,int level,String referencePropId){

		logger.info("验证当前model的属性值是否存在开始...");

		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("propName",propName);
		parmMap.put("modelIdList",modelIdList);
		parmMap.put("modelId",modelId);
		parmMap.put("channelId",channelId);
		parmMap.put("level",level);
		if(referencePropId!=null){
			String refPropId = "%{\"extraPropId\":\""+referencePropId+"\"%";
			parmMap.put("referencePropId", refPropId);
		}else {
			parmMap.put("referencePropId", referencePropId);
		}


		List<Map<String,String>> verifyList = super.selectList(Constants.DAO_NAME_SPACE_CMS + "verify_prop_value_exist", parmMap);
		logger.info("验证当前model的属性值是否存在结束...");
		return verifyList;
	}

	/**
	 *
	 * @param propertyValue
	 * @return
	 */
	public int addPropValue(PropertyValue propertyValue){
		logger.info("添加属性值开始...");
		int count = super.insert(Constants.DAO_NAME_SPACE_CMS + "insert_prop_value", propertyValue);
		logger.info("添加属性值结束.属性值为: " + propertyValue.getProp_id());
		return count;
	}

	/**
	 *
	 * @param propId
	 * @return
	 */
	public int removePorpValue(String propId,String channelId,int level,String levelValue){
		logger.info("删除值开始...");
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("propId",propId);
		parmMap.put("channelId",channelId);
		parmMap.put("level",level);
		parmMap.put("levelValue", levelValue);
		int count = super.delete(Constants.DAO_NAME_SPACE_CMS + "delete_sub_prop_value", parmMap);

		logger.info("删除了"+count+"条");

		return count;
	}

	/**
	 *
	 * @param propValue
	 * @param user
	 * @param propId
	 * @param channelId
	 * @param level
	 * @param levelValue
	 * @return
	 */
	public int updateSubPropValue(String propValue,String user,String propId,String channelId,int level,String levelValue){

		logger.info("更新从属性值开始...");

		Map<String, Object> parmMap = new HashMap<String, Object>();

		parmMap.put("propValue",propValue);
		parmMap.put("user", user);
		parmMap.put("propId",propId);
		parmMap.put("channelId",channelId);
		parmMap.put("level",level);
		parmMap.put("levelValue",levelValue);

		int count = super.update(Constants.DAO_NAME_SPACE_CMS + "update_sub_prop_value", parmMap);

		logger.info("更新了"+count+"条");

		return count;
	}

	/**
	 * 更新产品发布状态.
	 * @param modelIdList
	 * @param channelId
	 * @return
	 */
	public int updateProductPublishStatus(String user,List<String> modelIdList,String channelId){
		logger.info("更新产品发布状态开始...");

		Map<String, Object> parmMap = new HashMap<String, Object>();

		parmMap.put("user",user);
		parmMap.put("modelIdList",modelIdList);
		parmMap.put("channelId",channelId);

		int count = super.update(Constants.DAO_NAME_SPACE_CMS + "update_product_publish_status", parmMap);

		logger.info("更新了"+count+"条");

		return count;
	}

}
