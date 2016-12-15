package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.bean.com.TmTaskControlBean;
import com.voyageone.service.model.com.TmTaskControlModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
@Repository
public interface ComMtTaskDaoExt {

	Integer selectTaskConfigCount(Map<String, Object> params);

	List<TmTaskControlBean> selectTaskConfigByPage(Map<String, Object> params);

	Integer selectTypeCount(Map<String, Object> params);

	List<ComMtTaskBean> selectTypeByPage(Map<String, Object> params);

	TmTaskControlModel selectTaskConfig(Map<String, Object> params);

	List<ComMtTaskBean> searchTaskByChannelId(@Param("channelCfgName") String channelCfgName,
			@Param("runCfgName") String runCfgName, @Param("orderChannelId") String channelId);

}
