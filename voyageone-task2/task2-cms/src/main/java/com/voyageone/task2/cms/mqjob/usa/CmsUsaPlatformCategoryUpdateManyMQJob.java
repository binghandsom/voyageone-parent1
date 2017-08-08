package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateManyMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateOneMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/8/7.
 */
@Service
@RabbitListener()
public class CmsUsaPlatformCategoryUpdateManyMQJob extends TBaseMQCmsService<CmsUsaPlatformCategoryUpdateManyMQMessageBody> {

    @Autowired
    ProductService productService;

    @Autowired
    SxProductService sxProductService;

    @Autowired
    SellerCatService sellerCatService;

    @Override
    public void onStartup(CmsUsaPlatformCategoryUpdateManyMQMessageBody messageBody) throws Exception {
        $info("接收到批量更新SN Other Category MQ消息体" + JacksonUtil.bean2Json(messageBody));
        List<String> productCodes = messageBody.getProductCodes();
        Integer cartId = messageBody.getCartId();
        for (String productCode : productCodes) {
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), productCode);
            if (cmsBtProductModel != null) {
                //获取美国平台对象
                CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getUsPlatform(cartId);

                List<CmsBtProductModel_SellerCat> sellerCats = platform.getSellerCats();
                ArrayList<CmsBtProductModel_SellerCat> newSllerCats = new ArrayList<>();

                List<Map<String, String>> maps = messageBody.getpCatPathAndPCatIds();
                for (Map<String, String> map : maps) {
                    String pCatPath = map.get("pCatPath");
                    CmsBtProductModel_SellerCat newSellerCat = sellerCatService.getSellerCat(messageBody.getChannelId(), cartId, pCatPath);
                    if (newSellerCat != null) {
                        if (messageBody.getStatue()) {
                            //添加类目,要判断是否重复
                            boolean match = false;
                            for (CmsBtProductModel_SellerCat sellerCat : sellerCats) {
                                if (sellerCat.getcId().equalsIgnoreCase(newSellerCat.getcId())) {
                                    //有重复的不用添加
                                    match = true;
                                }
                            }
                            //没有重复的
                            if (!match) {
                                sellerCats.add(newSellerCat);
                            }
                        } else {
                            //移除类目
                            sellerCats.remove(newSellerCat);
                        }
                    }
                }
                update(productCode, messageBody, sellerCats);
                if (CmsConstants.ProductStatus.Approved.name().equalsIgnoreCase(platform.getStatus())) {
                    //上新状态,更新上新表
                    sxProductService.insertSxWorkLoad(messageBody.getChannelId(), productCode, cartId, getTaskName());
                }
            }
        }
    }

    private void update(String productCode, CmsUsaPlatformCategoryUpdateManyMQMessageBody messageBody, List<CmsBtProductModel_SellerCat> sellerCats) {
        Integer cartId = messageBody.getCartId();
        List<BulkUpdateModel> bulkList = new ArrayList<>(1);
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("usPlatforms.P" + cartId + ".sellerCats", sellerCats);
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
