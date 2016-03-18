/**
 * Feed Attributes 品牌方属性弹出框 Controller
 */
define([
    'angularAMD'
], function (angularAMD) {
    angularAMD.controller('popFieldFeedDetailCtl', function ($scope, translationService, data) {

        $scope.productModel = data;

        $scope.initialize = function () {
            getFeedAttributes();
        };

        function getFeedAttributes() {
            var parms = {
                productCode: $scope.productModel.productCode
            };
            translationService.getFeedAttributes(parms).then(function (res) {

                var feedAttributes = res.data;

                $scope.feedAttributesModel = [];

                for (var keyName in feedAttributes){
                    var item = {key:keyName,value:feedAttributes[keyName]}
                    $scope.feedAttributesModel.push(item);
                }
            });
        }
    });
});