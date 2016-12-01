define([
    'cms',
    './sortEnum'
], function (cms, sortEnum) {

    cms.controller("newCategoryController", (function () {

        function NewCategoryCtl($routeParams, productTopService, alert, confirm, notify) {
            this.routeParams = angular.fromJson($routeParams.cartInfo);
            this.sortList = sortEnum.getSortByCd(this.routeParams.cartId);
            this.productTopService = productTopService;
            this.searchInfo = {};
            this.sort = {};
            this.codeStr = '';
            this.isSeachAdd = false;
            this.paging = {
                curr: 1, total: 0, size: 10, fetch: this.goPage.bind(this)
            };
            this.notify = notify;
            this.alert = alert;
            console.log($routeParams);
        }

        NewCategoryCtl.prototype.init = function () {
            //初始化方法
            var self = this,
                productTopService = self.productTopService,
                routeParams = self.routeParams;
            productTopService.init({catId: routeParams.catId}).then(function (res) {
                self.brandList = res.data.brandList;

                self.sort = _.find(self.sortList, function (ele) {
                    return ele.sValue == res.data.sortColumnName;
                });
                if (self.sort)
                    self.sort.sortType = res.data.sortType;
            });
            self.search();
            self.getTopList();
        };

        NewCategoryCtl.prototype.clear = function () {
            //清空查询条件
            this.searchInfo = {};
            this.codeStr = "";
        };

        NewCategoryCtl.prototype.search = function (sortInfo) {
            //查询
            var self = this;
            this.goPage(1, this.paging.size, sortInfo);
            var data = this.getSearchInfo();
            this.productTopService.getCount(data).then(function (res) {
                self.paging.total = res.data;
                console.log(data);
            });
        };

        NewCategoryCtl.prototype.getSearchInfo = function () {
            //获取搜索条件
            var self = this;

            var upEntity = angular.copy(self.searchInfo);

            upEntity.cartId = this.routeParams.cartId;

            upEntity.sellerCatId = this.routeParams.catId;

            upEntity.sellerCatPath = this.routeParams.catPath;

            upEntity.codeList = self.codeStr.split("\n");

            return upEntity;
        };

        NewCategoryCtl.prototype.goPage = function (pageIndex, size, sortInfo) {
            //跳转到指定页
            var self = this;
            var data = this.getSearchInfo();
            data.pageIndex = pageIndex;
            data.pageSize = size;
            if (sortInfo) {
                data.sortColumnName = sortInfo.sortColumnName;
                data.sortType = sortInfo.sortType;
            }
            this.productTopService.getPage(data).then(function (res) {
                self.modelList = res.data;
            });
        }


        NewCategoryCtl.prototype.sortSearch = function (sortColumnName, sortType) {
            //排序字段保存 搜索
            var self = this,
                _sort,
                routeParams = self.routeParams;

            if (!sortColumnName || !sortType)
                return;

            _sort = _.find(self.sortList, function (ele) {
                return ele.sValue == sortColumnName.replace("✓", routeParams.cartId);
            });

            if (self.sort)
                _sort.sortType = sortType;

            self.sort = _sort;

            //调用搜索
            this.search({sortColumnName: _sort.sValue, sortType: sortType});
        };
        NewCategoryCtl.prototype.selectAll = function ($event) {
            //全选
            var checkbox = $event.target;
            for (var i = 0; i < this.modelList.length; i++) {
                this.modelList[i].isChecked = checkbox.checked;
            }
        };
        NewCategoryCtl.prototype.getSelectedCodeList = function () {
            //获取选中商品的code
            var codeList = [];
            if (this.modelList) {
                var lenght = this.modelList.length;
                for (var i = 0; i < lenght; i++) {
                    if (this.modelList[i].isChecked) {
                        codeList.push(this.modelList[i].code);
                    }
                }
            }
            return codeList;
        };

        NewCategoryCtl.prototype.addTopProductClick = function () {
            //加入置顶区
            var parameter = {};
            if (this.isSeachAdd) {
                //全量加入
                parameter.isSeachAdd = this.isSeachAdd;
                parameter.searchParameter = this.getSearchInfo();
            }
            else {
                var codeList = this.getSelectedCodeList();
                if (codeList.length == 0) {
                    this.alert("请选择商品");
                }
                parameter.codeList = codeList;
            }
            parameter.cartId = this.routeParams.cartId;
            parameter.sellerCatId = this.routeParams.catId;


            var self = this;
            this.productTopService.addTopProduct(parameter).then(function (res) {
                self.search();
                self.getTopList();
                self.notify.success('提交成功');
            });
        }

        NewCategoryCtl.prototype.getTopList = function () {
            //获取置顶区商品信息
            var self = this;
            var parameter = {};
            parameter.cartId = this.routeParams.cartId;
            parameter.sellerCatId = this.routeParams.catId;
            this.productTopService.getTopList(parameter).then(function (res) {
                self.topList = res.data;
            });
        };

        NewCategoryCtl.prototype.getTopCodeList = function () {
            //获取置顶区商品的code
            var codeList = [];
            if (this.topList) {
                var lenght = this.topList.length;
                for (var i = 0; i < lenght; i++) {
                    codeList.push(this.topList[i].code);
                }
            }
            return codeList;
        }
        NewCategoryCtl.prototype.saveTopProduct = function () {
            //保存置顶区商品
            var self = this;
            var parameter = {};
            parameter.cartId = this.routeParams.cartId;
            parameter.sellerCatId = this.routeParams.catId;
            parameter.codeList = this.getTopCodeList();
            this.productTopService.saveTopProduct(parameter).then(function (res) {
                self.notify.success('保存成功');
            });
        }

        NewCategoryCtl.prototype.clearTopProduct = function () {
            //置顶区清空
            this.topList = [];
        }
        NewCategoryCtl.prototype.remove = function (index) {
            this.topList.splice(index, 1);
        };

        NewCategoryCtl.prototype.moveKeys = {
            up: 'up',
            upToTop: 'upToTop',
            down: 'down',
            downToLast: 'downToLast'
        };

        NewCategoryCtl.prototype.moveProduct = function moveProduct(i, moveKey) {
            var self = this,
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

        return NewCategoryCtl;

    })());

});