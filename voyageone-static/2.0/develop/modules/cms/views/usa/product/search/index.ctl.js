/**
 * @description 高级检索
 * @author piao
 */
define([
    'cms',
    'modules/cms/directives/navBar.directive'
], function (cms) {

    cms.controller('usProductSearchController', class UsProductSearchController {

        constructor(popups,advanceSearch) {
            let self = this;

            self.popups = popups;
            self.advanceSearch = advanceSearch;

            self.pageOption = {curr: 1, total: 0, size: 10, fetch: self.search},
            self.searchInfo = {};
            self.searchResult = {
                productList:[]
            };

            self.masterData = {
                platforms:[],
                brandList:[]
            };
        }

        init(){
            let self = this;
            // 查询masterData,包括platforms、brandList
            this.advanceSearch.init().then(res => {
               if (res.data) {
                    self.masterData.platforms = res.data.platforms;
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
            console.log(searchInfo.codeList);
            _.extend(searchInfo, self.pageOption);
            // console.log(searchInfo);
            self.advanceSearch.search(searchInfo).then(res => {
                if (res.data) {
                    self.searchResult.productList = res.data.productList;
                }
                console.log(self.searchResult.productList);
            });
        }

        // 自定义列弹出
        popCustomAttributes(){
            let self = this;

            self.popups.openCustomAttributes().then(res => {

            })
        }

        popBatchPrice() {
            let self = this;

            self.popups.openBatchPrice().then(res => {

            });
        }

    });

});