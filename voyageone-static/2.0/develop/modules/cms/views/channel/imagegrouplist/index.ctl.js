/**
 * Created by 123 on 2016/5/3.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("imageGroupController", (function () {
        function ImageGroupController($routeParams, imageGroupService, cActions, confirm, alert, notify) {
            this.platformList = [];
            this.imageType = "";
            this.beginModified = "";
            this.endModified = "";
            this.brandNameList = [];

            this.brandName = "";
            this.productTypeList = [];
            this.productType = "";
            this.sizeTypeList = [];
            this.sizeType = "";

            this.imageGroupList = [];
            this.pageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: function () {
                    this.search();
                }
            };
            this.imageGroupService = imageGroupService;
        }

        ImageGroupController.prototype = {
            init: function () {
                var main = this;
                main.imageGroupService.init().then(function (res) {
                    main.platformList = res.data.platformList;
                    main.brandNameList = res.data.brandNameList;
                    main.productTypeList = res.data.productTypeList;
                    main.sizeTypeList = res.data.sizeTypeList;
                    main.search();
                })
            },
            search: function () {
                var main = this;
                main.imageGroupService.search({
                    "platformList" : main.platformList,
                    "imageType" : main.imageType,
                    "beginModified" : main.beginModified,
                    "endModified" : main.endModified,
                    "brandName" : main.brandName,
                    "productType" : main.productType,
                    "sizeType" : main.sizeType
                }).then(function (res) {
                        main.imageGroupList = res.data.imageGroupList;
                        main.pageOption.total = res.data.imageGroupList.length;
                        main.pageOption.curr = 1;
                })
            },
            delete: function (imageGroupId) {
                var main = this;
                main.imageGroupService.delete({
                    "imageGroupId" : imageGroupId
                }).then(function (res) {

                })
            }
        };

        return ImageGroupController;

    })())
});