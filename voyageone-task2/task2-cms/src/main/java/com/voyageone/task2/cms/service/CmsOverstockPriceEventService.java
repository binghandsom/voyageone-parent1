package com.voyageone.task2.cms.service;

import com.overstock.mp.mpc.externalclient.api.ErrorDetails;
import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.model.EventStatusType;
import com.overstock.mp.mpc.externalclient.model.EventType;
import com.overstock.mp.mpc.externalclient.model.EventsType;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.overstock.bean.event.OverstockEventTypeUpdateRequest;

import com.voyageone.components.overstock.service.OverstockEventService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/7/28.
 * @version 2.0.0
 */
@Service
public class CmsOverstockPriceEventService extends BaseCronTaskService {

    @Autowired
    private OverstockEventService overstockEventService;
    @Autowired
    private ProductService productService;
    @Autowired
    private FeedInfoService feedInfoService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsOverstockPriceEventJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        getPriceEvent();
    }

    /*
    * @description 增量库存取得子函数（库存取得，本地更新）
    * @param threadCount 线程数
    */
    public boolean getPriceEvent() {
        boolean ret = true;

        try {
            //  调用API event Id 取得
            $info("OverStock queryingForNewVariationPrice");
            Result<EventsType> result = overstockEventService.queryingForNewVariationPrice();

            int statusCode = result.getStatusCode();
            ErrorDetails errMsg = result.getErrorDetails();
            EventsType eventsType = result.getEntity();



            // 返回正常的场合
            if (statusCode == 200) {
                List<EventType> eventTypeList = null;
                if(eventsType != null){
                    eventTypeList = eventsType.getEvent();
                }
                // 最后一页
                if (eventTypeList == null || eventTypeList.size() == 0) {
                    $info("价格没有变化");
                } else {

                    for (int i = 0; i < eventTypeList.size(); i++) {
                        // 仅有Event Id 信息
                        EventType eventType = eventTypeList.get(i);

                        $info("OverStock queryingForEventDetail event id =" + eventType.getId());
                        // Event 详细信息取得
                        Result<EventType> eventTypeDetail = overstockEventService.queryingForEventDetail(String.valueOf(eventType.getId()));

                        int statusCodeSub = eventTypeDetail.getStatusCode();
                        ErrorDetails errMsgSub = eventTypeDetail.getErrorDetails();

                        if (statusCodeSub == 200) {

                            List<EventType> eventTypeListPara = new ArrayList<EventType>();
                            eventTypeListPara.add(eventTypeDetail.getEntity());

                            for (EventType event : eventTypeListPara) {
                                String sku = "024-" + event.getVariation().getFullSku();

                                BigDecimal priceNet = event.getVariation().getSellingPrice().getAmount();
                                $info(sku + ":" + priceNet);
                                updatePriceNet(sku,priceNet);
                            }
//                             调用API event 状态更新
                            ret = updateHandledEvents(eventTypeListPara);

                            if (!ret) {
                                break;
                            }
                        } else {
                            $info("OverStock Price Increment queryingForEventDetail 调用异常 statusCode = " + statusCodeSub + " msg = " + errMsgSub.getErrorCode() + "," + errMsgSub.getErrorMessage());
                            logIssue("OverStock Price Increment queryingForEventDetail 调用异常 statusCode = " + statusCodeSub + " msg = " + errMsgSub.getErrorCode() + "," + errMsgSub.getErrorMessage());
                            break;
                        }
                    }
                }
                // 返回异常的场合
            } else {
                $info("OverStock queryingForNewVariationPrice 调用异常 statusCode = " + statusCode + " msg = " + errMsg.getErrorCode() + "," + errMsg.getErrorMessage());
                logIssue("OverStock queryingForNewVariationPrice 调用异常 statusCode = " + statusCode + " msg = " + errMsg.getErrorCode() + "," + errMsg.getErrorMessage());
                ret = false;
            }
        } catch (Exception e) {
            $error("OverStock queryingForNewVariationPrice 调用异常 ", e);
            logIssue("OverStock queryingForNewVariationPrice 调用异常 " + e.getMessage());
            ret = false;
        }

        return ret;
    }

    private void updatePriceNet(String sku, BigDecimal price) {
//        CmsBtProductModel cmsBtProductModel = productService.getProductBySku("024", sku);
//        if (cmsBtProductModel != null) {
//            for (CmsBtProductModel_Sku cmsBtProductModel_sku : cmsBtProductModel.getCommon().getSkus()) {
//                if (cmsBtProductModel_sku.getSkuCode().equalsIgnoreCase(sku)) {
//                    cmsBtProductModel_sku.setClientRetailPrice(price.doubleValue());
//                    break;
//                }
//            }
//            productService.updateProductCommon("024", cmsBtProductModel.getProdId(), cmsBtProductModel.getCommon(), getTaskName(), false);
//        }
        List<CmsBtFeedInfoModel> cmsBtFeedInfoModels = feedInfoService.getProductListBySku("024", sku);
        if (cmsBtFeedInfoModels != null && cmsBtFeedInfoModels.size() > 0) {
            cmsBtFeedInfoModels.forEach(cmsBtFeedInfoModel -> {
                for (CmsBtFeedInfoModel_Sku feedSku : cmsBtFeedInfoModel.getSkus()) {
                    if (feedSku.getSku().equalsIgnoreCase(sku)) {
                        feedSku.setPriceClientRetail(price.doubleValue());
                        feedSku.setPriceNet(Math.ceil(price.multiply(new BigDecimal(0.88)).doubleValue()));
                        Double weight = 3.0;
                        if(feedSku.getWeightOrg() != null){
                            weight = Double.parseDouble(feedSku.getWeightOrg());
                        }
                        weight = Math.ceil(new BigDecimal(weight+0.5).doubleValue());
                        feedSku.setPriceCurrent(Math.ceil(new BigDecimal((price.doubleValue()*0.88+weight*3.5+1)*6.7/(1 - 0.15 - 0.06 - 0.119 - 0.05)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()));
                        if (cmsBtFeedInfoModel.getUpdFlg() == CmsConstants.FeedUpdFlgStatus.Succeed) {
                            cmsBtFeedInfoModel.setUpdFlg(CmsConstants.FeedUpdFlgStatus.Pending);
                        }
                        feedInfoService.updateFeedInfo(cmsBtFeedInfoModel);
                        break;
                    }
                }
            });
        }
    }

    /**
     * @description OverStock Event 状态更新
     */
    private boolean updateHandledEvents(List<EventType> eventTypeList) throws Exception {
        boolean ret = true;

        OverstockEventTypeUpdateRequest request = new OverstockEventTypeUpdateRequest();

        for (int i = 0; i < eventTypeList.size(); i++) {
            request.setEventId(String.valueOf(eventTypeList.get(i).getId()));
            request.setEventStatusType(EventStatusType.ACKNOWLEDGED);
            Result<EventType> result = overstockEventService.updatingEventStatus(request);

            int statusCode = result.getStatusCode();
            ErrorDetails errMsg = result.getErrorDetails();

            if (statusCode != 200) {
                ret = false;

                $info("OverStock updatingEventStatus 调用异常 event_id = " + eventTypeList.get(i).getId() + " statusCode = " + statusCode + " msg = " + errMsg.getErrorMessage());
                logIssue("OverStock updatingEventStatus 调用异常 event_id = " + eventTypeList.get(i).getId() + " statusCode = " + statusCode + " msg = " + errMsg.getErrorMessage());
            }
        }

        return ret;
    }
}
