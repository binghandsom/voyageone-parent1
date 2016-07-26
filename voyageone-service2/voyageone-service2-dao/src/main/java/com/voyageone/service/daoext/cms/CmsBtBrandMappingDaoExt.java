package com.voyageone.service.daoext.cms;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.voyageone.service.bean.cms.CmsBtBrandBean;
import com.voyageone.service.bean.cms.CmsBtBrandMappingBean;

/**
 * Created by Wangtd on 7/25/16.
 */
@Repository
public interface CmsBtBrandMappingDaoExt {

	/**
	 * 检索品牌映射关系（合计）
	 */
	Long searchBrandsCount(Map<String, Object> params);
	
	/**
	 * 检索品牌映射关系（分页）
	 */
	List<CmsBtBrandMappingBean> searchBrandsByPage(Map<String, Object> params);
	
	/**
	 * 检索Master品牌
	 */
	List<CmsBtBrandBean> searchMasterBrands(@Param("channelId") String channelId, @Param("cartId") int cartId,
			@Param("brandName") String brandName);
	
	/**
	 * 检索聚美品牌
	 */
	List<CmsBtBrandBean> searchJmBrands(@Param("langId") String langId, @Param("brandName") String brandName);

	/**
	 * 检索已匹配的品牌映射关系
	 */
	List<CmsBtBrandMappingBean> searchMatchedBrands(@Param("channelId") String channelId, @Param("cartId") int cartId,
			@Param("langId") String langId, @Param("brandId") String brandId);

}
