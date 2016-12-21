package com.voyageone.task2.cms.service;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.category.match.MtCategoryKeysModel;
import com.voyageone.category.match.MtCategoryKeysService;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by james on 2016/12/19.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CmsBatchSetMainCategoryJob)
public class CmsBatchSetMainCategoryMqService extends BaseMQCmsService {

    final
    ProductService productService;

    final
    MtCategoryKeysService mtCategoryKeysService;

    @Autowired
    public CmsBatchSetMainCategoryMqService(ProductService productService, MtCategoryKeysService mtCategoryKeysService) {
        this.productService = productService;
        this.mtCategoryKeysService = mtCategoryKeysService;
    }

    @Override
    protected void onStartup(Map<String, Object> requestMap) throws Exception {
        // 获取参数
        String mCatId = StringUtils.trimToNull((String) requestMap.get("catId"));
        String mCatPath = StringUtils.trimToNull((String) requestMap.get("catPath"));
        List<Map> pCatList = (List<Map>) requestMap.get("pCatList");
        List<String> prodCodes = (List<String>) requestMap.get("prodIds");
        String userName = (String) requestMap.get("userName");
        String channelId = (String) requestMap.get("channelId");
        Integer cartIdObj = (Integer) requestMap.get("cartId");

        Map<String, Object> resultMap = new HashMap<>();
        if (mCatId == null || mCatPath == null || ListUtils.isNull(prodCodes)) {
            $warn("切换类目 缺少参数 params=" + requestMap.toString());
            resultMap.put("isChangeCategory", false);
            return;
        }

        MtCategoryKeysModel mtCategoryKeysModel =mtCategoryKeysService.getCategoryKeysModel(mCatPath.replace(">","/"));

        List<Integer> cartList = null;
        if (cartIdObj == null || cartIdObj == 0) {
            // 表示全平台更新
            // 店铺(cart/平台)列表
            List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList = cartTypeList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
        } else {
            cartList = new ArrayList<>(1);
            cartList.add(cartIdObj);
        }
        $info(String.format("channel=%s mCatPath=%s prodCodes size=$d",channelId, mCatPath, prodCodes.size()));
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'common.fields.code':{$in:#}}");
        updObj.setQueryParameters(prodCodes);
        if(mtCategoryKeysModel != null){
            updObj.setUpdate("{$set:{'common.catId':#,'common.catPath':#,'common.fields.categoryStatus':'1','common.fields.categorySetter':#,'common.fields.categorySetTime':#,'common.fields.productType':#,'common.fields.sizeType':#}}");
            updObj.setUpdateParameters(mCatId, mCatPath, userName, DateTimeUtil.getNow(),mtCategoryKeysModel.getProductTypeEn(),mtCategoryKeysModel.getSizeTypeEn());
        }else{
            updObj.setUpdate("{$set:{'common.catId':#,'common.catPath':#,'common.fields.categoryStatus':'1','common.fields.categorySetter':#,'common.fields.categorySetTime':#}}");
            updObj.setUpdateParameters(mCatId, mCatPath, userName, DateTimeUtil.getNow());
        }
        WriteResult rs = productService.updateMulti(updObj, channelId);
        $info("切换类目 product更新结果 " + rs.toString());

        for (Integer cartId : cartList) {
            updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':{$in:#},'platforms.P#':{$exists:true},'platforms.P#.pAttributeStatus':{$in:[null,'','0']}}");
            updObj.setQueryParameters(prodCodes, cartId, cartId);

            boolean isInCatFlg = false;
            String pCatId = null;
            String pCatPath = null;
            for (Map pCatObj : pCatList) {
                if (cartId.toString().equals(pCatObj.get("cartId"))) {
                    isInCatFlg = true;
                    pCatId = StringUtils.trimToNull((String) pCatObj.get("catId"));
                    pCatPath = StringUtils.trimToNull((String) pCatObj.get("catPath"));
                    break;
                }
            }
            if (isInCatFlg && (pCatId == null || pCatPath == null)) {
                $debug(String.format("changeProductCategory 该平台未匹配此主类目 cartid=%d, 主类目path=%s, 主类目id=%s, platformCategory=%s", cartId, mCatPath, mCatId, pCatList.toString()));
            } else if (!isInCatFlg) {
                $debug(String.format("changeProductCategory 该平台未匹配此主类目 cartid=%d, 主类目path=%s, 主类目id=%s,", cartId, mCatPath, mCatId));
            }
            if (pCatId != null || pCatPath != null) {
                updObj.setUpdate("{$set:{'platforms.P#.pCatId':#,'platforms.P#.pCatPath':#,'platforms.P#.pCatStatus':'1'}}");
                updObj.setUpdateParameters(cartId, pCatId, cartId, pCatPath, cartId);
                rs = productService.updateMulti(updObj, channelId);
                $info("切换类目 product更新结果 " + rs.toString());
            }

        }
    }
}
