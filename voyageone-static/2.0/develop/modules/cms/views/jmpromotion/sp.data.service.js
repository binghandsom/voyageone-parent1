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
        self.dateFilter = $filter('date');
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
            return resp.data.map(function (item) {
                if (item.module.displayStartTime)
                    item.module.displayStartTime = new Date(item.module.displayStartTime);
                if (item.module.displayEndTime)
                    item.module.displayEndTime = new Date(item.module.displayEndTime);
                return item;
            });
        });
    };

    SpDataService.prototype.saveModules = function (modules) {
        var self = this,
            jmPromotionDetailService = self.jmPromotionDetailService,
            dateFilter = self.dateFilter;

        // like deep copy
        modules = modules.map(function (item) {
            var clone = {
                tag: angular.copy(item.tag),
                module: angular.copy(item.module)
            };

            if (clone.module.displayStartTime)
                clone.module.displayStartTime = dateFilter(clone.module.displayStartTime, 'yyyy-MM-dd HH:mm:ss');
            if (clone.module.displayEndTime)
                clone.module.displayEndTime = dateFilter(clone.module.displayEndTime, 'yyyy-MM-dd HH:mm:ss');

            return clone;
        });

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