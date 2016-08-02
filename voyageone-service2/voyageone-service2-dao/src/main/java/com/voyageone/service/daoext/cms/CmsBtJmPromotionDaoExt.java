package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.util.MapModel;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionDaoExt {
    List<MapModel> selectListByWhere(Map<String, Object> map);

    /**
     * 获取相关渠道的可用promotions
     * @return
     */
    List<MapModel> selectActivesOfChannel(Map<String, Object> map);

    List<CmsBtJmPromotionModel> selectEndList(Date nowDate);

    List<MapModel> selectMaxJmHashId(String channelId);
}
