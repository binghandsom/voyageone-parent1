/**
 * @description 美国店铺内分类
 * @author piao
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    './sortEnum',
    'modules/cms/directives/navBar.directive'
], function (cms, carts, sortEnum) {

    cms.controller('usCategoryController',class UsCategoryController{

        constructor($routeParams,advanceSearch, productTopService, alert, notify, confirm, $filter,popups,selectRowsFactory){
            let self = this;

            self.$routeParams = $routeParams;
            self.srInstance = new selectRowsFactory();
            self.productTopService = productTopService;
            self.alert = alert;
            self.notify = notify;
            self.confirm = confirm;
            self.$filter = $filter;
            self.advanceSearch = advanceSearch;
            self.tempUpEntity = {};
            self.sort = {};
            self.searchResult = {};
            self.productSelList =  {selList: []};
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

        // 处理请求参数
        handleQueryParams() {
            let self = this;
            let searchInfo = angular.copy(self.searchInfo);
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
                let platformStatusObj = _.pick(searchInfo.platformStatus, function (value, key, object) {
                    return value;
                });
                searchInfo.platformStatus = _.keys(platformStatusObj);
            }

            //处理类目和店铺内分类
            if(self.tempUpEntity.pCatPathListTmp)
            // _.extend(searchInfo, {pCatPathType:1}); // 1 in, 2 not in
                searchInfo.pCatPathList = _.pluck(self.tempUpEntity.pCatPathListTmp,'catPath');
            if(self.tempUpEntity.cidValueTmp)
            // _.extend(searchInfo, {shopCatType:1}); // 1 in, 2 not in
                searchInfo.cidValue = _.pluck(self.tempUpEntity.cidValueTmp,'catId');

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

            // 分页参数处理
            _.extend(searchInfo, {productPageNum:self.pageOption.curr, productPageSize:self.pageOption.size});
            return searchInfo;
        }

        clear() {
            let self = this;
            self.searchInfo = {
                brandSelType:1,
                pCatPathType:1,
                shopCatType:1
            };
        }

        dismiss(attrName){
            this.searchInfo[attrName] = null;
        }

        search(){
            let self = this,
                data = self.getSearchInfo();
              //  sort = self.sort;

           // if (sort) {
           //     data.sortColumnName = sort.sValue;
           //     data.sortType = sort.sortType;
           // }

            self.srInstance.clearCurrPageRows();
            self.advanceSearch.search(data).then(res => {
                if (res.data) {
                    self.searchResult.productList = res.data.productList;
                    self.pageOption.total = res.data.productListTotal;

                    self.searchResult.productList.forEach(productInfo => {
                       // self.setFreeTagList(productInfo);
                        self.srInstance.currPageRows({"id": productInfo.prodId, "code": productInfo.common.fields["code"]});
                    });

                    self.productSelList = self.srInstance.selectRowsInfo;

                }
            });

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

        /**
         * 批量操作前判断是否选中
         * @param cartId
         * @param callback
         */
        $chkProductSel(cartId, callback) {
            let self = this;

            if (cartId === null || cartId === undefined) {
                cartId = 0;
            } else {
                cartId = parseInt(cartId);
            }

            let selList;

            if (!self._selall) {
                selList = self.getSelectedProduct('code');
                if (selList.length === 0) {
                    self.alert(self.$translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                    return;
                }
                callback(cartId, selList);
            } else {
                if (self.pageOption.total === 0) {
                    self.alert(self.$translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                    return;
                }

                self.confirm(`您已启动“检索结果全量”选中机制，本次操作对象为检索结果中的所有产品<h3>修改记录数:&emsp;<span class='label label-danger'>${self.pageOption.total}</span></h3>`).then(function () {
                    callback(cartId);
                });
            }
        }

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

        // 自定义列弹出
        popCustomAttributes() {
            let self = this;
            self.popups.openCustomAttributes().then(res => {
                self.customColumnNames = {};
                self.customColumns.selCommonProps = self.getSelectedProps(self.customColumns.commonProps,res.selCommonProps,'propId');
                self.customColumns.selPlatformAttributes = self.getSelectedProps(self.customColumns.platformAttributes, res.selPlatformAttributes,'value');
                self.customColumns.selPlatformSales = res.selPlatformSales;
            })
        }

        popBatchPrice(cartId) {
            let self = this;
            if(self.getSelectedProduct('code').length == 0){
                self.alert("please choose at least one!!!");
                return;
            }
            self.popups.openBatchPrice({
                selAll:self._selall,
                codeList:self.getSelectedProduct('code'),
                queryMap:self.handleQueryParams(),
                cartId:cartId? cartId :0
            }).then(res => {
                //根据返回参数确定勾选状态,"1",需要清除勾选状态,"0"不需要清除勾选状态
                if(res.success == "1"){
                    //需要清除勾选状态
                    self.clearSelList();
                    self._selall = 0;
                }
                if(res.type == 1){
                    self.notify.success('Update Success');
                }else {
                    self.alert('Update Defeated');
                }
            });
        }

        // 批量修改Free tags
        /**
         * 添加产品到指定自由标签
         */
        addFreeTag () {
            let self = this;
            let selCodeList = self.getSelectedProduct('code');
            if (!self._selall && _.size(selCodeList) == 0) {
                self.alert("Please select at least one record.");
                return;
            }
            let params = {
                orgFlg: '2',
                selTagType: '6',
                selAllFlg: self._selall ? 1 : 0,
                selCodeList: self.getSelectedProduct('code'),
                searchInfo: self.handleQueryParams()
            };
            self.popups.openUsFreeTag(params).then(res => {
                let msg = '';
                if (_.size(res.selectdTagList) > 0) {
                    let freeTagsTxt = _.chain(res.selectdTagList).map(function (key, value) {
                        return key.tagPathName;
                    }).value();
                    msg = "Set free tags for selected products:<br>" + freeTagsTxt.join('; ');
                } else {
                    msg = "Clear free tags for selected products";
                }
                let freeTags = _.chain(res.selectdTagList).map(function (key, value) {
                    return key.tagPath;
                }).value();
                self.confirm(msg)
                    .then(function () {
                        var data = {
                            "type":"usa",
                            "tagPathList": freeTags,
                            "prodIdList": selCodeList,
                            "isSelAll": self._selall ? 1 : 0,
                            "orgDispTagList": res.orgDispTagList,
                            'searchInfo': self.handleQueryParams()
                        };
                        self.$searchAdvanceService2.addFreeTag(data).then(function () {
                            // notify.success($translate.instant('TXT_MSG_SET_SUCCESS'));
                            self.notify.success("Set free tags succeeded.");
                            self.clearSelList();
                            self._selall = false;
                            self.search();
                        })
                    });
            });
        };

        //进行上下架操作
        batchList(cartId,activeStatus,usPlatformName){
            let self = this;
            if(self.getSelectedProduct('code').length == 0){
                self.alert("please choose at least one!!!");
                return;
            }
            self.popups.openUsList({
                selAll:self._selall,
                codeList:self.getSelectedProduct('code'),
                queryMap:self.handleQueryParams(),
                cartId:cartId? cartId :0,
                //操作状态1为上架,0为下架
                activeStatus:activeStatus,
                usPlatformName:usPlatformName
            }).then(res => {
                if(res.success == "1"){
                    //需要清除勾选状态
                    self.clearSelList();
                    self._selall = 0;
                }
                self.notify.success('Update Success');
            });
        }

        /**
         * 获取自由标签tagName
         * @param productInfo
         */
        setFreeTagList(productInfo){
            let self = this,
                _usFreeTags = [];

            productInfo.usFreeTags.forEach(tag => {
                let _tag = self.masterData.freeTags[tag];

                _usFreeTags.push(_tag.tagName);
            });

            productInfo._usFreeTags = _usFreeTags;

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

    });

});