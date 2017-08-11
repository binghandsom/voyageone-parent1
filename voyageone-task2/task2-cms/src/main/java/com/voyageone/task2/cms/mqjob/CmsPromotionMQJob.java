package com.voyageone.task2.cms.mqjob;

import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.TipItemPromDTO;
import com.taobao.api.domain.TipPromUnitDTO;
import com.taobao.api.domain.TipSkuPromUnitDTO;
import com.taobao.api.response.ItemSkusGetResponse;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.components.tmall.service.TbPromotionService;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPromotionMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.ims.ImsBtProductModel;
import com.voyageone.task2.cms.dao.PromotionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/5/2.
 */
@Service
@VOSubRabbitListener()
public class CmsPromotionMQJob extends TBaseMQCmsSubService<CmsPromotionMQMessageBody> {

    private final
    PromotionDao promotionDao;

    private final
    PromotionService promotionService;

    private final
    TbPromotionService tbPromotionService;

    private final
    TbItemService tbItemService;

    private final
    ImsBtProductDao imsBtProductDao;

    private final
    PlatformPriceService platformPriceService;

    private final
    ProductService productService;

    @Autowired
    public CmsPromotionMQJob(PromotionDao promotionDao, PromotionService promotionService, TbPromotionService tbPromotionService, TbItemService tbItemService, ImsBtProductDao imsBtProductDao, PlatformPriceService platformPriceService, ProductService productService) {
        this.promotionDao = promotionDao;
        this.promotionService = promotionService;
        this.tbPromotionService = tbPromotionService;
        this.tbItemService = tbItemService;
        this.imsBtProductDao = imsBtProductDao;
        this.platformPriceService = platformPriceService;
        this.productService = productService;
    }

    @Override
    public void onStartup(CmsPromotionMQMessageBody messageBody) throws Exception {
        $info(JacksonUtil.bean2Json(messageBody));
        CmsBtPromotionModel cmsBtPromotion = promotionService.getByPromotionId(messageBody.getPromotionId());
        if(cmsBtPromotion == null){
            $info("活动没有找到");
        }
        if (messageBody.getTriggerTime() == null || (messageBody.getTriggerTime() != null && cmsBtPromotion.getTriggerTime() != null && messageBody.getTriggerTime() == cmsBtPromotion.getTriggerTime().getTime())){
            if ("1".equals(cmsBtPromotion.getPromotionType())) {
                updateSalePrice(cmsBtPromotion.getChannelId(), cmsBtPromotion.getCartId(), messageBody.getPromotionId(), messageBody.getSender());
            } else if ("2".equals(cmsBtPromotion.getPromotionType())) {
//                updateTeJiaBaoPromotion(cmsBtPromotion.getChannelId(), cmsBtPromotion.getCartId(), messageBody.getPromotionId());
            }
        }else {
            $info("该条MQ已经失效不需要执行");
        }

    }

