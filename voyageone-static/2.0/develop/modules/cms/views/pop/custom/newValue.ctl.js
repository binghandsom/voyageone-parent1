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
        $scope.valList = context.valueList;
        $scope.vm.cat_path = context.from;
        if ($scope.vm.cat_path == '0') {
            $scope.vm.cat_path = '共通属性';
        }

        /**
         * 类目发生变化时,动态获取对应的属性值
         * @param catPath
         */
        $scope.valueChange = function(catPath ){
            var catPathVal = catPath;
            if (catPathVal == '共通属性') {
                catPathVal = '0';
            }
            attributeService.init({cat_path:catPathVal,unsplitFlg:1})
                .then(function (res){
                    $scope.vm.valList = res.data.valList;
                });
        };

        /**
         * 保存新增属性值数据
         */
        $scope.ok = function () {

            var checkResult = true;
            _.each($scope.valueList, function(value) {

                if (_.isEqual(value.prop_id, $scope.vm.prop_id)
                     && _.isEqual(value.value_original, $scope.vm.value_original)) {
                    alert("该自定义属性值已经存在");
                    checkResult = false;
                }
            });

            if (checkResult) {
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
            }

        };

        $scope.close = function () {
            $modalInstance.dismiss();
        }
    });

});