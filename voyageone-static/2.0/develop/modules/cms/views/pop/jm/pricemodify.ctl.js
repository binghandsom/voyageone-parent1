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
            $scope.model.discount = $scope.model.discount ? $scope.model.discount * 0.1 : 1;
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
        $scope.mnumberKeydown=function(e){
            var ss=window.event||e;
            if(!((ss.keyCode>47&&ss.keyCode<58)||(ss.keyCode>64&&ss.keyCode<91)||(ss.keyCode>95&&ss.keyCode<106))){
                ss.preventDefault();
            }
        }
    });
});