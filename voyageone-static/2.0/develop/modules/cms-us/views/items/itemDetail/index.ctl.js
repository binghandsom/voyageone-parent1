define([
    'cms'
], function (cms) {

    cms.controller('itemDetailController', class itemDetailController {
        constructor(popups, $routeParams, itemDetailService, alert,$location,notify) {
            this.popups = popups;
            this.itemDetailService = itemDetailService;
            this.alert = alert;
            this.$location = $location;
            this.notify = notify;

            this.id = $routeParams.id;
            if (!this.id) {
                this.alert("Feed not exists.");
                return;
            }

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
            self.itemDetailService.detail({id: self.id}).then((resp) => {
                if (resp.data && resp.data.feed) {
                    self.feed = resp.data.feed;
                    // 处理Feed数据
                    self.filterFeed();
                    self.brandList = resp.data.brandList;
                    self.productTypeList = resp.data.productTypeList;
                    self.sizeTypeList = resp.data.sizeTypeList;

                    // self.materialList = resp.data.materialList;
                    // self.originList = resp.data.originList;
                    // self.colorMap = resp.data.colorMap;
                } else {
                    let id = self.id;
                    let message = `Feed(id:${id}) not exists.`;
                    self.alert(message).then((res) => {
                        // self.$location.path("");
                    });
                }
            });
        }

        // 处理Feed数据
        filterFeed() {
            let self = this;
            let hasUrlkey = self.feed.attribute.urlkey && _.size(self.feed.attribute.urlkey) > 0;
            _.extend(self.feed, {hasUrlkey:hasUrlkey});
            if (!hasUrlkey) {
                // 计算urlKey
                self.generateUrlKey();
            }
            // 将sku->weightOrg转换成float
            angular.forEach(self.feed.skus, function (sku) {
                sku.weightOrg = parseFloat(sku.weightOrg);
            });
            // 处理Abstract和accessory
            if (self.feed.attribute.abstract && _.size(self.feed.attribute.abstract) > 0) {
                _.extend(self.feed, {abstract:self.feed.attribute.abstract[0]});
            }
            if (self.feed.attribute.accessory && _.size(self.feed.attribute.accessory) > 0) {
                _.extend(self.feed, {accessory:self.feed.attribute.accessory[0]});
            }
            // 处理orderlimitcount
            if (self.feed.attribute.orderlimitcount && _.size(self.feed.attribute.orderlimitcount) > 0) {
                _.extend(self.feed, {orderlimitcount:self.feed.attribute.orderlimitcount[0]});
            }
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
        setSkuProperty(sku,property) {
            let self = this;
            if (!sku) {
                angular.forEach(self.feed.skus, function (sku) {
                    sku[property] = self.setting[property];
                })
            }
            self.setSkuCNPrice();
        }
        // 同步计算中国相关价格
        setSkuCNPrice() {
            let self = this;
            let productType = self.feed.productType;
            angular.forEach(self.feed.skus, function (sku) {
                let priceClientMsrp = sku['priceClientMsrp'];
                let priceNet = sku['priceNet'];
                if (productType.toLowerCase() == "shoes") {
                    // ->中国建议售价 (msrp + msrp * 0.2 + 11) * 6.4
                    // ->中国指导售价 (price + msrp * 0.2 + 11) * 6.4
                    sku['priceMsrp'] = (priceClientMsrp + priceClientMsrp*0.2 + 11)*6.4;
                    sku['priceCurrent'] = (priceNet + priceClientMsrp*0.2 + 11)*6.4;
                } else if (productType.toLowerCase() == "gear") {
                    // ->中国建议售价 msrp * 11
                    // ->中国指导售价 msrp * 8
                    sku['priceMsrp'] = priceClientMsrp*11;
                    sku['priceCurrent'] = priceClientMsrp*8;
                } else {
                    // ->中国建议售价 msrp * 11
                    // ->中国指导售价 price * 11
                    sku['priceMsrp'] = priceClientMsrp*11;
                    sku['priceCurrent'] = priceNet*11;
                }
            })
        }

        // Save or Submit or Approve the Feed
        // flag:0 or 1; 0-Only Save,1-Submit/Approve to next status
        save(flag) {
            let self = this;

            // 如果是Approve,approve price是否打钩。价格是否为0或者500，如果是0或者500警告用户再次确认
            // if (self.feed.status === '') {
            //
            // }



            // 处理Abstract和accessory
            self.feed.attribute.abstract = [self.feed.abstract];
            self.feed.attribute.accessory = [self.feed.accessory];
            // 处理orderlimitcount
            self.feed.attribute.orderlimitcount = [self.feed.orderlimitcount];




            let parameter = {feed:self.feed, flag:flag};
            self.itemDetailService.update(parameter).then((res) => {
                if (res.data) {
                    self.notify.success("Operation succeeded.");
                    _.extend(self.feed, res.data);
                    // 处理Feed数据
                    self.filterFeed();
                }
            });
        }

        popUsCategory() {
            let self = this;
            self.popups.openUsCategory().then(context => {
                _.extend(self.feed, {category:context.catPath})
            });
        }

        initImage(num) {
            let self = this;
            let urlKey = "";
            if (self.feed.hasUrlkey && num > 0) {
                urlKey = self.feed.attribute.urlkey[0];
                if (!self.feed.image) {
                    self.feed.image = [];
                }
                let count = _.size(self.feed.image);
                let add = num - count;
                if (add > 0) {
                    for (let i=1; i<=add; i++) {
                        self.feed.image.push("http://image.sneakerhead.com/is/image/sneakerhead/" + urlKey + "-" + (count + i));
                    }
                } else {
                    self.feed.image.splice(add);
                }
            }
        }
        addImage() {
            let self = this;
            if (!self.feed.image) {
                self.feed.image = [];
            }
            self.feed.image.push("");
        }
        deleteImage(index) {
            let self = this;
            self.feed.image.splice(index, 1);
        }
        initBoxImage(num) {
            let self = this;
            let urlKey = "";
            if (self.feed.hasUrlkey && num > 0) {
                urlKey = self.feed.attribute.urlkey[0];
                if (!self.feed.boxImage) {
                    self.feed.boxImage = [];
                }
                let count = _.size(self.feed.boxImage);
                let add = num - count;
                if (add > 0) {
                    for (let i=1; i<=add; i++) {
                        self.feed.boxImage.push("http://image.sneakerhead.com/is/image/sneakerhead/" + urlKey + "-2-" + (count + i));
                    }
                } else {
                    self.feed.boxImage.splice(add);
                }
            }
        }
        addBoxImage() {
            let self = this;
            if (!self.feed.boxImage) {
                self.feed.boxImage = [];
            }
            self.feed.boxImage.push("");
        }
        deleteBoxImage(index) {
            let self = this;
            self.feed.boxImage.splice(index, 1);
        }

    });

});