/**
 * otherPlatformPopupController
 */

define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, Carts) {
    /**
     * @ngdoc object
     * @name otherPlatformPopupController
     * @type {OtherPlatformPopupController}
     */
    return cms.controller('otherPlatformPopupController', (function () {

        function OtherPlatformPopupController(platformMappingService, context, $uibModalInstance) {

            this.platformMappingService = platformMappingService;
            this.$modal = $uibModalInstance;
            this.context = context;
            this.carts = Carts;

            /**
             *
             * @type {object[]}
             */
            this.mappingPaths = null;
        }

        OtherPlatformPopupController.prototype = {

            init: function () {

                this.platformMappingService.getOtherMappingPath({

                    mainCategoryId: this.context.mainCategoryId

                }).then(function (res) {

                    this.mappingPaths = res.data;
                }.bind(this));
            },
            cancel: function () {

                this.$modal.dismiss();
            }
        };

        return OtherPlatformPopupController;

    })());
});