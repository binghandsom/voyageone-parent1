package com.voyageone.task2.cms.mqjob;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.response.ware.WareListResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
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
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesMonitorMQMessageBody;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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
@RabbitListener()
public class CmsShelvesMonitorMQJob extends TBaseMQCmsService<CmsShelvesMonitorMQMessageBody> {

    private final CmsBtShelvesService cmsBtShelvesService;

    private final CmsBtShelvesProductService cmsBtShelvesProductService;

    private final TbProductService tbProductService;

    private final ProductService productService;

    private final JdWareService jdWareService;

    private final CmsMqSenderService cmsMqSenderService;

    @Autowired
    public CmsShelvesMonitorMQJob(CmsBtShelvesProductService cmsBtShelvesProductService, TbProductService tbProductService, ProductService productService, CmsBtShelvesService cmsBtShelvesService, JdWareService jdWareService, CmsMqSenderService cmsMqSenderService) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.tbProductService = tbProductService;
        this.productService = productService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.jdWareService = jdWareService;
        this.cmsMqSenderService = cmsMqSenderService;
    }

    @Override
    public void onStartup(CmsShelvesMonitorMQMessageBody messageMap) throws Exception {
        Integer shelvesId = messageMap.getShelvesId();
        if(shelvesId != null) {
            $info("shelvesId = "+shelvesId +" 商品状态取得开始");
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
                numIidGroup.forEach((s, cmsBtShelvesProductModels) -> es.execute(()->syuPlatformInfo(cmsBtShelvesModel.getChannelId(),cmsBtShelvesModel.getCartId(),s,cmsBtShelvesProductModels)));
                es.shutdown();
                es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

                // 只更新货架的最后更新时间
                CmsBtShelvesModel shelvesModel = new CmsBtShelvesModel();
                shelvesModel.setId(cmsBtShelvesModel.getId());
                shelvesModel.setLastUpdate(new Date());
                cmsBtShelvesService.update(shelvesModel);
                $info("shelvesId = "+shelvesId +" 商品状态取得结束");
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
//        shopBean.setSessionKey("620272892e6145ee7c3ed73c555b4309f748ZZ9427ff3412641101981");
//        shopBean.setShop_name("Jewelry海外旗舰店");
        try {
            TmallItemUpdateSchemaGetResponse itemUpdateSchemaGetResponse = tbProductService.doGetWareInfoItem(numiid, shopBean);
            long threadNo = Thread.currentThread().getId();
            $info("threadNo:" + threadNo + " numiid:" + numiid );

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
            }else{
                $info("threadNo:" + threadNo + " numiid:" + numiid +" 取得异常");
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
        long threadNo = Thread.currentThread().getId();
        try {
            $info("threadNo:" + threadNo + " numiid:" + numiid );
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
            $info("threadNo:" + threadNo + " numiid:" + numiid +" 取得异常");
            e.printStackTrace();
            $error(e);
        }
    }
    private void setInfo(String channelId, int itemStatus, List<SkuBean> resultList, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductModel -> {
            cmsBtShelvesProductModel.setCartInventory(0);
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

    private void sendMq(CmsShelvesMonitorMQMessageBody messageMap){
        Integer shelvesId = messageMap.getShelvesId();
        if(CacheHelper.getValueOperation().get("ShelvesMonitor_"+ shelvesId) != null){
            cmsMqSenderService.sendMessage(messageMap, 30);
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
