/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('MasterBrandMappingController', (function () {
        function MasterBrandMappingController(masterBrandService,popups) {
            this.masterBrandService = masterBrandService;
            this.popups = popups;
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
                var self = this,
                    data = this.prodPageOption;
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
            },
            displayFlg: function (item) {
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
            },
            ngClick: function(list){
                var self = this;
                if(list.masterFlag==null || list.masterFlag==1){
                    self.popups.openFeedToMasterBrand()
                }
            }
        };
        return MasterBrandMappingController;
    })())
});