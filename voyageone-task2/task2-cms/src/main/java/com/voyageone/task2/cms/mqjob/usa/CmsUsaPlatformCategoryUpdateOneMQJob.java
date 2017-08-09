package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPlatformCategoryUpdateMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsCategoryReceiveMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateOneMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2017/8/7.
 */
@Service
@RabbitListener()
public class CmsUsaPlatformCategoryUpdateOneMQJob extends TBaseMQCmsService<CmsUsaPlatformCategoryUpdateOneMQMessageBody> {

    @Autowired
    ProductService productService;

    @Autowired
    SxProductService sxProductService;

    @Autowired
    SellerCatService sellerCatService;

    @Autowired
    CmsMqSenderService cmsMqSenderService;

    @Override
    public void onStartup(CmsUsaPlatformCategoryUpdateOneMQMessageBody messageBody) throws Exception {
        $info("接收到批量更新SN Primary Category MQ消息体" + JacksonUtil.bean2Json(messageBody));
        ArrayList<String> fullCatIds = new ArrayList<>();
        List<String> productCodes = messageBody.getProductCodes();
        Integer cartId = messageBody.getCartId();
        for (String productCode : productCodes) {
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), productCode);
            if (cmsBtProductModel != null) {
                //获取美国平台对象
                CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getUsPlatform(cartId);
                //主类目有改动
                List<CmsBtProductModel_SellerCat> sellerCats = platform.getSellerCats();
                ArrayList<CmsBtProductModel_SellerCat> copysSllerCats = new ArrayList<>();
                copysSllerCats.addAll(sellerCats);

                for (CmsBtProductModel_SellerCat sellerCat : copysSllerCats) {
                    //先用旧的pcatId匹配
                    if (sellerCat.getcId().equalsIgnoreCase(platform.getpCatId())) {
                        //移除旧的
                        sellerCats.remove(sellerCat);
                        fullCatIds.addAll(getAllpCatIds(sellerCat));
                        break;
                    }
                }
                CmsBtProductModel_SellerCat newSellerCat = sellerCatService.getSellerCat(messageBody.getChannelId(), cartId, messageBody.getpCatPath());
                if (newSellerCat != null) {
                    sellerCats.add(newSellerCat);
                    fullCatIds.addAll(getAllpCatIds(newSellerCat));
                }
                update(productCode, messageBody, sellerCats);
                if (CmsConstants.ProductStatus.Approved.name().equalsIgnoreCase(platform.getStatus())) {
                    //上新状态,更新上新表
                    sxProductService.insertSxWorkLoad(messageBody.getChannelId(), productCode, cartId, getTaskName());
                }
            }
        }
        CmsCategoryReceiveMQMessageBody body = new CmsCategoryReceiveMQMessageBody();
        body.setChannelId(messageBody.getChannelId());
        body.setCartId(cartId.toString());
        body.setSender(messageBody.getSender());
        //去重复
        HashSet<String> set = new HashSet<>();
        set.addAll(fullCatIds);
        fullCatIds.clear();
        fullCatIds.addAll(set);
        body.setFullCatIds(fullCatIds);
        if (ListUtils.notNull(fullCatIds)){
            cmsMqSenderService.sendMessage(body);
        }
    }

    private ArrayList<String> getAllpCatIds(CmsBtProductModel_SellerCat newSellerCat) {
        ArrayList<String> temp = new ArrayList<>();
        List<String> strings = newSellerCat.getcIds();
        for (int i = 0; i < strings.size(); i++) {
            if (i == 0) {
                temp.add(strings.get(i));
            }else {
                StringBuilder builder = new StringBuilder(strings.get(0));
                for (int j = 1; j <= i; j++) {
                    builder.append("-" + strings.get(j));
                }
                temp.add(builder.toString());
            }
        }
        return temp;
    }

    private void update(String productCode, CmsUsaPlatformCategoryUpdateOneMQMessageBody messageBody, List<CmsBtProductModel_SellerCat> sellerCats) {
        Integer cartId = messageBody.getCartId();
        List<BulkUpdateModel> bulkList = new ArrayList<>(1);
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("usPlatforms.P" + cartId + ".pCatPath", messageBody.getpCatPath());
        updateMap.put("usPlatforms.P" + cartId + ".pCatId", messageBody.getpCatId());
        updateMap.put("usPlatforms.P" + cartId + ".pCatStatus", 1);
        updateMap.put("usPlatforms.P" + cartId + ".sellerCats", sellerCats);

        Boolean flag = messageBody.getFlag();
        if (flag != null && flag.booleanValue()) {
            String googleCategory = "";
            String googleDepartment = "";
            String priceGrabberCategory = "";

            String seoTitle = "";
            String seoDescription = "";
            String seoKeywords = "";
            Map<String, Object> mapping = messageBody.getMapping();
            if (MapUtils.isNotEmpty(mapping)) {
                googleCategory = StringUtils.trimToEmpty((String) mapping.get("googleCategory"));
                googleDepartment = StringUtils.trimToEmpty((String) mapping.get("googleDepartment"));
                priceGrabberCategory = StringUtils.trimToEmpty((String) mapping.get("priceGrabberCategory"));

                seoTitle = StringUtils.trimToEmpty((String) mapping.get("seoTitle"));
                seoDescription = StringUtils.trimToEmpty((String) mapping.get("seoDescription"));
                seoKeywords = StringUtils.trimToEmpty((String) mapping.get("seoKeywords"));
            }

            updateMap.put("common.fields.googleCategory", googleCategory);
            updateMap.put("common.fields.googleDepartment", googleDepartment);
            updateMap.put("common.fields.priceGrabberCategory", priceGrabberCategory);

            updateMap.put("usPlatforms.P" + cartId + ".fields.seoTitle", seoTitle);
            updateMap.put("usPlatforms.P" + cartId + ".fields.seoDescription", seoDescription);
            updateMap.put("usPlatforms.P" + cartId + ".fields.seoKeywords", seoKeywords);
        }

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("common.fields.code", productCode);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        BulkWriteResult bulkWriteResult = productService.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, messageBody.getSender() == null ? getTaskName() : messageBody.getSender(), "$set");
        $info("更新产品表,productCode:" + productCode + " cartId:" + cartId + " writeResult:" + JacksonUtil.bean2Json(bulkWriteResult));
    }


}
