/**
 * @description 高级检索
 * @author piao
 */
define([
    'cms',
    'modules/cms/directives/navBar.directive'
], function (cms) {

    cms.controller('usProductSearchController', class UsProductSearchController {

        constructor(popups, advanceSearch,selectRowsFactory,$parse) {
            let self = this;

            self.popups = popups;
            self.srInstance = new selectRowsFactory();
            self.$parse = $parse;
            self.advanceSearch = advanceSearch;

            self.pageOption = {curr: 1, total: 0, size: 10, fetch: function(){
                self.search();
            }};
            self.searchInfo = {}; // 检索条件
            self.searchResult = {
                productList: []
            };
            self.productSelList =  {selList: []};
            self.masterData = {
                platforms: [],
                usPlatforms: [],
                brandList: [],
                platformStatus:[
                    {status:'Pending',display:'Pending'},
                    {status:'OnSale', display:'List'},
                    {status:'InStock', display:'Delist'}
                ]
            };
            self.customColumns = {
                selCommonProps:[],
                selPlatformAttributes:[],
                selPlatformSales:[]
            };
            self.columnArrow = {};
        }

        init() {
            let self = this;
            // 查询masterData,包括platforms、brandList
            this.advanceSearch.init().then(res => {
                if (res.data) {
                    // 美国平台、中国平台
                    let channelPlatforms = res.data.platforms;
                    self.masterData.platforms = _.filter(channelPlatforms, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartId >= 20 && cartId < 928;
                    });
                    self.masterData.usPlatforms = _.filter(channelPlatforms, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartId > 0 && cartId < 20;
                    });
                    // 品牌列表
                    self.masterData.brandList = res.data.brandList;

                    // 用户自定义列
                    self.customColumns.commonProps = res.data.commonProps;
                    self.customColumns.platformAttributes = res.data.platformAttributes;
                    self.customColumns.platformSales = res.data.platformSales;
                    self.customColumns.selCommonProps = self.getselectedProps(res.data.commonProps,res.data.selCommonProps,'propId');
                    self.customColumns.selPlatformAttributes = self.getselectedProps(res.data.platformAttributes, res.data.selPlatformAttributes,'value');
                    //self.customColumns.selPlatformSales = self.getselectedProps(res.data.platformSales,res.data.selPlatformSales);

                    console.log(self.customColumns.selPlatformAttributes);

                }
            });
            this.search();
        }

        search() {
            let self = this;
            let searchInfo = self.handleQueryParams();

            self.srInstance.clearCurrPageRows();
            self.advanceSearch.search(searchInfo).then(res => {
                if (res.data) {
                    self.searchResult.productList = res.data.productList;
                    self.pageOption.total = res.data.productListTotal;

                    self.searchResult.productList.forEach(productInfo => {
                        self.srInstance.currPageRows({"id": productInfo.prodId, "code": productInfo.common.fields["code"]});
                    });

                    self.productSelList = self.srInstance.selectRowsInfo;
                }
            });
        }

        /**
         * @description 此方法可优化 先偷个懒
         * @param array  总数组
         * @param selectedArray 用户选择数组
         * @returns {Array}
         */
        getselectedProps(array,selectedArray,attrName){
            let result = [];

            if(!selectedArray || selectedArray.length === 0)
                return result;

            selectedArray.forEach(prop => {

                let obj = array.find(item => {
                    return item[attrName] === prop;
                });

                result.push(obj);

            });

            return result;

        }

        getProductValue(element,prop){
            let self = this,
                attrName;

            if(prop.hasOwnProperty('propId'))
                attrName = 'propId';
            else
                attrName = 'value';

            let _func = self.$parse(prop[attrName]);

            //console.log('attr',);

            return _func(element) ? _func(element) : '';

        }

        // 处理请求参数
        handleQueryParams() {
            let self = this;
            let searchInfo = angular.copy(self.searchInfo);
            if (!searchInfo) {
                searchInfo = {};
            }
            // codeList 换行符分割
            if (searchInfo.codeList) {
                let codeList = searchInfo.codeList.split("\n");
                searchInfo.codeList = codeList;
            }
            // 处理平台状态
            if (searchInfo.platformStatus) {
                let platformStatusObj = _.pick(searchInfo.platformStatus, function (value, key, object) {
                    return value;
                });
                searchInfo.platformStatus = _.keys(platformStatusObj);
            }

            // 分页参数处理
            _.extend(searchInfo, {productPageNum:self.pageOption.curr, productPageSize:self.pageOption.size});
            return searchInfo;
        }

        clear() {
            let self = this;
            self.searchInfo = {};
        }

        // 自定义列弹出
        popCustomAttributes() {
            let self = this;
            self.popups.openCustomAttributes().then(res => {
                self.customColumns.selCommonProps = self.getselectedProps(self.customColumns.commonProps,res.selCommonProps,'propId');
                self.customColumns.selPlatformAttributes = self.getselectedProps(self.customColumns.platformAttributes, res.selPlatformAttributes,'value');
                //self.customColumns.selPlatformSales = self.getselectedProps(res.data.platformSales,res.data.selPlatformSales);
            })
        }

        popBatchPrice(cartId) {
            let self = this;

            self.popups.openBatchPrice({
                selAll:self._selall,
                codeList:self.getSelectedProduct('code'),
                queryMap:self.handleQueryParams(),
                cartId:cartId? cartId :0
            }).then(res => {
                //根据返回参数确定勾选状态,"1",需要清除勾选状态,"0"不需要清除勾选状态
            });
        }

        popUsFreeTag() {
            let self = this;
            let params = {
                orgFlg: '0',
                tagType: '6',
                selAllFlg: '0',
                selCodeList: [],
                searchInfo: {}
            };
            self.popups.openUsFreeTag(params).then(res => {
                console.log(res);
            });
        }

        /**
         * @description 类目pop框
         */
        popCategory() {
            let self = this;

            if (Number(self.searchInfo.cartId) === 5) {
                //只有亚马逊显示类目
                self.popups.openAmazonCategory({cartId: 5}).then(res => {

                });
            } else {
                //sneakerhead 显示店铺内分类
                self.popups.openUsCategory({cartId: self.searchInfo.cartId, from: ''}).then(res => {

                });
            }
        }

        canCategory() {
            const arr = ['1', '12', '6', '11', '5'];

            return arr.indexOf(this.searchInfo.cartId) < 0;
        }

        batchCategory(){
            let self = this;

            console.log(self.getSelectedProduct());

        }

        /**
         * 获取选中产品 getSelectedProduct('code')
         * @param  id  or code
         * @returns {Array}
         */
        getSelectedProduct(onlyAttr){
            let self = this;

            if(onlyAttr){
                return _.pluck(self.productSelList.selList,onlyAttr);
            }else{
                return self.productSelList.selList;
            }
        }

        clearSelList(){
            this.srInstance.clearSelectedList();
        }

        //进行上下架操作
        batchList(cartId,activeStatus,usPlatformName){
            let self = this;

            self.popups.openUsList({
                selAll:self._selall,
                codeList:self.getSelectedProduct('code'),
                queryMap:self.handleQueryParams(),
                cartId:cartId? cartId :0,
                //操作状态1为上架,0为下架
                activeStatus:activeStatus,
                usPlatformName:usPlatformName
            }).then(res => {

            });
        }

        /**
         * 检索列排序
         * */
        columnOrder (columnName) {
            let self  = this,
                column,
                columnArrow = self.columnArrow;

            _.forEach(columnArrow, function (value, key) {
                if (key != columnName)
                    columnArrow[key] = null;
            });

            column = columnArrow[columnName];

            if (!column) {
                column = {};
                column.mark = 'unsorted';
                column.count = null;
            }

            column.count = !column.count;

            //偶数升序，奇数降序
            if (column.count)
                column.mark = 'sort-desc';
            else
                column.mark = 'sort-up';

            columnArrow[columnName] = column;

            self.searchByOrder(columnName, column.mark);
        };

        getArrowName(columnName) {
            let columnArrow = this.columnArrow;

            if (!columnArrow || !columnArrow[columnName])
                return 'unsorted';

            return columnArrow[columnName].mark;
        };

        searchByOrder(columnName, sortOneType) {
            let self = this,
                searchInfo = self.searchInfo;

            searchInfo.sortOneName = columnName;
            searchInfo.sortOneType = sortOneType == 'sort-up' ? '1' : '-1';

            self.search();

        }

        /**
         * 弹出usFreeTags修改框
         */
        popUpdateFreeTags() {

            let self = this;
            console.log(self._selall);
            let params = {
                orgFlg: '2',
                tagType: '6',
                selAllFlg: '0',
                selCodeList: self.getSelectedProduct("code"),
                searchInfo: self.handleQueryParams()
            };
            self.popups.openUsFreeTag(params).then(res => {
               console.log(res);
            });
        }

    });

});