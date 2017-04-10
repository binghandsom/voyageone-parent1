/**
 * Created by linanbin on 15/12/7.
 */

define(['cms'], function (cms) {

    cms.controller("mqListSetController", (function () {
        function mqListSetController($mqErrorListService, $routeParams, popups) {
            this.$mqErrorListService = $mqErrorListService;
            this.$routeParams = $routeParams;
            this.mqErrorPageOption = {curr: 1, total: 0, size: 10, fetch: this.search.bind(this)};
            this.name = "";
            this.title = "";
            this.typeStatus = {};
            this.popups = popups;
        }

        /**
         * 数据初始化
         */
        mqListSetController.prototype.init = function () {
            var self = this;
            self.search();
        };

        mqListSetController.prototype.clear = function () {
            this.name = '';
            this.title = '';
            this.typeStatus = {};
        };

        mqListSetController.prototype.search = function () {
            var self = this,
                _typeValue = [],
                data = self.mqErrorPageOption;

            _.forEach(self.typeStatus, function (value, key) {
                if (value) {
                    _typeValue.push(key);
                }
            });

            _.extend(data, {
                "name": this.name,
                "title": this.title,
                "type": this.type,
                "userName": self.$routeParams.userName,
                "typeValue": _typeValue
            });

            self.$mqErrorListService.search(data).then(function (res) {
                self.mqErrorList = res.data.mqErrorList;
                self.mqErrorPageOption.total = res.data.mqErrorCnt;
                self.type = res.data.type;
            });

        };

        mqListSetController.prototype.getStatusName = function (id) {
            var self = this,
                stateList = self.type;

            if (!id)
                return;

            return _.find(stateList, function (ele) {
                return ele.id === id;
            });
        };

        /**
         * 弹出sku级code错误列表
         */
        mqListSetController.prototype.popMqSkuCodeError = function (errorEntiy) {
            var self = this;

            self.popups.openMqSkuCodeError(errorEntiy);
        };

        return mqListSetController;

    })());
});