package com.voyageone.common.components.jd.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.WareDelistingGetRequest;
import com.jd.open.api.sdk.request.ware.WareListingGetRequest;
import com.jd.open.api.sdk.response.ware.WareDelistingGetResponse;
import com.jd.open.api.sdk.response.ware.WareListingGetResponse;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jd.JdBase;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 京东运营类 api 调用服务
 * <p/>
 * Created by Kylin on 2015/7/15.
 */
@Component
public class JdSaleService extends JdBase {

    public List<Ware> getOnListProduct(String strOrderChannelId, String strCardId, String strPageIndex, String strFieldList) throws JdException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        WareListingGetRequest request = new WareListingGetRequest();

        request.setPage(strPageIndex);
        request.setPageSize("100");
        request.setFields(strFieldList);
        request.setStartModified(DateTimeUtil.format(DateUtils.addDays(DateTimeUtil.getDate(), -7), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        request.setEndModified(DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        WareListingGetResponse response = reqApi(shopInfo, request);

        return response.getWareInfos();
    }

    public List<Ware> getDeListProduct(String strOrderChannelId, String strCardId, String strPageIndex, String strFieldList) throws JdException {


        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);

        WareDelistingGetRequest request = new WareDelistingGetRequest();

        request.setPage(strPageIndex);
        request.setPageSize("100");
        request.setFields(strFieldList);
        request.setStartModified(DateTimeUtil.format(DateUtils.addDays(DateTimeUtil.getDate(), -7), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        request.setEndModified(DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        WareDelistingGetResponse response = reqApi(shopInfo, request);

        return response.getWareInfos();
    }
}
