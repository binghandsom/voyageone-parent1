define(['cms',
        './blackBrand.service.dev'],
    function (cms) {
        cms.controller('BlackBrandListController', (function () {

            function BlackBrandListController(blackBrandService) {
                var self = this;

                self.blackBrandService = blackBrandService;

                self.searchInfo = {
                    cartId: null,
                    categoryType: 3,
                    categoryId: null
                };
                self.paging = {
                    curr: 1, total: 0, fetch: function () {
                        self.search();
                    }
                };
            }

            BlackBrandListController.prototype.init = function () {
                var self = this;

                self.search();
            };

            BlackBrandListController.prototype.search = function(){
                var self = this,
                    searchInfo = self.searchInfo,
                    paging = self.paging,
                    blackBrandService = self.blackBrandService;

                blackBrandService.list().then(function (res) {
                    paging.total = res.data.total;
                    self.dataList =  res.data.list;
                });

            };

            return BlackBrandListController;

        })());
    });