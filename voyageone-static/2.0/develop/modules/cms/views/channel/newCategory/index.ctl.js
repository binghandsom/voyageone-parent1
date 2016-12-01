define([
    'cms',
    './sortEnum'
], function (cms, sortEnum) {

    cms.controller("newCategoryController", (function () {

        function NewCategoryCtl($routeParams, productTopService) {
            var self = this;
            self.routeParams = angular.fromJson($routeParams.cartInfo);
            self.sortList = sortEnum.getSortByCd(self.routeParams.cartId);
            self.productTopService = productTopService;
            self.searchInfo = {};
            self.sort = {};
            self.codeStr = '';
            self.paging = {
                curr: 1, total: 0, size: 10, fetch:this.goPage.bind(this)
            };
            console.log(self.routeParams);
        }

        NewCategoryCtl.prototype.init = function () {
            var self = this,
                productTopService = self.productTopService,
                routeParams = self.routeParams;

           // self.getTopList();

            productTopService.init({catId: routeParams.catId}).then(function (res) {
                self.brandList = res.data.brandList;

                self.sort = _.find(self.sortList, function (ele) {
                    return ele.sValue == res.data.sortColumnName;
                });

                if (self.sort)
                    self.sort.sortType = res.data.sortType;
            });


        };
        // int cartId;//平台id
        // List<String> brandList;//品牌名称
        // boolean isInclude;//  brand是否包含
        // String compareType;
        // Integer quantity;//库存数量
        // String pCatId;//商品分类
        // List<String> codeList;//   款号/Code/SKU   换行分隔
        // String sortColumnName;// 排序列名称
        // int sortType;//排序类型   1：升序         -1：降序
        // int pageIndex;//当前页
        // int pageSize;//当前页行数
        NewCategoryCtl.prototype.search = function () {
            this.goPage(1, this.paging.size);
           var data= this.getSearchInfo();
            this.productTopService.getCount(data).then(function (res) {
                paging.total = res;
            });
        };
        NewCategoryCtl.prototype.getSearchInfo= function  () {
            var self = this;
            var upEntity = angular.copy(self.searchInfo);
            upEntity.cartId = this.routeParams.cartId;
            upEntity.pCatId = this.routeParams.catId;
            upEntity.codeList = self.codeStr.split("\n");
            return upEntity;
        }
        NewCategoryCtl.prototype.goPage= function(pageIndex, size) {
            var data = this.getSearchInfo();
            data.pageIndex = pageIndex;
            data.pageSize = size;
            this.productTopService.getPage(data).then(function (res) {
                self.pageList = res.data;
            });
        }
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

            //调用搜索
        };

        NewCategoryCtl.prototype.getTopList = function () {
            var self = this,
                productTopService = self.productTopService;

            productTopService.getTopList({}).then(function (res) {
                self.topList = res.data;
            });
        };

        NewCategoryCtl.prototype.remove = function(index){
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