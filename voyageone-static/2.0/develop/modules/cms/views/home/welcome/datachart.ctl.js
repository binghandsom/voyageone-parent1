
define([
    'angularAMD',
    'underscore'
], function (angularAMD) {
    function dController($scope, $menuService,notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory,$sessionStorage) {
        $scope.vm={sumData:{}};
        $scope.init = function () {
            $menuService.getHomeSumData().then(function (res) {

                $scope.vm.sumData = res.data;
                console.log( $scope.vm);
            });
        };
        $scope.jump=jump;
        function jump(url,param){
            $sessionStorage.feedSearch = param;
            $location.path(url);
        }
    }
    dController.$inject = ['$scope', '$menuService', 'notify', '$routeParams', '$location', 'alert', '$translate', 'confirm', 'cRoutes', 'selectRowsFactory','$sessionStorage'];
    return dController;
});
