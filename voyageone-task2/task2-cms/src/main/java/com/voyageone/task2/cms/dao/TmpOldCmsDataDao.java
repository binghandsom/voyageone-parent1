package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.bean.TmpOldCmsDataBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TmpOldCmsDataDao extends BaseDao {

	/**
	 * 取出要处理的商品列表
	 */
	public List<TmpOldCmsDataBean> getList() {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_tmp_old_cms_data_select");
	}

	/**
	 * 看看数据库中是否存在
	 */
	public int checkExist(String channelId, String code) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channel_id", channelId);
		paramMap.put("code", code);

		return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_tmp_old_cms_data_check_exist", paramMap);
	}

	/**
	 * 将cms_tmp_old_cms_data表的 标志置位->1
	 */
	public int setFinish(String channelId, String cart_id, String code) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channel_id", channelId);
		paramMap.put("cart_id", cart_id);
		paramMap.put("code", code);
		return updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_tmp_old_cms_data_set_finish", paramMap);
	}

}