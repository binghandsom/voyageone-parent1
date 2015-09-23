package com.voyageone.batch.wms.service.inventory;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.modelbean.ViwLogicInventoryBean;
import com.voyageone.common.components.cn.CnInventoryService;
import com.voyageone.common.components.cn.beans.InventoryUpdateBean;
import com.voyageone.common.components.cn.enums.InventorySynType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 刷新斯伯丁的所有商品库存
 *
 * Created by jonas on 15/6/8.
 */
@Service
public class WmsReFlushInventoryService extends BaseTaskService {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private CnInventoryService cnInventoryService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "wmsReFlushInventory";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 现在暂时只有斯伯丁使用
        // 所以可以暂时不考虑国内外的情况
        // 但已经预留参数

        ShopBean shopBean = ShopConfigs.getShop(ChannelConfigEnums.Channel.SP.getId(), "25");

        reFlushCn(shopBean);
    }

    private void reFlushCn(ShopBean shopBean) {

        List<ViwLogicInventoryBean> all = inventoryDao.getLogicInventory(getTaskName(), shopBean.getOrder_channel_id(), shopBean.getCart_id());
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存刷新件数："+all.size());

        List<InventoryUpdateBean> reqBeans = convert(all, shopBean);
        String res = null;

        if (reqBeans.size() > 0) {
            try {
                res = cnInventoryService.UpdateInventory(InventorySynType.INCREMENT, shopBean, reqBeans);

                if (res == null || !res.contains("Success")) {
                    logIssue(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存刷新失败", shopBean);
                }
            } catch (UnsupportedEncodingException e) {
                logIssue(e);
            }
        }
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存刷新结束。" + StringUtils.null2Space2(res));
    }

    /**
     * 转换数据到库存，根据 shop 检查是否是国外店铺
     */
    private List<InventoryUpdateBean> convert(List<ViwLogicInventoryBean> all, ShopBean shopBean) {

        // 参数预留
        // 但不进行实际检查

        List<InventoryUpdateBean> list = new ArrayList<>();

        for (ViwLogicInventoryBean bean: all) {
            InventoryUpdateBean inventoryUpdateBean = new InventoryUpdateBean();

            inventoryUpdateBean.setSku(bean.getSku());
            inventoryUpdateBean.setQty(bean.getQty_china_logic());

            list.add(inventoryUpdateBean);
        }

        return list;
    }
}
