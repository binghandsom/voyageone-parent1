
/**
 * @description 由于angular service为单例模式
 *              该service产品详情页统一ajax入口
 */
define([
    'cms'
], function (cms) {

    class SearchUtilService {
        constructor($parse) {
            this.$parse = $parse;
        }

        // 处理请求参数
        handleQueryParams(selfScope) {
            let _this = selfScope,
                searchInfo = angular.copy(_this.searchInfo);

            if (!searchInfo) {
                searchInfo = {};
            }
            // codeList 换行符分割
            let codeList = [];
            if (searchInfo.codeList) {
                codeList = searchInfo.codeList.split("\n");
            }
            searchInfo.codeList = codeList;
            // 处理平台状态
            if (searchInfo.platformStatus) {
                let platformStatusObj = _.pick(searchInfo.platformStatus, function (value) {
                    return value;
                });
                searchInfo.platformStatus = _.keys(platformStatusObj);
            }

            //处理类目和店铺内分类
            if(_this.tempUpEntity.pCatPathListTmp)
                searchInfo.pCatPathList = _.pluck(_this.tempUpEntity.pCatPathListTmp,'catPath');

            if(_this.tempUpEntity.cidValueTmp)
                searchInfo.cidValue = _.pluck(_this.tempUpEntity.cidValueTmp,'catId');

            //价格范围排序修改
            if(searchInfo.sortOneName && searchInfo.sortOneName.split(',').length === 2){
                let _priceResult = '';

                if(searchInfo.sortOneType === '1'){
                    _priceResult = searchInfo.sortOneName.split(',')[0]
                }else{
                    _priceResult = searchInfo.sortOneName.split(',')[1];
                }

                _priceResult.replace(/P(\d)+([A-Z,a-z,\\.])+/g, function (match) {
                    _priceResult = match;
                });

                searchInfo.sortOneName = _priceResult.replace('.','_');
            }

            if(/^[-1,0-9]{1,}$/.test(searchInfo.sortOneName)){
                searchInfo.sortOneName = `P${searchInfo.sortOneName}_customSale`;
            }

            // 分页参数处理
            _.extend(searchInfo, {productPageNum:_this.pageOption.curr, productPageSize:_this.pageOption.size});
            return searchInfo;
        }

        /**
         * 显示自定义列的名称
         * @param array
         * @param selectedArray
         * @param attrName
         * @param selfScope
         * @returns {Array}
         */
        getSelectedProps(array,selectedArray,attrName,selfScope){
            let _this = selfScope,
                result = [];

            if(!selectedArray || selectedArray.length === 0)
                return result;

            selectedArray.forEach(prop => {

                let obj = array.find(item => {
                    if(attrName === 'value'){
                        if(item.value.split(",").length === 2){
                            return _.contains(item.value.split(","),prop.value);
                        }else{
                            return item.value === prop.value;
                        }
                    }else{
                        return item[attrName] === prop;
                    }

                });

                if(attrName === 'value'){
                    if(!_this.customColumnNames[obj.name]){
                        result.push(obj);
                        _this.customColumnNames[obj.name] = true;
                    }
                }else{
                    result.push(obj);
                }

            });

            return result;

        }

        /**
         * 获取自定义里
         * @param element
         * @param prop
         * @returns {*}
         */
        getProductValue(element,prop){
            let self = this,
                attrName;

            if(prop.hasOwnProperty('propId'))
                attrName = 'propId';
            else
                attrName = 'value';

            let valueStr = prop[attrName];

            if(valueStr.split(",").length === 2){
                let _func1 = self.$parse(valueStr.split(",")[0]),
                    _func2 = self.$parse(valueStr.split(",")[1]);

                let priceSt = _func1(element) ? _func1(element) : '',
                    priceEd = _func2(element) ? _func2(element) : '';

                if(priceSt === priceEd){
                    return priceSt;
                }else{
                    return `${priceSt} ~ ${priceEd}`;
                }

            }else{
                let _func = self.$parse(prop[attrName]),
                    result = _func(element);

                if(result){
                    if(prop.toLabel === 1){
                        result = result === '1' ? 'yes':'no';
                    }
                }else{
                    result = '';
                }

                return result;
            }
        }

        /**
         * @description 获取销量值
         * @param productInfo
         * @param prop
         */
        getPlatformSaleValue(productInfo,prop){
            let _result;

            if(prop.calculateFlag){
                _result = 'calculation...';
            }else{
                let result = productInfo.sales["P"+prop.cartId];

                _result = result? result: 0;

            }

            return _result;
        }

        /**
         * 批量操作前判断是否选中
         * @param cartId
         * @param callback
         */
        $chkProductSel(cartId, callback,selfScope) {
            let _this = selfScope;

            if (cartId === null || cartId === undefined) {
                cartId = 0;
            } else {
                cartId = parseInt(cartId);
            }

            let selList;

            if (!_this._selall) {
                selList = _this.getSelectedProduct('code');
                if (selList.length === 0) {
                    _this.alert(_this.$translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                    return;
                }
                callback(cartId, selList);
            } else {
                if (_this.pageOption.total === 0) {
                    _this.alert(_this.$translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                    return;
                }

                self.confirm(`You have started “Select All Items By Query Terms ”，This action object is the search result for all products<h3>Modify the number of records:&emsp;<span class='label label-danger'>${self.pageOption.total}</span></h3>`).then(function () {
                    callback(cartId);
                });
            }
        }


    }

    cms.service('searchUtilService', SearchUtilService);

});
