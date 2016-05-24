package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chuanyu.liang on 16/5/29.
 *
 * @version 2.0.0
 */
    @Repository
public interface CmsMtImageCreateFileDaoExt {
    List<CmsMtImageCreateFileModel> selectByTaskId(int taskId);
}
