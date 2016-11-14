package com.voyageone.task2.cms.service;

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
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 */
@Service
public class CmsShelvesMonitorService extends BaseMQCmsService {

    @Autowired
    private CmsBtShelvesService cmsBtShelvesService;

    @Autowired
    private CmsBtShelvesProductService cmsBtShelvesProductService;

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private ProductService productService;

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        Integer shelvesId = (Integer) messageMap.get("shelvesId");

        CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);

        List<CmsBtShelvesProductModel> cmsBtShelvesProducts = cmsBtShelvesProductService.getByShelvesId(shelvesId);

        Map<String, List<CmsBtShelvesProductModel>> numIidGroup = new HashedMap();

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
    }

    public void syuPlatformInfo(String channelId, Integer cartId, String numiid, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        if(shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.TM.getId())){
            syuPlatformInfoTM(channelId, shopBean, numiid, cmsBtShelvesProductModels);
        }

    }

    public void syuPlatformInfoTM(String channelId, ShopBean shopBean, String numiid, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels){

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
                setInfo(channelId, itemStatus.getDefaultValue(), resultList, cmsBtShelvesProductModels);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (TopSchemaException e) {
            e.printStackTrace();
        }
    }
    public void setInfo(String channelId, String itemStatus, List<SkuBean> resultList, List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductModel -> {
            cmsBtShelvesProductModel.setStatus(Integer.parseInt(itemStatus));
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, cmsBtShelvesProductModel.getProductCode());

            cmsBtProductModel.getCommon().getSkus().forEach(cmsBtProductModel_sku -> {
                SkuBean sku = resultList.stream().filter(skuBean -> skuBean.equals(cmsBtProductModel_sku.getSkuCode())).findFirst().orElse(null);
                if(sku != null){
                    Integer qty = cmsBtShelvesProductModel.getCartInventory() == null?0:cmsBtShelvesProductModel.getCartInventory();
                    cmsBtShelvesProductModel.setCartInventory(qty+sku.getQty());
                }
            });
        });
        return;
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
