define([
    'vms',
    'directives/angularBarcode.directive'
], function (vms) {
    vms.controller('OrderInfoController', (function () {

        function OrderInfoController(alert, notify, confirm, popups, orderInfoService, shipmentScanPopupService) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.orderInfoService = orderInfoService;
            this.popups = popups;
            this.shipmentScanPopupService = shipmentScanPopupService;
            this.barcodeOpts = {
                width:2.5,
                height:120,
                displayValue: true,
                fontSize: 24
            };
            this.oneDay = 24 * 60 * 60 * 1000;
            this.twoDay = 2 * this.oneDay;
            this.threeDay = 3 * this.oneDay;
            this.oneMonth = 30 * this.oneDay;

            var now = new Date();
            var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            this.orderDateFrom = new Date(today.getTime() - 6 * this.oneMonth);
            this.orderDateTo = today;

            this.pageInfo = {
                curr: 1,
                total: 0,
                size: 10,
                fetch: this.search.bind(this)
            };

            this.searchInfo = {
                curr: this.pageInfo.curr,
                total: this.pageInfo.total,
                size: this.pageInfo.size,
                status: "1",
                consolidationOrderId: "",
                sku: "",
                orderDateFrom: "",
                orderDateTo: "",
                sortParamBean: {
                    columnName: "consolidation_order_time",
                    direction: "ASC"
                }
            };

            this.sortParamBeanForDownload  = {
                columnName: "consolidation_order_id",
                direction: "ASC"
            };

            this.channelConfig = {
                vendorOperateType: 'SKU'
            };
            this.orderStatusList = [];
            this.data = [];
            this.currentShipment = undefined;
        }

        OrderInfoController.prototype.init = function () {
            var self = this;
            self.orderInfoService.init().then(function (data) {
                // 获取当前shipment
                self.currentShipment = data.currentShipment;
                // 获取可选的订单状态
                self.orderStatusList = data.orderStatusList;
                //获取shipment状态
                self.shipmentStatusList = data.shipmentStatusList;
                // 记录用户的操作方式(sku/order)
                self.channelConfig = data.channelConfig;

                var sessionSearchInfo = JSON.parse(sessionStorage.getItem('orderSearchInfo'));
                if (sessionSearchInfo) {
                    self.searchInfo = sessionSearchInfo;
                    if (sessionSearchInfo.orderDateFrom)
                        self.orderDateFrom = new Date(sessionSearchInfo.orderDateFrom);
                    if (sessionSearchInfo.orderDateTo)
                        self.orderDateTo = new Date(sessionSearchInfo.orderDateTo);
                }
                self.search();
            });
        };

        OrderInfoController.prototype.search = function () {
            var self = this;
            self.manageSearchInfo();
            self.orderInfoService.search(self.searchInfo).then(function (data) {
                self.pageInfo.total = data.orderInfo.total;
                self.data = data.orderInfo.orderList.map(function (item) {
                        item.className = 'bg-default';
                        item.subClassName = 'bg-sub-default';
                        item.collapse = self.collapse;
                        var date = new Date();
                        if (item.status == '7') {
                            item.className = 'bg-gainsboro';
                            item.subClassName = 'bg-sub-gainsboro';
                        }
                        else if (item.status == '1') {
                            if (self.channelConfig.vendorOperateType == 'ORDER') {
                                date = new Date(item.consolidationOrderTime);
                            } else if (self.channelConfig.vendorOperateType == 'SKU') {
                                date = new Date(item.consolidationOrderTime);
                            } else {
                                self.alert('TXT_MISSING_REQUIRED_CHANNEL_CONFIG');
                            }
                            if ((new Date().getTime() - date.getTime()) >= self.threeDay) {
                                item.className = 'bg-danger';
                                item.subClassName = 'bg-sub-danger';
                            } else if ((new Date().getTime() - date.getTime()) >= self.twoDay) {
                                item.className = 'bg-warning';
                                item.subClassName = 'bg-sub-warning';
                            }
                        }
                        return item;
                    }
                )
            })
        };

        OrderInfoController.prototype.cancelOrder = function (item) {
            var self = this;
            self.confirm('TXT_CONFIRM_TO_CANCEL_ORDER').then(function () {
                self.orderInfoService.cancelOrder(item).then(function () {
                    self.notify.success('TXT_CANCELLED');
                    self.search()
                })
            })
        };

        OrderInfoController.prototype.cancelSku = function (item) {
            var self = this;
            self.confirm('TXT_CONFIRM_TO_CANCEL_SKU').then(function () {
                self.orderInfoService.cancelSku(item).then(function (data) {
                    if (data.success > 0)
                        self.notify.success('TXT_CANCELLED');
                    else self.notify.danger('TXT_ERROR');
                    self.search();
                })
            })
        };

        OrderInfoController.prototype.downloadPickingList = function () {
            var self = this;
            self.manageSearchInfo();
            var req = angular.copy(self.searchInfo);
            if ('ORDER' == self.channelConfig.vendorOperateType)
                req.sortParamBean = self.sortParamBeanForDownload;
            $.download.post('/vms/order/order_info/downloadPickingList', {"searchInfo": JSON.stringify(req)}, self.afterDownload, self);
        };

        OrderInfoController.prototype.afterDownload = function (responseContent, param, context) {
            if (!responseContent) return;
            var res = JSON.parse(responseContent);
            if (res.message != '') {
                context.alert(res.message);
            }
        };

        OrderInfoController.prototype.toggleAll = function () {
            var self = this;
            var collapse = (self.collapse = !self.collapse);
            self.data.forEach(function (item) {
                item.collapse = collapse;
            });
        };

        OrderInfoController.prototype.getStatusName = function (statusValue) {
            var self = this;
            for (var i = 0; i < self.orderStatusList.length; i++) {
                if (self.orderStatusList[i].value == statusValue) {
                    var currentStatus = self.orderStatusList[i];
                    break;
                }
            }
            // Receive with Error 太长了
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        OrderInfoController.prototype.popShipment = function (type) {
            var self = this;
            //1:Open；3：Shipped；4：Arrived；5：Recevied；6：Receive with Error
            var pendingShipmentStatus = "1";
            if (type == "edit") {
                pendingShipmentStatus = self.currentShipment.status;
            } else if (type == "end") {
                pendingShipmentStatus = "3";
            }
            var shipmentInfo = {
                shipment: self.currentShipment,
                type: type,
                pendingShipmentStatus: pendingShipmentStatus,
                statusList: this.shipmentStatusList,
                channelConfig: this.channelConfig
            };

            this.popups.openShipment(shipmentInfo).then(function (shipment) {
                self.currentShipment = shipment;
                if (type == "new") {
                    var url = '#/shipment/shipment_info/shipment_detail/' + shipment.id;
                    location.href = url;
                }
            });
        };

        OrderInfoController.prototype.popAddToShipmentForSku = function () {
            var url = '#/shipment/shipment_info/shipment_detail/' + this.currentShipment.id;
            location.href = url;
        }

        OrderInfoController.prototype.popAddToShipmentForOrder = function (item) {
            var self = this;
            self.scanPopupInitialInfo = {
                "shipment": self.currentShipment,
                "consolidationOrderId": item.consolidationOrderId
            };
            self.shipmentScanPopupService.init(self.scanPopupInitialInfo).then(function (shipmentDetails) {
                shipmentDetails.scanPopupInitialInfo = self.scanPopupInitialInfo;
                self.popups.openAddShipment(shipmentDetails).then(function () {
                    self.init();
                });
            });
        };

        OrderInfoController.prototype.printLabel = function (index) {
            var self = this;
            var canvas = $('#label' + index).find('canvas').get(0);
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            var img = canvas.toDataURL("image/png");
            popupWin.document.write('<div><strong style="font-size: 32px;">[Order' + ' No.] ' + self.data[index].consolidationOrderId+'</strong></div><hr><img src="'+img+'"/>');
            popupWin.document.close();
            popupWin.print();
            popupWin.close();
        };

        OrderInfoController.prototype.manageSearchInfo = function () {
            var self = this;
            if (self.orderDateFrom === undefined || self.orderDateTo === undefined) {
                self.alert("TXT_PLEASE_INPUT_A_VALID_DATE");
                return;
            } else if (self.orderDateFrom) {
                self.searchInfo.orderDateFrom = self.orderDateFrom;
            } else self.searchInfo.orderDateFrom = undefined;
            if (self.orderDateTo) {
                var date = angular.copy(self.orderDateTo);
                date.setDate(date.getDate() + 1);
                self.searchInfo.orderDateTo = date;
            } else {
                self.searchInfo.orderDateTo = undefined;
            }
            self.searchInfo.curr = self.pageInfo.curr;
            self.searchInfo.size = self.pageInfo.size;
            sessionStorage.setItem('orderSearchInfo', JSON.stringify(self.searchInfo));
        };

        OrderInfoController.prototype.changeSearchOrder = function (columnName) {
            var self = this;
            if (columnName == self.searchInfo.sortParamBean.columnName) {
                if ('ASC' == self.searchInfo.sortParamBean.direction)
                    self.searchInfo.sortParamBean.direction = 'DESC';
                else self.searchInfo.sortParamBean.direction = 'ASC';
            } else {
                self.searchInfo.sortParamBean.columnName = columnName;
                self.searchInfo.sortParamBean.direction = 'ASC';
            }
            self.search();
        };

        return OrderInfoController;
    }()));
});
