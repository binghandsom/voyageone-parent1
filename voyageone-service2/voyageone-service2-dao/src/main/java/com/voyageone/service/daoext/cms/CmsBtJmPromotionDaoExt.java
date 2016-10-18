package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    List<MapModel> selectMaxJmHashId(String channelId);

    List<MapModel> selectJmProductHashId(String channelId);

    int updateSumbrandById(int id);

    List<Integer> selectCloseJmPromotionId();

    List<Map<String,Object>> selectCloseJmPromotionSku(Integer jmPromotionId);

    int updatePromotionStatus(@Param("jmPromotionId") int jmPromotionId, @Param("modifier") String modifier);
}
