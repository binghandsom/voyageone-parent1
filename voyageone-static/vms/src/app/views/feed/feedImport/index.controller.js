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
            this.clientFileName = "";
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
                if (this.uploadDateStart === undefined || this.uploadDateEnd === undefined) {
                    this.alert('TXT_PLEASE_INPUT_A_VALID_DATE');
                    return;
                }
                var uploadDateStart1 = "";
                var uploadDateEnd1 = "";
                if (this.uploadDateStart != "" && this.uploadDateStart != null) {
                    uploadDateStart1 = new Date(this.uploadDateStart).getTime();
                }
                if (this.uploadDateEnd != "" && this.uploadDateEnd != null) {
                    uploadDateEnd1 =  new Date(this.uploadDateEnd);
                    uploadDateEnd1.setDate(uploadDateEnd1.getDate() + 1);
                    uploadDateEnd1 = uploadDateEnd1.getTime();
                }
                main.feedImportResultService.search({
                    "status": main.status,
                    "clientFileName": main.clientFileName,
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
            },

            download: function (errorFileName) {
                var main = this;
                $.download.post('/vms/feed/feed_import_result/downloadFeedErrorFile', {"errorFileName": errorFileName}, this.afterDownload, main);
            },

            afterDownload:function (responseContent, param, context) {
                var res = JSON.parse(responseContent);
                if (res.message != '') {
                    context.alert(res.message);
                }
            }
        }
        return FeedImportResultController;

    }()));
});