define(['cms'], function (cms) {

    function SpDataService(jmPromotionService, jmPromotionDetailService, JmPromotionImagesService,$routeParams,$q,notify) {
        var self = this;
        self.promotionId = parseInt($routeParams['promId']);
        self.jmPromotionId = parseInt($routeParams['jmpromId']);
        self.commonUpEntity = {
            promotionId:self.promotionId,
            jmPromotionId:self.jmPromotionId
        };
        self.notify = notify;
        self.$q = $q;
        self.jmPromotionService = jmPromotionService;
        self.jmPromotionDetailService = jmPromotionDetailService;
        self.JmPromotionImagesService = JmPromotionImagesService;
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

    SpDataService.prototype.saveModules = function (modules) {
        var self = this,
            jmPromotionDetailService = self.jmPromotionDetailService;

        return jmPromotionDetailService.savePromotionTagModules(modules).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.initPromotionImages = function initPromotionImages(){
       var self = this,
           defer = self.$q.defer(),
           JmPromotionImagesService = self.JmPromotionImagesService;

        JmPromotionImagesService.init(self.commonUpEntity).then(function(res){
            defer.resolve(res);
        },function(res){
            defer.reject(res);
        });

        return defer.promise;
    };

    SpDataService.prototype.savePromotionImages = function savePromotionImages(upEntity){
      var self = this,
          notify = self.notify,
          JmPromotionImagesService = self.JmPromotionImagesService;

        JmPromotionImagesService.save(_.extend(upEntity,self.commonUpEntity)).then(function(){
            notify.success("SUCCESS");
        });
    };

    cms.service('spDataService', SpDataService);
});