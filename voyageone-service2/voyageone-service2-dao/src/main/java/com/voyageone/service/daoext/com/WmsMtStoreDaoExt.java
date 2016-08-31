package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.CtStoreConfigBean;
import com.voyageone.service.bean.com.WmsMtStoreBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/2/12
 */
@Repository
public interface WmsMtStoreDaoExt {

	Integer selectStoreCount(Map<String, Object> params);

	List<WmsMtStoreBean> selectStoreByPage(Map<String, Object> params);

	Integer selectStoreConfigCount(Map<String, Object> params);

	List<CtStoreConfigBean> selectStoreConfigByPage(Map<String, Object> params);

	List<String> selectIdsByChannel(List<String> ids);

	List<WmsMtStoreBean> selecAllStore(@Param("orderChannelId") String channelId);


}
