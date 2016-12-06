package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.com.TmSmsConfigBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/15
 */
@Repository
public interface TmSmsConfigDaoExt {

	Integer selectSmsConfigCount(Map<String, Object> params);

	List<TmSmsConfigBean> selectSmsConfigByPage(Map<String, Object> params);

}
