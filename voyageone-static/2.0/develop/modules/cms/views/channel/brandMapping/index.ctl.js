/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('BrandMappingController', (function () {
        function BrandMappingController($translate) {
            this.$translate = $translate;
            this.searchInfo = {
                selectedCart: null,
                selectedStatus: 2
            };
            this.cartList = [
                { cartId: 20, cartName: "聚美" },
                { cartId: 22, cartName: "京东" },
                { cartId: 23, cartName: "天猫" }
            ];
            this.brandMappingList = [
                {
                    id: "01",
                    masterBrand: "aaa",
                    brand: "aaa-1",
                    status: 1
                }, {
                    id: "02",
                    masterBrand: "bbb",
                    brand: "",
                    status: 0
                }, {
                    id: "03",
                    masterBrand: "ccc",
                    brand: "ccc-1",
                    status: 1
                }
            ]
        }

        BrandMappingController.prototype = {
            init: function () {
            },
            selectCart:function () {
                var self = this;
                if (self.searchInfo.selectedCart == 20) self.brandName="聚美";
                if (self.searchInfo.selectedCart == 22) self.brandName="京东";
                if (self.searchInfo.selectedCart == 23) self.brandName="天猫";
            }
        };
        return BrandMappingController;
    })())
});