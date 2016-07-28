/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('HomeController', (function () {
        function HomeController() {
            this.todayData = {
                labels: ["New Order", "Sku"],
                data: [[12, 200]],
                series: ['Today‘s'],
                colors : ['#339be6']
            };
            this.errorData = {
                labels: ["Shippent", "aaaa", "bbbb", "cccc"],
                data: [[104, 50, 10, 50]],
                series: ['Error'],
                colors : ['#339be6']
            };
        }

        return HomeController;
    }()));
});