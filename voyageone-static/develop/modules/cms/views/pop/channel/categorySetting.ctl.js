/**
 * Created by 123 on 2016/5/20.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('categorySettingCtl',(function(){
        function CategorySettingCtl($scope){
            this.scope = $scope;
            this.level = "";
            this.maxTag = "";
        };

        CategorySettingCtl.prototype = {
            init:function(){

            },
            submitSet:function(){
                this.level = +$scope.vm.level;
                this.maxTag = +$scope.vm.maxTag;
                this.scope.$dismiss();
            }
        };

        return CategorySettingCtl;
    })());
});