/**
 * Created by sofia on 6/8/2016.
 */
/**
 * Created by linanbin on 15/12/7.
 */
define([
    'underscore',
    'modules/cms/controller/popup.ctl',
    'modules/cms/directives/keyValue.directive',
    'modules/cms/service/search.advance2.service',
    'modules/cms/service/product.detail.service'
], function (_) {

    function searchIndex($scope, $routeParams, searchAdvanceService2, $fieldEditService, feedMappingService, productDetailService, channelTagService, confirm, $translate, notify, alert, sellerCatService, platformMappingService, attributeService) {

        $scope.vm = {
            searchInfo: {
                compareType: null,
                brand: null,
                tags: [],
                priceChgFlg: '0',
                priceDiffFlg: '0',
                tagTypeSelectValue: '0',
                promotionList: [],
                catgoryList: [],
                cidValue: [],
                _selall: false
            },
            groupPageOption: {curr: 1, total: 0, fetch: getGroupList},
            productPageOption: {curr: 1, total: 0, fetch: getProductList},
            groupList: [],
            productList: [],
            currTab: "product",
            status: {
                open: true
            },
            groupSelList: {selList: []},
            productSelList: {selList: []},
            custAttrList: [],
            sumCustomProps: [],
            platform: {catPath: null},
            masterCat: {catPath: null},
            feedCat: {catPath: null},
            channelInner: {catPath: null},
            promotion: {tagPathList: null},
            free: {tagPathList: null}
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = function () {
            //$scope.vm.status.open = false;//收缩搜索栏
            search();
        };
        $scope.exportFile = exportFile;
        $scope.getGroupList = getGroupList;
        $scope.getProductList = getProductList;
        $scope.openCategoryMapping = openCategoryMapping;
        $scope.openMasterCategoryMapping = openMasterCategoryMapping;
        $scope.openFeedCategoryMapping = openFeedCategoryMapping;
        $scope.openChannelInnerCategory = openChannelInnerCategory;
        $scope.bindCategory = bindCategory;
        $scope.add = addCustAttribute;
        $scope.del = delCustAttribute;
        $scope.openAddPromotion = openAddPromotion;
        $scope.openAddChannelCategoryFromAdSearch = openAddChannelCategory;
        $scope.openJMActivity = openJMActivity;
        $scope.openBulkUpdate = openBulkUpdate;
        $scope.getTagList = getTagList;
        $scope.getCat = getCat;
        $scope.addFreeTag = addFreeTag;
        $scope.openAdvanceImagedetail = openAdvanceImagedetail;
        $scope.openApproval = openApproval;
        $scope.platformCategoryMapping = platformCategoryMapping;
        $scope.openTagManagement = openTagManagement;
        /**
         * 初始化数据.
         */
        function initialize() {
            // 如果来至category 或者 header的检索,将初始化检索条件

            if ($routeParams.type == "1") {
                $scope.vm.searchInfo.catPath = decodeURIComponent($routeParams.value);
            } else if ($routeParams.type == "2") {
                $scope.vm.searchInfo.codeList = $routeParams.value;
            }
            searchAdvanceService2.init().then(function (res) {
                    $scope.vm.masterData = res.data;
                    $scope.vm.promotionList = _.where(res.data.promotionList, {isAllPromotion: 0});
                    $scope.vm.custAttrList.push({inputVal: "", inputOpts: "", inputOptsKey: ""});
                    $scope.vm.cartList = res.data.cartList;
                })
                .then(function () {
                    // 如果来至category 或者header search 则默认检索
                    $scope.vm.tblWidth = '100%'; // group tab的原始宽度
                    $scope.vm.tblWidth2 = '100%'; // product tab的原始宽度
                    if ($routeParams.type == "3") {
                        var catObj = _.find($scope.vm.masterData.cartList, function (item) {
                            return item.add_name2 == $routeParams.catType;
                        });
                        $scope.vm.searchInfo.cartId = catObj.value;
                        getCat();
                    }
                    if ($routeParams.type != undefined) {
                        search();
                    }
                })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear() {
            $scope.vm.searchInfo = {
                compareType: null,
                brand: null,
                tags: [],
                priceChgFlg: '0',
                priceDiffFlg: '0',
                tagTypeSelectValue: '0',
                cidValue: [],
                _selall: false
            };
            $scope.vm.masterData.tagList = [];
            $scope.vm.masterData.catList = [];
            $scope.vm.custAttrList = [{inputVal: "", inputOpts: ""}];
        }

        /**
         * 检索
         */
        function search() {
            // 默认设置成第一页
            $scope.vm.groupPageOption.curr = 1;
            $scope.vm.productPageOption.curr = 1;

            $scope.vm.searchInfo.custAttrMap = angular.copy($scope.vm.custAttrList);
            searchAdvanceService2.search($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.productPageOption).then(function (res) {
                $scope.vm.customProps = res.data.customProps;
                var sumCustomProps = [];
                _.forEach($scope.vm.customProps, function (data) {
                    sumCustomProps.push(data.feed_prop_translation)
                    sumCustomProps.push(data.feed_prop_original)
                });
                $scope.vm.sumCustomProps = sumCustomProps;
                $scope.vm.commonProps = res.data.commonProps;
                $scope.vm.selSalesType = res.data.selSalesType;

                $scope.vm.groupList = res.data.groupList;
                $scope.vm.groupPageOption.total = res.data.groupListTotal;
                $scope.vm.groupSelList = res.data.groupSelList;
                $scope.vm.productList = res.data.productList;
                $scope.vm.productPageOption.total = res.data.productListTotal;
                $scope.vm.productSelList = res.data.productSelList;
                for (idx in res.data.freeTagsList) {
                    var prodObj = $scope.vm.productList[idx];
                    prodObj._freeTagsInfo = res.data.freeTagsList[idx];
                }
                // 计算表格宽度
                $scope.vm.tblWidth = (($scope.vm.commonProps.length + $scope.vm.sumCustomProps.length) * 120 + $scope.vm.selSalesType.length * 100 + 980) + 'px';
                $scope.vm.tblWidth2 = (($scope.vm.commonProps.length + $scope.vm.sumCustomProps.length) * 120 + $scope.vm.selSalesType.length * 115 + 1100) + 'px';
            })
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
            if (fileType == 1) {
                msg = '即将导出Code级的搜索结果，请确认。';
            } else if (fileType == 2) {
                msg = '即将导出Group级的搜索结果，请确认。';
            } else if (fileType == 3) {
                msg = '即将导出SKU级的搜索结果，请确认。';
            }
            confirm(msg).result
                .then(function () {
                    $scope.vm.searchInfo.fileType = fileType;
                    searchAdvanceService2.exportFile($scope.vm.searchInfo);
                });
        }

        /**
         * 分页处理group数据
         */
        function getGroupList() {
            searchAdvanceService2.getGroupList($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.groupSelList, $scope.vm.commonProps, $scope.vm.customProps, $scope.vm.selSalesType)
                .then(function (res) {
                    $scope.vm.groupList = res.data.groupList == null ? [] : res.data.groupList;
                    $scope.vm.groupPageOption.total = res.data.groupListTotal;
                    $scope.vm.groupSelList = res.data.groupSelList;
                });
        }

        /**
         * 分页处理product数据
         */
        function getProductList() {
            searchAdvanceService2.getProductList($scope.vm.searchInfo, $scope.vm.productPageOption, $scope.vm.productSelList, $scope.vm.commonProps, $scope.vm.customProps, $scope.vm.selSalesType)
                .then(function (res) {
                    $scope.vm.productList = res.data.productList == null ? [] : res.data.productList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productSelList = res.data.productSelList;
                    for (idx in res.data.freeTagsList) {
                        var prodObj = $scope.vm.productList[idx];
                        prodObj._freeTagsInfo = res.data.freeTagsList[idx];
                    }
                });
        }

        /**
         * popup出添加到promotion的功能
         * @param promotion
         * @param openAddToPromotion
         */
        function openAddPromotion(promotion, openAddToPromotion) {
            var selList = getSelProductList();
            if (selList.length == 0) {
                alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                return;
            }
            openAddToPromotion(promotion, selList).then(function () {
                searchAdvanceService2.clearSelList();
                getGroupList();
                getProductList();
            })
        }

        /**
         * popup出添加到CategoryEdit的功能
         * @param openCategoryEdit
         */
        function openAddChannelCategory(openAddChannelCategoryEdit) {
            _chkProductSel(null, _openAddChannelCategory);

            function _openAddChannelCategory(cartId, selList) {
                openAddChannelCategoryEdit(selList, cartId).then(function () {
                    getGroupList();
                    getProductList();
                })
            }
        }

        /**
         * popup出添加到聚美Promotion的功能
         * @param promotion
         * @param openJMActivity
         */
        function openJMActivity(promotion, openJMActivity) {
            openJMActivity(promotion, getSelProductList()).then(function () {
                searchAdvanceService2.clearSelList();
                getGroupList();
                getProductList();
            })
        }

        /**
         * popup出批量修改产品的field属性
         * @param openFieldEdit
         */
        function openBulkUpdate(openFieldEdit) {
            _chkProductSel(null, _openBulkUpdate);

            function _openBulkUpdate(cartId, selList) {
                openFieldEdit(selList).then(function () {
                    searchAdvanceService2.clearSelList();
                    getGroupList();
                    getProductList();
                })
            }
        }

        /**
         * popup弹出切换主数据类目
         * @param popupNewCategory
         */
        function openCategoryMapping(popupNewCategory) {
            _chkProductSel(null, _openCategoryMapping);

            function _openCategoryMapping(cartId, selList) {
                feedMappingService.getMainCategories()
                    .then(function (res) {
                        popupNewCategory({
                            categories: res.data,
                            from: null
                        }).then(function (res) {
                                bindCategory(res, selList)
                            }
                        );
                    });
            }
        }

        /**
         * 类目变更
         */
        function bindCategory(context, selList) {
            confirm($translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY')).result
                .then(function () {
                    var productIds = [];
                    if (selList) {
                        _.forEach(selList, function (object) {
                            productIds.push(object.id);
                        });
                    }
                    var data = {
                        prodIds: productIds,
                        catId: context.selected.catId,
                        catPath: context.selected.catPath
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
         * 添加新search选项
         */
        function addCustAttribute() {
            if ($scope.vm.custAttrList.length < 5) {
                $scope.vm.custAttrList.push({inputOptsKey: "", inputOpts: "", inputVal: ""});
            } else {
                alert("最多只能添加5项")
            }
        }

        function delCustAttribute(idx) {
            if ($scope.vm.custAttrList.length > 1) {
                $scope.vm.custAttrList.splice(idx, 1);
            } else {
                alert("最少保留一项")
            }
        }

        // 当选择搜索时设置输入框
        $scope.setSelValue = function (option, custAtts) {
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
                    searchAdvanceService2.getCustSearchList(option.inputOptsKey, option.inputType).then(function (res) {
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
         * 查询指定标签类型下的所有标签(list形式)
         */
        function getTagList() {
            if ($scope.vm.searchInfo.tagTypeSelectValue == '0' || $scope.vm.searchInfo.tagTypeSelectValue == '' || $scope.vm.searchInfo.tagTypeSelectValue == undefined) {
                $scope.vm.masterData.tagList = [];
                return;
            }
            channelTagService.getTagList({'tagTypeSelectValue': $scope.vm.searchInfo.tagTypeSelectValue})
                .then(function (res) {
                    $scope.vm.masterData.tagList = res.data;
                });
        }

        /**
         * 查询指定店铺cart类型下的所有类目(list形式)
         */
        function getCat(cartObj) {
            if (cartObj == null || cartObj == undefined || cartObj == '') {
                $scope.vm.searchInfo.cartId = -1;
            } else {
                $scope.vm.searchInfo.cartId = parseInt(cartObj.value);
            }
            // 清空平台相关查询条件
            $scope.vm.searchInfo.productStatus = null;
            $scope.vm.searchInfo.platformStatus = null;
            $scope.vm.searchInfo.errorListStatus = null;

            $scope.vm.searchInfo.promotionList = null;

            $scope.vm.searchInfo.tags = [];
            $scope.vm.searchInfo.priceChgFlg = '0';
            $scope.vm.searchInfo.tagTypeSelectValue = '0';
            $scope.vm.searchInfo.sortSales = '0';
            $scope.vm.searchInfo.salesSortType = null;
            $scope.vm.searchInfo.cidValue = [];

            $scope.vm.searchInfo.priceEnd = '';
            $scope.vm.searchInfo.priceStart = '';
            $scope.vm.searchInfo.priceType = '';
            $scope.vm.searchInfo.createTimeStart = '';
            $scope.vm.searchInfo.createTimeTo = '';

            $scope.vm.masterData.catList = [];

            if ($scope.vm.searchInfo.cartId == -1) {
                $scope.vm._cart_display = 0;
                $scope.vm._mmmcart_display = 0;
                return;
            }
            $scope.vm._cart_display = 1;
            $scope.vm._mmmcart_display = 1;
            if (cartObj.ismm || cartObj.value == 27) {
                // 如果是minimall店铺或者是聚美平台，则不显示店铺内分类
                $scope.vm._mmmcart_display = 0;
            }
            sellerCatService.getCat({"cartId": $scope.vm.searchInfo.cartId, "isTree": false})
                .then(function (resp) {
                    $scope.vm.masterData.catList = resp.data.catTree;
                }).then(function () {
                if ($routeParams.type == 3)
                    $scope.vm.searchInfo.cidValue = $routeParams.value.split("|");
            });
        }

        /**
         * 检查销量排序的设值
         */
        $scope.chkSalesTypeList = function () {
            if ($scope.vm.searchInfo.sortSalesType == '' || $scope.vm.searchInfo.sortSalesType == undefined) {
                $scope.vm.searchInfo.sortSales = '';
                return;
            }
        };

        /**
         * 添加产品到指定自由标签
         */
        function addFreeTag(tagBean) {
            _chkProductSel(null, _addFreeTag);

            function _addFreeTag(cartId, selList) {
                var productIds = [];
                if (selList && selList.length) {
                    _.forEach(selList, function (object) {
                        productIds.push(object.id);
                    });
                }

                confirm("将对选定的产品添加自由标签" + tagBean.tagPathName).result
                    .then(function () {
                        searchAdvanceService2.addFreeTag(tagBean.tagPath, productIds).then(function () {
                            notify.success($translate.instant('TXT_MSG_SET_SUCCESS'));
                            searchAdvanceService2.clearSelList();
                            getGroupList();
                            getProductList();
                        })
                    });
            }
        }

        function openAdvanceImagedetail(item) {
            var picList = [];
            for (var attr in item.commom.fields) {
                if (attr.indexOf("images") >= 0) {
                    var image = _.map(item.common.fields[attr], function (entity) {
                        var imageKeyName = "image" + attr.substring(6, 7);
                        return entity[imageKeyName] != null ? entity[imageKeyName] : "";
                    });
                    picList.push(image);
                }
            }
            this.openImagedetail({'mainPic': picList[0][0], 'picList': picList, 'search': 'master'});
        }

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
                confirm('即将对检索结果全量进行处理，总共商品数为 ' + $scope.vm.productPageOption.total).result.then(function () {
                    callback(cartId, null, context);
                });
            }
        }

        // 商品上下架
        $scope._openPutOnOff = function (openPutOnOffFnc, cartId) {
            _chkProductSel(cartId, __openPutOnOff);

            function __openPutOnOff(cartId, _selProdList) {
                var productIds = [];
                if (_selProdList && _selProdList.length) {
                    _.forEach(_selProdList, function (object) {
                        productIds.push(object.code);
                    });
                }
                var property = {'cartId': cartId, '_option': 'putonoff', 'productIds': productIds};
                property.isSelAll = $scope.vm._selall ? 1 : 0;
                openPutOnOffFnc(property).then(
                    function () {
                        $scope.search();
                    });
            }
        };

        // 商品审批
        function openApproval(openUpdateApprovalFnc, cartId) {
            _chkProductSel(cartId, __openApproval);

            function __openApproval(cartId, _selProdList) {
                confirm($translate.instant('TXT_BULK_APPROVAL')).result
                    .then(function () {
                        var productIds = [];
                        if (_selProdList && _selProdList.length) {
                            _.forEach(_selProdList, function (object) {
                                productIds.push(object.code);
                            });
                        }
                        var property = {'cartId': cartId, '_option': 'approval', 'productIds': productIds};
                        property.isSelAll = $scope.vm._selall ? 1 : 0;

                        function check(propParams) {
                            return $fieldEditService.setProductFields(propParams).then(callback);
                        }

                        function callback(res) {
                            if (res.data.ecd == null || res.data.ecd == undefined) {
                                alert("提交请求时出现错误");
                                return;
                            }
                            if (res.data.ecd == 1) {
                                // 存在未ready状态
                                alert("未选择商品，请选择后再操作。");
                                return;
                            }
                            if (res.data.ecd == 2) {
                                // 存在未ready状态
                                alert("下列商品不是ready状态，无法审批，请修改。以下是商品CODE列表:<br><br>" + res.data.codeList.join('， '));
                                return;
                            }
                            if (res.data.ecd == 3) {
                                // 商品价格有问题
                                return openUpdateApprovalFnc({
                                    'resData': res.data,
                                    'propertyInfo': property
                                }).then(function (data) {
                                    return check(data);
                                });
                            }
                            $scope.search();
                            notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        }

                        check(property);
                    });
            }
        }

        /**
         * popup弹出选择平台数据类目
         * @param popupNewCategory
         */
        function platformCategoryMapping(popupNewCategory) {
            platformMappingService.getPlatformCategories({cartId: $scope.vm.searchInfo.cartId})
                .then(function (res) {
                    if (!res.data || !res.data.length) {
                        alert("没数据");
                        return null;
                    }
                    return popupNewCategory({
                        from: "",
                        categories: res.data
                    });
                }).then(function (context) {
                $scope.vm.searchInfo.pCatPath = context.selected.catPath;
                $scope.vm.searchInfo.pCatId = context.selected.catId;
            });
        }

        /**
         * popup弹出选择主类目数据
         * @param popupNewCategory
         */
        function openMasterCategoryMapping(popupNewCategory) {
            feedMappingService.getMainCategories()
                .then(function (res) {
                    popupNewCategory({
                        categories: res.data,
                        from: null
                    }).then(function (res) {
                            $scope.vm.searchInfo.mCatPath = res.selected.catPath;
                            $scope.vm.searchInfo.mCatId = res.selected.catId;
                        }
                    );
                });
        }

        /**
         * popup弹出选择feed类目数据
         * @param popupNewCategory
         */
        function openFeedCategoryMapping(popupNewCategory) {
            attributeService.getCatTree()
                .then(function (res) {
                    if (!res.data.categoryTree || !res.data.categoryTree.length) {
                        alert("没数据");
                        return null;
                    }
                    return popupNewCategory({
                        categories: res.data.categoryTree,
                        from: ""
                    }).then(function (context) {
                            $scope.vm.searchInfo.fCatPath = context.selected.catPath;
                            $scope.vm.searchInfo.fCatId = context.selected.catId;
                        }
                    );
                });
        }

        /**
         * popup出添加到CategoryEdit的功能
         * @param openCategoryEdit
         */
        function openChannelInnerCategory(openAddChannelCategoryEdit) {
            var selList = getSelProductList();
            if ($scope.vm.currTab === 'group') {
                selList = $scope.vm.groupSelList.selList;
            } else {
                selList = $scope.vm.productSelList.selList;
            }
            openAddChannelCategoryEdit(selList).then(function (context) {
                getGroupList();
                getProductList();
                $scope.vm.channelInner.catPath = context.catPath;
            })
        }

        /**
         * popup出选择Tag的功能
         * @param openFreeTag
         */
        function openTagManagement(openFreeTag, isPromoTag) {
            openFreeTag.then(function (res) {
                isPromoTag ? $scope.vm.promotion.tagPathList = res.selectdTagList : $scope.vm.free.tagPathList = res.selectdTagList;
            });
        }
    }

    searchIndex.$inject = ['$scope', '$routeParams', 'searchAdvanceService2', '$fieldEditService', 'feedMappingService', '$productDetailService', 'channelTagService', 'confirm', '$translate', 'notify', 'alert', 'sellerCatService', 'platformMappingService', 'attributeService'];
    return searchIndex;
});
