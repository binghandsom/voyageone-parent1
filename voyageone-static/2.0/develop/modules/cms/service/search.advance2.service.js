/**
 * Created by linanbin on 15/12/3.
 */

define([
    'angularAMD',
    'underscore',
    'modules/cms/enums/Carts'
], function (angularAMD, _, Carts) {
    angularAMD.service('searchAdvanceService2', searchAdvanceService2);

    function searchAdvanceService2($q, blockUI, $translate, selectRowsFactory, $searchAdvanceService2, $filter, cActions) {

        this.init = init;
        this.search = search;
        this.getGroupList = getGroupList;
        this.getProductList = getProductList;
        this.exportFile = exportFile;
        this.addFreeTag = addFreeTag;
        this.getCustSearchList = getCustSearchList;
        this.clearSelList = clearSelList;

        var tempGroupSelect = new selectRowsFactory();
        var tempProductSelect = new selectRowsFactory();

        /**
         * 初始化数据
         * @returns {*}
         */
        function init() {
            var defer = $q.defer();
            $searchAdvanceService2.init().then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索group和product
         * @param data
         * @returns {*}
         */
        function search(data, groupPagination, productPagination) {
            var defer = $q.defer();
            data = resetSearchInfo(data);
            // 设置groupPage
            data.groupPageNum = groupPagination.curr;
            data.groupPageSize = groupPagination.size;
            // 设置productPage
            data.productPageNum = productPagination.curr;
            data.productPageSize = productPagination.size;

            $searchAdvanceService2.search(data).then(function (res) {

                // 重新初始化选中标签
                tempGroupSelect = new selectRowsFactory();
                tempProductSelect = new selectRowsFactory();
                // 获取group列表
                _resetGroupList(res.data, res.data.commonProps, res.data.customProps, res.data.selSalesType, data);
                // 获取product列表
                _resetProductList(res.data, res.data.commonProps, res.data.customProps, res.data.selSalesType, data);

                defer.resolve (res);
            });
            return defer.promise;
        }

        function exportFile (data) {
            data = resetSearchInfo(data);
            function _exportFileCallback (res) {
                var obj = JSON.parse(res);
                if (obj.code == '4001') {
                    alert("查询参数不正确，请重试。");
                } else if (obj.code == '4002') {
                    alert("未设置下载文件名。");
                } else if (obj.code == '4003') {
                    alert("创建文件时出错。");
                }
            }
            $.download.post(cActions.cms.search.$searchAdvanceService2.root + cActions.cms.search.$searchAdvanceService2.exportProducts, {params: JSON.stringify(data)}, _exportFileCallback);
        }

        /**
         * 检索group
         * @param data
         * @returns {*}
         */
        function getGroupList(data, pagination, list, commonProps, customProps, selSalesTypes) {
            var defer = $q.defer();

            $searchAdvanceService2.getGroupList(resetGroupPagination(data, pagination)).then(function (res) {
                _resetGroupList(res.data, commonProps, customProps, selSalesTypes);
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索product
         * @param data
         * @returns {*}
         */
        function getProductList(data, pagination, list, commonProps, customProps, selSalesTypes) {
            var defer = $q.defer();
            $searchAdvanceService2.getProductList(resetProductPagination(data, pagination)).then(function (res) {
                _resetProductList(res.data, commonProps, customProps, selSalesTypes);
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 添加自由标签
         * @param data
         * @returns {*}
         */
        function addFreeTag(tagPath, prodIdList, selAllFlg) {
            var defer = $q.defer();
            var data = {"tagPath":tagPath, "prodIdList":prodIdList, "isSelAll":selAllFlg};

            $searchAdvanceService2.addFreeTag(data).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 自定义搜索条件中，当选择的项目为下拉列表时，获取下拉列表的值
         * @param fieldsId
         * @returns {*}
         */
        function getCustSearchList(fieldsId, inputType) {
            var defer = $q.defer();
            var data = {"fieldsId":fieldsId, "inputType":inputType};

            $searchAdvanceService2.getCustSearchList(data).then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 将searchInfo转换成server端使用的bean接口
         * @param data
         * @returns {*}
         */
        function resetSearchInfo (data) {
            var searchInfo = angular.copy (data);
            searchInfo.productStatus = _returnKey (searchInfo.productStatus);
            searchInfo.platformStatus = _returnKey(searchInfo.platformStatus);
            if (searchInfo.hasErrorFlg) {
                searchInfo.hasErrorFlg = 1;
            } else {
                searchInfo.hasErrorFlg = 0;
            }
            if (searchInfo.shopCatStatus) {
                searchInfo.shopCatStatus = 1;
            } else {
                searchInfo.shopCatStatus = 0;
            }
            if (searchInfo.pCatStatus) {
                searchInfo.pCatStatus = 1;
            } else {
                searchInfo.pCatStatus = 0;
            }

            if (!_.isUndefined(searchInfo.codeList) && !_.isNull(searchInfo.codeList))
                searchInfo.codeList = searchInfo.codeList.split("\n");
            return searchInfo;
        }

        /**
         * 添加group的当前页码和每页显示size到server端使用的bean接口
         * @param data
         * @param pagination
         * @returns {*}
         */
        function resetGroupPagination (data, pagination) {
            var searchInfo = resetSearchInfo(data);
            searchInfo.groupPageNum = pagination.curr;
            searchInfo.groupPageSize = pagination.size;
            return searchInfo
        }

        /**
         * 添加product的当前页码和每页显示size到server端使用的bean接口
         * @param data
         * @param pagination
         * @returns {*}
         */
        function resetProductPagination (data, pagination) {
            var searchInfo = resetSearchInfo(data);
            searchInfo.productPageNum = pagination.curr;
            searchInfo.productPageSize = pagination.size;
            return searchInfo
        }

        /**
         * 如果checkbox被选中,返回被选中的value.
         * eg.[{new: true, pending: false, approved: true}] -> [new, approved]
         * @param object
         * @returns {*}
         */
        function _returnKey(object) {
            return _.chain(object)
                .map(function(value, key) { return value ? key : null;})
                .filter(function(value) { return value;})
                .value();
        }

        /**
         * 设置group list
         * @param data
         * @returns {*}
         * @private
         */
        function _resetGroupList (data, commonProps, customProps, selSalesTypes, searchParam) {
            tempGroupSelect.clearCurrPageRows();
            for (idx in data.groupList) {
                var prodObj = data.groupList[idx];
                prodObj._grpPriceInfoList = data.grpPriceInfoList[idx];
            }
            _.forEach(data.groupList, function (groupInfo, index) {

                var commArr = [];
                _.forEach(commonProps, function (data) {
                    var itemVal = groupInfo.common.fields[data.propId];
                    // 原始主商品的转换
                    if (data.propId == 'isMasterMain') {
                        if (itemVal == 1) {
                            itemVal = '是';
                        } else if (itemVal == 0) {
                            itemVal = '否';
                        }
                    }
                    if (itemVal == undefined) {
                        itemVal = "";
                    }
                    commArr.push({value: itemVal.toString()});
                });
                groupInfo.commArr = commArr;
                var custArr = [];
                _.forEach(customProps, function (data) {
                    var itemVal = groupInfo.feed.cnAtts[data.feed_prop_original];
                    var orgAttsitemVal= groupInfo.feed.orgAtts[data.feed_prop_original];
                    if (itemVal == undefined) {
                        itemVal = "";
                    }
                    custArr.push({value: itemVal});
                    custArr.push({value: orgAttsitemVal});
                });
                groupInfo.custArr = custArr;
                var selSalesTyeArr = [];
                _.forEach(selSalesTypes, function (data) {
                    var selValue = data.value;
                    var dotIdx = selValue.indexOf(".", 6);
                    var itemValObj = groupInfo.sales[selValue.substring(6, dotIdx)];
                    var itemVal = null;
                    if (itemValObj == undefined) {
                        itemVal = "0";
                    } else {
                        dotIdx = selValue.lastIndexOf(".");
                        itemVal = itemValObj[selValue.substring(dotIdx + 1)];
                        if (itemVal == undefined) {
                            itemVal = "0";
                        }
                    }
                    selSalesTyeArr.push({value: itemVal});
                });
                groupInfo.selSalesTyeArr = selSalesTyeArr;

                // 初始化数据选中需要的数组
                tempGroupSelect.currPageRows({"id": groupInfo.prodId, "code": groupInfo.common.fields["code"], "prodIds": data.grpProdIdList[index]});

                // 设置price detail
                groupInfo.groupBean.priceSale = _setGroupPriceSale(groupInfo, searchParam);

                // 设置time detail
                groupInfo.groupBean.timeDetail = _setTimeDetail(groupInfo);

                groupInfo.grpImgList = data.grpImgList[index];

                groupInfo._grpProdChgInfo = data.grpProdChgInfoList[index];

            });
            data.groupSelList = tempGroupSelect.selectRowsInfo;

            return data;
        }

        /**
         * 设置product list
         * @param data
         * @returns {*}
         * @private
         */
        function _resetProductList (data, commonProps, customProps, selSalesTypes, searchParam) {
            tempProductSelect.clearCurrPageRows();
            _.forEach(data.productList, function (productInfo, index) {
                var commArr = [];
                _.forEach(commonProps, function (data) {
                    var itemVal = productInfo.common.fields[data.propId];
                    // 原始主商品的转换
                    if (data.propId == 'isMasterMain') {
                        if (itemVal == 1) {
                            itemVal = '是';
                        } else if (itemVal == 0) {
                            itemVal = '否';
                        }
                    }
                    if (itemVal == undefined || itemVal == null) {
                        itemVal = "";
                    }
                    commArr.push({value: itemVal.toString()});
                });
                productInfo.commArr = commArr;

                var custArr = [];
                _.forEach(customProps, function (data) {
                    var itemVal = productInfo.feed.cnAtts[data.feed_prop_original];
                    var orgAttsitemVal= productInfo.feed.orgAtts[data.feed_prop_original];
                    if (itemVal == undefined || itemVal == null) {
                        itemVal = "";
                    }
                    custArr.push({value: itemVal});
                    custArr.push({value: orgAttsitemVal});
                });
                productInfo.custArr = custArr;

                var selSalesTyeArr = [];
                _.forEach(selSalesTypes, function (data) {
                    var selValue = data.value;
                    var dotIdx = selValue.indexOf(".", 6);
                    var itemValObj = productInfo.sales[selValue.substring(6, dotIdx)];
                    var itemVal = null;
                    if (itemValObj == undefined || itemValObj == null) {
                        itemVal = "0";
                    } else {
                        dotIdx = selValue.lastIndexOf(".");
                        itemVal = itemValObj[selValue.substring(dotIdx + 1)];
                        if (itemVal == undefined || itemVal == null) {
                            itemVal = "0";
                        }
                    }
                    selSalesTyeArr.push({value: itemVal});
                });
                productInfo.selSalesTyeArr = selSalesTyeArr;

                var cartArr = [];
                if (productInfo.platforms) {
                    _.forEach(productInfo.platforms, function (data) {
                        if (data.cartId == undefined || data.cartId == '' || data.cartId == null) {
                            return;
                        }
                        var cartItem = {};
                        cartItem.cartId = parseInt(data.cartId);
                        cartItem.platformStatus = data.pStatus;
                        cartItem.publishTime = data.pPublishTime;
                        cartItem.numiid = data.pNumIId;
                        // 设置产品状态显示区域的css(背景色)
                        var cssVal = '';
                        var statusTxt = '';
                        var publishError = false;
                        cartItem.cssVal = {};

                        if (data.status == 'Approved') {
                            if (data.pStatus == 'OnSale') {
                                cssVal = 'DeepSkyBlue';
                                statusTxt = 'OnSale';
                            } else if (data.pStatus == 'InStock') {
                                cssVal = 'Orange';
                                statusTxt = 'InStock';
                            } else if (data.pStatus == 'WaitingPublish') {
                                cssVal = 'Chocolate';
                                statusTxt = 'WaitingPublish';
                            } else {
                                cssVal = 'YellowGreen';
                                statusTxt = 'Approved';
                            }
                        } else if (data.status == 'Ready') {
                            cssVal = 'yellow';
                            statusTxt = 'Ready';
                        } else {
                            cssVal = 'DarkGray';
                            statusTxt = 'Pendding';
                        }

                        if (data.pPublishError == 'Error') {
                            cssVal = 'red';
                            publishError = true;
                        }

                        if (cssVal) {
                            cartItem.cssVal = { "background-color" : cssVal };
                        }
                        cartItem.statusTxt = statusTxt;
                        cartItem.publishError = publishError;
                        cartArr.push(cartItem);
                    });
                }
                productInfo.carts = cartArr;
                if (productInfo.carts) {
                    _.forEach(productInfo.carts, function (data) {
                        var cartInfo = Carts.valueOf(data.cartId);
                        if (cartInfo == null || cartInfo == undefined) {
                            data._purl = '';
                            data._pname = '';
                        } else {
                            if (data.numiid == null || data.numiid == '' || data.numiid == undefined) {
                                data._purl = '';
                            } else {
                                if (data.cartId == 27) {
                                    data._purl = cartInfo.pUrl + data.numiid + '.html';
                                } else {
                                    data._purl = cartInfo.pUrl + data.numiid;
                                }
                            }
                            data._pname = cartInfo.name;
                        }
                    });
                }

                // 初始化数据选中需要的数组
                tempProductSelect.currPageRows({"id": productInfo.prodId, "code": productInfo.common.fields["code"]});

                // 设置sku销售渠道信息
                productInfo.skuDetail = _setSkuDetail(productInfo.platforms);

                // 设置在各平台上的建议售价
                productInfo.priceMsrp = _setPriceSale(productInfo.platforms, searchParam, 'pPriceMsrpSt', 'pPriceMsrpEd');
                // 设置指导售价
                productInfo.priceDetail = _setPriceDetail(productInfo.platforms, cartArr);
                // 设置在各平台上的最终售价
                productInfo.priceSale = _setPriceSale(productInfo.platforms, searchParam, 'pPriceSaleSt', 'pPriceSaleEd');

                // 设置time detail
                productInfo.groupBean.timeDetail = _setTimeDetail(productInfo);

                productInfo._prodChgInfo = data.prodChgInfoList[index];
                productInfo._prodOrgChaName = data.prodOrgChaNameList[index];

            });
            data.productSelList = tempProductSelect.selectRowsInfo;

            return data;
        }

        /**
         * 设置sku的销售渠道信息
         * @param platforms
         * @returns {Array}
         * @private
         */
        function _setSkuDetail(platforms) {
            var result = [];
            _.forEach(platforms, function (platformObj) {
                if (platformObj.cartId && platformObj.cartId != 0 && platformObj.skus && platformObj.skus.length > 0) {
                    var cartInfoObj = Carts.valueOf(parseInt(platformObj.cartId));
                    var cartInfo = "";
                    _.forEach(platformObj.skus, function (skuObj) {
                         cartInfo += skuObj.skuCode + ",";
                    });
                    result.push(cartInfoObj.name + ": " + cartInfo.substr(0, cartInfo.length -1));
                }
            });
            return result;
        }

        /**
         * 设置Price Detail 产品指导价
         * @param groups
         * @returns {string}
         * @private
         */
        function _setPriceDetail(object, cartArr) {
            if (cartArr == null || cartArr == undefined || cartArr.length == 0) {
                return '';
            }
            // 设置retail price
            var platObj = object["P" + cartArr[0].cartId];
            if (platObj == null || platObj == undefined) {
                return '';
            }
            var tempRetailPriceDetail = _setOnePriceDetail("", platObj.pPriceRetailSt, platObj.pPriceRetailEd);
            return tempRetailPriceDetail;
        }

        /**
         * 设置页面上显示的价格 group最终价格
         * @param object
         * @returns {*}
         * @private
         */
        function _setGroupPriceSale(object, searchParam) {
            return _setPriceSale(object._grpPriceInfoList, searchParam, 'priceSaleSt', 'priceSaleEd');
        }

        /**
         * 设置页面上显示的最终价格
         * @param object
         * @returns {*}
         * @private
         */
        function _setPriceSale(object, searchParam, stakey, endKey) {
            if (object == null || object == undefined) {
                return [];
            }
            if (object) {
                var fstLine = [];
                var result = [];
                var fstCode = 0;
                if (searchParam && searchParam.cartId) {
                    fstCode = searchParam.cartId;
                }
                _.forEach(object, function (data) {
                    if (data == null || data == undefined || data.cartId == null || data.cartId == undefined || data.cartId == 0) {
                        return;
                    }
                    var priceItem = '';
                    var cartInfo = Carts.valueOf(data.cartId);
                    if (cartInfo == null || cartInfo == undefined) {
                        priceItem += data.cartId;
                    } else {
                        priceItem += cartInfo.name;
                    }
                    priceItem += ': ';
                    // 合计sku价格的上下限
                    if (data[stakey] == data[endKey]) {
                        priceItem += $filter('number')(data[stakey], 2);
                    } else {
                        priceItem += $filter('number')(data[stakey], 2);
                        priceItem += " ~ ";
                        priceItem += $filter('number')(data[endKey], 2);
                    }
                    if (fstCode == data.cartId) {
                        fstLine.push(priceItem);
                    } else {
                        result.push(priceItem);
                    }
                });
                fstLine = fstLine.concat(result);
                return fstLine;
            }
            return [];
        }

        /**
         * 设置Price Detail
         * @param priceStart
         * @param priceEnd
         * @returns {*}
         * @private
         */
        function _setOnePriceDetail(title, priceStart, priceEnd) {
            var result = null;
            if (!_.isUndefined(priceStart) && !_.isNull(priceStart)
                && !_.isUndefined(priceEnd) && !_.isNull(priceEnd)) {
                result = _.isEqual(priceStart, priceEnd)
                    ? $filter('number')(priceStart, 2)
                    : $filter('number')(priceStart, 2) + " ~ " + $filter('number')(priceEnd, 2);
            } else {
                result = _.isNumber(priceStart)
                    ? $filter('number')(priceStart, 2)
                    : ((_.isNumber(priceEnd)
                    ? $filter('number')(priceEnd, 2)
                    : null));
            }

            return _.isNull(result) ? null : title + result;
        }

        /**
         * 设置time detail
         * @param platforms
         * @private
         */
        function _setTimeDetail(product) {
            var result = [];

            if(!_.isEmpty(product.created))
                result.push($translate.instant('TXT_CREATE_TIME_WITH_COLON') + product.created.substring(0, 19));

            var platforms = product.groupBean;
            if(!_.isEmpty(platforms.publishTime))
                result.push($translate.instant('TXT_PUBLISH_TIME_WITH_COLON') + platforms.publishTime.substring(0, 19));

            if(!_.isEmpty(platforms.inStockTime))
                result.push($translate.instant('TXT_ON_SALE_TIME_WITH_COLON') + platforms.inStockTime.substring(0, 19));

            return result;
        }

        function clearSelList(){
            tempGroupSelect.clearSelectedList();
            tempProductSelect.clearSelectedList();
        }
    }
});