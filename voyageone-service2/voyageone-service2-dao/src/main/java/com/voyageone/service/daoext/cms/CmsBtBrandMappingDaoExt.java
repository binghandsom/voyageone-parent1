package com.voyageone.service.daoext.cms;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.cms.BrandBtMappingBean;

/**
 * Created by Wangtd on 7/25/16.
 */
@Repository
public interface CmsBtBrandMappingDaoExt {

	/**
	 * 检索品牌映射关系
	 */
	List<BrandBtMappingBean> searchBrands(Map<String, Object> params);

}
