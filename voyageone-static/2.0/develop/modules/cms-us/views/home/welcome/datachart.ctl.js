/**
 * @description 首页

 */
define([
    'cms'
], function (cms) {

    cms.controller('datachartController', class DatachartController {

        constructor($menuService,alert,commonService,$location,$rootScope,$sessionStorage,$scope) {
            this.vm = {};
            this.$menuService = $menuService;
            this.alert = alert;
            this.commonService = commonService;
            this.$location = $location;
            this.$rootScope = $rootScope;
            this.$sessionStorage = $sessionStorage;
            this.$scope = $scope;

            // 页面数据
            this.feedInfo = [];

            // 数据初始化
            this.init();
        }

        init() {
            let self = this;

            self.$menuService.getHomeSumData().then(function (res) {
                self.vm.sumData = res.data;
            });

            // Feed统计数
            self.commonService.getFeedInfo().then((res) => {
                if (res.data) {
                    self.feedInfo = res.data.feedInfo == null ? [] : res.data.feedInfo;
                }
            });
        }

        goLink(feedItem) {
            let self = this;
            let linkParameter = self.$scope.$eval(feedItem.linkParameter);
            _.extend(self.$sessionStorage.auth, {selfAuth:linkParameter.selfAuth});

            let timeStamp = new Date().getTime();

            location.href = `#${feedItem.linkUrl}?time=${timeStamp}`;

        }

    });

});
