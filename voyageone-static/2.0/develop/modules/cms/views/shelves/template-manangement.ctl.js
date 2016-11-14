/**
 * Created by dell on 2016/11/14.
 */
define([
        "cms"
    ],function (cms) {
        cms.controller("shelvesTemplateController", (function () {

            function ShelvesTemplateController($scope, shelvesTemplateService) {
                this.$scope = $scope;
                this.shelvesTemlateService = shelvesTemplateService;
            }

            ShelvesTemplateController.prototype = {
                init: function () {
                    this.shelvesTemlateService.init().then(function (resp) {
                        
                    });
                }
            }
            
        })());
    }
);
