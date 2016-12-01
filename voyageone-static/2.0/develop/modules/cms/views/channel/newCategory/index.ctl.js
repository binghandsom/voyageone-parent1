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
        }

        NewCategoryCtl.prototype.init = function () {
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
            self.getTopList();
        };

        NewCategoryCtl.prototype.clear=function () {
            this.searchInfo = {};
            this.codeStr="";
        };

        NewCategoryCtl.prototype.search = function (sortInfo) {
            var self = this;
            this.goPage(1, this.paging.size,sortInfo);
           var data= this.getSearchInfo();
            this.productTopService.getCount(data).then(function (res) {
                self.paging.total = res.data;
                console.log(data);
            });
        };

        NewCategoryCtl.prototype.getSearchInfo= function  () {
            var self = this;
            var upEntity = angular.copy(self.searchInfo);
            upEntity.cartId = this.routeParams.cartId;
            upEntity.pCatId = this.routeParams.catId;
            upEntity.codeList = self.codeStr.split("\n");
            return upEntity;
        };

        NewCategoryCtl.prototype.goPage= function(pageIndex, size,sortInfo) {
            var self=this;
            var data = this.getSearchInfo();
            data.pageIndex = pageIndex;
            data.pageSize = size;
            if(sortInfo)
            {
                data.sortColumnName=sortInfo.sortColumnName;
                data.sortType=sortInfo.sortType;
            }
            this.productTopService.getPage(data).then(function (res) {
                self.modelList = res.data;
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
            this.search({sortColumnName:_sort.sValue,sortType:sortType});
        };

        NewCategoryCtl.prototype.getSelectedCodeList=function()
        {
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

        NewCategoryCtl.prototype.addTopProductClick=function () {
            var codeList = this.getSelectedCodeList();
            if (codeList.length==0) {
                alert("请选择商品");
            }
            var parameter = {};
            parameter.cartId = this.routeParams.cartId;
            parameter.pCatId = this.routeParams.catId;
            parameter.codeList = codeList;
            
            var self=this;
            this.productTopService.addTopProduct(parameter).then(function (res) {
                self.search();
                self.getTopList();
            });
        }

        NewCategoryCtl.prototype.getTopList = function () {
            var self = this;
            var parameter = {};
            parameter.cartId = this.routeParams.cartId;
            parameter.pCatId = this.routeParams.catId;
            this.productTopService.getTopList(parameter).then(function (res) {
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