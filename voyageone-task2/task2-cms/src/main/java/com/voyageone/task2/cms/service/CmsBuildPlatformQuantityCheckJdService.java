package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.jd.service.JdSaleService;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Charis on 2016/11/29.
 */
@Service
public class CmsBuildPlatformQuantityCheckJdService extends BaseCronTaskService {

    // 检查商品库存状态
    private static final int CHECKSTOCKNUM = -1; // (无库存)

    @Autowired
    private JdSaleService jdSaleService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformQuantityCheckJdJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        // 循环所有渠道
        if (ListUtils.notNull(channelIdList)) {
            for (String channelId : channelIdList) {

                checkProduct(channelId, CartEnums.Cart.JD.getValue());

                checkProduct(channelId, CartEnums.Cart.JG.getValue());

                checkProduct(channelId, CartEnums.Cart.JGJ.getValue());

                checkProduct(channelId, CartEnums.Cart.JGY.getValue());
            }
        }
    }

    /**
     * 平台商品检查
     *
     * @param channelId 渠道ID
     * @param cartId    平台ID
     */
    public void checkProduct(String channelId, int cartId) {
        List<String> onSaleWareIdList = null;
        ShopBean shopProp = null;

        try {
            // 获取店铺配置信息
            shopProp = Shops.getShop(channelId, cartId);
            if (shopProp == null) {
                return;
            }
            $info("获取店铺信息成功! [channelId:%s] [cartId:%s]", channelId, cartId);

            // 获取该店铺下该平台在售且总库存为0的商品WareId
            onSaleWareIdList = jdSaleService.getJdWareIdList(channelId, String.valueOf(cartId),
                    CmsConstants.PlatformStatus.OnSale.name(), CHECKSTOCKNUM);

            if (onSaleWareIdList.size() == 0) {
                return;
            }
            $info("获取在售零库存的商品wareId成功! [channelId:%s] [cartId:%s] [零库存商品Count:%s]", channelId, cartId, onSaleWareIdList.size());
        } catch(Exception e) {
            String err = String.format("对该平台在售零库存的商品下架处理失败！[channelId:%s] [cartId:%s] [message:%s]", channelId, cartId, e.getMessage());
            $error(err);
            return;
        }

            // 对在售且总库存为0的商品进行下架处理
        for (String wareId : onSaleWareIdList) {
            try
            {
                jdSaleService.doWareUpdateDelisting(shopProp, Long.parseLong(wareId), true);
                $info("零库存商品下架成功 -> [wareId:%s]", wareId);
            } catch(Exception e) {
                $error("零库存商品下架失败 -> [wareId:%s]%s", wareId, e.getMessage());
            }
        }

    }
}
