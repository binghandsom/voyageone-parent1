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
                $scope.layout = true;
                $scope.single = false;
                $scope.initialize = function () {
                    init();
                };
                function init() {
                    shelvesTemplateService.detail({
                        templateId:context.id
                    }).then(function (resp) {
                        $scope.modelBean = resp.data == null ? {} : resp.data;
                        var templateType = $scope.modelBean.templateType;
                        if (0 == templateType){
                            $scope.layout = true;
                            $scope.single = false;
                        }else {
                            $scope.layout = false;
                            $scope.single = true;
                        }
                    });
                }

                $scope.editSubmit = function () {
                    shelvesTemplateService.edit($scope.modelBean).then(function (resp) {
                        $scope.$close();
                    });
                }
            }
            return ShelvesTemplateAddController;

        })());
    }
);
