/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope,promotionService) {

        $scope.vm = {"promotionList": []};
        $scope.searchInfo = {};
        $scope.groupPageOption = {curr: 1, total: 198, size: 30, fetch: $scope.search};

        $scope.initialize  = function () {

        }

        $scope.search = function () {
            promotionService.getPromotionList($scope.searchInfo).then(function(res){
                $scope.vm.promotionList = res.data;
                $scope.groupPageOption.total = $scope.vm.promotionList.size;
            },function(res){
                alert("e");
            })
        }
    };
});