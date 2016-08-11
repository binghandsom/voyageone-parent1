package com.voyageone.service.daoext.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.model.admin.TmOrderChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/11
 */
@Repository
public interface TmOrderChannelDaoExt {

	List<Map<String, Object>> selectAllCompany();

	List<TmOrderChannelModel> selectChannelList(Map<String, Object> params);

	Integer selectChannelCount(Map<String, Object> params);
	
}
