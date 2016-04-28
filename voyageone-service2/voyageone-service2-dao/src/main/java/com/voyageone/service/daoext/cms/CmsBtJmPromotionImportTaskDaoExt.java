package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionImportTaskModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtJmPromotionImportTaskDaoExt {
     public List<CmsBtJmPromotionImportTaskModel> getByPromotionId(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);
    }
