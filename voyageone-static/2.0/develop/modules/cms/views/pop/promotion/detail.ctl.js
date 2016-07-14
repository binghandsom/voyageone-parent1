/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPromotionDetailCtl', function ($scope, promotionService, cartList, items, confirm, notify, $translate,$filter) {

       // $scope.promotion = {};
        $scope.tejiabao = false;
        $scope.cartList = cartList;
        $scope.datePicker = [];
        $scope.isEdit = false;
        $scope.editModel={promotionModel:{},tagList:[]};
        $scope.initialize = function () {
            if (items) {
                promotionService.getEditModel(items.id).then(function (res) {
                    loadData(res.data);
                });
                //$scope.promotion = angular.copy(items);
            } else {
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": ""}];
            }
        };
        function  loadData(data) {
            var promotionModel = data.promotionModel;
            if (promotionModel.prePeriodStart) promotionModel.prePeriodStart = new Date(promotionModel.prePeriodStart);
            if (promotionModel.prePeriodEnd) promotionModel.prePeriodEnd = new Date(promotionModel.prePeriodEnd);
            if (promotionModel.activityStart)  promotionModel.activityStart = new Date(promotionModel.activityStart);
            if (promotionModel.activityEnd) promotionModel.activityEnd = new Date(promotionModel.activityEnd);
            if (promotionModel.preSaleStart)  promotionModel.preSaleStart = new Date(promotionModel.preSaleStart);
            if (promotionModel.preSaleEnd) promotionModel.preSaleEnd = new Date(promotionModel.preSaleEnd);
            $scope.isEdit = !promotionModel.promotionStatus; //1:open  0:close
            if (promotionModel.tejiabaoId != "0") {
                $scope.tejiabao = true;
            }
            $scope.editModel = data;
        }
        $scope.addTag = function () {
            if ($scope.editModel.tagList) {
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": ""});
            } else {
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": ""}];
            }
        };

        $scope.delTag = function (parent, node) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    var index;
                    index = _.indexOf(parent, node);
                    if (index > -1) {
                        parent.splice(index, 1);
                    }
                });
        };

        $scope.ok = function () {
            if (!$scope.tejiabao)
                $scope.editModel.promotionModel.tejiabaoId = "0";

            if (!$scope.promotionForm.$valid || !$scope.editModel.tagList)
                return;

            if ($scope.editModel.tagList.find(function (tag) {
                    return !tag.tagName;
                })) {
                notify.danger('Please give me a Tag !');
                return;
            }
                $scope.editModel.promotionModel.prePeriodStart = dateToString($scope.editModel.promotionModel.prePeriodStart);
                $scope.editModel.promotionModel.prePeriodEnd = dateToString($scope.editModel.promotionModel.prePeriodEnd);
                $scope.editModel.promotionModel.activityStart = dateToString($scope.editModel.promotionModel.activityStart);
                $scope.editModel.promotionModel.activityEnd = dateToString($scope.editModel.promotionModel.activityEnd);
                $scope.editModel.promotionModel.preSaleStart = dateToString($scope.editModel.promotionModel.preSaleStart);
                $scope.editModel.promotionModel.preSaleEnd = dateToString($scope.editModel.promotionModel.preSaleEnd);
            if($scope.editModel.promotionModel.activityStart > $scope.editModel.promotionModel.activityEnd){
                alert("活动时间检查：请输入结束时间>开始时间。")
                return;
            }

            if($scope.editModel.promotionModel.preSaleStart > $scope.editModel.promotionModel.preSaleEnd){
                alert("预售时间检查：请输入结束时间>开始时间。")
                return;
            }

            if($scope.editModel.promotionModel.prePeriodStart > $scope.editModel.promotionModel.prePeriodEnd){
                alert("预热时间检查：请输入结束时间>开始时间。")
                return;
            }

            if($scope.editModel.promotionModel.prePeriodStart > $scope.editModel.promotionModel.activityStart){
                alert("预热开始时间不能晚于活动开始时间");
                return;
            }
            if($scope.editModel.promotionModel.preSaleStart > $scope.editModel.promotionModel.activityStart){
                alert("预售开始时间不能晚于活动开始时间");
                return;
            }
            promotionService.saveEditModel($scope.editModel).then(function (res) {
                $scope.$close();
            });
            //if (!items) {
            //    promotionService.insertPromotion($scope.promotion).then(function () {
            //        $scope.$close();
            //    });
            //} else {
            //    promotionService.updatePromotion($scope.promotion).then(function () {
            //
            //        $scope.$close();
            //    });
            //}
        }
        function dateToString(date){
            if(date && date instanceof Date){
                return $filter("date")(date,"yyyy-MM-dd");
            }else{
                if(date)    return date;
                return "";
            }
        }
        function stringTodate(date){
            if(date){
               return new Date(date);
            }else{
                return null;
            }
        }
    });
});