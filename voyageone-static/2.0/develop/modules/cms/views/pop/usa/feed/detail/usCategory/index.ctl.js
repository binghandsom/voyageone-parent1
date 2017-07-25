define([
    'cms'
], function (cms) {

    cms.controller('usCategoryController', class UsCategoryController {

        constructor(context, sellerCatService, $uibModalInstance) {
            this.context = context ? context : {};
            this.sellerCatService = sellerCatService;
            this.$uibModalInstance = $uibModalInstance;
            this.totalCategory = [];
            this.catArrs = [];
            if(context.froms)
                this.froms = context.froms;
            this.selected = {};
            this.selectObj = {};
        }

        init() {
            const self = this;

            self.sellerCatService.getCat({cartId: self.context.cartId}).then(res => {
                let len = self.totalCategory.length;
                self.totalCategory.push({index: len, children: res.data.catTree});

                //默认展开
                self.defaultSpread();
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

        defaultSpread() {
            let self = this,
                totalCategory = self.totalCategory;

            if(self.context.muiti){
                if(!self.context.froms)
                    return;

                let from = self.catArrs =  angular.copy(self.context.froms);
                angular.forEach(from, function (element) {
                    let _element = {};
                    _element[element.catId] = element.catPath;
                    _.extend(self.selectObj, _element);
                });
            }else{
                if (!self.context.from || self.context.from === '')
                    return;

                self.context.from.split(">").forEach((catPath, index) => {

                    if(totalCategory[index] && totalCategory[index].children){
                        totalCategory[index].children.forEach((item) => {
                            if (item.catName === catPath) {
                                totalCategory[index].selectedCat = item;
                                if (item.children.length > 0)
                                    totalCategory.push({index: totalCategory.length, children: item.children});
                            }
                        });
                    }

                });
            }

        }

        finish() {
            let self = this;


            if(self.context.muiti)
                self.$uibModalInstance.close(self.catArrs);
            else
                self.$uibModalInstance.close(self.selected);
        }

        selectCat(item) {
            let self = this,
                _index;

            _index = _.map(self.catArrs, function (item) {
                return item.catPath;
            }).indexOf(item.catPath);

            if (_index < 0) {
                self.catArrs.push({catId: item.catId, catPath: item.catPath});
                let _element = {};
                _element[item.catId] = item.catPath;
                _.extend(self.selectObj, _element);
            }
            else
                self.catArrs.splice(_index, 1);
        }


    });

});