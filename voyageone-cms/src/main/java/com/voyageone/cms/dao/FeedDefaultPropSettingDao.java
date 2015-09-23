package com.voyageone.cms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.modelbean.FeedDefaultPropModel;
import com.voyageone.cms.modelbean.FeedDefaultPropOptionModel;
import com.voyageone.common.Constants;
@Repository
public class FeedDefaultPropSettingDao extends BaseDao{
	
	/**
	 * 获取所有共同属性.
	 * @param channelId
	 * @return
	 */
	public List<FeedDefaultPropModel> getFeedDefaultProps(String channelId){
		Map<String, Object> parms = new HashMap<>();
		parms.put("channelId", channelId);
		return selectList(Constants.DAO_NAME_SPACE_CMS +"get_feed_default_props", parms);
	}
	
	/**
	 * 获取所有共同属性选项.
	 * @param channelId
	 * @return
	 */
	public List<FeedDefaultPropOptionModel> getFeedDefaultPropOptions(){
		return selectList(Constants.DAO_NAME_SPACE_CMS +"get_feed_default_prop_options");
	}
	
	/**
	 * 删除当前渠道下所有的默认属性值.
	 * @param channelId
	 * @return
	 */
	public int delete(String channelId) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channelId", channelId);

		return super.delete("delete_curChannel_props", parmMap);
	}
	
	/**
	 * 保存当前渠道下的默认属性和默认值.
	 * @param models
	 * @return
	 */
	public int save(List<FeedDefaultPropModel> models) {

		Map<String, List<FeedDefaultPropModel>> insertDataMap = new HashMap<String, List<FeedDefaultPropModel>>();
		insertDataMap.put("list", models);

		int inCount = super.updateTemplate.insert("insert_curChannel_props", insertDataMap);

		return inCount;
	}
	
	/**
	 * 查看当前渠道是否已经存在.
	 * @param channelId
	 * @return
	 */
	public boolean isExist(String channelId){
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("channelId", channelId);
		int count = selectOne(Constants.DAO_NAME_SPACE_CMS +"get_feed_default_prop_count", parmMap);
		if (count>0) {
			return true;
		}
		return false;
	} 

}
