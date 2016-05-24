/**
 * Created by 123 on 2016/5/20.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('categorySettingCtl', function ($scope,context) {
        $scope.vm = {
            level:"",
            maxTag:""
        }
        $scope.submitSet = function(){
            context.level = +$scope.vm.level;
            context.maxTag = +$scope.vm.maxTag;
            $scope.$dismiss();
        }
    });
});