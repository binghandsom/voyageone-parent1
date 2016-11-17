package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.response.ware.WareListResponse;
import com.taobao.api.ApiException;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.fields.cms.CmsBtShelvesProductModelStatus;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by james on 2016/11/11.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_ShelvesMonitorJob)
public class CmsShelvesMonitorMQService extends BaseMQCmsService {


    private final CmsBtShelvesService cmsBtShelvesService;

    private final CmsBtShelvesProductService cmsBtShelvesProductService;

    private final TbProductService tbProductService;

    private final ProductService productService;

    private final MqSender sender;

    private final JdWareService jdWareService;

    @Autowired
    public CmsShelvesMonitorMQService(CmsBtShelvesProductService cmsBtShelvesProductService, TbProductService tbProductService, ProductService productService, MqSender sender, CmsBtShelvesService cmsBtShelvesService, JdWareService jdWareService) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.tbProductService = tbProductService;
        this.productService = productService;
        this.sender = sender;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.jdWareService = jdWareService;
    }

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        Integer shelvesId = (Integer) messageMap.get("shelvesId");
        if(shelvesId != null) {
            CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);
            List<CmsBtShelvesProductModel> cmsBtShelvesProducts = cmsBtShelvesProductService.getByShelvesId(shelvesId);
            if(!ListUtils.isNull(cmsBtShelvesProducts) && cmsBtShelvesModel != null) {
                Map<String, List<CmsBtShelvesProductModel>> numIidGroup = new HashedMap();

                // 按numiid进行分组
                cmsBtShelvesProducts.forEach(cmsBtShelvesProductModel -> {
                    if (!StringUtil.isEmpty(cmsBtShelvesProductModel.getNumIid())) {
                        if (numIidGroup.containsKey(cmsBtShelvesProductModel.getNumIid())) {
                            numIidGroup.get(cmsBtShelvesProductModel.getNumIid()).add(cmsBtShelvesProductModel);
                        } else {
                            List<CmsBtShelvesProductModel> temp = new ArrayList<CmsBtShelvesProductModel>();
                            temp.add(cmsBtShelvesProductModel);
                            numIidGroup.put(cmsBtShelvesProductModel.getNumIid(), temp);
                        }
                    }
                });
                ExecutorService es  = Executors.newFixedThreadPool(5);
                numIidGroup.forEach((s, cmsBtShelvesProductModels) -> syuPlatformInfo(cmsBtShelvesModel.getChannelId(),cmsBtShelvesModel.getCartId(),s,cmsBtShelvesProductModels));
                es.shutdown();
                es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

                // 只更新货架的最后更新时间
                CmsBtShelvesModel shelvesModel = new CmsBtShelvesModel();
                shelvesModel.setId(cmsBtShelvesModel.getId());
                shelvesModel.setLastUpdate(new Date());
                cmsBtShelvesService.update(shelvesModel);
            }
            sendMq(messageMap);
        }
    }

    private void syuPlatformInfo(String channelId, Integer cartId, String numiid, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        if(shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.TM.getId())){
            syuPlatformInfoTM(channelId, shopBean, numiid, cmsBtShelvesProductModels);
        }else if(shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.JD.getId())){
            syuPlatformInfoJD(channelId, shopBean, numiid, cmsBtShelvesProductModels);
        }

        // 更新数据库
        cmsBtShelvesProductService.updatePlatformStatus(cmsBtShelvesProductModels);


    }

    private void syuPlatformInfoTM(String channelId, ShopBean shopBean, String numiid, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels){

//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("620230429acceg4103a72932e22e4d53856b145a192140b2854639042");
        try {
            TmallItemUpdateSchemaGetResponse itemUpdateSchemaGetResponse = tbProductService.doGetWareInfoItem(numiid, shopBean);


            if (null != itemUpdateSchemaGetResponse && itemUpdateSchemaGetResponse.isSuccess()) {
                Map<String, Field> fieldMap = SchemaReader.readXmlForMap(itemUpdateSchemaGetResponse
                        .getUpdateItemResult());
                MultiComplexField skuField = (MultiComplexField) fieldMap.get("sku");
                SingleCheckField itemStatus = (SingleCheckField) fieldMap.get("item_status");

                List<SkuBean> resultList = new ArrayList<>();
                if (null == skuField
                        || null == skuField.getDefaultComplexValues()
                        || skuField.getDefaultComplexValues().size() == 0) {
                    InputField outerIdField = (InputField) fieldMap.get("outer_id");
                    InputField quantityField = (InputField) fieldMap.get("quantity");
                    String quantity = quantityField.getDefaultValue();
                    String outerId = outerIdField.getDefaultValue();
                    SkuBean skuBean = new SkuBean(outerId, Integer.parseInt(quantity));
                    resultList.add(skuBean);
                } else {
                    skuField.getDefaultComplexValues().forEach(skuValue -> {
                        SkuBean sku = new SkuBean(skuValue.getInputFieldValue("sku_outerId"),
                                Integer.parseInt(skuValue.getInputFieldValue("sku_quantity")));
                        resultList.add(sku);
                    });
                }
                setInfo(channelId, "0".equalsIgnoreCase(itemStatus.getDefaultValue())?0:1, resultList, cmsBtShelvesProductModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
            $error(e);
        }
    }

    private void syuPlatformInfoJD(String channelId, ShopBean shopBean, String numiid, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {

//        shopBean.setApp_url("https://api.jd.com/routerjson");
//        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
//        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
//        shopBean.setSessionKey("8bac1a4d-3853-446b-832d-060ed9d8bb8c");
        try {
            WareListResponse wareListResponse = jdWareService.getJdProduct(shopBean, numiid, "ware_id,skus");
            int itemStatus = CmsBtShelvesProductModelStatus.OFF;
            List<SkuBean> resultList = new ArrayList<>();
            if (wareListResponse != null) {
                for (Sku item:wareListResponse.getWareList().get(0).getSkus()){
                    SkuBean skuBean = new SkuBean(item.getOuterId(), (int) item.getStockNum());
                    if(skuBean.getQty() > 0) itemStatus =  CmsBtShelvesProductModelStatus.ON;
                    resultList.add(skuBean);
                };
            }
            setInfo(channelId, itemStatus, resultList, cmsBtShelvesProductModels);
        } catch (Exception e) {
            e.printStackTrace();
            $error(e);
        }
    }
    private void setInfo(String channelId, int itemStatus, List<SkuBean> resultList, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductModel -> {
            cmsBtShelvesProductModel.setStatus(itemStatus);

            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, cmsBtShelvesProductModel.getProductCode());
            cmsBtShelvesProductModel.setCmsInventory(cmsBtProductModel.getCommon().getFields().getQuantity());
            cmsBtProductModel.getCommon().getSkus().forEach(cmsBtProductModel_sku -> {
                SkuBean sku = resultList.stream().filter(skuBean -> skuBean.equals(cmsBtProductModel_sku.getSkuCode())).findFirst().orElse(null);
                if(sku != null){
                    Integer qty = cmsBtShelvesProductModel.getCartInventory() == null?0:cmsBtShelvesProductModel.getCartInventory();
                    cmsBtShelvesProductModel.setCartInventory(qty+sku.getQty());
                }
            });
        });
    }

    private void sendMq(Map<String, Object> messageMap){
        Integer shelvesId = (Integer) messageMap.get("shelvesId");
        if(CacheHelper.getValueOperation().get("ShelvesMonitor_"+ shelvesId) != null){
            sender.sendMessage(MqRoutingKey.CMS_BATCH_ShelvesMonitorJob, messageMap, 30);
        }
    }

    public static class SkuBean {
        private String sku;
        private Integer qty;

        public SkuBean(String sku, Integer qty) {
            this.sku = sku;
            this.qty = qty;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        @Override
        public boolean equals(Object sku) {
            if (sku instanceof String) {
                return sku.toString().equalsIgnoreCase(this.sku);
            } else {
                return super.equals(sku);
            }
        }
    }

}
