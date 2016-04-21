package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionDaoExt {
    List getListByWhere(Map<String, Object> map);
}
