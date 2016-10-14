/**
 * Created by sofia on 2016/10/13.
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/service/product.detail.service',
    'modules/cms/controller/popup.ctl'
], function (cms, carts) {
    cms.controller('CodeMoveController', (function () {
        function CodeMoveController(productDetailService, notify, confirm, alert, popups) {
            this.cartId = null;
            this.cartName;
            this.productCode;
            this.sourceGroupId = null;
            this.sourceGroupName;
            this.destGroupType = "new";
            this.searchCode;
            this.groupList = [];
            this.destGroupId = null;
            this.destGroupName;
            this.productUrl;

            this.show = false;
            this.popups = popups;
            this.showView = false;

            this.productDetailService = productDetailService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
        }

        CodeMoveController.prototype = {
            init: function () {
                var self = this;
                var moveCodeInfo = JSON.parse(window.sessionStorage.getItem('moveCodeInfo'));
                if (moveCodeInfo) {
                    self.cartId = moveCodeInfo.cartId;
                    self.cartName = moveCodeInfo.cartName;
                    self.productCode = moveCodeInfo.productCode;
                    self.productUrl = carts.valueOf(parseInt(moveCodeInfo.cartId)).pUrl;
                }
                self.productDetailService.moveCodeInit({
                    productCode: self.productCode,
                    cartId: self.cartId
                }).then(function (resp) {
                    self.sourceGroupId = resp.data.sourceGroupId;
                    self.sourceGroupName = resp.data.sourceGroupName;
                });
            },
            search: function () {
                var self = this;
                self.productDetailService.moveCodeSearch({
                    searchCode: self.searchCode,
                    cartId: self.cartId,
                    sourceGroupId: self.sourceGroupId
                }).then(function (resp) {
                    self.groupList = resp.data.groupList;
                    self.destGroupId = null;
                    self.destGroupName = null;
                });
            },
            ifShow: function (item) {
                var self = this;
                switch (item.type) {
                    case 'selectGroup':
                        item.value == "select" ? self.show = true : self.show = false;
                        break;
                    case 'buildView':
                        self.showView = true;
                }

            },
            selGroup: function (group) {
                var self = this;
                self.destGroupId = group.groupInfo.groupId;
                if (group.mainProductInfo.common.fields.originalTitleCn != "") {
                    self.destGroupName = group.mainProductInfo.common.fields.originalTitleCn;
                } else {
                    self.destGroupName = group.mainProductInfo.common.fields.productNameEn;
                }
            },

            openImageDetail : function (item) {
                var self = this;
                if (item.common == undefined || item.common.fields == undefined) {
                    return;
                }
                var picList = [];
                for (var attr in item.common.fields) {
                    if (attr.indexOf("images") >= 0) {
                        var image = _.map(item.common.fields[attr], function (entity) {
                            var imageKeyName = "image" + attr.substring(6, 7);
                            return entity[imageKeyName] != null ? entity[imageKeyName] : "";
                        });
                        picList.push(image);
                    }
                }
                self.popups.openImagedetail({'mainPic': picList[0][0], 'picList': picList, 'search': 'master'});
            },

            preview: function () {
                var self = this;
                self.productDetailService.moveCodePreview({
                    destGroupType: self.destGroupType,
                    productCode: self.productCode,
                    sourceGroupId: self.sourceGroupId,
                    destGroupId: self.destGroupId,
                    sourceGroupName: self.sourceGroupName,
                    destGroupName: self.destGroupName
                }).then(function (resp) {
                    self.sourceGroupInfoBefore = resp.data.sourceGroupInfoBefore;
                    self.sourceCodeInfoBefore = resp.data.sourceCodeInfoBefore;
                    self.destGroupInfoBefore = resp.data.destGroupInfoBefore;
                    self.destCodeInfoBefore = resp.data.destCodeInfoBefore;
                    self.sourceGroupInfoAfter = resp.data.sourceGroupInfoAfter;
                    self.sourceGroupInfoAfterDeleted = resp.data.sourceGroupInfoAfterDeleted;
                    self.sourceCodeInfoAfter = resp.data.sourceCodeInfoAfter;
                    self.destGroupInfoAfter = resp.data.destGroupInfoAfter;
                    self.destCodeInfoAfter = resp.data.destCodeInfoAfter;
                    self.ifShow({type:'buildView',value:true})
                });
            },

            move: function (type) {
                var self = this;
                self.popups.openSKUMoveConfirm(type).then(function (resp) {
                    self.alert("11");
                });
            }

        };
        return CodeMoveController;
    })())
});