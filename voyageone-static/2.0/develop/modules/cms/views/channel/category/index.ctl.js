/**
 * Created by tony-piao on 2016/5/20.
 * 店铺分类controller
 */

define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    "use strict";
    return cms.controller('categoryController', (function (){

        function categoryController($scope, notify, confirm, $translate) {

        }


        categoryController.prototype = {
            initialize:function(){

            },
            setSellCat:function(level,maxTag){

            }
        };

        return categoryController;

    })());
});