package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Service
public class CmsPriceHistoryService extends BaseAppService {

    @Autowired
    private ProductService productService;

    /**
     * @param params 检索条件
     * @return 价格修改记录列表
     */
    public Map<String, Object> getPriceHistory(Map<String, Object> params, UserSessionBean userInfo, String language) {
        Map<String, Object> result = new HashMap<>();
        //页面channelId
        String channelId=userInfo.getSelChannelId();
        //页面code
        String code=(String) params.get("code");
        //Sku
        String skuCode="";
        //判断是否是初始化的Sku
        Boolean isFirstSku= (Boolean) params.get("isFirstSku");
        //取得数据库默认的第一个Sku
        if(isFirstSku){
            //根据页面Code取得对应的SkuList
            CmsBtProductModel cmsBtProductModel=productService.getProductByCode(channelId,code);
            if(cmsBtProductModel.getCommon().getSkus().size()>0){
                skuCode=cmsBtProductModel.getCommon().getSkus().get(0).getSkuCode();
                //获取SkuList
                result.put("skuList",cmsBtProductModel.getCommon().getSkus());
            }else{
                result.put("skuList",new ArrayList<CmsBtProductModel_Sku>());
            }
        }else{
            skuCode=(String) params.get("sku");
        }
        //根据Sku取得Sku级别的价格履历
        List<CmsBtPriceLogModel> list = productService.getPriceLog(skuCode, channelId, params);
        result.put("list", list);
        //根据取得sku取得Sku履历的件数
        int total = productService.getPriceLogCnt(skuCode,channelId, params);
        result.put("total", total);
        return result;
    }
}
