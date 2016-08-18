define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPriceModifyCtl', function ($scope,jmPromotionDetailService,alert,context, $routeParams) {
        $scope.model={priceValueType:1,priceType:"1",discount:0,price:0};
        var listPromotionProduct=[];
        var jmPromotionId=undefined;
        var isBegin=false;
        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
            //$scope.vm.attsList = $routeParams.attsList;
            if (context) {
                listPromotionProduct = context.listPromotionProduct;
                jmPromotionId = context.jmPromotionId;
                isBegin = context.isBegin;
            }
        };
        $scope.ok = function () {

            if(listPromotionProduct.length==0)
            {
                alert("请选择修改价格的商品!");
                return;
            }
            //  if(预热已开始&&synch_status==2)//包含已上新的商品 提示
            var isUpdate=true;
            if(isBegin) {
                for (var i = 0; i < listPromotionProduct.length; i++) {
                    if (listPromotionProduct[i].synchStatus == 2) {
                        isUpdate=false;
                        confirm("专场已上线，变更价格一旦同步至平台将引起客诉，点击确认继续操作。").then(function () {
                          isUpdate=true;
                        });
                    }
                }
            }
            if(!isUpdate) return;
            $scope.model.listPromotionProductId = $scope.getSelectedPromotionProductIdList(listPromotionProduct);
            $scope.model.jmPromotionId=jmPromotionId;
            var parameter= angular.copy($scope.model);
            parameter.discount = $scope.model.discount ? $scope.model.discount * 0.1 : 1;
            jmPromotionDetailService.batchUpdateDealPrice(parameter).then(function (res) {
                if (!res.data.result) {
                    alert(res.data.msg);
                    return;
                }
                $scope.$close();
                context.search();
            }, function (res) {
              alert(res);
            })
        }
        $scope.getSelectedPromotionProductIdList = function (modelList) {
            var listPromotionProductId = [];
            for (var i = 0; i < modelList.length; i++) {
                if (modelList[i].isChecked) {
                    listPromotionProductId.push(modelList[i].id);
                }
            }
            return listPromotionProductId;
        }
        $scope.mnumberKeydown=function(e){
            var ss=window.event||e;
            if(!((ss.keyCode>47&&ss.keyCode<58)||(ss.keyCode>64&&ss.keyCode<91)||(ss.keyCode>95&&ss.keyCode<106))){
                ss.preventDefault();
            }
        }
    });
});