package com.voyageone.batch.wms.service.createReport;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.dao.ClientInventoryDao;
import com.voyageone.batch.wms.dao.ClientOrderDao;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by fred on 2015/8/6.
 */
public abstract  class CreateReportBaseService extends BaseTaskService {


    @Autowired
    protected ClientInventoryDao clientInventoryDao;

    @Autowired
    protected ClientOrderDao clientOrderDao;


    public abstract void doRun(String channelId,String taskName);

}
