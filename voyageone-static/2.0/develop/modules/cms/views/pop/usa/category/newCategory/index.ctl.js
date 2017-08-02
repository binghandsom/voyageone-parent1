/**
 * 新增店铺分类项
 * Created by tony-piao on 2016/5/20.
 */
define([
    'cms'
], function (cms) {

    cms.controller('IncreaseCategoryController',class IncreaseCategoryController{
        constructor($scope, context, alert){
            this.scope = $scope;
            this.context = context;
            this.selectObject = context.selectObject;
            this.alert = alert;
            this.catName = "";
            this.catPath = "";
            this.urlKey = "";
        }

        init(){
            let self = this,
                selectObject = self.selectObject,
                context = self.context;

            if (selectObject) {
                //页面有数据的情况
                if (context.root == true) {
                    self.catPath = "";
                } else {
                    self.catPath = selectObject.catPath;
                }
            } else {
                //页面没有数据的情况
                if (context.root == true) {
                    self.catPath = "";
                } else {
                    self.scope.$dismiss();
                    self.alert('TXT_MSG_TAG');
                }
            }
        }

        save(){
            let self = this,
                selectObject = self.selectObject,
                parentCatId,
                context = self.context;

            parentCatId = context.root ? "0" : selectObject.catId;

            self.context.ctrl.save(context.root,selectObject, parentCatId, self.catName, self.urlKey);
            self.scope.$dismiss();
        }

    });

});