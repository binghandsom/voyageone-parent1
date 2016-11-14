/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms",
        "modules/cms/controller/popup.ctl"
    ],function (cms) {
        cms.controller("shelvesTemplateController", (function () {

            function ShelvesTemplateController($scope, shelvesTemplateService, popups) {
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
            return ShelvesTemplateController;

        })());
    }
);
