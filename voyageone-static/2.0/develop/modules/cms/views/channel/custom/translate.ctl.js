define([
    'cms'
], function (cms) {

    cms.controller('translateController', (function () {

        function TranslateCtl($routeParams, attrTranslateService,popups) {
            var self = this;
            self.nameEn = $routeParams.nameEn;
            self.popups = popups;
            self.attrTranslateService = attrTranslateService;
            self.searchInfo = {
                type: '',
                propName: '',
                propValue: ''
            };
            self.paging = {
                skip: 1, total: 0, limit:10,fetch: function () {
                    self.search();
                }
            };
        }

        TranslateCtl.prototype.init = function () {
            var self = this;

            if (self.nameEn) {
                var jsonStr = sessionStorage.getItem(self.nameEn);
                self.datasource = angular.fromJson(jsonStr);
            }

            self.search();

        };

        TranslateCtl.prototype.search = function () {
            var self = this, searchInfo = self.searchInfo,
                entity = self.datasource.entity;

            searchInfo.type = entity.type;

            self.attrTranslateService.init(_.extend(searchInfo,self.paging)).then(function (res) {
                self.attrValues = res.data.resultData;
                self.paging.total = res.data.total;
            });
        };

        TranslateCtl.prototype.openAddAttributeValue = function(){
            var self = this,
                popups = self.popups;

            popups.openAddAttrValue().then(function () {

            });
        };

        return TranslateCtl;

    })());

});