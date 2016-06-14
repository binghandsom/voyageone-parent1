/**
 * @author tony-piao
 * 京东产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("jdSchema", function (productDetailService,feedMappingService,productDetailService,platformMappingService,$translate,notify,$location,$anchorScroll) {
        return {
            restrict: "E",
            replace: false,
            transclude: true,
            templateUrl : "views/product/jd.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productId: "=productId",
                cartInfo:"=cartInfo"
            },
            link: function (scope,element) {
                scope.vm = {
                    productDetails:null,
                    productCode:"",
                    mastData:null,
                    platform:null,
                    status:"Pending",
                    skuTemp:{},
                    checkFlag:{translate:0, tax:0, category:0, attribute:0},
                    resultFlag:0,
                    sellerCats:[]
                };

                initialize();
                scope.jdCategoryMapping = jdCategoryMapping;
                scope.openSellerCat = openSellerCat;
                scope.saveProduct = saveProduct;
                scope.validSchema = validSchema;
                scope.goto = function (id) {
                    console.log($("#"+id).offset().top);
                    $("body").animate({ scrollTop:  $("#"+id).offset().top}, 1000);
                };
                /**
                 * 获取京东页面初始化数据
                 */
                function initialize(){
                    productDetailService.getProductPlatform({cartId:scope.cartInfo.value,prodId:scope.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.platform = resp.data.platform;

                        if(scope.vm.platform != null){
                            scope.vm.platform.status = scope.vm.status = scope.vm.platform.status == null ? scope.vm.status : scope.vm.platform.status;
                            scope.vm.checkFlag.category = scope.vm.platform.pCatPath == null ? 0 : 1;
                            scope.vm.platform.pStatus = scope.vm.platform.pStatus == null ? "WaitingPublish" : scope.vm.platform.pStatus;
                            scope.vm.sellerCats = scope.vm.platform.sellerCats;
                        }

                        _.each(scope.vm.mastData.skus,function(mSku){
                                scope.vm.skuTemp[mSku.skuCode] = mSku;
                        });

/*                      scope.vm.checkFlag.translate = scope.vm.mastData.translateStatus == null ? 0 : scope.vm.mastData.translateStatus;
                        scope.vm.checkFlag.tax = scope.vm.mastData.hsCodeStatus == null ? 0 : scope.vm.mastData.hsCodeStatus;      */
                        scope.vm.checkFlag.translate = 1;
                        scope.vm.checkFlag.tax = 1;

                    });
                    productDetailService.getProductInfo({productId: scope.productId})
                        .then(function (res) {
                            scope.vm.productDetails = res.data.productInfo;
                            scope.vm.productCode = res.data
                        })
                }

                function jdCategoryMapping(productInfo, popupNewCategory) {
                    platformMappingService.getPlatformCategories({cartId: scope.cartInfo.value})
                        .then(function (res) {
                            if (!res.data || !res.data.length) {
                                alert("没数据");
                                return null;
                            }
                            return popupNewCategory({
                                from:scope.vm.platform == null?"":scope.vm.platform.pCatPath,
                                categories: res.data,
                                plateSchema:true
                            });
                        }).then(function (context) {
                            if(scope.vm.platform != null){
                                if(context.selected.catPath == scope.vm.platform.pCatPath)
                                    return;
                            }

                            productDetailService.changePlatformCategory({cartId:scope.cartInfo.value,prodId:scope.productId,catId:context.selected.catId}).then(function(resp){
                                scope.vm.platform = resp.data.platform;
                                scope.vm.platform.pCatPath = context.selected.catPath;
                                scope.vm.checkFlag.category = 1;
                                scope.vm.platform.status = scope.vm.status =  "Pending";
                            });
                        });
                }

                function openSellerCat (openAddChannelCategoryEdit) {
                    var selectedIds = {};
                    scope.vm.sellerCats.forEach(function(element){
                        selectedIds[element.cid]=true;
                    });
                    console.log(selectedIds);
                    var selList = [{"code": scope.vm.productDetails.productCode, "sellerCats":scope.vm.platform.sellerCats,"cartId":scope.cartInfo.value,"selectedIds":selectedIds,plateSchema:true}];
                    openAddChannelCategoryEdit(selList).then(function (context) {
                            /**清空原来店铺类分类*/
                            scope.vm.sellerCats = [];
                            angular.forEach(context.saveInfo.fullCatId,function(item,index){
                                var cids = item.split("-");
                                var cid = cids[cids.length-1];
                                var cNames =  context.saveInfo.fullCNames[index].split(">");
                                var cName = cNames[cNames.length-1];
                                scope.vm.sellerCats.push({cid:cid,cids:cids,cName:cName,cNames:cNames});
                            });

                    })
                }

                function saveProduct(){

                    var statusCount = 0;
                     for(var attr in scope.vm.checkFlag){
                         statusCount += scope.vm.checkFlag[attr] == true ? 1 : 0;
                     }

                    if(scope.vm.platform.status == "Approved" && scope.vm.platform.pBrandName == null){
                        notify.danger("请先确认是否在京东后台申请过相应品牌");
                        return;
                    }

                    switch (scope.vm.status){
                        case "Pending":
                                scope.vm.platform.status = scope.vm.status = statusCount == 4 ? "Ready" : scope.vm.platform.status;
                                break;
                        case "Ready":
                                scope.vm.platform.status = scope.vm.status = "Approved";
                                break;
                    }

                     scope.vm.platform.pAttributeStatus = 1;
                     scope.vm.platform.sellerCats = scope.vm.sellerCats;
                     scope.vm.platform.cartId = scope.cartInfo.value;

                      //判断价格
                    productDetailService.updateProductPlatformChk({prodId:scope.productId,platform:scope.vm.platform}).then(function(resp){
                        scope.vm.platform.modified = resp.data.modified;
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    },function(resp){

                    });

/*                     productDetailService.updateProductPlatform({prodId:scope.productId,platform:scope.vm.platform}).then(function(resp){
                         scope.vm.platform.modified = resp.data.modified;
                         notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    });*/
                }

                function validSchema(save){
                    if (!scope.vm.platform)
                        return false;

                    if (!scope.vm.platform.schemaFields)
                        return false;

                    return !scope.vm.platform.schemaFields.some(function(schema){
                        if(schema.form == null)
                            return true;
                        if(schema.form.$valid == false){
                            if(save)
                                alert(schema.name + "不能为空！");
                           return true;
                        }
                    });
                }
            }
        };
    });
});