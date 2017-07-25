/**
 * @description 首页

 */
define([
    'cms'
], function (cms) {

    cms.controller('usaDatachartController', class UsaDatachartController {

        constructor($menuService,alert,commonService,$location,$rootScope,$sessionStorage,$scope) {
            this.vm = {};
            this.$menuService = $menuService;
            this.alert = alert;
            this.commonService = commonService;
            this.$location = $location;
            this.$rootScope = $rootScope;
            this.$sessionStorage = $sessionStorage;
            this.$scope = $scope;

            this.usPlatforms = {};

            // 页面数据
            this.feedInfo = [];
            this.platformInfo = {};
            this.platformHeader = {
                "Pending Items":"Pending Items",
                "Listing Items":"OnSale Items",
                "Delisting Items":"InStock Items",
                // "Delisting Items(with Inventory)":"InStock Items(with Inventory)",
                "All Items":"All Items"
            };

            // 数据初始化
            this.init();
        }

        init() {
            let self = this;

            self.$menuService.getHomeSumData().then(function (res) {
                self.vm.sumData = res.data;
            });

            self.commonService.getChannelCarts().then(res => {
                if (res.data) {
                    let usPlatforms = _.filter(res.data, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartObj.lang_id === "en" && cartId > 0 && cartId < 20;
                    });
                    _.each(usPlatforms, cartObj => {
                        let cartId = cartObj.value;
                        let cartName = cartObj.name;
                        self.usPlatforms[cartId] = cartName;
                    });
                }
            });

            // Feed统计数
            self.commonService.getFeedInfo().then((res) => {
                if (res.data) {
                    self.feedInfo = res.data.feedInfo == null ? [] : res.data.feedInfo;
                    self.platformInfo = res.data.platformInfo == null ? {} : res.data.platformInfo;
                }
            });

        }

        // 跳转到Feed检索页
        goLink(feedItem) {
            let self = this;
            let linkParameter = self.$scope.$eval(feedItem.linkParameter);
            _.extend(self.$sessionStorage.auth, {selfAuth:linkParameter.selfAuth});

            let timeStamp = new Date().getTime();

            location.href = `#${feedItem.linkUrl}?time=${timeStamp}`;

        }


        // 跳转到Product检索页
        linkToSearch (platformItem) {
            let self = this;
            self.$location.path(platformItem.linkUrl + "/" + platformItem.linkParameter);
        }
    });

});
