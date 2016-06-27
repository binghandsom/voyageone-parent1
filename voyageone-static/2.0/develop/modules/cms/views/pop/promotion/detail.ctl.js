/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPromotionDetailCtl', function ($scope, promotionService, cartList, items, confirm, notify, $translate,$filter) {

        $scope.promotion = {};
        $scope.tejiabao = false;
        $scope.cartList = cartList;
        $scope.datePicker = [];
        $scope.isEdit = false;

        $scope.initialize = function () {
            if (items) {
                $scope.promotion = angular.copy(items);
                $scope.isEdit = $scope.promotion.promotionStatus;
                if ($scope.promotion.tejiabaoId != "0") {
                    $scope.tejiabao = true;
                }
            } else {
                $scope.promotion.tagList = [{"id": "", "channelId": "", "tagName": ""}];
            }
        };

        $scope.addTag = function () {
            if ($scope.promotion.tagList) {
                $scope.promotion.tagList.push({"id": "", "channelId": "", "tagName": ""});
            } else {
                $scope.promotion.tagList = [{"id": "", "channelId": "", "tagName": ""}];
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
                $scope.promotion.tejiabaoId = "0";

            if (!$scope.promotionForm.$valid || !$scope.promotion.tagList)
                return;

            if ($scope.promotion.tagList.find(function (tag) {
                    return !tag.tagName;
                })) {
                notify.danger('Please give me a Tag !');
                return;
            }

                $scope.promotion.prePeriodStart = dateToString($scope.promotion.prePeriodStart);
                $scope.promotion.prePeriodEnd = dateToString($scope.promotion.prePeriodEnd);
                $scope.promotion.activityStart = dateToString($scope.promotion.activityStart);
                $scope.promotion.activityEnd = dateToString($scope.promotion.activityEnd);
                $scope.promotion.preSaleStart = dateToString($scope.promotion.preSaleStart);
                $scope.promotion.preSaleEnd = dateToString($scope.promotion.preSaleEnd);

            if (!items) {
                promotionService.insertPromotion($scope.promotion).then(function () {
                    $scope.$close();
                });
            } else {
                promotionService.updatePromotion($scope.promotion).then(function () {

                    $scope.$close();
                });
            }
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