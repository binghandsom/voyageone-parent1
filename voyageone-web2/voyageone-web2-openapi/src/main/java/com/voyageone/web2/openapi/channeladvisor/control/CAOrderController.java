package com.voyageone.web2.openapi.channeladvisor.control;

import com.voyageone.service.bean.vms.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.channeladvisor.constants.CAUrlConstants;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import com.voyageone.service.bean.vms.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.service.bean.vms.channeladvisor.request.ShipRequest;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Order controller
 *
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(
        value = CAUrlConstants.ROOT
)
public class CAOrderController extends OpenApiBaseController {

    @Autowired
    private CAOrderService caOrderService;

    /**
     * @api {get} /rest/channeladvisor/orders 获取订单列表
     * @apiName getOrders
     * @apiDescription 根据状态或限制条数获取订单列表
     *
     * @apiGroup OrderApi
     * @apiVersion 2.0.0
     * @apiPermission channeladvisor
     *
     * @apiHeaderExample (系统级参数) {String} SellerID <code>对应平台内部的SellerID(ClientId)</code>
     * @apiHeaderExample (系统级参数) {String} SellerToken <code>对应平台内部的SellerToken(ClientSecret)</code>
     *
     * @apiParam (应用级参数) {String} status <code>status 可选</code>
     * @apiParam (应用级参数) {Integer} limit <code>limit 可选</code>
     *
     * @apiSuccess (返回字段) {String} ResponseBody responseBody
     * @apiSuccess (返回字段) {ResponseStatusEnum} Status status
     * @apiSuccess (返回字段) {Integer} PendingUri PendingUri
     * @apiSuccess (返回字段) {List(ErrorModel)} Errors errors
     * @apiSuccess (返回字段) {boolean} hasErrors hasErrors
     *
     * @apiSuccessExample Success Response
     * {
     *  "ResponseBody": [
     *    {
     *      "ID": "M123W",
     *      "OrderStatus": "ReleasedForShipment",
     *      "OrderDateUtc": "2016-09-21T03:01:59.8508404Z",
     *      "BuyerAddress": {
     *        "EmailAddress": "tatooine_sales@tosche-stations.com",
     *        "FirstName": "Watto",
     *        "LastName": "Toydarian",
     *        "AddressLine1": "321 Landing St.",
     *        "City": "Mos Eisley",
     *        "Country": "US",
     *        "PostalCode": "ME-123",
     *        "StateOrProvince": "NC",
     *        "AddressLine2": "Ste. 104",
     *        "CompanyName": "Tosche Stations",
     *        "DaytimePhone": "919-123-4567",
     *        "EveningPhone": null,
     *        "NameSuffix": "Jr."
     *      },
     *      "ShippingAddress": {
     *        "EmailAddress": "orphanpilot1996@hotmail.com",
     *        "FirstName": "Luke",
     *        "LastName": "Skywalker",
     *        "AddressLine1": "654 Binary Sundet Dr.",
     *        "City": "Tuskan Territory",
     *        "Country": "US",
     *        "PostalCode": "TT-456",
     *        "StateOrProvince": "NC",
     *        "AddressLine2": "C/O \"The chosen one\"",
     *        "CompanyName": "Skywalker Evaporator Farms",
     *        "DaytimePhone": "480-987-6543",
     *        "EveningPhone": null,
     *        "NameSuffix": null
     *      },
     *      "RequestedShippingMethod": "Bantha Union Express",
     *      "DeliverByDateUtc": "2016-09-23T03:31:59.8508404Z",
     *      "ShippingLabelURL": "http://www.asapsystems.com/instructions/FAQ_FedEx_2D_Barcode_Information.pdf",
     *      "TotalPrice": 514.25,
     *      "TotalTaxPrice": 5.00,
     *      "TotalShippingPrice": 5.00,
     *      "TotalShippingTaxPrice": 0.75,
     *      "TotalGiftOptionPrice": 1.00,
     *      "TotalGiftOptionTaxPrice": 0.25,
     *      "TotalFees": 3.00,
     *      "Currency": "USD",
     *      "VatInclusive": false,
     *      "Items": [
     *        {
     *          "ID": "M000W",
     *          "SellerSku": "REBEL X-WING",
     *          "UnitPrice": 100.00,
     *          "Quantity": 5
     *        }
     *      ]
     *    },
     *    {
     *      "ID": "M456W",
     *      "OrderStatus": "ReleasedForShipment",
     *      "OrderDateUtc": "2016-09-21T03:01:59.8664407Z",
     *      "BuyerAddress": {
     *        "EmailAddress": "tatooine_sales@tosche-stations.com",
     *        "FirstName": "Watto",
     *        "LastName": "Toydarian",
     *        "AddressLine1": "321 Landing St.",
     *        "City": "Mos Eisley",
     *        "Country": "US",
     *        "PostalCode": "ME-123",
     *        "StateOrProvince": "NC",
     *        "AddressLine2": "Ste. 104",
     *        "CompanyName": "Tosche Stations",
     *        "DaytimePhone": "919-123-4567",
     *        "EveningPhone": null,
     *        "NameSuffix": "Jr."
     *      },
     *      "ShippingAddress": {
     *        "EmailAddress": "orphanpilot1996@hotmail.com",
     *        "FirstName": "Luke",
     *        "LastName": "Skywalker",
     *        "AddressLine1": "654 Binary Sundet Dr.",
     *        "City": "Tuskan Territory",
     *        "Country": "US",
     *        "PostalCode": "TT-456",
     *        "StateOrProvince": "NC",
     *        "AddressLine2": "C/O \"The chosen one\"",
     *        "CompanyName": "Skywalker Evaporator Farms",
     *        "DaytimePhone": "480-987-6543",
     *        "EveningPhone": null,
     *        "NameSuffix": null
     *      },
     *      "RequestedShippingMethod": "Bantha Union Express",
     *      "DeliverByDateUtc": "2016-09-23T03:31:59.8664407Z",
     *      "ShippingLabelURL": "http://www.asapsystems.com/instructions/FAQ_FedEx_2D_Barcode_Information.pdf",
     *      "TotalPrice": 588.05,
     *      "TotalTaxPrice": 5.00,
     *      "TotalShippingPrice": 5.00,
     *      "TotalShippingTaxPrice": 0.75,
     *      "TotalGiftOptionPrice": 1.00,
     *      "TotalGiftOptionTaxPrice": 0.25,
     *      "TotalFees": 3.00,
     *      "Currency": "USD",
     *      "VatInclusive": false,
     *      "Items": [
     *        {
     *          "ID": "M333W",
     *          "SellerSku": "LIGHTSABER_BLUE_MED",
     *          "UnitPrice": 11.56,
     *          "Quantity": 1
     *        },
     *        {
     *          "ID": "M222W",
     *          "SellerSku": "LIGHTSABER_RED_LG",
     *          "UnitPrice": 15.56,
     *          "Quantity": 4
     *        },
     *        {
     *          "ID": "M000W",
     *          "SellerSku": "REBEL X-WING",
     *          "UnitPrice": 100.00,
     *          "Quantity": 5
     *        }
     *      ]
     *    }
     *  ],
     *  "Status": "Complete",
     *  "PendingUri": null,
     *  "Errors": [
     *
     *  ]
     *}
     * @apiErrorExample  错误示例
     *{
     *  "Status": "Failed",
     *  "PendingUri": null,
     *  "Errors": [
     *    {
     *      "ID": "InvalidToken",
     *      "ErrorCode": "4001",
     *      "Message": "Your seller token is invalid. Please contact our support team to obtain a valid authentication token."
     *    }
     *  ],
     *  "HasErrors": true,
     *  "ResponseBody": null
     *}
     * @apiErrorExample 错误编码
     * 1000:Info
     * 2000:Warning
     * 3000:SystemFailure
     * 3001:SystemUnavailable
     * 3002:RateLimitExceeded
     * 4000:AuthorizationFailure
     * 4001:InvalidToken
     * 4002:InvalidSellerID
     * 5000:ProductNotFound
     * 5001:ProductMissingRequiredFields
     * 5002:ProductFailedDataValidation
     * 5003:ProductFailedCreate
     * 5004:ProductFailedUpdate
     * 5100:ProductMissingCategory
     * 5101:ProductInvalidCategory
     * 5200:ProductPendingReview
     * 6000:OrderNotFound
     * 6001:InvalidOrder
     * 6002:InvalidOrderStatus
     * 6100:ShipmentFailed
     * 6101:InvalidTrackingNumber
     * 6102:InvalidShippingCarrier
     * 6103:InvalidShippingClass
     * 6200:RefundFailed
     * 6201:UnsupportedRefundReason
     * 6300:CancellationFailed
     * 6301:UnsupportedCancellationReason
     * 7000:InvalidRequest
     * 7001:MissingRequiredParameter
     * 7002:InvalidRequiredParameter
     * 8000:BatchNotFound
     * @apiExample  业务说明
     *  根据状态或限制条数获取订单
     * @apiExample 使用表
     *  使用vms_bt_client_orders表
     *  使用vms_bt_client_order_details表
     */
    /**
     * 根据状态或限制条数获取订单
     *
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.GET_ORDERS, method = RequestMethod.GET)
    public ActionResponse getOrders(HttpServletRequest request) {
        return caOrderService.getOrders(request.getParameter("status"), request.getParameter("limit"));
    }

    /**
     * 根据状态或限制条数获取订单
     *
     * @param request request
     * @return response
     */
    /*@RequestMapping(value = CAUrlConstants.ORDERS.GET_ORDERS, method = RequestMethod.POST)
    public ActionResponse getOrdersPost(HttpServletRequest request) {
        throw new CAApiException(ErrorIDEnum.InvalidOrderStatus);
    }*/

