package com.voyageone.task2.cms.service.feed;

import com.overstock.mp.mpc.externalclient.api.ErrorDetails;
import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.model.EventType;
import com.overstock.mp.mpc.externalclient.model.EventsType;
import com.overstock.mp.mpc.externalclient.model.ProductType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.overstock.bean.product.OverstockProductOneQueryRequest;
import com.voyageone.components.overstock.service.OverstockEventService;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.SuperFeedOverStockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2017/3/13.
 */
@Service
public class OverStockTempService extends BaseCronTaskService {

    @Autowired
    OverstockEventService overstockEventService;

    public Integer getEventProduct() {
        try {
            Result<EventsType> result = overstockEventService.queryingForNewVariation();
            int statusCode = result.getStatusCode();
            ErrorDetails errMsg = result.getErrorDetails();
            EventsType eventsType = result.getEntity();
            int count = 0;

            List<EventType> eventTypeList = eventsType.getEvent();
            int page = 1;
            // 返回正常的场合
            if (statusCode == 200) {
                if (eventTypeList.size() == 0) {
                    $info("价格没有变化");
                } else {
                    $info("page=" + page +"页 size=" + eventTypeList.size()+" OverStock queryingForEventDetail event id =" + eventTypeList.get(0).getId());
                    page++;
                    List<EventType> eventTypeListPara = new ArrayList<EventType>();
                    overstockEventService.updateHandledEvents(eventTypeList);
                }
                return eventTypeList.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected String getTaskName() {
        return "OverStockTempJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        int cnt;
        do{
            cnt = 0;
            cnt = getEventProduct();
        }while (cnt > 0);
    }
}
