package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchPlatformFieldsMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by james on 2016/11/4.
 */
@Service
@RabbitListener()
public class CmsBatchEditPlatformFieldsMqJob extends TBaseMQCmsService<CmsBatchPlatformFieldsMQMessageBody> {

    private final ProductService productService;

    private final CmsBtProductDao cmsBtProductDao;

    private final SxProductService sxProductService;

    private final ProductStatusHistoryService productStatusHistoryService;

    @Autowired
    public CmsBatchEditPlatformFieldsMqJob(ProductService productService, CmsBtProductDao cmsBtProductDao, SxProductService sxProductService, ProductStatusHistoryService productStatusHistoryService) {
        this.productService = productService;
        this.cmsBtProductDao = cmsBtProductDao;
        this.sxProductService = sxProductService;
        this.productStatusHistoryService = productStatusHistoryService;
    }

    @Override
    public void onStartup(CmsBatchPlatformFieldsMQMessageBody messageBody) throws Exception {

        Map<String,String> failList = new HashMap<>();

        List<String> productCodes = messageBody.getProductCodes();
        String channelId = messageBody.getChannelId();
        Integer cartId = messageBody.getCartId();
        String fieldsId = messageBody.getFieldsId().replace(".","->");
        Object fieldsValue = messageBody.getFieldsValue();
        String userName = messageBody.getSender();

        // 循环更新平台的商品属性
        productCodes.forEach(code -> {
            try {
                CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
                if (cmsBtProductModel != null && cmsBtProductModel.getPlatform(cartId) != null) {
                    CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = cmsBtProductModel.getPlatform(cartId);
                    if (cmsBtProductModel_platform_cart.getFields() == null)
                        cmsBtProductModel_platform_cart.setFields(new BaseMongoMap<>());
                    cmsBtProductModel_platform_cart.getFields().setAttribute(fieldsId, fieldsValue);
                    $info(String.format("channelId=%s, cartId=%s, code=%s, fieldsId=%s , fieldsValue=%s", channelId, cartId, code, fieldsId, JacksonUtil.bean2Json(fieldsValue)));

                    HashMap<String, Object> queryMap = new HashMap<>();
                    queryMap.put("common.fields.code", code);
                    List<BulkUpdateModel> bulkList = new ArrayList<>();
                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("platforms.P" + cartId + ".fields." + fieldsId, fieldsValue);
                    BulkUpdateModel model = new BulkUpdateModel();
                    model.setUpdateMap(updateMap);
                    model.setQueryMap(queryMap);
                    bulkList.add(model);
                    cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");

                    if (CmsConstants.ProductStatus.Approved.toString().equalsIgnoreCase(cmsBtProductModel_platform_cart.getStatus())) {
                        sxProductService.insertSxWorkLoad(channelId, new ArrayList<String>(Arrays.asList(code)), cartId, userName);
                    }
                    String insertMsg = "平台属性:" + messageBody.getFieldsName() + ": " + fieldsValue;
                    productStatusHistoryService.insert(channelId, code, cmsBtProductModel_platform_cart.getStatus(), cartId, EnumProductOperationType.BatchSetPlatformAttr, insertMsg, userName);
                }else{
                    failList.put(code, "没有 platform数据");
                }
            }catch (Exception e){
                failList.put(code, Arrays.toString(e.getStackTrace()));
            }
        });
        if(failList.size()>0){
            cmsSuccessIncludeFailLog(messageBody, JacksonUtil.bean2Json(failList));
        }else{
            cmsSuccessLog(messageBody, "共处理了"+productCodes.size()+"个");
        }
    }
}
