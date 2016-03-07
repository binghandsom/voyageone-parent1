/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddAttributeValueNewCtl', function ($scope,$modalInstance, attributeValueService, attributeService , notify ,$translate) {

        $scope.vm = {
            prop_id:"",
            value_original:"",
            value_translation:""
        };

        attributeService.getCatList()
            .then(function (res){
            $scope.vm.categoryList = res.data.categoryList;

        });


        $scope.valueChange=function(catPath){
            attributeService.getCatTree({cat_path:catPath,unsplitFlg:1})
                .then(function (res){
                    $scope.vm.valList = res.data.valList;

                });
        };

        $scope.ok = function () {
            var nData = {};
            nData.value_original = $scope.vm.value_original;
            nData.value_translation = $scope.vm.value_translation;

            attributeValueService.add(nData)
                .then(function (res) {
                    if(res.code == 0){
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        $scope.$parent.initialize();

                    }else if(res.code == 1){
                        notify($translate.instant('TXT_MSG_PRODUCT_IS_PUBLISHING'));
                    }
                });
            $scope.$close();

            //$modalInstance.close(nData);
        };

    });

});