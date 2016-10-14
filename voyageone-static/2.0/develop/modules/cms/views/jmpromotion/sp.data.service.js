define(['cms'], function (cms) {

    function SpDataService(jmPromotionService, $routeParams) {
        this.promotionId = parseInt($routeParams['promId']);
        this.jmPromotionId = parseInt($routeParams['jmpromId']);
        this.jmPromotionService = jmPromotionService;
    }

    SpDataService.prototype.getPromotion = function () {
        var self = this,
            jmPromotionService = self.jmPromotionService;

        return jmPromotionService.get(self.jmPromotionId).then(function (resp) {
            return resp.data;
        });
    };

    cms.service('spDataService', SpDataService);
});