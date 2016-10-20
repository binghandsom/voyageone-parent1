define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPriceModifyCtl', function ($scope,jmPromotionDetailService,alert,context, $routeParams) {
        $scope.vm={tagNameList:[],tagList:[]};
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
            if (listPromotionProduct.length == 0) {
                alert("请选择修tag的商品!");
                return;
            }

            var parameter = {};
            parameter.listPromotionProductId = $scope.getSelectedPromotionProductIdList(listPromotionProduct);
            parameter.jmPromotionId = jmPromotionId;
            parameter.tagNameList = $scope.vm.tagNameList;

            jmPromotionDetailService.batchUpdateDealPrice(parameter).then(function (res) {
                if (!res.data.result) {
                    alert(res.data.msg);
                    return;
                }
                $scope.$close();
                context.search();
            }, function (res) {
                alert(res);
            });
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

    });
});