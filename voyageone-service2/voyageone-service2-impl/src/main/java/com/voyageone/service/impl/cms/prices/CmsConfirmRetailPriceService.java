package com.voyageone.service.impl.cms.prices;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.CmsBtPriceConfirmLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchConfirmRetailPriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 指导价变更批量确认
 *
 * @author jiangjusheng on 2016/09/20
 * @version 2.0.0
 */
@Service
public class CmsConfirmRetailPriceService extends VOAbsLoggable {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtPriceConfirmLogService priceConfirmLogService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;

    public void confirmPlatformsRetailPrice(AdvSearchConfirmRetailPriceMQMessageBody messageBody) {

        String channelId = StringUtils.trimToNull(messageBody.getChannelId());
        String userName = StringUtils.trimToNull(messageBody.getUserName());
        List<String> codeList = messageBody.getCodeList();
        List<Integer> cartList = messageBody.getCartList();

        JongoQuery qryObj = new JongoQuery();
        JongoUpdate updObj = new JongoUpdate();
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);

        // 获取产品的信息
        for (Integer cartIdVal : cartList) {
            qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartIdVal + ".skus.0':{$exists:true}}");
            qryObj.setParameters(codeList);
            qryObj.setProjection("{'common.fields.code':1,'platforms.P" + cartIdVal + ".skus':1,'platforms.P" + cartIdVal + ".cartId':1,'_id':0}");

            List<String> newCodeList = new ArrayList<>();
            boolean isUpdFlg = false;
            List<CmsBtProductModel> prodObjList = productService.getList(channelId, qryObj);
            for (CmsBtProductModel prodObj : prodObjList) {
                String prodCode = prodObj.getCommonNotNull().getFieldsNotNull().getCode();

                List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartIdVal).getSkus();
                for (BaseMongoMap skuObj : skuList) {
                    Boolean isSaleFlg = (Boolean) skuObj.get("isSale");
                    String chgFlg = StringUtils.trimToEmpty(skuObj.getStringAttribute("priceChgFlg"));
                    if ((chgFlg.startsWith("U") || chgFlg.startsWith("D")) && isSaleFlg) {
                        // 指导价有变更
                        skuObj.put("priceChgFlg", "0");
                        skuObj.put("confPriceRetail", skuObj.getDoubleAttribute("priceRetail"));
                        isUpdFlg = true;
                    }
                }

                // 更新产品的信息
                if (isUpdFlg) {
                    newCodeList.add(prodCode);
                    updObj.setQuery("{'common.fields.code':#}");
                    updObj.setQueryParameters(prodCode);
                    updObj.setUpdate("{$set:{'platforms.P" + cartIdVal + ".skus':#,'modified':#,'modifier':#}}");
                    updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userName);
                    BulkWriteResult rs = bulkList.addBulkJongo(updObj);
                    if (rs != null) {
                        $debug(String.format("指导价变更批量确认 channelId=%s 执行结果=%s", channelId, rs.toString()));
                    }

                    // 保存确认历史
                    priceConfirmLogService.addConfirmed(channelId, prodCode, prodObj.getPlatformNotNull(cartIdVal), userName);
                }
            }

            // 记录商品修改历史
            productStatusHistoryService.insertList(channelId, newCodeList, cartIdVal, EnumProductOperationType.BatchConfirmRetailPrice, "", userName);
        }
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("指导价变更批量确认 channelId=%s 结果=%s", channelId, rs.toString()));
        }
    }

}
