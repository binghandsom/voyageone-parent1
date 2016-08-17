package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.ComMtValueChannelBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
@Repository
public interface ComMtValueChannelDaoExt {

	Integer searchChannelAttributeCount(Map<String, Object> params);

	List<ComMtValueChannelBean> searchChannelAttributeByPage(Map<String, Object> params);

}
