package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.FeedCustomPropBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义属性表
 */
@Repository
public class FeedCustomPropDao extends BaseDao {

	/**
	 * 获取指定店铺的所有的自定义属性(店铺级共通)
	 * @param channelId channel id
	 * @return (属性)
	 */
	public List<String> getList_common(String channelId) {
		List<String> result = new ArrayList<>();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("channel_id", channelId);

		List<FeedCustomPropBean> feedCustomPropBeanList_common = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_feed_custom_prop_select_all_common", paramMap);

		for (FeedCustomPropBean feedCustomPropBean : feedCustomPropBeanList_common) {
			result.add(feedCustomPropBean.getFeed_prop());
		}

		return result;
	}

	/**
	 * 获取指定店铺的所有的自定义属性(非共通)
	 * @param channelId channel id
	 * @return (类目path, 属性列表)
	 */
	public Map<String, List<String>> getList_not_common(String channelId) {
		Map<String, List<String>> result = new HashMap<>();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("channel_id", channelId);

		List<FeedCustomPropBean> feedCustomPropBeanList_not_common = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_feed_custom_prop_select_all_not_common", paramMap);

		for (FeedCustomPropBean feedCustomPropBean : feedCustomPropBeanList_not_common) {
			// 先看看map里有没有这个类目的
			if (result.containsKey(feedCustomPropBean.getFeed_cat_path())) {
				// 已有的场合
				result.get(feedCustomPropBean.getFeed_cat_path()).add(feedCustomPropBean.getFeed_prop());
			} else {
				// 没有的场合
				List<String> lst = new ArrayList<>();
				lst.add(feedCustomPropBean.getFeed_prop());
				result.put(feedCustomPropBean.getChannel_id(), lst);
			}
		}

		return result;
	}

}