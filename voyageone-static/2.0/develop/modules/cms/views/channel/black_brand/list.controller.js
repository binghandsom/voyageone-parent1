define(['cms'], function (cms) {
        cms.controller('BlackBrandListController', (function () {

            function BlackBrandListController(menuService, blackBrandService, confirm, $translate, notify) {
                var self = this;

                menuService.getPlatformType().then(function (resp) {
                    var cartList = _.filter(resp, function (cart) {
                        return cart.value != 0 && cart.value != 1 && cart.value < 900;
                    });
                    self.cartList = cartList;
                });

                self.blackBrandService = blackBrandService;
                self.confirm = confirm;
                self.$translate = $translate;
                self.notify = notify;
                self.searchInfo = {
                    brandType: null,
                    cart: {},
                    status: null,
                    brand: ''
                };
                self.paging = {
                    curr: 1, total: 0, fetch: function () {
                        self.search();
                    }
                };
            }

            /**
             * 初始化
             */
            BlackBrandListController.prototype.init = function () {
                var self = this;

                self.search();
            };

            /**查询*/
            BlackBrandListController.prototype.search = function () {
                var self = this,
                    cartIdList,
                    upEntity,
                    searchInfo = self.searchInfo,
                    paging = self.paging,
                    blackBrandService = self.blackBrandService;

                cartIdList = _.map(searchInfo.cart, function (value, key) {
                    if (value)
                        return +key;
                });

                upEntity = _.extend(self.paging, {
                    cartIdList: searchInfo.brandType == 2 ? cartIdList : null,
                    brandType: +searchInfo.brandType,
                    status: searchInfo.status === "true" ? true : false,
                    brand: searchInfo.brand
                });

                blackBrandService.list(upEntity).then(function (res) {
                    paging.total = res.data.total;
                    self.dataList = res.data.list;
                });

            };

            /**
             * 添加/移除 黑名单
             * @param mark ：添加/移除 标识
             * @param content： button内容
             */
            BlackBrandListController.prototype.update = function (mark, content) {
                if (!mark)
                    return;

                var self = this,
                    $translate = self.$translate,
                    confirm = self.confirm,
                    notify = self.notify,
                    blackBrandService = self.blackBrandService;

                $translate("TXT_BLACK_LIST_CONFIRM", {content: $translate.instant(content)}).then(function (msg) {
                    confirm(msg).then(function () {
                        blackBrandService.update().then(function () {
                            notify.success('TXT_MSG_UPDATE_SUCCESS');
                        }, function () {
                            notify.danger('TXT_MSG_UPDATE_FAIL');
                        });
                    });
                });
            };

            /**
             * 批量添加/移除 黑名单
             * @param mark: 添加/移除 标识
             * @param content:button内容
             */
            BlackBrandListController.prototype.batchUpdate = function (mark, content) {
                if (!mark)
                    return;

                var self = this,
                    $translate = self.$translate,
                    confirm = self.confirm,
                    notify = self.notify,
                    blackBrandService = self.blackBrandService;

                $translate("TXT_BLACK_LIST_BATCH_CONFIRM", {content: $translate.instant(content)}).then(function (msg) {
                    confirm(msg).then(function () {
                        blackBrandService.batchUpdate().then(function () {
                            notify.success('TXT_MSG_UPDATE_SUCCESS');
                        }, function () {
                            notify.danger('TXT_MSG_UPDATE_FAIL');
                        });
                    });
                });
            };

            return BlackBrandListController;

        })());
    });