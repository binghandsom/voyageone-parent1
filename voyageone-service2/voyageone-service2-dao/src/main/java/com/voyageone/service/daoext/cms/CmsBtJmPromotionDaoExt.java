package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionDaoExt {

    List<MapModel> getJmPromotionList(Map<String, Object> map);

    long getJmPromotionCount(Map<String, Object> map);

    List<MapModel> selectListByWhere(Map<String, Object> map);

    /**
     * 获取相关渠道的可用promotions
     *
     * @return
     */
    List<MapModel> selectActivesOfChannel(Map<String, Object> map);

    List<CmsBtJmPromotionModel> selectEndList(Date nowDate);

    List<MapModel> selectMaxJmHashId(@Param("channelId") String channelId, @Param("productCode") String productCode);

    List<MapModel> selectMaxJmHashId2(@Param("channelId") String channelId, @Param("productCode") String productCode);

    List<MapModel> selectJmProductHashId(@Param("channelId") String channelId, @Param("productCode") String productCode);

    int updateSumbrandById(int id);

    List<Integer> selectCloseJmPromotionId();

    List<Map<String, Object>> selectCloseJmPromotionSku(Integer jmPromotionId);

    List<Integer> selectEffectiveJmPromotionId();

    int updatePromotionStatus(@Param("jmPromotionId") int jmPromotionId, @Param("modifier") String modifier);

    /**
     * 更新活动下的有库存产品数
     */
    int updatePromotionProdSumInfo(@Param("channelId") String channelId, @Param("cartId") int cartId);

    List<Integer> selectJmPromotionBegin();

    /**
     * 设置聚美活动各阶段的状态
     */
    int setJmPromotionStepStatus(Map<String, Object> map);

    /**
     * 更新活动基本信息(根据画面输入，可以有空值，注意：这里指更新画面可以输入的项目)
     */
    int updateByInput(CmsBtJmPromotionModel record);

    //加入活动有效列表
    List<MapModel> selectAddPromotionList(@Param("channelId") String channelId, @Param("cartId") int cartId, @Param("codeList") List<String> codeList, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);
}
