/**
 * Created by linanbin on 15/12/7.
 */

define(['cms'], function (cms) {
    cms.controller("mqListSetController", (function () {
        function mqListSetController($mqErrorListService) {
            this.$mqErrorListService = $mqErrorListService;
            this.mqErrorPageOption = {curr: 1, total: 0, size: 10, fetch: this.search.bind(this)};
            this.name = "";
            this.title = "";
        }

        /**
         * 数据初始化
         */
        mqListSetController.prototype.init = function () {
            var self = this;
            self.search();
        };

        mqListSetController.prototype.search = function () {
            var self = this,
                data = this.mqErrorPageOption;
            _.extend(data, {"name": this.name});
            _.extend(data, {"title": this.title});
            self.$mqErrorListService.search(data).then(function (res) {
                self.mqErrorList = res.data.mqErrorList;
                self.mqErrorPageOption.total = res.data.mqErrorCnt;
            });
        };
        return mqListSetController;
    })());
});