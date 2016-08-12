define(function (require) {

    var cms = require('cms'),
        _ = require('underscore');

    cms.controller('RePriceController', (function () {

        function RePriceController(rePriceService) {

            var self = this;

            self.rePriceService = rePriceService;

            self.getChannelList();
        }

        RePriceController.prototype.getChannelList = function () {
            var self = this,
                rePriceService = self.rePriceService;

            rePriceService.getChannelList().then(function (resp) {
                var channelList = resp.data;
                self.channelList = channelList;
                self.selectedChannel = channelList[0];

                self.getPlatformList();
            });
        };

        RePriceController.prototype.getPlatformList = function () {

            var self = this,
                rePriceService = self.rePriceService,
                selectedChannel = self.selectedChannel;

            rePriceService.getPlatformList(selectedChannel).then(function (resp) {
                var platformList = resp.data;
                self.platformList = platformList;
                self.selectedPlatform = platformList[0];

                self.getCartList();
            });
        };

        RePriceController.prototype.getCartList = function () {
            var self = this,
                rePriceService = self.rePriceService,
                selectedPlatform = self.selectedPlatform,
                selectedChannel = self.selectedChannel;

            rePriceService.getCartList({
                platform: selectedPlatform,
                channel: selectedChannel
            }).then(function (resp) {
                var cartList = resp.data;
                self.cartList = cartList;
                self.selectedCart = cartList[0];

                self.getPlatformCategoryList();
            });
        };

        RePriceController.prototype.getPlatformCategoryList = function () {
            var self = this,
                rePriceService = self.rePriceService,
                selectedCart = self.selectedCart;

            rePriceService.getPlatformCategoryList(selectedCart).then(function (resp) {
                var categoryList = resp.data;
                self.categoryList = categoryList;
                self.selectedCategory = categoryList.length ? categoryList[0] : null;
            });
        };

        RePriceController.prototype.setUpdateFlg = function () {
            var self = this,
                rePriceService = self.rePriceService,
                selectedCart = self.selectedCart,
                selectedChannel = self.selectedChannel,
                selectedCategory = self.selectedCategory,
                useCategory = self.useCategory;

            rePriceService.setUpdateFlg({
                cart: selectedCart,
                channel: selectedChannel,
                category: useCategory ? selectedCategory : null
            }).then(function (resp) {
                alert(resp.data);
            });
        };

        return RePriceController;

    }()));
});