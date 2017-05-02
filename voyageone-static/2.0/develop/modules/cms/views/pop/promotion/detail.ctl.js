/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPromotionDetailCtl', function ($scope, promotionService, cartList, items, confirm, notify, $translate, $filter) {

        $scope.tejiabao = false;
        $scope.cartList = cartList;
        $scope.datePicker = [];
        $scope.isEdit = false;
        $scope.editModel = {promotionModel: {"promotionType":0}, tagList: []};

        $scope.initialize = function () {
            if (items) {
                promotionService.getEditModel(items.id).then(function (res) {
                    $scope.currentTimeStamp = res.data.currentTimeStamp;
                    loadData(res.data.editModel);
                });
            } else {
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": ""}];
            }
        };

        function loadData(data) {
            var promotionModel = data.promotionModel;
            if (promotionModel.prePeriodStart) promotionModel.prePeriodStart = new Date(promotionModel.prePeriodStart);
            if (promotionModel.prePeriodEnd) promotionModel.prePeriodEnd = new Date(promotionModel.prePeriodEnd);
            if (promotionModel.activityStart)  promotionModel.activityStart = new Date(promotionModel.activityStart);
            if (promotionModel.activityEnd) promotionModel.activityEnd = new Date(promotionModel.activityEnd);
            if (promotionModel.preSaleStart)  promotionModel.preSaleStart = new Date(promotionModel.preSaleStart);
            if (promotionModel.preSaleEnd) promotionModel.preSaleEnd = new Date(promotionModel.preSaleEnd);
            if (promotionModel.triggerTime) promotionModel.triggerTime = new Date(promotionModel.triggerTime);
            $scope.isEdit = !promotionModel.promotionStatus; //1:open  0:close
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
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
                .then(function () {
                    var index;
                    index = _.indexOf(parent, node);
                    if (index > -1) {
                        parent.splice(index, 1);
                    }
                });
        };

        $scope.ok = function () {

            if (!$scope.promotionForm.$valid || !$scope.editModel.tagList)
                return;

            if ($scope.editModel.tagList.length == 0) {
                notify.danger($translate.instant("TXT_ADD_TAG"));
                return;
            }

            var hasTag = _.every($scope.editModel.tagList, function (element) {
                return element.tagName;
            });

            if (!hasTag)
                return;

            var _editModel = angular.copy($scope.editModel);
            _editModel.promotionModel.prePeriodStart = dateToString(_editModel.promotionModel.prePeriodStart);
            _editModel.promotionModel.prePeriodEnd = dateToString(_editModel.promotionModel.prePeriodEnd);
            _editModel.promotionModel.activityStart = dateToString(_editModel.promotionModel.activityStart);
            _editModel.promotionModel.activityEnd = dateToString(_editModel.promotionModel.activityEnd);
            _editModel.promotionModel.preSaleStart = dateToString(_editModel.promotionModel.preSaleStart);
            _editModel.promotionModel.preSaleEnd = dateToString(_editModel.promotionModel.preSaleEnd);

            if (_editModel.promotionModel.activityStart > _editModel.promotionModel.activityEnd) {
                alert("活动时间检查：请输入结束时间>开始时间。");
                return;
            }

            if (_editModel.promotionModel.preSaleStart > _editModel.promotionModel.preSaleEnd) {
                alert("预售时间检查：请输入结束时间>开始时间。");
                return;
            }

            if (_editModel.promotionModel.prePeriodStart > _editModel.promotionModel.prePeriodEnd) {
                alert("预热时间检查：请输入结束时间>开始时间。");
                return;
            }

            if (_editModel.promotionModel.prePeriodStart > _editModel.promotionModel.activityStart) {
                alert("预热开始时间不能晚于活动开始时间");
                return;
            }
            if (_editModel.promotionModel.preSaleStart > _editModel.promotionModel.activityStart) {
                alert("预售开始时间不能晚于活动开始时间");
                return;
            }
            if(_editModel.promotionModel.promotionType == 0){
                _editModel.promotionModel.triggerType = 0;
            }

            if(_editModel.promotionModel.triggerType == 0){
                _editModel.promotionModel.triggerTime = null;
            }

            if(_editModel.promotionModel.triggerTime){
                _editModel.promotionModel.mqId = null;
            }

            if (_editModel.promotionModel.promotionType != 2)
                $scope.editModel.promotionModel.tejiabaoId = "0";

            promotionService.saveEditModel(_editModel).then(function () {
                $scope.$close();
            });

        };

        function dateToString(date) {
            if (date && date instanceof Date) {
                return $filter("date")(date, "yyyy-MM-dd");
            } else {
                if (date)    return date;
                return "";
            }
        }

        function stringTodate(date) {
            if (date) {
                return new Date(date);
            } else {
                return null;
            }
        }

        $scope.canTimeOut = function (timeStr) {
            if (items) {
                if (!timeStr)
                    return false;

                return new Date(timeStr).getTime() < $scope.currentTimeStamp;
            } else
                return false;


        }
    });
});