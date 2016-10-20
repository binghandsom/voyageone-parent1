/**
 * @description 主品牌设置
 * @author piao
 * @version V2.8.0
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.controller('masterBrandApplicationController', (function () {

        function masterBrandApplicationController(masterBrandApplicationService, popups) {
            this.popups = popups;
            this.masterBrandApplicationService = masterBrandApplicationService;
            this.searchInfo = {
                status: 0,
                waitUpEntity: {
                    statusList: [0],
                    masterBrandEn: '',
                    channelId: '',
                    feedBrand: '',
                    cartBrandName: ''
                },
                alreadyUpEntity: {
                    statusList: [1],
                    masterBrandEn: ''
                }
            };
            this.waitCheckOption = {curr: 1, size: 10, fetch: this.search.bind(this)};
            this.alreadyCheckOption = {curr: 1, size: 10, fetch: this.search.bind(this)};
        }

        masterBrandApplicationController.prototype.init = function () {
            var self = this,
                masterBrandApplicationService = self.masterBrandApplicationService;

            /*            masterBrandApplicationService.init().then(function(res){
             console.log("res",res);
             });*/

        };

        masterBrandApplicationController.prototype.search = function () {
            var self = this,
                searchInfo = self.searchInfo,
                upEntity,
                masterBrandApplicationService = self.masterBrandApplicationService;

            if (searchInfo.status == 0)
                upEntity = _.extend(self.waitCheckOption, searchInfo.waitUpEntity);
            else
                upEntity = _.extend(self.alreadyCheckOption, searchInfo.alreadyUpEntity);

            masterBrandApplicationService.search(upEntity).then(function (res) {
                if (searchInfo.status == 0)
                    self.waitCheckOption.total = res.data.masterBrandsCount;
                else
                    self.alreadyCheckOption.total = res.data.masterBrandsCount;

                self.dataList = res.data.masterBrandList;
            });
        };


        masterBrandApplicationController.prototype.popMasterBrandCheck = function () {
            var self = this,
                popups = self.popups;

            popups.openMasterBrandCheck({}).then(function (context) {

            });
        };

        masterBrandApplicationController.prototype.popMasterBrandEdit = function () {
            var self = this,
                popups = self.popups;

            popups.openMasterBrandEdit({}).then(function (context) {

            });

        };


        return masterBrandApplicationController;

    })());

});