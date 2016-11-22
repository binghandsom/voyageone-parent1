package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.daoext.cms.CmsBtShelvesProductHistoryDaoExt;
import com.voyageone.service.fields.cms.CmsBtShelvesProductHistoryModelStatus;
import com.voyageone.service.fields.cms.CmsBtShelvesProductModelStatus;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesProductHistoryModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
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
     */
    @VOTransactional
    public void batchInsert(Integer shelvesId, List<String> productCode, Integer status, String modifier) {
        if (CollectionUtils.isNotEmpty(productCode)) {
            List<List<String>> splitCode = CommonUtil.splitList(productCode,100);
            splitCode.forEach(strings -> cmsBtShelvesProductHistoryDaoExt.batchInsert(shelvesId,strings,status, modifier));
        }
    }
}
