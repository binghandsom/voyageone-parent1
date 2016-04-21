package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.model.cms.CmsBtFeedCustomPropValueModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注意点:
 * 	1. 这段代码包含以下两个表的操作:
 * 		voyageone_cms2.cms_bt_feed_custom_prop
 * 		voyageone_cms2.cms_bt_feed_custom_prop_value
 * 	2. 相关业务逻辑代码在: CmsBtFeedCustomPropService
 * Created by zhujiaye on 16/2/26.
 */
@Repository
public class CmsBtFeedCustomPropAndValueDao extends ServiceBaseDao {

	/**
	 * 获取自定义属性列表
	 * @param channel_id channel id
	 * @return 自定义属性列表
	 */
	public List<FeedCustomPropWithValueBean> selectPropList(String channel_id) {

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("channel_id", channel_id);

		return selectList("cms_bt_feed_custom_prop_select", paramMap);
	}

	/**
	 * 获取自定义属性值翻译列表
	 * @param channel_id channel id
	 * @return 自定义属性值翻译列表
	 */
	public List<CmsBtFeedCustomPropValueModel> selectPropValue(String channel_id) {

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("channel_id", channel_id);

		return selectList("cms_bt_feed_custom_prop_value_select", paramMap);
	}

}
