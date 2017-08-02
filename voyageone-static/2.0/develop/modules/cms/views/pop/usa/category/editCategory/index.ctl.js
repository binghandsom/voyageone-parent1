/**
 * @description
 */
define([
    'cms'
],function (cms) {

    cms.controller('EditCategoryController',class EditCategoryController{

        constructor(context,$modalInstance,popups){
            this.category = context;
            this.$modalInstance = $modalInstance;
            this.popups = popups;
        }

        init(){
            let self = this,
                catPath = self.category.catPath,
                parentCatPaths = catPath.split('>');

            parentCatPaths.splice(catPath.split('>').length - 1);
            if(parentCatPaths.length  > 0)
                self.parentCatPath = `${parentCatPaths.join('>')} >`;

        }

        popCategory(option, attrName) {
            let self = this;

            self.popups.openAmazonCategory(option).then(res => {
                self.category.mapping[attrName] = res.catPath;
            });
        }

        finish(){
            let self = this;

            self.$modalInstance.close(self.category);

        }

    })

});