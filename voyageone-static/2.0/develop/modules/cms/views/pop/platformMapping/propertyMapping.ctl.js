/**
 * Created by 123 on 2016/8/16.
 */
/**
 * @Description:切换主商品
 * @Author piao wenjie
 */

define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms,carts) {
    'use strict';
    return cms.controller('propertyMappingController', (function () {

        function PropertyMappingController(context,$uibModalInstance,popups) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.popups = popups;
            this.valueArr = [];
        }

        PropertyMappingController.prototype = {
          init:function(){
              var self = this;
              if(self.context.cartId)
                self.context.cartName = carts.valueOf(+self.context.cartId).desc;
          },
          openPpPropertySetting:function(){
              var self = this;
              self.popups.openPropertySetting(self.context).then(function(context){
                  //{cat,value}
                  self.valueArr.push(context)
              });
          },
          order:function(arrow,index){

                var curr = this.valueArr[index];
                var repIndex = arrow == "up" ? index - 1 : index+1;

                if(repIndex < 0 || repIndex > this.valueArr.length - 1)
                    return;

                var tmp = this.valueArr.splice(repIndex,1,curr);
                this.valueArr.splice(index,1,tmp[0]);
          },
          remove:function(index){
              this.valueArr.splice(index, 1)
          },
          confirm:function(){
              var valueList = _.map(this.valueArr,function(item){
                  return {type:item.type,append:item.append,value:item.value};
              });

              this.context.value = JSON.stringify(valueList);
              this.uibModalInstance.close();
          }
        };

        return PropertyMappingController;

    })());
});