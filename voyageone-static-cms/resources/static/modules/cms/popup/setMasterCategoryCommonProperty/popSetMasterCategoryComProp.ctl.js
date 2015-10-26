/**
 * Created by sky on 2015/08/31
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require ('modules/cms/popup/setMasterCategoryCommonProperty/popSetMasterCategoryComProp.service');
    cmsApp.controller('popSetMasterCategoryComPropController', ['$scope', 'popSetMasterCategoryComPropService', 'userService', '$modalInstance', 'productDataArray', 'notify',
        function ($scope, popSetMasterCategoryComPropService, userService, $modalInstance, productDataArray, notify) {

            var commonUtil = require('components/util/commonUtil');

            var modelIdArray = [];

            for(var i=0;i<productDataArray.length;i++){
                var productData = productDataArray[i];
                if(_.indexOf(modelIdArray, productData.modelId)==-1){
                    modelIdArray.push(productData.modelId);
                }
            }

            $scope.modelIdList = modelIdArray;

            // 关闭对话框
            $scope.close = closeDialog;

            $scope.productStatus={};

            $scope.getOptions = function () {

                popSetMasterCategoryComPropService.getOptions({}).then(function(response) {

                    $scope.options = response.data.options;

                });
            };

            $scope.update = function(productStatus){

                var propertyList =[];

                var delPropertyList =[];

                if(productStatus.itemStatus){
                    var masterProp = {propName:"商品状态",propValue:productStatus.itemStatus};
                }

                if(productStatus.itemStatus==1){
                    if(productStatus.startTime){
                        var startTime = commonUtil.doFormatDate (productStatus.startTime);
                        propertyList.push({propName:"开始时间",propValue:startTime});
                    }

                }else{
                    delPropertyList.push({propName:"开始时间"});
                }



                var updateData={
                    masterProperty:masterProp,
                    properties:propertyList,
                    delProperties:delPropertyList,
                    tarModelList:$scope.modelIdList
                }

                popSetMasterCategoryComPropService.update(updateData).then(
                    function(){
                        closeDialog();
                        notify("CMS_TXT_MSG_UPDATE_SUCCESS");
                    }

                )

            }

            function closeDialog() {
                $modalInstance.dismiss('close');
            }

        }]);

});