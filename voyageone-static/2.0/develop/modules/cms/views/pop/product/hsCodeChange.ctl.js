/**
 * @Description:税号切换
 * @Author piao wenjie
 */

define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms,carts) {
    'use strict';
    return cms.controller('HsCodeChangeController', (function () {

        function HsCodeChange(context,$uibModalInstance,productDetailService) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.productDetailService = productDetailService;
            this.vm = {
                result:[]
            }
        }

        HsCodeChange.prototype = {
            init:function(){
                var self = this;

                self.productDetailService.hsCodeChg({prodId:self.context.prodId,hsCode:self.context.hsCodeNew}).then(function(res){
                    _.each(res.data, function(element,key){
                        var _hsObject = {cartId:key,cartInfo:carts.valueOf(+key)};
                        _.each(element,function(element,key){
                            _.extend(_hsObject,{skuCode:key,prideOld:element[0],priceNew:element[1]});
                        });
                        self.vm.result.push(_hsObject);
                    });

                },function(){
                    /**错误处理*/
                    self.uibModalInstance.close("error");
                });
            },
            update:function(mark){
                this.uibModalInstance.close(mark);
            }
        };

        return HsCodeChange;

    })());
});
