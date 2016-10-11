/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('MasterBrandMappingController', (function () {
        function MasterBrandMappingController(masterBrandService) {
            this.masterBrandService = masterBrandService;
            this.prodPageOption = {curr: 1, size: 10, fetch: this.search.bind(this)};
            this.selectedBrand = '';
            this.statusList = [0, 2, 3];
        }

        MasterBrandMappingController.prototype = {
            /**
             * 数据初始化
             */
            init: function () {
                var self = this;
                self.search(true);
            },
            /**
             * 检索查询
             */
            search: function (init) {
                var self = this;
                var data = this.prodPageOption;
                if (!init) {
                    self.statusList = [];
                }
                _.each(self.status, function (value, key) {
                    if (value === true) {
                        self.statusList.push(+key)
                    }
                });
                _.extend(data, {"statusList": self.statusList});
                _.extend(data, {"selectedBrand": self.selectedBrand});
                self.masterBrandService.init(data).then(function (res) {
                    self.masterBrandList = res.data.masterBrandList;
                    self.prodPageOption.total = res.data.masterBrandsCount;
                });
            }
        };
        return MasterBrandMappingController;
    })())
});