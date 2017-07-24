/**
 * @description 美国店铺内分类
 * @author piao
 */
define([
    'cms',
    './sortEnum'
], function (cms, sortEnum) {

    cms.controller('usCategoryController',class UsCategoryController{

        constructor($routeParams){
            this.$routeParams = $routeParams;
        }

        init(){
            let self = this;

            console.log(self.$routeParams.category);
        }

    });

});