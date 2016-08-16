package com.voyageone.service.daoext.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.admin.ComMtThirdPartyConfigBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/16
 */
@Repository
public interface ComMtThirdPartyConfigDaoExt {

	Integer searchThirdPartyConfigCount(Map<String, Object> params);

	List<ComMtThirdPartyConfigBean> searchThirdPartyConfigByPage(Map<String, Object> params);

}
