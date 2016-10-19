/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popJMPromotionDetailCtl', function ($scope,jmPromotionService,alert,context,confirm,$translate,$filter) {
        $scope.vm = {"jmMasterBrandList":[], "isFromBox": false};
        $scope.editModel = {model:{}};
        $scope.datePicker = [];

        $scope.$watch('editModel.model.activityEnd', function (newValue, oldValue) {
            if (newValue && $scope.vm.isFromBox) {
                $scope.vm.isFromBox = false;
                $scope.editModel.model.activityEnd = new Date(newValue.toString().substr(0, 11) + "23:59:59");
            }
        });

        $scope.initialize  = function () {
            /** 默认值设置 */
            $scope.vm.currentTime = new Date();
            if (context.id) {
                // 编辑
                jmPromotionService.getEditModel(context.id).then(function (res) {
                    $scope.editModel.model = res.data.model;
                    $scope.editModel.tagList = res.data.tagList;
                    $scope.editModel.model.activityStart = formatToDate($scope.editModel.model.activityStart);
                    $scope.editModel.model.activityEnd = formatToDate($scope.editModel.model.activityEnd);
                    $scope.editModel.model.prePeriodStart = formatToDate($scope.editModel.model.prePeriodStart);
                    $scope.editModel.model.signupDeadline = formatToDate($scope.editModel.model.signupDeadline);
                    // 准备期是否结束
                    $scope.vm.isDeadline = false;
                    if ($scope.editModel.model.signupDeadline < $scope.vm.currentTime) {
                        $scope.vm.isDeadline = true;
                    }
                    // 预热是否开始
                    $scope.vm.isBeginPre = false;
                    if ($scope.editModel.model.prePeriodStart < $scope.vm.currentTime) {
                        $scope.vm.isBeginPre = true;
                    }
                    // 活动是否开始
                    $scope.vm.isBegin = false;
                    if ($scope.editModel.model.activityStart < $scope.vm.currentTime) {
                        $scope.vm.isBegin = true;
                    }
                    // 活动是否结束
                    $scope.vm.isEnd = false;
                    if ($scope.editModel.model.activityEnd < $scope.vm.currentTime) {
                        $scope.vm.isEnd = true;
                    }

                    if ($scope.editModel.model.promotionType) {
                        $scope.editModel.model.promotionType = $scope.editModel.model.promotionType.toString();
                    }
                    // 转换活动场景的值
                    if ($scope.editModel.model.promotionScene) {
                        var sceneArr = JSON.parse($scope.editModel.model.promotionScene);
                        $scope.editModel.model.promotionScene = [];
                        angular.forEach(sceneArr, function(element) {
                            $scope.editModel.model.promotionScene[element] = true;
                        });
                    } else {
                        $scope.editModel.model.promotionScene = [];
                    }
                });
            } else {
                // 新建
                $scope.editModel.model.status=0;
                $scope.editModel.tagList = [];
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "移动端专场首推单品", active:1});
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "", active:1});
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

        $scope.delTag = function (tag) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
                .then(function () {
                  tag.active=0;
                });
        };

        $scope.ok = function() {
            var start = new Date($scope.editModel.model.activityStart);
            var end = new Date($scope.editModel.model.activityEnd);

            if($scope.editModel.model.activityStart > $scope.editModel.model.activityEnd){
                alert("活动时间检查：请输入结束时间>开始时间，最小间隔为30分钟。");
                return;
            }

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

            if($scope.editModel.model.signupDeadline > $scope.editModel.model.prePeriodStart){
                alert("报名截止日期不能晚于预热开始时间");
                return;
            }

            if ($scope.editModel.tagList.length === 0){
                alert("请至少添加一个标签");
                return;
            }
            var hasTag = _.every($scope.editModel.tagList, function(element) {
                return element.tagName;
            });
            if(!hasTag)
                return;

            var _upEntity = angular.copy($scope.editModel);

            _upEntity.tagList= _.filter( _upEntity.tagList, function(tag){ return tag.tagName != "";});
            _upEntity.model.activityStart = formatToStr(_upEntity.model.activityStart);
            _upEntity.model.activityEnd = formatToStr(_upEntity.model.activityEnd);
            _upEntity.model.prePeriodStart = formatToStr(_upEntity.model.prePeriodStart);
            _upEntity.model.prePeriodEnd =_upEntity.model.activityEnd; //formatToStr(_upEntity.model.prePeriodEnd);
            _upEntity.model.signupDeadline = formatToStr(_upEntity.model.signupDeadline);
            _upEntity.model.comment = _upEntity.model.comment || "";

            // 转换活动场景的值
            _upEntity.model.promotionScene = JSON.stringify(_returnKey (_upEntity.model.promotionScene));

            jmPromotionService.saveModel(_upEntity).then(function () {
                context = $scope.editModel.model;
                $scope.$close();
            })
        };

        /**禁用日期*/
        $scope.disabled = function(date, mode) {
            return ( mode === 'day' && $scope.vm.isBeginPre );
        };

        // 只用于监控活动结束时间发生变化
        $scope.onDateChange = function () {
            $scope.vm.isFromBox = true;

            if ($scope.vm.datePicker2) {
                $scope.vm.datePicker2 = false;
            } else {
                $scope.vm.datePicker2 = true;
            }
        };

        /**
         *
         * @param date 字符串格式为yyyy-MM-dd ss:ss:ss
         * @returns {Date}
         */
        function formatToDate(date){
             return new Date(date) ;//$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        }

        function formatToStr(date){
            return $filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        }

        /**
         * 如果checkbox被选中,返回被选中的value.
         * eg.[{new: true, pending: false, approved: true}] -> [new, approved]
         * @param object
         * @returns {*}
         */
        function _returnKey(object) {
            return _.chain(object)
                .map(function(value, key) { return value ? key : null;})
                .filter(function(value) { return value;})
                .value();
        }
    });
});