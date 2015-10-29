package com.voyageone.batch.ims.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.TipItemPromDTO;
import com.taobao.api.domain.TipPromUnitDTO;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.dao.PromotionDao;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbPromotionService;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by james.li on 2015/10/29.
 */
@Repository
public class ImsPromotionService extends BaseTaskService {
    @Autowired
    private PromotionDao promotionDao;

    private final static Integer ThreadPoolCount = 3;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.IMS;
    }

    @Override
    public String getTaskName() {
        return "ImsPromotionTask";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 开启线程池
        ExecutorService executor = Executors.newFixedThreadPool(ThreadPoolCount);

        taskControlList.forEach(taskControl -> {
                    if ("order_channel_id".equals(taskControl.getCfg_name())) {
                        String channelId = taskControl.getCfg_val1();

                        // 取得Config信息
                        List<ShopConfigBean> shopConfig = ShopConfigs.getConfigs(channelId, CartEnums.Cart.TG.getId(), ShopConfigEnums.Name.promotion_id);

                        // Cfg_val2 = 1的场合才执行
                        if (shopConfig != null && "1".equals(shopConfig.get(0).getCfg_val2())) {
                            Long promotionId = Long.parseLong(shopConfig.get(0).getCfg_val1());
                            executor.execute(() -> postPromotion(channelId, CartEnums.Cart.TG.getId(), promotionId));
                        }
                    }
                }
        );
        Thread.sleep(1000);
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            Thread.sleep(1000);
        }
    }

    /**
     * 调用天猫特价宝API
     * @param channelId
     * @param cartId
     * @param promotionId
     */
    private void postPromotion(String channelId, String cartId, Long promotionId) {
        List<Map> items = promotionDao.getPromotionItem(channelId, cartId);
        // 取得shop信息
        ShopBean shopBean = ShopConfigs.getShop(channelId, cartId);
        List<Integer> succeedProduct = new ArrayList<>();

        items.forEach(item -> {
            TbPromotionService tbPromotionService = new TbPromotionService();
            TipItemPromDTO tipItemPromDTO = new TipItemPromDTO();
            tipItemPromDTO.setCampaignId(promotionId);
            TipPromUnitDTO tipPromUnitDTO = new TipPromUnitDTO();
            tipPromUnitDTO.setDiscount(Long.parseLong(item.get("item").toString()));
            tipItemPromDTO.setItemLevelProm(tipPromUnitDTO);
            tipItemPromDTO.setItemId(Long.parseLong(item.get("num_iid").toString()));
            try {
                boolean status = false;
                if ("0".equalsIgnoreCase(item.get("promotion_add_status").toString())) {
                    status = tbPromotionService.addPromotion(shopBean, tipItemPromDTO);
                } else {
                    status = tbPromotionService.updatePromotion(shopBean, tipItemPromDTO);
                }
                // 成功的场合把product_id保存起来
                if (status) {
                    succeedProduct.add((Integer) item.get("product_id"));
                }
            } catch (ApiException e) {
                e.printStackTrace();
                issueLog.log(e, ErrorType.BatchJob,SubSystem.IMS);
            }
        });
        // 把成功的产品更新数据库
        if (succeedProduct.size() > 0) {
            promotionDao.updatePromotionStatus(channelId, cartId, succeedProduct);
        }
    }
}
