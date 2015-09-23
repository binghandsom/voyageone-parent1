/**
 * @Name:    popup.service.js
 * @Date:    2015/5/6
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var omsApp = require ('modules/oms/oms.module');

    omsApp.service ('popSearchCustomerService', ['$q', 'omsAction', 'ajaxService', 'omsType', 'omsCommonService',
        function ($q, omsAction, ajaxService, omsType, omsCommonService) {

            /**
             * get the code list for this page.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCodeList = function () {
                var defer = $q.defer ();
                var codeTypeList = [];
                codeTypeList.push ({id: omsType.country, showBlank: true});

                omsCommonService.doGetCodeList (codeTypeList)
                    .then (function (data) {
                    defer.resolve (data);
                });

                return defer.promise;
            };

            /**
             * 检索出客户信息.
             * @param data
             * @returns {*}
             */
            this.doGetCustomerList = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_addneworder_doGetCustomerInfo)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

        }]);

    omsApp.service ('popAddNoteCustomerService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {


            //保存客户Notes
            this.doSaveNote = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_customer_doSaveNotes)
                    .then (
                    function (response) {
                        defer.resolve (response);
                    });
                return defer.promise;
            };
        }]);

    omsApp.service ('popSearchSkuService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {


            //保存客户Notes
            this.doGetSkuList = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_common_service_doGetSKUInfo)
                    .then (
                    function (response) {
                        defer.resolve (response.data);
                    });
                return defer.promise;
            };
        }]);

    omsApp.service ('popAddManualOrderService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {


            //保存客户Notes
            this.doGetOrderList = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_addneworder_doInit)
                    .then (
                    function (response) {
                        defer.resolve (response.data);
                    });
                return defer.promise;
            };
        }]);

    omsApp.service ('popCancelOrderService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {

            /**
             * 取消整个订单
             * @param data
             * @returns {promise|*|r.promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doCancelOrder = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doCancelOrder)
                    .then (
                    function (response) {
                        defer.resolve (response.data);
                    });
                return defer.promise;
            };

            /**
             * 取消sku
             * @param data
             * @returns {*}
             */
            this.doCancelOrderDetail = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doCancelLineItems)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popReturnOrderService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {

            /**
             * 处理退货sku.
             * @param data
             * @returns {*}
             */
            this.doReturnOrderDetail = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doReturnLineItem)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popDiscountOrderService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {

            /**
             * 处理order的打折
             * @param data
             * @returns {*}
             */
            this.doDiscountOrder = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doSaveAdjustment)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * get the orderDetail info from server.
             * @param orderNumber
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doDiscountOrderDetail = function (data) {

                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doSaveOrderDetailDiscount)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popShippingOrderService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {

            /**
             * 处理order的shipping fare
             * @param data
             * @returns {*}
             */
            this.doShippingFareOrder = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doSaveAdjustment)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popSoldToAddressService', ['$q', 'omsAction', 'ajaxService', 'omsType', 'omsCommonService',
        function ($q, omsAction, ajaxService, omsType, omsCommonService) {

            /**
             * get the code list for this page.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCodeList = function () {
                var defer = $q.defer ();
                var codeTypeList = [];
                codeTypeList.push ({id: omsType.country, showBlank: true});

                omsCommonService.doGetCodeList (codeTypeList)
                    .then (function (data) {
                    defer.resolve (data);
                });

                return defer.promise;
            };

            /**
             * 根据客户Id取得客户信息
             * @param data
             * @returns {*}
             */
            this.doGetCustomerInfo = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doGetCustomerInfoForSold)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * 更新顾客地址以及该订单的Sold To地址.
             * @param data
             * @returns {*}
             */
            this.doUpdateSoldToAddress = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_updateAddress)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popShipToAddressService', ['$q', 'omsAction', 'ajaxService', 'omsType', 'omsCommonService',
        function ($q, omsAction, ajaxService, omsType, omsCommonService) {

            /**
             * get the code list for this page.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCodeList = function () {
                var defer = $q.defer ();
                var codeTypeList = [];
                codeTypeList.push ({id: omsType.country, showBlank: true});

                omsCommonService.doGetCodeList (codeTypeList)
                    .then (function (data) {
                    defer.resolve (data);
                });

                return defer.promise;
            };

            /**
             * 更新该订单额Ship To地址
             * @param data
             * @returns {*}
             */
            this.doUpdateShipToAddress = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doUpdateShipAddress)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popBindOrderService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {

            /**
             * 取得被绑定order的信息
             * @param data
             * @returns {*}
             */
            this.getConfrimBindPriceDiffOrder = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doPreApprovePriceDiffOrder)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * Bind差价订单到主订单
             * @param data
             * @returns {*}
             */
            this.doBindPriceDiffOrder = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doApprovePriceDiffOrder)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popAddNoteOrderService', ['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {

            /**
             * doSaveNotes
             * @param data
             * @returns {*}
             */
            this.doSaveNotes = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doSaveNotes)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);

    omsApp.service ('popRefundOrderService', ['$q', 'omsAction', 'ajaxService', 'omsCommonService', 'omsType',
        function ($q, omsAction, ajaxService, omsCommonService, omsType) {

	        /**
	         * doInitRefund
	         * @param data
	         * @returns {*}
	         */
	        this.doInitRefund = function (data) {
	            var defer = $q.defer ();
	            ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doInitRefund)
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
                codeTypeList.push ({id: omsType.expressCode, showBlank: true});
                omsCommonService.doGetCodeList (codeTypeList)
                    .then (function (data) {
                    defer.resolve (data);
                });
                return defer.promise;
            };

            /**
             * doChangeRefund
             * @param data
             * @returns {*}
             */
            this.doChangeRefund = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doChangeRefund)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doAgreeRefund
             * @param data
             * @returns {*}
             */
            this.doAgreeRefund = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doAgreeRefund)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doAgreeRefundSynOMS
             * @param data
             * @returns {*}
             */
            this.doAgreeRefundSynOMS = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doRefundsAgreeSynOMS)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doAgreeRefundCN
             * @param data
             * @returns {*}
             */
            this.doAgreeRefundCN = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doAgreeRefundCN)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doRefundRefuse
             * @param data
             * @returns {*}
             */
            this.doRefundRefuse = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doRefuseRefund)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doAgreeReturnGoods
             * @param data
             * @returns {*}
             */
            this.doAgreeReturnGoods = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doAgreeReturnGoods)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doRefuseReturnGoods
             * @param data
             * @returns {*}
             */
            this.doRefuseReturnGoods = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doRefuseReturnGoods)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

            /**
             * doRefillReturnGoods
             * @param data
             * @returns {*}
             */
            this.doRefillReturnGoods = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, omsAction.oms_orders_orderdetail_doRefillReturnGoods)
                    .then (function (response) {
                    defer.resolve (response.data);
                });
                return defer.promise;
            };
        }]);
});