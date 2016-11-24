/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms"
    ],function (cms) {
        cms.controller("ShelvesTemplateAddController", (function () {

            function ShelvesTemplateAddController($scope, shelvesTemplateService, popups, context) {
                $scope.vm = {
                    templateTypes:{},
                    clientTypes:{},
                    carts:{}
                };
                $scope.vm.templateTypes = context.templateTypes;
                $scope.vm.clientTypes = context.clientTypes;
                $scope.vm.carts = context.carts;
                // 默认新建【布局模板】，每行个数为【1】
                $scope.modelBean = {
                    "templateType":"0",
                    "numPerLine":2
                };
                $scope.layout = true; // ng-show 默认显示【布局模板】输入框
                $scope.single = false; // ng-show 默认不显示【单品模板】输入框
                $scope.layoutRequired = true;
                $scope.singleRequired = false;
                $scope.commonRequired = false;

                $scope.initialize = function () {
                    init();
                };
                function init() {
                    shelvesTemplateService.init().then(function (resp) {
                        $scope.vm.templates = resp.data == null ? [] : resp.data;
                    });
                }

                $scope.popNewShelvesTemplate = function () {
                    popups.shelvesTemplateAdd();
                }

                $scope.addSubmit = function () {
                    shelvesTemplateService.add($scope.modelBean).then(function (resp) {
                        $scope.$close();
                    });
                }

                $scope.selTemplateType = function () {
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
                    }
                }
            }
            return ShelvesTemplateAddController;

        })());
    }
);
