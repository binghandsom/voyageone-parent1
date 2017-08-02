/**
 * 新增店铺分类项
 * Created by tony-piao on 2016/5/20.
 */
define([
    'cms'
], function (cms) {

    cms.controller('IncreaseCategoryController',class IncreaseCategoryController{
        constructor(context,$modalInstance){
            this.context = context;
            this.$modalInstance = $modalInstance;
            this.selectObject = context.selectObject;
            this.result = {};
        }

        init(){
            let self = this,
                selectObject = self.selectObject;

            if(selectObject){
                self.preCatPath = `${selectObject.catPath}>`;
            }
        }

        save(){
            let self = this;

            self.$modalInstance.close(self.result);
        }

    });

});