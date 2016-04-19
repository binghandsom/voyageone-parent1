/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {

    /**
     * 如果checkbox被选中,返回被选中的value.
     * eg.[{new: true, pending: false, approved: true}] -> [new, approved]
     * @param object
     * @returns {*}
     */
    function _returnKey(object) {
        return _.chain(object)
            .map(function(value, key) { return value ? key : null;})
            .filter(function(value) { return value != null;})
            .value();
    }

    return cms.controller('jmImageManageController', (function () {

        function JmImageManageController($translate, $CmsMtMasterInfoService, notify, confirm, alert) {

            this.translate = $translate;
            this.cmsMtMasterInfoService = $CmsMtMasterInfoService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;

            this.searchInfo = {synFlgList: []};
            this.imageDataList = [];
            this.imageDataPageOption = {curr: 1, total: 0, size: 10, fetch: this.search.bind(this)}
            this.status = {
                open: true
            }
        }

        JmImageManageController.prototype = {

            // 初始化检索
            initialize: function () {
                this.imageDataPageOption.curr = 1;
                this.search();
            },

            clear: function () {
                this.searchInfo = {synFlgList: []};
            },

            // 检索
            search: function () {
                var self = this;
                var data = angular.copy(self.searchInfo);
                data.synFlgList = _returnKey(data.synFlgList);
                data.start = (self.imageDataPageOption.curr - 1) * self.imageDataPageOption.size;
                data.length = self.imageDataPageOption.size;
                self.cmsMtMasterInfoService.search(data).then (function(res) {
                    self.imageDataList = res.data.masterInfoList;
                    self.imageDataPageOption.total = res.data.masterInfoListTotal;
                })
            },

            // 保存单个图片
            saveImage: function (imageData) {
                var self = this;
                self.cmsMtMasterInfoService.saveImage(imageData).then(function () {
                    self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                })
            },

            // 单个图片上传
            reUpload: function (imageData) {
                var self = this;
                self.cmsMtMasterInfoService.updateJMImg(imageData).then(function () {
                    self.notify.success(self.translate.instant('TXT_MSG_UPLOAD_IMAGE_SUCCESS'));
                })
            },

            // 删除的单个图片
            deleteImage: function (imageData) {
                var self = this;
                self.confirm(self.translate.instant('TXT_MSG_DELETE_ITEM')).result.then(function () {
                    self.cmsMtMasterInfoService.deleteImage(imageData).then(function () {
                        self.search();
                        self.notify.success(self.translate.instant('TXT_MSG_DELETE_SUCCESS'));
                    })
                })
            },

            // 新加图片信息
            openImage: function (imageData, openImageSetting) {
                openImageSetting({
                    imageData: {
                        brandName: imageData.brandName,
                        productType: imageData.productType,
                        sizeType: imageData.sizeType,
                        dataType: imageData.dataType.toString()
                    }
                }).then(this.initialize.bind(this));
            },
            loadJmMasterBrand:function() {
                var self = this;
                self.cmsMtMasterInfoService.loadJmMasterBrand().then(function (result) {
                    console.log(result);
                    if (result.data.result) {
                        self.alert("同步成功");
                    }
                    else {
                        self.alert(result.data.msg);
                    }
                })
            }
        };

        return JmImageManageController
    })());
});