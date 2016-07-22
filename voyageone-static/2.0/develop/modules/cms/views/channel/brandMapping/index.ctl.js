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
                cartList: [
                    {
                        cartId: 20,
                        cartName: "聚美"
                    },
                    {
                        cartId: 22,
                        cartName: "京东"
                    },
                    {
                        cartId: 23,
                        cartName: "天猫"
                    }
                ],

                selectedStatus: 2
            };
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
            selectCart: function () {
                var self = this;
                var brandName = "";
                if (self.searchInfo.selectedCart == 20) brandName = '聚美';
                self.$translate.instant('TXT_BRAND').replace("%s", brandName);

                console.log(brandName);
            }
        };
        return BrandMappingController;
    })())
});