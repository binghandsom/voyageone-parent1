/**
 * Created by sofia on 5/6/2016.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function () {
    function cmsCatController($scope) {
        $scope.firstCats = [
            {id: 001, name: "箱包"},
            {id: 002, name: "美妆"},
            {id: 003, name: "电脑"},
            {id: 004, name: "服饰"}
        ];
        $scope.secondCats = [
            {id: 001, name: "护肤品"},
            {id: 002, name: "男士护肤"},
            {id: 003, name: "彩妆"},
            {id: 004, name: "染发霜"}
        ];
    };
    cmsCatController.$inject = ['$scope'];
    return cmsCatController;
});