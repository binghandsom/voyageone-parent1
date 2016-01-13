/**
 * Created by linanbin on 15/11/27.
 */

define([], function () {

    function indexController($scope) {
        var vm = $scope;

        vm.title = "this is a test";
    };

    indexController.$inject = ['$scope'];
    return indexController;
});