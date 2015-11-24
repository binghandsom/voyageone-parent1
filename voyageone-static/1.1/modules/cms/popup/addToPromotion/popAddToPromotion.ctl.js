/**
 * Created by edward-pc1 on 2015/8/24.
 * Modified by sky on 2015/08/24
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require ('modules/cms/popup/addToPromotion/popAddToPromotion.service');
    cmsApp.controller('popAddToPromotionController', ['$scope', 'popAddToPromotionService', 'userService', '$modalInstance', 'productList', 'notify',
        function ($scope, popAddToPromotionService, userService, $modalInstance, productList, notify) {

            var commonUtil = require('components/util/commonUtil');
            var channelId = userService.getSelChannel();
            var vm = $scope.vm = {};
            vm.promotionIdArray = {selectOneFlagList: [], selectIdList: []};
            vm.productIdArray = productList;

            //初始化
            $scope.initialize = init;
            //根据所给月份获取月份下所有的promotion
            $scope.getPromInfoByMonth = getPromInfoByMonth;
            //根据选中的产品和选中的promotion建立关系
            $scope.productAddToPromotion = productAddToPromotion;
            //关闭对话框
            $scope.close = closeDialog;

            function init () {
                popAddToPromotionService.getActivePromotionMonth(channelId).then(function (res) {
                    vm.promotionMonthsUS = res.data.promMothListUS;
                    vm.promotionMonthsCH = res.data.promMothListCH;
                    _.each(vm.promotionMonthsUS, function(item){
                        item = new Date(item);
                    });
                    //初始化为显示最小的月份的promotion类容
                    getPromInfo();
                });
            }

            function getPromInfo() {
                var paramMap = {channelId: channelId, cartLocation: "US"};
                popAddToPromotionService.getPromInfo(paramMap).then(function (res) {
                    vm.promInfoListUSTotall = res.data.promInfoListUS;
                    getPromInfoByMonth("US", vm.promotionMonthsUS[0]);
                    var paramMap = {channelId: channelId, cartLocation: "CH"};
                    popAddToPromotionService.getPromInfo(paramMap).then(function (res) {
                        vm.promInfoListCHTotall = res.data.promInfoListCH;
                        getPromInfoByMonth("CH", vm.promotionMonthsCH[0]);
                    })
                })
            }

            function getPromInfoByMonth(cartLocation, monthInfo) {
                if(cartLocation == "US"){
                    vm.promInfoListUS =  _.filter(vm.promInfoListUSTotall, function(v){
                        return v.promotionMonth == monthInfo;
                    })
                }else{
                    vm.promInfoListCH = _.filter(vm.promInfoListCHTotall, function(v){
                        return v.promotionMonth == monthInfo;
                    })
                }
            }

            function productAddToPromotion(){
                var paramMap = {promotionIdArray: vm.promotionIdArray.selectIdList, productIdArray: vm.productIdArray};
                popAddToPromotionService.createPromProductRelation(paramMap).then(function (res){
                    if(res.data.successFlg) {
                        notify.success("CMS_MSG_ADD_TO_PROMOTION_SUCCESS");
                    }
                });
                //清空选中数组
                vm.promotionIdArray = [];
                closeDialog();
            }

            function closeDialog() {
                $modalInstance.dismiss('close');
            }

        }]);

});