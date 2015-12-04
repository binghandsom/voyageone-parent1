/**
 * @Name:    popMoveToModelController.js
 * @Date:    2015/08/26
 * @User:    sky
 * @Version: 1.0.0
 */
define(function (require) {
    var cmsApp = require('modules/cms/cms.module');

    require ('modules/cms/popup/moveToModel/popMoveToModel.service');
    require ("components/directives/dialogs/dialogs");

    cmsApp.controller('popMoveToModelController',
        ['$scope', 'popMoveToModelService', 'userService', '$modalInstance', 'productInfoArray', 'notify',
        function ($scope, popMoveToModelService, userService, $modalInstance, productInfoArray, notify) {

            var _ = require ('underscore');
            var vm = $scope.vm = {};

            vm.channelId = userService.getSelChannel();
            vm.productInfoArray = productInfoArray;
            vm.modelInfoArray = [];

            //删除产品
            $scope.delProduct = delProduct;
            //关闭对话框
            $scope.closeDialog = closeDialog;
            //根据model查询category主类目
            $scope.searchByModel = searchByModel;
            //更改model
            $scope.changeModel = changeModel;

            function closeDialog() {
                $modalInstance.dismiss('close');
            }

            function searchByModel() {
                vm.modelId = null;
                popMoveToModelService.searchCategoryByModel({
                    key: vm.modelSearchValue,
                    channelId: vm.channelId
                }).then(function (res) {
                    vm.modelInfoArray = [];
                    vm.categoryList = res.data.categoryList;
                    _.forEach (res.data.categoryList, function (modelInfo) {
                        vm.modelInfoArray.push({
                            id: modelInfo.modelId,
                            name: getFullModelWithPath(modelInfo.parentCategoryPath, modelInfo.model)
                        });
                    });
                })
            }

            function delProduct(productInfo) {
                vm.productInfoArray = _.filter(vm.productInfoArray, function (v) {
                    return v != productInfo
                })
            }

            function changeModel() {
                // 取得被选中的Model的所有信息.
                var selectedModelInfo = _.filter(vm.categoryList, function (m) {
                    return m.modelId == vm.modelId;
                });

                // 取得Product Id列表.
                var productIds = [];
                _.forEach(vm.productInfoArray, function (productInfo) {
                    productIds.push(productInfo.id);
                });

                var data = {
                    channelId: vm.channelId,
                    productIds: productIds,
                    modelId: vm.modelId,
                    modelInfo: selectedModelInfo
                };
                popMoveToModelService.changeModel(data).then(function () {
                    notify.success("CMS_MSG_MOVE_TO_MODEL_SUCCESS");
                    closeDialog();
                })
            }

            /**
             * 取得Model的name以及所属的Category的路径.
             * @param parentCategoryPath
             * @param model
             * @returns {string}
             */
            function getFullModelWithPath (parentCategoryPath, model) {
                var str = "";
                _.forEach(parentCategoryPath, function (categoryInfo) {
                    str += categoryInfo.name + " / ";
                });
                return str + model;
            }

        }]);
});