package com.voyageone.service.daoext.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.TmOrderChannelConfigBean;
import com.voyageone.service.model.com.TmOrderChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/11
 */
@Repository
public interface TmOrderChannelDaoExt {

	List<Map<String, Object>> selectAllCompany();

	List<TmOrderChannelModel> selectChannelByPage(Map<String, Object> params);

	Integer selectChannelCount(Map<String, Object> params);

	Integer selectChannelConfigCount(Map<String, Object> params);

	List<TmOrderChannelConfigBean> selectChanneConfigByPage(Map<String, Object> params);
	
}
