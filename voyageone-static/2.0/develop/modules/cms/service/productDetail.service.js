/**
 * @Name:    productPropsEditService
 * @Date:    2015/12/18
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define([
	'cms',
	'underscore'
],function (cms, _) {

	cms.service("productDetailService", productDetailService);

	function productDetailService ($q, $productDetailService) {

		this.getProductInfo = getProductInfo;
		this.saveProductInfo = saveProductInfo;
		this.saveSkuInfo = saveSkuInfo;
		this.saveProductDetail = saveProductDetail;

		/**
		 * 获取页面产品信息
		 * @param formData
         * @returns {*}
         */
		function getProductInfo (formData) {
			var defer = $q.defer();
			$productDetailService.getProductInfo(formData)
			.then (function (res) {
				var result = angular.copy(res);

				// 设定custom列表中的feed被选中
				result.data.productInfo.feedAttributes.orgAtts = _returnNew(res.data.productInfo.feedAttributes.orgAtts, result.data.productInfo.feedAttributes.customIds);
				result.data.productInfo.feedAttributes.cnAtts = _returnNew(res.data.productInfo.feedAttributes.cnAtts);

				// 设定哪些原始feed被添加到custom列表
				var feedKeys = _returnKeys(res.data.productInfo.feedAttributes.orgAtts);
				result.data.feedKeys = feedKeys;
				result.data.feedAtts = _returnNew(res.data.feedAtts, feedKeys);

				// 设置sku的渠道列表是否被选中
				angular.forEach(result.data.skus, function (sku) {
					var SelSkuCarts = [];
					angular.forEach(sku.skuCarts, function(skuCart) {
						SelSkuCarts[skuCart] = true;
					});
					sku.SelSkuCarts = SelSkuCarts;
				});

				defer.resolve(result);
			});

			return defer.promise;
		}

		/**
		 * 保存产品详情和产品自定义
		 * @param formData
		 * @returns {*|Promise}
         */
		function saveProductInfo (formData) {

			// TODO 需要对formData做处理
			return $productDetailService.saveProductInfo(formData);
		}

		/**
		 * 保存SKU列表信息
		 * @param formData
		 * @returns {*}
         */
		function saveSkuInfo (formData) {
			// TODO 需要对formData做处理
			return $productDetailService.saveSkuInfo(formData);
		}

		/**
		 * 同时保存产品详情,产品自定义,SKU列表信息
		 * @param productFormData
		 * @param skuFormData
		 * @returns {*|Promise.<T>}
         */
		function saveProductDetail (productFormData, skuFormData) {
			// TODO 需要对formData做处理
			return saveProductInfo(productFormData).then(function () {
				return saveSkuInfo(skuFormData);
			})
		}

		/**
		 * 返回新对象
		 * @param {key: value} data
		 * @private
		 */
		function _returnNew (data, list) {
			var result = [];
			for(var key in data) {
				if (list != null)
					result.push({key: key, value: data[key], selected: _.contains(list, key)});
				else
					result.push({key: key, value: data[key]});
			}
			return result;
		}

		/**
		 * 返回keys
		 * @param data
		 * @returns {Array}
         * @private
         */
		function _returnKeys (data) {
			var result = [];
			for(var key in data) {
				result.push(key);
			}
			return result;
		}

	}

});