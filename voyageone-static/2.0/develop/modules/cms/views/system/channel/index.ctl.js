define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {


    function indexCtrl($scope, $translate, usjoiService, notify, confirm, alert) {
        $scope.mallOptions = [{id: '-1', name: 'All'}, {id: '1', name: 'Yes'}, {id: '0', name: 'No'}];
        $scope.allowMinimallOption = '-1';// 默认-1
        $scope.service = usjoiService;
        $scope.notify = notify;
        $scope.confirm = confirm;
        $scope.$translate = $translate;
        $scope.alert = alert;

        $scope.active = "1";


        // init
        $scope.initialize = function () {
            var self = this;
            self.refreshTable();
        }

        $scope.clear = function () {
            var self = this;
            self.channelId = null;
            self.channelName = null;
            self.allowMinimallOption = '-1';
            self.active = "1";
            self.refreshTable();
        };
        $scope.save = function (model) {
            var self = this;
            self.confirm(self.$translate.instant('BEATING')).result
                .then(function () {
                    self.service.update(model);
                }, function () {//cancel
                    model.is_usjoi = 0;
                });
        };
        $scope.refreshTable = function () { //检索列表
            var self = this;
            var param = {
                active: self.active,
                channelId: self.channelId,
                channelName: self.channelName,
                allowMinimallOption: self.allowMinimallOption
            };
            self.service.getList(_.extend(param, self.page)).then(function (result) {
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
        $scope.delete = function (index, el) {
            var self = this;
            self.confirm(self.$translate.instant('TXT_MSG_DO_DELETE') + el.full_name).result.then(function () {
                el.active = 0; //删除 FIXME 是否需要改成对应的active
                self.service.update(el).then(function () {
                    //self.tableSource.splice(index, 1);
                    $scope.page.curr = 1;
                    $scope.refreshTable();
                });
            })
        };

        $scope.openChannelEdit = function (index, el, popUp) {
            var self = this;
            popUp(el||{}).then(function (data) {
                if(!data) return; //什么都没改动

                if (index == -1) {//add
                    self.tableSource.splice(0, 0, data);
                }else{
                    self.tableSource.splice(index, 1, data);
                }
            });
        };


        $scope.openChannelSetting = function (el, popUp) {
            var self = this;
            popUp(el).then(function (data) {
                self.updateChannelCartIds(data);
            });
        };
        $scope.page = {
            curr: 1, total: 0, fetch: function () {
                $scope.refreshTable();
            }
        };

    };
    indexCtrl.$inject = ['$scope', '$translate', 'usjoiService', 'notify', 'confirm', 'alert'];
    return indexCtrl;
});
