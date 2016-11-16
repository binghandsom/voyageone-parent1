/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms",
        "modules/cms/controller/popup.ctl"
    ], function (cms) {
        cms.controller("shelvesTemplateController", (function () {

            function ShelvesTemplateController($scope, shelvesTemplateService, popups, confirm) {
                $scope.open = true;
                $scope.vm = {
                    templateTypes: {},
                    clientTypes: {},
                    carts: {}
                };

                $scope.templates = [];

                $scope.searchBean = {
                    templateName: ""
                };

                $scope.initialize = function () {
                    init();
                };
                function init() {
                    shelvesTemplateService.init().then(function (resp) {
                        $scope.vm.templateTypes = resp.data.templateTypes == null ? {} : resp.data.templateTypes;
                        $scope.vm.clientTypes = resp.data.clientTypes == null ? {} : resp.data.clientTypes;
                        $scope.vm.carts = resp.data.carts == null ? [] : resp.data.carts;
                        $scope.templates = resp.data.templates == null ? [] : resp.data.templates;
                    });
                }

                $scope.popNewShelvesTemplate = function () {
                    var upEntity = angular.copy($scope.vm);
                    popups.shelvesTemplateAdd(upEntity).then(function () {
                        search();
                    });
                }
                $scope.popEditShelvesTemplate = function (template) {
                    var upEntity = angular.copy($scope.vm);
                    popups.shelvesTemplateEdit(_.extend(upEntity, template)).then(function () {
                        search();
                    });
                }
                $scope.deleteShelvesTemplate = function (context) {
                    confirm("确定要删除吗？").then(function () {
                        shelvesTemplateService.delete({
                            "templateId": context.id
                        }).then(function (resp) {
                            search();
                        });
                    });
                }
                $scope.searchShelvesTemplate = function () {
                    search();
                }
                function search() {
                    var copy_clientTypes = angular.copy($scope.searchBean.clientTypes);
                    var clientTypesObj = _.pick(copy_clientTypes, function (value, key, object) {
                        return value;
                    });
                    var clientTypes = _.keys(clientTypesObj);
                    var copy_carts = angular.copy($scope.searchBean.carts);
                    var cartsObj = _.pick(copy_carts, function (value, key, object) {
                        return value;
                    });
                    var carts = _.keys(cartsObj);
                    var copy_templateTypes = angular.copy($scope.searchBean.templateTypes);
                    var templateTypesObj = _.pick(copy_templateTypes, function (value, key, object) {
                        return value;
                    });
                    var templateTypes = _.keys(templateTypesObj);
                    shelvesTemplateService.search(
                        _.extend(_.extend(_.extend(angular.copy($scope.searchBean),{"carts":carts}), {"templateTypes": templateTypes}), {"clientTypes":clientTypes})
                    ).then(function (resp) {
                        $scope.templates = resp.data.templates == null ? [] : resp.data.templates;
                    });
                }

                $scope.clear = function () {
                    $scope.searchBean = {};
                }
            }

            return ShelvesTemplateController;

        })());
    }
);
