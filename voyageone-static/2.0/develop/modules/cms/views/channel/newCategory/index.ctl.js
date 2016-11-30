define([
    'cms',
    './sortEnum',
    './service.dev'
], function (cms, sortEnum) {

    cms.controller("newCategoryController", (function () {

        function NewCategoryCtl($routeParams, productTopService) {
            var self = this;
            self.routeParams = angular.fromJson($routeParams.cartInfo);
            self.sortList = sortEnum.getSortByCd(self.routeParams.cartId);
            self.productTopService = productTopService;
            self.searchInfo = {};
            self.sort = {};
            self.codeStr = '';
            self.paging = {
                curr: 1, total: 0, size: 10, fetch: function () {
                    self.search();
                }
            };
        }

        NewCategoryCtl.prototype.init = function () {
            var self = this,
                productTopService = self.productTopService,
                routeParams = self.routeParams;

            self.getTopList();

            //brandList,sortColumnName,sortType
            productTopService.init({cartId: routeParams.cartId}).then(function (res) {
                self.brandList = res.brandList;

                self.sort = _.find(self.sortList, function (ele) {
                    return ele.sValue == res.sortColumnName;
                });

                if (self.sort)
                    self.sort.sortType = res.sortType;
            });


        };

        NewCategoryCtl.prototype.search = function () {
            var self = this,
                paging = self.paging,
                upEntity = angular.copy(self.searchInfo),
                productTopService = self.productTopService;

            productTopService.getPage(angular.extend(upEntity, {
                codeList: self.codeStr.split("\n")
            })).then(function (res) {
                self.pageList = res.data;
            });

            productTopService.getCount().then(function (res) {
                paging.total = res;
            });

        };

        NewCategoryCtl.prototype.sortSearch = function (sortColumnName, sortType) {
            var self = this,
                _sort,
                routeParams = self.routeParams;

            if (!sortColumnName || !sortType)
                return;

            _sort = _.find(self.sortList, function (ele) {
                return ele.sValue == sortColumnName.replace("✓", routeParams.cartId);
            });

            if (self.sort)
                _sort.sortType = sortType;

            self.sort = _sort;

            //调用搜索
        };

        NewCategoryCtl.prototype.getTopList = function () {
            var self = this,
                productTopService = self.productTopService;

            productTopService.getTopList({}).then(function (res) {
                self.topList = res.data;
            });
        };

        return NewCategoryCtl;

    })());

});