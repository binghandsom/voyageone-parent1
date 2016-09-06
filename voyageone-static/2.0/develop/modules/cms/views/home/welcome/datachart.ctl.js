define([
    'underscore'
], function () {
    function dController($scope, $menuService, $location, cRoutes, $sessionStorage , alert) {
        $scope.vm = {sumData: {}};
        $scope.init = function () {

            alert("首页<span class='text-u-red'>【每小时刷新一次】</span>，如与其它反馈有些许差异，请稍后重新确认。");

            $menuService.getHomeSumData().then(function (res) {
                $scope.vm.sumData = res.data;
            });
        };
        $scope.jump = jump;
        function jump(url, param) {
            $sessionStorage.feedSearch = param;
            $location.path(url);
        }

        /**
         * search by input value.
         */
        $scope.goSearchPage = function (bizType, value) {
            $sessionStorage.feedSearch = value;
            $location.path(cRoutes.search_advance_param.url + "4/" + bizType + '/0/0');
        }

    }

    dController.$inject = ['$scope', '$menuService', '$location', 'cRoutes', '$sessionStorage','alert'];

    return dController;
});
