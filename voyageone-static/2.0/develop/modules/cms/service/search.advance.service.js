/**
 * Created by linanbin on 15/12/3.
 */

define([
    'angularAMD',
    'underscore',
    'modules/cms/enums/Carts'
], function (angularAMD, _, Carts) {
    angularAMD
        .service('searchAdvanceService', searchAdvanceService);

    function searchAdvanceService($q, $translate, selectRowsFactory, $searchAdvanceService, $filter, cActions) {

        this.init = init;
        this.search = search;
        this.getGroupList = getGroupList;
        this.getProductList = getProductList;
        this.exportFile = exportFile;
        this.addFreeTag = addFreeTag;

        var tempGroupSelect = new selectRowsFactory();
        var tempProductSelect = new selectRowsFactory();

        /**
         * 初始化数据
         * @returns {*}
         */
        function init() {
            var defer = $q.defer();
            $searchAdvanceService.init().then(function (res) {
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

            $searchAdvanceService.search(data).then(function (res) {

                // 重新初始化选中标签
                tempGroupSelect = new selectRowsFactory();
                tempProductSelect = new selectRowsFactory();
                // 获取group列表
                _resetGroupList(res.data, res.data.commonProps, res.data.customProps, res.data.selSalesType);
                // 获取product列表
                _resetProductList(res.data, res.data.commonProps, res.data.customProps, res.data.selSalesType);

                defer.resolve (res);
            });
            return defer.promise;
        }

        function exportFile (data) {
            data = resetSearchInfo(data);
            $.download.post(cActions.cms.search.$searchAdvanceService.root + cActions.cms.search.$searchAdvanceService.exportProducts, {params: JSON.stringify(data)});
        }

        /**
         * 检索group
         * @param data
         * @returns {*}
         */
        function getGroupList(data, pagination, list, commonProps, customProps, selSalesTypes) {
            var defer = $q.defer();

            $searchAdvanceService.getGroupList(resetGroupPagination(data, pagination)).then(function (res) {
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
            $searchAdvanceService.getProductList(resetProductPagination(data, pagination)).then(function (res) {
                _resetProductList(res.data, commonProps, customProps, selSalesTypes);
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索group和product
         * @param data
         * @returns {*}
         */
        function addFreeTag(tagPath, prodIdList) {
            var defer = $q.defer();
            var data = {"tagPath":tagPath, "prodIdList":prodIdList};

            $searchAdvanceService.addFreeTag(data).then(function (res) {
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

            searchInfo.labelType = _returnKey(searchInfo.labelType);
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
        function _resetGroupList (data, commonProps, customProps, selSalesTypes) {
            tempGroupSelect.clearCurrPageRows();
            _.forEach(data.groupList, function (groupInfo, index) {

                var commArr = [];
                _.forEach(commonProps, function (data) {
                    var itemVal = groupInfo.fields[data.propId];
                    if (itemVal == undefined) {
                        itemVal = "";
                    }
                    commArr.push({value: itemVal});
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
                tempGroupSelect.currPageRows({"id": groupInfo.prodId, "code": groupInfo.fields["code"], "prodIds": data.grpProdIdList[index]});

                // 设置Inventory Detail
                // TODO 因为group显示的时候只返回了主商品的信息,所以无法拿到下面所有product的库存.
                //groupInfo.inventoryDetail = _setInventoryDetail(groupInfo.skus);

                // 设置price detail
                groupInfo.groupBean.priceDetail = _setPriceDetail(groupInfo.groupBean);

                groupInfo.groupBean.priceSale = _setPriceSale(groupInfo.groupBean);

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
        function _resetProductList (data, commonProps, customProps, selSalesTypes) {
            tempProductSelect.clearCurrPageRows();
            _.forEach(data.productList, function (productInfo, index) {
                var commArr = [];
                _.forEach(commonProps, function (data) {
                    var itemVal = productInfo.fields[data.propId];
                    if (itemVal == undefined) {
                        itemVal = "";
                    }
                    commArr.push({value: itemVal});
                });
                productInfo.commArr = commArr;
                var custArr = [];
                _.forEach(customProps, function (data) {
                    var itemVal = productInfo.feed.cnAtts[data.feed_prop_original];
                    var orgAttsitemVal= productInfo.feed.orgAtts[data.feed_prop_original];
                    if (itemVal == undefined) {
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
                productInfo.selSalesTyeArr = selSalesTyeArr;

                // 初始化数据选中需要的数组
                tempProductSelect.currPageRows({"id": productInfo.prodId, "code": productInfo.fields["code"]});

                // 设置Inventory Detail
                productInfo.inventoryDetail = _setInventoryDetail(productInfo.skus);

                // 设置sku销售渠道信息
                productInfo.skuDetail = _setSkuDetail(productInfo.skus);

                // 设置price detail
                productInfo.priceDetail = _setPriceDetail(productInfo.fields);

                productInfo.priceSale = _setPriceSale(productInfo.fields);

                // 设置time detail
                productInfo.groupBean.timeDetail = _setTimeDetail(productInfo);

                productInfo._prodChgInfo = data.prodChgInfoList[index];
                productInfo._prodOrgChaName = data.prodOrgChaNameList[index];

            });
            data.productSelList = tempProductSelect.selectRowsInfo;

            return data;
        }

        /**
         * 设置Inventory Detail
         * @param skus
         * @private
         */
        function _setInventoryDetail(skus) {
            var result = [];
            _.forEach(skus, function (sku) {
                result.push(sku.skuCode + ": " + (sku.qty ? sku.qty: 0));
            });
            return result;
        }

        /**
         * 设置sku的销售渠道信息
         * @param skus
         * @returns {Array}
         * @private
         */
        function _setSkuDetail(skus) {
            var result = [];
            _.forEach(skus, function (sku) {
                var cartInfo = "";
                _.forEach(sku.skuCarts, function (skuCart) {
                    var CartInfo = Carts.valueOf(parseInt(skuCart));
                    if (!_.isUndefined(CartInfo))
                        cartInfo += CartInfo.name + ",";
                });
                result.push(sku.skuCode + ": " + cartInfo.substr(0, cartInfo.length -1));
            });
            return result;
        }

        /**
         * 设置Price Detail
         * @param groups
         * @returns {Array}
         * @private
         */
        function _setPriceDetail(object) {
            var result = [];
            var tempMsrpDetail = _setOnePriceDetail($translate.instant('TXT_MSRP_WITH_COLON'), object.priceMsrpSt, object.priceMsrpEd);
            if (!_.isNull(tempMsrpDetail))
                result.push(tempMsrpDetail);

            // 设置retail price
            var tempRetailPriceDetail = _setOnePriceDetail($translate.instant('TXT_RETAIL_PRICE_WITH_COLON'), object.priceRetailSt, object.priceRetailEd);
            if (!_.isNull(tempRetailPriceDetail))
                result.push(tempRetailPriceDetail);

            return result;
        }

        /**
         * 设置页面上显示的价格
         * @param object
         * @returns {*}
         * @private
         */
        function _setPriceSale(object) {
            if (object.priceSaleSt == object.priceSaleEd)
                return object.priceSaleSt != null ? $filter('number')(object.priceSaleSt, 2) : '0.00';
            else
                return $filter('number')(object.priceSaleSt, 2) + '~' + $filter('number')(object.priceSaleEd, 2);
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
    }
});