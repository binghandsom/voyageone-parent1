package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/22
 */
@Repository
public interface ComMtTrackingInfoConfigDaoExt {

	Integer selectCartTrackingCount(Map<String, Object> params);

	List<ComMtTrackingInfoConfigBean> selectCartTrackingByPage(Map<String, Object> params);

}
