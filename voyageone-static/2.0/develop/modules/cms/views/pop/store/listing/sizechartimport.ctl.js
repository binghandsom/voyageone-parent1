/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popSizeChartImportCtl', function ($scope,context, $routeParams,alert) {

        $scope.vm = {
            content : ""
        }

        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
            $scope.vm.imageMain = $routeParams.imageMain;
            $scope.vm.imageList = $routeParams.imageList;
        };

        $scope.importSize = function(){
            var importArr = $scope.vm.content.split(/\t/g),resultArr = [];

            for(var i=0,length=importArr.length;i<length;i++) {
                var tmp = importArr[i].split(/\n/g);
                if(tmp.length <= 1){
                    var tmpObj = new Object();
                    tmpObj.value = tmp[0]
                    resultArr.push(tmpObj);
                } else{
                    for (var j = 0, length2 = tmp.length; j < length2; j++) {
                        var tmpObj = new Object();
                        tmpObj.value = tmp[j]
                        resultArr.push(tmpObj);
                    }
                }
            }

            for(var i=0,length=resultArr.length;i<length;i++){
                if((i+1)%3 == 0){
                    if(Number(resultArr[i].value) > 1 || Number(resultArr[i].value) < 0){
                        alert("请检查导入格式，第三位数据为0或者1");
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