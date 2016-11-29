define([
    'cms',
    './service.dev'
], function (cms) {

    cms.controller("newCategoryController", (function () {

        function NewCategoryCtl($routeParams,productTopService) {
            var self = this;
            self.routeParams = angular.fromJson($routeParams.cartInfo);
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

            //brandList,sortColumnName,sortType
            productTopService.init({cartId:routeParams.cartId}).then(function(res){
                self.brandList = res.brandList;
            });


        };

        NewCategoryCtl.prototype.search = function(){
            var self = this,
                paging = self.paging,
                upEntity = angular.copy(self.searchInfo),
                productTopService = self.productTopService;

            productTopService.getPage(angular.extend(upEntity,{
                codeList:self.codeStr.split("\n")
            })).then(function(res){
                self.pageList = res.data;
            });

            productTopService.getCount().then(function(res){
                paging.total = res;
            });

        };

        return NewCategoryCtl;

    })());

});