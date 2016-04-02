/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popSizeChartImportCtl', function ($scope,context, $routeParams,alert,$translate) {

        $scope.vm = {
            content : ""
        };

        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
            $scope.vm.imageMain = $routeParams.imageMain;
            $scope.vm.imageList = $routeParams.imageList;
        };

        $scope.importSize = function(){
            var importArr = $scope.vm.content.split(/\t/g),resultArr = [];

            angular.forEach(importArr, function(data,index){
                var tmp = data.split(/\n/g);
                if(tmp.length <= 1){
                    var tmpObj = {};
                    tmpObj.value = data;
                    resultArr.push(tmpObj);
                } else{
                    angular.forEach(tmp, function(data,index){
                        var tmpObj = {};
                        tmpObj.value = data;
                        resultArr.push(tmpObj);
                    });
                }
            });
           if(resultArr.length % 3 != 0){
               alert($translate.instant("TXT_SIZE_CHART_NOTICE_PS"));
               return;
           }
           for(var i=0,length=resultArr.length;i<length;i++){
                if((i+1)%3 == 0){
                    if(Number(resultArr[i].value) > 1 || Number(resultArr[i].value) < 0 || isNaN(resultArr[i].value)){
                        alert($translate.instant("TXT_SIZE_CHART_NOTICE_third"));
                        return;
                    }
                }
                resultArr[i].index = parseInt(i/3);
            }
            context.import(resultArr);
            $scope.$dismiss();
        }

    });
});