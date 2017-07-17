define([
    'cms'
], function (cms) {

    cms.controller('amazonCategoryController', class AmazonCategoryController {

        constructor(context, $productDetailService, $uibModalInstance) {
            this.context = context ? context : {};
            this.productDetailService = $productDetailService;
            this.$uibModalInstance = $uibModalInstance;
            this.totalCategory = [];
            this.selected = {};
        }

        init() {
            const self = this;

            self.productDetailService.getPlatformCategories({cartId: self.context.cartId}).then(res => {
                let len = self.totalCategory.length;
                self.totalCategory.push({index: len, children: res.data});

                self.defaultSpread();
            });

        }

        defaultSpread() {
            let self = this,
                totalCategory = self.totalCategory;

            if (!self.context.from || self.context.from === '')
                return;

            self.context.from.split(">").forEach((catPath, index) => {
                totalCategory[index].children.forEach((item) => {
                    if (item.catName === catPath) {
                        totalCategory[index].selectedCat = item;
                        if (item.children.length > 0)
                            totalCategory.push({index: totalCategory.length, children: item.children});
                    }
                });
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

        finish() {
            let self = this;

            self.$uibModalInstance.close(self.selected);
        }

    });

});