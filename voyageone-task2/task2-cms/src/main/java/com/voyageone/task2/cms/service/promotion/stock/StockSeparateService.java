package com.voyageone.task2.cms.service.promotion.stock;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.dao.cms.CmsBtBeatInfoDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.service.model.cms.CmsBtBeatInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存隔离batch
 *
 * @author morse.lu
 * @version 0.0.1, 16/3/29
 */
@Service
public class StockSeparateService extends BaseTaskService {

    @Autowired
    private StockInfoService stockInfoService;

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;

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
        return "CmsStockSeparateJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("status", stockInfoService.STATUS_WAITING_SEPARATE);
        List<Map<String, Object>> resultData = cmsBtStockSeparateItemDao.selectStockSeparateItem(param);

        // 取得可用库存
//        String usableStock = stockInfoService.getUsableStock();
    }
}
