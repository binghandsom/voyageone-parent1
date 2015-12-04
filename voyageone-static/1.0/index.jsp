<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">

	<title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
This is my JSP page. <br>
</body>
<%
	String webRootPath = request.getContextPath();
%>
<script charset="UTF-8" type="text/javascript" src="<%=webRootPath%>/dep/jquery/jquery.min.js"></script>
<script  type="text/javascript">
	var rootPath = "<%=webRootPath%>";

	// /addneworder/doSave
	/*var FormOrdersSearch = {
	 "orderInfo" : {
	 orderNumber : "7777099",

	 // 有就传
	 customerId : "",

	 // address
	 //	bill to
	 name : "纪明1",
	 company : "chaobo22",
	 email : "jiming1221@163.com",
	 address : "长宁路1302弄73支弄32号504室1",
	 address2 : "长宁路1302弄73支弄32号504室2",
	 city : "市辖区",
	 state : "上海市",
	 zip : "200051",
	 country : "China",
	 phone : "15000708158",

	 //	ship to
	 shipName : "ship纪明",
	 shipCompany : "ship chaobo",
	 shipEmail : "ship jiming1221@163.com",
	 shipAddress : "ship 长宁路1302弄73支弄32号504室1",
	 shipAddress2 : "ship 长宁路1302弄73支弄32号504室2",
	 shipCity : "ship 市辖区",
	 shipState : "ship 上海市",
	 shipZip : "200052",
	 shipCountry : "ship China",
	 shipPhone : "15000708159",

	 // custom fields
	 localShipOnHold : false,
	 waitRealRefund : false,
	 priceDifferenceNoPay : true,
	 useTmallPointFee : 50,

	 // message and notes
	 orderInst : "orderInst",
	 comments : "comments",
	 invoiceInfo : "上海潮舶",
	 invoice : "YES",

	 // payment
	 shipping : "SF Standard",
	 cartId : "23",
	 poNumber : "123456",

	 // server set field
	 orderChannelId : "001",

	 // calu field
	 productTotal : "2884",
	 finalProductTotal : "2884",

	 surcharge : "100",
	 revisedSurcharge : "100",

	 discount : "-200",
	 revisedDiscount : "-200",

	 shippingTotal : "10",
	 finalShippingTotal : "10",

	 grandTotal : "2794",
	 finalGrandTotal : "2794",

	 expectedNet : "2794",
	 actualNet : "0",
	 balanceDue : "2794",

	 coupon : "",
	 couponDiscount : "0",
	 couponOk : "",
	 revisedCouponDiscount : "0",

	 shippedWeight : "0",
	 actualShippedWeight : "0",

	 discountType : "2",
	 discountPercent : "0",

	 sourceOrderId : "12",
	 orderKind : "1"
	 },
	 "orderDetailsList" : [
	 // 物品信息
	 {	orderNumber : "7777099",
	 sku : "glm03-pgry-8",
	 quantityOrdered : "2",
	 product : "Nike Air Force 1 Premium1",
	 pricePerUnit : "927"
	 },
	 {
	 orderNumber : "7777099",
	 sku : "511371-100-12",
	 quantityOrdered : "1",
	 product : "Nike Air Force 1 Premium2",
	 pricePerUnit : "1030"
	 }
	 ],
	 "transactionsList" : [
	 {
	 orderNumber : "7777099",
	 description : "Payment Received at Manual Orders",
	 amount : 890,
	 type : "Cash"
	 },
	 {
	 orderNumber : "7777099",
	 description : "Payment Received at Manual Orders",
	 amount : 70,
	 type : "Cash"
	 }
	 ]
	 };*/

	// /orderdetail/doInit
	/*var FormOrdersSearch = {
	 sourceOrderId : '1031288889826335'
	 };*/

	// 价差订单
	// /doApprovePriceDiffOrder
	// 				返回sourceorderid
	// 				bindNumberKind : "0"(sourceOrderId)
	//								"1"(orderNumber)
	/*var FormOrdersSearch = {
	 orderNumber : "1000066387",
	 bindNumber : '1031288889826335',
	 bindNumberKind : "0"

	 };*/

	// doGetCode
	/*var FormOrdersSearch = {
	 typeIdList : ['6','9']
	 };*/
	/*var FormOrdersSearch = {
	 typeIdList : [
	 {	id: '6',
	 showBlank: true

	 },
	 {
	 id: '9',
	 showBlank: false
	 }
	 ]
	 };*/

	// doGetSKUInfo
	/*var FormOrdersSearch = {
	 skuStartsWith : "skuStartsWith",
	 skuIncludes : "skuIncludes",
	 nameStartsWith : "nameStartsWith",
	 nameIncludes : "nameIncludes",
	 desStartsWith : "desStartsWith",
	 desIncludes : "desIncludes"
	 };*/

	// 	doGetCustomerInfo
	/*var FormOrdersSearch = {
	 customerId : "",
	 orderNumber : "",
	 lastName : "",
	 phone : "150"
	 };*/

	// 	doSaveAdjustment
	/*var FormOrdersSearch = {
	 adjustmentItem : {
	 orderNumber : "8888009",
	 adjustmentType : "1",
	 adjustmentReason : "测试用",
	 adjustmentNumber : "50"
	 },
	 orderPrice : {
	 orderNumber : "8888009",

	 productTotal : "1",
	 finalProductTotal : "2",
	 surcharge : "3",
	 revisedSurcharge : "4",
	 discount : "5",
	 revisedDiscount : "6",
	 couponDiscount : "7",
	 revisedCouponDiscount : "8",
	 shippingTotal : "9",
	 finalShippingTotal : "10",
	 grandTotal : "11",

	 finalGrandTotal : "12",
	 expectedNet : "13",
	 actualNet : "14",
	 balanceDue : "15"
	 }
	 };*/

	// 百分比
	/*var FormOrdersSearch = {
	 orderNumber : "7777099",
	 adjustmentType : "2",
	 adjustmentReason : "测试用",
	 adjustmentNumber : "0.05",
	 adjustmentDiscountType : "3"
	 };*/
	// 手工输入
	/*var FormOrdersSearch = {
	 orderNumber : "7777099",
	 adjustmentType : "2",
	 adjustmentReason : "测试用",
	 adjustmentNumber : "100",
	 adjustmentDiscountType : "2"
	 };*/
	/*var FormOrdersSearch = {
	 orderNumber : "7777099",
	 adjustmentType : "4",
	 adjustmentReason : "测试用",
	 adjustmentNumber : "100",
	 adjustmentDiscountType : ""
	 };*/

	// doSetOrderStatus
	/*var FormOrdersSearch = {
	 orderNumber : '7777099',
	 orderStatus  : 'In Processing'
	 };*/

	// doSetOrderDetailStatus
	/*var FormOrdersSearch = {
	 orderNumber : '7777099',
	 itemNumber : '',
	 status : 'In Processing'
	 };*/

	// doSetOrderOtherProp
	/*var FormOrdersSearch = {
	 orderNumber : '7777099' ,
	 waitRealRefund : false ,
	 priceDifferenceNoPay : true ,
	 useTmallPointFee : '10.06'
	 };*/

	// doReturnLineItem
	/*var FormOrdersSearch = {
	 orderNumber : '1000000002' ,
	 orderDetailsList : [
	 // 物品信息
	 {
	 itemNumber : "2"
	 },
	 {
	 itemNumber : "3"
	 }
	 ],
	 returnShipping : true
	 };*/

	// doCancelLineItems
	// doSaveOrderDetailDiscount
	/*var FormOrdersSearch = {
	 orderNumber : '300001706' ,
	 orderDetailsList : [
	 // 物品信息
	 {
	 itemNumber : "1",
	 pricePerUnit : "50"
	 }
	 ],
	 reason : "123456",
	 returnShipping : true
	 };*/

	// doUnReturnLineItem
	/*var FormOrdersSearch = {
	 orderNumber : '1000000002' ,
	 orderDetailsList : [
	 // 物品信息
	 {
	 itemNumber : "2"
	 },
	 {
	 itemNumber : "3"
	 }
	 ],
	 returnShipping : true
	 };*/
	// doApprove
	/*var FormOrdersSearch = {
	 orderNumber : '1000000005'
	 };*/

	// doCancelOrder
	/*var FormOrdersSearch = {
	 orderNumber : '6611009',
	 reason : 'test'
	 };*/

	// /orderdetail/doInitRefund
	/*var FormOrdersSearch = {
	 sourceOrderId : '952155835617654'
	 };*/

	// /orderdetail/doGetRefundMessages
	/*var FormOrdersSearch = {
	 refundInfo : {
	 sourceOrderId : '952155835617654',
	 orderChannelId : '002',
	 cartId : '23',
	 refundId : '40634536695476',
	 refundPhase : 'onsale',
	 refundFee : '400',
	 refundTime : '2015-05-16 15:12:41',
	 processFlag : false
	 }
	 };*/

	// /orderdetail/doReturnGoodsAgree
	/*var FormOrdersSearch = {
	 refundInfo : {
	 refundId : '47996674635476',
	 refundPhase : 'onsale',
	 content : '纪 测试',
	 refundPhase : 'onsale'
	 }
	 };*/

	// doAddRefundMessage
	/*var FormOrdersSearch = {
	 refundId : '45702825345476',
	 content : '纪 测试用',
	 image : 'image'
	 };*/

	// doRefundRefuse
	/*var FormOrdersSearch = {
	 refundInfo : {
	 orderChannelId : '002',
	 cartId : '23',
	 refundId : '41445344885476',
	 content : '拒绝退款 测试用',
	 image : 'data:image/jpeg;base64,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',
	 refundPhase : 'onsale'
	 }
	 };*/

	// doRefundsAgree
	var FormOrdersSearch = {
		refundInfo : {
			orderChannelId : '002',
			cartId : '23',
			refundId : '41487163105476',
			refundFee : '1.00',
			refundPhase : 'onsale',
			code : '902177'
		}
	};

	// doReturnGoodsRefuse
	/*var FormOrdersSearch = {
	 refundInfo : {
	 orderChannelId : '002',
	 cartId : '23',
	 refundId : '41479665625476',
	 image : 'data:image/jpeg;base64,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',
	 refundPhase : 'onsale'
	 }
	 };*/

	// doReturnGoodsAgree
	/*var FormOrdersSearch = {
	 refundInfo : {
	 orderChannelId : '002',
	 cartId : '23',
	 refundId : '41445344885476',
	 refundPhase : 'onsale',
	 content : '纪 测试用'
	 }
	 };*/

	// doReturnGoodsRefill
	/*var FormOrdersSearch = {
	 refundInfo : {
	 orderChannelId : '002',
	 cartId : '23',
	 refundId : '41445344885476',
	 refundPhase : 'onsale',
	 logisticsWaybillNo : 'EG893082275CS',
	 logisticsCompanyCode : 'EMS'
	 }
	 };*/

	// doRefundReview
	/*var FormOrdersSearch = {
	 refundInfo : {
	 orderChannelId : '002',
	 cartId : '23',
	 refundId : '41445504245476',
	 refundPhase : 'onsale',
	 reviewResult : true,
	 content : '纪 测试用 doRefundReview'
	 }
	 };*/

	$(function(){
		testReq();
	});

	function testReq() {
		//$.post(rootPath + "/oms/orders/addneworder/doInit.html", JSON.stringify(FormOrdersSearch), testReq_end,'json');
		//$.post(rootPath + "/oms/orders/orderdetail/doGetNotesPic.html?imgPath=D:\\panda.jpg", FormOrdersSearch, testReq_end,'json');
		//$.post(rootPath + "/oms/orders/orderdetail/doGetDetailPic.html?imgPath='http://image.sneakerhead.com/is/image/sneakerhead/tmall-imgn?$460$&$img=nike-women-dunk-sky-hi-sneaker-boot-616738001-1&layer=2&originN=0,.5&pos=0,105'", FormOrdersSearch, testReq_end,'json');
		//$.post(rootPath + "/oms/orders/addneworder/doSave.html", JSON.stringify(FormOrdersSearch), testReq_end,'json');

		$.ajax({
			type: "POST",
			//url: rootPath + "/oms/orders/addneworder/doSave.html",
			//url: rootPath + "/oms/orders/addneworder/doInit.html",
			//url: rootPath + "/oms/orders/addneworder/doGetCustomerInfo.html",

			//url: rootPath + "/core/common/service/doGetCode.html",
			//url: rootPath + "/oms/common/service/doGetSKUInfo.html",

			//url: rootPath + "/oms/orders/orderdetail/doGetNotesPic.html?imgPath=panda",
			//url: rootPath + "/oms/orders/orderdetail/doInit.html",
			//url: rootPath + "/oms/orders/orderdetail/doSaveAdjustment.html",

			//url: rootPath + "/oms/orders/orderdetail/doSetOrderStatus.html",
			//url: rootPath + "/oms/orders/orderdetail/doSetOrderOtherProp.html",

			//url: rootPath + "/oms/orders/orderdetail/doReturnLineItem.html",
			//url: rootPath + "/oms/orders/orderdetail/doUnReturnLineItem.html",

			//url: rootPath + "/oms/orders/orderdetail/doApprove.html",

			//url: rootPath + "/oms/orders/orderdetail/doCancelOrder.html",

			//url: rootPath + "/oms/orders/orderdetail/doCancelLineItems.html",
			//url: rootPath + "/oms/orders/orderdetail/doSaveOrderDetailDiscount.html",

			//url: rootPath + "/oms/orders/orderdetail/doPreApprovePriceDiffOrder.html",

			// 废止
			//url: rootPath + "/oms/orders/orderdetail/doAddRefundMessage.html",

			//url: rootPath + "/oms/orders/orderdetail/doInitRefund.html",
			//url: rootPath + "/oms/orders/orderdetail/doGetRefundMessages.html",

			url: rootPath + "/oms/orders/orderdetail/doRefundsAgree.html",
			//url: rootPath + "/oms/orders/orderdetail/doRefundRefuse.html",

			//url: rootPath + "/oms/orders/orderdetail/doReturnGoodsAgree.html",
			//url: rootPath + "/oms/orders/orderdetail/doReturnGoodsRefuse.html",

			//url: rootPath + "/oms/orders/orderdetail/doReturnGoodsRefill.html",
			//url: rootPath + "/oms/orders/orderdetail/doRefundReview.html",

			data: JSON.stringify(FormOrdersSearch),
			//dataType:"json",
			contentType : 'application/json;charset=utf-8',
			success: function(data){
				console.log(data);
			},
			error: function(res){
				console.log(res);
			}
		});
	}


	function testReq_end(json) {
		console.log(json);
	}
</script>
</html>
