define(['cms'], function (cms) {

    function SpDataService(jmPromotionService, jmPromotionDetailService, $routeParams) {
        this.promotionId = parseInt($routeParams['promId']);
        this.jmPromotionId = parseInt($routeParams['jmpromId']);
        this.jmPromotionService = jmPromotionService;
        this.jmPromotionDetailService = jmPromotionDetailService;
    }

    SpDataService.prototype.getPromotion = function () {
        var self = this,
            jmPromotionService = self.jmPromotionService;

        return jmPromotionService.get(self.jmPromotionId).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.getPromotionModules = function () {
        var self = this,
            jmPromotionDetailService = self.jmPromotionDetailService;

        return jmPromotionDetailService.getPromotionTagModules(self.jmPromotionId).then(function (resp) {
            return resp.data;
        });
    };

    cms.service('spDataService', SpDataService);
});