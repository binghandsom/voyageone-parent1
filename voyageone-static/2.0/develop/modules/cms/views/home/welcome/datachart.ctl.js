/**
 * Created by sofia on 5/25/2016.
 */
define([
        "cms"
    ],function (cms) {
        cms.controller('dataChartController',(function () {
            function dataChartController($feedSearchService){
                this.$feedSearchService = $feedSearchService;
                this.searchInfo= {};
                this.searchInfo.pageNum = "1";
                this.searchInfo.pageSize = "10";
            }
            dataChartController.prototype = {
                init: function(){
                    var self = this;
                    self.$feedSearchService.search(self.searchInfo).then(function (res) {
                        self.feedPageOption.total = res.data.feedListTotal;
                        console.log(res)
                    })
                }
            };
            return dataChartController;
        })())
    });
