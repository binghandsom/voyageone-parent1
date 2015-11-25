/**
 * @Name:    module.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define (function (require) {

    require ('components/services/ajax.service');
    require ('components/services/cookie.service');
    require ('components/services/alert.service');
    require ('components/services/language.service');
    require ('components/services/message.service');
    require ('components/services/permission.service');
    require ('components/services/translate.service');

    var omsApp = angular.module ('omsModule', ['mainModule']);
    omsApp.constant ('omsAction', {

        /** oms 共通调用的url start **/
        'oms_common_service_doGetCode': '/oms/common/service/doGetCode',
        'oms_common_service_doGetSKUInfo': '/oms/common/service/doGetSKUInfo',
        /** oms 共通调用的url end **/

        'oms_default_index_doInit': '/oms/default/index/doInit',

        'oms_orders_search_doInit': '/oms/orders/search/doInit',
        'oms_orders_search_doSearch': '/oms/orders/search/doSearch',

        'oms_orders_index_doInit': '/oms/orders/index/doInit',

        /** oms order detail 页面使用 start **/
        'oms_orders_orderdetail_doInit': '/oms/orders/orderdetail/doInit',
        'oms_orders_orderdetail_doLockOrUnlockOrder': '/oms/orders/orderdetail/doLockOrUnlockOrder',
        'oms_orders_orderdetail_doApprove': '/oms/orders/orderdetail/doApprove',
        'oms_orders_orderdetail_doUpdateCustomerComment': '/oms/orders/orderdetail/doUpdateCustomerComment',
        'oms_orders_orderdetail_doUpdateInternalMessage': '/oms/orders/orderdetail/doUpdateInternalMessage',
        'oms_orders_orderdetail_doUpdateGiftMessage': '/oms/orders/orderdetail/doUpdateGiftMessage',
        'oms_orders_orderdetail_doUpdateInvoice': '/oms/orders/orderdetail/doUpdateInvoice',
        'oms_orders_orderdetail_doApprovePriceDiffOrder': '/oms/orders/orderdetail/doApprovePriceDiffOrder',
        'oms_orders_orderdetail_doUpdateOrderOtherProp': '/oms/orders/orderdetail/doUpdateOrderOtherProp',
        'oms_orders_orderdetail_doUpdateOrderCustomerRefused': '/oms/orders/orderdetail/doUpdateOrderCustomerRefused',
        'oms_orders_orderdetail_doCancelClientOrder': '/oms/orders/orderdetail/doCancelClientOrder',

        /* popup 画面使用 start */
        'oms_orders_orderdetail_doCancelOrder': '/oms/orders/orderdetail/doCancelOrder',
        'oms_orders_orderdetail_doCancelLineItems': '/oms/orders/orderdetail/doCancelLineItems',
        'oms_orders_orderdetail_doSaveOrderDetailDiscount': '/oms/orders/orderdetail/doSaveOrderDetailDiscount',
        'oms_orders_orderdetail_doReturnLineItem': '/oms/orders/orderdetail/doReturnLineItem',
        'oms_orders_orderdetail_doSaveAdjustment': '/oms/orders/orderdetail/doSaveAdjustment',
        'oms_orders_orderdetail_doGetCustomerInfoForSold': '/oms/orders/orderdetail/doGetCustomerInfoForSold',
        'oms_orders_orderdetail_updateAddress': '/oms/orders/orderdetail/updateAddress',
        'oms_orders_orderdetail_doUpdateShipAddress': '/oms/orders/orderdetail/doUpdateShipAddress',
        'oms_orders_orderdetail_doSaveNotes': '/oms/orders/orderdetail/doSaveNotes',
        'oms_orders_orderdetail_doPreApprovePriceDiffOrder': '/oms/orders/orderdetail/doPreApprovePriceDiffOrder',
        // 退款使用
        'oms_orders_orderdetail_doInitRefund': '/oms/orders/orderdetail/doInitRefund',
        'oms_orders_orderdetail_doChangeRefund': '/oms/orders/orderdetail/doGetRefundMessages',
        'oms_orders_orderdetail_doAddMessage': '/oms/orders/orderdetail/doAddMessage',

        'oms_orders_orderdetail_doAgreeRefund': '/oms/orders/orderdetail/doRefundsAgree',
        'oms_orders_orderdetail_doRefundsAgreeSynOMS': '/oms/orders/orderdetail/doRefundsAgreeSynOMS',
        'oms_orders_orderdetail_doAgreeRefundCN': '/oms/orders/orderdetail/doRefundsAgreeCN',
        'oms_orders_orderdetail_doRefuseRefund': '/oms/orders/orderdetail/doRefundRefuse',
        'oms_orders_orderdetail_doAgreeReturnGoods': '/oms/orders/orderdetail/doReturnGoodsAgree',
        'oms_orders_orderdetail_doRefuseReturnGoods': '/oms/orders/orderdetail/doReturnGoodsRefuse',
        'oms_orders_orderdetail_doRefillReturnGoods': '/oms/orders/orderdetail/doReturnGoodsRefill',
        /* popup 画面使用 end */
        /** oms order detail 页面使用 end **/

        /** oms new order 页面使用 start **/
        // 用于order detail 页面跳转到new order页面
        'oms_orders_addneworder_doInit': '/oms/orders/addneworder/doInit',
        /** oms new order 页面使用 end **/


        'oms_orders_addneworder_doGetCustomerInfo': '/oms/orders/addneworder/doGetCustomerInfo',
        'oms_orders_addneworder_doSaveOrder': '/oms/orders/addneworder/doSaveOther',
        'oms_orders_addneworder_doSaveOrderOriginal': '/oms/orders/addneworder/doSaveOriginal',
        'oms_orders_addneworder_doInitNewOrder': '/oms/orders/addneworder/doInitNewOrder',
        'oms_orders_orderdetail_doUpdateShipping': '/oms/orders/orderdetail/doUpdateShipping',
        'oms_orders_orderdetail_doRevertOrder': '/oms/orders/orderdetail/doRevertOrder',
        'oms_orders_orderdetail_doUnReturnLineItem': '/oms/orders/orderdetail/doUnReturnLineItem',
        //'oms_orders_addneworder_doGetCustomerInfo': '/oms/orders/addneworder/doGetCustomerInfo',

        /** accounting 页面使用 start **/
        'oms_orders_accounting_doSaveAccountingFile': '/oms/orders/accounting/doSaveAccountingFile',
        'oms_orders_accounting_doInit': '/oms/orders/accounting/doInit',
        'oms_orders_accounting_doSearchSettlementFile': '/oms/orders/accounting/doSearchSettlementFile',
        /** accounting 页面使用 end **/

        /** rate 页面使用 start **/
        'oms_orders_accounting_doSaveRate': '/oms/orders/accounting/doSaveRate',
        'oms_orders_accounting_doSearchRate': '/oms/orders/accounting/doSearchRate',
        /** rate 页面使用 end **/

        /** customer 页面使用 start **/
        'oms_customer_index_doInit': '/oms/customer/index/doInit',

        'oms_customer_search_doSearch': '/oms/customer/customerdetail/doCustomerSearch',
        'oms_customer_doDetailSearch': '/oms/customer/customerdetail/doCustomerDetailSearch',
        'oms_customer_doSaveNotes': '/oms/customer/customerdetail/doCustomerNotesAddOrEdit',
        'oms_customer_doInit':'/oms/customer/customerdetail/doCustomerInit'
        
    });

    /**
     * master数据的种类..
     */
    omsApp.constant ('omsType', {
        'quickFilter': '1',
        'shippingMethod': '2',
        'paymentMethod': '3',
        'itemStatus': '4',
        'orderStatus': '5',
        'invoice': '6',
        'localShipOnHold': '7',
        'freightByCustomer': '8',
        'shopCart': '9',
        'country': '10',
        'orderKind': '11',
        'invoiceKind': '27',
        'expressCode': '31',
        'currencyType': '36'
    });

    /**
     * 订单状态.
     */
    omsApp.constant ('orderStatus', {
        //Approved: "01",
        //Shipped: "02",
        //Returned: "03",
        //Cancelled: "04"
        InProcessing: "In Processing",
        Approved: "Approved",
        Shipped: "Shipped",
        Returned: "Returned",
        Cancelled: "Canceled"
    });

    /**
     * 订单种类.
     */
    omsApp.constant ('orderKinds', {
        OriginalOrder: "0",
        splitOrder: "1",
        presentOrder: "2",
        returnOrder: "3",
        priceDifferenceOrder: "4"
    });

    /**
     * cartId.
     */
    omsApp.constant ('cartId', {
        Tmall: "20",
        Taobao: "21",
        Offline: "22",
        TmallG: "23",
        JD: "24",
        CN: "25",
        JG: "26"
    });

    /**
     * platformId.
     */
    omsApp.constant ('platformId', {
        Ali: "1",
        JD: "2",
        CN: "3"
    });

    /**
     * oms的所有popup画面一览.
     */
    omsApp.constant ("omsPopupPages", {
        "popBindOrder": {
            "page": "/VoyageOne/modules/oms/popup/popBindOrder.dialog.tpl.html",
            "controller": "popBindOrderController"
        },
        "popAddManualOrder": {
            "page": "/VoyageOne/modules/oms/popup/popAddManualOrder.dialog.tpl.html",
            "controller": "popAddManualOrderController"
        },
        "popCancelOrder": {
            "page": '/VoyageOne/modules/oms/popup/popCancelOrder.dialog.tpl.html',
            "controller": 'popCancelOrderController'
        },
        "popReturnOrder": {
            "page": '/VoyageOne/modules/oms/popup/popReturnOrder.dialog.tpl.html',
            "controller": 'popReturnOrderController'
        },
        "popRefundOrder": {
            "page": '/VoyageOne/modules/oms/popup/popRefundOrder.dialog.tpl.html',
            "controller": 'popRefundOrderController'
        },
        "popRefundOrderCN": {
            "page": '/VoyageOne/modules/oms/popup/popRefundOrderCN.dialog.tpl.html',
            "controller": 'popRefundOrderCNController'
        },
        "popAddDiscountOrder": {
            "page": '/VoyageOne/modules/oms/popup/popAddDiscountOrder.dialog.tpl.html',
            "controller": 'popAddDiscountOrderController'
        },
        "popAddShipmentOrder": {
            "page": '/VoyageOne/modules/oms/popup/popAddShipmentOrder.dialog.tpl.html',
            "controller": 'popAddShipmentOrderController'
        },
        "popEditSoldToAddress": {
            "page": '/VoyageOne/modules/oms/popup/popEditSoldToAddress.dialog.tpl.html',
            "controller": 'popEditSoldToAddressController'
        },
        "popEditShipToAddress": {
            "page": '/VoyageOne/modules/oms/popup/popEditShipToAddress.dialog.tpl.html',
            "controller": 'popEditShipToAddressController'
        },
        "popAddNoteOrder": {
            "page": '/VoyageOne/modules/oms/popup/popAddNoteOrder.dialog.tpl.html',
            "controller": 'popAddNoteOrderController'
        },
        "popSearchSku": {
            "page": '/VoyageOne/modules/oms/popup/popSearchSku.dialog.tpl.html',
            "controller": 'popSearchSkuController'
        },
        "popSearchCustomer": {
            "page": '/VoyageOne/modules/oms/popup/popSearchCustomer.dialog.tpl.html',
            "controller": 'popSearchCustomerController'
        },
        "popAddNoteCustomer": {
            "page": '/VoyageOne/modules/oms/popup/popAddNoteCustomer.dialog.tpl.html',
            "controller": 'popAddNoteCustomerController'
        },
        "popMessage": {
            "page": "/VoyageOne/modules/oms/popup/popMessage.dialog.tpl.html",
            "controller": "popMessageController"
        },
        "popConfirmBindOrder": {
            "page": "/VoyageOne/modules/oms/popup/popConfirmBindOrder.dialog.tpl.html",
            "controller": "popConfirmBindOrderController"
        }
    });

    return omsApp;
})
;