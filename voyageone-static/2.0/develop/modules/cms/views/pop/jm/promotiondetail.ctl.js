
/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popJMPromotionDetailCtl', function ($scope,jmPromotionService,alert,context,confirm,$translate,$filter) {
        $scope.vm = {"jmMasterBrandList":[]};
        $scope.editModel = {model:{}};
        $scope.datePicker = [];
        $scope.initialize  = function () {
            console.log("aaP");
            console.log(context);
            if(context.id)
            {
                jmPromotionService.getEditModel(context.id).then(function (res) {
                    $scope.editModel = res.data;
                    $scope.editModel.model.activityStart = formatToDate($scope.editModel.model.activityStart);
                    $scope.editModel.model.activityEnd = formatToDate($scope.editModel.model.activityEnd);
                    $scope.editModel.model.prePeriodStart = formatToDate($scope.editModel.model.prePeriodStart);
                    $scope.editModel.model.prePeriodEnd = formatToDate($scope.editModel.model.prePeriodEnd);
                });
            }
            else
            {
                $scope.editModel.model.status=0;
            }
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
            });
        };
        $scope.addTag = function () {
            if ($scope.editModel.tagList) {
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "",active:1});
            } else {
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": "",active:1}];
            }
        };
        $scope.getTagList=function()
        {
            var tagList = _.filter( $scope.editModel.tagList, function(tag){ return tag.active==1; });
            return tagList||[];
        }
        $scope.delTag = function (tag) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                  tag.active=0;
                });
        };
        $scope.ok = function() {
            if (!$scope.promotionForm.$valid)
                return;
            $scope.editModel.tagList= _.filter( $scope.editModel.tagList, function(tag){ return tag.tagName!=""; });
            $scope.editModel.model.activityStart = formatToStr($scope.editModel.model.activityStart);
            $scope.editModel.model.activityEnd = formatToStr($scope.editModel.model.activityEnd);
            $scope.editModel.model.prePeriodStart = formatToStr($scope.editModel.model.prePeriodStart);
            $scope.editModel.model.prePeriodEnd = formatToStr($scope.editModel.model.prePeriodEnd);
            jmPromotionService.saveModel($scope.editModel).then(function (res) {
                    $scope.$close();
                if(context.search) {
                    context.search();
                }
            })
        };
        /**
         *
         * @param date 字符串格式为yyyy-MM-dd ss:ss:ss
         * @returns {Date}
         */
        function formatToDate(date){
             return new Date(date) ;//$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        };
        function formatToStr(date){
            return $filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        };
    });
});