define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('TagModifyCtl', function ($scope,promotionDetailService,alert,context, $routeParams) {
        $scope.vm={tagNameList:[],tagList:[],actionType:0};
        var selectProductList=[];
        var promotionId=undefined;
        var isBegin=false;
        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {
                    actionType:0
                };
            }
            //$scope.vm.attsList = $routeParams.attsList;
            if (context) {
                selectProductList = context.selectProductList;
                promotionId = context.promotionId;
                $scope.vm.tagList = context.tagList;
                isBegin = context.isBegin;
            }
        };
        $scope.ok = function () {
            if (selectProductList.length == 0 && context.selAll == false) {
                alert("请选择修改tag的商品!");
                return;
            }
            var listPromotionProductId = [];
            for (var i = 0; i < selectProductList.length; i++) {
                listPromotionProductId.push(selectProductList[i].id);
            }

            var parameter = {};
            parameter.listPromotionProductId = listPromotionProductId;
            parameter.promotionId = promotionId;
            parameter.key = context.key;
            parameter.selAll = context.selAll;
            parameter.actionType = $scope.vm.actionType;
            var productTagList = [];
            for (var i = 0; i < $scope.vm.tagNameList.length; i++) {
                var tagName = $scope.vm.tagNameList[i];
                var tag = _.find($scope.vm.tagList, function (tag) {
                    return tag.tagName == tagName;
                });
                productTagList.push({tagId: tag.id, tagName: tag.tagName,checked:2});//checked=2 新增
            }
            parameter.tagList = productTagList;

            promotionDetailService.updatePromotionListProductTag(parameter).then(function (res) {
                $scope.$close();
                context.search();
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        };

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