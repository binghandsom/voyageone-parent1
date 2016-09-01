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
            getNoMatchList: function () {
                /**当为尺码图时获取未匹配尺码*/
                var self = this;

                if (self.imageType == 2) {
                    self.sizeChartService.getNoMatchList({cartId:self.platform}).then(function (res) {
                        self.noMathOpt = res.data;
                    });
                } else {
                    self.noMathOpt = null;
                }
            },
            save: function () {
                var self = this,
                    listSizeChart;

                if(self.sizeChart){
                    if(_.isObject(self.sizeChart)){
                        listSizeChart = self.sizeChart;
                    }else{
                        listSizeChart = [{sizeChartName:self.sizeChart , sizeChartId: 0 }]
                    }
                }

                var upEntity = _.extend(listSizeChart,{
                    "platform": self.platform,
                    "imageGroupName": self.imageGroupName,
                    "viewType": self.viewType,
                    "imageType": self.imageType,
                    "brandName": self.brandName,
                    "productType": self.productType,
                    "sizeType" : self.sizeType
                });

                if(self.parent.from === 'detail'){
                    self.$uibModalInstance.close(upEntity);
                    return;
                }

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