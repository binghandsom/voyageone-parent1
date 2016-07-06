/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('FeedImportResultController', (function () {

        function FeedImportResultController(alert, notify, orderListService, popups) {
            this.alert = alert;
            this.notify = notify;
            this.orderListService = orderListService;
            this.popups = popups;
        }
        return FeedImportResultController;

    }()));
});