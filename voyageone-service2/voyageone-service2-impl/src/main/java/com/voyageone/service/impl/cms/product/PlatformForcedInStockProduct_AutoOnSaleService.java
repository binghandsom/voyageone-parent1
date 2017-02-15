package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TypeChannelsService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.PlatformActiveLogMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 *被迫下架的产品，自动上架
 */
@Service
public class PlatformForcedInStockProduct_AutoOnSaleService extends BaseService {
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    @Autowired
    TypeChannelsService typeChannelsService;
    //上架
    public void onSaleByChannelId(String channelId) {
        List<TypeChannelBean> listCarts =typeChannelsService.getPlatformTypeList(channelId,"cn");
        for (TypeChannelBean chanelBean : listCarts) {
            if (!StringUtils.isEmpty(chanelBean.getValue())) {
                int cartId = Integer.parseInt(chanelBean.getValue());
                if (cartId == 1 || cartId == 0) {
                    continue;
                }
                if (isAutoOnSale(channelId, cartId)) {
                    onSaleByChannelId(channelId, cartId);
                }
            }
        }
    }
    //是否自动上架
    public boolean isAutoOnSale(String channelId, int cartId) {
        CmsChannelConfigBean configBean = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.IS_FORCED_IN_STOCK_PRODUCT_AUTO_ON_SALE, cartId + "");
        if (configBean == null) return false;
        return "1".equals(configBean.getConfigValue1());
    }
    // 指定平台上架
    private void onSaleByChannelId(String channelId, int cartId) {
        //被迫下架的产品的code
        String queryformat = "{lock:'0',\"common.fields.quantity\":{ $gt:0},\"platforms.P%s.pStatus\":'OnSale',\"platforms.P%s.pReallyStatus\":'InStock'}";
        String strQuery = String.format(queryformat, cartId, cartId);
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery(strQuery);
        queryObj.setProjectionExt("common.fields.code");
        List<CmsBtProductModel> prodList = cmsBtProductDao.select(queryObj, channelId);
        if (prodList.size() == 0) return;

        //productCodes
        List<String> productCodes = new ArrayList<>();
        prodList.forEach(f -> {
            productCodes.add(f.getCommon().getFields().getCode());
        });

        PlatformActiveLogMQMessageBody mqMessageBody = new PlatformActiveLogMQMessageBody();
        mqMessageBody.setChannelId(channelId);
        mqMessageBody.setCartId(cartId);
        mqMessageBody.setActiveStatus(CmsConstants.PlatformActive.ToOnSale.name());
        mqMessageBody.setComment("平台被迫下架的产品，自动上架");
        mqMessageBody.setProductCodes(productCodes);
        mqMessageBody.setSender("autoOnSale");
        cmsMqSenderService.sendMessage(mqMessageBody);

    }
}
