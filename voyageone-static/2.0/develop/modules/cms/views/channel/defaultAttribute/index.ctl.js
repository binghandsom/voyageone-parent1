/**
 * @description 平台默认属性设置一览
 * @date 2016-8-11
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    './defaultAttrService.ctl'
], function (cms) {
    cms.controller('defaultAttributeController', (function () {

        function DefaultAttributeController($translate,$q,popups,notify,confirm,menuService,platformMappingService,defaultAttrService) {

            var self = this;

            self.$translate = $translate;
            self.q = $q;
            self.popups = popups;
            self.notify = notify;
            self.confirm = confirm;
            self.menuService = menuService;
            self.platformMappingService = platformMappingService;
            self.defaultAttrService = defaultAttrService;
            self.searchInfo = {
                cartId:'',
                categoryType:'',
                categoryId:''
            };
            self.paging = {curr: 1, total: 0,fetch:function(){self.search();}};
        }

        DefaultAttributeController.prototype = {
          init:function(){
              var self = this;
              self.menuService.getPlatformType().then(function(resp){
                  self.platformTypes = _.filter(resp,function(cart){return cart.value != 0 && cart.value != 1 }) ;
              });
          },
          jdCategoryMapping:function(){
              var self = this;
              self.platformMappingService.getPlatformCategories({cartId: self.searchInfo.cartId})
                  .then(function (res) {
                      return self.q(function(resolve, reject) {
                          if (!res.data || !res.data.length) {
                              self.notify.danger("数据还未准备完毕");
                              reject("数据还未准备完毕");
                          } else {
                              resolve(self.popups.popupNewCategory({
                                  from:"",
                                  categories: res.data,
                                  divType:">"
                              }));
                          }
                      });
                  }).then(function (context) {
                    self.searchInfo.categoryPath = context.selected.catPath;
                    self.searchInfo.categoryId = context.selected.catId;
              });
          },
          clear:function(){
              var self = this;
              _.each(self.searchInfo,function(attr,key){
                  self.searchInfo[key] = null;
              });
          },
          search:function(){
              var self = this;
              var _upEntity = _.extend(self.paging,{
                  "cartId": +self.searchInfo.cartId,
                  "categoryType": +self.searchInfo.categoryType,
                  "categoryPath": self.searchInfo.categoryPath
              });
              self.defaultAttrService.page(_upEntity).then(function(res){
                  self.paging.total = res.data.total;
                  self.dataList = res.data.list;
              });
          },
          switchType:function(){
              this.searchInfo.categoryPath = this.searchInfo.categoryType == '2' ? this.searchInfo.categoryPath : '';
          },
          deleteItem:function(item){
              var self = this;
              self.confirm(self.$translate.instant("TXT_MSG_DELETE_ITEM")).then(function(){
                  self.defaultAttrService.deleteItem().then(function(res){
                      if(res.data)
                        self.notify.success(self.$translate.instant("TXT_MSG_DELETE_SUCCESS"));
                      else
                        self.notify(self.$translate.instant("TXT_MSG_DELETE_FAIL"));
                  });
              });
          }
        };

        return DefaultAttributeController;
    })())
});