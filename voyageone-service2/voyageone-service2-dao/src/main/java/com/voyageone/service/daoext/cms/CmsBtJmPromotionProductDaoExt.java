package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductExtModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionProductDaoExt {

    List<MapModel> selectListByWhere(Map<String, Object> map);

    CmsBtJmPromotionProductModel selectByProductCodeChannelIdCmsBtJmPromotionId(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    // List<Map<String, Object>> selectExportInfoListByPromotionId(int promotionId);

    // List<Map<String, Object>> selectListCmsBtJmImportProductByPromotionId(int promotionId);

    int deleteByPromotionId(int promotionId);

    int deleteByProductIdListInfo(ProductIdListInfo parameter);

    int jmNewUpdateAll(int promotionId);

    int jmNewByProductIdListInfo(ProductIdListInfo parameter);

    int updateDealEndTimeAll(ParameterUpdateDealEndTimeAll parameter);

//    int updateDealEndTime(ParameterUpdateDealEndTime parameter);

    //jm2 begin
    List<MapModel> selectPageByWhere(Map<String, Object> map);//add

    int selectCountByWhere(Map<String, Object> ma);//add

    CmsBtJmPromotionProductModel selectFullMinusDateRepeat(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId, @Param("channelId") String channelId, @Param("productCode") String productCode, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);

    CmsBtJmPromotionProductModel selectDateRepeatByCode(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId, @Param("channelId") String channelId, @Param("productCode") String productCode, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);

    //add  不包含本次活动
    Boolean existsCode(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId, @Param("channelId") String channelId, @Param("productCode") String productCode, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);

    CmsBtJmPromotionProductModel selectByProductCode(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    List<Map<String, Object>> selectExportListByPromotionId(@Param("promotionId") int promotionId, @Param("codes")List<String> codes);

    int batchUpdateDealPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId, @Param("dealPrice") String dealPrice);

    //1. if未上传  then price_status=1   2.if已上传&预热未开始  then price_status=1
    int batchSynchPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int synchAllPrice(int promotionId);

    //1. if未上传  then synch_status=1
    int batchCopyDeal(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    //2.if已上传&预热未开始  then price_status=1
    int batchCopyDealUpdatePrice(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    //1. if未上传  then synch_status=1   2.if已上传  then price_status=1
    // int copyDealAll(int promotionId);
    int copyDealAll_UpdatePriceStatus(int promotionId);

    int copyDealAll_UpdateSynchStatus(int promotionId);

    int batchDeleteProduct(@Param("listPromotionProductId") List<Integer> listPromotionProductId);

    int deleteAllProduct(int promotionId);

    List<CmsBtJmPromotionProductModel> selectJMCopyList(int promotionId);
    /**
     * 获取jm_hash_id
     *
     * @param productCode
     * @param channelId
     * @return
     */
    List<String> selectJmHashIds(@Param("channelId") String channelId, @Param("productCode") String productCode, @Param("nowDate") Date nowDate);

    //是否存在在销售的商品
    CmsBtJmPromotionProductModel selectOnSaleByCode(@Param("channelId") String channelId, @Param("productCode") String productCode, @Param("nowDate") Date nowDate);

    int updateAvgPriceByPromotionProductId(long cmsBtJmPromotionProductId);

    int updateAvgPriceByListPromotionProductId(@Param("listPromotionProductId") List<Integer> listPromotionProductId);

    //获取变更数量
    int selectChangeCountByPromotionId(long cmsBtJmPromotionProductId);
    int selectCountByPromotionId(long cmsBtJmPromotionProductId);
    //获取本活动商品在其他活动处于在售状态的商品
    CmsBtJmPromotionProductModel selectOnSaleByNoPromotionId(@Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId, @Param("nowDate") Date nowDate);

    //获取synch_status!=2的商品
    List<CmsBtJmPromotionProductModel> selectNotSynchListByPromotionProductIds(@Param("listPromotionProductId") List<Integer> listPromotionProductId);

    List<CmsBtJmPromotionProductModel> selectNotSynchListByProductCodes(@Param("jmPromotionId") int jmPromotionId,@Param("listProductCode") List<String> listProductCode);
    //更新synch_status==2 的errorMsg
    int updateSynch2ErrorMsg(@Param("listPromotionProductId") List<Integer> listPromotionProductId, @Param("errorMsg") String errorMsg);

    List<CmsBtJmPromotionProductExtModel> selectProductInfoByTagId(Integer tagId);

    List<CmsBtJmPromotionProductExtModel> selectProductInfoByTagId2(Integer tagId);

    int updateRemark(@Param("jmPromotionProductId") int jmPromotionProductId,@Param("remark") String remark);

    List<String> selectCodesByJmPromotionId(int jmPromotionId);
    //jm2 end

    /**
     * 取得当前有效的活动下的所有产品
     */
    List<CmsBtJmPromotionProductModel> selectValidProductInfo(@Param("channelId") String channelId, @Param("cartId") int cartId);

    /**
     * 更新活动下的产品的库存数据
     */
    int updateProductStockInfo(@Param("jmPromotionProductList") List<CmsBtJmPromotionProductModel> productList);


    List<ProductImportBean> selectProductByJmPromotionId(Integer jmPromotionId);

    int updatePromotionTag(@Param("jmPromotionId") int jmPromotionId, @Param("channelId") String channelId);
}
