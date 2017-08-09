package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsCategoryReceiveMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateManyMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateOneMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    CmsMqSenderService cmsMqSenderService;

    @Override
    public void onStartup(CmsUsaPlatformCategoryUpdateManyMQMessageBody messageBody) throws Exception {
        $info("接收到批量更新SN Other Category MQ消息体" + JacksonUtil.bean2Json(messageBody));
        ArrayList<String> fullCatIds = new ArrayList<>();

        List<String> productCodes = messageBody.getProductCodes();
        Integer cartId = messageBody.getCartId();
        for (String productCode : productCodes) {
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), productCode);
            if (cmsBtProductModel != null) {
                //获取美国平台对象
                CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getUsPlatform(cartId);

                List<CmsBtProductModel_SellerCat> sellerCats = platform.getSellerCats();
                List<String> pCatPaths = messageBody.getpCatPath();

                for (String pCatPath : pCatPaths) {
                    CmsBtProductModel_SellerCat newSellerCat = sellerCatService.getSellerCat(messageBody.getChannelId(), cartId, pCatPath);
                    if (newSellerCat != null) {
                        if (messageBody.getStatue()) {
                            //添加类目,要判断是否重复
                            boolean match = false;
                            for (CmsBtProductModel_SellerCat sellerCat : sellerCats) {
                                if (sellerCat.getcId().equalsIgnoreCase(newSellerCat.getcId())) {
                                    //有重复的不用添加
                                    match = true;
                                    break;
                                }
                            }
                            //没有重复的
                            if (!match) {
                                sellerCats.add(newSellerCat);
                                //添加的
                                fullCatIds.addAll(getAllpCatIds(newSellerCat));
                            }
                        } else {
                            //移除类目
                            //判断是否为primary category 对应的类目,这种情况不能移除
                            if (!newSellerCat.getcId().equalsIgnoreCase(platform.getpCatId())) {
                                //非主类目对应的
                                sellerCats.remove(newSellerCat);
                                //移除的
                                fullCatIds.addAll(getAllpCatIds(newSellerCat));
                            } else {
                                //主类目对应的
                                $info("primary category,对应的 sellerCat,不能移除,productCode:" + productCode + " pCatPath:" + pCatPath);
                            }
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
            } else {
                StringBuilder builder = new StringBuilder(strings.get(0));
                for (int j = 1; j <= i; j++) {
                    builder.append("-" );
                    builder.append(strings.get(j));
                }
                temp.add(builder.toString());
            }
        }
        return temp;
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
