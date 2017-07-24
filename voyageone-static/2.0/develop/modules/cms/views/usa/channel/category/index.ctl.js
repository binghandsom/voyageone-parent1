/**
 * @description 美国店铺内分类
 * @author piao
 */
define([
    'cms',
    './sortEnum'
], function (cms, sortEnum) {

    cms.controller('usCategoryController',class UsCategoryController{

        constructor($routeParams, productTopService, alert, notify, confirm, $filter,popups){
            let self = this;

            self.$routeParams = $routeParams;
            self.productTopService = productTopService;
            self.alert = alert;
            self.notify = notify;
            self.confirm = confirm;
            self.$filter = $filter;
            self.catInfo = angular.fromJson(this.$routeParams.category);
            self.popups = popups;
            self.searchInfo = {
                cartId:8,
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

            upEntity.sellerCatId = self.catInfo.catId;
            upEntity.sellerCatPath = self.catInfo.catPath;
            upEntity.codeList = upEntity.codeList.split("\n");
            upEntity.platformStatus = _.chain(upEntity.platformStatus).map((value,key) => {
                if(value)
                    return key;
            }).filter(item => {return item}).value();

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

            self.productTopService.getPage(_.extend(paging, data)).then(function (res) {

                self.modelList = res.data;

                console.log(self.modelList);
            });

            productTopService.getCount(self.getSearchInfo()).then(function (res) {
                self.paging.total = res.data;
            });

            this.selAll = false;
        }

        addTopProductClick() {
            let self = this,
                routeParams = self.routeParams,
                confirm = self.confirm,
                parameter = {};

            if (self.isSeachAdd) {
                //全量加入
                parameter.isSeachAdd = self.isSeachAdd;
                parameter.searchParameter = self.getSearchInfo();
            } else {
                let codeList = self.getSelectedCodeList();
                if (codeList.length == 0) {
                    self.alert("请选择商品");
                    return;
                }
                parameter.codeList = codeList;
            }

            parameter.cartId = routeParams.cartId;
            parameter.sellerCatId = routeParams.catId;

            if (self.isSeachAdd) {
                confirm("您是否要全量移入置顶区").then(function () {
                    self.callAddTopProduct(parameter);
                });
            } else
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