define(['cms'], function (cms) {

    function SpDataService(jmPromotionService, $routeParams) {
        this.promotionId = $routeParams['promId'];
        this.jmPromotionId = $routeParams['jmpromId'];
        this.jmPromotionService = jmPromotionService;
    }

    SpDataService.prototype.getPromotion = function () {
        var self = this,
            jmPromotionService = self.jmPromotionService;

        return jmPromotionService.getSomeData({

        }).then(function (resp) {
            return resp.data;
        });
    };

    cms.service('spDataService', SpDataService);
});