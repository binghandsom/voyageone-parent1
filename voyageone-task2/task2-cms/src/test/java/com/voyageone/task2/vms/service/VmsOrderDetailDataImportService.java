package com.voyageone.task2.vms.service;

import com.aliyun.oss.common.utils.DateUtil;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.dao.vms.VmsBtOrderDetailDao;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class VmsOrderDetailDataImportService extends BaseTaskService {

    @Autowired
    private VmsBtOrderDetailDao vmsBtOrderDetailDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsOrderDetailDataImportService";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
    }

    public void main(String channelId, String type) throws Exception {

        if ("ORDER".equals(type)) {
            int i = 0;
            int rsvIdStartIndex = 1001;
            int consOrderIdStartIndex = 1001;
            int shipmentId = 1001;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = dateFormat.parse("2011-01-01 01:00:00");
            while (i < 10000) {
                if (consOrderIdStartIndex % 10 == 0) {
                    shipmentId++;
                }
                // 生成1到3的随机数
                int detailNumber = makeRandom(1,8);
                // 生成orderTime
                startDate = addMinutes(startDate, 30);
                 Date orderTime = startDate;
                for (int j = 0; j < detailNumber; j++) {
                    String sku = String.valueOf(makeRandom(101,200));
                    insertDetail(
                            "rsv" + rsvIdStartIndex++,
                            channelId,
                            "cons_order" + String.valueOf(consOrderIdStartIndex),
                            orderTime,
                            "order" + String.valueOf(consOrderIdStartIndex),
                            orderTime,
                            "sku" + channelId + sku,
                            channelId + sku,
                            "name" + channelId + sku,
                            shipmentId,
                            addHours(orderTime, 20),
                            addHours(orderTime, 10),
                            addHours(orderTime, 30)
                    );
                }
                consOrderIdStartIndex++;
                i += detailNumber;
            }
        } else {
            int i = 0;
            int rsvIdStartIndex = 1001;
            int consOrderIdStartIndex = 1001;
            int orderIdStartIndex = 1001;
            int countInConsOrder = 0;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = dateFormat.parse("2011-01-01 01:00:00");
            while (i < 200000) {
                int shipmentId;
                if (orderIdStartIndex % 2 == 0) {
                    shipmentId = consOrderIdStartIndex - 1;
                } else {
                    shipmentId = consOrderIdStartIndex;
                }

                // 生成1到3的随机数
                int detailNumber = makeRandom(1,3);
                // 生成orderTime
                Date orderTime = addMinutes(startDate, 0-(50-countInConsOrder) * 10);
                for (int j = 0; j < detailNumber; j++) {
                    String sku = String.valueOf(makeRandom(101,200));
                    insertDetail(
                            "rsv" + rsvIdStartIndex++,
                            channelId,
                            "cons_order" + String.valueOf(consOrderIdStartIndex),
                            startDate,
                            "order" + String.valueOf(orderIdStartIndex),
                            orderTime,
                            "sku" + channelId + sku,
                            channelId + sku,
                            "name" + channelId + sku,
                            shipmentId,
                            addHours(startDate, 20),
                            addHours(startDate, 10),
                            addHours(startDate, 30)
                    );
                    countInConsOrder++;

                }
                orderIdStartIndex++;
                i += detailNumber;
                if (countInConsOrder >= 50 ) {
                    countInConsOrder = 0;
                    consOrderIdStartIndex++;
                    startDate = addHours(startDate, 12);
                }
            }
        }


    }

    private Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return  calendar.getTime();
    }

    private Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return  calendar.getTime();
    }

    private void insertDetail(String reservationId, String channelId, String consolidationOrderId, Date consolidationOrderTime,
    String orderId, Date orderTime, String clientSku, String barCode, String name,
    int shipmentId, Date shipmentTime, Date containerizingTime, Date receivedTime) {
        VmsBtOrderDetailModel model = new VmsBtOrderDetailModel();
        model.setReservationId(reservationId);
        model.setChannelId(channelId);
        model.setConsolidationOrderId(consolidationOrderId);
        model.setConsolidationOrderTime(consolidationOrderTime);
        model.setOrderId(orderId);
        model.setOrderTime(orderTime);
        model.setCartId(23);
        model.setClientSku(clientSku);
        model.setBarcode(barCode);
        model.setName(name);
        model.setStatus("5");
        model.setShipmentId(shipmentId);
        model.setShipmentTime(shipmentTime);
        model.setContainerizingTime(containerizingTime);
        model.setContainerizer("vendor");
        model.setReceivedTime(receivedTime);
        model.setReceiver("will");
        model.setClientMsrp(new BigDecimal("100.00"));
        model.setClientNetPrice(new BigDecimal("100.00"));
        model.setClientRetailPrice(new BigDecimal("100.00"));
        model.setClientPromotionPrice(new BigDecimal("100.00"));
        model.setRetailPrice(new BigDecimal("100.00"));
        model.setCreater("vendor");
        model.setModifier("vendor");
        vmsBtOrderDetailDao.insert(model);
    }

    private int makeRandom(int start, int end) {
        return new Random().nextInt(end + 1 - start) + start;
    }

}