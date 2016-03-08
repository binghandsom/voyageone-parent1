/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddAttributeValueNewCtl', function ($scope,$modalInstance, attributeValueService, attributeService , notify ,$translate, context) {

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


            attributeValueService.add($scope.vm)
                .then(function () {
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    $modalInstance.close();
                });
        };

    });

});