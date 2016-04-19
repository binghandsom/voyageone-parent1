package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsBtJmPromotionImportTaskModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionImportTaskDaoExt {
     public List<CmsBtJmPromotionImportTaskModel> getByPromotionId(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);
    }
