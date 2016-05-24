/**
 * Created by sofia on 5/19/2016.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('addChannelCategoryController', function ($scope,$addChannelCategoryService) {
        $scope.vm = {
            categoryTypeSelectValue: 1,
            cartId: '',
            cartList: [],
            cartTree: []
        };
        $scope.initialize = function () {
            getCategoryList();
        };
        function getCategoryList() {
            $scope.cartList = [
                {name:'TM',value:'0'},
                {name:'TG',value:'1'},
                {name:'JD',value:'2'},
                {name:'JG',value:'3'}
            ];
            $scope.cartTree = {
                Tree1:[足球鞋,篮球鞋],
                Tree2:[新品,篮球鞋]
            }
            $scope.vm.cartList = $scope.cartList;
            $scope.vm.cartTree = $scope.cartTree;
        };
        
    });
});