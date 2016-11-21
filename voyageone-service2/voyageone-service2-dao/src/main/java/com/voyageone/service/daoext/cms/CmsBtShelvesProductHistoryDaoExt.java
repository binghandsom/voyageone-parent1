package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtShelvesProductHistoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/21.
 */
@Repository
public interface CmsBtShelvesProductHistoryDaoExt {

    void batchInsert(List<CmsBtShelvesProductHistoryModel> models);
}
