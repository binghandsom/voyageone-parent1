package com.voyageone.service.daoext.com;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.model.com.TmNewShopDataModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/31
 */
@Repository
public interface TmNewShopDataDaoExt {

	Integer selectNewShopCount(Map<String, Object> params);

	List<TmNewShopDataModel> selectNewShopByPage(Map<String, Object> params);

}
