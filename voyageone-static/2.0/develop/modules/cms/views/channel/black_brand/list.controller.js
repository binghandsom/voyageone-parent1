/**
 * @descrition 品牌黑名单一览
 * @author piao wenjie
 * @version 2.6.0
 */

define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, cartEnums) {

    var $brandType = {
        "0": "Feed品牌",
        "1": "Master品牌",
        "2": "平台品牌"
    };

    function cartToArray(cart) {
        return _.map(cart, function (value, key) {
            if (value)
                return +key;
        }).filter(function (item) {
            return item
        });
    }

    cms.controller('BlackBrandListController', (function () {

        function BlackBrandListController(menuService, blackBrandService, confirm, $routeParams, $translate, notify, alert,$localStorage) {
            var self = this;

            menuService.getPlatformType().then(function (resp) {
                var cartList = _.filter(resp, function (cart) {
                    return cart.value != 0 && cart.value != 1 && cart.value < 900;
                });
                self.cartList = cartList;
            });
            self.channelList;
            self.channel = $localStorage.user.channel;
            self.blackBrandService = blackBrandService;
            self.$routeParams = $routeParams;
            self.confirm = confirm;
            self.$translate = $translate;
            self.notify = notify;
            self.alert = alert;
            self.cartEnums = cartEnums;
            self.$brandType = $brandType;
            self.searchInfo = {
                brandType: '0',
                cart: {},
                status: null,
                brand: ''
            };
            self.paging = {
                curr: 1, total: 0, size: 10, fetch: function () {
                    self.search();
                }
            };
        }

        /**
         * 初始化
         */
        BlackBrandListController.prototype.init = function () {
            var self = this,
                searchInfo = self.searchInfo,
                params = self.$routeParams.params;
                self.blackBrandService.init(self.channel).then(function (res) {
                    self.channelList = res.data.channelList;
                    if (params) {
                        var paraArr = params.split("|");
                        searchInfo.brandType = paraArr[0] ? paraArr[0] : null;
                        searchInfo.status = paraArr[1] ? paraArr[1] : null;
                        if (paraArr[2]) {
                            searchInfo.cart[paraArr[2]] = true;
                        }
                    }
                    self.search();
                });
        };

        /**
         * 清空
         */
        BlackBrandListController.prototype.clear = function () {
            this.searchInfo = {
                brandType: '0',
                cart: {},
                status: null,
                brand: ''
            };
        };


        /**查询*/
        BlackBrandListController.prototype.search = function () {
            var self = this,
                cartIdList,
                upEntity,
                searchInfo = self.searchInfo,
                paging = self.paging,
                blackBrandService = self.blackBrandService;

            cartIdList = cartToArray(searchInfo.cart);

            upEntity = _.extend({
                pageNumber: self.paging.curr,
                pageSize: self.paging.size,
                cartIdList: searchInfo.brandType == 2 ? cartIdList : null,
                brandType: +searchInfo.brandType,
                status: searchInfo.status == null ? null : !!(+searchInfo.status),
                brand: searchInfo.brand,
                channelId:self.selChannelId
            });

            blackBrandService.list(upEntity).then(function (res) {
                paging.total = res.data.count;
                self.dataList = res.data.data;
            });

        };

        BlackBrandListController.prototype.seletAll = function () {
            var self = this;

            _.each(self.dataList, function (item) {
                item.selected = self.all;
            });
        };

        /**
         * 添加/移除 黑名单
         * @param mark ：添加/移除 标识
         * @param content： button内容
         * @param element
         */
        BlackBrandListController.prototype.update = function (mark, content, element) {

            var self = this,
                $translate = self.$translate,
                confirm = self.confirm,
                notify = self.notify,
                brandList = [],
                blackBrandService = self.blackBrandService;

            brandList.push(element);

            $translate("TXT_BLACK_LIST_CONFIRM", {content: $translate.instant(content)}).then(function (msg) {
                confirm(msg).then(function () {
                    blackBrandService.update({
                        status: mark,
                        brandList: brandList,
                        channelId:self.selChannelId
                    }).then(function () {
                        notify.success('TXT_MSG_UPDATE_SUCCESS');
                        self.search();
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

            var self = this,
                $translate = self.$translate,
                confirm = self.confirm,
                notify = self.notify,
                alert = self.alert,
                brandList,
                blackBrandService = self.blackBrandService;

            brandList = _.map(self.dataList, function (element) {
                if (element.selected) {
                    return element;
                }
            }).filter(function (item) {
                return item;
            });

            if (brandList.length == 0) {
                alert("TXT_MSG_NO_ROWS_SELECT");
                return;
            }

            $translate("TXT_BLACK_LIST_BATCH_CONFIRM", {content: $translate.instant(content)}).then(function (msg) {
                confirm(msg).then(function () {
                    blackBrandService.update({
                        status: mark,
                        brandList: brandList
                    }).then(function () {
                        notify.success('TXT_MSG_UPDATE_SUCCESS');
                        self.all = null;
                        self.search();
                    }, function () {
                        notify.danger('TXT_MSG_UPDATE_FAIL');
                    });
                });
            });
        };

        BlackBrandListController.prototype.selectCarts = function () {
            var self = this,
                brandType = self.searchInfo.brandType;

            if(brandType == '2'){
                _.each(self.cartList, function (item) {
                    self.searchInfo.cart[item.value] = true;
                });
            }

        };

        BlackBrandListController.prototype.checkCarts = function () {
            return cartToArray(this.searchInfo.cart).length > 0;
        };

        return BlackBrandListController;

    })());
});