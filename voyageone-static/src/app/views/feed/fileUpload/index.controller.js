define([
    'vms'
], function (vms) {
    vms.controller('FeedFileUploadController', (function () {

        function FeedFileUploadController(alert, notify, orderListService, popups) {
            this.alert = alert;
            this.notify = notify;
            this.orderListService = orderListService;
            this.popups = popups;
        }
        return FeedFileUploadController;

    }()));
});