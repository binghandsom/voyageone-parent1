/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function sizeDetailController($scope,$routeParams,sizeChartService,alert,notify,$translate) {

        $scope.vm = {
            originCondition : [],    //保存初始状态
            saveInfo : [],
            importList:[],
            brandNameList:[],
            productTypeList:[],
            sizeTypeList:[]

        };

        function sizeChartTr(){
            this.originalSize = "";
            this.adjustSize = "";
            this.usual = false;
        };

        $scope.initialize  = function () {
            getItemById();
            sizeChartService.init().then(function(resp){
                $scope.vm.brandNameList = _.pluck(resp.data.brandNameList == null?[]:resp.data.brandNameList,"value");
                $scope.vm.productTypeList = _.pluck(resp.data.productTypeList == null?[]:resp.data.productTypeList,"value");
                $scope.vm.sizeTypeList = _.pluck(resp.data.sizeTypeList == null?[]:resp.data.sizeTypeList,"value");
            });

        };

        function getItemById(){
            sizeChartService.detailSearch({sizeChartId:Number($routeParams.sizeChartId)}).then(function(resp){
                var item = $scope.vm.saveInfo  = resp.data.sizeChartList[0];
                $scope.vm.originCondition = angular.copy(resp.data.sizeChartList[0]);
                $scope.vm.importList = _.map(item.sizeMap == null ?[]:item.sizeMap, function(item){
                    if(item.usual == "0")
                        item.usual = false;
                    else
                        item.usual = true;
                    return item;
                });
            });
        }

        /**
         * 添加尺码表
         */
        $scope.addSize = function(){
            $scope.vm.importList.push(new sizeChartTr());
        };
        /**
         * 重置
         */
        $scope.reset = function(){
            $scope.vm.saveInfo = angular.copy($scope.vm.originCondition);
        };

        /**
         * 保存修改后的尺码表
         * {sizeChartName: "", finishFlag:"",brandNameList:[],productTypeList:[],sizeTypeList:[]}
         */
        $scope.saveFinish = function(){
            if($scope.vm.saveInfo.sizeChartName == ""){
                //$translate.instant('TXT_MSG_DELETE_SUCCESS')
                alert("请输入尺码表名称");
                return;
            }

            var upEntity = $scope.vm.saveInfo;
            sizeChartService.detailSave({sizeChartId:upEntity.sizeChartId,sizeChartName: upEntity.sizeChartName, finishFlag:upEntity.finish,
                                        brandNameList:upEntity.brandName,productTypeList:upEntity.productType,sizeTypeList:upEntity.sizeType}).then(function(){
                notify.success($translate.instant('TXT_MSG_ADD_SUCCESS'));
                getItemById();
                $scope.$close();
            });
        };

        /**
         * 保存导入（输入）的尺码表
         */
        $scope.saveSize = function(){
            if($scope.vm.saveInfo.sizeChartName == ""){
                alert($translate.instant('TXT_SIZE_CHART_NOTICE_REQUIED'));
                return;
            }

            var upEntity = $scope.vm.saveInfo,sizeMaps = angular.copy($scope.vm.importList) , flag = true , tmpOriginalSize = "";
            _.map(sizeMaps, function(item){
                            if(item.originalSize == tmpOriginalSize){
                                alert($translate.instant('TXT_SIZE_CHART_NOTICE_REPEAT'));
                                flag = false;
                                return;
                            }
                            else
                                tmpOriginalSize = item.originalSize;
                            if(item.originalSize == "" || item.adjustSize == ""){
                                alert($translate.instant('TXT_SIZE_CHART_NOTICE_NULL'));
                                flag = false;
                                return;
                            }
                            if(item.usual == true)
                                item.usual = "1";
                            else
                                item.usual = "0";
                            return item;
                        });
            if(!flag) return;
            sizeChartService.detailSizeMapSave({sizeChartId:upEntity.sizeChartId,sizeMap:sizeMaps}).then(function(){
                notify.success ($translate.instant('TXT_MSG_ADD_SUCCESS'));
                getItemById();
                $scope.$close();
            });
        };

        /**
         * 删除添加项
         * @param index
         */
        $scope.delSize = function(index){
            $scope.vm.importList.splice(index,1);
        };

        /**
         * 处理导入数组
         * @param result
         */
        $scope.import = function(result){
            $scope.vm.importList = [];
            var tmp = 0 , count = 0, obj = new sizeChartTr();
             for(var i=0,length=result.length;i<length;i++){
                 if(tmp == result[i].index){
                     switch(count){
                         case 0:
                             obj.originalSize = result[i].value;
                             break;
                         case 1:
                             obj.adjustSize = result[i].value;
                             break;
                         case 2:
                             obj.usual = result[i].value == 1?true:false;
                             break;
                         default:
                             break;
                     }
                     count++;
                 }else{
                     $scope.vm.importList.push(obj);
                     obj = new sizeChartTr();
                     obj.originalSize = result[i].value;
                     tmp = result[i].index;
                     count = 1;
                 }
             }
            $scope.vm.importList.push(obj);
        };


    }

    sizeDetailController.$inject = ['$scope','$routeParams','sizeChartService','alert','notify','$translate'];
    return sizeDetailController;
});