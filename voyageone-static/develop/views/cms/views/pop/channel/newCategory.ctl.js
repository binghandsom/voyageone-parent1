/**
 * 新增店铺分类项
 * Created by tony-piao on 2016/5/20.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    "use strict";
    angularAMD.controller('newCategoryCtl', (function(){
        function NewCategoryCtl($scope,context,alert){
            this.scope = $scope;
            this.context = context;
            this.selectObject = context.selectObject;
            this.alert = alert;
            this.catName = "";
            this.catPath = "";
        }
        NewCategoryCtl.prototype = {
            init:function(){
                if(this.selectObject){
                    //页面有数据的情况
                    if(this.context.root == true){
                        this.catPath = "";
                    }else{
                        this.catPath  = this.selectObject.catPath;
                    }
                }else{
                    //页面没有数据的情况
                    if(this.context.root  == true){
                        this.catPath = "";
                    }else{
                        this.scope.$dismiss();
                        this.alert('TXT_MSG_TAG');
                    }
                }
            },
            save:function(){
                var parentCatId = this.context.root ? "0" : this.selectObject.catId;
                this.context.ctrl.save(parentCatId,this.catName);
                this.scope.$dismiss();
            }
        };

        return NewCategoryCtl;
    })());
});