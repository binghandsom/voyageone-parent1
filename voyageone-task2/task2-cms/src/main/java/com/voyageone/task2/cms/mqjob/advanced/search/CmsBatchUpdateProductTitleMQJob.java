package com.voyageone.task2.cms.mqjob.advanced.search;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchUpdateProductTitleMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
@VOSubRabbitListener
public class CmsBatchUpdateProductTitleMQJob extends TBaseMQCmsSubService<CmsBatchUpdateProductTitleMQMessageBody> {

    @Autowired
    private ProductService productService;
    @Autowired
    private SxProductService sxProductService;


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
        List<BulkUpdateModel> bulkUpdateModels = new ArrayList<>();
        List<PlatformWorkloadInfo> platformWorkloadInfos = new ArrayList<>();

        CmsBtProductModel productModel = null;
        CmsBtProductModel_Field fields = null;
        for (String code : productCodes) {
            productModel =productService.getProductByCode(channelId, code);
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

            if("928".equals(channelId) || "024".equals(channelId)){
                updateMap.put("common.catConf", "1");
            }

            queryMap.put("channelId", channelId);
            queryMap.put("common.fields.code", fields.getCode());

            // 批量修改Model
            bulkUpdateModels.add(createBulkUpdateModel(updateMap, queryMap));

            // 产品如有平台状态Approved，则重新上新
            for (CmsBtProductModel_Platform_Cart platformCart : productModel.getPlatforms().values()) {
                Integer cartId = platformCart.getCartId();
                if (cartId.intValue() > 19 && cartId.intValue() < 900 && CmsConstants.ProductStatus.Approved.name().equals(platformCart.getStatus())) {
                    PlatformWorkloadInfo platformWorkloadInfo = new PlatformWorkloadInfo();
                    platformWorkloadInfo.setChannelId(channelId);
                    platformWorkloadInfo.setCode(fields.getCode());
                    platformWorkloadInfo.setCartId(cartId);
                    platformWorkloadInfo.setUsername(username);
                    platformWorkloadInfos.add(platformWorkloadInfo);
                }
            }
        }

        try {
            if (!bulkUpdateModels.isEmpty()) {
                // 批量修改产品标题
                BulkWriteResult writeResult = productService.bulkUpdateWithMap(channelId, bulkUpdateModels, username, "$set");
                $info(String.format("高级检索批量修改产品标题(channelId=%s, username=%s)结果(%s)", channelId, username, JacksonUtil.bean2Json(writeResult)));
            }

            if (!platformWorkloadInfos.isEmpty()) {
                // 平台Approved
                for (PlatformWorkloadInfo platformWorkloadInfo : platformWorkloadInfos) {
                    $debug(String.format("高级检索批量修改商品标题(channel=%s, code=%s)，平台(cartId=%d)Approved需重新上新", channelId, platformWorkloadInfo.getCode(), platformWorkloadInfo.getCartId()));
                    sxProductService.insertPlatformWorkload(channelId, platformWorkloadInfo.getCartId(), PlatformWorkloadAttribute.TITLE, Arrays.asList(platformWorkloadInfo.getCode()), username);
                }
            }

            if (failList.isEmpty()) {
                cmsSuccessLog(messageBody, "高级检索批量修改产品标题");
            } else {
                cmsSuccessIncludeFailLog(messageBody, "高级检索批量修改产品标题", failList);
            }
        } catch (Exception e) {
            $error(String.format("(%s)高级检索批量修改产品标题出错了", username), e);
            cmsLog(messageBody, OperationLog_Type.unknownException, "高级检索批量修改产品标题");
        }
    }

    private BulkUpdateModel createBulkUpdateModel(HashMap<String, Object> updateMap, HashMap<String, Object> queryMap) {
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        return model;
    }

    private class PlatformWorkloadInfo {
        private String channelId;
        private Integer cartId;
        private String code;
        private String username;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public Integer getCartId() {
            return cartId;
        }

        public void setCartId(Integer cartId) {
            this.cartId = cartId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
