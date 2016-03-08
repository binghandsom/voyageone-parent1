/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddAttributeValueCtl', function ($scope, $modalInstance, context, alert) {

        $scope.vm = {
            value_original:"",
            value_translation:""
        };

        $scope.ok = function () {
            var nData = {};
            nData.prop_original = $scope.vm.value_original;
            nData.prop_translation = $scope.vm.value_translation;

            //if (!_.isEmpty(_.where(context.from, nData))) {
            //    alert("该属性已经存在");
            //} else {
            //    $modalInstance.close(nData);
            //}

            _.each(context.from, function(value) {
                if (value.prop_original==nData.prop_original) {
                    alert("该属性已经存在");
                } else {
                     $modalInstance.close(nData);
                }

            });
            //$scope.$close();
        };
    });


});