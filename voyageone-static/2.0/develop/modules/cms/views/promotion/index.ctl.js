/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function indexController($scope,promotionService,confirm,menuService) {

        $scope.vm = {"promotionList": [],"cartList":[]};
        $scope.searchInfo = {};
        $scope.groupPageOption = {curr: 1, total: 198, size: 30, fetch: $scope.search};

        $scope.initialize  = function () {
            $scope.getCategoryType();
            $scope.search();
        }

        $scope.search = function () {
            promotionService.getPromotionList($scope.searchInfo).then(function(res){
                $scope.vm.promotionList = res.data;
                $scope.groupPageOption.total = $scope.vm.promotionList.size;
            },function(res){
            })
        }
        $scope.del = function (data) {
            confirm("是否要删除  "+data.promotionName,'删除').result.then(function(){
                var index = _.indexOf($scope.vm.promotionList,data);
                data.isActive = false
                promotionService.updatePromotion(data).then(function(res){
                    $scope.vm.promotionList.splice(index,1);
                    $scope.groupPageOption.total = $scope.vm.promotionList.size;
                },function(res){
                })
            },function(){

            })

        }


        $scope.getCategoryType = function() {
            menuService.getCategoryType().then(function(res){
                $scope.vm.cartList = res;
            })
        }
    };

    indexController.$inject = ['$scope','promotionService', 'confirm', 'menuService'];
    return indexController;
});