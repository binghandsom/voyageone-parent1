package com.voyageone.task2.cms.service;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by james on 2016/12/19.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CmsBatchSetMainCategoryJob)
public class CmsBatchSetMainCategoryMqService extends BaseMQCmsService {

    final
    ProductService productService;

    final
    CmsBtProductDao cmsBtProductDao;

    private final
    ProductStatusHistoryService productStatusHistoryService;


    @Autowired
    public CmsBatchSetMainCategoryMqService(ProductService productService, CmsBtProductDao cmsBtProductDao, ProductStatusHistoryService productStatusHistoryService) {
        this.productService = productService;
        this.cmsBtProductDao = cmsBtProductDao;
        this.productStatusHistoryService = productStatusHistoryService;
    }

    @Override
    protected void onStartup(Map<String, Object> requestMap) throws Exception {
        // 获取参数
        String mCatId = StringUtils.trimToNull((String) requestMap.get("catId"));
        String mCatPathCn = StringUtils.trimToNull((String) requestMap.get("catPath"));
        String mCatPathEn = StringUtils.trimToNull((String) requestMap.get("catPathEn"));
//        List<Map> pCatList = (List<Map>) requestMap.get("pCatList");
        List<String> productCodes = (List<String>) requestMap.get("prodIds");
        String userName = (String) requestMap.get("userName");
        String channelId = (String) requestMap.get("channelId");
//        Integer cartIdObj = (Integer) requestMap.get("cartId");
        String productTypeEn = (String) requestMap.get("productType");
        String sizeTypeEn = (String) requestMap.get("sizeType");
        String productTypeCn = (String) requestMap.get("productTypeCn");
        String sizeTypeCn = (String) requestMap.get("sizeTypeCn");

        if (mCatId == null || mCatPathCn == null || ListUtils.isNull(productCodes)) {
            $warn("切换类目 缺少参数 params=" + requestMap.toString());
            return;
        }
        $info(String.format("channel=%s mCatPath=%s prodCodes size=%d", channelId, mCatPathCn, productCodes.size()));
        productCodes.forEach(code -> {
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
                    if (cmsBtProductModel != null) {

                        HashMap<String, Object> queryMap = new HashMap<>();
                        queryMap.put("common.fields.code", code);
                        List<BulkUpdateModel> bulkList = new ArrayList<>();
                        HashMap<String, Object> updateMap = new HashMap<>();

                        updateMap.put("common.catId", mCatId);
                        updateMap.put("common.catPath", mCatPathCn);
                        updateMap.put("common.catPathEn", mCatPathEn);
                        updateMap.put("common.fields.categoryStatus", "1");
                        updateMap.put("common.fields.categorySetter", userName);
                        updateMap.put("common.fields.categorySetTime", DateTimeUtil.getNow());
                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getProductType())) {
                            updateMap.put("common.fields.productType", productTypeEn);
                        }
                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getProductTypeCn())) {
                            updateMap.put("common.fields.productTypeCn", productTypeCn);
                        }
                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getSizeType())) {
                            updateMap.put("common.fields.sizeType", sizeTypeEn);
                        }
                        if (StringUtil.isEmpty(cmsBtProductModel.getCommon().getFields().getSizeTypeCn())) {
                            updateMap.put("common.fields.sizeTypeCn", sizeTypeCn);
                        }
                        BulkUpdateModel model = new BulkUpdateModel();
                        model.setUpdateMap(updateMap);
                        model.setQueryMap(queryMap);
                        bulkList.add(model);
                        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
                        productStatusHistoryService.insert(channelId, code, "", 0, EnumProductOperationType.changeMainCategory, "修改主类目为" + mCatPathCn, userName);
                    }
                }
        );
//        List<Integer> cartList = null;
//        if (cartIdObj == null || cartIdObj == 0) {
//            // 表示全平台更新
//            // 店铺(cart/平台)列表
//            List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
//            cartList = cartTypeList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
//        } else {
//            cartList = new ArrayList<>(1);
//            cartList.add(cartIdObj);
//        }
//        for (Integer cartId : cartList) {
//            updObj = new JongoUpdate();
//            updObj.setQuery("{'common.fields.code':{$in:#},'platforms.P#':{$exists:true},'platforms.P#.pAttributeStatus':{$in:[null,'','0']}}");
//            updObj.setQueryParameters(prodCodes, cartId, cartId);
//
//            boolean isInCatFlg = false;
//            String pCatId = null;
//            String pCatPath = null;
//            for (Map pCatObj : pCatList) {
//                if (cartId.toString().equals(pCatObj.get("cartId"))) {
//                    isInCatFlg = true;
//                    pCatId = StringUtils.trimToNull((String) pCatObj.get("catId"));
//                    pCatPath = StringUtils.trimToNull((String) pCatObj.get("catPath"));
//                    break;
//                }
//            }
//            if (isInCatFlg && (pCatId == null || pCatPath == null)) {
//                $debug(String.format("changeProductCategory 该平台未匹配此主类目 cartid=%d, 主类目path=%s, 主类目id=%s, platformCategory=%s", cartId, mCatPath, mCatId, pCatList.toString()));
//            } else if (!isInCatFlg) {
//                $debug(String.format("changeProductCategory 该平台未匹配此主类目 cartid=%d, 主类目path=%s, 主类目id=%s,", cartId, mCatPath, mCatId));
//            }
//            if (pCatId != null || pCatPath != null) {
//                updObj.setUpdate("{$set:{'platforms.P#.pCatId':#,'platforms.P#.pCatPath':#,'platforms.P#.pCatStatus':'1'}}");
//                updObj.setUpdateParameters(cartId, pCatId, cartId, pCatPath, cartId);
//                rs = productService.updateMulti(updObj, channelId);
//                $info("切换类目 product更新结果 " + rs.toString());
//            }
//
//        }
    }
}
