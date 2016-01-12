package com.voyageone.batch.synship.service.clientShippingInfo;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Shipping;
import com.taobao.api.request.LogisticsOrdersGetRequest;
import com.taobao.api.response.LogisticsOrdersGetResponse;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.modelbean.ClientTrackingBean;
import com.voyageone.batch.synship.modelbean.Order;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
import com.voyageone.common.components.channelAdvisor.CaConstants;
import com.voyageone.common.components.channelAdvisor.bean.orders.OrderCriteria;
import com.voyageone.common.components.channelAdvisor.bean.orders.OrderResponseDetailHigh;
import com.voyageone.common.components.channelAdvisor.webservices.APIResultOfArrayOfOrderResponseItem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.voyageone.common.configs.beans.CodeBean;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jack on 2016-01-07.
 */
@Service
@Transactional
public class GetSpaldingClientShippingInfoService extends GetClientShippingBaseService {

    @Autowired
    private TransactionRunner transactionRunner;

    // 天猫
    private String cartId = "20";

    private String expCode = "EXP";

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JD 的内容。但暂时搁置
    }

    /**
     * 按渠道进行逻辑库存计算
     */
    public void  getSpaldingShippingInfo(String channelId,  List<Runnable> threads) {

        threads.add(() -> {

            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);

            $info(channel.getFull_name() + "获取物流信息Start");

            transactionRunner.runWithTran(() -> {

                //错误邮件初期化
                List<ReservationClientBean> errorReservationClientList = new ArrayList<>();

                //获取当前销售渠道的第三方配置信息
                HashMap<String, ThirdPartyConfigBean> configs = ThirdPartyConfigs.getThirdPartyConfigMap(channel.getOrder_channel_id());

                //设置调用参数
                LogisticsOrdersGetRequest param = setParam(configs);

                //调用TM的API 获取订单信息
                LogisticsOrdersGetResponse response;
                try {

                    int pageNum = 1;
                    while (true) {
                        // 天猫物流信息取得
                        response = getLogisticsOrders(channelId, channel.getFull_name(), param);
                        if (response.getErrorCode() != null) {
                            break;
                        }

                        // 插入tt_client_tracking 主KEY重复时 跳过
                        insertClientTracking(response, channel);

                        // 判定分页是否结束
                        if (response.getShippings().size() < Integer.parseInt(configs.get(CaConstants.OrderList.TM_PAGE_SIZE).getProp_val1())) {
                            break;
                        } else {
                            pageNum++;
                            param.setPageNo(new Long(pageNum));
                        }
                    }
                } catch (Exception e) {
                    $info(channel.getFull_name() + "获取物流信息失败：" + e.getMessage(), e);
                    logIssue(channel.getFull_name() + "获取物流信息失败：" + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            $info(channel.getFull_name() + "获取物流信息End");
        });
    }

    /**
     * 调用天猫（taobao.logistics.orders.get）API
     * @param channelId
     * @param request
     * @return LogisticsOrdersGetResponse
     */
    private LogisticsOrdersGetResponse getLogisticsOrders(String channelId, String channelName, LogisticsOrdersGetRequest request) throws ApiException {
        ShopBean shopInfo = ShopConfigs.getShop(channelId, cartId);
        LogisticsOrdersGetResponse response = tbLogisticsService.getLogisticsOrders(shopInfo, request);

        if (response.getErrorCode() != null) {
            logger.info("	errcode = " + response.getErrorCode());
            logger.info("	msg = " + response.getMsg());
            logger.info("	sub errcode = " + response.getSubCode());
            logger.info("	sub msg = " + response.getSubMsg());

            // TMall失败内容
            String errorContent = "	errcode = " + response.getErrorCode() +
                    "	msg = " + response.getMsg() +
                    "	sub errcode = " + response.getSubCode() +
                    "	sub msg = " + response.getSubMsg();
            logIssue(channelName + "Tmall 获取物流信息失败：" + errorContent);
        }
        return response;
    }

    /**
     * 设置调用天猫（taobao.logistics.orders.get）API的参数
     * @param configs
     * @return LogisticsOrdersGetRequest
     */
    private LogisticsOrdersGetRequest setParam(HashMap<String, ThirdPartyConfigBean> configs) {
        LogisticsOrdersGetRequest param = new LogisticsOrdersGetRequest();

        // 默认时间间隔24小时
        int timeInterval = 24;
        if (!StringUtils.isNullOrBlank2(configs.get(CaConstants.OrderList.TM_TIME_INTERVAL).getProp_val1())) {
            timeInterval = Integer.parseInt(configs.get(CaConstants.OrderList.TM_TIME_INTERVAL).getProp_val1());
        }

        //当前时间
        String endTime = DateTimeUtil.getLocalTime(0);
        Date endTimeDate = DateTimeUtil.parse(endTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
        //当前时间-1
        String startTime = DateTimeUtil.getLocalTime(timeInterval * -1);
        Date startTimeDate = DateTimeUtil.parse(startTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);

        param.setFields("tid,out_sid,company_name,created");
        param.setStartCreated(startTimeDate);
        param.setEndCreated(endTimeDate);
        param.setPageNo(1L);
        param.setPageSize(Long.parseLong(configs.get(CaConstants.OrderList.TM_PAGE_SIZE).getProp_val1()));

        $info("startTime:" + startTime);
        $info("endTime:" + endTime);

        return param;
    }

    /**
     * 插入tt_client_tracking 主KEY重复时 跳过
     *
     * @param list    LogisticsOrdersGetResponse
     * @param channel 渠道
     */
    private void insertClientTracking(LogisticsOrdersGetResponse list, OrderChannelBean channel) throws Exception {

        // 快递信息追加列表
        List<ClientTrackingBean> trackingList = new ArrayList<>();
        // 快递信息异常列表（例如：快第公司转化失败）
        List<String> errorTrackingLst= new ArrayList<>();
        // OMS不存在订单列表（漏单）
        List<String> errorNotExistOrder= new ArrayList<>();

        if (list != null) {
            $info(channel.getFull_name() + "----------返回的订单件数：" + list.getShippings().size());

            // OMS 订单一览取得
            List<Order> omsOrderList = getOMSOrdersList(list);

            for (Shipping order : list.getShippings()) {
                $info(channel.getFull_name() + "----------返回的订单信息 Tid：" + String.valueOf(order.getTid()));
                if (!isHavingShippingInfo(order)) {
                    $info(channel.getFull_name() + "----------返回的订单信息 无Shipping信息 Tid：" + String.valueOf(order.getTid()));
                    continue;
                }

                ClientTrackingBean bean = new ClientTrackingBean();
                bean.setOrder_channel_id(channel.getOrder_channel_id());
                //品牌方订单编号
                bean.setClient_order_id(getOrderNumber(omsOrderList, String.valueOf(order.getTid())));
                //平台订单编号
                bean.setSource_order_id(String.valueOf(order.getTid()));
                // 运单号
                bean.setTracking_no(order.getOutSid());
                // 快递公司
                bean.setTracking_type(getEXPName(order.getCompanyName()));

                //发货时间
                bean.setTracking_time(DateTimeUtil.format(order.getCreated(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));

                bean.setCreater(getTaskName());
                bean.setModifier(getTaskName());
                String json = bean == null ? "" : new Gson().toJson(bean);
                $info(channel.getFull_name() + "----------返回的订单信息：" + json);

                // 快递信息异常的场合
                if (StringUtils.isNullOrBlank2(bean.getTracking_type())|| StringUtils.isNullOrBlank2(bean.getTracking_no())) {
                    //String json = order == null ? "" : new Gson().toJson(order);
                    //$info(channel.getFull_name() + "----------返回的订单具体信息：" + json);
                    errorTrackingLst.add(bean.getSource_order_id());
                // OMS不存在订单列表（漏单）
                } else if(StringUtils.isNullOrBlank2(bean.getClient_order_id())) {
                    errorNotExistOrder.add(bean.getSource_order_id());
                } else {
                    // GMT时间转换
                    String channel_time_zone = ChannelConfigs.getVal1(channel.getOrder_channel_id(), ChannelConfigEnums.Name.channel_time_zone);
                    bean.setTracking_time(DateTimeUtil.getGMTTime(bean.getTracking_time(), StringUtils.isNullOrBlank2(channel_time_zone)?0:Integer.valueOf(channel_time_zone)));
                    trackingList.add(bean);
                }
            }

            // 快递信息异常订单
            if (errorTrackingLst.size() > 0) {
                $info(channel.getFull_name() + "----------订单的快递信息不全：" + errorTrackingLst.toString());
                logIssue(channel.getFull_name() + "订单的快递信息不全，无法进行CN港口模拟发货", errorTrackingLst);
            }

            // OMS 不存在订单
            if (errorNotExistOrder.size() > 0) {
                $info(channel.getFull_name() + "----------OMS中不存在订单：" + errorNotExistOrder.toString());
                logIssue(channel.getFull_name() + "OMS中不存在订单", errorNotExistOrder);
            }
        }

        //插入tt_client_tracking 主KEY重复时 跳过
        if (trackingList.size() > 0) {
            clientTrackingDao.insert(trackingList);
        }
        return;
    }

    /**
     * 运单信息是否存在判定
     *
     * @param order
     * @return 判定结果
     */
    private boolean isHavingShippingInfo(Shipping order) {
        boolean ret = false;

        if (!StringUtils.isEmpty(order.getCompanyName()) ||
                !StringUtils.isEmpty(order.getOutSid())) {
            ret = true;
        }

        return ret;
    }

    /**
     * OMS 订单一览取得
     *
     * @param logisticsOrdersGetResponse
     * @return OMS订单一览
     */
    private List<Order> getOMSOrdersList(LogisticsOrdersGetResponse logisticsOrdersGetResponse) {
        List<String> sourceOrderIdList = new ArrayList<String>();
        for (Shipping order : logisticsOrdersGetResponse.getShippings()) {
            sourceOrderIdList.add(String.valueOf(order.getTid()));
        }

        List<Order> orderList = orderDao.getOrdersListBySourceOrderIdList(sourceOrderIdList);

        return orderList;
    }

    /**
     * OMS 内部订单号取得
     *
     * @param omsOrderList OMS订单一览
     * @param sourceOrderId 天猫订单
     * @return OMS内部订单号
     */
    private String getOrderNumber(List<Order> omsOrderList, String sourceOrderId) {
        String orderNumber = "";

        for (Order order : omsOrderList) {
            if (order.getSourceOrderId().equals(sourceOrderId)) {
                orderNumber = order.getOrderNumber();
                break;
            }
        }

        return orderNumber;
    }

    /**
     * 快递公司编码取得
     *
     * @param companyName 快递公司名称
     * @return 快递公司编码
     */
    private String getEXPName(String companyName) {
        String expName = "";

        List<CodeBean> expCodes = Codes.getCodeList(expCode);
        for (CodeBean codeBean : expCodes) {
            if (!StringUtils.isEmpty(codeBean.getName1())) {
                if (companyName.contains(codeBean.getName1())) {
                    expName = codeBean.getName();
                    break;
                }
            }
        }

        return expName;
    }
}
