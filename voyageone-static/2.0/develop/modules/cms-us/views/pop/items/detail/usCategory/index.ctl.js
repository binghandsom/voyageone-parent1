define([
    'cms'
], function (cms) {

    cms.controller('usCategoryController', class UsCategoryController {

        constructor(context, sellerCatService, $uibModalInstance) {
            this.context = context ? context : {};
            this.sellerCatService = sellerCatService;
            this.$uibModalInstance = $uibModalInstance;
            this.totalCategory = [];
            this.selected = {};
        }

        init() {
            const self = this;

            self.sellerCatService.getCat({cartId: 1}).then(res => {
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

        finish() {
            let self = this;

            self.$uibModalInstance.close(self.selected);
        }

    });

});