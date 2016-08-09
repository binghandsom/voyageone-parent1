/**
 * @Name:    productPropsEditService
 * @Date:    2015/12/18
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define([
	'cms',
	'underscore',
	'modules/cms/enums/Carts'
],function (cms, _, Carts) {

	cms.service("groupDetailService", groupDetailService);

	function groupDetailService ($q, $groupDetailService, $translate, $filter) {

		this.getProductList = getProductList;
		this.init = init;
		this.setMainProduct = setMainProduct;

		function init(id) {
			var defer = $q.defer();
			$groupDetailService.init({id : id}).then(function (res) {
				_resetProductList(res.data);
				defer.resolve (res);
			});
			return defer.promise;
		}

		/**
		 * 检索product
		 * @param data
		 * @returns {*}
		 */
		function getProductList(id) {
			var defer = $q.defer();
			$groupDetailService.getProductList({id : id}).then(function (res) {
				_resetProductList(res.data);
				defer.resolve (res);
			});
			return defer.promise;
		}

		/**
		 * 设置该group的主数据
		 * @param data
		 * @returns {*}
         */
		function setMainProduct(data) {
			return $groupDetailService.setMainProduct(data);
		}

		/**
		 * 设置product list
		 * @param data
		 * @returns {*}
		 * @private
		 */
		function _resetProductList (data) {

			var cartId = data.groupInfo.cartId;

			_.forEach(data.productList, function (productInfo) {

				// 设置Inventory Detail
				productInfo.inventoryDetail = _setInventoryDetail(productInfo.common.skus);

				// 设置sku销售渠道信息
				productInfo.skuDetail = _setSkuDetail(productInfo.common.skus);

				// 设置price detail
				if (cartId != 0) {
					productInfo.priceDetail = _setPriceDetail(productInfo.platforms["P" + cartId], cartId);
					productInfo.priceSale = _setPriceSale(productInfo.platforms["P" + cartId], cartId);
					productInfo.platform = productInfo.platforms["P" + cartId];
				}
				else {
					productInfo.priceDetail = _setPriceDetail(productInfo.common.fields, cartId);
					productInfo.priceSale = _setPriceSale(productInfo.common.fields, cartId);
					productInfo.platform = {};
				}

			});

			var tempProductIds = [];
			_.forEach(data.productList, function (productInfo) {
				tempProductIds.push({id: productInfo.prodId, code: productInfo.common.fields.code});
			});
			data.productIds = tempProductIds;
		}

		/**
		 * 设置Inventory Detail
		 * @param skus
		 * @private
		 */
		function _setInventoryDetail(skus) {
			var result = [];
			_.forEach(skus, function (sku) {
				result.push(sku.skuCode + ": " + (sku.qty ? sku.qty: 0));
			});
			return result;
		}

		/**
		 * 设置Price Detail
		 * @param groups
		 * @returns {Array}
		 * @private
		 */
		function _setPriceDetail(object, cartId) {
			var result = [];
			if (cartId != 0) {
				var tempMsrpDetail = _setOnePriceDetail($translate.instant('TXT_MSRP_WITH_COLON'), object.pPriceMsrpSt, object.pPriceMsrpEd);
				if (!_.isNull(tempMsrpDetail))
					result.push(tempMsrpDetail);

				// 设置retail price
				var tempRetailPriceDetail = _setOnePriceDetail($translate.instant('TXT_RETAIL_PRICE_WITH_COLON'), object.pPriceRetailSt, object.pPriceRetailEd);
				if (!_.isNull(tempRetailPriceDetail))
					result.push(tempRetailPriceDetail);

				// 设置sale price
				var tempSalePriceDetail = _setOnePriceDetail($translate.instant('TXT_SALE_PRICE'), object.pPriceSaleSt, object.pPriceSaleEd);
				if (!_.isNull(tempSalePriceDetail))
					result.push(tempSalePriceDetail);
			} else {
				var tempMsrpDetail = _setOnePriceDetail($translate.instant('TXT_MSRP_WITH_COLON'), object.priceMsrpSt, object.priceMsrpEd);
				if (!_.isNull(tempMsrpDetail))
					result.push(tempMsrpDetail);

				// 设置retail price
				var tempRetailPriceDetail = _setOnePriceDetail($translate.instant('TXT_RETAIL_PRICE_WITH_COLON'), object.priceRetailSt, object.priceRetailEd);
				if (!_.isNull(tempRetailPriceDetail))
					result.push(tempRetailPriceDetail);
			}

			return result;
		}

		/**
		 * 设置页面上显示的价格
		 * @param object
		 * @returns {*}
		 * @private
		 */
		function _setPriceSale(object, cartId) {
			if (cartId != 0) {
				if (object.pPriceSaleSt == object.pPriceSaleEd)
					return object.pPriceSaleSt != null ? $filter('number')(object.pPriceSaleSt, 2) : '0.00';
				else
					return $filter('number')(object.pPriceSaleSt, 2) + '~' + $filter('number')(object.pPriceSaleEd, 2);
			} else {
				if (object.priceSaleSt == object.priceSaleEd)
					return object.priceSaleSt != null ? $filter('number')(object.priceSaleSt, 2) : '0.00';
				else
					return $filter('number')(object.priceSaleSt, 2) + '~' + $filter('number')(object.priceSaleEd, 2);
			}
		}

		/**
		 * 设置Price Detail
		 * @param priceStart
		 * @param priceEnd
		 * @returns {*}
		 * @private
		 */
		function _setOnePriceDetail(title, priceStart, priceEnd) {
			var result = null;
			if (!_.isUndefined(priceStart) && !_.isNull(priceStart)
					&& !_.isUndefined(priceEnd) && !_.isNull(priceEnd)) {
				result = _.isEqual(priceStart, priceEnd)
						? $filter('number')(priceStart, 2)
						: $filter('number')(priceStart, 2) + " ~ " + $filter('number')(priceEnd, 2);
			} else {
				result = _.isNumber(priceStart)
						? $filter('number')(priceStart, 2)
						: ((_.isNumber(priceEnd)
						? $filter('number')(priceEnd, 2)
						: null));
			}

			return _.isNull(result) ? null : title + result;
		}

		/**
		 * 设置time detail
		 * @param platforms
		 * @private
		 */
		function _setTimeDetail(product) {
			var result = [];

			if(!_.isEmpty(product.created))
				result.push($translate.instant('TXT_CREATE_TIME_WITH_COLON') + product.created.substring(0, 19));

			var platforms = product.groups;
			if(platforms.publishTime && !_.isEmpty(platforms.publishTime))
				result.push($translate.instant('TXT_PUBLISH_TIME_WITH_COLON') + platforms.publishTime.substring(0, 19));

			if(platforms.inStockTime && !_.isEmpty(platforms.inStockTime))
				result.push($translate.instant('TXT_ON_SALE_TIME_WITH_COLON') + platforms.inStockTime.substring(0, 19));

			return result;
		}

		/**
		 * 设置sku的销售渠道信息
		 * @param skus
		 * @returns {Array}
		 * @private
		 */
		function _setSkuDetail(skus) {
			var result = [];
			_.forEach(skus, function (sku) {
				result.push(sku.skuCode + ": " + sku.isSale);
			});
			return result;
		}
	}

});