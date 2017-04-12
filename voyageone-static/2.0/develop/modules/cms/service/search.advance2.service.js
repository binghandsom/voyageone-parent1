/**
 * Created by linanbin on 15/12/3.
 */

define([
    'angularAMD',
    'underscore',
    'modules/cms/enums/Carts',
    'modules/cms/enums/PlatformStatus'
], function (angularAMD, _, Carts, PlatformStatus) {
    angularAMD.service('searchAdvanceService2', searchAdvanceService2);

    function searchAdvanceService2($q, blockUI, $translate, selectRowsFactory, $searchAdvanceService2, $filter, cActions) {

        this.search = search;
        this.getGroupList = getGroupList;
        this.getProductList = getProductList;
        this.exportFile = exportFile;
        this.clearSelList = clearSelList;

        var tempGroupSelect = new selectRowsFactory();
        var tempProductSelect = new selectRowsFactory();

        /**
         * 检索group和product
         * @param data
         * @returns {*}
         */
        function search(data, groupPagination, productPagination) {
            var defer = $q.defer();
            data = resetSearchInfo(data);
            //// 设置groupPage
            //data.groupPageNum = groupPagination.curr;
            //data.groupPageSize = groupPagination.size;
            // 设置productPage
            data.productPageNum = productPagination.curr;
            data.productPageSize = productPagination.size;

            $searchAdvanceService2.search(data).then(function (res) {

                // 重新初始化选中标签
                tempGroupSelect = new selectRowsFactory();
                tempProductSelect = new selectRowsFactory();
                // 获取group列表
                //_resetGroupList(res.data, res.data.commonProps, res.data.customProps, res.data.selSalesType, res.data.selBiDataList, data);
                // 获取product列表
                _resetProductList(res.data, res.data.commonProps, res.data.customProps, res.data.selSalesType, res.data.selBiDataList, data);

                defer.resolve(res);
            });
            return defer.promise;
        }

        function exportFile(data) {
            data = resetSearchInfo(data);
            var defer = $q.defer();

            $searchAdvanceService2.exportProducts(data).then(function (res) {
                defer.resolve(res);
            });
            return defer.promise;
        }

        /**
         * 检索group
         * @param data
         * @returns {*}
         */
        function getGroupList(data, pagination, list, commonProps, customProps, selSalesTypes, selBiDataList) {
            var defer = $q.defer();

            $searchAdvanceService2.getGroupList(resetGroupPagination(data, pagination)).then(function (res) {
                _resetGroupList(res.data, commonProps, customProps, selSalesTypes, selBiDataList, data);
                defer.resolve(res);
            });
            return defer.promise;
        }

        /**
         * 检索product
         * @param data
         * @returns {*}
         */
        function getProductList(data, pagination, list, commonProps, customProps, selSalesTypes, selBiDataList) {
            var defer = $q.defer();
            $searchAdvanceService2.getProductList(resetProductPagination(data, pagination)).then(function (res) {
                _resetProductList(res.data, commonProps, customProps, selSalesTypes, selBiDataList, data);
                defer.resolve(res);
            });
            return defer.promise;
        }

        /**
         * 将searchInfo转换成server端使用的bean接口
         * @param data
         * @returns {*}
         */
        function resetSearchInfo(data) {
            var searchInfo = angular.copy(data);
            searchInfo.productStatus = _returnKey(searchInfo.productStatus);
            searchInfo.platformStatus = _returnKey(searchInfo.platformStatus);
            searchInfo.pRealStatus = _returnKey(searchInfo.pRealStatus);
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

            // 过滤重复的排序条件，相同的以后一个为准
            if (searchInfo.sortThreeName && searchInfo.sortThreeType) {
                if (searchInfo.sortTwoName && searchInfo.sortTwoType) {
                    if (searchInfo.sortThreeName == searchInfo.sortTwoName) {
                        searchInfo.sortTwoName = '';
                        searchInfo.sortTwoType = '';
                    }
                }
                if (searchInfo.sortOneName && searchInfo.sortOneType) {
                    if (searchInfo.sortThreeName == searchInfo.sortOneName) {
                        searchInfo.sortOneName = '';
                        searchInfo.sortOneType = '';
                    }
                }
            }
            if (searchInfo.sortTwoName && searchInfo.sortTwoType) {
                if (searchInfo.sortOneName && searchInfo.sortOneType) {
                    if (searchInfo.sortTwoName == searchInfo.sortOneName) {
                        searchInfo.sortOneName = '';
                        searchInfo.sortOneType = '';
                    }
                }
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
        function resetGroupPagination(data, pagination) {
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
        function resetProductPagination(data, pagination) {
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
                .map(function (value, key) {
                    return value ? key : null;
                })
                .filter(function (value) {
                    return value;
                })
                .value();
        }

        // 把取回的数据作相应转换(转换到画面对应的项目)
        function _resetProdInfo(prodInfo, commonProps, customProps, selSalesTypes, selBiDataList) {
            var commArr = [];
            _.forEach(commonProps, function (data) {
                var itemVal = '';

                switch (data.propId) {
                    case "comment":
                        itemVal = prodInfo.common.comment;
                        break;
                    case "created":
                        itemVal = prodInfo.created;
                        break;
                    case  "clientMsrpPrice":
                        itemVal = data.propId;
                        break;
                    case "isMasterMain":
                        // 原始主商品的转换
                        if (itemVal == 1)
                            itemVal = '是';
                        else if (itemVal == 0)
                            itemVal = '否';
                        break;
                    default:
                        itemVal = prodInfo.common.fields[data.propId];
                }

                if (itemVal == undefined)
                    itemVal = "";

                commArr.push({value: itemVal.toString()});
            });
            prodInfo.commArr = commArr;
            var custArr = [];
            _.forEach(customProps, function (data) {
                var itemVal = prodInfo.feed.cnAtts[data.feed_prop_original];
                var orgAttsitemVal = prodInfo.feed.orgAtts[data.feed_prop_original];
                if (itemVal == undefined) {
                    itemVal = "";
                }
                custArr.push({value: itemVal});
                custArr.push({value: orgAttsitemVal});
            });
            prodInfo.custArr = custArr;

            // 销量数据整理
            var selSalesTyeArr = [];
            _.forEach(selSalesTypes, function (data) {
                var selValue = data.value;
                var dotIdx = selValue.indexOf(".", 6);
                var itemValObj = prodInfo.sales[selValue.substring(6, dotIdx)];
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
            prodInfo.selSalesTyeArr = selSalesTyeArr;

            // bi数据整理
            var selBiDataArr = [];
            _.forEach(selBiDataList, function (data) {
                var selValue = data.value;
                var itemVal = getObjectValue(prodInfo, selValue);
                // 处理发布时间格式
                if (selValue.indexOf('.pPublishTime') != -1 && angular.isString(itemVal)) {
                    itemVal = itemVal.substring(0, 19);
                }
                selBiDataArr.push({value: itemVal});
            });
            prodInfo.selBiDataArr = selBiDataArr;

            // 解析对象值
            function getObjectValue(object, name) {
                var value = angular.copy(object);
                var names = name.split('.');
                for (var i = 0; i < names.length; i++) {
                    if (value.hasOwnProperty(names[i])) {
                        value = value[names[i]];
                    } else {
                        return '';
                    }
                }
                return value;
            }
        }

        /**
         * 设置group list
         * @param data
         * @returns {*}
         * @private
         */
        function _resetGroupList(data, commonProps, customProps, selSalesTypes, selBiDataList, searchParam) {
            tempGroupSelect.clearCurrPageRows();
            for (idx in data.groupList) {
                var prodObj = data.groupList[idx];
                prodObj._grpPriceInfoList = data.grpPriceInfoList[idx];
            }
            _.forEach(data.groupList, function (groupInfo, index) {
                _resetProdInfo(groupInfo, commonProps, customProps, selSalesTypes, selBiDataList);

                _resetCartInfo(groupInfo);

                // 初始化数据选中需要的数组
                tempGroupSelect.currPageRows({
                    "id": groupInfo.prodId,
                    "code": groupInfo.common.fields["code"],
                    "prodIds": data.grpProdIdList[index]
                });

                // 设置price detail
                groupInfo.groupBean.priceSale = _setGroupPriceSale(groupInfo, searchParam);

                // 设置time detail
                groupInfo.groupBean.timeDetail = _setTimeDetail(groupInfo);

                groupInfo.grpImgList = data.grpImgList[index];
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
        function _resetProductList(data, commonProps, customProps, selSalesTypes, selBiDataList, searchParam) {
            tempProductSelect.clearCurrPageRows();
            _.forEach(data.productList, function (productInfo, index) {
                _resetProdInfo(productInfo, commonProps, customProps, selSalesTypes, selBiDataList);

                _resetCartInfo(productInfo);

                // 初始化数据选中需要的数组
                tempProductSelect.currPageRows({"id": productInfo.prodId, "code": productInfo.common.fields["code"]});

                // 设置sku销售渠道信息
                productInfo.skuDetail = _setSkuDetail(productInfo.platforms);

                // 设置在各平台上的建议售价
                productInfo.priceMsrp = _setPriceSale(productInfo.platforms, searchParam, 'pPriceMsrpSt', 'pPriceMsrpEd');
                // 设置指导售价
                productInfo.priceRetail = _setPriceSale(productInfo.platforms, searchParam, 'pPriceRetailSt', 'pPriceRetailEd');
                productInfo._retailPriceCol = _setRetailPriceCol(productInfo.platforms, searchParam);
                // 设置在各平台上的最终售价
                productInfo.priceSale = _setPriceSale(productInfo.platforms, searchParam, 'pPriceSaleSt', 'pPriceSaleEd');

                // 设置time detail
                productInfo.groupBean.timeDetail = _setTimeDetail(productInfo);

                productInfo._prodOrgChaName = data.prodOrgChaNameList[index];
            });
            data.productSelList = tempProductSelect.selectRowsInfo;

            return data;
        }

        function _resetCartInfo(productInfo) {

            productInfo.carts = [];
            if (productInfo.platforms) {
                var ptms = [];
                _.forEach(productInfo.platforms, function (data) {
                    ptms.push(data);
                });
                productInfo.platforms = ptms.sort(function (a, b) {
                    return a.cartId > b.cartId;
                });

                _.forEach(productInfo.platforms, function (data) {
                    if (data.cartId == undefined || data.cartId == '' || data.cartId == null) {
                        return;
                    }
                    var cartItem = {};
                    cartItem.cartId = parseInt(data.cartId);
                    cartItem.platformStatus = data.pStatus;
                    cartItem.publishTime = data.pPublishTime;
                    cartItem.numiid = data.pNumIId;
                    cartItem.mallId = data.pPlatformMallId;
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
                        cartItem.cssVal = {"background-color": cssVal};
                    }
                    cartItem.statusTxt = statusTxt;
                    cartItem.publishError = publishError;

                    // 设置产品跳转URL
                    var cartInfo = Carts.valueOf(cartItem.cartId);
                    if (cartInfo == null || cartInfo == undefined) {
                        cartItem._purl = '';
                        cartItem._pname = '';
                    } else {
                        if (cartItem.numiid == null || cartItem.numiid == '' || cartItem.numiid == undefined || data.status != 'Approved') {
                            cartItem._purl = '';
                        } else {
                            if (cartItem.cartId == 27) {
                                cartItem._purl = cartInfo.pUrl + cartItem.mallId + '.html';
                            } else {
                                cartItem._purl = cartInfo.pUrl + cartItem.numiid;
                            }
                        }
                        cartItem._pname = cartInfo.name;
                    }
                    var stsCnVal = PlatformStatus.getStsTxt(data.pStatus, data.pReallyStatus, data.status);
                    if (stsCnVal) {
                        cartItem._pTxt = cartItem._pname + ':' + stsCnVal;
                    } else {
                        cartItem._pTxt = cartItem._pname;
                    }
                    if (data.pIsMain == '1') {
                        cartItem._isMain = true;
                    }

                    productInfo.carts.push(cartItem);
                });
            }

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
                    var skuTxt = cartInfo.substr(0, cartInfo.length - 1);
                    if (skuTxt == undefined || skuTxt == null) {
                        skuTxt = '';
                    }
                    if (cartInfoObj == undefined) {
                        skuTxt = platformObj.cartId + ": " + skuTxt
                    } else {
                        skuTxt = cartInfoObj.name + ": " + skuTxt
                    }
                    result.push(skuTxt);
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
            object._grpPriceInfoList = object._grpPriceInfoList.sort(function (a, b) {
                return a.cartId > b.cartId;
            });
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
                if (data[stakey] != null && data[stakey] != undefined && data[endKey] != null && data[endKey] != undefined) {
                    if (data[stakey] == data[endKey]) {
                        priceItem += $filter('number')(data[stakey], 2);
                    } else {
                        priceItem += $filter('number')(data[stakey], 2);
                        priceItem += " ~ ";
                        priceItem += $filter('number')(data[endKey], 2);
                    }
                }

                // 当是中国指导价时，要有价格变化提示
                if (stakey == 'pPriceRetailSt' && data.skus) {
                    for (idx in data.skus) {
                        if (data.skus[idx].priceChgFlg) {
                            var upFlg = data.skus[idx].priceChgFlg.indexOf('U');
                            var downFlg = data.skus[idx].priceChgFlg.indexOf('D');
                            var cssTxt = 'class="text-u-red font-bold"';
                            if (upFlg == 0) {
                                // 涨价
                                priceItem += '<label ' + cssTxt + '>&nbsp;(↑' + data.skus[idx].priceChgFlg.substring(upFlg + 1) + ')</label>'
                            } else if (downFlg == 0) {
                                // 降价
                                priceItem += '<label ' + cssTxt + '>&nbsp;(↓' + data.skus[idx].priceChgFlg.substring(downFlg + 1) + ')</label>'
                            }
                            break;
                        }
                    }
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

        function _setRetailPriceCol(object, searchParam) {
            var fstLine = {'pVal': '', 'pTxt': '', 'cssTxt': ''};
            if (object == null || object == undefined) {
                return fstLine;
            }

            var fstCode = 0;
            if (searchParam && searchParam.cartId) {
                fstCode = searchParam.cartId;
            }
            for (idx in object) {
                var data = object[idx];
                if (data == null || data == undefined || data.cartId == null || data.cartId == undefined || data.cartId == 0) {
                    continue;
                }
                if (fstCode != 0 && fstCode != data.cartId) {
                    continue;
                }

                var priceItem = '';
                var cartInfo = Carts.valueOf(data.cartId);
                if (cartInfo == null || cartInfo == undefined) {
                    priceItem += data.cartId;
                } else {
                    priceItem += cartInfo.name;
                }
                priceItem += ': ';
                // 合计sku价格的上下限 'pPriceRetailSt', 'pPriceRetailEd'
                if (data['pPriceRetailSt'] != null && data['pPriceRetailSt'] != undefined && data['pPriceRetailEd'] != null && data['pPriceRetailEd'] != undefined) {
                    if (data['pPriceRetailSt'] == data['pPriceRetailEd']) {
                        priceItem += $filter('number')(data['pPriceRetailSt'], 2);
                    } else {
                        priceItem += $filter('number')(data['pPriceRetailSt'], 2);
                        priceItem += " ~ ";
                        priceItem += $filter('number')(data['pPriceRetailEd'], 2);
                    }
                }

                // 当是中国指导价时，要有价格变化提示
                if (data.skus) {
                    for (idx in data.skus) {
                        if (data.skus[idx].priceChgFlg) {
                            var upFlg = data.skus[idx].priceChgFlg.indexOf('U');
                            var downFlg = data.skus[idx].priceChgFlg.indexOf('D');

                            fstLine.cssTxt = 'text-u-red font-bold';
                            if (upFlg == 0) {
                                // 涨价
                                fstLine.pTxt = '(↑' + data.skus[idx].priceChgFlg.substring(upFlg + 1) + ')'
                            } else if (downFlg == 0) {
                                // 降价
                                fstLine.pTxt = '(↓' + data.skus[idx].priceChgFlg.substring(downFlg + 1) + ')'
                            }
                            break;
                        }
                    }
                }
                fstLine.pVal = priceItem;
                if (fstCode == 0) {
                    // 未选择平台
                    break;
                }
            }

            return fstLine;
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

            if (!_.isEmpty(product.created))
                result.push($translate.instant('TXT_CREATE_TIME_WITH_COLON') + product.created.substring(0, 19));

            var platforms = product.groupBean;
            if (!_.isEmpty(platforms.publishTime))
                result.push($translate.instant('TXT_PUBLISH_TIME_WITH_COLON') + platforms.publishTime.substring(0, 19));

            if (!_.isEmpty(platforms.inStockTime))
                result.push($translate.instant('TXT_ON_SALE_TIME_WITH_COLON') + platforms.inStockTime.substring(0, 19));

            return result;
        }

        function clearSelList() {
            tempGroupSelect.clearSelectedList();
            tempProductSelect.clearSelectedList();
        }

    }
});