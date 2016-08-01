package com.voyageone.task2.vms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.vms.VmsBtDataAmountDao;
import com.voyageone.service.daoext.vms.VmsBtOrderDetailDaoExt;
import com.voyageone.service.daoext.vms.VmsBtShipmentDaoExt;
import com.voyageone.service.model.vms.VmsBtDataAmountModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 取得Home页面的显示信息
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsGetHomeInfoService extends BaseTaskService {

    @Autowired
    private VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;

    @Autowired
    private VmsBtShipmentDaoExt vmsBtShipmentDaoExt;

    @Autowired
    private VmsBtDataAmountDao vmsBtDataAmountDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsGetHomeInfoJob";
    }


    /**
     * 取得Home页面的显示信息
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {

            // 按渠道进行处理
            for (String orderChannelID : orderChannelIdList) {
                getHomeInfo(orderChannelID);
            }
        }
    }

    /**
     * 取得Home页面的显示信息
     *
     * @param channelId 渠道
     */
    public void getHomeInfo(String channelId) {

        $info("取得Home页面显示信息开始，channelId" + channelId);

        // 取得"New Order"件数
        // 现在时点
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        Map<String, Object> param = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("status", VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN);
            put("orderDateTo", now);
        }};
        int countOrder = vmsBtOrderDetailDaoExt.countOrder(param);
        int countSku = vmsBtOrderDetailDaoExt.countSku(param);

        // 更新"New Order"件数
        updateDataAmount(channelId, VmsConstants.DataAmount.NEW_ORDER_COUNT, String.valueOf(countOrder), "Today's New Order");
        updateDataAmount(channelId, VmsConstants.DataAmount.NEW_SKU_COUNT, String.valueOf(countSku), "Today's New Sku");

        // 取得"Receive Error"件数（最近10天）
        Map<String, Object> param1 = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("status", VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.RECEIVE_ERROR);
            put("shippedDateFrom", DateTimeUtil.addDays(now, -10));
            put("shippedDateTo", now);
        }};

        int countReceiveErrorShipment = vmsBtShipmentDaoExt.count(param1);

        // 更新"Receive Error"件数（最近10天）
        updateDataAmount(channelId, VmsConstants.DataAmount.RECEIVE_ERROR_SHIPMENT_COUNT, String.valueOf(countReceiveErrorShipment), "Receive Error Shipment");

        $info("取得Home页面显示信息完了，channelId" + channelId);
    }

    /**
     * 更新vms_bt_data_amount信息
     *
     * @param channelId 渠道
     * @param amountName 统计名称
     */
    private void updateDataAmount(String channelId, String amountName, String amountValue, String comment) {

        Map<String, Object> param = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("amountName", amountName);
        }};
        List<VmsBtDataAmountModel> models = vmsBtDataAmountDao.selectList(param);
        // 更新的情况
        if (models.size() > 0) {
            VmsBtDataAmountModel model = models.get(0);
            model.setAmountVal(amountValue);
            model.setModifier(getTaskName());
            model.setModified(null);
            vmsBtDataAmountDao.update(model);
        } else {
            // 新建的情况
            VmsBtDataAmountModel model = new VmsBtDataAmountModel();
            model.setChannelId(channelId);
            model.setAmountName(amountName);
            model.setAmountVal(amountValue);
            model.setComment(comment);
            model.setCreater(getTaskName());
            vmsBtDataAmountDao.insert(model);
        }

    }
}