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


        $scope.import = function(result){
            $scope.vm.importList = [];
            var tmp = 0 , count = 0, obj = new sizeChartTr();
             for(var i=0,length=result.length;i<length;i++){
                 if(tmp == result[i].index){
                     switch(count){
                         case 0:
                             obj.originSize = result[i].value;
                             break;
                         case 1:
                             obj.plateFormSize = result[i].value;
                             break;
                         case 2:
                             obj.customSize = result[i].value == 1?true:false;
                             break;
                         default:
                             break;
                     }
                     count++;
                 }else{
                     $scope.vm.importList.push(obj);
                     obj = new sizeChartTr();
                     obj.originSize = result[i].value;
                     tmp = result[i].index;
                     count = 1;
                 }
             }
            $scope.vm.importList.push(obj);
        }

    }

    sizeDetailController.$inject = ['$scope','$routeParams'];
    return sizeDetailController;
});