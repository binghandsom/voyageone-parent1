/*
 * CmsBtPromotionSkusDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtPromotionCodesDaoExtCamel {
    int deleteByPromotionId(int promotionId);
    int deleteByPromotionCodeList(Map<String,Object> map);
    int updateJmPromotionPrice(@Param("jmPromotionId")int jmPromotionId, @Param("listPromotionProductId") List<Long> listPromotionProductId);

    /**
     * 取得当前有效的活动下的所有产品
     */
    List<CmsBtPromotionCodesModel> selectValidProductInfo(@Param("channelId") String channelId, @Param("cartId") int cartId);

    /**
     * 更新活动下的产品的库存数据
     */
    int updateProductStockInfo(@Param("promotionProductList") List<CmsBtPromotionCodesModel> productList);
}