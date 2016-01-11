define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    'use strict';
    /**
     * @ngdoc object
     * @name platformMappingController
     * @description
     *
     * 平台类目到主数据类目的匹配
     */
    return cms.controller('platformMappingController', (function () {

        /**
         * @description
         * 创建平台到主数据类目匹配的画面 Controller 类
         * @constructor
         */
        function PlatformMappingController() {
        }

        PlatformMappingController.prototype = {

            init: function () {
                console.log('已加载');
            }
        };

        return PlatformMappingController;
    })());
});