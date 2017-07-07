define([
    'cms'
], function (cms) {

    cms.controller('itemDetailController', class itemDetailController {
        constructor(popups, $routeParams, itemDetailService, alert,$location,notify) {
            this.code = $routeParams.code;
            if (!this.code) {
                console.log("不存在");
                return;
            }
            this.popups = popups;
            this.itemDetailService = itemDetailService;
            this.alert = alert;
            this.$location = $location;
            this.notify = notify;

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

                    let hasUrlkey = self.feed.attribute.urlkey && _.size(self.feed.attribute.urlkey) > 0;
                    _.extend(self.feed, {hasUrlkey:hasUrlkey});
                    if (!hasUrlkey) {
                        // 计算urlKey
                        self.generateUrlKey();
                    }
                    // 将sku->weightOrg转换成int
                    angular.forEach(self.feed.skus, function (sku) {
                       sku.weightOrg = parseInt(sku.weightOrg);
                    });

                    // 处理approvePricing
                    let approvePricingFlag = self.feed.attribute.urlkey && _.size(self.feed.attribute.urlkey) > 0;



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

        // 生成UrlKey
        generateUrlKey() {
            let self = this;
            if (!self.feed.hasUrlkey && self.feed.name) {
                let urlkey = self.feed.name + "-" + self.feed.code;
                urlkey = urlkey.replace(/[^a-zA-Z0-9]+/g, " ");
                urlkey = urlkey.replace(/\s+/g, "-");
                self.feed.attribute.urlkey = [urlkey.toLowerCase()];
            }
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
            let parameter = {feed:self.feed, flag:flag};
            console.log(self.feed);
            self.itemDetailService.update(parameter).then((res) => {
                if (res.data) {
                    self.notify.success("Operation succeeded.");
                }
            });
        }

        popUsCategory() {
            this.popups.openUsCategory().then(context => {
                console.log('context', context);
            });
        }

    });

});