    /**
     * @api {get} /rest/channeladvisor/orders/{id} 获取单个订单
     * @apiName getOrderById
     * @apiDescription 根据id查询订单
     *
     * @apiGroup OrderApi
     * @apiVersion 2.0.0
     * @apiPermission channeladvisor
     *
     * @apiHeaderExample (系统级参数) {String} SellerID <code>对应平台内部的SellerID(ClientId)</code>
     * @apiHeaderExample (系统级参数) {String} SellerToken <code>对应平台内部的SellerToken(ClientSecret)</code>
     *
     * @apiParam (应用级参数) {String} id <code>id 必填</code>
     *
     * @apiSuccess (返回字段) {String} ResponseBody responseBody
     * @apiSuccess (返回字段) {ResponseStatusEnum} Status status
     * @apiSuccess (返回字段) {Integer} PendingUri PendingUri
     * @apiSuccess (返回字段) {List(ErrorModel)} Errors errors
     * @apiSuccess (返回字段) {boolean} hasErrors hasErrors
     *
     * @apiSuccessExample Success Response
     *{
     *  "Status": "Complete",
     *  "PendingUri": null,
     *  "Errors": [
     *
     *  ],
     *  "HasErrors": false,
     *  "ResponseBody": {
     *    "Currency": "USD",
     *    "ID": "028",
     *    "Items": [
     *      {
     *        "ID": "11",
     *        "SellerSku": "1737685",
     *        "Quantity": 1,
     *        "UnitPrice": 10.83
     *      },
     *      {
     *        "ID": "5",
     *        "SellerSku": "2700002",
     *        "Quantity": 1,
     *        "UnitPrice": 31.23
     *      },
     *      {
     *        "ID": "6",
     *        "SellerSku": "3035462",
     *        "Quantity": 1,
     *        "UnitPrice": 18.48
     *      },
     *      {
     *        "ID": "8",
     *        "SellerSku": "3037316",
     *        "Quantity": 1,
     *        "UnitPrice": 21.03
     *      },
     *      {
     *        "ID": "10",
     *        "SellerSku": "3100191",
     *        "Quantity": 1,
     *        "UnitPrice": 20.18
     *      },
     *      {
     *        "ID": "1",
     *        "SellerSku": "3108487",
     *        "Quantity": 1,
     *        "UnitPrice": 20.18
     *      },
     *      {
     *        "ID": "3",
     *        "SellerSku": "3117503",
     *        "Quantity": 1,
     *        "UnitPrice": 20.18
     *      },
     *      {
     *        "ID": "2",
     *        "SellerSku": "3321289",
     *        "Quantity": 1,
     *        "UnitPrice": 93.28
     *      },
     *      {
     *        "ID": "7",
     *        "SellerSku": "3338667",
     *        "Quantity": 1,
     *        "UnitPrice": 43.98
     *      },
     *      {
     *        "ID": "9",
     *        "SellerSku": "3483906",
     *        "Quantity": 1,
     *        "UnitPrice": 46.53
     *      },
     *      {
     *        "ID": "4",
     *        "SellerSku": "3569376",
     *        "Quantity": 1,
     *        "UnitPrice": 67.78
     *      }
     *    ],
     *    "OrderDateUtc": 1474254675000,
     *    "OrderStatus": null,
     *    "RequestedShippingMethod": "GZ",
     *    "TotalFees": 0.0,
     *    "TotalGiftOptionPrice": 0.0,
     *    "TotalGiftOptionTaxPrice": 0.0,
     *    "TotalPrice": 393.68,
     *    "TotalTaxPrice": 0.0,
     *    "TotalShippingPrice": 0.0,
     *    "TotalShippingTaxPrice": 0.0,
     *    "VatInclusive": false,
     *    "BuyerAddress": {
     *      "EmailAddress": "customer@tmall.hk",
     *      "FirstName": "VoyageOne Receiving Dept",
     *      "LastName": "ATTN: Modotex",
     *      "AddressLine1": "16724 Marquardt Ave.",
     *      "City": "Cerritos",
     *      "Country": "US",
     *      "PostalCode": "90703",
     *      "StateOrProvince": "CA",
     *      "AddressLine2": "02820160919111115001",
     *      "CompanyName": "",
     *      "DaytimePhone": "1-562-977-6408",
     *      "EveningPhone": "1-562-977-6408",
     *      "NameSuffix": ""
     *    },
     *    "DeliverByDateUtc": null,
     *    "ShippingAddress": null,
     *    "ShippingLabelURL": null
     *  }
     *}
     * @apiErrorExample  错误示例
     *{
     *  "Status": "Failed",
     *  "PendingUri": null,
     *  "Errors": [
     *    {
     *      "ID": "OrderNotFound",
     *      "ErrorCode": "6000",
     *      "Message": "order id not found."
     *    }
     *  ],
     *  "HasErrors": true,
     *  "ResponseBody": null
     *}
     * @apiErrorExample 错误编码
     * 1000:Info
     * 2000:Warning
     * 3000:SystemFailure
     * 3001:SystemUnavailable
     * 3002:RateLimitExceeded
     * 4000:AuthorizationFailure
     * 4001:InvalidToken
     * 4002:InvalidSellerID
     * 5000:ProductNotFound
     * 5001:ProductMissingRequiredFields
     * 5002:ProductFailedDataValidation
     * 5003:ProductFailedCreate
     * 5004:ProductFailedUpdate
     * 5100:ProductMissingCategory
     * 5101:ProductInvalidCategory
     * 5200:ProductPendingReview
     * 6000:OrderNotFound
     * 6001:InvalidOrder
     * 6002:InvalidOrderStatus
     * 6100:ShipmentFailed
     * 6101:InvalidTrackingNumber
     * 6102:InvalidShippingCarrier
     * 6103:InvalidShippingClass
     * 6200:RefundFailed
     * 6201:UnsupportedRefundReason
     * 6300:CancellationFailed
     * 6301:UnsupportedCancellationReason
     * 7000:InvalidRequest
     * 7001:MissingRequiredParameter
     * 7002:InvalidRequiredParameter
     * 8000:BatchNotFound
     * @apiExample  业务说明
     *  根据id查询订单
     * @apiExample 使用表
     *  使用vms_bt_client_orders表
     *  使用vms_bt_client_order_details表
     */
    /**
     * 根据id查询订单
     *
     * @param id id
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.GET_ORDER, method = RequestMethod.GET)
    public ActionResponse getOrderById(@PathVariable String id) {
        return caOrderService.getOrderById(id);
    }

    /**
     * @api {post} /rest/channeladvisor/orders/{id}/acknowledge 确认单个订单
     * @apiName acknowledgeOrder
     * @apiDescription 根据id确认订单
     *
     * @apiGroup OrderApi
     * @apiVersion 2.0.0
     * @apiPermission channeladvisor
     *
     * @apiHeaderExample (系统级参数) {String} SellerID <code>对应平台内部的SellerID(ClientId)</code>
     * @apiHeaderExample (系统级参数) {String} SellerToken <code>对应平台内部的SellerToken(ClientSecret)</code>
     *
     * @apiParam (应用级参数) {String} id <code>id 必填</code>
     *
     * @apiSuccess (返回字段) {String} ResponseBody responseBody
     * @apiSuccess (返回字段) {ResponseStatusEnum} Status status
     * @apiSuccess (返回字段) {Integer} PendingUri PendingUri
     * @apiSuccess (返回字段) {List(ErrorModel)} Errors errors
     * @apiSuccess (返回字段) {boolean} hasErrors hasErrors
     *
     * @apiSuccessExample Success Response
     *{
     *  "ResponseBody": {},
     *  "Status": "Complete",
     *  "PendingUri": null,
     *  "Errors": []
     *}
     * @apiErrorExample  错误示例
     *{
     *  "Status": "Failed",
     *  "PendingUri": null,
     *  "Errors": [
     *    {
     *      "ID": "OrderNotFound",
     *      "ErrorCode": "6000",
     *      "Message": "order id not found."
     *    }
     *  ],
     *  "HasErrors": true,
     *  "ResponseBody": null
     *}
     * @apiErrorExample 错误编码
     * 1000:Info
     * 2000:Warning
     * 3000:SystemFailure
     * 3001:SystemUnavailable
     * 3002:RateLimitExceeded
     * 4000:AuthorizationFailure
     * 4001:InvalidToken
     * 4002:InvalidSellerID
     * 5000:ProductNotFound
     * 5001:ProductMissingRequiredFields
     * 5002:ProductFailedDataValidation
     * 5003:ProductFailedCreate
     * 5004:ProductFailedUpdate
     * 5100:ProductMissingCategory
     * 5101:ProductInvalidCategory
     * 5200:ProductPendingReview
     * 6000:OrderNotFound
     * 6001:InvalidOrder
     * 6002:InvalidOrderStatus
     * 6100:ShipmentFailed
     * 6101:InvalidTrackingNumber
     * 6102:InvalidShippingCarrier
     * 6103:InvalidShippingClass
     * 6200:RefundFailed
     * 6201:UnsupportedRefundReason
     * 6300:CancellationFailed
     * 6301:UnsupportedCancellationReason
     * 7000:InvalidRequest
     * 7001:MissingRequiredParameter
     * 7002:InvalidRequiredParameter
     * 8000:BatchNotFound
     * @apiExample  业务说明
     *  根据id确认订单
     * @apiExample 使用表
     *  使用vms_bt_client_orders表
     *  使用vms_bt_client_order_details表
     */
    /**
     * 确认订单
     *
     * @param id id
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.ACKNOWLEDGE_ORDER, method = RequestMethod.POST)
    public ActionResponse acknowledgeOrder(@PathVariable String id) {
        return caOrderService.acknowledgeOrder(id);
    }

    /**
     * @api {post} /rest/channeladvisor/orders/{id}/ship 运输单个订单
     * @apiName shipOrder
     * @apiDescription 根据id发货订单
     *
     * @apiGroup OrderApi
     * @apiVersion 2.0.0
     * @apiPermission channeladvisor
     *
     * @apiHeaderExample (系统级参数) {String} SellerID <code>对应平台内部的SellerID(ClientId)</code>
     * @apiHeaderExample (系统级参数) {String} SellerToken <code>对应平台内部的SellerToken(ClientSecret)</code>
     *
     * @apiParam (应用级参数) {String} id <code>id 必填</code>
     *
     * @apiSuccess (返回字段) {String} ResponseBody responseBody
     * @apiSuccess (返回字段) {ResponseStatusEnum} Status status
     * @apiSuccess (返回字段) {Integer} PendingUri PendingUri
     * @apiSuccess (返回字段) {List(ErrorModel)} Errors errors
     * @apiSuccess (返回字段) {boolean} hasErrors hasErrors
     *
     * @apiSuccessExample Success Response
     *{
     *  "ResponseBody": {},
     *  "Status": "Complete",
     *  "PendingUri": null,
     *  "Errors": []
     *}
     * @apiErrorExample  错误示例
     *{
     *  "Status": "Failed",
     *  "PendingUri": null,
     *  "Errors": [
     *    {
     *      "ID": "OrderNotFound",
     *      "ErrorCode": "6000",
     *      "Message": "order id not found."
     *    }
     *  ],
     *  "HasErrors": true,
     *  "ResponseBody": null
     *}
     * @apiErrorExample 错误编码
     * 1000:Info
     * 2000:Warning
     * 3000:SystemFailure
     * 3001:SystemUnavailable
     * 3002:RateLimitExceeded
     * 4000:AuthorizationFailure
     * 4001:InvalidToken
     * 4002:InvalidSellerID
     * 5000:ProductNotFound
     * 5001:ProductMissingRequiredFields
     * 5002:ProductFailedDataValidation
     * 5003:ProductFailedCreate
     * 5004:ProductFailedUpdate
     * 5100:ProductMissingCategory
     * 5101:ProductInvalidCategory
     * 5200:ProductPendingReview
     * 6000:OrderNotFound
     * 6001:InvalidOrder
     * 6002:InvalidOrderStatus
     * 6100:ShipmentFailed
     * 6101:InvalidTrackingNumber
     * 6102:InvalidShippingCarrier
     * 6103:InvalidShippingClass
     * 6200:RefundFailed
     * 6201:UnsupportedRefundReason
     * 6300:CancellationFailed
     * 6301:UnsupportedCancellationReason
     * 7000:InvalidRequest
     * 7001:MissingRequiredParameter
     * 7002:InvalidRequiredParameter
     * 8000:BatchNotFound
     * @apiExample  业务说明
     *  根据id发货订单
     * @apiExample 使用表
     *  使用vms_bt_client_orders表
     *  使用vms_bt_client_order_details表
     */
    /**
     * 运送订单
     *
     * @param id      id
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.SHIP_ORDER, method = RequestMethod.POST)
    public ActionResponse shipOrder(@PathVariable String id, @RequestBody ShipRequest request) {
        return caOrderService.shipOrder(id, request);
    }

    /**
     * @api {post} /rest/channeladvisor/orders/{id}/cancel 取消单个订单
     * @apiName cancelOrder
     * @apiDescription 根据id取消订单
     *
     * @apiGroup OrderApi
     * @apiVersion 2.0.0
     * @apiPermission channeladvisor
     *
     * @apiHeaderExample (系统级参数) {String} SellerID <code>对应平台内部的SellerID(ClientId)</code>
     * @apiHeaderExample (系统级参数) {String} SellerToken <code>对应平台内部的SellerToken(ClientSecret)</code>
     *
     * @apiParam (应用级参数) {String} id <code>id 必填</code>
     *
     * @apiSuccess (返回字段) {String} ResponseBody responseBody
     * @apiSuccess (返回字段) {ResponseStatusEnum} Status status
     * @apiSuccess (返回字段) {Integer} PendingUri PendingUri
     * @apiSuccess (返回字段) {List(ErrorModel)} Errors errors
     * @apiSuccess (返回字段) {boolean} hasErrors hasErrors
     *
     * @apiSuccessExample Success Response
     *{
     *  "ResponseBody": {},
     *  "Status": "Complete",
     *  "PendingUri": null,
     *  "Errors": []
     *}
     * @apiErrorExample  错误示例
     *{
     *  "Status": "Failed",
     *  "PendingUri": null,
     *  "Errors": [
     *    {
     *      "ID": "OrderNotFound",
     *      "ErrorCode": "6000",
     *      "Message": "order id not found."
     *    }
     *  ],
     *  "HasErrors": true,
     *  "ResponseBody": null
     *}
     * @apiErrorExample 错误编码
     * 1000:Info
     * 2000:Warning
     * 3000:SystemFailure
     * 3001:SystemUnavailable
     * 3002:RateLimitExceeded
     * 4000:AuthorizationFailure
     * 4001:InvalidToken
     * 4002:InvalidSellerID
     * 5000:ProductNotFound
     * 5001:ProductMissingRequiredFields
     * 5002:ProductFailedDataValidation
     * 5003:ProductFailedCreate
     * 5004:ProductFailedUpdate
     * 5100:ProductMissingCategory
     * 5101:ProductInvalidCategory
     * 5200:ProductPendingReview
     * 6000:OrderNotFound
     * 6001:InvalidOrder
     * 6002:InvalidOrderStatus
     * 6100:ShipmentFailed
     * 6101:InvalidTrackingNumber
     * 6102:InvalidShippingCarrier
     * 6103:InvalidShippingClass
     * 6200:RefundFailed
     * 6201:UnsupportedRefundReason
     * 6300:CancellationFailed
     * 6301:UnsupportedCancellationReason
     * 7000:InvalidRequest
     * 7001:MissingRequiredParameter
     * 7002:InvalidRequiredParameter
     * 8000:BatchNotFound
     * @apiExample  业务说明
     *  根据id取消订单
     * @apiExample 使用表
     *  使用vms_bt_client_orders表
     *  使用vms_bt_client_order_details表
     */
    /**
     * 取消未运送订单
     *
     * @param id      id
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.CANCEL_ORDER, method = RequestMethod.POST)
    public ActionResponse cancelOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {
        return caOrderService.cancelOrder(id, request);
    }

    /**
     * @api {post} /rest/channeladvisor/orders/{id}/refund 退返单个订单
     * @apiName refundOrder
     * @apiDescription 根据id退返订单
     *
     * @apiGroup OrderApi
     * @apiVersion 2.0.0
     * @apiPermission channeladvisor
     *
     * @apiHeaderExample (系统级参数) {String} SellerID <code>对应平台内部的SellerID(ClientId)，存在于http请求头信息header</code>
     * @apiHeaderExample (系统级参数) {String} SellerToken <code>对应平台内部的SellerToken(ClientSecret)，存在于http请求头信息header</code>
     *
     * @apiParam (应用级参数) {String} id <code>id 必填</code>
     *
     * @apiSuccess (返回字段) {String} ResponseBody responseBody
     * @apiSuccess (返回字段) {ResponseStatusEnum} Status status
     * @apiSuccess (返回字段) {Integer} PendingUri PendingUri
     * @apiSuccess (返回字段) {List(ErrorModel)} Errors errors
     * @apiSuccess (返回字段) {boolean} hasErrors hasErrors
     *
     * @apiSuccessExample Success Response
     *{
     *  "ResponseBody": {},
     *  "Status": "Complete",
     *  "PendingUri": null,
     *  "Errors": []
     *}
     * @apiErrorExample  错误示例
     *{
     *  "Status": "Failed",
     *  "PendingUri": null,
     *  "Errors": [
     *    {
     *      "ID": "OrderNotFound",
     *      "ErrorCode": "6000",
     *      "Message": "order id not found."
     *    }
     *  ],
     *  "HasErrors": true,
     *  "ResponseBody": null
     *}
     * @apiErrorExample 错误编码
     * 1000:Info
     * 2000:Warning
     * 3000:SystemFailure
     * 3001:SystemUnavailable
     * 3002:RateLimitExceeded
     * 4000:AuthorizationFailure
     * 4001:InvalidToken
     * 4002:InvalidSellerID
     * 5000:ProductNotFound
     * 5001:ProductMissingRequiredFields
     * 5002:ProductFailedDataValidation
     * 5003:ProductFailedCreate
     * 5004:ProductFailedUpdate
     * 5100:ProductMissingCategory
     * 5101:ProductInvalidCategory
     * 5200:ProductPendingReview
     * 6000:OrderNotFound
     * 6001:InvalidOrder
     * 6002:InvalidOrderStatus
     * 6100:ShipmentFailed
     * 6101:InvalidTrackingNumber
     * 6102:InvalidShippingCarrier
     * 6103:InvalidShippingClass
     * 6200:RefundFailed
     * 6201:UnsupportedRefundReason
     * 6300:CancellationFailed
     * 6301:UnsupportedCancellationReason
     * 7000:InvalidRequest
     * 7001:MissingRequiredParameter
     * 7002:InvalidRequiredParameter
     * 8000:BatchNotFound
     * @apiExample  业务说明
     *  根据id退返订单
     * @apiExample 使用表
     *  使用vms_bt_client_orders表
     *  使用vms_bt_client_order_details表
     */
    /**
     * 退返订单
     *
     * @param id      id
     * @param request request
     * @return response
     */
    @RequestMapping(value = CAUrlConstants.ORDERS.REFUND_ORDER, method = RequestMethod.POST)
    public ActionResponse refundOrder(@PathVariable String id, @RequestBody OrderCancellationRequest request) {
        return caOrderService.refundOrder(id, request);
    }

    @RequestMapping(value = CAUrlConstants.ORDERS.ERROR_PATH_ORDER, method = RequestMethod.POST)
    public ActionResponse errorPathOrder(@PathVariable String id) {
        throw new CAApiException(ErrorIDEnum.InvalidRequest,"The endpoint URL does not exist. Please check the URL.");
    }

}

