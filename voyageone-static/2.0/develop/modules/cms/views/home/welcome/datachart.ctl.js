/**
 * @description 首页
 *              //http://localhost:3000/cms/home/menu/sumHome?channelId=010汇总页面
 */
define([
    'cms'
], function (cms) {

    cms.controller('datachartController', function ($scope, $menuService, $location, cRoutes, $sessionStorage,$localStorage) {
        $scope.vm = {sumData: {}};
        $scope.channelId = $localStorage.user.channel;
        $scope.init = function () {
            $menuService.getHomeSumData().then(function (res) {
                $scope.vm.sumData = res.data;
            });
        };

        $scope.jump = function (url, param) {
            $sessionStorage.feedSearch = param;
            $location.path(url);
        };

        /**
         * 参数格式：type|status|cart
         *          type: 0/1/2 = feed/master/platform
         *          status: 0/1 = 未加入/已加入
         */
        $scope.jumpBlackBrand = function (params) {
            var url = "/marketing/black-brand/" + params;
            $location.path(url);
        };

        $scope.jumpBlackBrandNew = function (type, status, cart) {
            var url = "/marketing/black-brand/" + type + "|" + status + "|" + cart;
            $location.path(url);
        };

        /**
         * search by input value.
         */
        $scope.goSearchPage = function (bizType, value) {
            $sessionStorage.feedSearch = value;
            $location.path(cRoutes.search_advance_param.url + "4/" + bizType + '/0/0');
        }
    });

});
