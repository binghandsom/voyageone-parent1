define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popJMPromotionEncoreCtl', function ($scope, jmPromotionService, alert, context, confirm, $translate, $filter) {
        $scope.vm = {"isFromBox": false};
        $scope.editModel = {model: {}};
        $scope.datePicker = [];
        $scope.srcJmPromotionId = context.promotion.id;

        $scope.$watch('editModel.model.activityEnd', function (newValue) {
            if (newValue && $scope.vm.isFromBox) {
                $scope.vm.isFromBox = false;
                $scope.editModel.model.activityEnd = new Date(newValue.toString().substr(0, 11) + "23:59:59");
            }
        });

        $scope.initialize = function () {
            /** 默认值设置 */





            jmPromotionService.init().then(function (res) {
                $scope.vm.metaData = res.data;
                $scope.editModel.model.masterBrandName =context.promotion.masterBrandName;
            });
        };

        $scope.addTag = function (tag) {
            var tagList = $scope.editModel.tagList,
                newTag;

            if (!tagList)
                $scope.editModel.tagList = tagList = [];

            newTag = {
                model: {id: "", channelId: "", tagName: "", active: 1},
                featured: false
            };

            if (tag)
                newTag = angular.merge(newTag, tag);

            tagList.push(newTag);

            return newTag;
        };

        $scope.delTag = function (tag, index) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
                .then(function () {
                    // 如果数据在后台已经存在
                    // 那么就逻辑删除
                    // 否则物理删除
                    if (!tag.model.id)
                        $scope.editModel.tagList.splice(index, 1);
                    else
                        tag.model.active = 0;
                });
        };

        $scope.ok = function () {

            var start = new Date($scope.editModel.model.activityStart);
            var end = new Date($scope.editModel.model.activityEnd);

            if ($scope.editModel.model.activityStart > $scope.editModel.model.activityEnd) {
                alert("活动时间检查：请输入结束时间>开始时间，最小间隔为30分钟。");
                return;
            }

            if (end.getTime() - start.getTime() < 30 * 60 * 1000) {
                alert("活动时间检查：最小间隔为30分钟。");
                return;
            }

            if ($scope.editModel.model.prePeriodStart > $scope.editModel.model.prePeriodEnd) {
                alert("预热时间检查：请输入结束时间>开始时间。");
                return;
            }

            if ($scope.editModel.model.prePeriodStart > $scope.editModel.model.activityStart) {
                alert("预热开始时间不能晚于活动开始时间");
                return;
            }

            if ($scope.editModel.model.signupDeadline > $scope.editModel.model.prePeriodStart) {
                alert("报名截止日期不能晚于预热开始时间");
                return;
            }

            var _upEntity = angular.copy($scope.editModel);

            _upEntity.model.activityStart = formatToStr(_upEntity.model.activityStart);
            _upEntity.model.activityEnd = formatToStr(_upEntity.model.activityEnd);
            _upEntity.model.prePeriodStart = formatToStr(_upEntity.model.prePeriodStart);
            _upEntity.model.prePeriodEnd = _upEntity.model.activityEnd; //formatToStr(_upEntity.model.prePeriodEnd);
            _upEntity.model.signupDeadline = formatToStr(_upEntity.model.signupDeadline);
            _upEntity.model.comment = _upEntity.model.comment || "";

            // 转换活动场景的值
            _upEntity.model.promotionScene = JSON.stringify(_returnKey(_upEntity.model.promotionScene));

            _upEntity.model.id = $scope.srcJmPromotionId;
            jmPromotionService.encore(_upEntity.model).then(function (res) {
                $scope.$close(res);
            })
        };

        // 检查checkbox是否有输入
        $scope.checkboxVal = function (inputArr) {
            var inputObj = _.find(inputArr, function (item) {
                return item == true;
            });
            return !inputObj;
        };

        /**禁用日期*/
        $scope.disabled = function (date, mode) {
            return ( mode === 'day' && $scope.vm.isBeginPre );
        };

        // 只用于监控活动结束时间发生变化
        $scope.onDateChange = function () {
            $scope.vm.isFromBox = true;
            $scope.vm.datePicker2 = !$scope.vm.datePicker2;
        };

        $scope.onJmBrandChange = function () {
            $scope.editModel.model.brand = '';
            $scope.editModel.model.cmsBtJmMasterBrandId = '';

            if ($scope.editModel.model.masterBrandName) {
                var inputObj = _.find($scope.vm.metaData.jmMasterBrandList, function (item) {
                    return item.value == $scope.editModel.model.masterBrandName;
                });
                if (inputObj) {
                    $scope.editModel.model.brand = inputObj.value;
                    $scope.editModel.model.cmsBtJmMasterBrandId = inputObj.name;
                }
            }
        };

        /**
         *
         * @param date 字符串格式为yyyy-MM-dd ss:ss:ss
         * @returns {Date}
         */
        function formatToDate(date) {
            return new Date(date);//$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        }

        function formatToStr(date) {
            return $filter("date")(new Date(date), "yyyy-MM-dd HH:mm:ss");
        }

        /**
         * 如果checkbox被选中,返回被选中的value.
         * eg.[{new: true, pending: false, approved: true}] -> [new, approved]
         * @param object
         * @returns {*}
         */
        function _returnKey(object) {
            return _.chain(object)
                .map(function (value, key) {
                    return value ? key : null;
                })
                .filter(function (value) {
                    return value;
                })
                .value();
        }
    });
});