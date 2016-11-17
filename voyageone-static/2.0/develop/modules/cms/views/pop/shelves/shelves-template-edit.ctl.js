/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms"
    ],function (cms) {
        cms.controller("ShelvesTemplateEditController", (function () {

            function ShelvesTemplateAddController($scope, shelvesTemplateService, popups, context) {
                $scope.vm = {
                    templateTypes:{},
                    clientTypes:{},
                    carts:{}
                };
                $scope.vm.templateTypes = context.templateTypes;
                $scope.vm.clientTypes = context.clientTypes;
                $scope.vm.carts = context.carts;

                $scope.modelBean = {};
                $scope.layout = false; // ng-show 显示【布局模板】输入框
                $scope.single = false; // ng-show 显示【单品模板】输入框
                $scope.layoutRequired = true;
                $scope.singleRequired = false;
                $scope.commonRequired = false;

                $scope.previewFlag = false; // 是否显示预览按钮
                $scope.initialize = function () {
                    init();
                };
                function init() {
                    shelvesTemplateService.detail({
                        templateId:context.id
                    }).then(function (resp) {
                        $scope.modelBean = resp.data == null ? {} : resp.data;
                        var templateTypeVal = $scope.modelBean.templateType;
                        if ("0" == templateTypeVal){ // 布局模板
                            $scope.layout = true;
                            $scope.single = false;
                            $scope.layoutRequired = true;
                            $scope.singleRequired = false;
                            $scope.commonRequired = false;
                        }else if("1" == templateTypeVal) { // 单品模板
                            $scope.layout = false;
                            $scope.single = true;
                            $scope.layoutRequired = false;
                            $scope.singleRequired = true;
                            $scope.commonRequired = true;
                            if ($scope.modelBean.htmlImageTemplate) {
                                $scope.previewFlag = true;
                            }
                        }
                    });
                }

                $scope.editSubmit = function () {
                    shelvesTemplateService.edit($scope.modelBean).then(function (resp) {
                        $scope.$close();
                    });
                }

                $scope.preview = function () {
                    // var htmlImageTemplate = $scope.modelBean.htmlImageTemplate;
                    // var imageTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/20161026-240x342-jiarugouwuche?$sn_240x342$&$yuanjia={yuanjia}&$shiyijia={shiyijia}&$product={image}&$name={name}";
                    var imageTemplate = $scope.modelBean.htmlImageTemplate;
                    if (imageTemplate.indexOf("{yuanjia}") != -1) {
                        imageTemplate = imageTemplate.replace("{yuanjia}", "1299");
                    }
                    if (imageTemplate.indexOf("{image}") != -1) {
                        imageTemplate = imageTemplate.replace("{image}", "sneakerhead/290-250");
                    }
                    if (imageTemplate.indexOf("{name}") != -1) {
                        imageTemplate = imageTemplate.replace("{name}", encodeURIComponent("天天搞机-魅蓝5全球首发"));
                    }
                    if (imageTemplate.indexOf("{promotionPrice}") != -1) {
                        imageTemplate = imageTemplate.replace("{promotionPrice}", "799");
                    }
                    window.open(imageTemplate);
                }

                $scope.checkPreview = function () {
                    if ($scope.modelBean.htmlImageTemplate) {
                        $scope.previewFlag = true;
                    }else {
                        $scope.previewFlag = false;
                    }
                }
            }
            return ShelvesTemplateAddController;

        })());
    }
);



