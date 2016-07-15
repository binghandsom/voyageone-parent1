package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtProductStatusHistoryModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtProductStatusHistoryDaoExt {
    void insertList(@Param("list") List<CmsBtProductStatusHistoryModel> list);

    List selectPage(Map<String, Object> param);

    long selectCount(Map<String, Object> param);
}
