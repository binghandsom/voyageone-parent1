package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import com.voyageone.service.bean.com.TmChannelShopBean;
import com.voyageone.service.bean.com.TmChannelShopConfigBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/19
 */
public interface TmChannelShopDaoExt {

	Integer selectCartShopCount(Map<String, Object> params);

	List<TmChannelShopBean> selectCartShopByPage(Map<String, Object> params);

	Integer selectCartShopConfigCount(Map<String, Object> params);

	List<TmChannelShopConfigBean> selectCartShopConfigByPage(Map<String, Object> params);

}
