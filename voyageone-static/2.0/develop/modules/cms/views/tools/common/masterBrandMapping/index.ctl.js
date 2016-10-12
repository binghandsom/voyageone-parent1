/**
 * @decription   主品牌匹配
 * @author gjl
 * @version V2.8.0
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.controller('MasterBrandMappingController', (function () {

        function MasterBrandMappingController(masterBrandService, popups) {
            this.masterBrandService = masterBrandService;
            this.popups = popups;
            this.prodPageOption = {curr: 1, size: 10, fetch: this.search.bind(this)};
            this.feedBrand = '';
            this.statusList = [0, 2, 3];
        }

        MasterBrandMappingController.prototype.init = function () {
            var self = this;

            self.search(true);
        };

        MasterBrandMappingController.prototype.search = function (init) {
            var self = this,
                data = this.prodPageOption;

            if (!init)
                self.statusList = [];

            _.each(self.status, function (value, key) {
                if (value === true) {
                    self.statusList.push(+key)
                }
            });

            _.extend(data, {"statusList": self.statusList, "feedBrand": self.feedBrand});

            self.masterBrandService.search(data).then(function (res) {
                self.masterBrandList = res.data.masterBrandList;
                self.prodPageOption.total = res.data.masterBrandsCount;
            });
        };

        MasterBrandMappingController.prototype.popBrandMatching = function (item) {
            var self = this,
                popups = self.popups;

            if (item.masterFlag == null || item.masterFlag == 1) {
                console.log("待确认");
            }else if(item.masterFlag == 2){
                popups.openBrandMatching({}).then(function(){

                });
            }
        };

        MasterBrandMappingController.prototype.displayFlg = function (item) {

            switch (item) {
                case 0 :
                    return 'Master品牌申请中';
                case 1 :
                    return '已匹配';
                case 2:
                    return '待匹配（审核驳回）';
                case 3 :
                    return '未匹配';
                default :
                    return '未匹配';
            }
        };

        return MasterBrandMappingController;

    })())
});
