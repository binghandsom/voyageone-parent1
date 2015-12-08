package com.voyageone.oms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.formbean.InFormOrderdetailAddLineItem;
import com.voyageone.oms.formbean.InFormOrderdetailAdjustmentItem;
import com.voyageone.oms.formbean.InFormOrderdetailReturn;
import com.voyageone.oms.formbean.OutFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.oms.formbean.OutFormOrderdetailNotes;
import com.voyageone.oms.formbean.OutFormOrderdetailOrderHistory;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormOrderdetailPayments;
import com.voyageone.oms.formbean.OutFormOrderdetailRefunds;
import com.voyageone.oms.formbean.OutFormOrderdetailShipping;
import com.voyageone.oms.formbean.OutFormOrderdetailTracking;
import com.voyageone.oms.formbean.OutFormOrderdetailTransactions;
import com.voyageone.oms.modelbean.CustomerBean;
import com.voyageone.oms.modelbean.OrderDetailsBean;
import com.voyageone.oms.modelbean.OrdersBean;

/**
 * OMS 订单明细检索service
 * 
 * @author jacky
 *
 */
public interface OmsOrderDetailsSearchService {

	/**
	 * 获得历史订单信息，根据order_number
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailOrderHistory> getOrdersHistoryInfo(String orderNumber);
	
	/**
	 * 获得历史订单信息，根据source_order_id
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailOrderHistory> getOrdersHistoryInfoBySourceOrderId(String sourceOrderId, UserSessionBean user);
	
	/**
	 * 获得订单列表
	 * 
	 * @return
	 */
	public List<String> getOrderNumberList(List<OutFormOrderdetailOrders> ordersList);
	
	/**
	 * 获得主订单信息
	 * 
	 * @return
	 */
	public OutFormOrderdetailOrders getMainOrderInfo(String sourceOrderId);
	
	/**
	 * 根据订单信息，设置主订单信息
	 * 
	 * @return
	 */
	public void setMainOrderInfo(OutFormOrderdetailOrders mainOrderInfo, OutFormOrderdetailOrders orderInfo, List<OutFormOrderdetailTransactions> orderTransactionsList);
	
	/**
	 * 获得订单信息（含明细）
	 * 
	 * @return
	 */
	public OutFormOrderdetailOrders getOrdersInfo(String orderNumber, UserSessionBean user);
	
	/**
	 * 获得订单列表信息（含明细）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailOrders> getOrdersList(String sourceOrderId, UserSessionBean user);
	
//	/**
//	 * 获得订单Notes信息
//	 * 
//	 * @return
//	 */
//	public List<OutFormOrderdetailNotes> getOrderNotesInfo(String orderNumber);
	
	/**
	 * 获得订单Notes信息（根据一组订单号）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailNotes> getOrderNotesInfoBySourceOrderId(String sourceOrderId, UserSessionBean user);
	
	/**
	 * 获得订单Tracking信息（废止 -> getOrderShippingInfo）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailTracking> getOrderTrackingInfo(String orderNumber);
	
	/**
	 * 获得订单Shipping信息
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailShipping> getOrderShippingInfo(String orderNumber);
	
	/**
	 * 获得订单Shipping信息（根据一组订单号）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailShipping> getOrderShippingInfoByOrderNumberList(List<String> orderNumberList);

	
	/**
	 * 获得订单Transactions信息，根据一组（order_number）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailTransactions> getOrderTransactionsInfo(String sourceOrderId, UserSessionBean user);
	
	/**
	 * 获得订单Payments信息，根据一组（sourceOrderId）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailPayments> getOrderPaymentsInfo(String sourceOrderId, UserSessionBean user);

	/**
	 * 权限检查 
	 * 
	 * @return
	 */
	public boolean isAuthorized(UserSessionBean user, OutFormOrderdetailOrders orderInfo);
	
	/**
	 * Notes图片取得
	 * 
	 * @return
	 */
	public void getNotesPic(HttpServletRequest request,	HttpServletResponse response, String imgPath) throws Exception ;
	
	/**
	 * SKU图片取得
	 * 
	 * @return
	 */
	public void getSKUPic(HttpServletRequest request, HttpServletResponse response, String imgPath) throws Exception ;
	
	/**
	 * Adjustment保存
	 * 
	 * @return
	 */
	public boolean saveAdjustment(InFormOrderdetailAdjustmentItem inFormOrderdetailOrderPrice, UserSessionBean user);
	
