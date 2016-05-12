/**
 * Created by linanbin on 15/12/7.
 */
define([
    'underscore',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/search.advance.service'
], function (_) {

    function searchIndex($scope, $routeParams, searchAdvanceService, feedMappingService, productDetailService, channelTagService, confirm, $translate, notify, alert) {

        $scope.vm = {
            searchInfo: {
                compareType: null,
                brand: null,
                tags:[],
                priceChgFlg: '0',
                priceDiffFlg: '0',
                tagTypeSelectValue: '0'
            },
            groupPageOption: {curr: 1, total: 0, fetch: getGroupList},
            productPageOption: {curr: 1, total: 0, fetch: getProductList},
            groupList: [],
            productList: [],
            currTab: "group",
            status: {
                open: true
            },
            groupSelList: { selList: []},
            productSelList: { selList: []},
            custAttrList: []
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = function(){
            $scope.vm.status.open = false;//收缩搜索栏
            search();
        };
        $scope.exportFile = exportFile;
        $scope.getGroupList = getGroupList;
        $scope.getProductList = getProductList;
        $scope.openCategoryMapping = openCategoryMapping;
        $scope.bindCategory = bindCategory;
        $scope.add = addCustAttribute;
        $scope.del = delCustAttribute;
        $scope.openAddPromotion = openAddPromotion;
        $scope.openJMActivity = openJMActivity;
        $scope.openBulkUpdate = openBulkUpdate;
        $scope.getTagList = getTagList;
        $scope.addFreeTag = addFreeTag;

        /**
         * 初始化数据.
         */
        function initialize () {
            // 如果来至category 或者 header的检索,将初始化检索条件

            if ($routeParams.type == "1") {
                $scope.vm.searchInfo.catPath = decodeURIComponent($routeParams.value);
            } else if ($routeParams.type == "2") {
                $scope.vm.searchInfo.codeList = $routeParams.value;
            }
            searchAdvanceService.init().then(function (res) {
                $scope.vm.masterData = res.data;
                $scope.vm.custAttrList.push({inputVal: "", inputOpts: ""});
            })
            .then(function() {
                // 如果来至category 或者header search 则默认检索
                $scope.vm.tblWidth = '100%'; // group tab的原始宽度
                $scope.vm.tblWidth2 = '100%'; // product tab的原始宽度
                if ($routeParams.type == "1"
                    || $routeParams.type == "2") {
                    search();
                }
            })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear () {
            $scope.vm.searchInfo = {
                compareType: null,
                brand: null,
                tags:[],
                priceChgFlg: '0',
                priceDiffFlg: '0',
                tagTypeSelectValue: '0'
            };
            $scope.vm.masterData.tagList = [];
            $scope.vm.custAttrList = [{ inputVal: "", inputOpts: "" }];
        }

        /**
         * 检索
         */
        function search () {
            // 默认设置成第一页
            $scope.vm.groupPageOption.curr = 1;
            $scope.vm.productPageOption.curr = 1;

            $scope.vm.searchInfo.custAttrMap = angular.copy($scope.vm.custAttrList);
            searchAdvanceService.search($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.productPageOption).then(function (res) {
                $scope.vm.customProps = res.data.customProps;
                $scope.vm.commonProps = res.data.commonProps;

                $scope.vm.groupList = res.data.groupList;
                //_.forEach($scope.vm.groupList, function (groupInfo) {
                //    var commArr = [];
                //    _.forEach($scope.vm.commonProps, function (data) {
                //        var itemVal = groupInfo.fields[data.propId];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        commArr.push({value: itemVal});
                //    });
                //    groupInfo.commArr = commArr;
                //    var custArr = [];
                //    _.forEach($scope.vm.customProps, function (data) {
                //        var itemVal = groupInfo.feed.cnAtts[data.feed_prop_original];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        custArr.push({value: itemVal});
                //    });
                //    groupInfo.custArr = custArr;
                //});
                //for (idx in $scope.vm.groupList) {
                //    var grpObj = $scope.vm.groupList[idx];
                //    grpObj.grpImgList = res.data.grpImgList[idx];
                //    grpObj._grpProdChgInfo = res.data.grpProdChgInfoList[idx];
                //}

                $scope.vm.groupPageOption.total = res.data.groupListTotal;
                $scope.vm.groupSelList = res.data.groupSelList;

                $scope.vm.productList = res.data.productList;
                //_.forEach($scope.vm.productList, function (prodInfo) {
                //    var commArr = [];
                //    _.forEach($scope.vm.commonProps, function (data) {
                //        var itemVal = prodInfo.fields[data.propId];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        commArr.push({value: itemVal});
                //    });
                //    prodInfo.commArr = commArr;
                //    var custArr = [];
                //    _.forEach($scope.vm.customProps, function (data) {
                //        var itemVal = prodInfo.feed.cnAtts[data.feed_prop_original];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        custArr.push({value: itemVal});
                //    });
                //    prodInfo.custArr = custArr;
                //});
                //for (idx in $scope.vm.productList) {
                //    var prodObj = $scope.vm.productList[idx];
                //    prodObj._prodChgInfo = res.data.prodChgInfoList[idx];
                //    prodObj._prodOrgChaName = res.data.prodOrgChaNameList[idx];
                //}

                $scope.vm.productPageOption.total = res.data.productListTotal;
                $scope.vm.productSelList = res.data.productSelList;
                for (idx in res.data.freeTagsList) {
                    var prodObj = $scope.vm.productList[idx];
                    prodObj._freeTagsInfo = res.data.freeTagsList[idx];
                }

                // 计算表格宽度
                $scope.vm.tblWidth = (($scope.vm.commonProps.length + $scope.vm.customProps.length) * 120 + 980) + 'px';
                $scope.vm.tblWidth2 = (($scope.vm.commonProps.length + $scope.vm.customProps.length) * 120 + 1150) + 'px';
            })
        }

        /**
         * 数据导出
         */
        function exportFile () {
            searchAdvanceService.exportFile($scope.vm.searchInfo);
        }

        /**
         * 分页处理group数据
         */
        function getGroupList () {
            searchAdvanceService.getGroupList($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.groupSelList, $scope.vm.commonProps, $scope.vm.customProps)
            .then(function (res) {
                $scope.vm.groupList = res.data.groupList == null ? [] : res.data.groupList;
                $scope.vm.groupPageOption.total = res.data.groupListTotal;
                $scope.vm.groupSelList = res.data.groupSelList;

                //_.forEach($scope.vm.groupList, function (groupInfo) {
                //    var commArr = [];
                //    _.forEach($scope.vm.commonProps, function (data) {
                //        var itemVal = groupInfo.fields[data.propId];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        commArr.push({value: itemVal});
                //    });
                //    groupInfo.commArr = commArr;
                //    var custArr = [];
                //    _.forEach($scope.vm.customProps, function (data) {
                //        var itemVal = groupInfo.feed.cnAtts[data.feed_prop_original];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        custArr.push({value: itemVal});
                //    });
                //    groupInfo.custArr = custArr;
                //});
                //for (idx in $scope.vm.groupList) {
                //    var grpObj = $scope.vm.groupList[idx];
                //    grpObj.grpImgList = res.data.grpImgList[idx];
                //    grpObj._grpProdChgInfo = res.data.grpProdChgInfoList[idx];
                //}
            });
        }

        /**
         * 分页处理product数据
         */
        function getProductList () {
            searchAdvanceService.getProductList($scope.vm.searchInfo, $scope.vm.productPageOption, $scope.vm.productSelList, $scope.vm.commonProps, $scope.vm.customProps)
            .then(function (res) {
                $scope.vm.productList = res.data.productList == null ? [] : res.data.productList;
                $scope.vm.productPageOption.total = res.data.productListTotal;
                $scope.vm.productSelList = res.data.productSelList;

                //_.forEach($scope.vm.productList, function (prodInfo) {
                //    var commArr = [];
                //    _.forEach($scope.vm.commonProps, function (data) {
                //        var itemVal = prodInfo.fields[data.propId];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        commArr.push({value: itemVal});
                //    });
                //    prodInfo.commArr = commArr;
                //    var custArr = [];
                //    _.forEach($scope.vm.customProps, function (data) {
                //        var itemVal = prodInfo.feed.cnAtts[data.feed_prop_original];
                //        if (itemVal == undefined) {
                //            itemVal = "";
                //        }
                //        custArr.push({value: itemVal});
                //    });
                //    prodInfo.custArr = custArr;
                //});
                //for (idx in $scope.vm.productList) {
                //    var prodObj = $scope.vm.productList[idx];
                //    prodObj._prodChgInfo = res.data.prodChgInfoList[idx];
                //    prodObj._prodOrgChaName = res.data.prodOrgChaNameList[idx];
                //}
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
        function openAddPromotion (promotion, openAddToPromotion) {
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
            openAddToPromotion(promotion, getSelProductList()).then(function () {
                getGroupList();
                getProductList();
            })
        }

        /**
         * popup出添加到聚美Promotion的功能
         * @param promotion
         * @param openJMActivity
         */
        function openJMActivity (promotion, openJMActivity) {
            openJMActivity(promotion, getSelProductList()).then(function () {
                getGroupList();
                getProductList();
            })
        }

        /**
         * popup出批量修改产品的field属性
         * @param openFieldEdit
         */
        function openBulkUpdate (openFieldEdit) {
            openFieldEdit(getSelProductList()).then(function () {
                getGroupList();
                getProductList();
            })
        }

        /**
         * popup弹出切换主数据类目
         * @param popupNewCategory
         */
        function openCategoryMapping (popupNewCategory) {
            var selList = getSelProductList();
            if (selList && selList.length) {
                feedMappingService.getMainCategories()
                    .then(function (res) {
                        popupNewCategory({
                            categories: res.data,
                            from: null
                        }).then( function (res) {
                                bindCategory (res)
                            }
                        );
                    });
            } else {
                alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
            }
        }

        function bindCategory (context) {
            confirm($translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY')).result
                .then(function () {
                    var productIds = [];
                    _.forEach(getSelProductList(), function (object) {
                        productIds.push(object.id);
                    });
                    var data = {
                        prodIds: productIds,
                        catId: context.selected.catId,
                        catPath: context.selected.catPath
                    };
                    productDetailService.changeCategory(data).then(function (res) {
                        if(res.data.isChangeCategory) {
                            notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            $scope.search();
                        }
                        else
                        // TODO 需要enka设计一个错误页面 res.data.publishInfo
                            notify($translate.instant('TXT_MSG_PRODUCT_IS_PUBLISHING'));
                    })
                });
        }

        /**
         * 添加新search选项
         */
        function addCustAttribute () {
            if ($scope.vm.custAttrList.length < 5) {
                $scope.vm.custAttrList.push({inputVal: "", inputOpts: ""});
            } else {
                alert("最多只能添加5项")
            }
        }

        function delCustAttribute (idx) {
            if ($scope.vm.custAttrList.length > 1) {
                $scope.vm.custAttrList.splice(idx, 1);
            } else {
                alert("最少保留一项")
            }
        }

        /**
         * 返回选中的数据,如果选中的是groups则返回的group下面所有的product,如果选择的是product,则只返回选中的product
         * @returns {Array}
         */
        function getSelProductList () {
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
        function getTagList () {
            if ($scope.vm.searchInfo.tagTypeSelectValue == '0' || $scope.vm.searchInfo.tagTypeSelectValue == '' || $scope.vm.searchInfo.tagTypeSelectValue == undefined) {
                $scope.vm.masterData.tagList = [];
                return;
            }
            channelTagService.getTagList({'tagTypeSelectValue':$scope.vm.searchInfo.tagTypeSelectValue})
                .then(function (res) {
                    $scope.vm.masterData.tagList = res.data;
                });
        }

        /**
         * 添加产品到指定自由标签
         */
        function addFreeTag (tagBean) {
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
            if (selList && selList.length) {
                var productIds = [];
                _.forEach(selList, function (object) {
                    productIds.push(object.id);
                });

                confirm("将对选定的产品添加自由标签" + tagBean.tagPathName).result
                    .then(function () {
                        searchAdvanceService.addFreeTag(tagBean.tagPath, productIds).then(function () {
                            notify.success ($translate.instant('TXT_MSG_SET_SUCCESS'));
                            getGroupList();
                            getProductList();
                        })
                    });
            } else {
                alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
            }
        }

    }
    searchIndex.$inject = ['$scope', '$routeParams', 'searchAdvanceService', 'feedMappingService', '$productDetailService', 'channelTagService', 'confirm', '$translate', 'notify', 'alert'];
    return searchIndex;
});
