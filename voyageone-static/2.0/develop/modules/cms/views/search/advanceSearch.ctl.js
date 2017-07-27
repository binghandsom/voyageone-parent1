define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/controller/popup.ctl',
    'modules/cms/directives/keyValue.directive',
    'modules/cms/service/search.advance2.service',
    'modules/cms/service/product.detail.service',
    './advance.search.append.ctl'
], function (cms, carts) {

    function listWithThumb(list) {
        // 预先处理商品的缩略图，简化页面上如下的缩略图绑定
        // groupInfo.common.fields.images6 && groupInfo.common.fields.images6[0].image6
        //     ?groupInfo.common.fields.images6[0].image6
        //     :groupInfo.common.fields.images1[0].image1
        // 还有这个
        // productInfo.common.fields.images6 && productInfo.common.fields.images6[0].image6
        // ?productInfo.common.fields.images6[0].image6
        // :productInfo.common.fields.images1[0].image1
        if (list) {
            list.forEach(function (i) {
                var f = i.common.fields;
                i.thumb = f.images6 && f.images6.length && f.images6[0].image6
                    ?f.images6[0].image6
                    :f.images1[0].image1
            });
        }
        return list;
    }

    cms.controller('advanceSearchController', function searchIndex($scope, $routeParams, searchAdvanceService2, $searchAdvanceService2, $fieldEditService, productDetailService, systemCategoryService, $addChannelCategoryService, confirm, $translate, notify, alert, sellerCatService, platformMappingService, attributeService, $sessionStorage, cActions, popups, $q, shelvesService, $localStorage) {

            $scope.vm = {
                searchInfo: {
                    compareType: null,
                    brand: null,
                    tags: [],
                    priceChgFlg: '',
                    priceDiffFlg: '',
                    tagTypeSelectValue: '0',
                    promotionList: [],
                    catgoryList: [],
                    cidValue: [],
                    promotionTagType: 1,
                    freeTagType: 1,
                    supplierType: 1,
                    brandSelType: 1,
                    mCatPathType: 1,
                    fCatPathType: 1,
                    shopCatType: 1,
                    pCatPathType: 1,
                    productSelType: '1',
                    sizeSelType: '1',
                    salesType: 'All',
                    custGroupType: '1'
                },
                _selall: false,
                groupPageOption: {curr: 1, total: 0, fetch: getGroupList},
                productPageOption: {curr: 1, total: 0, fetch: getProductList},
                exportPageOption: {curr: 1, size: 10, total: 0, fetch: exportSearch},
                groupList: [],
                productList: [],
                codeMap: [],
                currTab: "product",
                status: {open: true},
                groupSelList: {selList: []},
                productSelList: {selList: []},
                custAttrList: [],
                sumCustomProps: [],
                platform: {catPath: null},
                masterCat: {catPath: null},
                feedCat: {catPath: null},
                channelInner: {catPath: null},
                _cart_tab_act: false,
                channelId: ""
            };
            $scope.searchInfoBefo;
            $scope.exportStatus = ["正在生成", "完成", "失败"];
            $scope.initialize = initialize;
            $scope.clear = clear;
            $scope.search = function (curr) {
                search(curr);
                $scope.vm._selall = false;
            };
            $scope.exportFile = exportFile;
            $scope.exportSearch = exportSearch;
            $scope.getGroupList = getGroupList;
            $scope.getProductList = getProductList;
            $scope.openCategoryMapping = openCategoryMapping;
            $scope.refreshProductCategory = refreshProductCategory;
            $scope.openMasterCategoryMapping = openMasterCategoryMapping;
            $scope.openFeedCategoryMapping = openFeedCategoryMapping;
            $scope.openChannelInnerCategory = openChannelInnerCategory;
            $scope.bindCategory = bindCategory;
            $scope.add = addCustAttribute;
            $scope.del = delCustAttribute;
            $scope.openAddPromotion = openAddPromotion;
            $scope.openAddChannelCategoryFromAdSearch = openAddChannelCategoryFromAdSearch;
            $scope.openJMActivity = openJMActivity;
            $scope.openBulkUpdate = openBulkUpdate;
            $scope.getCat = getCat;
            $scope.openAdvanceImagedetail = openAdvanceImagedetail;
            $scope.openApproval = openApproval;
            $scope.openIntelligentPublish = openIntelligentPublish;
            $scope.platformCategoryMapping = platformCategoryMapping;
            $scope.openTagManagement = openTagManagement;
            $scope.dismiss = dismiss;
            $scope.jdCategoryMapping = jdCategoryMapping;
            $scope.editPlatformAttribute = editPlatformAttribute;
            $scope.addShelves = addShelves;
            $scope._chkProductSel = _chkProductSel;

            /**
             * 初始化数据.
             */
            function initialize() {
                // 如果来至category 或者 header的检索,将初始化检索条件
                if ($routeParams.type == 10001) {
                    $scope.vm.searchInfo.fCatPathList = [$routeParams.value1];
                    $scope.vm.feedCats = [{catId: $routeParams.value2, catPath: $routeParams.value1}];
                }
                if ($routeParams.type == "1") {
                    // 从菜单栏而来，检索主数据（TODO--注*现已不使用）
                    $scope.vm.searchInfo.catPath = decodeURIComponent($routeParams.value);
                } else if ($routeParams.type == "2") {
                    // 从header部的全局搜索而来
                    $scope.vm.searchInfo.codeList = decodeURIComponent($routeParams.value1);
                }
                $searchAdvanceService2.init().then(function (res) {
                    $scope.vm.masterData = res.data;
                    $scope.vm.promotionList = _.where(res.data.promotionList, {isAllPromotion: 0});
                    $scope.vm.custAttrList.push({inputVal: "", inputOpts: "", inputOptsKey: ""});
                    $scope.vm.cartList = res.data.cartList;
                    if ($scope.vm.cartList.length == 1) {
                        $scope.vm._cartType_ = $scope.vm.cartList[0];
                        getCat($scope.vm._cartType_);
                    }

                    // 如果来至category 或者header search 则默认检索
                    $scope.vm.tblWidth = '100%'; // group tab的原始宽度
                    $scope.vm.tblWidth2 = '100%'; // product tab的原始宽度
                    if ($routeParams.type == "3") {
                        // 从菜单栏而来，检索店铺内分类
                        var catObj = _.find($scope.vm.masterData.cartList, function (item) {
                            return item.value == $routeParams.value1;
                        });
                        $scope.vm.searchInfo.cartId = $routeParams.value1;
                        // 打开平台搜索tab栏
                        $scope.vm._cart_tab_act = true;
                        // 设置渠道名称
                        $scope.vm._cartType_ = catObj;
                        getCat($scope.vm._cartType_);
                        // 设置查询值后检索
                        $scope.vm._shopCatValues = [decodeURIComponent($routeParams.value3)];
                        $scope.vm.searchInfo.cidValue = [$routeParams.value2];
                        search();
                        return;
                    }
                    if ($routeParams.type == "1" || $routeParams.type == "2" || $routeParams.type == "10001") {
                        search();
                        return;
                    }
                    if ($routeParams.type == '4' && $sessionStorage.feedSearch) {
                        // 从主页而来的检索
                        if ($routeParams.value1 > 10) {
                            $scope.vm._cart_tab_act = true;
                            var catObj = _.find($scope.vm.masterData.cartList, function (item) {
                                return item.value == $sessionStorage.feedSearch.cartId;
                            });
                            $scope.vm._cartType_ = catObj;
                            getCat($scope.vm._cartType_);
                        }
                        $scope.vm.searchInfo = angular.copy($sessionStorage.feedSearch);
                        if ($scope.vm.searchInfo == undefined) {
                            $scope.vm.searchInfo = {};
                        }
                        search();
                        if ($sessionStorage.feedSearch) delete $sessionStorage.feedSearch;
                    }
                    $scope.vm.channelId = res.data.channelId == null ? "" : res.data.channelId;
                })
            }

            /**
             * 清空画面上显示的数据
             */
            function clear(child) {
                $scope.vm.searchInfo = {
                    compareType: null,
                    brand: null,
                    tags: [],
                    priceChgFlg: '',
                    priceDiffFlg: '',
                    tagTypeSelectValue: '0',
                    cidValue: [],
                    promotionTagType: 1,
                    freeTagType: 1,
                    supplierType: 1,
                    brandSelType: 1,
                    mCatPathType: 1,
                    fCatPathType: 1,
                    shopCatType: 1,
                    pCatPathType: 1,
                    shopCatStatus: null,
                    inventory: '',
                    salesStart: null,
                    salesEnd: null,
                    priceStart: null,
                    priceEnd: null,
                    publishTimeStart: null,
                    publishTimeTo: null,
                    createTimeStart: null,
                    createTimeTo: null,
                    fCatPath: null
                };
                $scope.vm._selall = false;
                $scope.vm._cartType_ = '';
                getCat(null);
                $scope.vm.masterData.tagList = [];
                $scope.vm.masterData.catList = [];
                $scope.vm.custAttrList = [{inputVal: "", inputOpts: ""}];
                $scope.vm.platform.catPath = null;
                $scope.vm.masterCat.catPath = null;
                $scope.vm.feedCat.catPath = null;
                $scope.vm.channelInner.catPath = null;
                $scope.vm._shopCatValues = null;
                $scope.vm._promotionTags = null;
                $scope.vm._freeTags = null;

                if (child.columnArrow) {
                    _.forEach(child.columnArrow, function (value, key) {
                        child.columnArrow[key] = null;
                    });
                }

            }

            /**
             * 检索
             */
            function search(curr) {
                // 检查输入数据 库存/金额
                var intVal = $scope.vm.searchInfo.inventory;
                if (!(intVal == null || intVal == undefined || intVal == '')) {
                    if (isNaN(intVal)) {
                        alert("库存必须是数字");
                        return;
                    }
                }
                intVal = $scope.vm.searchInfo.priceStart;
                if (!(intVal == null || intVal == undefined || intVal == '')) {
                    if (isNaN(intVal)) {
                        alert("价格范围必须是数字");
                        return;
                    }
                }
                intVal = $scope.vm.searchInfo.priceEnd;
                if (!(intVal == null || intVal == undefined || intVal == '')) {
                    if (isNaN(intVal)) {
                        alert("价格范围必须是数字");
                        return;
                    }
                }
                intVal = $scope.vm.searchInfo.salesStart;
                if (!(intVal == null || intVal == undefined || intVal == '')) {
                    if (isNaN(intVal)) {
                        alert("销量范围必须是数字");
                        return;
                    }
                }
                intVal = $scope.vm.searchInfo.salesEnd;
                if (!(intVal == null || intVal == undefined || intVal == '')) {
                    if (isNaN(intVal)) {
                        alert("销量范围必须是数字");
                        return;
                    }
                }

                // 默认设置成第一页
                if (curr) {
                    $scope.vm.groupPageOption.curr = curr;
                    $scope.vm.productPageOption.curr = curr;
                }
                //$scope.vm.groupPageOption.curr = 1;
                //$scope.vm.productPageOption.curr = 1;

                $scope.vm.searchInfo.custAttrMap = angular.copy($scope.vm.custAttrList);
                $scope.searchInfoBefo = angular.copy($scope.vm.searchInfo);
                $scope.searchInfoBefo = searchAdvanceService2.resetSearchInfo($scope.searchInfoBefo);
                searchAdvanceService2.search($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.productPageOption).then(function (res) {
                    $scope.vm.customProps = res.data.customProps;
                    var sumCustomProps = [];
                    _.forEach($scope.vm.customProps, function (data) {
                        sumCustomProps.push({'name': data.feed_prop_translation});
                        sumCustomProps.push({'name': data.feed_prop_original});
                    });
                    $scope.vm.sumCustomProps = sumCustomProps;
                    $scope.vm.commonProps = res.data.commonProps;
                    $scope.vm.selSalesType = res.data.selSalesType;
                    if ($scope.vm.selSalesType == null || $scope.vm.selSalesType == undefined) {
                        $scope.vm.selSalesType = [];
                    }
                    $scope.vm.selBiDataList = res.data.selBiDataList;
                    if ($scope.vm.selBiDataList == null || $scope.vm.selBiDataList == undefined) {
                        $scope.vm.selBiDataList = [];
                    }

                    $scope.vm.productUrl = res.data.productUrl;
                    $scope.vm.groupList = listWithThumb(res.data.groupList);
                    $scope.vm.groupPageOption.total = res.data.groupListTotal;
                    $scope.vm.groupSelList = res.data.groupSelList;
                    $scope.vm.productList = listWithThumb(res.data.productList);
                    $scope.vm.codeMap = res.data.codeMap;
                    $scope.vm.qtyList = res.data.qtyList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productSelList = res.data.productSelList;
                    for (var idx in res.data.freeTagsList) {
                        var prodObj = $scope.vm.productList[idx];
                        prodObj._freeTagsInfo = res.data.freeTagsList[idx];
                    }
                    $scope.vm.currTab = "product";

                    if ($scope.vm.currTab == 'product')
                        $scope.vm.currTab2 = true;
                    else
                        $scope.$feedActive = false;

                    $scope.vm.fstShowGrpFlg = true;

                    contructQty($scope.vm.productList, $scope.vm.codeMap);

                    // 计算表格宽度
                    $scope.vm.tblWidth = ($scope.vm.commonProps.length * 170 + $scope.vm.sumCustomProps.length * 100 + $scope.vm.selSalesType.length * 150 + $scope.vm.selBiDataList.length * 150 + 400) + 'px';
                    $scope.vm.tblWidth2 = ($scope.vm.commonProps.length * 170 + $scope.vm.sumCustomProps.length * 100 + $scope.vm.selSalesType.length * 150 + $scope.vm.selBiDataList.length * 180 ) + 'px';
                })
            }

            function contructQty(productList, codeMap) {
                _.map(productList, function (element) {
                    // 复制记录产品的原始originalTitleCn，如果为空，则设置productNameEn显示为originalTitleCn
                    var oldOriginalTitleCn = angular.copy(element.common.fields.originalTitleCn);
                    if (!oldOriginalTitleCn) {
                        oldOriginalTitleCn = angular.copy(element.common.fields.productNameEn);
                        element.common.fields.originalTitleCn = oldOriginalTitleCn;
                    }
                    _.extend(element, {"oldOriginalTitleCn": oldOriginalTitleCn});
                    element.saleQty = (function () {
                        var qtyArr = codeMap[element.common.fields.code],
                            _cartId = $scope.vm.searchInfo.cartId,
                            result1 = [], result2 = [];

                        _.each(qtyArr, function (value, key) {
                            if (_cartId && key == carts.valueOf($scope.vm.searchInfo.cartId).name) {
                                result1.push(key + ":" + value);
                            } else {
                                result2.push(key + ":" + value);
                            }
                        });
                        if (_cartId)
                            return result1.concat(result2);
                        else
                            return result2;
                    })();
                });
            }

            /**
             * 数据导出
             */
            function exportFile(fileType) {
                if ($scope.vm.productPageOption.total == 0) {
                    alert($translate.instant('TXT_MSG_NO_PRODUCT_ROWS'));
                    return;
                }
                var msg = '';
                var selList = getSelProductList();
                $scope.vm.searchInfo._selCodeList = [];

                if (selList.length > 0 && !$scope.vm._selall) {
                    msg = '<br>仅导出选中的记录，如需导出全部记录，请回到一览画面取消选择。';
                    $scope.vm.searchInfo._selCodeList = _.pluck(selList, 'code');
                } else {
                    msg = '<br>将导出所有的商品记录，如需只导出部分商品，请回到一览画面选择指定商品。';
                }

                switch (fileType) {
                    case 1:
                        msg = '即将导出Code级的搜索结果，请确认。' + msg;
                        break;
                    case 2:
                        msg = '即将导出Group级的搜索结果，请确认。' + msg;
                        break;
                    case 3:
                        msg = '即将导出SKU级的搜索结果，请确认。' + msg;
                        break;
                    case 4:
                        msg = '即将导出聚美上新SKU级的搜索结果，请确认。' + msg;
                        break;
                    case 5:
                        msg = "即将根据搜索结果导出报备文件，请确认。" + msg;
                        break;
                }

                popups.openColumnForDownLoad({
                    fileType: fileType
                }).then(function (inventoryDetails) {
                    confirm(msg).then(function () {
                        $scope.vm.searchInfo.fileType = fileType;
                        $scope.vm.searchInfo.inventoryDetails = inventoryDetails;
                        searchAdvanceService2.exportFile($scope.vm.searchInfo).then(function (res) {
                            var ecd = res.data.ecd;
                            if (ecd == undefined || ecd == '4003') {
                                alert("创建文件时出错。");
                            } else if (ecd == '4002') {
                                alert("未选择导出文件类型。");
                            } else if (ecd == '4004') {
                                alert("已经有一个任务还没有执行完毕。请稍后再导出");
                            } else if (ecd == '0') {
                                notify.success($translate.instant('TXT_SUBMIT_SUCCESS'));
                            }
                        });
                    });
                });

            }

            /**
             * 初始化显示group数据
             */
            $scope.firstShowGroupList = function () {
                $scope.vm.currTab = 'group';
                if ($scope.vm.fstShowGrpFlg) {
                    $scope.vm.fstShowGrpFlg = false;
                    getGroupList();
                }

                if ($scope.vm.currTab == 'product')
                    $scope.vm.currTab2 = true;
                else
                    $scope.$feedActive = false;
            };

            /**
             * 分页处理group数据
             */
            function getGroupList() {
                $scope.searchInfoBefo = angular.copy($scope.vm.searchInfo);
                $scope.searchInfoBefo = searchAdvanceService2.resetSearchInfo($scope.searchInfoBefo);
                searchAdvanceService2.getGroupList($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.groupSelList, $scope.vm.commonProps, $scope.vm.customProps, $scope.vm.selSalesType, $scope.vm.selBiDataList)
                    .then(function (res) {

                        if (res.data.groupList) {
                            $scope.vm.groupList = listWithThumb(res.data.groupList)
                        } else {
                            $scope.vm.groupList = []
                        }

                        $scope.vm.groupPageOption.total = res.data.groupListTotal;
                        $scope.vm.groupSelList = res.data.groupSelList;
                        $scope.vm._selall = false;
                    });
            }

            /**
             * 分页处理product数据
             */
            function getProductList() {
                $scope.searchInfoBefo = angular.copy($scope.vm.searchInfo);
                $scope.searchInfoBefo = searchAdvanceService2.resetSearchInfo($scope.searchInfoBefo);
                searchAdvanceService2.getProductList($scope.vm.searchInfo, $scope.vm.productPageOption, $scope.vm.productSelList, $scope.vm.commonProps, $scope.vm.customProps, $scope.vm.selSalesType, $scope.vm.selBiDataList)
                    .then(function (res) {

                        if (res.data.productList) {
                            $scope.vm.productList = listWithThumb(res.data.productList)
                        } else {
                            $scope.vm.productList = []
                        }

                        $scope.vm.codeMap = res.data.codeMap;
                        $scope.vm.productPageOption.total = res.data.productListTotal;
                        $scope.vm.productSelList = res.data.productSelList;
                        $scope.vm._selall = false;
                        for (var idx in res.data.freeTagsList) {
                            var prodObj = $scope.vm.productList[idx];
                            prodObj._freeTagsInfo = res.data.freeTagsList[idx];
                        }
                        contructQty($scope.vm.productList, $scope.vm.codeMap);
                    });
            }

            /**
             * popup出添加到promotion的功能
             * @param promotion
             * @param openAddToPromotion
             */
            function openAddPromotion(promotion, cartObj, openAddToPromotionFnc) {
                promotion.cartId = cartObj.value;
                promotion.cartName = cartObj.name;
                _chkProductSel(null, _openAddPromotion, {'isSelAll': $scope.vm._selall ? 1 : 0, 'promotion': promotion});

                function _openAddPromotion(cartId, selList, context) {
                    openAddToPromotionFnc(context.promotion, selList, context).then(function () {
                        searchAdvanceService2.clearSelList();
                        $scope.search();
                    })
                }
            }

            function addShelves(shelvesId) {
                _chkProductSel(null, _addShelves, {'isSelAll': $scope.vm._selall ? 1 : 0, 'shelvesId': shelvesId});

                function _addShelves(cartId, selList, context) {
                    var productIds = [];
                    if (selList) {
                        _.forEach(selList, function (object) {
                            productIds.push(object.code);
                        });
                    }
                    context.productCodes = productIds;
                    context.searchInfo = $scope.searchInfoBefo;
                    shelvesService.addProduct(context).then(function () {
                        notify.success($translate.instant('TXT_SUBMIT_SUCCESS'));
                        $scope.search();
                    });
                }
            }

            /**
             * popup出添加到聚美Promotion的功能
             * @param promotion
             * @param openJMActivity
             */
            function openJMActivity(promotion, cartObj, openAddJMActivityFnc) {
                promotion.cartId = cartObj.value;
                promotion.cartName = cartObj.name;
                _chkProductSel(null, _openJMActivity, {'isSelAll': $scope.vm._selall ? 1 : 0, 'promotion': promotion});

                function _openJMActivity(cartId, selList, context) {
                    openAddJMActivityFnc(context.promotion, selList, context).then(function () {

                        $scope.search();
                    })
                }
            }

            /**
             * popup出批量修改产品的field属性
             * @param openFieldEdit
             */
            function openBulkUpdate(openFieldEdit, cartId) {
                _chkProductSel(null, _openBulkUpdate, {'isSelAll': $scope.vm._selall ? 1 : 0, "cartId": parseInt(cartId)});

                function _openBulkUpdate(cartId, selList, context) {
                    var selCnt = 0;
                    if (!$scope.vm._selall) {
                        var selList = getSelProductList();
                        selCnt = selList.length;
                    } else {
                        selCnt = $scope.vm.productPageOption.total;
                    }
                    context.selCnt = selCnt;
                    context.autoSynPrice = $scope.vm.masterData.autoApprovePrice;
                    context.searchInfo = $scope.searchInfoBefo;
                    openFieldEdit(selList, context).then(function (res) {
                        $scope.search();
                    })
                }
            }


            function refreshProductCategory() {
                _chkProductSel(null, _refreshProductCategory);

                function _refreshProductCategory(cartId, selList) {
                    var productIds = [];
                    if (selList) {
                        _.forEach(selList, function (object) {
                            productIds.push(object.code);
                        });
                    }
                    var data = {
                        prodIds: productIds,
                        isSelAll: $scope.vm._selall ? 1 : 0,
                        searchInfo: $scope.searchInfoBefo
                    };
                    productDetailService.refreshProductCategory(data).then(function (res) {
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        $scope.search();
                    });
                }
            }

            /**
             * popup弹出切换主数据类目
             * @param popupNewCategory
             */
            function openCategoryMapping(popupNewCategoryFnc) {
                _chkProductSel(null, _openCategoryMapping);

                function _openCategoryMapping(cartId, selList) {
                    systemCategoryService.getNewsCategoryList().then(function (res) {
                        popupNewCategoryFnc({
                            categories: res.data
                        }).then(function (context) {
                            bindCategory(context.selected, selList);
                        });
                    });
                }
            }

            /**
             * 类目变更
             */
            function bindCategory(selectedCat, selList) {
                confirm($translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY'))
                    .then(function () {
                        var productIds = [];
                        if (selList) {
                            _.forEach(selList, function (object) {
                                productIds.push(object.code);
                            });
                        }
                        var pCatList = selectedCat.platformCategory;
                        if (pCatList == undefined || pCatList == null || pCatList == '') {
                            pCatList = [];
                        }
                        var data = {
                            prodIds: productIds,
                            catId: selectedCat.catId,
                            catPath: selectedCat.catPath,
                            catPathEn: selectedCat.catPathEn,
                            pCatList: pCatList,
                            productType: selectedCat.productTypeEn,
                            sizeType: selectedCat.sizeTypeEn,
                            productTypeCn: selectedCat.productTypeCn,
                            sizeTypeCn: selectedCat.sizeTypeCn,
                            hscodeName8: selectedCat.hscodeName8,
                            hscodeName10: selectedCat.hscodeName10,
                            isSelAll: $scope.vm._selall ? 1 : 0,
                            searchInfo: $scope.searchInfoBefo
                        };
                        productDetailService.changeCategory(data).then(function (res) {
                            if (res.data.isChangeCategory) {
                                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                                $scope.search();
                            } else {
                                notify($translate.instant('TXT_MSG_PRODUCT_IS_PUBLISHING'));
                            }
                        })
                    });
            }

            /**
             * 添加新的自定义查询选项
             */
            function addCustAttribute() {
                if ($scope.vm.custAttrList.length < 5) {
                    $scope.vm.custAttrList.push({inputOptsKey: "", inputOpts: "", inputVal: ""});
                } else {
                    alert("最多只能添加5项")
                }
            }

            // 删除自定义查询选项
            function delCustAttribute(idx) {
                if ($scope.vm.custAttrList.length > 0) {
                    $scope.vm.custAttrList.splice(idx, 1);
                }
            }

            // 当选择搜索时设置输入框
            $scope.setSelValue = function (option, custAtts, typeVal) {
                if (typeVal && typeVal == 2) {
                    // 项目条件切换
                    option.inputVal = '';
                    return;
                }

                // 搜索项目切换
                option.inputOpts = null;
                option.inputVal = '';

                option.inputType = custAtts.valType;
                if (option.inputType == undefined) {
                    option.inputType = 'string';
                }
                option.inputOptsKey = custAtts.configCode;
                if (option.inputType.indexOf('list') == 0) {
                    option.display = 0;
                    // 从服务器取回下拉列表的值(先从本地缓存取，没有再从服务器取)
                    option._inputOptsList = [];
                    if ($scope.vm._inputOptsListMap == undefined) {
                        $scope.vm._inputOptsListMap = {};
                    }
                    var localOptsList = $scope.vm._inputOptsListMap[option.inputOptsKey];
                    if (localOptsList == undefined) {
                        // 本地缓存中没有该列表
                        var data = {"fieldsId": option.inputOptsKey, "inputType": option.inputType};
                        $searchAdvanceService2.getCustSearchList(data).then(function (res) {
                            option._inputOptsList.push({"name": '未设值', "value": ''});
                            if (res && res.data) {
                                option._inputOptsList = option._inputOptsList.concat(res.data);
                            }
                            $scope.vm._inputOptsListMap[option.inputOptsKey] = option._inputOptsList;
                        });
                    } else {
                        option._inputOptsList = option._inputOptsList.concat(localOptsList);
                    }
                } else {
                    option.display = 1;
                    // 设置比较操作符
                    if ('string' == option.inputType) {
                        // 对应类型是字符串
                        option._inputOptsList = [];
                        option._inputOptsList.push({"name": '未设值', "value": 4});
                        option._inputOptsList.push({"name": '已设值', "value": 5});
                        option._inputOptsList.push({"name": '包含', "value": 6});
                        option._inputOptsList.push({"name": '不包含', "value": 7});

                    } else if ('number' == option.inputType) {
                        // 对应类型是数值
                        option._inputOptsList = [];
                        option._inputOptsList.push({"name": '未设值', "value": 4});
                        option._inputOptsList.push({"name": '>', "value": 1});
                        option._inputOptsList.push({"name": '=', "value": 2});
                        option._inputOptsList.push({"name": '<', "value": 3});
                    }
                }
            };

            /**
             * 返回选中的数据,如果选中的是groups则返回的group下面所有的product,如果选择的是product,则只返回选中的product
             * @returns {Array}
             */
            function getSelProductList() {
                var selList = [];
                if ($scope.vm.currTab === 'group') {
                    _.forEach($scope.vm.groupSelList.selList, function (info) {
                        selList.push({"id": info.id, "code": info.code});
                        _.forEach(info.prodIds, function (prodInfo) {
                            selList.push({"id": prodInfo.prodId, "code": prodInfo.code});
                        })
                    });
                } else {
                    selList = $scope.vm.productSelList.selList;
                }
                return selList;
            }

            /**
             * 查询指定店铺cart类型下的所有类目(list形式)
             */
            function getCat(cartObj) {
                if (cartObj == null || cartObj == undefined || cartObj == '') {
                    $scope.vm.searchInfo.cartId = 0;
                } else {
                    $scope.vm.searchInfo.cartId = parseInt(cartObj.value);
                }
                // 清空平台相关查询条件
                $scope.vm.searchInfo.productStatus = null;
                $scope.vm.searchInfo.platformStatus = null;
                $scope.vm.searchInfo.pRealStatus = null;
                $scope.vm.searchInfo.hasErrorFlg = null;
                $scope.vm.searchInfo.promotionTagType = 1;
                $scope.vm.searchInfo.promotionTags = null;
                $scope.vm._shopCatValues = null;
                $scope.vm._promotionTags = null;

                $scope.vm.searchInfo.salesType = 'All';
                $scope.vm.searchInfo.salesSortType = null;
                $scope.vm.searchInfo.salesStart = null;
                $scope.vm.searchInfo.salesEnd = null;

                $scope.vm.searchInfo.pCatId = null;
                $scope.vm.searchInfo.pCatPath = null;
                $scope.vm.searchInfo.pCatStatus = null;

                $scope.vm.searchInfo.cidValue = [];
                $scope.vm.searchInfo.shopCatStatus = null;

                $scope.vm.searchInfo.publishTimeStart = null;
                $scope.vm.searchInfo.publishTimeTo = null;
                $scope.vm.searchInfo.priceEnd = null;
                $scope.vm.searchInfo.priceStart = null;
                $scope.vm.searchInfo.priceType = null;
                $scope.vm.searchInfo.priceChgFlg = null;
                $scope.vm.searchInfo.priceDiffFlg = null;
                $scope.vm.searchInfo.propertyStatus = null;
                $scope.vm.searchInfo.pCatPathList = [];

                $scope.vm.masterData.catList = [];


                if ($scope.vm.searchInfo.cartId == 0) {
                    $scope.vm._cart_display = 0;
                    $scope.vm._mmmcart_display = 0;
                    return;
                }
                $scope.vm._cart_display = 1;
                $scope.vm._mmmcart_display = 1;
                if (cartObj.cartType == 3 || cartObj.value == 27) {
                    // 如果是minimall店铺或者是聚美平台，则不显示店铺内分类
                    $scope.vm._mmmcart_display = 0;
                }
                sellerCatService.getCat({"cartId": $scope.vm.searchInfo.cartId, "isTree": false})
                    .then(function (resp) {
                        $scope.vm.masterData.catList = resp.data.catTree;
                    }).then(function () {
                    if ($routeParams.type == 3)
                        $scope.vm.searchInfo.cidValue = $routeParams.value2.split("|");
                });
            }

            /**
             * 检查销量排序的设值
             */
            $scope.chkSalesTypeList = function () {
                if ($scope.vm.searchInfo.sortSalesType == '' || $scope.vm.searchInfo.sortSalesType == undefined) {
                    $scope.vm.searchInfo.sortSales = '';

                }
            };

            /**
             * 添加产品到指定自由标签
             */
            $scope.addFreeTag = function (openFreeTag) {
                _chkProductSel(null, _addFreeTag);

                function _addFreeTag(cartId, selList) {
                    var productIds = [];
                    if (selList && selList.length) {
                        _.forEach(selList, function (object) {
                            productIds.push(object.code);
                        });
                    }
                    openFreeTag({
                        'orgFlg': 2,
                        'tagTypeSel': '4',
                        'cartId': $scope.vm.searchInfo.cartId,
                        'productIds': productIds,
                        'selAllFlg': $scope.vm._selall ? 1 : 0,
                        'searchInfo': $scope.searchInfoBefo
                    }).then(function (res) {
                        // 设置自由标签
                        var msg = '';
                        if (res.selectdTagList && res.selectdTagList.length > 0) {
                            var freeTagsTxt = _.chain(res.selectdTagList).map(function (key, value) {
                                return key.tagPathName;
                            }).value();
                            msg = "将对选定的产品设置自由标签:<br>" + freeTagsTxt.join('; ');
                        } else {
                            msg = "将对选定的产品清空自由标签";
                        }
                        var freeTags = _.chain(res.selectdTagList).map(function (key, value) {
                            return key.tagPath;
                        }).value();
                        confirm(msg)
                            .then(function () {
                                var data = {
                                    "tagPathList": freeTags,
                                    "prodIdList": productIds,
                                    "isSelAll": $scope.vm._selall ? 1 : 0,
                                    "orgDispTagList": res.orgDispTagList,
                                    'searchInfo': $scope.searchInfoBefo
                                };
                                $searchAdvanceService2.addFreeTag(data).then(function () {
                                    notify.success($translate.instant('TXT_MSG_SET_SUCCESS'));
                                    searchAdvanceService2.clearSelList();
                                    $scope.search();
                                })
                            });
                    });

                }
            };

            function openAdvanceImagedetail(item) {
                if (item.common == undefined || item.common.fields == undefined) {
                    return;
                }
                var picList = [];
                for (var i = 1; i <= 9; i++) {
                    if (item.common.fields["images" + i]) {
                        var image = _.map(item.common.fields["images" + i], function (entity) {
                            var imageKeyName = "image" + i;
                            return entity[imageKeyName] != null ? entity[imageKeyName] : "";
                        });
                        picList.push(image);
                    } else {
                        picList.push([""]);
                    }
                }

                this.openImagedetail({'mainPic': picList[0][0], 'picList': picList, 'search': 'master'});
            }

            // 检查是否已勾选商品
            function _chkProductSel(cartId, callback, context) {
                if (cartId == null || cartId == undefined) {
                    // 全平台处理
                    cartId = 0;
                } else {
                    cartId = parseInt(cartId);
                }

                var selList = null;
                if (!$scope.vm._selall) {
                    selList = getSelProductList();
                    if (selList.length == 0) {
                        alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                        return;
                    }
                    callback(cartId, selList, context);
                } else {
                    if ($scope.vm.productPageOption.total == 0) {
                        alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                        return;
                    }
                    confirm("您已启动“检索结果全量”选中机制，本次操作对象为检索结果中的所有产品<h3>修改记录数:&emsp;<span class='label label-danger'>" + $scope.vm.productPageOption.total + "</span></h3>").then(function () {
                        callback(cartId, null, context);
                    });
                }
            }

            // 商品上下架
            $scope._openPutOnOff = function (openPutOnOffFnc, cartId) {
                _chkProductSel(parseInt(cartId), __openPutOnOff);

                function __openPutOnOff(cartId, _selProdList) {
                    var productIds = [];
                    if (_selProdList && _selProdList.length) {
                        _.forEach(_selProdList, function (object) {
                            productIds.push(object.code);
                        });
                    }
                    var property = {
                        'cartId': cartId,
                        '_option': 'putonoff',
                        'productIds': productIds,
                        'searchInfo': $scope.searchInfoBefo
                    };
                    property.isSelAll = $scope.vm._selall ? 1 : 0;
                    openPutOnOffFnc(property).then(
                        function () {
                            $scope.search();
                        });
                }
            };

            // 智能上新
            function openIntelligentPublish(cartId) {
                _chkProductSel(parseInt(cartId), __openIntelligentPublish);

                function __openIntelligentPublish(cartId, _selProdList) {
                    var _confirmMsg = '以下2种属性未完成的商品将被无视，点击【确定】启动智能上新。<br>（1）税号个人&nbsp;（2）平台品牌';

                    confirm(_confirmMsg).then(function () {
                        var productIds = [];
                        if (_selProdList && _selProdList.length) {
                            _.forEach(_selProdList, function (object) {
                                productIds.push(object.code);
                            });
                        }
                        $fieldEditService.intelligentPublish({
                            cartId: cartId,
                            productIds: productIds,
                            isSelectAll: $scope.vm._selall ? 1 : 0,
                            searchInfo: $scope.searchInfoBefo
                        }).then(function () {
                            alert('已完成商品的智能上新！');
                            $scope.search();
                        });
                    });
                }
            }

            /**
             * 商品审核
             * @param openUpdateApprovalFnc
             * @param cartId
             */
            function openApproval(cartId) {
                _chkProductSel(parseInt(cartId), function (cartId, _selProdList) {

                    confirm($translate.instant('TXT_BULK_APPROVAL')).then(function () {
                        var productIds = [];

                        if (_selProdList && _selProdList.length) {
                            _.forEach(_selProdList, function (object) {
                                productIds.push(object.code);
                            });
                        }

                        $fieldEditService.setProductFields({
                            cartId: cartId,
                            _option: 'approval',
                            productIds: productIds,
                            isSelAll: $scope.vm._selall ? 1 : 0,
                            searchInfo: $scope.searchInfoBefo
                        }).then(approveCallback);

                    });
                });

            }

            function approveCallback(res) {
                if (res.data == null || res.data.ecd == null || res.data.ecd == undefined) {
                    alert("提交请求时出现错误");
                    return;
                }
                if (res.data.ecd == 1) {
                    alert("未选择商品，请选择后再操作。");
                    return;
                }
                if (res.data.ecd == 2) {
                    // 存在未ready状态
                    var errMsg;

                    if (res.data.codeList.length > 10) {
                        errMsg = res.data.codeList.slice(0, 9).join('， ') + ' ．．．．．．';
                    } else {
                        errMsg = res.data.codeList.join('， ');
                    }

                    if (res.data.ts)
                        alert("下列商品没有设置税号，无法审批，请修改。以下是商品CODE列表:<br><br>" + errMsg);
                    else
                        alert("下列商品pending状态，无法审批，请修改。以下是商品CODE列表:<br><br>" + errMsg);

                    return;
                }
                if (res.data.ecd == 3) {
                    // 商品价格有问题
                    popups.openUpdateApproval({
                        'resData': res.data,
                        'propertyInfo': property
                    }).then(function (data) {
                        return check(data);
                    });
                }
                $scope.search();
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            }

            // 设置最终售价
            $scope.toopenSalePrice = function (openSalePriceFnc, cartId) {
                _chkProductSel(parseInt(cartId), _toopenSalePrice);

                function _toopenSalePrice(cartId, _selProdList) {
                    var productIds = [];
                    if (_selProdList && _selProdList.length) {
                        _.forEach(_selProdList, function (object) {
                            productIds.push(object.code);
                        });
                    }
                    var property = {'cartId': cartId, '_option': 'saleprice', 'productIds': productIds};
                    property.isSelAll = $scope.vm._selall ? 1 : 0;
                    property.searchInfo = $scope.searchInfoBefo;

                    var config = $scope.vm.masterData.autoApprovePrice["0"];
                    if ($scope.vm.masterData.autoApprovePrice[cartId]) {
                        config = $scope.vm.masterData.autoApprovePrice[cartId];
                    }

                    openSalePriceFnc({'property': property, 'cartList': $scope.vm.cartList, 'config': config}).then(
                        function () {
                            $scope.search();
                        });
                }
            };

            /**
             * popup弹出选择平台数据类目
             * @param popupNewCategory
             */
            function platformCategoryMapping(popCategoryMul) {
                platformMappingService.getPlatformCategories({cartId: $scope.vm.searchInfo.cartId})
                    .then(function (res) {
                        if (!res.data || !res.data.length) {
                            alert("没数据");
                            return null;
                        }

                        if ($scope.vm.searchInfo.pCatPathList != null) {
                            $scope.vm.adVanceCats = _.filter($scope.vm.adVanceCats, function (item) {
                                return $scope.vm.searchInfo.pCatPathList.indexOf(item.catPath) > -1;
                            });
                        } else {
                            $scope.vm.adVanceCats = null;
                        }

                        return popCategoryMul({
                            from: $scope.vm.adVanceCats,
                            categories: res.data
                        });
                    })
                    .then(function (context) {
                        $scope.vm.adVanceCats = context;
                        $scope.vm.searchInfo.pCatPathList = _.map(context, function (item) {
                            return item.catPath;
                        });
                    });
            }

            /**
             * popup弹出选择主类目数据
             * @param popupNewCategory
             */
            function openMasterCategoryMapping(popupCategoryFnc) {
                systemCategoryService.getNewsCategoryList()
                    .then(function (res) {
                        popupCategoryFnc({
                            categories: res.data,
                            from: ""
                        }).then(function (res) {
                            if (!$scope.vm.searchInfo.mCatPath) {
                                $scope.vm.searchInfo.mCatPath = [];
                                $scope.vm.searchInfo.mCatPath.push(res.selected.catPath)
                            } else {
                                if ($scope.vm.searchInfo.mCatPath.indexOf($scope.vm.searchInfo.mCatPath) < 0) {
                                    $scope.vm.searchInfo.mCatPath.push(res.selected.catPath)
                                }
                            }
                        });
                    });
            }

            /**
             * popup弹出选择feed类目数据
             * @param popupNewCategory
             */
            function openFeedCategoryMapping(popCategoryMul) {
                attributeService.getCatTree()
                    .then(function (res) {
                        if (!res.data.categoryTree || !res.data.categoryTree.length) {
                            alert("没数据");
                            return null;
                        }

                        if ($scope.vm.searchInfo.fCatPathList != null) {
                            $scope.vm.feedCats = _.filter($scope.vm.feedCats, function (item) {
                                return $scope.vm.searchInfo.fCatPathList.indexOf(item.catPath) > -1;
                            });
                        } else {
                            $scope.vm.feedCats = null;
                        }

                        return popCategoryMul({
                            categories: res.data.categoryTree,
                            from: $scope.vm.feedCats,
                            divType: "-"
                        }).then(function (context) {
                            $scope.vm.feedCats = context;
                            $scope.vm.searchInfo.fCatPathList = _.map(context, function (item) {
                                return item.catPath;
                            });
                        });
                    });
            }

            /**
             * popup出店铺内分类选择框(查询用)
             * @param openCategoryEdit
             */
            function openChannelInnerCategory(openAddChannelCategoryEdit) {
                openAddChannelCategoryEdit([], $scope.vm.searchInfo.cartId, {'isQuery': true}).then(function (context) {
                    if (_.isArray(context.sellerCats)) {
                        // 设置画面显示用的值
                        var shopCatValues = [];
                        _.forEach(context.sellerCats, function (catObj) {
                            shopCatValues.push(catObj.cName);
                        });
                        $scope.vm._shopCatValues = shopCatValues;

                        // 设置查询用的参数
                        var cidValue = [];
                        _.forEach(context.sellerCats, function (catObj) {
                            cidValue.push(catObj.cId);
                        });
                        $scope.vm.searchInfo.cidValue = cidValue;
                    }
                })
            }

            /**
             * popup出添加店铺内分类的对话框（批量追加用）
             * @param openCategoryEdit
             */
            function openAddChannelCategoryFromAdSearch(openAddChannelCategoryEdit, cartId) {
                _chkProductSel(cartId, _openAddChannelCategory, {
                    'isSelAll': $scope.vm._selall ? 1 : 0,
                    "searchInfo": $scope.searchInfoBefo
                });

                function _openAddChannelCategory(cartId, selList, context) {
                    openAddChannelCategoryEdit(selList, cartId, context).then(function (res) {
                        var productIds = [];
                        _.forEach(selList, function (object) {
                            productIds.push(object.code);
                        });
                        res.productIds = productIds;
                        res.isSelAll = $scope.vm._selall ? 1 : 0;
                        res.searchInfo = $scope.searchInfoBefo;
                        $addChannelCategoryService.save(res).then(function () {
                            notify.success($translate.instant('TXT_SUBMIT_SUCCESS'));
                            $scope.search();
                        });
                    })
                }
            }

            /**
             * popup出选择Tag的功能（包括活动标签和自由标签）
             * @param openFreeTag
             */
            function openTagManagement(openFreeTag, isPromoTag) {
                openFreeTag.then(function (res) {
                    if (isPromoTag) {
                        // 查询活动标签
                        $scope.vm._promotionTags = res.selectdTagList;
                        $scope.vm.searchInfo.promotionTags = _.chain(res.selectdTagList).map(function (key) {
                            return key.tagPath;
                        }).value();
                    } else {
                        // 查询自由标签
                        $scope.vm._freeTags = res.selectdTagList;
                        $scope.vm.searchInfo.freeTags = _.chain(res.selectdTagList).map(function (key) {
                            return key.tagPath;
                        }).value();
                    }
                });
            }

            // 选中‘未设置’，清除输入框中的值
            $scope.chkSelStatus = function (stsType) {
                if (stsType == 1) {
                    $scope.vm.searchInfo.pCatPath = '';
                    $scope.vm.searchInfo.pCatId = '';
                } else if (stsType == 2) {
                    $scope.vm._shopCatValues = [];
                    $scope.vm.searchInfo.cidValue = [];
                }
            };

            // 清空所填项目
            function dismiss(item) {
                switch (item) {
                    case 'mCatPath':
                        $scope.vm.searchInfo.mCatPath = null;
                        $scope.vm.searchInfo.mCatId = null;
                        break;
                    case 'fCatPath':
                        $scope.vm.searchInfo.fCatPath = null;
                        $scope.vm.searchInfo.fCatId = null;
                        break;
                    case 'freeTag':
                        $scope.vm._freeTags = null;
                        $scope.vm.searchInfo.freeTags = null;
                        break;
                    case 'pCatStatus':
                        $scope.vm.searchInfo.pCatPath = null;
                        $scope.vm.searchInfo.pCatId = null;
                        break;
                    case 'shopCat':
                        $scope.vm._shopCatValues = null;
                        $scope.vm.searchInfo.cidValue = null;
                        break;
                    case 'promotionTag':
                        $scope.vm._promotionTags = null;
                        $scope.vm.searchInfo.promotionTags = null;
                        break;
                    default:
                        $scope.vm.searchInfo[item] = null;
                        break;
                }
            }

            // 查询数据文件创建的状态
            function exportSearch(page) {
                $scope.vm.exportPageOption.curr = !page ? $scope.vm.exportPageOption.curr : page;

                $searchAdvanceService2.exportSearch({
                    "pageNum": $scope.vm.exportPageOption.curr,
                    "pageSize": $scope.vm.exportPageOption.size
                }).then(function (res) {
                    $scope.vm.exportList = res.data.exportList;
                    _.each($scope.vm.exportList, function (item) {
                        item.fileName = item.fileName.split(",");
                    });
                    $scope.vm.exportPageOption.total = res.data.exportListTotal;
                })
            }

            // 下载已创建完成的数据文件
            $scope.openDownload = function (fileName, status) {
                if (status == -1) {
                    alert("文件已经过期，请重新下载");
                    return;
                }
                function _exportFileCallback(res) {
                    var obj = JSON.parse(res);
                    if (obj.code == '4004') {
                        alert("此文件不存在");
                    }
                }

                $.download.post(cActions.cms.search.$searchAdvanceService2.root + cActions.cms.search.$searchAdvanceService2.exportDownload, {"fileName": fileName}, _exportFileCallback);
            };

            // 指导价变更批量确认
            $scope.cfmRetailPrice = function (cartObj) {
                var cartIdVal = 0;
                if (cartObj != undefined && cartObj != null) {
                    cartIdVal = cartObj.value;
                }
                _chkProductSel(parseInt(cartIdVal), __cfmRetailPrice);

                function __cfmRetailPrice(cartId, _selProdList) {
                    var msg = "";
                    if (cartId == 0) {
                        msg = "即将对选中的商品全店铺批量确认指导价变更";
                    } else {
                        msg = "即将对选中的商品批量确认指导价变更";
                    }

                    confirm(msg).then(function () {
                        var productIds = [];
                        if (_selProdList && _selProdList.length) {
                            _.forEach(_selProdList, function (object) {
                                productIds.push(object.code);
                            });
                        }
                        var property = {'cartId': cartId, '_option': 'retailprice', 'productIds': productIds};
                        property.isSelAll = $scope.vm._selall ? 1 : 0;
                        property.searchInfo = $scope.searchInfoBefo;
                        $fieldEditService.setProductFields(property).then(function (res) {
                            if (res.data == null || res.data.ecd == null || res.data.ecd == undefined) {
                                alert($translate.instant('TXT_COMMIT_ERROR'));
                                return;
                            }
                            if (res.data.ecd == 1) {
                                // 未选择商品
                                alert($translate.instant('未选择商品，请选择后再操作'));
                                return;
                            }
                            $scope.search();
                            notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        });
                    });
                }
            };

            /**
             @description 类目popup
             * @param productInfo
             * @param popupNewCategory popup实例
             */
            function jdCategoryMapping(cartId) {
                _chkProductSel(null, _openAddPromotion, {"cartId": cartId, "selList": []});

                function _openAddPromotion(cartId, selList, context) {

                    if (selList && selList.length > 0) {
                        selList.forEach(function (item) {
                            context.selList.push(item.code);
                        })
                    }
                    productDetailService.getPlatformCategories({"cartId": context.cartId})
                        .then(function (res) {
                            return $q(function (resolve, reject) {
                                if (!res.data || !res.data.length) {
                                    notify.danger("数据还未准备完毕");
                                    reject("数据还未准备完毕");
                                } else {
                                    resolve(popups.popupNewCategory({
                                        //' from: scope.vm.platform == null ? "" : scope.vm.platform.pCatPath,
                                        categories: res.data,
                                        divType: ">",
                                        plateSchema: true
                                    }));
                                }
                            });
                        }).then(function (data) {

                        confirm("将要批量更新商品类目，是否确认？").then(function () {
                            $fieldEditService.bulkSetCategory({
                                'isSelAll': $scope.vm._selall ? 1 : 0,
                                "productIds": context.selList,
                                "cartId": +context.cartId,
                                "pCatPath": data.selected.catPath,
                                "pCatId": data.selected.catId,
                                "searchInfo": $scope.searchInfoBefo
                            }).then(function () {
                                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                                $scope.search();
                            });
                        });

                    });

                }
            }

            function editPlatformAttribute(cartId) {
                _chkProductSel(null, _openAddPromotion, {"cartId": cartId, "selList": []});

                function _openAddPromotion(cartId, selList, context) {

                    if (selList && selList.length > 0) {
                        selList.forEach(function (item) {
                            context.selList.push(item.code);
                        })
                    }
                    popups.popupPlatformPopOptions({
                        productIds: context.selList,
                        isSelAll: $scope.vm._selall,
                        cartId: context.cartId,
                        searchInfo: $scope.searchInfoBefo
                    });

                }
            }

            // 重新计算价格（指导价）
            $scope.refreshRetailPrice = function (cartObj) {
                var cartIdVal = 0;
                if (cartObj != undefined && cartObj != null) {
                    cartIdVal = cartObj.value;
                }
                _chkProductSel(parseInt(cartIdVal), __refreshRetailPrice);

                function __refreshRetailPrice(cartId, _selProdList) {
                    var msg = "";
                    if (cartId == 0) {
                        msg = "即将对选中的商品(全店铺)批量重新计算指导售价.";
                    } else {
                        msg = "即将对选中的商品(" + cartObj.name + ")批量重新计算指导售价.";
                    }
                    if ($scope.vm.masterData.autoApprovePrice == '1') {
                        msg += "<br>自动同步到最终售价.";
                    } else {
                        msg += "<br>不同步到最终售价.";
                    }

                    confirm(msg).then(function () {
                        var productIds = [];
                        if (_selProdList && _selProdList.length) {
                            _.forEach(_selProdList, function (object) {
                                productIds.push(object.code);
                            });
                        }
                        var property = {
                            'cartId': cartId,
                            '_option': 'refreshRetailPrice',
                            'productIds': productIds,
                            'searchInfo': $scope.searchInfoBefo
                        };
                        property.isSelAll = $scope.vm._selall ? 1 : 0;
                        $fieldEditService.setProductFields(property).then(function (res) {
                            if (res.data == null || res.data.ecd == null || res.data.ecd == undefined) {
                                alert($translate.instant('TXT_COMMIT_ERROR'));
                                return;
                            }
                            if (res.data.ecd == 1) {
                                // 未选择商品
                                alert($translate.instant('未选择商品，请选择后再操作'));
                                return;
                            }
                            $scope.search();
                            notify.success($translate.instant('TXT_SUBMIT_SUCCESS'));
                        });
                    });
                }
            };

            /**
             * 高级检索加入活动
             */
            $scope.popJoinPromotion = function (cartBean) {

                _chkProductSel(cartBean.value, function (cartId, selList, context) {
                    popups.openJoinPromotion(_.extend({
                        cartBean: cartBean,
                        selList: selList,
                        searchInfo: $scope.searchInfoBefo
                    }, context)).then(function (context) {

                    });
                }, {'isSelAll': $scope.vm._selall ? 1 : 0});


            };

            $scope.getAutoSyncPriceSale = function (cartBean) {

                var configValue1 = null;
                if ($scope.vm.masterData.autoApprovePrice) {
                    if ($scope.vm.masterData.autoApprovePrice[cartBean.value]) {
                        configValue1 = $scope.vm.masterData.autoApprovePrice[cartBean.value].configValue1;
                    }

                    if (!configValue1 && $scope.vm.masterData.autoApprovePrice[0]) {
                        configValue1 = $scope.vm.masterData.autoApprovePrice[0].configValue1;
                    }

                    if ("1" == configValue1) return true;
                } else {
                    return false;
                }

            };

            /**
             * 客户建议售价变更确认
             * @param cartObj 平台信息
             */
            $scope.clientMsrpConfirm = function () {
                _chkProductSel('0', function (cartId, _selProdList) {

                    confirm('即将对选中的商品全店铺批量确认指导价变更').then(function () {

                        $fieldEditService.bulkConfClientMsrp({
                            productIds: _.pluck(_selProdList, 'code'),
                            isSelAll: $scope.vm._selall ? 1 : 0,
                            searchInfo: $scope.searchInfoBefo
                        }).then(function (res) {

                            if (res.data) {
                                $scope.search();
                                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            }

                        });

                    });

                });
            };

            $scope.canPartAprroval = function (cartId) {
                return [23, 20, 24, 26, 27, 30, 31].indexOf(Number(cartId)) > -1;
            };

            $scope.isExistCommonProps = function (propId) {
                return _.some($scope.vm.commonProps, function (item) {
                    return item.propId == propId;
                })
            };

            $scope.commonPropsFilter = function (item) {
                return searchAdvanceService2.commonFilter.indexOf(item.propId) < 0;

            };

            /**
             * 为单商品添加自由标签
             * @param productInfo 产品信息
             */
            $scope.addProductFreeTag = function (productInfo) {
                var prodId = productInfo.prodId;
                var productCodes = [productInfo.common.fields.code];
                popups.openFreeTag({
                    'orgFlg': 2,
                    'tagTypeSel': '4',
                    'cartId': $scope.vm.searchInfo.cartId,
                    'productIds': productCodes,
                    'selAllFlg': 0
                }).then(function (res) {
                    // 设置自由标签
                    var msg = '';
                    if (res.selectdTagList && res.selectdTagList.length > 0) {
                        var freeTagsTxt = _.chain(res.selectdTagList).map(function (key, value) {
                            return key.tagPathName;
                        }).value();
                        msg = "将对选定的产品设置自由标签:<br>" + freeTagsTxt.join('; ');
                    } else {
                        msg = "将对选定的产品清空自由标签";
                    }
                    var freeTags = _.chain(res.selectdTagList).map(function (key, value) {
                        return key.tagPath;
                    }).value();
                    var _freeTagsInfo = _.chain(res.selectdTagList).map(function (key, value) {
                        return key.tagPathName;
                    }).value();
                    confirm(msg)
                        .then(function () {
                            var data = {
                                "tagPathList": freeTags,
                                "prodIdList": productCodes,
                                "isSelAll": 0,
                                "orgDispTagList": res.orgDispTagList,
                                "singleProd": 1 // 是否是单一针对商品进行自由标签编辑
                            };
                            $searchAdvanceService2.addFreeTag(data).then(function () {

                                productInfo.freeTags = freeTags;
                                productInfo._freeTagsInfo = _freeTagsInfo;

                                notify.success($translate.instant('TXT_MSG_SET_SUCCESS'));
                                searchAdvanceService2.clearSelList();
                            })
                        });
                });
            };

            /**
             * 部分上新操作
             * @param cartInfo 平台信息
             */
            $scope.batchLoadAttr = function (cartInfo) {

                _chkProductSel(cartInfo.value, function (cartId, _selProdList) {
                    var attribute = [];
                    switch (Number(cartInfo.value)) {
                        case 20:
                        case 23:
                            attribute = ['description', 'title', 'item_images', 'seller_cids', 'sell_points', 'wireless_desc'];
                            break;
                        case 24:
                        case 26:
                            attribute = ['description', 'title', 'seller_cids'];
                            break;
                        case 27:
                            attribute = ['description', 'title', 'item_images'];
                            break;
                        case 30:
                        case 31:
                            attribute = ['description', 'title', 'item_images', 'seller_cids'];
                            break;
                    }

                    popups.openLoadAttribute({
                        attribute: attribute
                    }).then(function (res) {
                        $scope.approveAttr = null;
                        $scope.approveAttr = res;

                        $fieldEditService.setProductFields({
                            cartId: cartId,
                            _option: 'partApproval',
                            productIds: _.pluck(_selProdList, 'code'),
                            isSelAll: $scope.vm._selall ? 1 : 0,
                            searchInfo: $scope.searchInfoBefo,
                            platformWorkloadAttributes: $scope.approveAttr
                        }).then(approveCallback);

                    });

                });

            }

        }
    );

});
