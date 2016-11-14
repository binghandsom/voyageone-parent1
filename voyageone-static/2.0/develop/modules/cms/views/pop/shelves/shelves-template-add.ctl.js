/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms"
    ],function (cms) {
        cms.controller("ShelvesTemplateAddController", (function () {

            function ShelvesTemplateAddController($scope, shelvesTemplateService, popups) {
                $scope.vm = {
                    templates : [],
                    status : {
                        open : true
                    }
                };

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
            }
            return ShelvesTemplateAddController;

        })());
    }
);
