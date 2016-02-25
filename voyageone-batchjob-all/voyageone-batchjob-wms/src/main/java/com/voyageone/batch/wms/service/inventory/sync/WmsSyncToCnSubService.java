package com.voyageone.batch.wms.service.inventory.sync;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.common.components.cn.CnInventoryService;
import com.voyageone.common.components.cn.beans.InventoryUpdateBean;
import com.voyageone.common.components.cn.enums.InventorySynType;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存同步的子服务，用于提供同步到独立域名的功能
 * <br />
 * Created by jonas on 15/6/10.
 */
@Service
public class WmsSyncToCnSubService extends WmsSyncInventoryBaseService {

    @Autowired
    private CnInventoryService cnInventoryService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 CN 的内容。但暂时搁置
    }

    public void syncSite(List<InventorySynLogBean> inventorySynLogBeans, ShopBean shopBean) {

        $info("-----" + shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步开始");

        List<InventoryUpdateBean> infoBeans = new ArrayList<>();

        for (InventorySynLogBean inventorySynLogBean : inventorySynLogBeans) {
            InventoryUpdateBean bean = new InventoryUpdateBean();

            bean.setQty(inventorySynLogBean.getQty());
            bean.setSku(inventorySynLogBean.getSku());

            infoBeans.add(bean);
        }
        $info("-----" +shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存需要同步件数：" + infoBeans.size());
        String res = null;

        if (infoBeans.size() > 0 ) {
            try {
                res = cnInventoryService.UpdateInventory(InventorySynType.INCREMENT, shopBean, infoBeans);

                if (res != null && res.contains("Success")) {

                    for (InventorySynLogBean inventorySynLogBean : inventorySynLogBeans)

                        movePass(inventorySynLogBean);
                }
            } catch (Exception e) {
                $info("-----" +shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步失败：" + e);
                logFailRecord(e, shopBean);
            }

        }

        $info("-----" +shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步结束。" + StringUtils.null2Space2(res));
    }
}
