/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms"
    ],function (cms) {
        cms.controller("shelvesTemplateController", (function () {

            function ShelvesTemplateController($scope, shelvesTemplateService) {
                $scope.vm = {
                    templates : []
                };

                $scope.initialize = function () {
                    init();
                };
                function init() {
                    shelvesTemplateService.init().then(function (resp) {
                        $scope.vm.templates = resp.data == null ? [] : resp.data;
                    });
                }
            }
            return ShelvesTemplateController;

        })());
    }
);
