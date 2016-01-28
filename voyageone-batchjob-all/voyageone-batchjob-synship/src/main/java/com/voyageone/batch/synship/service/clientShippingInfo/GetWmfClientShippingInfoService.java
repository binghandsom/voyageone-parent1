package com.voyageone.batch.synship.service.clientShippingInfo;

import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
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
 * Created by jack on 2016-01-08.
 */
@Service
@Transactional
public class GetWmfClientShippingInfoService extends GetClientShippingBaseService {

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JD 的内容。但暂时搁置
    }

    /**
     * 获取Wmf的物流以及超卖信息
     */
    public void  getWmfShippingInfo(String channelId,  List<Runnable> threads) {

        threads.add(() -> {

            // 取得渠道信息
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);

            // 取得店铺信息
            List<ShopBean> shops = ShopConfigs.getChannelShopList(channelId);

            $info(channel.getFull_name() + "获取物流信息Start");

            for (ShopBean shop : shops){

               if (shop.getCart_id().equals(CartEnums.Cart.TG.getId())) {
                   getTgWmfShippingInfo(channel, shop);
               }
               else if (shop.getCart_id().equals(CartEnums.Cart.JG.getId())) {
                   $info(channel.getFull_name() + "（" + CartEnums.Cart.JG.toString() + "）无须取得相关信息");
               }else {
                   $info(channel.getFull_name() + "的店铺不对，无法取得相关信息，店铺：" + shop.getCart_id());
                   logIssue(channel.getFull_name() + "的店铺不对，无法取得相关信息，店铺：" + shop.getCart_id());
               }
           }

            $info(channel.getFull_name() + "获取物流信息End");
        });
    }


    /**
     * 获取Wmf的天猫国际店的物流以及超卖信息
     */
    private void getTgWmfShippingInfo(OrderChannelBean channel, ShopBean shop) {
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

            try {
                List<ReservationClientBean> clientOrderList = new ArrayList<>();

                // 从tt_reservation表中取得该店铺的status=11(open)的记录,同时需要再抽出oms_bt_orders中的client_order_id
                List<ReservationClientBean> openclientOrderList = reservationDao.getReservationDatasByShop(orderChannelId, shop.getCart_id(), CodeConstants.Reservation_Status.Open);
                clientOrderList.addAll(openclientOrderList);

                // 从tt_reservation表中取得该店铺的status=99(cancel)的记录,同时品牌方还没有取消
                List<ReservationClientBean> cancelclientOrderList = reservationDao.getCancelReservationDatasByShop(orderChannelId, shop.getCart_id(), CodeConstants.Reservation_Status.Cancelled);
                clientOrderList.addAll(cancelclientOrderList);

                int clientOrderListSize = clientOrderList.size();
                $info(fullName + "（" + shopName + "）" + "-----Open订单件数：" + clientOrderListSize);

                if (clientOrderListSize > 0) {
                    // Magento初始化
                    magentoApiServiceImpl.setOrderChannelId(orderChannelId);

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
                            //调用Magento服务的API 获取订单信息
                            SalesOrderInfoResponseParam response = magentoApiServiceImpl.getSalesOrderInfoWithOneSession(clientOrderId);

                            if (response != null) {
                                // Magento服务的返回结果
                                SalesOrderEntity salesOrderEntity = response.getResult();
                                // 订单状态
                                String magentoOrderStatus = salesOrderEntity.getStatus();

                                $info(fullName + "（" + shopName + "）" + "-----Order_Number：" + orderNumber + "，Client_order_id：" + clientOrderId + "，Magento_order_status：" + magentoOrderStatus);

                                // 品牌方发货
                                if (MagentoConstants.WMF_Status.Shipped.equalsIgnoreCase(magentoOrderStatus)) {
                                    ShipClientOrderList.add(clientOrder);
                                }

                                // 品牌方取消订单
                                else if (MagentoConstants.WMF_Status.Canceled.equalsIgnoreCase(magentoOrderStatus)) {
                                    if (CodeConstants.Reservation_Status.Open.equals(clientOrder.getStatus())) {
                                        BackClientOrderList.add(clientOrder);
                                    }
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
                        SetShipOrderList(channel, ShipClientOrderList, PortConfigEnums.Port.SYB.getId());
                    }

                    // 更新超卖订单状态
                    int backOrderSize = BackClientOrderList.size();
                    $info(channel.getFull_name() + "（" + shop.getShop_name() + "）" + "-----超卖订单件数：" + backOrderSize);
                    if (backOrderSize > 0) {
                        SetBackOrderList(channel, BackClientOrderList);
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
