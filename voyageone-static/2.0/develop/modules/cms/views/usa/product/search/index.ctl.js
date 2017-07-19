/**
 * @description 高级检索
 * @author piao
 */
define([
    'cms',
    'modules/cms/directives/navBar.directive'
], function (cms) {

    cms.controller('usProductSearchController', class UsProductSearchController {

        constructor(popups, advanceSearch,selectRowsFactory) {
            let self = this;

            self.popups = popups;
            self.srInstance = new selectRowsFactory();
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
                    self.customColumns.selCommonProps = res.data.selCommonProps == null ? [] : res.data.selCommonProps;
                    self.customColumns.selPlatformAttributes = res.data.selPlatformAttributes == null ? [] : res.data.selPlatformAttributes;
                    self.customColumns.selPlatformSales = res.data.selPlatformSales == null ? [] : res.data.selPlatformSales;
                }
            });
            this.search();
        }

        search() {
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

        clear() {
            let self = this;
            self.searchInfo = {};
        }

        // 自定义列弹出
        popCustomAttributes() {
            let self = this;
            self.popups.openCustomAttributes().then(res => {
                self.customColumns.selCommonProps = res.selCommonProps;
                self.customColumns.selPlatformAttributes = res.selPlatformAttributes;
                self.customColumns.selPlatformSales = res.selPlatformSales;
            })
        }

        popBatchPrice() {
            let self = this;

            self.popups.openBatchPrice({
                selAll:"false",
                codeList:["000009515"],
                queryMap:{},
                cartId:1
            }).then(res => {

            });
        }

        popUsFreeTag() {
            let self = this;

            self.popups.openUsFreeTag({
                orgFlg: 2,
                tagTypeSel: '4',
                cartId: 23,
                productIds: null,
                selAllFlg: 0,
                searchInfo: self.searchInfoBefo
            }).then(res => {

            })
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

        }

        /**
         * 获取选中产品
         * @param onlyAttr 按照属性名抽出数组
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

        batchList(){
            let self = this;

            self.popups.openUsList().then(res => {

            });
        }

    });

});