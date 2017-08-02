/**
 * @description 店铺内分类管理
 */
define([
    'cms'
],function (cms) {

    cms.controller('shopCategoryController',class ShopCategoryController{

        constructor(sellerCatService,popups){
            this.sellerCatService = sellerCatService;
            this.popups = popups;
            this.totalCategory = [];
        }

        init(){
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

            if (category && category.children.length > 0) {
                let len = totalCategory.length;
                totalCategory.push({index: len, children: category.children});
            }

            categoryItem.selectedCat = category;
            self.selected = category;

        }

        popEditCategory(model,$event){
            let self = this;

            self.popups.openEditCategory(model).then(res => {

            });

            $event.stopPropagation();
        }

    })

});