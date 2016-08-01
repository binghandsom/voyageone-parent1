/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('HomeController', (function () {
        function HomeController(homeInfoService) {
            this.homeInfoService = homeInfoService;
            this.countOrder = 0;
            this.countSku = 0
            this.countReceiveErrorShipment = 0;;
        }
        HomeController.prototype = {
            init: function () {
                var main = this;
                main.homeInfoService.init().then(function (res) {
                    main.countOrder = res.countOrder;
                    main.countSku = res.countSku;
                    main.countReceiveErrorShipment = res.countReceiveErrorShipment;
                })
            },
            gotoShipmentInfo: function () {
                searchInfo = {
                    status: "6",
                };
                window.sessionStorage.setItem('shipmentSearchInfo', JSON.stringify(searchInfo));
                window.location.href = "#/shipment/shipment_info";
            }
        };

        return HomeController;
    }()));
});