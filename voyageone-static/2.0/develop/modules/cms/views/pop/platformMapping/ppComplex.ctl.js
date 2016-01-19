/**
 * complexMappingPopupController
 */

define([
    'cms',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms) {
    'use strict';
    return cms.controller('complexMappingPopupController', (function () {

        /**
         * Simple / Complex Mapping 弹出框的 Controller
         * @param {ComplexMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @constructor
         */
        function ComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.mainCategoryId = this.context.mainCategoryId;
            this.ppPlatformMappingService = ppPlatformMappingService;

            this.selected = {
                valueFrom: this.options.valueFrom.MASTER,
                value: null
            };
            this.mainCategoryPath = null;
            this.options.values = null;
        }

        ComplexMappingPopupController.prototype = {
            options: {
                valueFrom: {
                    'MASTER': {desc: 'MASTER（Product画面->商品详情属性）'},
                    'SKU': {desc: 'SKU（Product画面->SKU属性）'}
                }
            },
            init: function () {
                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                    this.loadValue();
                }.bind(this));
            },
            loadValue: function () {
                switch (this.selected.valueFrom) {
                    case this.options.valueFrom.MASTER:
                        this.ppPlatformMappingService.getMainCategoryProps(this.mainCategoryId).then(function (props) {
                            this.options.values = props;
                        }.bind(this));
                        break;
                    case this.options.valueFrom.SKU:
                        this.ppPlatformMappingService.getMainCategorySkuProps(this.mainCategoryId).then(function (props) {
                            this.options.values = props;
                        }.bind(this));
                        break;
                }
            },
            ok: function () {
                this.cancel();
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return ComplexMappingPopupController;

    })());
});