/**
 * Created by 123 on 2016/5/3.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("imageGroupDetailController", (function () {
        function ImageGroupDetailController($routeParams, imageGroupDetailService, confirm, alert, notify, popups) {
            this.confirm = confirm;
            this.alert = alert;
            this.notify = notify;
            this.imageGroupId = $routeParams['imageGroupId'];
            this.platformList = [];
            this.brandNameList = [];
            this.productTypeList = [];
            this.sizeTypeList = [];
            this.imageTypeList = [];
            this.imageList = [];

            this.imageGroupDetailService = imageGroupDetailService;
            this.popups = popups;
        }

        ImageGroupDetailController.prototype.init = function () {
            var self = this,
                data;

            self.imageGroupDetailService.init({"imageGroupId": self.imageGroupId}).then(function (res) {
                data = res.data;

                self.platformList = data.platformList;
                self.brandNameList = data.brandNameList;
                self.productTypeList = data.productTypeList;
                self.sizeTypeList = data.sizeTypeList;
                self.imageTypeList = data.imageTypeList;

                self.imageGroupInfo = data.imageGroupInfo;
                self.platform = data.imageGroupInfo.cartId + "";
                self.cartId = data.imageGroupInfo.cartId + "";
                self.brandName = data.imageGroupInfo.brandName;
                self.productType = data.imageGroupInfo.productType;
                self.sizeType = data.imageGroupInfo.sizeType;
                self.imageGroupName = data.imageGroupInfo.imageGroupName;

                self.imageType = data.imageGroupInfo.imageType + "";
                self.viewType = data.imageGroupInfo.viewType + "";

                self.search();
            })
        };

        ImageGroupDetailController.prototype.search = function () {
            var self = this;
            self.imageGroupDetailService.search({"imageGroupId": self.imageGroupId}).then(function (res) {
                if (res.data != null) {
                    self.imageList = res.data;
                }
            })
        };

        ImageGroupDetailController.prototype.openImgDetails = function () {
            var self = this;

            self.from = "detail";
            self.popups.openImgGroupAdd(self).then(function () {
                self.refresh();
            });

        };

        ImageGroupDetailController.prototype.refresh = function (originUrl) {
            var self = this;
            self.confirm('TXT_MSG_DO_REFRESH_IMAGE').then(function () {
                self.imageGroupDetailService.refresh({
                    "imageGroupId": self.imageGroupId,
                    "originUrl": originUrl
                }).then(function () {
                    self.notify.success('TXT_MSG_REFRESH_IMAGE_SUCCESS');
                    self.search();
                }, function (err) {
                    if (err.displayType == null) {
                        self.alert('TXT_MSG_REFRESH_IMAGE_FAIL');
                    }
                })
            })

        };

        ImageGroupDetailController.prototype.refreshPage = function () {
            var self = this;
            self.imageGroupDetailService.search({"imageGroupId": self.imageGroupId}).then(function (res) {
                if (res.data != null) {
                    self.imageList = res.data;
                }
            })

        };

        ImageGroupDetailController.prototype.delete = function (originUrl) {
            var self = this;
            self.confirm('TXT_MSG_DO_DELETE').then(function () {
                self.imageGroupDetailService.delete({
                    "imageGroupId": self.imageGroupId,
                    "originUrl": originUrl
                }).then(function () {
                    self.notify.success('TXT_MSG_DELETE_SUCCESS');
                    self.search();
                }, function (err) {
                    if (err.displayType == null) {
                        self.alert('TXT_MSG_DELETE_FAIL');
                    }
                })
            })
        };

        ImageGroupDetailController.prototype.moveUp = function (originUrl) {
            var self = this;
            self.imageGroupDetailService.move({
                "imageGroupId": self.imageGroupId,
                "originUrl": originUrl,
                "direction": "up"
            }).then(function (res) {
                self.imageList = res.data;
                self.search();
            })

        };

        ImageGroupDetailController.prototype.moveDown = function (originUrl) {
            var self = this;
            self.imageGroupDetailService.move({
                "imageGroupId": self.imageGroupId,
                "originUrl": originUrl,
                "direction": "down"
            }).then(function (res) {
                self.imageList = res.data;
                self.search();
            })

        };

        return ImageGroupDetailController;

    })())
});