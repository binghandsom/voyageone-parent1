/**
 * Created by sofia on 6/7/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('popSalePriceCtl', function ($scope, $fieldEditService, $translate, $modalInstance, $filter, confirm, notify, alert, context, cActions) {

        $scope.vm = {
            property: context.property,
            cartId: context.property.cartId.toString(),
            cartList: context.cartList,
            config:context.config,
            _optStatus: false,
            priceTypeId: 0,
            roundType: 0,
            skuUpdType: 0
        };

        if(context.config){
            $scope.vm.roundType = context.config.configValue3 ? context.config.configValue3 + "" : 0;
            $scope.vm.skuUpdType = context.config.configValue2 ? context.config.configValue2 + "" : 0;
        }

        $scope.save = function () {
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
            $scope.vm.property.priceType = $scope.vm.priceType;
            $scope.vm.property.optionType = $scope.vm.optType;
            $scope.vm.property.priceValue = $scope.vm.priceValue;
            $scope.vm.property.roundType = parseInt($scope.vm.roundType);
            $scope.vm.property.skuUpdType = parseInt($scope.vm.skuUpdType);
            confirm($translate.instant('TXT_BULK_SETSALEPRICE')).then(function(){
                _setProductFields($scope.vm.property);
            });

            function _setProductFields(params) {
                $fieldEditService.setProductFields(params).then(function (res) {
                    if (res.data.ecd == null || res.data.ecd == undefined) {
                        alert($translate.instant('TXT_COMMIT_ERROR'));
                        return;
                    }
                    if (res.data.ecd == 1) {
                        // 未选择商品
                        alert($translate.instant('未选择商品，请选择后再操作'));
                        return;
                    }
                    if (res.data.ecd == 6) {
                        // 数据错误
                        alert("商品[code=" + res.data.prodCode + "]的数据错误，没有skuCode。");
                        return;
                    }
                    if (res.data.ecd == 7) {
                        alert("未填写价格，请填写后再操作。");
                        return;
                    }
                    if (res.data.ecd == 8) {
                        // 数据错误
                        alert("商品[code=" + res.data.prodCode + ", sku=" + res.data.skuCode + "]的价格计算发生错误。请联系IT.");
                        return;
                    }
                    if (res.data.ecd == 9) {
                        // 数据错误
                        alert("商品[code=" + res.data.prodCode + ", sku=" + res.data.skuCode + "]的数据错误，没有[" + res.data.priceType + "]的数据。");
                        return;
                    }
                    if (res.data.ecd == 10) {
                        // 数据错误
                        alert("商品[code=" + res.data.prodCode + ", sku=" + res.data.skuCode + "]的最终售价计算结果为负数，请重新输入。");
                        return;
                    }

                    if (res.data.ecd == 0) {
                        if (res.data.unProcList != undefined && res.data.unProcList > 0) {
                            // 有未处理的商品
                            alert('批量更新最终售价已完成，但是有一些商品因不符合验证规则而没有处理，<br>请点击[确定]<label class="text-u-red font-bold">下载</label>未处理的商品code一览。').then($scope.exportFile);
                        }
                    }

                    $modalInstance.close();
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                });
            }
        };

        $scope.exportFile = function() {
            function _exportFileCallback (res) {
                var obj = JSON.parse(res);
                if (obj.code == '4001') {
                    alert("创建文件时出错。");
                    return;
                }
                if (obj.code == '4000') {
                    alert("创建文件时出错。");
                }
            }
            $.download.post(cActions.cms.pop.$fieldEditService.root + '/' + cActions.cms.pop.$fieldEditService.dldUnProcCode4PriceSale, {}, _exportFileCallback);
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
            // $scope.vm.roundType = "1";
            if (priceTypeVal == 4) {
                // 基准价格为None时只允许选等于号
                $scope.vm._optStatus = true;
                $scope.vm.optType = '=';
                $scope.vm._opeText = '';
                $scope.vm._typeText = '';
                $scope.vm.priceInputFlg = true;
                // $scope.vm.skuUpdType = "0";
            } else {
                $scope.vm._optStatus = false;
                $scope.vm.optType = '';
                $scope.vm._opeText = '';
                $scope.vm._typeText = $translate.instant(typeTxt);
                $scope.vm.priceInputFlg = false;
                // $scope.vm.skuUpdType = "1";
            }
        };

    });
});