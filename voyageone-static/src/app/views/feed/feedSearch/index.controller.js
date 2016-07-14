/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('FeedInfoSearchController', (function () {

        function FeedInfoSearchController(feedInfoSearchService) {
            this.feedInfoSearchService = feedInfoSearchService;
            this.feedInfoList = [];
            this.parentSku = "";
            this.name = "";
            this.category = "";
            this.priceStart = "";
            this.priceEnd = "";
            this.pageOption = {
                curr: 1,
                total: 0,
                fetch: this.getFeedInfoList.bind(this)
            };
        }

        FeedInfoSearchController.prototype = {
            init: function () {
                this.search();
            },
            getFeedInfoList: function () {
                var main = this;
                main.feedInfoSearchService.search({
                    "code": main.parentSku,
                    "name": main.name,
                    "category": main.category,
                    "priceStart": priceStart,
                    "priceEnd": priceEnd,
                    "curr": main.pageOption.curr,
                    "size": main.pageOption.size
                }).then(function (res) {
                    main.feedInfoList = res.feedInfoList;
                    main.pageOption.total = res.total;
                })
            },

            search: function () {
                this.pageOption.curr = 1;
                this.getFeedInfoList();
            }
        }


        return FeedInfoSearchController;

    }()));
});