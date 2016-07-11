/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('FeedImportResultController', (function () {

        function FeedImportResultController(feedImportResultService, alert) {
            this.feedImportResultService = feedImportResultService;
            this.alert = alert;
            this.feedImportResultList = [];
            this.statusList = [];
            this.status="";
            this.fileName = "";
            this.uploadDateStart = "";
            this.uploadDateEnd = "";
            this.pageOption = {
                curr: 1,
                total: 0,
                fetch: this.getFeedImportResultList.bind(this)
            };
        }
        FeedImportResultController.prototype = {
            init: function () {
                var main = this;
                main.feedImportResultService.init().then(function (res) {
                    main.statusList = res.statusList;
                    main.search();
                })
            },
            getFeedImportResultList: function () {
                var main = this;
                var uploadDateStart1 = new Date().getTime();
                var uploadDateEnd1 = new Date().getTime();
                main.feedImportResultService.search({
                    "status": main.status,
                    "fileName": main.fileName,
                    "uploadDateStart": uploadDateStart1,
                    "uploadDateEnd": uploadDateEnd1,
                    "curr": main.pageOption.curr,
                    "size": main.pageOption.size
                }).then(function (res) {
                    main.feedImportResultList = res.feedImportResultList;
                    main.pageOption.total = res.total;
                })
            },
            search: function () {
                this.pageOption.curr = 1;
                this.getFeedImportResultList();
            }
        }
        return FeedImportResultController;

    }()));
});