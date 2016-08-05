package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.WareDelistingGetRequest;
import com.jd.open.api.sdk.request.ware.WareListingGetRequest;
import com.jd.open.api.sdk.response.ware.WareDelistingGetResponse;
import com.jd.open.api.sdk.response.ware.WareListingGetResponse;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
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

    /**
     * 获取上架/在售状态的产品列表(上架时间在前一天)
     * 只返回 ware_id (即num_iid)
     */
    public List<Ware> getOnListProduct(String strOrderChannelId, String strCardId, String strPageIndex, String pageSize) throws JdException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        WareListingGetRequest request = new WareListingGetRequest();

        request.setPage(strPageIndex);
        request.setPageSize(pageSize);
        request.setFields("ware_id");
        request.setStartModified(DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00");
        request.setEndModified(DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));

        WareListingGetResponse response = reqApi(shopInfo, request);
        if (response == null) {
            return null;
        }
        return response.getWareInfos();
    }

    /**
     * 获取下架/在库状态的产品列表(下架时间在前一天)
     * 只返回wareId(即num_iid)
     */
    public List<Ware> getDeListProduct(String strOrderChannelId, String strCardId, String strPageIndex, String pageSize) throws JdException {
        ShopBean shopInfo = Shops.getShop(strOrderChannelId, strCardId);
        WareDelistingGetRequest request = new WareDelistingGetRequest();

        request.setPage(strPageIndex);
        request.setPageSize(pageSize);
        request.setFields("ware_id");
        request.setStartModified(DateTimeUtil.format(DateUtils.addDays(DateTimeUtilBeijing.getCurrentBeiJingDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT) + " 00:00:00");
        request.setEndModified(DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));

        WareDelistingGetResponse response = reqApi(shopInfo, request);
        if (response == null) {
            return null;
        }
        return response.getWareInfos();
    }
}
