package com.voyageone.batch.synship.service.clientShippingInfo;

import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
import com.voyageone.common.components.gilt.bean.GiltOrder;
import com.voyageone.common.components.gilt.bean.GiltOrderStatus;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PortConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.magento.api.MagentoConstants;
import com.voyageone.common.util.StringUtils;
import magento.SalesOrderEntity;
import magento.SalesOrderInfoResponseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacky on 2016-02-17.
 */
@Service
@Transactional
public class GetGiltClientShippingInfoService extends GetClientShippingBaseService {

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
    }

    /**
     * 获取Gilt的物流以及超卖信息
     */
    public void getGiltShippingInfo(String channelId,  List<Runnable> threads) {

        threads.add(() -> {

            // 取得渠道信息
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);

            // 取得店铺信息
            List<ShopBean> shops = ShopConfigs.getChannelShopList(channelId);

            $info(channel.getFull_name() + "获取物流信息Start");

            for (ShopBean shop : shops){

               if (shop.getCart_id().equals(CartEnums.Cart.TG.getId())) {
                   getTgGiltShippingInfo(channel, shop);
               }else {
                   $info(channel.getFull_name() + "的店铺不对，无法取得相关信息，店铺：" + shop.getCart_id());
                   logIssue(channel.getFull_name() + "的店铺不对，无法取得相关信息，店铺：" + shop.getCart_id());
               }
           }

            $info(channel.getFull_name() + "获取物流信息End");
        });
    }

    /**
     * 获取Gilt的天猫国际店的物流以及超卖信息
     */
    private void getTgGiltShippingInfo(OrderChannelBean channel, ShopBean shop) {
        // 渠道ID
        String orderChannelId = channel.getOrder_channel_id();
        // 店铺全名
        String fullName = channel.getFull_name();
        // 店铺名
        String shopName = shop.getShop_name();

        $info(fullName + "（" + shopName + "）" + "获取物流信息Start");

        transactionRunner.runWithTran(() -> {

            //超卖订单初期化
            List<ReservationClientBean> BackClientOrderList = new ArrayList<>();
            //发货订单初期化
            List<ReservationClientBean> ShipClientOrderList = new ArrayList<>();
            //订单初期化
            List<ReservationClientBean> cancelClientOrderList = new ArrayList<>();

            try {
                List<ReservationClientBean> clientOrderList = new ArrayList<>();

                // 从tt_reservation表中取得该店铺的status=11(open)的记录,同时需要再抽出oms_bt_orders中的client_order_id
                List<ReservationClientBean> openOrderList = reservationDao.getReservationDatasByShop(orderChannelId, shop.getCart_id(), CodeConstants.Reservation_Status.Open);
                clientOrderList.addAll(openOrderList);

                // 从tt_reservation表中取得该店铺的status=99(cancel)的记录,同时品牌方还没有取消
                List<ReservationClientBean> cancelOrderList = reservationDao.getCancelReservationDatasByShop(orderChannelId, shop.getCart_id(), CodeConstants.Reservation_Status.Cancelled);
                clientOrderList.addAll(cancelOrderList);

                int clientOrderListSize = clientOrderList.size();
                $info(fullName + "（" + shopName + "）" + "-----Open订单件数：" + clientOrderListSize);

                if (clientOrderListSize > 0) {
                    for (ReservationClientBean clientOrder : clientOrderList) {
                        // OMS订单号
                        String orderNumber = String.valueOf(clientOrder.getOrder_number());
                        // 品牌方订单号
                        String clientOrderId = clientOrder.getClient_order_id();

                        if (StringUtils.isNullOrBlank2(clientOrderId)) {
                            $info(fullName + "（" + shopName + "）" + "-----的品牌方订单号未设置，无法取得相关信息，Order_Number：" + orderNumber + "，Client_order_id：" + clientOrderId);
                            logIssue(fullName + "（" + shopName + "）" + "的品牌方订单号未设置，无法取得相关信息", "Order_Number：" + orderNumber + "，Client_order_id：" + clientOrderId);
                            continue;

                        } else {
                            //调用Gilt服务的API 获取订单信息
                            GiltOrder giltOrder = giltOrderService.getOrderById(clientOrderId);

                            if (giltOrder != null) {
                                // 订单状态
                                GiltOrderStatus giltOrderStatus = giltOrder.getStatus();

                                $info(fullName + "（" + shopName + "）" + "-----Order_Number：" + orderNumber + "，Client_order_id：" + clientOrderId + "，Gilt_order_status：" + giltOrderStatus);

                                // 品牌方发货
                                if (GiltOrderStatus.shipped.equals(giltOrderStatus)) {
                                    ShipClientOrderList.add(clientOrder);
                                }

                                // 品牌方取消订单
                                else if (GiltOrderStatus.cancelled.equals(giltOrderStatus)) {
                                    if (CodeConstants.Reservation_Status.Open.equals(clientOrder.getStatus())) {
                                        BackClientOrderList.add(clientOrder);
                                    }else {
                                        cancelClientOrderList.add(clientOrder);
                                    }

                                // 发货和取消以外的状态
                                } else {
                                    continue;
                                }

                            }else {
                                $info(fullName + "（" + shopName + "）" + "-----品牌方信息取得失败，Order_Number：" + orderNumber + "，Client_order_id：" + clientOrderId);
                                logIssue(fullName + "（" + shopName + "）" + "品牌方信息取得失败", "Order_Number：" + orderNumber + "，Client_order_id：" + clientOrderId);
                                continue;
                            }
                        }

                    }

                    // 更新发货订单状态
                    int shipOrderSize = ShipClientOrderList.size();
                    $info(channel.getFull_name() + "（" + shop.getShop_name() + "）" + "-----发货订单件数：" + shipOrderSize);
                    if (shipOrderSize > 0) {
                        SetShipOrderList(channel, ShipClientOrderList, PortConfigEnums.Port.LA.getId());
                    }

                    // 更新超卖订单状态
                    int backOrderSize = BackClientOrderList.size();
                    $info(channel.getFull_name() + "（" + shop.getShop_name() + "）" + "-----超卖订单件数：" + backOrderSize);
                    if (backOrderSize > 0) {
                        SetBackOrderList(channel, BackClientOrderList);
                    }

                    // 更新取消订单状态（品牌方确认取消）
                    int cancelOrderSize = cancelClientOrderList.size();
                    $info(channel.getFull_name() + "（" + shop.getShop_name() + "）" + "-----取消订单件数：" + cancelOrderSize);
                    if (cancelOrderSize > 0) {
                        SetCancelOrderList(channel, cancelClientOrderList);
                    }
                }

            } catch (Exception e) {
                $info(fullName + "（" + shopName + "）" + "获取物流信息失败：" + e.getMessage());
                logIssue(fullName + "（" + shopName + "）" + "获取物流信息失败：" + e.getMessage());
                throw new RuntimeException(e);
            }
        });

        $info(fullName + "（" + shopName + "）" + "获取物流信息End");

    }


}
