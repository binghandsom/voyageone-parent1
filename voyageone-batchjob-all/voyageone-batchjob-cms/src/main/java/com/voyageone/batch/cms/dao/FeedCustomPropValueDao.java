package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.FeedCustomPropValueBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义翻译表
 */
@Repository
public class FeedCustomPropValueDao extends BaseDao {

	/**
	 * 获取指定店铺的所有的自定义翻译(店铺级共通)
	 * @param channelId channel id
	 * @return (属性, 翻译)
	 */
	public Map<String, String> getList_common(String channelId) {
		Map<String, String> result = new HashMap<>();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("channel_id", channelId);

		List<FeedCustomPropValueBean> feedCustomPropValueBeanList_common = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_feed_custom_prop_value_select_all_common", paramMap);

		for (FeedCustomPropValueBean feedCustomPropValueBean : feedCustomPropValueBeanList_common) {
			result.put(feedCustomPropValueBean.getFeed_prop_origin(), feedCustomPropValueBean.getFeed_prop_translate());
		}

		return result;
	}

	/**
	 * 获取指定店铺的所有的自定义翻译(非共通)
	 * @param channelId channel id
	 * @return (类目path, <属性, 翻译>)
	 */
	public Map<String, Map<String, String>> getList_not_common(String channelId) {
		Map<String, Map<String, String>> result = new HashMap<>();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("channel_id", channelId);

		List<FeedCustomPropValueBean> customPropValueBeanList_not_common = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_bt_feed_custom_prop_value_select_all_not_common", paramMap);

		for (FeedCustomPropValueBean feedCustomPropValueBean : customPropValueBeanList_not_common) {
			// 先看看map里有没有这个类目的
			if (result.containsKey(feedCustomPropValueBean.getFeed_cat_path())) {
				// 已有的场合
				result.get(feedCustomPropValueBean.getFeed_cat_path()).put(feedCustomPropValueBean.getFeed_prop_origin(), feedCustomPropValueBean.getFeed_prop_translate());
			} else {
				// 没有的场合
				Map<String, String> map = new HashMap<>();
				map.put(feedCustomPropValueBean.getFeed_prop_origin(), feedCustomPropValueBean.getFeed_prop_translate());
				result.put(feedCustomPropValueBean.getFeed_cat_path(), map);
			}
		}

		return result;
	}

}