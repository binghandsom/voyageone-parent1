/**
 * platformPropMappingController
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    'use strict';
    return cms.controller('platformPropMappingController', (function () {

        function PlatformMappingController(platformMappingService, $routeParams) {

            this.platformMappingService = platformMappingService;

            this.platformCategoryId = $routeParams['platformCategoryId'];
        }

        PlatformMappingController.prototype = {

            init: function () {

                alert(this.platformCategoryId);
            }
        };

        return PlatformMappingController;
    })());
});