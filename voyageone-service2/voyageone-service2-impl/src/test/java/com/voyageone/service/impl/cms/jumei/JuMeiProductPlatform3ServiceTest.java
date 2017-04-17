package com.voyageone.service.impl.cms.jumei;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.reponse.HtDealUpdateResponse;
import com.voyageone.components.jumei.request.HtDealUpdateRequest;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.cms.jumei2.JuMeiProductPlatform3Service;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class JuMeiProductPlatform3ServiceTest {
    @Autowired
    JuMeiProductPlatform3Service service;
    String Client_id="131";
    String Client_key="7e059a48c30c67d2693be14275c2d3be";
    String Sign="0f9e3437ca010f63f2c4f3a216b7f4bc9698f071";
    String url="http://openapi.ext.jumei.com/";

    @Autowired
    JumeiHtDealService htDealService;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    ProductService productService;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;
    StringBuffer sb = new StringBuffer();

    @Test
    public void testUpdateJmByPromotionId() throws Exception {
        int promotionId = 76;
        service.updateJmByPromotionId(1308);
    }
    @Test
    public void update() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey(Client_id);
        shopBean.setAppSecret(Sign);
        shopBean.setSessionKey(Client_key);
        shopBean.setApp_url(url);


        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = daoExtCmsBtJmPromotionProduct.selectJMCopyList(1308);


        for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {

             List<String> productCodes = new ArrayList<>();
            productCodes.add(model.getProductCode());
//                     listCmsBtJmPromotionProductModel.stream().map((productModel) -> String.valueOf(productModel.getProductCode())).collect(Collectors.toList());

            // 获取产品信息
            JongoQuery query = new JongoQuery();
            query.setQuery("{\"common.fields.code\": #}");
            query.setParameters(model.getProductCode());
//        query.setProjection("{\"orgChannelId\": 1,\"common.fields.code\": 1, \"platforms.P27\": 1}");
            List<CmsBtProductModel> productMongos = productService.getList("012", query);

            // 获取活动中sku列表
            List<Map<String, Object>> listSku = daoExtCmsBtJmPromotionSku.selectExportListByPromotionId(1308, productCodes);

            // 获取product中目前有效销售的sku
//            Map<String, List<jmHtDealCopyDealSkusData>> productSkus = new HashMap<>();
//            Map<String, List<String>> isNotSaleSkuList = new HashMap<>();
//            Map<String, String> originalHashIdList = new HashMap<>();
            productMongos.forEach((product) -> {
                sb = new StringBuffer();

                // 取得逻辑库存
                Map<String, String> queryMap = new HashMap<>();
                queryMap.put("channelId", product.getOrgChannelId());
                queryMap.put("code", product.getCommon().getFields().getCode());
//                List<WmsBtInventoryCenterLogicModel> inventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailByCode(queryMap);

//                List<jmHtDealCopyDealSkusData> skuList = new ArrayList<>();
                product.getPlatform(27).getSkus()
                        .forEach((skuInfo) -> {
                            String skuCode = skuInfo.getStringAttribute("skuCode");
                            if (Boolean.valueOf(skuInfo.getStringAttribute("isSale"))) {

                                List<Object> promotionSkuList = listSku.stream()
                                        .filter((promotionSku) -> skuCode.equals(String.valueOf(promotionSku.get("skuCode"))))
                                        .collect(Collectors.toList());

                                if (promotionSkuList.size() <= 0)
                                    return;

                                Map<String, String> promotionSkuMap = (Map<String, String>)promotionSkuList.get(0);

                                if (!StringUtil.isEmpty(promotionSkuMap.get("jmSkuNo"))) {

                                    product.getPlatform(27).getSkus().forEach(sku -> {

                                        if (sku.getStringAttribute("skuCode").equals(skuCode)
                                                && sku.getIntAttribute("qty") > 0) {
                                            sb.append(String.valueOf(promotionSkuMap.get("jmSkuNo")) + ",");
                                        }
                                    });

                                }
                            }
                        });




                HtDealUpdateRequest request = new HtDealUpdateRequest();
                request.setJumei_hash_id(product.getPlatform(27).getpNumIId());
                HtDealUpdate_DealInfo data = new HtDealUpdate_DealInfo();
                data.setJumei_sku_no(sb.toString().substring(0, sb.toString().length() - 1));
                request.setUpdate_data(data);
                try {

                    HtDealUpdateResponse response = htDealService.update(shopBean, request);
                } catch (Exception ex) {
                    System.out.println(ex.getStackTrace());
                }

            });
            //{"error_code":"505","reason":"error","response":"仓库[0]不存在或者未启用"}
        }
    }
}
