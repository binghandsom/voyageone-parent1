package com.voyageone.batch.wms.service.inventory.sync;

import com.taobao.api.ApiException;
import com.taobao.api.response.ItemQuantityUpdateResponse;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.common.components.tmall.bean.ItemQuantityBean;
import com.voyageone.common.components.tmall.TbInventoryService;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库存同步的子服务，用于提供同步到淘宝或天猫的功能
 * <br />
 * Created by jonas on 15/6/10.
 */
@Service
public class WmsSyncToTaobaoSubService extends WmsSyncInventoryBaseService {

    @Autowired
    private TbInventoryService tbInventoryService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 TM 的内容。但暂时搁置
    }

    public void syncTaobao(List<InventorySynLogBean> inventorySynLogBeans, ShopBean shopBean) {

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步开始");
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存需要同步件数："+inventorySynLogBeans.size());

        for (InventorySynLogBean inventorySynLogBean : inventorySynLogBeans)

            syncTaobao(inventorySynLogBean, shopBean);

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步结束");
    }

    private void syncTaobao(InventorySynLogBean inventorySynLogBean, ShopBean shopBean) {

        ItemQuantityBean itemQuantityBean = getItemQuantityBean(inventorySynLogBean);

        // 如果获取不到内容，则说明参数有问题，不调用 api 进行库存同步
        // 只记录这个内容，进行提示。
        if (itemQuantityBean == null) {
            logFailRecord("参数有错误", inventorySynLogBean);
            return;
        }

        // num_iid 违法的时候，将该记录进行忽略处理（默认是0，该记录不作处理）
//        if (itemQuantityBean.getNum_iid() < 1) {
//            moveIgnore(inventorySynLogBean, "不是正确的 num_iid");
//            return;
//        }

        ItemQuantityUpdateResponse res = null;
        try {
            res = tbInventoryService.itemQuantityUpdate(shopBean, itemQuantityBean);
        } catch (ApiException ae) {
            logFailRecord(ae, inventorySynLogBean);
        } catch (Exception e) {
            logFailRecord(e, inventorySynLogBean);
            return;
        }

        if (res == null) return;

        // 有返回内容，但没有错误代码，就认为是成功调用
        if (StringUtils.isEmpty(res.getErrorCode())) {

            // 成功后，迁移数据到历史表
            movePass(inventorySynLogBean);
            return;
        }

        switch (res.getSubCode()) {
            // 商品id对应的商品已经被删除
            case "isv.item-is-delete:invalid-numIid-or-iid":
            case "isv.item-is-delete:invalid-numIid-or-iid-tmall":
            // 商品id对应的商品不存在
            case "isv.item-not-exist:invalid-numIid-or-iid":
            case "isv.item-not-exist:invalid-numIid-or-iid-tmall":
            // 传入的sku的属性找不到对应的sku记录
            case "isv.invalid-parameter:sku-properties":
            case "isv.invalid-parameter:sku-properties-tmall":
            // 库存更新失败（不允许更新）
            case "isv.item-quantity-item-update-service-error-tmall":
            // 预售商品，不能全量更新库存，只能增量更新库存;
            case "isv.item-is-presale:invalid-presale-tags-id-tmall":
                moveIgnore(inventorySynLogBean, res.getSubMsg());

                logFailRecord(res.getSubMsg(), inventorySynLogBean);
                break;

            default:
                String failMessage = String.format("code: %s; sub_code: %s; msg: %s; sub_msg: %s;",
                        res.getErrorCode(),
                        res.getSubCode(),
                        res.getMsg(),
                        res.getSubMsg());

                logFailRecord(failMessage, inventorySynLogBean);
                break;
        }
    }

    private ItemQuantityBean getItemQuantityBean(InventorySynLogBean inventorySynLogBean) {

        String sku = inventorySynLogBean.getSku();

        // 返回为空，等待后续做记录处理，这里不同于下面的 num_iid 处理
        if (StringUtils.isEmpty(sku)) {
            return null;
        }

        ItemQuantityBean itemQuantityBean = new ItemQuantityBean();

        String num_iid = inventorySynLogBean.getNum_iid();

        // 如果 num_iid 不是数字，则标记为 -1，等待后续做忽略处理
        if (!StringUtils.isNumeric(num_iid))
            itemQuantityBean.setNum_iid(-1);
        else
            itemQuantityBean.setNum_iid(Long.valueOf(num_iid));

        itemQuantityBean.setQuantity(inventorySynLogBean.getQty());

        // JC暂定特殊处理
//        if (inventorySynLogBean.getOrder_channel_id().equals(ChannelConfigEnums.Channel.JC.getId())) {
//            sku = sku.toUpperCase();
//        }

        // 判断是按Product还是按SKU来同步库存
        if (WmsConstants.QuantityUpdateType.PRODUCT.equals(inventorySynLogBean.getQuantity_update_type())) {
            itemQuantityBean.setSku("");
        } else {
            itemQuantityBean.setSku(sku);
        }

        // 设置，是全量还是增量更新
        itemQuantityBean.setIsTotal(WmsConstants.InventorySynType.FULL.equals(inventorySynLogBean.getSyn_type()) || WmsConstants.InventorySynType.FORCE.equals(inventorySynLogBean.getSyn_type()));

        return itemQuantityBean;
    }
}
