/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('BrandMappingController', (function () {
        function BrandMappingController(brandMappingService, $translate) {
        	this.brandMappingService = brandMappingService;
            this.$translate = $translate;
            this.searchInfo = {
                selectedCart: null,
                selectedStatus: 2
            };
            this.cartList = [];
            this.brandMappingList = [];
        }

        BrandMappingController.prototype = {
            init: function () {
            	var self = this;
            	self.brandMappingService.init().then(function(res) {
            		self.cartList = res.data.cartList;
            	});
            },
            clear: function() {
            	var self = this;
            	self.searchInfo.selectedCart = null;
            	self.searchInfo.selectedStatus = '2';
            	self.searchInfo.selectedBrand = '';
            },
            selectCart: function () {
                var self = this;
            	self.brandMappingList = [];
                for (var i = 0; i < self.cartList.length; i++) {
                	if (self.cartList[i].value == self.searchInfo.selectedCart) {
                		self.brandName = self.cartList[i].name;
                		break;
                	}
                }
            },
            searchBrands: function() {
            	var self = this;
            	var post = {
            		'cartId': self.searchInfo.selectedCart,
            		'mappingState': self.searchInfo.selectedStatus,
            		'brandName': self.searchInfo.selectedBrand
            	};
            	self.brandMappingService.searchBrands(post).then(function(res) {
            		self.brandMappingList = res.data.brandList;
            	});
            }
        };
        
        return BrandMappingController;
    })())
});