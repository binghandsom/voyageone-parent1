/**
 * @description 店铺内分类管理
 */
define([
    'cms'
], function (cms) {

    function getNodeByName(catName, tree) {

        if (tree == null)
            return null;

        let result = null;

        _.find(tree, function (element) {
            if (element.catName && element.catName == catName) {
                result = element;
                return true;
            }

            if (element.children && element.children.length) {
                result = getNodeByName(catName, element.children);
                if (result)
                    return true;
            }

        });

        return result;
    }

    cms.controller('shopCategoryController', class ShopCategoryController {

        constructor(sellerCatService, popups, notify,$rootScope) {
            this.sellerCatService = sellerCatService;
            this.popups = popups;
            this.notify = notify;
            this.$rootScope = $rootScope;
            this.totalCategory = [];
        }

        init() {
            let self = this;

            self.sellerCatService.getCat({cartId: 8}).then(res => {
                let len = self.totalCategory.length;
                self.totalCategory.push({index: len, children: res.data.catTree});

            });
        }

        /**
         * @param category 子节点
         * @param categoryItem 父节点
         */
        openCategory(category, categoryItem) {
            const self = this,
                totalCategory = self.totalCategory;

            totalCategory.splice(categoryItem.index + 1);


            let len = totalCategory.length;
            if(category.children)
                totalCategory.push({index: len, children: category.children});
            else
                totalCategory.push({index: len, children: []});

            categoryItem.selectedCat = category;
            self.selected = category;

        }

        popEditCategory(model, $event) {
            let self = this;

            self.popups.openEditCategory(model).then(res => {

                self.$rootScope.categoryTreeList=[];
                self.sellerCatService.updateCat({
                    cartId: 8,
                    catId: res.catId,
                    catName: res.catName,
                    mapping: res.mapping
                }).then(res => {
                    self.notify.success('update success');

                    //实时更新左侧店铺内分类菜单
                    self.$rootScope.$broadcast('asyncCategorys',res.data);
                });

            });

            $event.stopPropagation();
        }

        popIncreaseCategory(categoryItem) {
            let self = this,
                index = categoryItem.index,
                selectedCat = {},
                totalCategory = self.totalCategory;

            if (index !== 0) {
                selectedCat = totalCategory[index - 1].selectedCat;
            }

            self.popups.openEditCategory({
                type:'add',
                parentCatPath: selectedCat.catPath ? selectedCat.catPath : ''
            }).then(response => {

                let parentCatId = index === 0 ? 0 : selectedCat.catId;

                self.sellerCatService.addCat({
                    cartId: 8,
                    catName: response.catName,
                    mapping:response.mapping,
                    parentCatId: parentCatId,
                    urlKey:response.urlKey
                }).then(function (res) {
                    let newNode = getNodeByName(response.catName, res.data.catTree);

                    if (index === 0)
                        self.totalCategory[0].children.push(newNode);
                    else
                        selectedCat.children.push(newNode);

                    //实时更新左侧店铺内分类菜单
                    self.$rootScope.$broadcast('asyncCategorys',res.data);

                    self.notify.success('category add success');

                });

            });

        }

        saveSorts() {
            let self = this,
                sellerCatService = self.sellerCatService;

            sellerCatService.sortableCat({tree: self.totalCategory[0].children, cartId: 8}).then(() => {
                self.notify.success('sort success');
            });

        }

    })

});