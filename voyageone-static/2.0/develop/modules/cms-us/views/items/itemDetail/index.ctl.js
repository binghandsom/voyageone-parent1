define([
           'cms'
       ], function (cms) {

    cms.controller('itemDetailController', class itemDetailController {
        constructor(popups, $routeParams, itemDetailService, alert,$location) {
            this.code = $routeParams.code;
            if (!this.code) {
                console.log("不存在");
                return;
            }
            this.popups = popups;
            this.itemDetailService = itemDetailService;
            this.alert = alert;
            this.$location = $location;

            this.feed = {};
            this.brandList = [];
            this.productTypeList = [];
            this.sizeTypeList = [];
            this.materialList = [{value:"wood",name:"wood"}];
            this.originList = [{value:"CN",name:"CN"}];
            this.colorMap = [{value:"Red",name:"Red"}];
            this.setting = {
                weightOrg:"",
                weightOrgUnit:"",
                priceClientMsrp:"",
                priceNet:"",
                priceMsrp:"",
                priceCurrent:"",

                weightOrgUnits:['kg','lb']
            };
            this.init();
        }

        init() {
            let self = this;
            // 根据code加载Feed
            self.itemDetailService.detail({code: self.code}).then((resp) => {
                if (resp.data && resp.data.feed) {
                    self.feed = resp.data.feed;
                    // 将sku->weightOrg转换成int
                    angular.forEach(self.feed.skus, function (sku) {
                       sku.weightOrg = parseInt(sku.weightOrg);
                    });
                    self.brandList = resp.data.brandList;
                    self.productTypeList = resp.data.productTypeList;
                    self.sizeTypeList = resp.data.sizeTypeList;

                    // self.materialList = resp.data.materialList;
                    // self.originList = resp.data.originList;
                    // self.colorMap = resp.data.colorMap;
                } else {
                    let code = self.code;
                    let message = `Feed(Code:${code}) not exists.`;
                    self.alert(message).then((res) => {
                        self.$location.path("");
                    });
                }
            });

        }

        // 统一设置SKU属性
        setSkuProperty(property) {
            let self = this;
            angular.forEach(self.feed.skus, function (sku) {
                sku[property] = self.setting[property];
            })
        }

        // Save or Submit or Approve the Feed
        // flag:0 or 1; 0-Only Save,1-Submit/Approve to next status
        save(flag) {
            let self = this;
            console.log(self.feed)
        }

        popBatchApprove() {
            let self = this;

            self.popups.openBatchApprove();
        }

    });

});