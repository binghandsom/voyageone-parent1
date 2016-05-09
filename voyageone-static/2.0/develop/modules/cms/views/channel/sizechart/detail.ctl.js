/**
 * Created by tony-piao on 2016/4/28.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function sizeDetailController($scope,$routeParams,sizeChartService) {
        $scope.vm = {
            originCondition : JSON.parse($routeParams.sizeChart),    //保存初始状态
            saveInfo : JSON.parse($routeParams.sizeChart),
            importList:[],
            brandNameList:[],
            productTypeList:[],
            sizeTypeList:[]

        }

        function sizeChartTr(){
            this.originSize = "";
            this.plateFormSize = "";
            this.customSize = false;
        }

        $scope.initialize  = function () {
            sizeChartService.init().then(function(resp){
                $scope.vm.brandNameList = _.pluck(resp.data.brandNameList,"value");
                $scope.vm.productTypeList = _.pluck(resp.data.productTypeList,"value");
                $scope.vm.sizeTypeList = _.pluck(resp.data.sizeTypeList,"value");
            });

        };

        $scope.addSize = function(){
            $scope.vm.importList.push(new sizeChartTr());
        }

        /**
         * 保存修改后的尺码表
         * {sizeChartName: "", finishFlag:"",brandNameList:[],productTypeList:[],sizeTypeList:[]}
         */
        $scope.saveFinish = function(){
            var upEntity = $scope.vm.saveInfo;
            sizeChartService.editSave({sizeChartName: upEntity.sizeChartName, finishFlag:upEntity.finish,
                                        brandNameList:upEntity.brandName,productTypeList:[],sizeTypeList:[]}).then(function(){
                notify.success ("添加成功！");
                $scope.$close();
            });
        }

        /**
         * 保存导入（输入）的尺码表
         */
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

    sizeDetailController.$inject = ['$scope','$routeParams','sizeChartService'];
    return sizeDetailController;
});