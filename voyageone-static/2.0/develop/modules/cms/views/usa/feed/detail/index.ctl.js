/**
 * @description feed detail
 */
define([
    'cms'
], function (cms) {

    cms.controller('feedDetailController', class FeedDetailController {
        constructor(popups, $routeParams, itemDetailService, alert, $location, notify, confirm, $sessionStorage) {
            this.popups = popups;
            this.itemDetailService = itemDetailService;
            this.alert = alert;
            this.$location = $location;
            this.notify = notify;
            this.confirm = confirm;
            this.$sessionStorage = $sessionStorage;
            this.auth = this.$sessionStorage.auth.auth;

            this.id = $routeParams.id;
            if (!this.id) {
                this.alert("Feed not exists.");
                return;
            }
            // CmsBtFeedInfoModel
            this.feed = {};
            // 共通属性
            this.brandList = [];
            this.productTypeList = [];
            this.sizeTypeList = [];
            this.materialList = [];
            this.originList = [];
            this.colorMap = [];
            this.platforms = [];
            this.platformsObj = {};
            // 设置
            this.setting = {
                weightOrg: "",
                weightOrgUnit: "",
                priceClientMsrp: "",
                priceNet: "",
                priceMsrp: "",
                priceCurrent: "",

                weightOrgUnits: ['lb', 'kg'],

                // 平台价格
                platformPrice: {}
            };
            this.topFeedList = null; // 同Model查询结果
            this.imageUrl = "http://image.sneakerhead.com/is/image/sneakerhead/";
            this.init();
        }

        init() {
            let self = this;
            // 根据code加载Feed
            self.itemDetailService.detail({id: self.id}).then((resp) => {
                if (resp.data && resp.data.feed) {
                    self.brandList = resp.data.brandList;
                    self.productTypeList = resp.data.productTypeList;
                    self.sizeTypeList = resp.data.sizeTypeList;
                    self.materialList = resp.data.materialList;
                    self.originList = resp.data.originList;
                    self.colorMap = resp.data.colorMap;

                    // 平台
                    self.platforms = resp.data.platforms;

                    // FeedModel
                    self.feed = resp.data.feed;
                    // 记录原始Feed数据中是否有urlkey
                    self.hasUrlkey();
                    // 处理Feed数据
                    self.filterFeed();

                    if (!self.feed.usPlatformPrices) {
                        self.feed.usPlatformPrices = [];
                    }
                    if (!self.feed.platformPrices) {
                        self.feed.platformPrices = [];
                    }
                    // Platform Price
                    _.each(self.platforms, platform => {
                        let cartId = parseInt(platform.value);
                        self.platformsObj[cartId] = cartId < 20 ? platform.name : platform.add_name2;
                        let matchOne;
                        if (cartId < 20) {
                            matchOne = _.find(self.feed.usPlatformPrices, priceObj => {
                                return cartId === priceObj.cartId;
                            });
                            if (!matchOne) {
                                let platformPrice = {cartId:cartId,isSale:1};
                                self.feed.usPlatformPrices.push(platformPrice);
                            } else {
                                _.extend(matchOne, {cartName:platform.name});
                            }
                        } else if (cartId > 20) {
                            matchOne = _.find(self.feed.platformPrices, priceObj => {
                                return cartId === priceObj.cartId;
                            });
                            if (!matchOne) {
                                let platformPrice = {cartId:cartId,isSale:1};
                                self.feed.platformPrices.push(platformPrice);
                            } else {
                                _.extend(matchOne, {cartName:platform.name});
                            }
                        }
                    });

                    // 如果有中国平台中国价格为null, 则触发计算价格
                    if (self.feed.feedAuth > 1) {
                        let noCNPriceOne = _.find(self.feed.platformPrices, platformPrice => {
                            return platformPrice.priceMsrp == null || platformPrice.priceCurrent == null;
                        });
                        if (noCNPriceOne) {
                            self.calculatePrice();
                        }
                    }
                } else {
                    let id = self.id;
                    let message = `Feed(id:${id}) not exists.`;
                    self.alert(message);
                }
            });
        }

        // 判断Feed是否已有urlkey
        hasUrlkey() {
            let self = this;
            let hasUrlkey = !!self.feed.attribute.urlkey && _.size(self.feed.attribute.urlkey) > 0;
            _.extend(self.feed, {hasUrlkey:hasUrlkey});
        }

        // 处理Feed数据
        filterFeed() {
            let self = this;

            // 用户Feed操作权限和Feed状态对比
            let feedStatus = self.feed.status;
            let feedAuth = 0;
            if (feedStatus == "Ready") {
                feedAuth = 3;
            } else if (feedStatus == "Pending") {
                feedAuth = 2;
            } else if (feedStatus == "New") {
                feedAuth = 1;
            } else {
                feedAuth = 4;
            }
            _.extend(self.feed, {feedAuth:feedAuth});

            if (!self.feed.hasUrlkey) {
                self.generateUrlKey();
            } else {
                _.extend(self.feed, {urlkey:self.feed.attribute.urlkey[0]});
            }
            // 处理Abstract和accessory
            if (!!self.feed.attribute.abstract && _.size(self.feed.attribute.abstract) > 0) {
                _.extend(self.feed, {abstract: self.feed.attribute.abstract[0]});
            }
            if (!!self.feed.attribute.accessory && _.size(self.feed.attribute.accessory) > 0) {
                _.extend(self.feed, {accessory: self.feed.attribute.accessory[0]});
            }
            // 处理orderlimitcount
            if (!!self.feed.attribute.orderlimitcount && _.size(self.feed.attribute.orderlimitcount) > 0) {
                _.extend(self.feed, {orderlimitcount: parseInt(self.feed.attribute.orderlimitcount[0])});
            }
            // 处理colorMap
            if (!!self.feed.attribute.colorMap && _.size(self.feed.attribute.colorMap) > 0) {
                _.extend(self.feed, {colorMap: self.feed.attribute.colorMap[0]});
            }
            // 处理attribute.categoriesTree
            if (!!self.feed.attribute.categoriesTree && _.size(self.feed.attribute.categoriesTree) > 0) {
                _.extend(self.feed, {categoriesTree: eval(self.feed.attribute.categoriesTree[0])});
            }
            // 处理amazonBrowseTree
            if (!!self.feed.attribute.amazonBrowseTree && _.size(self.feed.attribute.amazonBrowseTree) > 0) {
                _.extend(self.feed, {amazonBrowseTree: self.feed.attribute.amazonBrowseTree[0]});
            }
            // 处理googleCategory、googleDepartment、priceGrabberCategory
            if (!!self.feed.attribute.googleCategory && _.size(self.feed.attribute.googleCategory) > 0) {
                _.extend(self.feed, {googleCategory: self.feed.attribute.googleCategory[0]});
            }
            if (!!self.feed.attribute.googleDepartment && _.size(self.feed.attribute.googleDepartment) > 0) {
                _.extend(self.feed, {googleDepartment: self.feed.attribute.googleDepartment[0]});
            }
            if (!!self.feed.attribute.priceGrabberCategory && _.size(self.feed.attribute.priceGrabberCategory) > 0) {
                _.extend(self.feed, {priceGrabberCategory: self.feed.attribute.priceGrabberCategory[0]});
            }
            // 处理phoneOrderOnly
            if (!!self.feed.attribute.phoneOrderOnly && _.size(self.feed.attribute.phoneOrderOnly) > 0) {
                _.extend(self.feed, {phoneOrderOnly: self.feed.attribute.phoneOrderOnly[0]});
            }
            // 处理seoTitle、seoDescription、seoKeywords
            if (!!self.feed.attribute.seoTitle && _.size(self.feed.attribute.seoTitle) > 0) {
                _.extend(self.feed, {seoTitle: self.feed.attribute.seoTitle[0]});
            }
            if (!!self.feed.attribute.seoDescription && _.size(self.feed.attribute.seoDescription) > 0) {
                _.extend(self.feed, {seoDescription: self.feed.attribute.seoDescription[0]});
            }
            if (!!self.feed.attribute.seoKeywords && _.size(self.feed.attribute.seoKeywords) > 0) {
                _.extend(self.feed, {seoKeywords: self.feed.attribute.seoKeywords[0]});
            }

            // 处理特殊属性
            if (!!self.feed.attribute.freeShipping && _.size(self.feed.attribute.freeShipping) > 0) {
                _.extend(self.feed, {freeShipping: self.feed.attribute.freeShipping[0]});
            } else {
                _.extend(self.feed, {freeShipping: "1"});
            }
            if (!!self.feed.attribute.rewardEligible && _.size(self.feed.attribute.rewardEligible) > 0) {
                _.extend(self.feed, {rewardEligible: self.feed.attribute.rewardEligible[0]});
            } else {
                _.extend(self.feed, {rewardEligible: "1"});
            }
            if (!!self.feed.attribute.discountEligible && _.size(self.feed.attribute.discountEligible) > 0) {
                _.extend(self.feed, {discountEligible: self.feed.attribute.discountEligible[0]});
            } else {
                _.extend(self.feed, {discountEligible: "1"});
            }
            if (!!self.feed.attribute.sneakerheadPlus && _.size(self.feed.attribute.sneakerheadPlus) > 0) {
                _.extend(self.feed, {sneakerheadPlus: self.feed.attribute.sneakerheadPlus[0]});
            } else {
                _.extend(self.feed, {sneakerheadPlus: "1"});
            }
            if (!!self.feed.attribute.newArrival && _.size(self.feed.attribute.newArrival) > 0) {
                _.extend(self.feed, {newArrival: self.feed.attribute.newArrival[0]});
            } else {
                _.extend(self.feed, {newArrival: "1"});
            }
            if (!!self.feed.attribute.taxable && _.size(self.feed.attribute.taxable) > 0) {
                _.extend(self.feed, {taxable: self.feed.attribute.taxable[0]});
            } else {
                _.extend(self.feed, {taxable: "1"});
            }

            // 处理Item和Box Image
            if (_.size(self.feed.image) > 0) {
                self.currentFeedImage = self.feed.image[0];
                _.extend(self.feed, {imageNum: _.size(self.feed.image)});
            }

            if (self.feed.attribute.boximages && _.size(self.feed.attribute.boximages) > 0) {
                self.currentBoxImage = self.feed.attribute.boximages[0];
                _.extend(self.feed, {boxImageNum: _.size(self.feed.attribute.boximages)});
            }

            // Feed.weight=0.5lb, 如果是这样的数据将其扯开为weight=0.5, weightUnit=lb
            let weight = self.feed.weight;
            if (weight && isNaN(weight)) {
                let weightUnit = _.find(self.setting.weightOrgUnits, weightUnit => {
                    return weight.indexOf(weightUnit) > 0;
                });
                if (weightUnit) {
                    _.extend(self.feed, {weight:parseFloat(weight.replace(weightUnit, "")), weightUnit:weightUnit})
                }
            } else {
                self.feed.weight = !weight ? null : parseFloat(weight);
            }
        }

        // 生成UrlKey
        generateUrlKey() {
            let self = this;
            if (!self.feed.hasUrlkey) {
                if (self.feed.name) {
                    let urlkey = self.feed.name + "-" + self.feed.code;
                    urlkey = urlkey.replace(/[^a-zA-Z0-9]+/g, " ");
                    urlkey = urlkey.replace(/\s+/g, "-");
                    urlkey = urlkey.toLowerCase();
                    self.feed.attribute.urlkey = [urlkey];
                    _.extend(self.feed, {urlkey: urlkey});
                } else {
                    self.feed.attribute.urlkey = null;
                    _.extend(self.feed, {urlkey: null});
                }
            }
        }

        // Set Platform Price
        setPlatformPrice(property, value) {
            let self = this;
            if (property && value && !isNaN(value)) {
                _.each(self.feed.usPlatformPrices, platformPrice => {
                    platformPrice[property] = value;
                });
            }
        }

        // 计算中国价格
        calculatePrice() {
            let self = this;
            self.itemDetailService.setPrice({feed: self.feed}).then((res) => {
                if (res.data) {
                    self.feed.skus = res.data.skus;

                    var priceScope = {
                        priceClientRetailMin: res.data.priceClientRetailMin,
                        priceClientMsrpMin: res.data.priceClientMsrpMin,
                        priceClientRetailMax: res.data.priceClientRetailMax,
                        priceClientMsrpMax: res.data.priceClientMsrpMax,
                        usPlatformPrices: res.data.usPlatformPrices,
                        platformPrices: res.data.platformPrices
                    };
                    _.extend(self.feed, priceScope);
                }
            });
        }

        save(flag) {
            let self = this;

            // 如果是Approve,approve price是否打钩。价格是否为0或者500，如果是0或者500警告用户再次确认
            if (self.feed.status == 'Ready' && flag == '1') {
                if (self.feed.approvePricing != '1') {
                    self.alert("Please check 'Approve Pricing'.");
                    return;
                }
                // Msrp or price O时禁止Approve
                let checkPlatforms = _.filter(self.feed.usPlatformPrices, platformPrice => {
                    return platformPrice.priceClientMsrp == 0 || platformPrice.priceClientRetail == 0;
                });
                let platforms = [];
                angular.forEach(checkPlatforms, function (platform) {
                    platforms.push(self.platformsObj[platform.cartId]);
                });
                if (_.size(platforms) > 0) {
                    let message = `Platform[${platforms}] Msrp($) or price($) is 0, feed can't be approved.`;
                    self.alert(message);
                    return;
                }
                checkPlatforms = _.filter(self.feed.usPlatformPrices, function (platformPrice) {
                    return platformPrice.priceClientMsrp == 500 || platformPrice.priceClientRetail == 500;
                });
                if (!checkPlatforms || _.size(checkPlatforms) == 0) {
                    // let ctx = {
                    //     updateModel: true,
                    //     codeList: [self.feed.code]
                    // };
                    // self.popups.openBatchApprove(ctx).then((res) => {
                    //     if (res.success) {
                    //         _.extend(self.feed, {approveInfo: res.approveInfo});
                    //         self.saveFeed(flag);
                    //     }
                    // });
                    self.saveFeed(flag);
                } else {
                    platforms = [];
                    angular.forEach(checkPlatforms, function (platform) {
                        platforms.push(self.platformsObj[platform.cartId]);
                    });
                    let message = `Platform[${platforms}] Msrp($) or price($) is 500, continue to approve?`;
                    self.confirm(message).then((confirmed) => {
                        /*let ctx = {
                            updateModel: true,
                            codeList: [self.feed.code]
                        };
                        self.popups.openBatchApprove(ctx).then((res) => {
                            if (res.success) {
                                _.extend(self.feed, {approveInfo: res.approveInfo});
                                self.saveFeed(flag);
                            }
                        });*/
                        self.saveFeed(flag);
                    }, () => {

                    })
                }
            } else {
                self.saveFeed(flag);
            }
        }

        saveFeed(flag) {
            let self = this;
            // 处理Abstract和accessory
            self.feed.attribute.abstract = [self.feed.abstract];
            self.feed.attribute.accessory = [self.feed.accessory];
            // 处理orderlimitcount
            self.feed.attribute.orderlimitcount = [self.feed.orderlimitcount];
            // 处理colorMap
            self.feed.attribute.colorMap = [self.feed.colorMap];
            // 处理amazonBrowseTree
            self.feed.attribute.amazonBrowseTree = [self.feed.amazonBrowseTree];
            // 处理attribute.categoriesTree
            self.feed.attribute.categoriesTree = [JSON.stringify(self.feed.categoriesTree)];

            // 处理googleCategory、googleDepartment、priceGrabberCategory
            self.feed.attribute.googleCategory = [self.feed.googleCategory];
            self.feed.attribute.googleDepartment = [self.feed.googleDepartment];
            self.feed.attribute.priceGrabberCategory = [self.feed.priceGrabberCategory];

            // 处理phoneOrderOnly
            self.feed.attribute.phoneOrderOnly = [self.feed.phoneOrderOnly];
            // 处理seoTitle、seoDescription、seoKeywords
            self.feed.attribute.seoTitle = [self.feed.seoTitle];
            self.feed.attribute.seoDescription = [self.feed.seoDescription];
            self.feed.attribute.seoKeywords = [self.feed.seoKeywords];

            // 处理特殊属性
            self.feed.attribute.freeShipping = [self.feed.freeShipping];
            self.feed.attribute.rewardEligible = [self.feed.rewardEligible];
            self.feed.attribute.discountEligible = [self.feed.discountEligible];
            self.feed.attribute.sneakerheadPlus = [self.feed.sneakerheadPlus];
            self.feed.attribute.newArrival = [self.feed.newArrival];
            self.feed.attribute.taxable = [self.feed.taxable];

            let parameter = {feed: self.feed, flag: flag};

            self.itemDetailService.update(parameter).then((res) => {
                if (res.data) {
                    self.notify.success("Operation succeeded.");
                    _.extend(self.feed, res.data);
                    // 如果保存时有urlKey,那之后对应的input就disabled了
                    self.hasUrlkey();
                    // 处理Feed数据
                    self.filterFeed();
                }
            });
        }

        /**
         * @description 展开店铺内分类
         * @param option{cartId,muiti,from}
         */
        popUsCategory(option, attr) {
            let self = this;

            self.popups.openUsCategory(option).then(context => {
                if(option.muiti){
                    let categories = _.pluck(context, "catPath");
                    _.extend(self.feed.attribute, {categories:categories});
                    _.extend(self.feed, {categoriesTree:context});
                }else{
                    let category = context.catPath;
                    let categoryCatId = context.catId;
                    let seoTitle = "";
                    let seoKeywords = "";
                    let seoDescription = "";
                    let amazonBrowseTree = "";
                    let googleCategory = "";
                    let googleDepartment = "";
                    let priceGrabberCategory = "";
                    if (!!context.mapping) {
                        seoTitle = context.mapping.seoTitle;
                        seoKeywords = context.mapping.seoKeywords;
                        seoDescription = context.mapping.seoDescription;
                        amazonBrowseTree = context.mapping.amazon;
                        googleCategory = context.mapping.googleCategory;
                        googleDepartment = context.mapping.googleDepartment;
                        priceGrabberCategory = context.mapping.priceGrabber;
                    }
                    let attrs = {
                        category:category,
                        categoryCatId:categoryCatId,
                        seoTitle:seoTitle,
                        seoKeywords:seoKeywords,
                        seoDescription:seoDescription,
                        amazonBrowseTree:amazonBrowseTree,
                        googleCategory:googleCategory,
                        googleDepartment:googleDepartment,
                        priceGrabberCategory:priceGrabberCategory
                    };
                    _.extend(self.feed, attrs);
                }

            });
        }

        initImage(num) {
            let self = this;
            self.confirm("Make sure of setting the image count to <strong style='color:red'> " + num + "</strong>").then(confirmed => {
                if (num <= 0) {
                    self.feed.image = [];
                    self.feed.imageNum = 0;
                    self.currentFeedImage = "";
                } else {
                    if (!self.feed.image) {
                        self.feed.image = [];
                    }
                    let count = _.size(self.feed.image);
                    let add = num - count;
                    if (add != 0) {
                        if (add > 0) {
                            if (self.feed.urlkey) {
                                for (let i = 1; i <= add; i++) {
                                    self.feed.image.push(self.imageUrl + self.feed.urlkey + "-" + (count + i));
                                }
                            } else {
                                self.alert("No urlkey!");
                            }
                        } else {
                            self.feed.image.splice(add);
                        }

                        if (self.currentFeedImage === "") {
                            self.currentFeedImage = self.feed.image[0];
                        }
                    }
                }
            });
        }

        addImage() {
            let self = this;
            if (!self.feed.image) {
                self.feed.image = [];
            }
            self.feed.image.push("");
            self.feed.imageNum = _.size(self.feed.image);

            if (self.currentFeedImage === "") {
                self.currentFeedImage = self.feed.image[0];
            }
        }

        deleteImage(index) {
            let self = this;
            self.feed.image.splice(index, 1);
            let imageNum = self.feed.imageNum - 1;
            self.feed.imageNum = imageNum;
            if (imageNum == 0) {
                self.currentFeedImage = "";
            } else {
                self.currentFeedImage = self.feed.image[0];
            }
        }

        initBoxImage(num) {
            let self = this;
            self.confirm("Make sure of setting the image count to <strong style='color:red'> " + num + "</strong>").then(confirmed => {
                if (!num || num <= 0) {
                    self.feed.attribute.boximages = [];
                    self.feed.boxImageNum = 0;
                    self.currentBoxImage = "";
                } else {
                    if (!self.feed.attribute.boximages) {
                        self.feed.attribute.boximages = [];
                    }
                    let count = _.size(self.feed.attribute.boximages);
                    let add = num - count;
                    if (add != 0) {
                        if (add > 0) {
                            if (self.feed.urlkey) {
                                for (let i = 1; i <= add; i++) {
                                    self.feed.attribute.boximages.push(self.imageUrl + self.feed.urlkey + "-2" + (count + i));
                                }
                            } else {
                                self.alert("No urlkey!");
                            }
                        } else {
                            self.feed.attribute.boximages.splice(add);
                        }

                        if (self.currentBoxImage === "") {
                            self.currentBoxImage = self.feed.attribute.boximages[0];
                        }
                    }
                }
            });
        }

        addBoxImage() {
            let self = this;
            if (!self.feed.attribute.boximages) {
                self.feed.attribute.boximages = [];
            }
            self.feed.attribute.boximages.push("");
            self.feed.boxImageNum = _.size(self.feed.attribute.boximages);
            if (self.currentBoxImage === "") {
                self.currentBoxImage = self.feed.attribute.boximages[0];
            }
        }

        deleteBoxImage(index) {
            let self = this;
            self.feed.attribute.boximages.splice(index, 1);
            let boxImageNum = self.feed.boxImageNum - 1;
            self.feed.boxImageNum = boxImageNum;
            if (boxImageNum == 0) {
                self.currentBoxImage = "";
            } else {
                self.currentBoxImage = self.feed.attribute.boximages[0];
            }
        }

        // 同Model
        showTopModel(top) {
            let self = this;

            self.isShowModal = !self.isShowModal;
            self.getTopModel(top);

        }
        getTopModel(top) {
            let self = this;
            if (self.feed.model) {
                self.itemDetailService.getTopModel({
                   code: self.feed.code,
                   model: self.feed.model,
                   top: top
               }).then((res) => {
                    if (res.data) {
                        self.topFeedList = res.data;
                        // 如果从Product查询的Code属性,那图片只是图片名称,不是完整链接
                        angular.forEach(self.topFeedList, feed => {
                            // _id为空则说明信息从Product查询而来
                            if (!feed._id) {
                                let newImage = [];
                                _.each(feed.image, image => {
                                    image = self.imageUrl + image;
                                    newImage.push(image);
                                });
                                feed.image = newImage;
                            }
                        });
                    }
                })
            }
        }

        // Copy其他code部分属性
        copyAttr(feed) {
            let self = this;
            // feed.xx属性复制
            let attribute = {
                brand: feed.brand,
                productType: feed.productType,
                sizeType: feed.sizeType,
                material: feed.material,
                origin: feed.origin,
                usageEn: feed.usageEn,
                shortDescription: feed.shortDescription,
                longDescription: feed.longDescription,
                category: feed.category

            };
            _.extend(self.feed, attribute);
            // feed.attribute.xx属性复制
            attribute = {
                amazonBrowseTree: feed.attribute.amazonBrowseTree,
                abstract: feed.attribute.abstract,
                accessory: feed.attribute.accessory,
                orderlimitcount: feed.attribute.orderlimitcount,
                amazonBrowseTree:feed.attribute.amazonBrowseTree,
                googleCategory:feed.attribute.googleCategory,
                googleDepartment:feed.attribute.googleDepartment,
                priceGrabberCategory:feed.attribute.priceGrabberCategory,
                categories:feed.attribute.categories,
                categoriesTree:feed.attribute.categoriesTree
            };
            _.extend(self.feed.attribute, attribute);
            this.filterFeed();
        }

        /**
         * @description 弹出亚马逊类目  cartId：5
         */
        popCategory(option, attrName) {
            let self = this;

            self.popups.openAmazonCategory(option).then(res => {
                self.feed[attrName] = res.catPath;
            });
        }

        goDetail(url) {
            if (!url)
                return;

            window.open(url);
        }

        changeImages(index, imageUrl, arrays) {
            arrays.splice(index, 1, imageUrl);
        }

    });

});