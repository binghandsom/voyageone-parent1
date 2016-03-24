package com.voyageone.task2.cms.service;

import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.TipItemPromDTO;
import com.taobao.api.domain.TipPromUnitDTO;
import com.taobao.api.domain.TipSkuPromUnitDTO;
import com.taobao.api.response.ItemSkusGetResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.PromotionDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbItemService;
import com.voyageone.common.components.tmall.TbPromotionService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/2/16.
 * @version 2.0.0
 */
@Service
public class CmsPromotrionService extends BaseTaskService {
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsPromotionJob";
    }

    @Autowired
    private PromotionDao promotionDao;

    @Autowired
    private TbItemService tbItemService;

    @Autowired
    private TbPromotionService tbPromotionService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        for (TaskControlBean taskControlBean : taskControlList) {
            if ("order_channel_id".equalsIgnoreCase(taskControlBean.getCfg_name())) {
                String channelId  = taskControlBean.getCfg_val1();
                String cartId  = taskControlBean.getCfg_val2();
                updatePromotion(channelId,cartId);
            }
        }
    }

    private void updatePromotion(String channelId, String cartId) {
        List<Map> items = promotionDao.getPromotionItem(channelId, cartId);
        if (items.size() == 0) return;
        // 取得shop信息
        ShopBean shopBean = ShopConfigs.getShop(channelId, cartId);
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
        List<String> succeedProduct = new ArrayList<>();
        Map<String, List<String>> failProduct = new HashMap<>();
        items.forEach(item -> {
            TaobaoResponse response;
            try {
                Long promotionId = Long.parseLong(item.get("tejiabaoId").toString());
                // 先删除之前的提价商品
                tbPromotionService.removePromotion(shopBean, Long.parseLong(item.get("num_iid").toString()), promotionId);

                TipItemPromDTO tipItemPromDTO = new TipItemPromDTO();
                tipItemPromDTO.setCampaignId(promotionId);
                tipItemPromDTO.setItemId(Long.parseLong(item.get("num_iid").toString()));
                System.out.println(item.get("num_iid") + "开始");
                List<Map> productList = (List<Map>) item.get("productList");

                // 根据商品ID列表获取SKU信息 因为需要知道天猫的SKU的ID
                response = tbItemService.getSkuInfo(shopBean, item.get("num_iid").toString(), "sku_id,num_iid,outer_id");
                ItemSkusGetResponse skuids = (ItemSkusGetResponse) response;
                // 商品里有SKU的场合 更新特价的时候以SKU为单位更新 （因为TM部分类目下没有SKU 那就用ITEM单位更新）
                if (skuids.getSkus() != null) {
                    System.out.println("skuids" + skuids.getSkus().size() + "");
                    List<TipSkuPromUnitDTO> tipSkuPromUnitDTOs = new ArrayList<TipSkuPromUnitDTO>();
                    // 遍历该num_iid下所有的SKU
                    for (Map product : productList) {
                        List<Map> skuList = (List<Map>) product.get("skuList");
                        // 遍历code下面的所有SKU
                        skuList.forEach(map -> {
                            // 价格与MSRP价格不一致的sku才加特价宝
                            if (!StringUtils.isEmpty((String) map.get("promotionPrice"))) {
                                TipSkuPromUnitDTO tipSkuPromUnitDTO = new TipSkuPromUnitDTO();
                                tipSkuPromUnitDTO.setDiscount(Long.parseLong(map.get("promotionPrice").toString()) * 100);
                                // 获取SKU对已TM的SKUID
                                skuids.getSkus().forEach(sku -> {
                                    if (sku.getOuterId() != null) {
                                        if (sku.getOuterId().equalsIgnoreCase(map.get("sku").toString())) {
                                            tipSkuPromUnitDTO.setSkuId(sku.getSkuId());
                                        }
                                    }
                                });
                                if (tipSkuPromUnitDTO.getSkuId() != null && tipSkuPromUnitDTO.getSkuId() > 0) {
                                    tipSkuPromUnitDTOs.add(tipSkuPromUnitDTO);
                                }
                            }
                        });
                    }
                    if(tipSkuPromUnitDTOs.size() > 0){
                        tipItemPromDTO.setSkuLevelProms(tipSkuPromUnitDTOs);
                    }

                } else {
                    // ITEM单位更新
                    List<Map> skuList = (List<Map>) productList.get(0).get("skuList");
                    if (!StringUtils.isEmpty((String) skuList.get(0).get("promotionPrice"))) {
                        TipPromUnitDTO tipPromUnitDTO = new TipPromUnitDTO();
                        tipPromUnitDTO.setDiscount(Long.parseLong(skuList.get(0).get("promotionPrice").toString()) * 100);
                        tipItemPromDTO.setItemLevelProm(tipPromUnitDTO);
                    }
                }
                // 调用天猫特价宝
                if(tipItemPromDTO.getSkuLevelProms() != null || tipItemPromDTO.getItemLevelProm() != null){
                    response = tbPromotionService.updatePromotion(shopBean, tipItemPromDTO);
                }else {
                    logger.info("活动价格为空可能该商品已从活动中删除");
                    throw new BusinessException("活动价格为空可能该商品已从活动中删除");
                }

                // 成功的场合把product_id保存起来
                if (response != null && response.getErrorCode() == null) {
                    succeedProduct.add(item.get("promotionId").toString() + "," + item.get("num_iid").toString());
                } else {
                    // 失败的场合 错误信息取得
                    String fail = "";
                    if (response == null) {
                        System.out.println("超时");
                        fail = "超时";
                    } else {
                        System.out.println("getSubMsg" + response.getSubMsg());
                        fail = response.getSubMsg();
                    }
                    if (failProduct.get(fail) == null) {
                        List<String> temp = new ArrayList<>();
                        temp.add(item.get("promotionId").toString() + "," + item.get("num_iid").toString());
                        failProduct.put(fail, temp);
                    } else {
                        failProduct.get(fail).add((item.get("promotionId").toString() + "," + item.get("num_iid").toString()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (failProduct.get(e.getMessage()) == null) {
                    List<String> temp = new ArrayList<>();
                    temp.add(item.get("promotionId").toString() + "," + item.get("num_iid").toString());
                    failProduct.put(e.getMessage(), temp);
                } else {
                    failProduct.get(e.getMessage()).add(item.get("promotionId").toString() + "," + item.get("num_iid").toString());
                }
                logger.info(e.getMessage());
            }
        });
        // 把成功的产品更新数据库
        succeedProduct.forEach(s -> {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("promotionId", s.split(",")[0]);
            parameter.put("taskType", 0);
            parameter.put("num_iid", s.split(",")[1]);
            parameter.put("synFlg", "2");
            parameter.put("errMsg", "");
            parameter.put("modifier", getTaskName());
            promotionDao.updatePromotionStatus(parameter);
        });


        // 把错误信息更新到数据库中
        failProduct.forEach((s, numIids) -> {
            numIids.forEach(numIid -> {
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("promotionId", numIid.split(",")[0]);
                parameter.put("taskType", 0);
                parameter.put("num_iid", numIid.split(",")[1]);
                parameter.put("synFlg", "3");
                parameter.put("errMsg", s);
                parameter.put("modifier", getTaskName());
                promotionDao.updatePromotionStatus(parameter);
            });
        });
    }
}