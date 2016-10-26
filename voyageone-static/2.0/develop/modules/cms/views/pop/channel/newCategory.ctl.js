/**
 * 新增店铺分类项
 * Created by tony-piao on 2016/5/20.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    "use strict";
    angularAMD.controller('newCategoryCtl', (function () {

        function NewCategoryCtl($scope, context, alert) {
            this.scope = $scope;
            this.context = context;
            this.selectObject = context.selectObject;
            this.alert = alert;
            this.catName = "";
            this.catPath = "";
        }

        NewCategoryCtl.prototype.init = function () {
            var self = this,
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
        };

        NewCategoryCtl.prototype.save = function () {
            var self = this,
                selectObject = self.selectObject,
                parentCatId,
                context = self.context;

            parentCatId = context.root ? "0" : selectObject.catId;

            self.context.ctrl.save(selectObject, parentCatId, self.catName);
            self.scope.$dismiss();
        };


        return NewCategoryCtl;
    })());
});