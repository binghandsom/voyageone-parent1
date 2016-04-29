package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.util.MapModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionDaoExt {
    List<MapModel> getListByWhere(Map<String, Object> map);

    /**
     * 获取相关渠道的可用promotions
     * @param channelId
     * @return
     */
    List<MapModel> getActivesOfChannel(String channelId);

}
