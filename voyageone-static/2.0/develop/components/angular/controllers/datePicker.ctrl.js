/**
 * @Description:
 * 弹出日期控件
 * @User:    Edward
 * @Version: 0.2.0, 2015-10-10
 */

angular.module('voyageone.angular.controllers.datePicker', [])
    .controller('datePickerCtrl', function ($scope, $translate, uibDatepickerPopupConfig) {
        var vm = this;

        vm.formats = ['yyyy-MM-dd', 'yyyy-MM-dd HH:mm:ss'];

        $scope.formatDate = vm.formats[0];
        $scope.formatDateTime = vm.formats[1];
        $scope.opened = false;

        $scope.open = open;
        uibDatepickerPopupConfig.currentText = $translate.instant('BTN_TODAY');
        uibDatepickerPopupConfig.clearText = $translate.instant('BTN_CLEAR');
        uibDatepickerPopupConfig.closeText = $translate.instant('BTN_CLOSE');

        function open($event, swhich, count) {
            $event.preventDefault();
            $event.stopPropagation();

            // 如果该数组未空,则初始化数组
            if ($scope.$parent.datePicker.length == 0) {
                for(var i = 0; i < count; i++) {
                    if (swhich == i)
                        $scope.$parent.datePicker.push({opened: true});
                    else
                        $scope.$parent.datePicker.push({opened: false});
                }
            }
            // 关闭其他日期控件画面
            else {
                angular.forEach($scope.$parent.datePicker, function (object, index) {
                    if (swhich == index)
                        object.opened = !object.opened;
                    else
                        object.opened = false;
                });
            }
        }
    });