define([
    'cms'
], function (cms) {

    var _attibute = {
        description: "商品PC描述",
        title: "商品标题",
        item_images: "商品主图",
        seller_cids: "店铺内分类",
        sell_points: "商品卖点",
        wireless_desc: "	商品APP描述"
    };

    cms.controller('loadAttributeController', (function () {

        function loadAttributeCtl(context, $uibModalInstance,alert) {
            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
            this.attibuteName = _attibute;
            this.alert = alert;
            this.targetAttr = {};
        }


        loadAttributeCtl.prototype.ok = function () {
            var self = this, resultArr = _.map(self.targetAttr, function (value, key) {
                if (value)
                    return key;
            }).filter(function(ele){
                return ele;
            });

            if(!resultArr || resultArr.length == 0){
                self.alert('没有要上新的属性，请选择普通上新！');
                self.$uibModalInstance.dismiss();
            }

            this.$uibModalInstance.close(resultArr);
        };

        return loadAttributeCtl;
    })());

});