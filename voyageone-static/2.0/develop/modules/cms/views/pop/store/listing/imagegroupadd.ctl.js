/**
 * Created by 123 on 2016/5/3.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('popImageGroupAddCtl', (function () {

        function PopImageGroupAddCtl(data, imageGroupService, sizeChartService, alert, notify, $uibModalInstance) {
            this.alert = alert;
            this.notify = notify;
            this.parent = data;
            this.imageGroupService = imageGroupService;
            this.sizeChartService = sizeChartService;
            this.platformList = data.platformList;
            this.imageTypeList = data.imageTypeList;
            this.brandNameList = data.brandNameList;
            this.productTypeList = data.productTypeList;
            this.sizeTypeList = data.sizeTypeList;
            this.$uibModalInstance = $uibModalInstance;
            this.platform = data.platform ? data.platform : "";
            this.imageGroupName = data.imageGroupName;
            this.viewType = data.viewType ? data.viewType : "";
            this.imageType = data.imageType ? data.imageType : "";
            this.brandName = data.brandName ? data.brandName : [];
            this.productType = data.productType ? data.productType : [];
            this.sizeType = data.sizeType ? data.sizeType : [];
        }

        PopImageGroupAddCtl.prototype = {
            init: function () {
                var self = this;

                self.imageGroupInfo = self.parent.imageGroupInfo;

                if (imageGroupInfo.sizeChartId > 0) {
                    self.selectedSize = {
                        cartId: self.platform,
                        sizeChartName: imageGroupInfo.sizeChartName,
                        sizeChartId: imageGroupInfo.sizeChartId
                };

                    self.chartType = "match";
                    self.sizeChart = self.selectedSize;
                }

                self.getNoMatchList();
            },
            getNoMatchList: function () {
                /**当为尺码图时获取未匹配尺码*/
                var self = this;

                if (self.imageType == 2) {
                    self.sizeChartService.getNoMatchList({cartId: self.platform}).then(function (res) {
                        self.noMathOpt = res.data;
                        if(self.selectedSize)
                            self.noMathOpt.push(self.selectedSize);
                    });
                } else {
                    self.noMathOpt = null;
                }
            },
            save: function () {
                var self = this,
                    listSizeChart;

                if (self.sizeChart) {
                    if (_.isObject(self.sizeChart)) {
                        listSizeChart = self.sizeChart;
                    } else {
                        listSizeChart = [{sizeChartName: self.sizeChart, sizeChartId: 0}]
                    }
                }

                var upEntity = _.extend({
                    "imageGroupId":self.imageGroupInfo?self.imageGroupInfo.imageGroupId:0,
                    "platform": self.platform,
                    "imageGroupName": self.imageGroupName,
                    "viewType": self.viewType,
                    "imageType": self.imageType,
                    "brandName": self.brandName,
                    "productType": self.productType,
                    "sizeType": self.sizeType
                },listSizeChart);

                self.imageGroupService.save(upEntity).then(function () {
                    self.notify.success('TXT_MSG_UPDATE_SUCCESS');
                    self.$uibModalInstance.close();
                    self.parent.search();
                }, function (err) {
                    if (err.displayType == null) {
                        self.alert('TXT_MSG_UPDATE_FAIL');
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