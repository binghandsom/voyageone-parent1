/**
 * @Name:    searchService.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define (function (require) {

    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/common/common.service');

    omsApp.service ('searchService', [
        '$q', 'omsAction', 'ajaxService', 'omsType', 'omsCommonService',
        function ($q, omsAction, ajaxService, omsType, omsCommonService) {

            /**
             * page initialize
             * @returns {*}
             */
            this.doGetInitData = function () {
                var defer = $q.defer ();
                ajaxService.ajaxPostOnlyByUrl (omsAction.oms_orders_search_doInit)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * get the code list for this page.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCodeList = function () {
                var defer = $q.defer ();
                var codeTypeList = [];
                codeTypeList.push ({id: omsType.quickFilter, showBlank: true});
                codeTypeList.push ({id: omsType.orderStatus, showBlank: true});
                codeTypeList.push ({id: omsType.localShipOnHold, showBlank: true});
                codeTypeList.push ({id: omsType.invoice, showBlank: true});
                codeTypeList.push ({id: omsType.itemStatus, showBlank: true});
                codeTypeList.push ({id: omsType.shippingMethod, showBlank: true});
                codeTypeList.push ({id: omsType.paymentMethod, showBlank: true});
                codeTypeList.push ({id: omsType.freightByCustomer, showBlank: true});

                omsCommonService.doGetCodeList (codeTypeList)
                    .then (function (data) {
                    defer.resolve (data);
                });

                return defer.promise;
            };

            /**
             * 检索操作
             * @param data
             * @returns {*}
             */
            this.doSearch = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_search_doSearch)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('orderDetailService', [
        '$q', 'omsAction', 'ajaxService', 'omsType', 'omsCommonService',
        function ($q, omsAction, ajaxService, omsType, omsCommonService) {

            var _ = require ('underscore');

            /**
             * get the code list for this page.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCodeList = function () {
                var defer = $q.defer ();
                var codeTypeList = [];
                codeTypeList.push ({id: omsType.invoice, showBlank: true});
                codeTypeList.push ({id: omsType.shippingMethod, showBlank: false});
                codeTypeList.push ({id: omsType.invoiceKind, showBlank: true});

                omsCommonService.doGetCodeList (codeTypeList)
                    .then (function (data) {
                    defer.resolve (data);
                });
                return defer.promise;
            };

            /**
             * get the orderDetail info from server.
             * @param sourceOrderId
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetOrderDetail = function (sourceOrderId) {

                var data = {};
                data.sourceOrderId = sourceOrderId;

                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doInit)
                    .then (function (response) {
                    //addSelectedInOrderHistoryList(response.data.orderInfo.orderNumber, response.data.orderHistoryList);
                    translateOrderNumberHistoryTree (response.data);
                    defer.resolve (response.data);
                })
                    .then (function (data) {
                    defer.resolve (data);
                });
                return defer.promise;
            };

            /**
             * change order lock status.
             * @param editOrderLockStatus
             * @returns {promise|*|r.promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doLockOrUnlockOrder = function (editOrderLockStatus) {

                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (editOrderLockStatus, omsAction.oms_orders_orderdetail_doLockOrUnlockOrder)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * 更新commment栏目中的customer comment.
             * @param data
             * @returns {promise|*|r.promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doUpdateOrderCustomerComment = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateCustomerComment)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doUpdateInternalMessage
             * @param data
             * @returns {*}
             */
            this.doUpdateInternalMessage = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateInternalMessage)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doUpdateGiftMessage
             * @param data
             * @returns {*}
             */
            this.doUpdateGiftMessage = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateGiftMessage)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doUpdateInvoice
             * @param data
             * @returns {*}
             */
            this.doUpdateInvoice = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateInvoice)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
            
            /**
             * doUpdateShipping
             * @param data
             * @returns {*}
             */
            this.doUpdateShipping = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateShipping)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
            
            /**
             * doUpdateOrderOtherProp
             * @param data
             * @returns {*}
             */
            this.doUpdateOrderOtherProp = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateOrderOtherProp)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doUpdateOrderCustomerRefused
             * @param data
             * @returns {*}
             */
            this.doUpdateOrderCustomerRefused = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateOrderCustomerRefused)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doCancelClientOrder
             * @param data
             * @returns {*}
             */
            this.doCancelClientOrder = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doCancelClientOrder)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * approve order.
             * @param data
             * @returns {promise|*|r.promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doApprove = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doApprove)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * translate the orderNumber history to nav-tree's style.
             * @param data
             */
            function translateOrderNumberHistoryTree (data) {

                var orderHistoryTreeList = [];
                //var orderHistoryForTreeLink = [];

                _.each (data.orderHistoryList, function (orderHistoryInfo) {
                    var orderNumbers = [];
                    _.each (orderHistoryInfo.orderNumbers, function (orderNumberInfo) {
                        orderNumbers.push ({label: orderNumberInfo.orderNumber, sourceOrderId: orderNumberInfo.sourceOrderId});
                    });
                    orderHistoryTreeList.push ({label: orderHistoryInfo.orderDate, children: orderNumbers});
                });

                data.orderHistoryTreeList = orderHistoryTreeList;
                //data.orderHistoryForTreeLink = orderHistoryForTreeLink;
            }
        }]);

    omsApp.service ('addNewOrderService', ['$q', 'omsAction', 'omsType', 'ajaxService', 'omsCommonService',
        function ($q, omsAction, omsType, ajaxService, omsCommonService) {

            /**
             * get the code list for this page.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCodeList = function () {
                var defer = $q.defer ();

                var codeTypeList = [];
                codeTypeList.push ({id: omsType.country, showBlank: false});
                codeTypeList.push ({id: omsType.invoice, showBlank: true});
                codeTypeList.push ({id: omsType.orderKind, showBlank: false});

                omsCommonService.doGetCodeList (codeTypeList)
                    .then (function (data) {
                    defer.resolve (data);
                });
                return defer.promise;
            };

            /**
             * get the stores by user's company.
             * @returns {promise|*|r.promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetStoreList = function () {
                var defer = $q.defer ();

                ajaxService.ajaxPostOnlyByUrl (omsAction.oms_orders_addneworder_doInitNewOrder)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /***
             * doSaveOrder
             * 保存新建的订单
             * @param data
             * @param scope
             * @returns {promise|*|r.promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doSaveOrder = function (data, scope) {
                var defer = $q.defer ();
                ajaxService.ajaxPost (data, omsAction.oms_orders_addneworder_doSaveOrder, scope)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /***
             * doSaveOrder
             * 保存新建的订单
             * @param data
             * @param scope
             * @returns {promise|*|r.promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doSaveOrderOriginal = function (data, scope) {
                var defer = $q.defer ();
                ajaxService.ajaxPost (data, omsAction.oms_orders_addneworder_doSaveOrderOriginal, scope)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };


        }]);

    omsApp.service ('orderIndexService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {

            /**
             * check the login user access.
             * @returns {*}
             */
            this.doInit = function () {
                var defer = $q.defer ();
                ajaxService.ajaxPostOnlyByUrl (omsAction.oms_orders_index_doInit)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

        }]);

    omsApp.service ('accountingService', ['$q', 'omsAction', 'omsType', 'ajaxService', 'omsCommonService',
        function ($q, omsAction, omsType, ajaxService, omsCommonService) {

            /**
             * page initialize
             * @returns {*}
             */
            this.doGetInitData = function () {
                var defer = $q.defer ();
                ajaxService.ajaxPostOnlyByUrl (omsAction.oms_orders_accounting_doInit)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * 检索操作
             * @param data
             * @returns {*}
             */
            this.doSearch = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_accounting_doSearchSettlementFile)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);
});