    public void updateSalePrice(String channelId, Integer cartId, Integer promotionId, String sender){
        List<Map> items = promotionDao.getPromotionItem(promotionId);
        if (items.size() == 0) return;
        items.forEach(item->{
            List<Map> productList = (List<Map>) item.get("productList");
            for(Map product: productList){
                List<Map> skuList = (List<Map>) product.get("skuList");
                List<BaseMongoMap<String, Object>> skus = new ArrayList<BaseMongoMap<String, Object>>();
                skuList.stream()
                        .filter(sku->sku.get("promotionPrice") != null && !StringUtil.isEmpty(sku.get("promotionPrice").toString()) && Double.parseDouble(sku.get("promotionPrice").toString()) > 0)
                        .forEach(sku->{
                            BaseMongoMap<String, Object> temp = new BaseMongoMap<String, Object>();
                            temp.putAll(sku);
                            skus.add(temp);
                        });
                try {
                    if(skus.size() > 0) {
                        updatePrice(channelId, item.get("orgChanneld").toString(), product.get("product_code").toString(), skus, item.get("num_iid").toString(), cartId);
                    }else{
                        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, (String) product.get("product_code"));
                        platformPriceService.updateSkuPrice(channelId, cartId, cmsBtProductModel, sender);
                    }
                    updateStatus(Integer.parseInt(item.get("promotionId").toString()), item.get("num_iid").toString(), 2, "", null);
                } catch (Exception e) {
                    updateStatus(Integer.parseInt(item.get("promotionId").toString()), item.get("num_iid").toString(), 3, e.getMessage(),product.get("product_code").toString());
                    e.printStackTrace();
                }
            }
        });


    }



    // 特价宝刷价格
    public void updateTeJiaBaoPromotion(String channelId, Integer cartId, Integer promotionId) {
        List<Map> items = promotionDao.getPromotionItem(promotionId);
        if (items.size() == 0) return;
        // 取得shop信息
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        if(!shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM.getId())) return;
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
//        List<String> succeedProduct = new ArrayList<>();
//        Map<String, List<String>> failProduct = new HashMap<>();
        items.forEach(item -> {
            TaobaoResponse response;
            try {
                Long tejiabaoId = Long.parseLong(item.get("tejiabaoId").toString());
                // 先删除之前的提价商品
                tbPromotionService.removePromotion(shopBean, Long.parseLong(item.get("num_iid").toString()), tejiabaoId);

                TipItemPromDTO tipItemPromDTO = new TipItemPromDTO();
                tipItemPromDTO.setCampaignId(tejiabaoId);
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
                                tipSkuPromUnitDTO.setDiscount(Math.round(Double.parseDouble(map.get("promotionPrice").toString()) * 100));
                                // 获取SKU对已TM的SKUID
                                skuids.getSkus().forEach(sku -> {
                                    if (sku.getOuterId() != null) {
                                        if (sku.getOuterId().equalsIgnoreCase(map.get("skuCode").toString())) {
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
                    if (tipSkuPromUnitDTOs.size() > 0) {
                        tipItemPromDTO.setSkuLevelProms(tipSkuPromUnitDTOs);
                    }

                } else {
                    // ITEM单位更新
                    List<Map> skuList = (List<Map>) productList.get(0).get("skuList");
                    if (!StringUtils.isEmpty((String) skuList.get(0).get("promotionPrice"))) {
                        TipPromUnitDTO tipPromUnitDTO = new TipPromUnitDTO();
                        tipPromUnitDTO.setDiscount(Math.round(Double.parseDouble(skuList.get(0).get("promotionPrice").toString()) * 100));
                        tipItemPromDTO.setItemLevelProm(tipPromUnitDTO);
                    }
                }
                // 调用天猫特价宝
                if (tipItemPromDTO.getSkuLevelProms() != null || tipItemPromDTO.getItemLevelProm() != null) {
                    response = tbPromotionService.updatePromotion(shopBean, tipItemPromDTO);
                } else {
                    $info("活动价格为空可能该商品已从活动中删除");
                    throw new BusinessException("活动价格为空可能该商品已从活动中删除");
                }

                // 成功的场合把product_id保存起来
                if (response != null && response.getErrorCode() == null) {
                    updateStatus(Integer.parseInt(item.get("promotionId").toString()), item.get("num_iid").toString(), 2, "",null);
//                    succeedProduct.add(item.get("promotionId").toString() + "," + item.get("num_iid").toString());
                } else {
                    // 失败的场合 错误信息取得
                    String fail = "";
                    if (response == null) {
                        System.out.println("超时");
                        fail = "超时";
                    } else {
                        fail = (response.getSubMsg() == null ? "" : response.getSubMsg()) + (response.getMsg() == null ? "" : response.getMsg());
                        System.out.println(fail);
                    }
                    updateStatus(Integer.parseInt(item.get("promotionId").toString()), item.get("num_iid").toString(), 3, fail,null);
                }
            } catch (Exception e) {
                $error(e);
                updateStatus(Integer.parseInt(item.get("promotionId").toString()), item.get("num_iid").toString(), 3, e.getMessage(),null);
                $info(e.getMessage());
            }
        });
    }

    private void updatePrice(String channelId, String orgChannelId, String prodCode, List<BaseMongoMap<String, Object>> skus, String numiid, Integer cartId) throws Exception {
        ShopBean shopBean = Shops.getShop(channelId, cartId);
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
        String updType ="";
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id()) || PlatFormEnums.PlatForm.JD.getId().equals(shopBean.getPlatform_id())) {
            // 先要判断更新类型
            ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode(channelId, cartId, prodCode, orgChannelId);
            if (imsBtProductModel == null) {
                $error("PriceService 产品数据不全 未配置ims_bt_product表 channelId=%s, cartId=%d, prod=%s", channelId, cartId, prodCode);
                throw new BusinessException("产品数据不全,未配置ims_bt_product表！");
            }
            updType = org.apache.commons.lang3.StringUtils.trimToNull(imsBtProductModel.getQuantityUpdateType());
            if (updType == null || (!"s".equals(updType) && !"p".equals(updType))) {
                $error("PriceService 产品数据不全 未配置ims_bt_product表quantity_update_type channelId=%s, cartId=%d, prod=%s", channelId, cartId, prodCode);
                throw new BusinessException("产品数据不全,未配置ims_bt_product表quantity_update_type！");
            }
        }

        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 天猫平台直接调用API
            platformPriceService.tmUpdatePriceBatch(shopBean, skus, "promotionPrice", updType, numiid);

        }  else if (PlatFormEnums.PlatForm.JD.getId().equals(shopBean.getPlatform_id())) {
            // votodo -- JdSkuService  京东平台 更新商品SKU的价格
            platformPriceService.jdUpdatePriceBatch(shopBean, skus, "promotionPrice", updType);
        }
    }
    private void updateStatus(Integer promotionId, String num_iid, Integer synFlg, String errMsg, String code) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("promotionId", promotionId);
        parameter.put("taskType", 0);
        parameter.put("num_iid", num_iid);
        parameter.put("synFlg", synFlg);
        parameter.put("errMsg", errMsg);
        parameter.put("key", code);
        parameter.put("modifier", getTaskName());
        promotionDao.updatePromotionStatus(parameter);
    }
}