	/**
	 * LineItem删除
	 * 
	 * @return
	 */
	public void delLineItemMain(OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * LineItem追加
	 * 
	 * @return
	 */
	public void addLineItemMain(InFormOrderdetailAddLineItem inFormOrderdetailAddLineItem, AjaxResponseBean result, UserSessionBean user);
	
	
	/**
	 * 订单锁定状态变更 
	 * 
	 * @return
	 */
	public void changeLockStatusMain(String sourceOrderId,  boolean lockFlag, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单取消
	 * 
	 * @return
	 */
	public void cancelOrderMain(String sourceOrderId, String orderNumber, String reason, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单恢复
	 * 
	 * @return
	 */
	public void revertOrderMain(String orderNumber, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单状态更新
	 * 
	 * @return
	 */
	public void setOrderStatus(OrdersBean orderInfo, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单明细状态更新
	 * 
	 * @return
	 */
	public void setOrderDetailStatus(OrderDetailsBean orderInfo, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单其他属性更新
	 *
	 * @return
	 */
	public void updateOrderOtherPropMain(OutFormOrderdetailOrders orderInfo, AjaxResponseBean result, UserSessionBean user);

	/**
	 * 客户拒绝属性更新
	 *
	 * @return
	 */
	public void updateCustomerRefusedMain(OutFormOrderdetailOrders orderInfo, AjaxResponseBean result, UserSessionBean user);

	/**
	 * 订单其他属性更新
	 *
	 * @return
	 */
	public void cancelClientOrderMain(OutFormOrderdetailOrders orderInfo, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单明细Return
	 * 
	 * @return
	 */
	public void returnOrderDetailMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单明细Cancel
	 * 
	 * @return
	 */	
	public void cancelLineItemsMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单明细打折
	 * 
	 * @return
	 */
	public void saveOrderDetailDiscountMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user);

	/**
	 * 订单明细unReturn
	 * 
	 * @return
	 */
	public void unReturnOrderDetailMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 订单Approve
	 * 
	 * @return
	 */
	public void approveOrderMain(String sourceOrderId, String orderNumber, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * Notes图片保存
	 * 
	 * @return
	 */
	public boolean saveNotes(Map<String, Object> map, UserSessionBean user);
	
	/**
	 * 更改Sold to Address
	 * 
	 * @return
	 */
	public void updateAddressMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 更改Ship to Address
	 * 
	 * @return
	 */
	public void updateShipAddressMain(OutFormOrderdetailOrders bean,AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 注释保存
	 * 
	 * @return
	 */
	public void updateInternalMessage(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user);

	/**
	 * GiftMessage保存
	 * 
	 * @return
	 */
	public void updateGiftMessage(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * shipping保存
	 * 
	 * @return
	 */
	public void updateShippingMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user);

	// TODO 删除预订
	public int updateAddress(OutFormOrderdetailOrders bean);

	public OutFormAddNewOrderCustomer getCustomerInfoforSold(String customerId);

	public boolean updateCustomerInfo(CustomerBean bean);

	public int updateComment(OutFormOrderdetailOrders bean);
	/**
	 * OrderInst保存
	 * 
	 * @return
	 */
	public void updateCustomerComment(OutFormOrderdetailOrders bean,AjaxResponseBean result, UserSessionBean user);
	/**
	 * Invoice保存
	 * 
	 * @return
	 */
	public void updateInvoiceMain(OutFormOrderdetailOrders bean,AjaxResponseBean result, UserSessionBean user);
	/**
	 * InvoiceInfo保存
	 * 
	 * @return
	 */
	public void updateInvoiceInfoMain(OutFormOrderdetailOrders bean,AjaxResponseBean result, UserSessionBean user);

	/**
	 * 返回值共通设定对应
	 * 
	 * @return
	 */
	public void setSuccessReturn(String sourceOrderId, String orderNumber, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 差价订单approved设定对应
	 * 
	 * @return
	 */
	public void approvePriceDiffOrderMain(String orderNumber, String bindNumber, String bindNumberKind, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 差价订单approved预处理
	 * 
	 * @return
	 */
	public void preApprovePriceDiffOrderMain(String orderNumber, String bindNumber, String bindNumberKind, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 获得订单退款信息
	 * 
	 * @return
	 */
	public void getOrderRefundsMain(String sourceOrderId, String cartId, boolean isShowHistoryOnly, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 获得订单退款Message信息
	 * 
	 * @return
	 */
	public void getOrderRefundMessagesMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);

	/**
	 * 追加订单退款Message信息
	 * 
	 * @return
	 */
	public void addOrderRefundMessageMain(String refundId, String content, String image, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 拒绝订单退款
	 * 
	 * @return
	 */
	public void refundRefuseMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 *  同意订单退款
	 * 
	 * @return
	 */
	public void refundsAgreeMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);

	/**
	 *  同意订单退款（同步OMS）
	 *
	 * @return
	 */
	public void refundsAgreeMainSynOMS(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);

	/**
	 *  同意订单退款CN
	 *
	 * @return
	 */
	public void refundsAgreeMainCN(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);

	/**
	 * 同意退货
	 * 
	 * @return
	 */
	public void returnGoodsAgreeMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 拒绝退货
	 * 
	 * @return
	 */
	public void returnGoodsRefuseMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);

	/**
	 * 卖家回填物流信息
	 * 
	 * @return
	 */
	public void returnGoodsRefillMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);
	
	/**
	 * 审核退款单
	 * 
	 * @return
	 */
	public void refundReviewMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user);
}
