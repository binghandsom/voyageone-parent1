package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.model.com.TmPortConfigModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/23
 */
@Repository
public interface TmPortConfigDaoExt {

	Integer selectPortConfigCount(Map<String, Object> params);

	List<TmPortConfigModel> selectPortConfigByPage(Map<String, Object> params);

}
