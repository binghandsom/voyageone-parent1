/**
 * Created by 123 on 2016/5/3.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("imageGroupDetailController", (function () {
        function ImageGroupDetailController($routeParams, imageGroupDetailService, cActions, confirm, alert, notify) {
            this.confirm = confirm;
            this.alert = alert;
            this.notify = notify;
            this.imageGroupId = $routeParams['imageGroupId'];
            this.platformList = [];
            this.platform = "";
            this.brandNameList = [];
            this.brandName = [];
            this.productTypeList = [];
            this.productType = [];
            this.sizeTypeList = [];
            this.sizeType = [];

            this.imageGroupName = "";
            this.imageType = "";
            this.viewType = "";

            this.imageList = [];

            this.imageGroupDetailService = imageGroupDetailService;
        }

        ImageGroupDetailController.prototype = {
            init: function () {
                var main = this;
                main.imageGroupDetailService.init({"imageGroupId":main.imageGroupId}).then(function (res) {
                    main.platformList = res.data.platformList;
                    main.brandNameList = res.data.brandNameList;
                    main.productTypeList = res.data.productTypeList;
                    main.sizeTypeList = res.data.sizeTypeList;
                    main.platform = res.data.imageGroupInfo.cartId + "";
                    main.brandName = res.data.imageGroupInfo.brandName;
                    main.productType = res.data.imageGroupInfo.productType;
                    main.sizeType = res.data.imageGroupInfo.sizeType;
                    main.imageGroupName = res.data.imageGroupInfo.imageGroupName;
                    main.imageType = res.data.imageGroupInfo.imageType + "";;
                    main.viewType = res.data.imageGroupInfo.viewType + "";;
                    main.search();
                })
            },
            search: function () {
                var main = this;
                main.imageGroupDetailService.search({"imageGroupId":main.imageGroupId}).then(function (res) {
                    if (res.data != null) {
                    main.imageList = res.data;
                    }
                })
            },
            save: function () {
                var main = this;
                main.imageGroupDetailService.save({
                    "imageGroupId":main.imageGroupId,
                    "platform":main.platform,
                    "brandName":main.brandName,
                    "productType":main.productType,
                    "sizeType":main.sizeType,
                    "imageGroupName":main.imageGroupName,
                    "imageType":main.imageType,
                    "viewType":main.viewType
                }).then(function (res) {
                    main.notify.success('TXT_MSG_UPDATE_SUCCESS');
                }, function (err) {
                    if (err.displayType == null) {
                        main.alert('TXT_MSG_UPDATE_FAIL');
                    }
                });
            },
            refresh: function (originUrl) {
                var main = this;
                main.confirm('TXT_MSG_DO_DELETE').result.then(function () {
                    main.imageGroupDetailService.refresh({
                        "imageGroupId" : main.imageGroupId,
                        "originUrl" : originUrl
                    }).then(function (res) {
                        main.notify.success('TXT_MSG_DELETE_SUCCESS');
                        main.search();
                    }, function (err) {
                        if (err.displayType == null) {
                            main.alert('TXT_MSG_DELETE_FAIL');
                        }
                    })
                })
            },
            delete: function (originUrl) {
                var main = this;
                main.confirm('TXT_MSG_DO_DELETE').result.then(function () {
                    main.imageGroupDetailService.delete({
                        "imageGroupId" : main.imageGroupId,
                        "originUrl" : originUrl
                    }).then(function (res) {
                        main.notify.success('TXT_MSG_DELETE_SUCCESS');
                        main.search();
                    }, function (err) {
                        if (err.displayType == null) {
                            main.alert('TXT_MSG_DELETE_FAIL');
                        }
                    })
                })
            },
            moveUp: function (originUrl) {
                var main = this;
                main.imageGroupDetailService.move({
                    "imageGroupId" : main.imageGroupId,
                    "originUrl" : originUrl
                }).then(function (res) {
                    main.imageList = res.data;
                })
            },
            moveDown: function (originUrl) {
                var main = this;
                main.imageGroupDetailService.move({
                    "imageGroupId" : main.imageGroupId,
                    "originUrl" : originUrl
                }).then(function (res) {
                    main.imageList = res.data;
                })
            }
        };

        return ImageGroupDetailController;

    })())
});