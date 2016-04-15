define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {


    function indexCtrl($scope, $routeParams, $translate, usjoiService, notify, confirm, alert) {
        $scope.mallOptions = [{id: '-1', name: 'All'}, {id: '1', name: 'Yes'}, {id: '0', name: 'No'}];
        $scope.allowMinimallOption = '-1';// 默认-1
        $scope.service = usjoiService;
        $scope.notify = notify;
        $scope.confirm = confirm;
        $scope.$translate = $translate;
        $scope.alert = alert;



         // init
        $scope.initialize= function () { var self = this; }

        $scope.clear = function () {
            var self = this;
            self.channelId = null;
            self.channelName = null;
            self.allowMinimallOption = '-1';
            setTimeout(function () {
                self.refreshTable();
            }, 300);
        };
        $scope.save = function (model) {
            var self = this;
            self.confirm(self.$translate.instant('BEATING')).result
                .then(function () {
                    self.service.update(model);
                },function(){//cancel
                    model.is_usjoi=0;
                });
        };
        $scope.refreshTable = function () { //检索列表
            var self = this;
            var param = {
                channelId: self.channelId,
                channelName: self.channelName,
                allowMinimallOption: self.allowMinimallOption
            };
            self.service.getList(_.extend(param,self.page)).then(function (result) {
                self.tableSource = result.data.data;  //bind
                self.page.total = result.data.total;
            });
        };
        $scope.updateChannelCartIds = function (data) {
            if (data) {
                this.tableSource.forEach(function (el) {
                    if (el.order_channel_id === data.order_channel_id) {
                        el.cart_ids = data.cart_ids;
                    }
                });
            }

        };
        $scope.openChannelSetting = function (el,popUp) {
            var self = this;
            popUp(el).then(function (data) {
                self.updateChannelCartIds(data);
            });
        };
        $scope.page = {curr: 1, total: 0, size: 20, fetch: function(){$scope.refreshTable();}};

    };
    indexCtrl.$inject=['$scope', '$routeParams', '$translate', 'usjoiService', 'notify', 'confirm', 'alert'];
    return indexCtrl;
});
