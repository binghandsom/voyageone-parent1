package com.voyageone.web2.cms.views.product;

import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PRODUCT.PRICE_LOG;
import com.voyageone.web2.cms.bean.PriceLogBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jonasvlag on 16/7/5.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = PRICE_LOG.ROOT)
public class CmsPriceLogController extends CmsController {

    @Autowired
    private CmsBtPriceLogService priceLogService;

    @Autowired
    private CmsPriceLogService priceLogViewService;

    @RequestMapping(PRICE_LOG.PAGE)
    public AjaxResponse page(@RequestBody PriceLogBean params) {

        String sku = params.getSku();

        if (StringUtils.isEmpty(sku))
            sku = null;

        String channelId = getUser().getSelChannelId();

        List<CmsBtPriceLogModel> data = priceLogService.getPage(sku, params.getCode(), params.getCart(), channelId, params.getOffset(), params.getLimit());

        int count = priceLogService.getCount(sku, params.getCode(), params.getCart(), channelId);

        return json("data", data, "count", count);
    }

    @RequestMapping(PRICE_LOG.EXPORT)
    public ResponseEntity export(PriceLogBean params) {

        String sku = params.getSku();

        if (StringUtils.isEmpty(sku))
            sku = null;

        String channelId = getUser().getSelChannelId();

        byte[] excelContents = priceLogViewService.exportExcel(sku, params.getCode(), params.getCart(), channelId);

        return genResponseEntityFromBytes("price log.xls", excelContents);
    }
}
