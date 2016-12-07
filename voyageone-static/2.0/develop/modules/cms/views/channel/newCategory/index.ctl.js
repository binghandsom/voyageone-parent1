define([
    'cms',
    './sortEnum'
], function (cms, sortEnum) {

    cms.controller("newCategoryController", (function () {

        function NewCategoryCtl($routeParams, productTopService, alert, notify) {
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

        /**
         * 查询操作  调用查询结果集和获取总数两个接口
         * @param sortInfo
         */
        NewCategoryCtl.prototype.search = function (sortInfo) {
            var self = this,
                productTopService = self.productTopService;

            self.goPage(1, self.paging.size, sortInfo);

            productTopService.getCount(self.getSearchInfo()).then(function (res) {
                self.paging.total = res.data;
            });
        };

        /**
         * @returns 返回查询上行参数
         */
        NewCategoryCtl.prototype.getSearchInfo = function () {
            var self = this,
                upEntity = angular.copy(self.searchInfo);

            upEntity.cartId = this.routeParams.cartId;
            upEntity.sellerCatId = this.routeParams.catId;
            upEntity.sellerCatPath = this.routeParams.catPath;
            upEntity.codeList = self.codeStr.split("\n");

            return upEntity;
        };

        /**
         * 跳转到指定页
         * @param pageIndex
         * @param size
         * @param sortInfo
         */
        NewCategoryCtl.prototype.goPage = function (pageIndex, size, sortInfo) {
            var self = this,
                data = this.getSearchInfo();

            data.pageIndex = pageIndex;
            data.pageSize = size;
            if (sortInfo) {
                data.sortColumnName = sortInfo.sortColumnName;
                data.sortType = sortInfo.sortType;
            }
            this.productTopService.getPage(data).then(function (res) {
                self.modelList = res.data;
            });
        };

        /**
         * 排序字段保存 搜索
         * @param sortColumnName
         * @param sortType
         */
        NewCategoryCtl.prototype.sortSearch = function (sortColumnName, sortType) {
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

            self.search({sortColumnName: sortColumnName.replace("✓", routeParams.cartId), sortType: sortType});
        };

        /**
         * 普通商品区全选操作
         * @param $event
         */
        NewCategoryCtl.prototype.selectAll = function ($event) {
            var checkbox = $event.target;

            for (var i = 0; i < this.modelList.length; i++) {
                this.modelList[i].isChecked = checkbox.checked;
            }
        };

        /**
         * 获取选中商品的code
         * @returns Array
         */
        NewCategoryCtl.prototype.getSelectedCodeList = function () {

            var codeList = [];

            if (this.modelList && this.modelList.length > 0) {
                for (var i = 0, length = this.modelList.length; i < length; i++) {
                    if (this.modelList[i].isChecked) {
                        codeList.push(this.modelList[i].code);
                    }
                }
            }

            return codeList;
        };

        /**
         * 加入置顶区
         */
        NewCategoryCtl.prototype.addTopProductClick = function () {
            var self = this,
                routeParams = self.routeParams,
                parameter = {};

            if (this.isSeachAdd) {
                //全量加入
                parameter.isSeachAdd = this.isSeachAdd;
                parameter.searchParameter = this.getSearchInfo();
            } else {
                var codeList = this.getSelectedCodeList();
                if (codeList.length == 0) {
                    this.alert("请选择商品");
                }
                parameter.codeList = codeList;
            }

            parameter.cartId = routeParams.cartId;
            parameter.sellerCatId = routeParams.catId;

            self.productTopService.addTopProduct(parameter).then(function () {
                self.search();
                self.getTopList();
                self.notify.success('提交成功');
            });
        };

        /**
         * 获取置顶区商品信息
         */
        NewCategoryCtl.prototype.getTopList = function () {
            var self = this,
                routeParams = self.routeParams;

            self.productTopService.getTopList({
                cartId: routeParams.cartId,
                sellerCatId: routeParams.catId
            }).then(function (res) {
                self.topList = res.data;
            });
        };

        /**
         * 获取置顶区商品的code
         * @returns {Array}
         */
        NewCategoryCtl.prototype.getTopCodeList = function () {
            var self = this,
                codeList = [];

            if (self.topList) {
                for (var i = 0, length = self.topList.length; i < length; i++) {
                    codeList.push(this.topList[i].code);
                }
            }

            return codeList;
        };

        /**
         * 保存置顶区商品
         */
        NewCategoryCtl.prototype.saveTopProduct = function () {
            var self = this,
                routeParams = self.routeParams;

            this.productTopService.saveTopProduct({
                cartId: routeParams.cartId,
                sellerCatId: routeParams.catId,
                codeList: self.getTopCodeList()
            }).then(function () {
                self.notify.success('保存成功');
            });
        };

        /**
         * 置顶区清空
         */
        NewCategoryCtl.prototype.clearTopProduct = function () {
            this.topList = [];
        };


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