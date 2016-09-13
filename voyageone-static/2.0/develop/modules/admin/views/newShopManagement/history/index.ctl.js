/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('HistoryConfigController', (function () {
        function HistoryConfigController(popups, alert, confirm, newShopService) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.newShopService = newShopService;
            this.pageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.historyList = [];
            this.searchInfo = {
                channelId: '',
                channelName: '',
                modifiedFrom: '',
                modifiedTo: '',
                pageInfo: this.pageOption
            }
        }

        HistoryConfigController.prototype = {
            init: function () {
                var self = this;
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.newShopService.searchNewShopByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'channelId': self.searchInfo.channelId,
                        'channelName': self.searchInfo.channelName,
                        'modifiedFrom': self.searchInfo.modifiedFrom,
                        'modifiedTo': self.searchInfo.modifiedTo
                    })
                    .then(function (res) {
                        self.historyList = res.data.result;
                        self.pageOption.total = res.data.count;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.pageOption,
                    channelId: '',
                    channelName: '',
                    modifiedFrom: '',
                    modifiedTo: ''
                }
            },
            edit: function (item) {
                var self = this;
                self.newShopService.getNewShopById(item.id).then(function (res) {
                    var data = JSON.parse(res.data.data);
                    data.id = item.id;
                    window.sessionStorage.setItem('valueBean', JSON.stringify(data));
                    window.location.href = "#/newShop/guide?reload&edit";
                })
            },
            delete: function (item) {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        self.newShopService.deleteNewShop(item.id).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return HistoryConfigController;
    })())
});