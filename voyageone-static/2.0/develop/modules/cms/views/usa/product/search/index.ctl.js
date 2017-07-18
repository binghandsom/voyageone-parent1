/**
 * @description 高级检索
 * @author piao
 */
define([
           'cms',
           'modules/cms/directives/navBar.directive'
       ], function (cms) {

    cms.controller('usProductSearchController', class UsProductSearchController {

        constructor(popups, advanceSearch) {
            let self = this;

            self.popups = popups;
            self.advanceSearch = advanceSearch;

            self.pageOption = {curr: 1, total: 0, size: 10, fetch: self.search},
                self.searchInfo = {};
            self.searchResult = {
                productList: []
            };

            self.masterData = {
                platforms: [],
                usPlatforms: [],
                brandList: []
            };
        }

        init() {
            let self = this;
            // 查询masterData,包括platforms、brandList
            this.advanceSearch.init().then(res => {
                if (res.data) {
                    let channelPlatforms = res.data.platforms;
                    self.masterData.platforms = _.filter(channelPlatforms, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartId >= 20 && cartId < 928;
                    });
                    self.masterData.usPlatforms = _.filter(channelPlatforms, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartId > 0 && cartId < 20;
                    });
                    self.masterData.brandList = res.data.brandList;
                }
            });
            this.search();
        }

        search() {
            let self = this;
            let searchInfo = angular.copy(self.searchInfo);
            // codeList 换行符分割
            if (searchInfo.codeList) {
                let codeList = searchInfo.codeList.split("\n");
                searchInfo.codeList = codeList;
            }

            _.extend(searchInfo, self.pageOption);

            self.advanceSearch.search(searchInfo).then(res => {
                if (res.data) {
                    self.searchResult.productList = res.data.productList;
                }
            });
        }

        // 自定义列弹出
        popCustomAttributes() {
            let self = this;

            self.popups.openCustomAttributes().then(res => {

            })
        }

        popBatchPrice() {
            let self = this;

            self.popups.openBatchPrice().then(res => {

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

    });

});