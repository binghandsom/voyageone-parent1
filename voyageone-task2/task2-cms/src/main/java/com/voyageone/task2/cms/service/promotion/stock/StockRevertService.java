package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库存还原batch
 *
 * @author morse.lu
 * @version 0.0.1, 16/3/29
 */
@Service
public class StockRevertService extends BaseTaskService {


    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "CmsStockRevertJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }
}
