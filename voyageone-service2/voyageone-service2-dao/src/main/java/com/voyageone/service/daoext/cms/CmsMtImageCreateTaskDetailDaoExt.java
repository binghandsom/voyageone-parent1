package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dell on 2016/5/3.
 */
@Repository
public interface CmsMtImageCreateTaskDetailDaoExt {
    public void insertList(@Param("list") List<CmsMtImageCreateTaskDetailModel> list);
}
