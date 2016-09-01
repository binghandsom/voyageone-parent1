/**
 * Created by 123 on 2016/5/3.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("imageGroupController", (function () {
        function ImageGroupController( imageGroupService, confirm, alert, notify,popups) {
            this.confirm = confirm;
            this.alert = alert;
            this.notify = notify;
            this.popups = popups;
            this.platformList = [];
            this.imageTypeList = [];
            this.imageType = "";
            this.beginModified = "";
            this.endModified = "";

            this.brandNameList = [];
            this.brandName = [];
            this.productTypeList = [];
            this.productType = [];
            this.sizeTypeList = [];
            this.sizeType = [];

            this.imageGroupList = [];
            this.pageOption = {
                curr: 1,
                total: 0,
                fetch: this.getImageGroupList.bind(this)
            };
            this.imageGroupService = imageGroupService;
        }

        ImageGroupController.prototype = {
            init: function () {
                var main = this;
                main.imageGroupService.init().then(function (res) {
                    main.platformList = res.data.platformList;
                    main.imageTypeList = res.data.imageTypeList;
                    main.brandNameList = res.data.brandNameList;
                    main.productTypeList = res.data.productTypeList;
                    main.sizeTypeList = res.data.sizeTypeList;
                    main.search();
                })
            },
            getImageGroupList: function () {
                var main = this;
                main.imageGroupService.search({
                    "platformList" : main.platformList,
                    "imageType" : main.imageType,
                    "beginModified" : main.beginModified,
                    "endModified" : main.endModified,
                    "brandName" : main.brandName,
                    "productType" : main.productType,
                    "sizeType" : main.sizeType,
                    "curr" : main.pageOption.curr,
                    "size" : main.pageOption.size
                }).then(function (res) {
                    main.imageGroupList = res.data.imageGroupList;
                    main.pageOption.total = res.data.total;
                })
            },
            search: function () {
                this.pageOption.curr = 1;
                this.getImageGroupList();
            },
            clear: function () {
                this.imageType = "";
                this.beginModified = "";
                this.endModified = "";
                this.brandName = [];
                this.productType = [];
                this.sizeType = [];
                _.each(this.platformList, function (platform) {
                    platform.show = false;
                });
            },
            delete: function (entity) {
                var self = this,
                    popups = self.popups,
                    chart = false;

                if(entity.sizeChartId)
                    chart = true;

                popups.openTemplateConfirm({
                    content:"是否同时删除尺码表?",
                    imageGroupId:entity.imageGroupId,
                    sizeChartId:entity.sizeChartId,
                    chart:chart,
                    from:"image"
                }).then(function(context){
                    if(context === true){
                        self.notify.success('TXT_MSG_DELETE_SUCCESS');
                        self.search();
                    }else{
                        if (err.displayType == null) {
                            self.alert('TXT_MSG_DELETE_FAIL');
                        }
                    }
                });

            }
        };

        return ImageGroupController;

    })())
});