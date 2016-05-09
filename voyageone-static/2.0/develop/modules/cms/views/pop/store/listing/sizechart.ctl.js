/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popSizeChartCtl', function ($scope, context,sizeChartService,notify) {
        $scope.vm = {
            saveInfo:{sizeChartName: "", finishFlag:"",brandNameList:[],productTypeList:[],sizeTypeList:[]}
        };

        $scope.dropdown = context;

        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
        };

        $scope.save = function(){
           sizeChartService.editSave($scope.vm.saveInfo).then(function(){
                $scope.$close();
                context.search();
            });
        }

    });
});