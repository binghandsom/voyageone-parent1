package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.TmCarrierChannelBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
@Repository
public interface TmCarrierChannelDaoExt {

	Integer selectCarrierConfigCount(Map<String, Object> params);

	List<TmCarrierChannelBean> selectCarrierConfigByPage(Map<String, Object> params);

	List<Map<String, Object>> selectAllCarrier();

}
