define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPriceModifyCtl', function ($scope,jmPromotionDetailService,alert,context, $routeParams) {
        $scope.model={priceValueType:1,priceType:"1",discount:0,price:0};
        var listPromotionProductId=[];
        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
            //$scope.vm.attsList = $routeParams.attsList;
            if(context)
            {
                listPromotionProductId=context.listPromotionProductId;
            }
        };
        $scope.ok = function () {
            $scope.model.listPromotionProductId =listPromotionProductId;
            if(listPromotionProductId.length==0)
            {
                alert("请选择修改价格的商品!");
                return;
            }
            jmPromotionDetailService.batchUpdateDealPrice( $scope.model).then(function () {
                $scope.$close();
                context.search();
            }, function (res) {
              alert(res);
            })
        }
    });
});