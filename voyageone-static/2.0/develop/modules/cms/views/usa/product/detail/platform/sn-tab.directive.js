/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/service/product.detail.service',
    'modules/cms/directives/platFormStatus.directive',
    'modules/cms/directives/noticeTip.directive',
    'modules/cms/directives/contextMenu.directive',
    '../detail.data.service'
], function (cms, carts) {

    class SnTabController{

        constructor($scope,detailDataService,$usProductDetailService,notify,$rootScope,popups,confirm,alert){
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.$usProductDetailService = $usProductDetailService;
            this.notify = notify;
            this.$rootScope = $rootScope;
            this.popups = popups;
            this.confirm = confirm;
            this.alert = alert;

            // 图片展示
            this.imageView = {
                imageNum:0,
                images:[],
                boxImageNum:0,
                boxImages:[],
                imageUrl: $rootScope.imageUrl,
                currImage:{image1:""},
                currBoxImage:{image2:""}
            };

            //平台类目
            this.categorys = {};
            this.showModel = false;
            // SKU
            this.selAllSkuFlag = false;
            // 平台状态
            this.platformStatus = this.detailDataService.platformStatus;
        }

        init(){
            let self = this;

            self.detailDataService.getProductInfo({prodId:self.$scope.productInfo.productId}).then(res => {

                self.mastData = res.data.mastData;
                self.platform = res.data.platform;

                if(!self.platform.platform.pStatus){
                    self.platform.platform.pStatus = 'Pending';
                }

                self.productComm = res.data.productComm;
                self.freeTagList = res.data.freeTagList;

                let freeTags = [];
                _.each(self.freeTagList, tag => {
                    freeTags.push(tag.tagPath);
                });
                self.freeTags = freeTags;

                // 图片
                let images1 = self.productComm.fields.images1;
                if (images1 && _.size(images1) > 0) {
                    self.imageView.images = images1;
                    self.imageView.imageNum = _.size(images1);
                    self.imageView.currImage = images1[0];
                }
                let images2 = self.productComm.fields.images2;
                if (images2 && _.size(images2) > 0) {
                    self.imageView.boxImages = images2;
                    self.imageView.boxImageNum = _.size(images2);
                    self.imageView.currBoxImage = images2[0];
                }

                // SKU 是否全选
                let flag = true;
                _.each(self.platform.platform.skus, sku => {
                    let isSale = sku.isSale;
                    if (!isSale) {
                        flag = false;
                    }
                    // 去productComm.skus匹配重量和尺码等属性
                    let commonSku = _.find(self.productComm.skus, commSku => {
                        return sku.skuCode == commSku.skuCode;
                    });
                    if (commonSku) {
                        let commAttr = {
                            barcode:commonSku.barcode,
                            clientSize:commonSku.clientSize,
                            size:commonSku.size,
                            weight:commonSku.weight,
                            weightUnit:commonSku.weightUnit
                        };
                        _.extend(sku, commAttr);
                    }
                });

                self.selAllSkuFlag = flag;

                self.categorys.priceGrabberCategory = self.searchField("priceGrabberCategory", self.productComm.schemaFields);
                self.categorys.googleDepartment = self.searchField("googleDepartment", self.productComm.schemaFields);
                self.categorys.googleCategory = self.searchField("googleCategory", self.productComm.schemaFields);

                // console.log(res.data);
                // _.each(self.platform.platform.platformFields, item => {
                //    console.log(item.id);
                // });

            });
        }

        popCategory(option, attrName) {
            let self = this;

            self.popups.openAmazonCategory(option).then(res => {
                self.categorys[attrName].value = res.catPath;
            });
        }

        // Save
        save(status) {
            let self = this;
            let platform = angular.copy(self.platform);
            if (status) {
                platform.platform.pStatus = status;
            }
            let productComm = angular.copy(self.productComm);
            productComm.fields.images1 = self.imageView.images;
            productComm.fields.images2 = self.imageView.boxImages;
            let parameter = {
                prodId:self.$scope.productInfo.productId,
                data:{
                    mastData:self.mastData,
                    platform:platform,
                    productComm:productComm,
                    freeTags:self.freeTags
                }
            };
            this.$usProductDetailService.updateCommonProductInfo(parameter).then(res => {
                if (res.data) {
                    self.notify.success("Save success.");
                    self.platform.platform.pStatus = platform.platform.pStatus;
                }
            });
        }

        /**
         * Pop category
         * @param option  cartId:number
         *                froms:[]
         *                muiti:boolean
         */
        popUsCategory(option) {
            let self = this;

            self.popups.openUsCategory(option).then(context => {
                if(option.muiti){
                    self.platform.sellerCarts = context;
                } else {
                    // SN Primary Category
                    let selNode = {pCatId:context.catId, pCatPath:context.catPath};
                    _.extend(self.platform.platform, selNode);

                    let confirmMsg = "Whether to cover the properties associated with the SN primary category?"
                                     + "(including Google Category, Google DepartMent, PriceGrabber Category, "
                                     + "SEO attributes...)";
                    self.confirm(confirmMsg).then(confirmed => {
                        if (context.mapping) {
                            self.searchField("googleCategory", self.productComm.schemaFields).value = context.mapping.googleCategory;
                            self.searchField("googleDepartment", self.productComm.schemaFields).value = context.mapping.googleDepartment;
                            self.searchField("priceGrabberCategory", self.productComm.schemaFields).value = context.mapping.priceGrabber;

                            self.searchField("seoTitle", self.platform.platform.platformFields).value = context.mapping.seoTitle;
                            self.searchField("seoKeywords", self.platform.platform.platformFields).value = context.mapping.seoKeywords;
                            self.searchField("seoDescription", self.platform.platform.platformFields).value = context.mapping.seoDescription;
                        } else {
                            self.searchField("googleCategory", self.productComm.schemaFields).value = "";
                            self.searchField("googleDepartment", self.productComm.schemaFields).value = "";
                            self.searchField("priceGrabberCategory", self.productComm.schemaFields).value = "";

                            self.searchField("seoTitle", self.platform.platform.platformFields).value = "";
                            self.searchField("seoKeywords", self.platform.platform.platformFields).value = "";
                            self.searchField("seoDescription", self.platform.platform.platformFields).value = "";
                        }
                    });
                }
            });
        }

        /**全schema中通过name递归查找field*/
        searchField(fieldId, schema) {
            let self = this;

            let result = null;

            _.find(schema, function (field) {

                if (field.id === fieldId) {
                    result = field;
                    return true;
                }

                if (field.fields && field.fields.length) {
                    result = self.searchField(fieldId, field.fields);
                    if (result)
                        return true;
                }

                return false;
            });

            return result;
        }

        // 添加Item -> image
        addImage() {
            let self = this;
            self.imageView.images.push({});
            self.imageView.imageNum = _.size(self.imageView.images);

            if (!self.imageView.currImage.image1 && self.imageView.imageNum > 0) {
                self.imageView.currImage = self.imageView.images[0];
            }
        }
        // 删除Item -> image
        deleteImage(index) {
            let self = this;
            self.imageView.images.splice(index, 1);
            let imageNum = self.imageView.imageNum - 1;
            self.imageView.imageNum = imageNum;
            if (imageNum == 0) {
                self.imageView.currImage = {}
            } else {
                self.imageView.currImage = self.imageView.images[0];
            }
        }
        // 初始化Item -> image
        initImage(num) {
            let self = this;

            if(num > 15){
                self.notify.danger('Please not more than 15');
                self.imageView.imageNum = null;
                return false;
            }

            self.confirm("Make sure of setting the image count to <strong style='color:red'> " + num + "</strong>").then(confirmed => {
                if (num <= 0) {
                    self.imageView.currImage = {};
                    self.imageView.images = [];
                    self.imageNum = 0;
                } else {
                    let count = _.size(self.imageView.images);
                    let add = num - count;
                    if (add != 0) {
                        if (add > 0) {
                            let urlkey = self.searchField("urlkey", self.productComm.schemaFields);
                            if (!urlkey || !urlkey.value) {
                                self.alert("No urlkey!")
                                return;
                            }
                            for (let i = 1; i <= add; i++) {
                                self.imageView.images.push({image1:urlkey.value + "-" + (count + i)});
                            }
                        } else {
                            self.imageView.images.splice(add);
                        }

                        if (!self.imageView.currImage.image1) {
                            self.imageView.currImage = self.imageView.images[0];
                        }
                    }
                }
            });
        }
        // 添加Box -> image
        addBoxImage() {
            let self = this;
            self.imageView.boxImages.push({});
            self.imageView.boxImageNum = _.size(self.imageView.boxImages);

            if (!self.imageView.currBoxImage.image2 && self.imageView.boxImageNum > 0) {
                self.imageView.currBoxImage = self.imageView.boxImages[0];
            }
        }
        // 删除Box -> image
        deleteBoxImage(index) {
            let self = this;
            self.imageView.boxImages.splice(index, 1);
            let boxImageNum = self.imageView.boxImageNum - 1;
            self.imageView.boxImageNum = boxImageNum;
            if (boxImageNum == 0) {
                self.imageView.currBoxImage = {}
            } else {
                self.imageView.currBoxImage = self.imageView.currBoxImage[0];
            }
        }
        // 初始化Box -> image
        initBoxImage(num) {
            let self = this;

            if(num > 15){
                self.notify.danger('Please not more than 15');
                self.imageView.boxImageNum = null;
                return false;
            }

            self.confirm("Make sure of setting the image count to <strong style='color:red'> " + num + "</strong>").then(confirmed => {
                if (num <= 0) {
                    self.imageView.currBoxImage = {};
                    self.imageView.boxImages = [];
                    self.boxImageNum = 0;
                } else {
                    let count = _.size(self.imageView.boxImages);
                    let add = num - count;
                    if (add != 0) {
                        if (add > 0) {
                            let urlkey = self.searchField("urlkey", self.productComm.schemaFields);
                            if (!urlkey || !urlkey.value) {
                                self.alert("No urlkey!")
                                return;
                            }
                            for (let i = 1; i <= add; i++) {
                                self.imageView.boxImages.push({image2:urlkey.value + "-2" + (count + i)});
                            }
                        } else {
                            self.imageView.boxImages.splice(add);
                        }

                        if (!self.imageView.currBoxImage.image2) {
                            self.imageView.currBoxImage = self.imageView.boxImages[0];
                        }
                    }
                }
            });
        }
        changeImages(index, imageUrl, arrays) {
            arrays.splice(index, 1, imageUrl);
        }

        // SKU可售选择
        selAllSku() {
            let self = this;
            _.each(self.platform.platform.skus, sku => {
                sku.isSale = self.selAllSkuFlag;
            });
        }
        checkSelAllSku(sku) {
            let self = this;
            let isSale = sku.isSale;
            if (!isSale) {
                self.selAllSkuFlag = false;
            } else {
                let notSelOne = _.find(self.platform.platform.skus, sku => {
                    return !sku.isSale;
                });
                self.selAllSkuFlag = !notSelOne;
            }
        }

        // Move model
        moveModel() {
            let self = this;
            let prodId = self.searchField("prodId", self.productComm.schemaFields);
            let code = self.searchField("code", self.productComm.schemaFields);
            let model = self.searchField("model", self.productComm.schemaFields);
            let parameter = {
                prodId:self.$scope.productInfo.productId,
                code:code.value,
                model:model.value
            };
            self.popups.openMoveModel(parameter).then(res => {
                model.value = res.model;
                self.productComm.fields.model = res.model;
                self.mastData.images = res.data;
            });
        }

        // Edit Group
        editGroup() {
            let self = this;
            let ctx = {

            };
            self.popups.openUsEditGroup({modelCodes:self.mastData.images}).then(res => {

            });
        }

        /**
         * 选择自由标签
         */
        setFreeTag () {
            //orgChkStsMap
            let self = this;
            let params = {
                orgFlg: '1',
                selTagType: '6',
                selAllFlg: 0,
                orgChkStsMap:self.freeTags
            };

            self.popups.openUsFreeTag(params).then(res => {
                // console.log(res);
                self.freeTagList = res.selectdTagList == null ? [] : res.selectdTagList;
                let freeTags = [];
                if (_.size(self.freeTagList) > 0) {
                    freeTags = _.chain(self.freeTagList).map(function (key, value) {
                        return key.tagPath;
                    }).value();
                }
                self.freeTags = freeTags;
            });
        };
    }

    cms.directive('snTab', function () {
        return {
            restrict: 'E',
            controller: SnTabController,
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/usa/product/detail/platform/sn-tab.directive.html'
        }
    })

});
