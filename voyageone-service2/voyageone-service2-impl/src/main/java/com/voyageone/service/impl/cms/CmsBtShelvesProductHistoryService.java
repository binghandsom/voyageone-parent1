package com.voyageone.service.impl.cms;

import com.voyageone.service.daoext.cms.CmsBtShelvesProductHistoryDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesProductHistoryModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/21.
 */
@Service
public class CmsBtShelvesProductHistoryService extends BaseService {

    @Autowired
    private CmsBtShelvesProductHistoryDaoExt cmsBtShelvesProductHistoryDaoExt;

    /**
     * 批量插入
     * @param models
     */
    void baseInsert(List<CmsBtShelvesProductHistoryModel> models) {
        if (CollectionUtils.isNotEmpty(models)) {
            cmsBtShelvesProductHistoryDaoExt.batchInsert(models);
        }
    }
}
