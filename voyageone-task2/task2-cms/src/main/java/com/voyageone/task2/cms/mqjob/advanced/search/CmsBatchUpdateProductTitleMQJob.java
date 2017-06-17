package com.voyageone.task2.cms.mqjob.advanced.search;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchUpdateProductTitleMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高级检索批量修改商品Title消息Job
 *
 * @Author rex.wu
 * @Create 2017-05-23 14:44
 */
@Service
@RabbitListener()
public class CmsBatchUpdateProductTitleMQJob extends TBaseMQCmsService<CmsBatchUpdateProductTitleMQMessageBody> {

    @Autowired
    private ProductService productService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;


    @Override
    public void onStartup(CmsBatchUpdateProductTitleMQMessageBody messageBody) throws Exception {

        String channelId = messageBody.getChannelId();
        List<String> productCodes = messageBody.getProductCodes();
        String title = messageBody.getTitle();
        String titlePlace = messageBody.getTitlePlace();
        String username = messageBody.getSender();
        if (StringUtils.isBlank(username)) {
            username = "CmsBatchUpdateProductTitleMQJob";
        }

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        List<String> successCodes = new ArrayList<>();
        List<BulkUpdateModel> bulkUpdateModels = new ArrayList<>();

        CmsBtProductModel productModel = null;
        CmsBtProductModel_Field fields = null;
        for (String code : productCodes) {
            productModel = productService.getProductByCode(channelId, code);
            if (productModel == null) {
                CmsBtOperationLogModel_Msg failOne = new CmsBtOperationLogModel_Msg();
                failOne.setSkuCode(code); // 产品Code
                failOne.setMsg("找不到商品");
                failList.add(failOne);
                continue;
            }
            fields = productModel.getCommon().getFields();

            HashMap<String, Object> updateMap = new HashMap<>();
            HashMap<String, Object> queryMap = new HashMap<>();

            String newTitle = "";
            if ("prefix".equals(titlePlace)) {
                newTitle = title + fields.getOriginalTitleCn();
            } else if ("suffix".equals(titlePlace)) {
                newTitle = fields.getOriginalTitleCn() + title;
            } else {
                newTitle = title;
            }
            updateMap.put("common.fields.originalTitleCn", newTitle);

            if ("928".equals(channelId) || "024".equals(channelId)) {
                updateMap.put("common.catConf", "1");
            }

            queryMap.put("channelId", channelId);
            queryMap.put("common.fields.code", fields.getCode());

            // 如果是Liking(928)，Master -> originalTitleCn修改后同步修改【聚美：产品名，长标题，中标题】【匠心界：商品名称】【官网同构：标题】
            if ("928".equals(channelId)) {

                updateMap.put("common.fields.translateStatus", "1");
                updateMap.put("common.fields.translator", username);
                updateMap.put("common.fields.translateTime", DateTimeUtil.getNow());

                // 聚美产品名 / 聚美长标题 / 聚美中标题
                CmsBtProductModel_Platform_Cart jmCart = productModel.getPlatform(CartEnums.Cart.JM);
                if (jmCart != null && jmCart.getFields() != null) {
                    String productNameCn = jmCart.getFields().getStringAttribute("productNameCn");
                    String productLongName = jmCart.getFields().getStringAttribute("productLongName");
                    String productMediumName = jmCart.getFields().getStringAttribute("productMediumName");

                    if (!newTitle.equals(productNameCn)) {
                        updateMap.put(String.format("platforms.P%s.fields.productNameCn", CartEnums.Cart.JM.getId()), newTitle);
                    }

                    if (!newTitle.equals(productLongName)) {
                        updateMap.put(String.format("platforms.P%s.fields.productLongName", CartEnums.Cart.JM.getId()), newTitle);
                    }

                    if (!newTitle.equals(productMediumName)) {
                        updateMap.put(String.format("platforms.P%s.fields.productMediumName", CartEnums.Cart.JM.getId()), newTitle);
                    }
                }

                // 官网同购标题
                CmsBtProductModel_Platform_Cart usjoiCart = productModel.getPlatform(CartEnums.Cart.LTT);
                if (usjoiCart != null && usjoiCart.getFields() != null) {
                    String usjoiCartTitle = usjoiCart.getFields().getStringAttribute("title");
                    if (!newTitle.equals(usjoiCartTitle)) {
                        updateMap.put(String.format("platforms.P%s.fields.title", CartEnums.Cart.LTT.getId()), newTitle);
                    }
                }

                // 匠心界标题
                CmsBtProductModel_Platform_Cart jgjCart = productModel.getPlatform(CartEnums.Cart.JGJ);
                if (jgjCart != null && jgjCart.getFields() != null) {
                    String productTitle = jgjCart.getFields().getStringAttribute("productTitle");
                    if (!newTitle.equals(productTitle)) {
                        updateMap.put(String.format("platforms.P%s.fields.productTitle", CartEnums.Cart.JGJ.getId()), newTitle);
                    }
                }

            }

            try {
                // 注意修改产品标题，如果平台Approved则平台上新
                BulkWriteResult writeResult = productService.bulkUpdateWithMap(channelId, Collections.singletonList(createBulkUpdateModel(updateMap, queryMap)), username, "$set");
                $info(String.format("(%s)高级检索批量修改产品(channelId=%s, code=%s)标题，结果：%s", username, channelId, code, JacksonUtil.bean2Json(writeResult)));

                for (CmsBtProductModel_Platform_Cart platformCart : productModel.getPlatforms().values()) {
                    Integer cartId = platformCart.getCartId();
                    if (cartId > 19 && cartId < 900 && CmsConstants.ProductStatus.Approved.name().equals(platformCart.getStatus())) {
                        $debug(String.format("(%s)高级检索批量修改产品标题(channel=%s, code=%s)，平台(cartId=%d)Approved需重新上新", username, channelId, code, cartId));
                        sxProductService.insertPlatformWorkload(channelId, cartId, PlatformWorkloadAttribute.TITLE, Collections.singletonList(code), username);
                    }
                }

                successCodes.add(code);

            } catch (Exception e) {
                $error(String.format("(%s)高级检索批量修改产品标题(channelId=%s, code=%s)错误", username, channelId, code), e);
                CmsBtOperationLogModel_Msg exceptionOne = new CmsBtOperationLogModel_Msg();
                exceptionOne.setSkuCode(code);
                exceptionOne.setMsg("高级检索批量修改产品标题错误");
                failList.add(exceptionOne);
            }
        }

        if (!successCodes.isEmpty()) {
            productStatusHistoryService.insertList(channelId, successCodes, -1,
                    EnumProductOperationType.BatchUpdateProductTitle, "高级检索 批量修改产品标题", username);
        }

        if (failList.isEmpty()) {
            cmsSuccessLog(messageBody, String.format("(%s)高级检索批量修改产品标题OK", username));
        } else {
            cmsSuccessIncludeFailLog(messageBody, String.format("(%s)高级检索批量修改产品标题部分失败", username), failList);
        }
    }

    private BulkUpdateModel createBulkUpdateModel(HashMap<String, Object> updateMap, HashMap<String, Object> queryMap) {
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        return model;
    }
}
