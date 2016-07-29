
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

            if(context.id){
                $scope.vm.isBegin=context.isBegin;//活动是否开始
                $scope.vm.isEnd= context.isEnd;//活动是否结束
                jmPromotionService.getEditModel(context.id).then(function (res) {
                    $scope.editModel.model = res.data.model;
                    $scope.editModel.tagList = res.data.tagList;
                    $scope.editModel.model.activityStart = formatToDate($scope.editModel.model.activityStart);
                    $scope.editModel.model.activityEnd = formatToDate($scope.editModel.model.activityEnd);
                    $scope.editModel.model.prePeriodStart = formatToDate($scope.editModel.model.prePeriodStart);
                    $scope.editModel.model.prePeriodEnd = formatToDate($scope.editModel.model.prePeriodEnd);
                });
            }else{
                $scope.editModel.model.status=0;
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": "",active:1}];
            }

            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
            });

            $scope.vm.currentTime = new Date();

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
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
                .then(function () {
                  tag.active=0;
                });
        };
        $scope.ok = function() {
            if (!$scope.promotionForm.$valid)
                return;
            if($scope.editModel.model.activityStart > $scope.editModel.model.activityEnd){
                alert("活动时间检查：请输入结束时间>开始时间，最小间隔为30分钟。")
                return;
            }
            var start = new Date($scope.editModel.model.activityStart);
            var end = new Date($scope.editModel.model.activityEnd);
            if(end.getTime()-start.getTime() < 30*60*1000 ){
                alert("活动时间检查：最小间隔为30分钟。");
                return;
            }

            if($scope.editModel.model.prePeriodStart > $scope.editModel.model.prePeriodEnd){
                alert("预热时间检查：请输入结束时间>开始时间。");
                return;
            }
            if($scope.editModel.model.prePeriodStart > $scope.editModel.model.activityStart){
                alert("预热开始时间不能晚于活动开始时间");
                return;
            }
            if($scope.editModel.model.prePeriodEnd > $scope.editModel.model.activityEnd){
                alert("预热结束时间不能晚于活动结束时间");
                return;
            }
            var _upEntity = angular.copy($scope.editModel);
            _upEntity.tagList= _.filter( _upEntity.tagList, function(tag){ return tag.tagName != "";});
            _upEntity.model.activityStart = formatToStr(_upEntity.model.activityStart);
            _upEntity.model.activityEnd = formatToStr(_upEntity.model.activityEnd);
            _upEntity.model.prePeriodStart = formatToStr(_upEntity.model.prePeriodStart);
            _upEntity.model.prePeriodEnd =_upEntity.model.activityEnd; //formatToStr(_upEntity.model.prePeriodEnd);
            _upEntity.model.comment = _upEntity.model.comment || ""; //formatToStr(_upEntity.model.prePeriodEnd);

            jmPromotionService.saveModel(_upEntity).then(function () {
                context =$scope.editModel.model;
                $scope.$close();

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