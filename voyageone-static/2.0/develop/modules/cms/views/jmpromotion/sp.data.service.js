define(['cms'], function (cms) {

    function SpDataService(jmPromotionService, jmPromotionDetailService, JmPromotionImagesService, $routeParams, $q, $filter) {
        var self = this;
        self.promotionId = parseInt($routeParams['promId']);
        self.jmPromotionId = parseInt($routeParams['jmpromId']);
        self.commonUpEntity = {
            promotionId: self.promotionId,
            jmPromotionId: self.jmPromotionId
        };
        self.$q = $q;
        self.jmPromotionService = jmPromotionService;
        self.jmPromotionDetailService = jmPromotionDetailService;
        self.JmPromotionImagesService = JmPromotionImagesService;
        self.dateFilter = $filter('date');
    }

    SpDataService.prototype.getPromotion = function getPromotion() {
        var self = this,
            jmPromotionService = self.jmPromotionService;

        return jmPromotionService.get(self.jmPromotionId).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.getPromotionModules = function getPromotionModules() {
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

    SpDataService.prototype.saveModules = function saveModules(modules) {
        var self = this,
            jmPromotionDetailService = self.jmPromotionDetailService,
            dateFilter = self.dateFilter,
            curJmPromotionId = self.jmPromotionId,
            jmPromotionObj = self.jmPromotionObj;

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

        var stsParam = { 'jmPromId': curJmPromotionId };
        stsParam.stepName = 'PromotionShelf';
        stsParam.stepStatus = 'Error';
        jmPromotionDetailService.setJmPromotionStepStatus(stsParam).then(function (resp) {
            jmPromotionDetailService.savePromotionTagModules(modules).then(function (resp) {
                stsParam.stepStatus = 'Success';
                jmPromotionDetailService.setJmPromotionStepStatus(stsParam).then(function (resp) {
                    jmPromotionObj.shelfStatus = 1;
                });
                return resp.data;
            });
        });
    };

    SpDataService.prototype.initPromotionImages = function initPromotionImages() {
        var self = this,
            defer = self.$q.defer(),
            JmPromotionImagesService = self.JmPromotionImagesService;

        JmPromotionImagesService.init(self.commonUpEntity).then(function (res) {
            defer.resolve(res);
        }, function (res) {
            defer.reject(res);
        });

        return defer.promise;
    };

    SpDataService.prototype.savePromotionImages = function savePromotionImages(upEntity) {
        var self = this,
            JmPromotionImagesService = self.JmPromotionImagesService,
            jmPromotionObj = self.jmPromotionObj;
        self.jmPromotionObj.imageStatus = 2;
        saveType = upEntity.saveType;

        return JmPromotionImagesService.save(_.extend(upEntity, self.commonUpEntity)).then(function (res) {
            if (saveType == 1 || saveType == 2) {
                jmPromotionObj.imageStatus = 1;
            }
        });
    };

    SpDataService.prototype.getPromotionImgTpl = function getPromotionImgTpl(upEntity) {
        var self = this,
            JmPromotionImagesService = self.JmPromotionImagesService;

        return JmPromotionImagesService.getImageTemplate(upEntity).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.getPromotionProducts = function getPromotionProducts(tagId) {
        var self = this,
            jmPromotionDetailService = self.jmPromotionDetailService;

        return jmPromotionDetailService.getPromotionProducts(tagId).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.saveProductSort = function saveProductSort(tagId, productList) {
        var self = this,
            jmPromotionDetailService = self.jmPromotionDetailService;

        return jmPromotionDetailService.saveProductSort({
            tagId: tagId,
            jmProductList: productList
        }).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.getJmTemplateUrls = function getJmTemplateUrls() {
        var self = this,
            jmPromotionDetailService = self.jmPromotionDetailService;

        return jmPromotionDetailService.getJmTemplateUrls().then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.getBayWindow = function getBayWindow() {
        var self = this,
            jmPromotionService = self.jmPromotionService;

        return jmPromotionService.getBayWindow(self.jmPromotionId).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.downloadSpecialImageZip = function downloadSpecialImageZip() {
        var self = this,
            JmPromotionImagesService = self.JmPromotionImagesService;

        return JmPromotionImagesService.downloadSpecialImageZip({
            promotionId: self.jmPromotionId
        }).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.downloadWaresImageZip = function downloadWaresImageZip() {
        var self = this,
            JmPromotionImagesService = self.JmPromotionImagesService;

        return JmPromotionImagesService.downloadWaresImageZip({
            promotionId: self.jmPromotionId
        }).then(function (resp) {
            return resp.data;
        });
    };

    SpDataService.prototype.saveBayWindow = function saveBayWindow(bayWindow) {
        var self = this,
            jmPromotionService = self.jmPromotionService,
        jmPromotionObj = self.jmPromotionObj;
        jmPromotionObj.bayWindowStatus = 2;

        return jmPromotionService.saveBayWindow(bayWindow).then(function (resp) {
            jmPromotionObj.bayWindowStatus = 1;
            return resp.data;
        });
    };

    cms.service('spDataService', SpDataService);
});