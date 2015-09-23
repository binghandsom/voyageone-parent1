package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.service.WmsSetPhysicsInventoryService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 物理库存计算
 * 根据入库，出库来进行对应库存增减
 *
 * @author Jack
 */
@Component("wmsSetPhysicsInventoryTask")
public class WmsSetPhysicsInventoryJob   extends BaseTaskJob  {

    @Autowired
    WmsSetPhysicsInventoryService wmsSetPhysicsInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSetPhysicsInventoryService;
    }
}
