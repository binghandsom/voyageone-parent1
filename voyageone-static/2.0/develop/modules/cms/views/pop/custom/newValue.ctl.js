/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddAttributeValueNewCtl', function ($scope,$modalInstance, attributeValueService, attributeService , notify ,$translate, context,alert) {

        $scope.vm = {
            prop_id:"",
            value_original:"",
            value_translation:""
        };
        $scope.categoryList = context.categoryList;

        /**
         * 类目发生变化时,动态获取对应的属性值
         * @param catPath
         */
        $scope.valueChange = function(catPath){
            attributeService.init({cat_path:catPath,unsplitFlg:1})
                .then(function (res){
                    $scope.vm.valList = res.data.valList;
                });
        };

        /**
         * 保存新增属性值数据
         */
        $scope.ok = function () {

            // TODO 用$filter过滤出来该prop_id一样的数据.

            /*var checkResult = true;
            if($scope.cat_path == "0"){
                _.each($scope.vm.valList, function(value) {
                    if (_.isEqual(value.prop_id, $scope.vm.prop_id)) {
                        alert("该属性已经存在");
                        checkResult = false;
                    }
                });
            }else{
                _.each($scope.categoryList, function(value) {
                    if (_.isEqual(value.value_original, $scope.vm.value_original)) {
                        alert("该属性已经存在");
                        checkResult = false;
                    }
                });
            }*/
            //_.each(context.from, function(value) {
            //    if (_.isEqual(value.value_original, $scope.vm.value_original)) {
            //        alert("该属性已经存在");
            //        checkResult = false;
            //    }
            //});
            /*if (checkResult) {
                $scope.vmInfo ={
                    prop_id: $scope.vm.prop_id,
                    value_original: $scope.vm.value_original,
                    value_translation: $scope.vm.value_translation
                };
                attributeValueService.add($scope.vmInfo)
                    .then(function () {
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        $modalInstance.close();
                        //$scope.$parent.initialize();
                    });
            }*/
            $scope.vmInfo ={
                prop_id: $scope.vm.prop_id,
                value_original: $scope.vm.value_original,
                value_translation: $scope.vm.value_translation
            };
            attributeValueService.add($scope.vmInfo)
                .then(function () {
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    $modalInstance.close();
                });
        };

        $scope.close = function () {
            $modalInstance.dismiss();
        }
    });

});