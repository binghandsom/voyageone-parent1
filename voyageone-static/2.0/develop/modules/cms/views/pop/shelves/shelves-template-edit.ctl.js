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
                    var htmlImageTemplate = $scope.modelBean.htmlImageTemplate;
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
