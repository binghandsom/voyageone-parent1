/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('BrandMappingController', (function () {
        function BrandMappingController(brandMappingService, $translate, popups,$routeParams) {
            this.brandMappingService = brandMappingService;
            this.$translate = $translate;
            this.popups = popups;
            this.$routeParams = $routeParams;
            this.platformPageOption = {curr: 1, total: 0, size: 10, fetch: this.searchBrands.bind(this)};
            this.searchInfo = {
                selectedCart: null,
                selectedStatus: 2,
                pageInfo: this.platformPageOption
            };
            this.cartList = [];
            this.brandMappingList = [];
        }

        BrandMappingController.prototype = {
            init: function () {
                var self = this;
                self.brandMappingService.init().then(function (res) {
                    self.cartList = res.data.cartList;
                    self.searchInfo.selectedCart = self.$routeParams.cartId ? self.$routeParams.cartId : null;
                    self.selectCart();
                });
            },
            clear: function () {
                var self = this;
                //self.searchInfo.selectedCart = null;
                self.searchInfo.selectedStatus = '2';
                self.searchInfo.selectedBrand = '';
                self.searchBrands();
            },
            selectCart: function () {
                var self = this;
                self.brandMappingList = [];
                for (var i = 0; i < self.cartList.length; i++) {
                    if (self.cartList[i].value == self.searchInfo.selectedCart) {
                        self.brandName = self.cartList[i].name;
                        break;
                    }
                }
                self.searchBrands();
            },
            searchBrands: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                var params = {
                    'cartId': self.searchInfo.selectedCart,
                    'mappingState': self.searchInfo.selectedStatus,
                    'brandName': self.searchInfo.selectedBrand,
                    'offset': (self.searchInfo.pageInfo.curr - 1) * self.searchInfo.pageInfo.size,
                    'size': self.searchInfo.pageInfo.size
                };
                self.brandMappingService.searchBrands(params).then(function (res) {
                    self.platformPageOption.total = res.data.brandCount;
                    self.brandMappingList = res.data.brandList;
                });
            },
            popPlatformMappingSetting: function (item) {
                var self = this;
                self.mappingDetail = {
                    'cartId': self.searchInfo.selectedCart,
                    'cartName': self.brandName,
                    'masterName': item.masterName
                };
                self.popups.openPlatformMappingSetting(self.mappingDetail).then(function (res) {
                    if (res.result == true) {
                        self.brandMappingService.addNewBrandMapping({
                            'cmsBrand': self.mappingDetail.masterName,
                            'cartId': self.searchInfo.selectedCart,
                            'brandId': res.brandId
                        }).then(function () {
                            self.searchBrands();
                        });
                    };
                })
            }
        };

        return BrandMappingController;
    })())
});