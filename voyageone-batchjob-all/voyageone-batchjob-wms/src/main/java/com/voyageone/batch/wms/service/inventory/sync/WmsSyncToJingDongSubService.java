package com.voyageone.batch.wms.service.inventory.sync;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.response.ware.WareSkuStockUpdateResponse;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.common.components.jd.JdInventoryService;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库存同步的子服务，用于提供同步到京东的功能
 * <br />
 * Created by jonas on 15/6/10.
 */
@Service
public class WmsSyncToJingDongSubService extends WmsSyncInventoryBaseService {

    @Autowired
    private JdInventoryService jdInventoryService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JD 的内容。但暂时搁置
    }

    public void syncJingdong(List<InventorySynLogBean> inventorySynLogBeans, ShopBean shopBean) {

        $info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步开始");
        $info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存需要同步件数："+inventorySynLogBeans.size());

        for (InventorySynLogBean inventorySynLogBean : inventorySynLogBeans)

            syncJingdong(inventorySynLogBean, shopBean);

        $info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步结束");
    }

    private void syncJingdong(InventorySynLogBean inventorySynLogBean, ShopBean shopBean) {

        WareSkuStockUpdateResponse res = null;

        try {
            res = jdInventoryService.skuStockUpdate(shopBean, inventorySynLogBean.getSku(), String.valueOf(inventorySynLogBean.getQty()));
        } catch (JdException e) {
            logFailRecord(e, inventorySynLogBean);
            return;
        }

        // 有返回内容，但没有错误代码，就认为是成功调用
        if (res != null) {

            if ("0".equals(res.getCode())) {
                // 成功后，迁移数据到历史表
                movePass(inventorySynLogBean);
                return;
            }

            String failMessage = String.format("msg: %s; desc: %s;",
                    res.getMsg(),
                    res.getZhDesc());

            // 失败的话，记录失败的信息
            logFailRecord(failMessage, inventorySynLogBean);
            return;
        }

        logFailRecord(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步失败，错误未知", inventorySynLogBean);
    }
}
