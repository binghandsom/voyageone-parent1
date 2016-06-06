package com.voyageone.task2.cms.service.product;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 从oms系统导入产品前90天订单信息,统计销量数据
 *
 * @author jason.jiang on 2016/05/24
 * @version 2.0.0
 */
@Service
public class CmsImportOrdersHisInfoService extends BaseTaskService {

    @Autowired
    private CmsCopyOrdersInfoService cmsCopyOrdersInfoService;
    @Autowired
    private CmsFindProdOrdersInfoService cmsFindProdOrdersInfoService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsImportOrdersHisInfoJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 从oms系统导入产品前90天订单信息
        cmsCopyOrdersInfoService.copyOrdersInfo(getTaskName());
        // 统计销售数据
        cmsFindProdOrdersInfoService.onStartup(taskControlList);
    }

}
