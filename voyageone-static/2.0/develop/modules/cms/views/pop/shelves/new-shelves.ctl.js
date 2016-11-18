define([
    'cms'
], function (cms) {

    function NewShelvesPopupController(context, $uibModalInstance, shelvesTemplateService, promotionService, shelvesService) {
        var self = this;

        self.$uibModalInstance = $uibModalInstance;
        self.context = context;
        self.shelvesTemplateService = shelvesTemplateService;
        self.promotionService = promotionService;
        self.shelvesService = shelvesService;

        self.getTemplate();

        angular.extend(self, context);

        self.usePromoPrice = !!context.promotionId;
    }

    NewShelvesPopupController.prototype = {
        getTemplate: function() {
            var self = this;
            var shelvesTemplateService = self.shelvesTemplateService;
            var context = self.context;

            return shelvesTemplateService.search({
                clientType: context.clientType,
                cartId: context.cartId
            }).then(function (resp) {
                var templates = resp.data.templates;
                self.layoutTemplates = templates.filter(function (t) {
                    return t.templateType === 0;
                });
                self.singleTemplates = templates.filter(function (t) {
                    return t.templateType === 1;
                });
            });
        },

        getPromotions: function() {
            var self = this;
            var promotionService = self.promotionService;
            var context = self.context;

            if (self.getPromotionsing)
                return;

            self.getPromotionsing = true;

            promotionService.getPromotionSimpleList({cartId: context.cartId}).then(function (resp) {
                self.promotionList = resp.data;

                self.getPromotionsing = false;
            });
        },

        changedUsePromoPrice: function() {
            var self = this;
            var usePromoPrice = self.usePromoPrice;

            if (!usePromoPrice) return;

            if (self.promotionList && self.promotionList.length) return;

            self.getPromotions();
        },

        ok: function () {
            var self = this;
            var context = self.context;
            var shelvesService = self.shelvesService;

            var shelvesModel = {
                shelvesName: self.shelvesName,
                cartId: context.cartId,
                clientType: context.clientType,
                layoutTemplateId: self.layoutTemplateId,
                singleTemplateId: self.singleTemplateId
            };

            if (self.usePromoPrice) {
                shelvesModel.promotionId = self.promotionId;
            }

            if (context.id) {
                shelvesModel.id = context.id;
                shelvesService.updateShelves(shelvesModel).then(function (resp) {
                    self.$uibModalInstance.close(resp.data);
                });
            } else {
                shelvesService.createShelves(shelvesModel).then(function (resp) {
                    self.$uibModalInstance.close(resp.data);
                });
            }
        },

        cancel: function () {
            this.$uibModalInstance.dismiss();
        }
    };

    cms.controller('NewShelvesPopupController', NewShelvesPopupController);
});