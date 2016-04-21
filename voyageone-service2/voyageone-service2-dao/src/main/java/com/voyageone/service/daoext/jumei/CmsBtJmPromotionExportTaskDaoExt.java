package com.voyageone.service.daoext.jumei;
import com.voyageone.service.model.jumei.CmsBtJmPromotionExportTaskModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CmsBtJmPromotionExportTaskDaoExt {
    public List<CmsBtJmPromotionExportTaskModel> getByPromotionId(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);
}
