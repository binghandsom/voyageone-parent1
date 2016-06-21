/**
 * Created by 123 on 2016/5/3.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('popImageGroupAddCtl', (function () {

        function PopImageGroupAddCtl(data, imageGroupService, alert, notify, $uibModalInstance) {
            this.alert = alert;
            this.notify = notify;
            this.parent = data;
            this.imageGroupService = imageGroupService;
            this.platformList = data.platformList;
            this.imageTypeList = data.imageTypeList;
            this.brandNameList = data.brandNameList;
            this.productTypeList = data.productTypeList;
            this.sizeTypeList = data.sizeTypeList;
            this.$uibModalInstance = $uibModalInstance;
            this.platform = "";
            this.imageGroupName = "";
            this.viewType = "";
            this.imageType = "";
            this.brandName = [];
            this.productType = [];
            this.sizeType = [];
        }
        PopImageGroupAddCtl.prototype = {
            save: function () {
                var main = this;
                main.imageGroupService.save({
                    "platform" : main.platform,
                    "imageGroupName" : main.imageGroupName,
                    "viewType" : main.viewType,
                    "imageType" : main.imageType,
                    "brandName" : main.brandName,
                    "productType" : main.productType,
                    "sizeType" : main.sizeType
                }).then(function (res) {
                    main.notify.success('TXT_MSG_UPDATE_SUCCESS');
                    main.$uibModalInstance.close();
                    main.parent.search();
                }, function (err) {
                    if (err.displayType == null) {
                        main.alert('TXT_MSG_UPDATE_FAIL');
                    }
                });
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };
        return PopImageGroupAddCtl;

    })())
});