define(['cms'], function (cms) {

    function SpDataService(jmPromotionService, jmPromotionDetailService, JmPromotionImagesService,$routeParams,notify) {
        this.promotionId = parseInt($routeParams['promId']);
        this.jmPromotionId = parseInt($routeParams['jmpromId']);
        this.notify = notify;
        this.jmPromotionService = jmPromotionService;
        this.jmPromotionDetailService = jmPromotionDetailService;
        this.JmPromotionImagesService = JmPromotionImagesService;
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

    SpDataService.prototype.savePromotionImages = function savePromotionImages(upEntity){
      var self = this,
          notify = self.notify,
          JmPromotionImagesService = self.JmPromotionImagesService;

        JmPromotionImagesService.save(upEntity).then(function(){
            notify.success("SUCCESS");
        });
    };

    cms.service('spDataService', SpDataService);
});