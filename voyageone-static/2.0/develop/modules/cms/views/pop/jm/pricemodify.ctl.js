define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPriceModifyCtl', function ($scope, $translate, jmPromotionDetailService,alert,context, $routeParams) {

        $scope.model = {priceValueType:1,priceType:"1",discount:0,price:0};
        var listPromotionProduct=[];
        var jmPromotionId=undefined;
        var isBegin=false;
        $scope.vm = {
            property: context.property,
            _optStatus: false,
            priceTypeId: 0,
            roundType: 1,
            skuUpdType: 1
        };

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
                alert("请选择修改价格的商品!");
                return;
            }
            // 检查输入
            if ($scope.vm.priceTypeId == 0) {
                alert("未选择基准价格，请选择后再操作。");
                return;
            }
            if ($scope.vm.optType == undefined || $scope.vm.optType == '') {
                alert("未选择表达式，请选择后再操作。");
                return;
            }
            if ($scope.vm.optType != '=' && ($scope.vm.priceValue == undefined || $scope.vm.priceValue == '')) {
                alert("未填写价格，请填写后再操作。");
                return;
            }
            if ($scope.vm.priceTypeId == 4 && ($scope.vm.priceValue == undefined || $scope.vm.priceValue == '')) {
                alert("未填写价格，请填写后再操作。");
                return;
            }
            // 检查输入数据
            var intVal = $scope.vm.priceValue;
            if (!(intVal == null || intVal == undefined || intVal == '')) {
                if (isNaN(intVal)) {
                    alert("价格必须是数字");
                    return;
                }
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
        };

        // 选择表达式时的画面检查
        $scope.chkOptionType = function () {
            $scope.vm.priceValue = null;
            if ($scope.vm.optType == '') {
                $scope.vm._opeText = '';
                $scope.vm.priceInputFlg = false;
            } else if ($scope.vm.optType == '=') {
                $scope.vm._opeText = '';
                if ($scope.vm.priceTypeId == 4) {
                    // 基准价格为None时才可以输入
                    $scope.vm.priceInputFlg = true;
                } else {
                    $scope.vm.priceInputFlg = false;
                }
            } else {
                $scope.vm._opeText = $scope.vm.optType;
                $scope.vm.priceInputFlg = true;
            }
        };

        // 选择基准价格时的画面检查
        $scope.chkPriceType = function (priceTypeVal, typeTxt) {
            $scope.vm.priceTypeId = priceTypeVal;
            $scope.vm.priceValue = null;
            $scope.vm.roundType = "1";
            if (priceTypeVal == 4) {
                // 基准价格为None时只允许选等于号
                $scope.vm._optStatus = true;
                $scope.vm.optType = '=';
                $scope.vm._opeText = '';
                $scope.vm._typeText = '';
                $scope.vm.priceInputFlg = true;
                $scope.vm.skuUpdType = "0";
            } else {
                $scope.vm._optStatus = false;
                $scope.vm.optType = '';
                $scope.vm._opeText = '';
                $scope.vm._typeText = $translate.instant(typeTxt);
                $scope.vm.priceInputFlg = false;
                $scope.vm.skuUpdType = "1";
            }
        };
    });
});