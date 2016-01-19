/**
 * complexMappingPopupController
 */

define([
    'cms',
    'modules/cms/models/ComplexMappingBean',
    'modules/cms/enums/FieldTypes',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, ComplexMappingBean, FieldTypes) {
    'use strict';

    return cms.controller('complexMappingPopupController', (function () {

        /**
         * Complex Mapping 弹出框的 Controller
         * @param {ComplexMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @param alert
         * @constructor
         */
        function ComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, alert) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.mainCategoryId = this.context.mainCategoryId;
            this.ppPlatformMappingService = ppPlatformMappingService;
            this.alert = alert;

            this.mainCategoryPath = null;
            this.selected = {
                value: null
            };
            this.options = {
                values: null
            };
        }

        ComplexMappingPopupController.prototype = {
            init: function () {

                if (this.context.property.type !== FieldTypes.complex) {
                    this.alert('当前属性不是 Complex 属性').result.then(function() {
                        this.cancel();
                    }.bind(this));
                    return;
                }

                this.ppPlatformMappingService.getMainCategoryPath(this.mainCategoryId).then(function (path) {
                    this.mainCategoryPath = path;
                    this.loadValue();
                }.bind(this));
            },
            loadValue: function () {
                this.ppPlatformMappingService.getMainCategoryPropsWithSku(this.mainCategoryId).then(function (props) {
                    this.options.values = props;
                    this.selected.value = props[0];
                }.bind(this));
            },
            ok: function () {
                var complexMapping = new ComplexMappingBean(this.context.property.id, this.selected.value.id);
                this.$uibModalInstance.close(complexMapping);
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return ComplexMappingPopupController;

    })());
});