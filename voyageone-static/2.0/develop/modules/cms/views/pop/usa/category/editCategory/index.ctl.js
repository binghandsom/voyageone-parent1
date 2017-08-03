/**
 * @description
 */
define([
    'cms'
], function (cms) {

    function isEmpty(attribute) {
        return attribute === undefined || attribute === '';
    }

    cms.controller('EditCategoryController', class EditCategoryController {

        constructor(context, $uibModalInstance, popups,alert) {
            this.context = context;
            this.category = context;
            this.$uibModalInstance = $uibModalInstance;
            this.popups = popups;
            this.alert = alert;
        }

        init() {
            let self = this;

            if (self.context.type === 'add') {
                self.parentCatPath = `${self.context.parentCatPath} >`;
                self.category = {
                    mapping: {}
                };
            } else {
                let catPath = self.category.catPath,
                    parentCatPaths = catPath.split('>');

                parentCatPaths.splice(catPath.split('>').length - 1);
                if (parentCatPaths.length > 0)
                    self.parentCatPath = `${parentCatPaths.join('>')} >`;
            }

        }

        popCategory(option, attrName) {
            let self = this;

            self.popups.openAmazonCategory(option).then(res => {
                self.category.mapping[attrName] = res.catPath;
            });
        }

        finish() {
            let self = this;

            if (self.context.type === 'add') {
                if (isEmpty(self.category.urlKey) || isEmpty(self.category.catName)) {
                    self.alert('please check category name & category\'s urlKey');
                    return false;
                }
            }


            self.$uibModalInstance.close(self.category);

        }

    })

});