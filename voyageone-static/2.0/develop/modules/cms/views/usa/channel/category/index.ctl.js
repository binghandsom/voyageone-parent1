/**
 * @description 美国店铺内分类
 * @author piao
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    './sortEnum'
], function (cms, carts, sortEnum) {

    cms.controller('usCategoryController',class UsCategoryController{

        constructor($routeParams,advanceSearch, productTopService, alert, notify, confirm, $filter,popups){
            let self = this;

            self.$routeParams = $routeParams;
            self.productTopService = productTopService;
            self.alert = alert;
            self.notify = notify;
            self.confirm = confirm;
            self.$filter = $filter;
            self.advanceSearch = advanceSearch;
            self.searchResult = {};
            self.catInfo = angular.fromJson(this.$routeParams.category);
            self.popups = popups;
            self.searchInfo = {
                cartId:carts.Sneakerhead.id,
                codeList:''
            };
            self.paging = {
                curr: 1, total: 0, size: 10, fetch: function () {
                    self.search();
                }
            };
            self.moveKeys = {
                up: 'up',
                upToTop: 'upToTop',
                down: 'down',
                downToLast: 'downToLast'
            };
            self.pageOption = {curr: 1, total: 0, size: 10, fetch: function(){
                self.search();
            }};
        }

        init(){
            let self = this,
                catInfo = self.catInfo;

            self.productTopService.init({catId: catInfo.catId}).then(function (res) {
                self.brandList = res.data.brandList;

              /*  self.sort = _.find(self.sortList, function (ele) {
                    return ele.sValue == res.data.sortColumnName;
                });
                if (self.sort)
                    self.sort.sortType = res.data.sortType;*/
            });

            self.search();
            self.getTopList();
        }

        getSearchInfo(){
            let self = this,
                upEntity = angular.copy(self.searchInfo);

            upEntity.cidValue = [self.catInfo.catId];
            upEntity.shopCatType = 1;
            upEntity.codeList = upEntity.codeList.split("\n");
            upEntity.platformStatus = _.chain(upEntity.platformStatus).map((value,key) => {
                if(value)
                    return key;
            }).filter(item => {return item}).value();

            _.extend(upEntity, {productPageNum:self.pageOption.curr, productPageSize:self.pageOption.size});
            console.log('upEntity',upEntity);

            return upEntity;
        }

        search(){
            let self = this,
                data = self.getSearchInfo(),
                paging = self.paging,
                sort = self.sort,
                productTopService = self.productTopService;

            if (sort) {
                data.sortColumnName = sort.sValue;
                data.sortType = sort.sortType;
            }

            self.advanceSearch.search(data).then(res => {
                if (res.data) {
                    self.searchResult.productList = res.data.productList;
                    self.pageOption.total = res.data.productListTotal;

                    self.searchResult.productList.forEach(productInfo => {
                        self.setFreeTagList(productInfo);
                        self.srInstance.currPageRows({"id": productInfo.prodId, "code": productInfo.common.fields["code"]});
                    });

                    // self.productSelList = self.srInstance.selectRowsInfo;
                }
            });

            // self.productTopService.getPage(_.extend(paging, data)).then(function (res) {
            //
            //     self.modelList = res.data;
            //
            //     console.log(self.modelList);
            // });
            //
            // productTopService.getCount(self.getSearchInfo()).then(function (res) {
            //     self.paging.total = res.data;
            // });

            this.selAll = false;
        }
        getTopList(){
            let self = this;
            self.productTopService.getTopList({"cartId":carts.Sneakerhead.id,"sellerCatId":self.catInfo.catId}).then(res => {
                if (res.data) {
                    self.topList = res.data;
                }
            });
        }
        addTopProductClick(productInfo) {
            let self = this,
                parameter = {};
                parameter.codeList = [productInfo.common.fields.code];

            parameter.cartId = carts.Sneakerhead.id;
            parameter.sellerCatId = self.catInfo.catId;
            self.callAddTopProduct(parameter);
        };

        callAddTopProduct(para) {
            let self = this;

            self.productTopService.addTopProduct(para).then(function () {
                self.search();
                self.getTopList();
                self.isSeachAdd = false;
                self.notify.success('提交成功');

            });
        };

        getTopCodeList() {
            let self = this,
                codeList = [];

            if (self.topList) {
                for (let i = 0, length = self.topList.length; i < length; i++) {
                    codeList.push(this.topList[i].code);
                }
            }

            return codeList;
        };

        /**
         * 保存置顶区商品
         */
        saveTopProduct() {
            let self = this,
                routeParams = self.routeParams;

            self.productTopService.saveTopProduct({
                cartId: routeParams.cartId,
                sellerCatId: routeParams.catId,
                codeList: self.getTopCodeList()
            }).then(function () {
                self.notify.success('保存成功');
                //刷新普通商品区
                self.search();
            });
        };

        moveProduct(i, moveKey) {
            let self = this,
                source = self.topList,
                moveKeys = self.moveKeys,
                temp;

            switch (moveKey) {
                case moveKeys.up:
                    if (i === 0)
                        return;
                    temp = source[i];
                    source[i] = source[i - 1];
                    source[i - 1] = temp;
                    return;
                case moveKeys.upToTop:
                    if (i === 0)
                        return;
                    temp = source.splice(i, 1);
                    source.splice(0, 0, temp[0]);
                    return;
                case moveKeys.down:
                    if (i === source.length - 1)
                        return;
                    temp = source[i];
                    source[i] = source[i + 1];
                    source[i + 1] = temp;
                    return;
                case moveKeys.downToLast:
                    if (i === source.length - 1)
                        return;
                    temp = source.splice(i, 1);
                    source.push(temp[0]);
                    return;
            }
        };

        popUsFreeTag() {
            let self = this;

            self.popups.openUsFreeTag({
                orgFlg: '1',
                tagType: '4',
                orgChkStsMap:self.searchInfo.usFreeTags
            }).then(res => {
                self.searchInfo.usFreeTags = _.pluck(res.selectdTagList,'tagPath');
            });
        }

    });

});