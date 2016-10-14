define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {

    function SpEditDirectiveController($scope, $routeParams, jmPromotionService, alert, confirm, $translate, $filter) {

        $scope.vm = {"jmMasterBrandList":[]};
        $scope.editModel = {model:{}};
        $scope.datePicker = [];

        $scope.initialize = function () {
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
            });
            jmPromotionService.GetEditModelExt($routeParams.jmpromId).then(function (res) {
                $scope.vm.isBeginPre = res.data.isBeginPre;       //预热是否开始
                $scope.vm.isEnd = res.data.isEnd;                //活动是否结束
                $scope.editModel.model = res.data.model;
                $scope.editModel.tagList = res.data.tagList;
                $scope.editModel.model.activityStart = formatToDate($scope.editModel.model.activityStart);
                $scope.editModel.model.activityEnd = formatToDate($scope.editModel.model.activityEnd);
                $scope.editModel.model.prePeriodStart = formatToDate($scope.editModel.model.prePeriodStart);
                $scope.editModel.model.prePeriodEnd = formatToDate($scope.editModel.model.prePeriodEnd);
                $scope.editModel.model.signupDeadline = formatToDate($scope.editModel.model.signupDeadline);
                if ($scope.editModel.model.promotionType) {
                    $scope.editModel.model.promotionType = $scope.editModel.model.promotionType.toString();
                }
                // 转换活动场景的值
                if ($scope.editModel.model.promotionScene) {
                    var sceneArr = $scope.editModel.model.promotionScene.split(",");
                    for (idx in sceneArr) {
                        $scope.editModel.model['promotionScene_' + sceneArr[idx]] = true;
                    }
                }
            });
        };

        $scope.addTag = function () {
            if ($scope.editModel.tagList) {
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "",active:1});
            } else {
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": "",active:1}];
            }
        };

        $scope.getTagList = getTagList;
        function getTagList(){
            var tagList = _.filter( $scope.editModel.tagList, function(tag){ return tag.active==1; });
            return tagList || [];
        }

        $scope.delTag = function (tag) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
                .then(function () {
                    tag.active=0;
                });
        };

        /**
         *
         * @param date 字符串格式为yyyy-MM-dd ss:ss:ss
         * @returns {Date}
         */
        function formatToDate(date){
            return new Date(date) ;//$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        }
    }

    cms.directive('spEdit', [function spEditDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$scope', '$routeParams', 'jmPromotionService', 'alert', 'confirm', '$translate', '$filter', SpEditDirectiveController],
            templateUrl: '/modules/cms/views/jmpromotion/spedit.directive.html'
        }
    }]);
});