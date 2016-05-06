/**
 * Created by 123 on 2016/4/28.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function sizeDetailController($scope,$routeParams) {
        $scope.vm = {
            importList:[]
        }

        function sizeChartTr(){
            this.originSize = "";
            this.plateFormSize = "";
            this.customSize = false;
        }

        $scope.initialize  = function () {
            //console.log($routeParams.sizeChart);
        };

        $scope.addSize = function(){
            $scope.vm.importList.push(new sizeChartTr());
        }

        $scope.saveSize = function(){
            console.log($scope.vm.importList);
        }

        $scope.delSize = function(index){
            $scope.vm.importList.splice(index,1);
        }
    }

    sizeDetailController.$inject = ['$scope','$routeParams'];
    return sizeDetailController;
});