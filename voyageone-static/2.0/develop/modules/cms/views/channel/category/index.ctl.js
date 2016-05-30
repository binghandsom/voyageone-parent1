/**
 * Created by tony-piao on 2016/5/20.
 * 店铺分类controller
 */

define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    "use strict";
    return cms.controller('categoryController', (function (){

        function CategoryController(platformMappingService,sellerCatService,alert,confirm,$translate) {

            this.platformMappingService = platformMappingService;
            this.sellerCatService = sellerCatService;
            this.alert = alert;
            this.confirm = confirm;
            this.translate = $translate;
            this.carts = [];
            this.tree = [];
            this.source = [] ;
            this.key = [];
            this.selected = [];
            this.cartInfo = {cart: null,level:0,maxTag:0};
            this.newIndex = {value:-1};
        }

        CategoryController.prototype = {
            init:function(){
                var self = this;
                self.platformMappingService.getCarts().then(function(res){
                    self.carts = res.data;
                });

            },
            /**
             * 当选择店铺时初始化页面，并根据店铺深度(level)设置div宽度
             */
            loadCategories:function(){
                var self = this;
                //获取店铺配置
                self.sellerCatService.init({"cartId":+self.cartInfo.cart}).then(function(res) {
                    self.cartInfo.level = res.data.MAX_SELLER_CAT_DEPTH;
                    self.cartInfo.maxTag = res.data.MAX_SELLER_CAT_CNT;
                }).then(function(){
                    self.sellerCatService.getCat({"cartId":+self.cartInfo.cart}).then(function(res) {
                        self.source = res.data.catTree;
                        self.search(0);
                        switch(+self.cartInfo.level){
                            case 1:
                                $(".category-drag-minWidth").css({"width":"90%"});
                                break;
                            case 2:
                                $(".category-drag-minWidth").css({"width":"48%"});
                                break;
                            case 3:
                                $(".category-drag-minWidth").css({"width":"32%"});
                                break;
                            case 4:
                                $(".category-drag-minWidth").css({"width":"24%"});
                                break;
                        }
                    });
                });

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
                            return item.catName === selected[index];
                        });
                    } else if (tree[index].indexOf(selected[index]) < 0) {
                        var indexSelected = tree[index].find(function (item) {
                            return item.catId === selected[index].catId;
                        });
                        if (indexSelected)
                            selected[index] = indexSelected;
                        else
                            selected[index] = tree[index][0];
                    }
                }
            },
            newCategory:function(root,level,openNewCategory){
                if(this.cartInfo.cart == null){
                    this.alert(this.translate.instant("TXT_STORE_CATEGORY_SELECT"));
                    return;
                }
                openNewCategory({root:root,selectObject:this.selected[level],save:this.save,ctrl:this});
            },
            save:function(parentCatId,catName){
                var self = this;
                this.selected[this.newIndex.value] = catName;
                self.sellerCatService.addCat({"cartId":+this.cartInfo.cart,"catName":catName,"parentCatId":parentCatId}).then(function(res) {
                    self.source = res.data.catTree;
                    self.search(0);
                });
            },
            delete:function(node){
                var self = this;
                self.confirm(this.translate.instant("TXT_MSG_DELETE_ITEM")).result.then(function () {
                    self.sellerCatService.delCat({"cartId":+self.cartInfo.cart,"catId":node.catId,"parentCatId":node.parentCatId}).then(function(res) {
                        self.source = res.data.catTree;
                        self.search(0);
                    });
                });

            },
            /**
             * 修改结点名称
             * @param node 树结构结点
             */
            updateCat:function(node){
                    var self = this;
                    if(node.value == null){
                        node.value = 1;
                        node.newCatName = node.catName;
                    }else{
                        node.value = null;
                        if(node.newCatName == "" || node.newCatName == null)
                            return;
                        self.sellerCatService.updateCat({"cartId":+this.cartInfo.cart,"catId":node.catId,"catName":node.newCatName}).then(function(res) {
                            self.source = res.data.catTree;
                            self.search(0);
                        });
                    }
            }

        };

        return CategoryController;

    })());
});