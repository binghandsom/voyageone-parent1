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
		this.updateProductInfo = updateProductInfo;
		this.updateSkuInfo = updateSkuInfo;
		this.updateProductDetail = updateProductDetail;

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
				result.data.productInfo.customAttributes.orgAtts = _returnNew(res.data.productInfo.customAttributes.orgAtts, result.data.productInfo.customAttributes.customIds);
				result.data.productInfo.customAttributes.cnAtts = _returnNew(res.data.productInfo.customAttributes.cnAtts);

				// 设定哪些原始feed被添加到custom列表
				var feedKeys = _returnKeys(res.data.productInfo.customAttributes.orgAtts);
				result.data.feedKeys = feedKeys;
				if(res.data.productInfo.feedInfoModel)
					result.data.productInfo.feedInfoModel.attributeList = _returnNew(res.data.productInfo.feedInfoModel.attributeList, feedKeys);

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
		function updateProductInfo (formData) {

			var data = {
				categoryId: formData.categoryId,
				categoryFullPath: formData.categoryFullPath,
				productId: formData.productId,
				modified: formData.modified,
				masterFields: [],
				customAttributes: formData.customAttributes
			};

			angular.forEach(formData.masterFields, function (field) {
				if (field.type != "LABEL" && field.isDisplay != 0)
					data.masterFields.push(field);
			});

			// TODO 需要对formData做处理
			return $productDetailService.updateProductMasterInfo(data);
		}

		/**
		 * 保存SKU列表信息
		 * @param formData
		 * @returns {*}
         */
		function updateSkuInfo (formData) {
			// TODO 需要对formData做处理
			return $productDetailService.updateProductSkuInfo(formData);
		}

		/**
		 * 同时保存产品详情,产品自定义,SKU列表信息
		 * @param productFormData
		 * @param skuFormData
		 * @returns {*|Promise.<T>}
         */
		function updateProductDetail (productFormData, skuFormData) {
			// TODO 需要对formData做处理
			return updateProductInfo(productFormData).then(function () {
				return updateSkuInfo(skuFormData);
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

		/**
		 * 判断当前页的选中状态
		 * @param list
		 * @private
		 */
		function _setCurrentPage (list) {
			list.selAllFlag = true;
			_.forEach(list.currPageRows, function (item) {
				if (_.findIndex(list.selList, item) > -1) {
					list.selFlag[item.id] = true;
				} else {
					list.selAllFlag = false;
				}
			});
		}

	}

});