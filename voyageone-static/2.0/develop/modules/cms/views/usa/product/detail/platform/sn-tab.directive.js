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

        constructor($scope,detailDataService,$usProductDetailService,notify,$rootScope,popups){
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.$usProductDetailService = $usProductDetailService;
            this.notify = notify;
            this.$rootScope = $rootScope;
            this.popups = popups;

            // 图片展示
            this.imageView = {
                imageNum:0,
                images:[],
                boxImageNum:0,
                boxImages:[],
                imageUrl: $rootScope.imageUrl,
                currImage:{image1:""},
                currBoxImage:{image2:""},
                urlkey:""
            };
            this.showModel = true;
            // SKU
            this.selAllSkuFlag = false;
        }

        init(){
            let self = this;

            self.detailDataService.getProductInfo({prodId:self.$scope.productInfo.productId}).then(res => {

                self.mastData = res.data.mastData;
                self.platform = res.data.platform;
                self.productComm = res.data.productComm;

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

                let urlkeyField = self.searchField("urlkey", self.productComm.schemaFields);
                if (urlkeyField && urlkeyField.value) {
                    self.imageView.urlkey = urlkeyField.value;
                }

                // SKU 是否全选
                let flag = true;
                _.each(self.platform.skus, sku => {
                    let isSale = sku.isSale;
                    if (!isSale) {
                        flag = false;
                    }
                });
                self.selAllSkuFlag = flag;

                console.log(res.data);
                _.each(self.productComm.schemaFields, item => {
                    if (item.id =='images1' || item.id =='images2') {

                        console.log(item);
                    }
                });
            });
        }

        // Save
        save(status) {
            let self = this;
            let platform = angular.copy(self.platform);
            if (status) {
                platform.pStatus = status;
            }
            let productComm = angular.copy(self.productComm);
            productComm.fields.images1 = self.imageView.images;
            productComm.fields.images2 = self.imageView.boxImages;
            let parameter = {
                prodId:self.$scope.productInfo.productId,
                data:{
                    mastData:self.mastData,
                    platform:platform,
                    productComm:productComm
                }
            };
            this.$usProductDetailService.updateCommonProductInfo(parameter).then(res => {
                if (res.data) {
                    self.notify.success("Save success.");
                    self.platform.pStatus = status;
                }
            });
        }

        /**
         * Pop category
         * @param option
         * @param attr
         */
        popUsCategory(option, attr) {
            let self = this;

            self.popups.openUsCategory(option).then(context => {
                if(option.muiti){
                    let categories = _.pluck(context, "catPath");
                    _.extend(self.feed.attribute, {categories:categories});
                    _.extend(self.feed, {categoriesTree:context});
                }else{

                    let selNode = {pCatId:context.catId, pCatPath:context.catPath};
                    _.extend(self.platform, selNode);
                    console.log(context);
                    // _.extend(self.feed, {category: context.catPath, categoryCatId:context.catId});
                    // if (!!context.mapping) {
                    //     let seoInfo = {};
                    //     if (!!context.mapping.seoTitle) {
                    //         _.extend(seoInfo, {seoTitle: context.mapping.seoTitle});
                    //     }
                    //     if (!!context.mapping.seoKeywords) {
                    //         _.extend(seoInfo, {seoKeywords: context.mapping.seoKeywords});
                    //     }
                    //     if (!!context.mapping.seoDescription) {
                    //         _.extend(seoInfo, {seoDescription: context.mapping.seoDescription});
                    //     }
                    //     // amazon、googleCategory、googleDepartment、priceGrabber
                    //     let category = {
                    //         amazonBrowseTree:context.mapping.amazon,
                    //         googleCategory:context.mapping.googleCategory,
                    //         googleDepartment:context.mapping.googleDepartment,
                    //         priceGrabberCategory:context.mapping.priceGrabber};
                    //     _.extend(self.feed, category);
                    //     _.extend(self.feed, seoInfo);
                    // }
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
            if (num <= 0) {
                self.imageView.currImage = {};
                self.imageView.images = [];
                self.imageNum = 0;
            } else {
                if (self.imageView.urlkey) {
                    let count = _.size(self.imageView.images);
                    let add = num - count;
                    if (add != 0) {
                        if (add > 0) {
                            for (let i = 1; i <= add; i++) {
                                self.imageView.images.push({image1:self.imageView.urlkey + "-" + (count + i)});
                            }
                        } else {
                            self.imageView.images.splice(add);
                        }

                        if (!self.imageView.currImage.image1) {
                            self.imageView.currImage = self.imageView.images[0];
                        }
                    }
                }
            }
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
            if (num <= 0) {
                self.imageView.currBoxImage = {};
                self.imageView.boxImages = [];
                self.boxImageNum = 0;
            } else {
                if (self.imageView.urlkey) {
                    let count = _.size(self.imageView.boxImages);
                    let add = num - count;
                    if (add != 0) {
                        if (add > 0) {
                            for (let i = 1; i <= add; i++) {
                                self.imageView.boxImages.push({image2:self.imageView.urlkey + "-2" + (count + i)});
                            }
                        } else {
                            self.imageView.boxImages.splice(add);
                        }

                        if (!self.imageView.currBoxImage.image2) {
                            self.imageView.currBoxImage = self.imageView.boxImages[0];
                        }
                    }
                }
            }
        }
        changeImages(index, imageUrl, arrays) {
            arrays.splice(index, 1, imageUrl);
        }

        // SKU可售选择
        selAllSku() {
            let self = this;
            _.each(self.platform.skus, sku => {
                sku.isSale = self.selAllSkuFlag;
            });
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
                console.log(res);
                model.value = res.model;
                console.log(self.searchField("model", self.productComm.schemaFields).value);
                self.mastData.images = res.data;
            });
        }
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
