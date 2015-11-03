package com.voyageone.batch.ims.service;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.TipItemPromDTO;
import com.taobao.api.domain.TipSkuPromUnitDTO;
import com.taobao.api.response.ItemSkusGetResponse;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.ims.dao.PromotionDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbItemService;
import com.voyageone.common.components.tmall.TbPromotionService;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private TbPromotionService tbPromotionService;

    @Autowired
    private TbItemService tbItemService;

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

        for (TaskControlBean taskControl : taskControlList) {
            if ("order_channel_id".equals(taskControl.getCfg_name())) {
                String channelId = taskControl.getCfg_val1();

                // 取得Config信息
                List<ShopConfigBean> shopConfig = ShopConfigs.getConfigs(channelId, CartEnums.Cart.TG.getId(), ShopConfigEnums.Name.promotion_id);

                // Cfg_val2 = 1的场合才执行
                if (shopConfig != null && "1".equals(shopConfig.get(0).getCfg_val2())) {
                    Long promotionId = Long.parseLong(shopConfig.get(0).getCfg_val1());
                    // 加入线程池
                    executor.execute(() -> updatePromotion(channelId, CartEnums.Cart.TG.getId(), promotionId));
                }
            }
        }
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
     *
     * @param channelId
     * @param cartId
     * @param promotionId
     */
    private void updatePromotion(String channelId, String cartId, Long promotionId) {
        List<Map> items = promotionDao.getPromotionItem(channelId, cartId);
        // 取得shop信息
        ShopBean shopBean = ShopConfigs.getShop(channelId, cartId);
        List<Map> succeedProduct = new ArrayList<>();
        Map<String, List<Map>> failProduct = new HashMap<>();
        items.forEach(item -> {
            TaobaoResponse response;
            try {
                // 根据商品ID列表获取SKU信息 因为需要知道天猫的SKU的ID
                response = tbItemService.getSkuInfo(shopBean, item.get("num_iid").toString(), "sku_id,num_iid,outer_id");

                ItemSkusGetResponse skuids = (ItemSkusGetResponse) response;
                if (skuids.getSkus() == null) {
                    throw new ApiException("numiid 不存在");
                }

                TipItemPromDTO tipItemPromDTO = new TipItemPromDTO();
                tipItemPromDTO.setCampaignId(promotionId);
                tipItemPromDTO.setItemId(Long.parseLong(item.get("num_iid").toString()));
                List<TipSkuPromUnitDTO> tipSkuPromUnitDTOs = new ArrayList<TipSkuPromUnitDTO>();
                List<Map> productList = (List<Map>) item.get("productList");
                for (Map product : productList) {
                    List<Map> skuList = (List<Map>) product.get("skuList");
                    List<Long> cnPriceFinalRmb = new ArrayList<Long>();
                    // 遍历code下面的所有SKU
                    skuList.forEach(map -> {
                        TipSkuPromUnitDTO tipSkuPromUnitDTO = new TipSkuPromUnitDTO();
                        tipSkuPromUnitDTO.setDiscount(Long.parseLong(map.get("cnPriceFinalRmb").toString()));
                        // 获取SKU对已TM的SKUID
                        skuids.getSkus().forEach(sku -> {
                            if (sku.getOuterId().equalsIgnoreCase(map.get("sku").toString())) {
                                tipSkuPromUnitDTO.setSkuId(sku.getSkuId());
                            }
                        });
                        tipSkuPromUnitDTOs.add(tipSkuPromUnitDTO);
                    });
                    tipItemPromDTO.setSkuLevelProms(tipSkuPromUnitDTOs);
                }

                // 调用天猫特价宝
                response = tbPromotionService.updatePromotion(shopBean, tipItemPromDTO);
                // 成功的场合把product_id保存起来
                if (response != null && response.getErrorCode() == null) {
                    succeedProduct.addAll((List<Map>) item.get("productList"));
                } else {
                    // 失败的场合 错误信息取得
                    String fail = "";
                    if (response == null) {
                        fail = "超时";
                    } else {
                        fail = response.getSubMsg();
                    }
                    if (failProduct.get(fail) == null) {
                        List<Map> temp = new ArrayList<>();
                        temp.addAll((List<Map>) item.get("productList"));
                        failProduct.put(fail, temp);
                    } else {
                        failProduct.get(fail).addAll((List<Map>) item.get("productList"));
                    }
                }
            } catch (Exception e) {
                if (failProduct.get(e.getMessage()) == null) {
                    List<Map> temp = new ArrayList<>();
                    temp.addAll((List<Map>) item.get("productList"));
                    failProduct.put(e.getMessage(), temp);
                } else {
                    failProduct.get(e.getMessage()).addAll((List<Map>) item.get("productList"));
                }
                logger.info(e.getMessage());
            }
        });
        // 把成功的产品更新数据库
        if (succeedProduct.size() > 0) {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("channelId", channelId);
            parameter.put("cartId", cartId);
            parameter.put("products", succeedProduct);
            parameter.put("priceStatus", "1");
            parameter.put("addStatus", "1");
            parameter.put("promotionFaildComment", "");
            promotionDao.updatePromotionStatus(parameter);
        }

        // 把错误信息更新到数据库中
        failProduct.forEach((s, integers) -> {
            if (integers.size() > 0) {
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("channelId", channelId);
                parameter.put("cartId", cartId);
                parameter.put("products", integers);
                parameter.put("priceStatus", "2");
                parameter.put("addStatus", "2");
                parameter.put("promotionFaildComment", s);
                promotionDao.updatePromotionStatus(parameter);
            }
        });
    }

}
