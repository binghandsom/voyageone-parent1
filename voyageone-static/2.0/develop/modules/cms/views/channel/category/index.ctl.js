/**
 * Created by tony-piao on 2016/5/20.
 * 店铺分类controller
 */

define([
    'cms',
    'underscore',
    './category.service.dev',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    "use strict";
    return cms.controller('categoryController', (function (){

        function CategoryController(platformMappingService, categoryService) {

            this.platformMappingService = platformMappingService;
            this.categoryService = categoryService;

            this.carts = [];
            this.tree = [];
            this.source = [] ;
            this.key = [];
            this.selected = [];
            /**
             * 下拉选中
             */
            this.cartInfo = {
                cart: null,
                level:4,
                maxTag:10
            };
        }

        CategoryController.prototype = {
            init:function(){
                var self = this;
                self.platformMappingService.getCarts().then(function(res){
                    self.carts = res.data;
                });
                //请求数据
                self.categoryService.getCategories().then(function(res) {
                    self.source = res.data;
                    self.search(0);
                });
            },
            loadCategories:function(){
                alert(this.cartInfo.cart)
            },
            byTagChildrenName:function(arr, index){
                var key = this.key[index];
                return key ? arr.filter(function (item) {
                    return item.catName.indexOf(key[index]) > -1;
                }) : arr;
            },
            search:function(index){
                var tree = this.tree;
                var source = this.source;
                var selected = this.selected;
                var prev;
                for (; index < this.cartInfo.level; index++) {
                    if (!index) {
                        tree[index] = this.byTagChildrenName(source, index);
                    } else {
                        prev = selected[index - 1];
                        if (prev)
                            tree[index] = this.byTagChildrenName(prev.children, index);
                        else {
                            tree[index] = [];
                            continue;
                        }
                    }

                    if (!selected[index]) {
                        selected[index] = tree[index][0];
                    } else if (_.isString(selected[index])) {
                        selected[index] = tree[index].find(function(item) {
                            return item.tagChildrenName === selected[index];
                        });
                    } else if (tree[index].indexOf(selected[index]) < 0) {
                        var indexSelected = tree[index].find(function (item) {
                            return item.id === selected[index].id;
                        });
                        if (indexSelected)
                            selected[index] = indexSelected;
                        else
                            selected[index] = tree[index][0];
                    }
                }
            }
        };

        return CategoryController;

    })());
});