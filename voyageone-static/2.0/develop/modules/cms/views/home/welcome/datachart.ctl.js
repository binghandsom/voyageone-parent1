
define([
    'angularAMD',
    'underscore'
], function (angularAMD) {
    function dController($scope, $menuService,notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory) {
        $scope.vm={sumData:{}};
        $scope.init = function () {
            $menuService.getHomeSumData().then(function (res) {
                console.log(res.data);
                $scope.vm.sumData = res.data;
            });
        }
    }
    dController.$inject = ['$scope', '$menuService', 'notify', '$routeParams', '$location', 'alert', '$translate', 'confirm', 'cRoutes', 'selectRowsFactory'];
    return dController;
});
