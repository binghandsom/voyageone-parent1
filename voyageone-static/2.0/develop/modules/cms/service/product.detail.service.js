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
	'modules/cms/enums/Status'
],function (cms, _, Status) {

	cms.service("productDetailService", productDetailService);

	function productDetailService ($q, $productDetailService, $filter) {

		this.getProductInfo = getProductInfo;
		//this.updateProductInfo = updateProductInfo;
		//this.updateSkuInfo = updateSkuInfo;
		this.updateProductDetail = updateProductDetail;
		this.changeCategory = changeCategory;

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
				//if (res.data.productInfo.customAttributes) {
				//	result.data.productInfo.customAttributes.orgAtts = _returnNew(res.data.productInfo.customAttributes.orgAtts, result.data.productInfo.customAttributes.customIds);
				//	result.data.productInfo.customAttributes.cnAtts = _returnNew(res.data.productInfo.customAttributes.cnAtts);
                //
				//	// 设定哪些原始feed被添加到custom列表
				//	var feedKeys = _returnKeys(res.data.productInfo.customAttributes.orgAtts);
				//	result.data.productInfo.feedKeys = feedKeys;
				//}
				var feedKeys = _.values(result.data.productInfo.customAttributes.customIds);
				if(res.data.productInfo.feedInfoModel) {
					result.data.productInfo.feedInfoModel = _returnNew(res.data.productInfo.feedInfoModel
							, feedKeys
							, result.data.productInfo.customAttributes);

				}

				// 设置sku的渠道列表是否被选中
				angular.forEach(result.data.skus, function (sku) {
					var SelSkuCarts = [];
					angular.forEach(sku.skuCarts, function(skuCart) {
						SelSkuCarts[skuCart] = true;
					});
					sku.SelSkuCarts = SelSkuCarts;
				});

				// 设置产品状态
				if (result.data.productInfo.productStatus) {

					switch (result.data.productInfo.productStatus.approveStatus) {
						case Status.NEW:
						case Status.PENDING:
							result.data.productInfo.productStatus.statusInfo = {
								isWaitingApprove: false,
								isApproved: false,
								isDisable: false
							};
							break;
						case Status.READY:
							result.data.productInfo.productStatus.statusInfo = {
								isWaitingApprove: true,
								isApproved: false,
								isDisable: false
							};
							break;
						case Status.APPROVED:
							result.data.productInfo.productStatus.statusInfo = {
								isWaitingApprove: false,
								isApproved: true,
								isDisable: true
							};
							break;
					}
				}

				defer.resolve(result);
			});

			return defer.promise;
		}

		/**
		 * 保存产品详情和产品自定义
		 * @param formData
		 * @returns {*|Promise}
         */
		//function updateProductInfo (formData) {
		//	var data = {
		//		categoryId: formData.categoryId,
		//		categoryFullPath: formData.categoryFullPath,
		//		productId: formData.productId,
		//		modified: formData.modified,
		//		masterFields: [],
		//		customAttributes: formData.customAttributes
		//	};
        //
		//	angular.forEach(formData.masterFields, function (field) {
		//		if (field.type != "LABEL" && field.isDisplay != 0)
		//			data.masterFields.push(field);
		//	});
        //
		//	return $productDetailService.updateProductMasterInfo(data);
		//}

		/**
		 * 保存SKU列表信息
		 * @param formData
		 * @returns {*}
         */
		//function updateSkuInfo (formData) {
        //
		//	var data = {
		//		categoryId: formData.categoryId,
		//		categoryFullPath: formData.categoryFullPath,
		//		productId: formData.productId,
		//		modified: formData.modified,
		//		skuFields: formData.skuFields
		//	};
		//	return $productDetailService.updateProductSkuInfo(data);
		//}

		/**
		 * 同时保存产品详情,产品自定义,SKU列表信息
		 * @param productFormData
		 * @param skuFormData
		 * @returns {*|Promise.<T>}
         */
		function updateProductDetail (formData) {
			_.forEach(formData.feedInfoModel, function (feedInfo) {
				if (feedInfo.selected)
					formData.customAttributes.cnAtts[feedInfo.key] = feedInfo.cnValue;
			});

			var data = {
				categoryId: formData.categoryId,
				categoryFullPath: formData.categoryFullPath,
				productId: formData.productId,
				modified: formData.modified,
				masterFields: [],
				customAttributes: formData.customAttributes,
				productStatus: {
					approveStatus: formData.productStatus.statusInfo.isApproved
							? Status.APPROVED
							: (formData.productStatus.statusInfo.isWaitingApprove ? Status.READY : formData.productStatus.approveStatus),
					translateStatus: formData.productStatus.translateStatus ? "1" : "0"/*,
					editStatus: formData.productStatus.editStatus ? "1" : "0"*/
				},
				skuFields: formData.skuFields
			};

			angular.forEach(formData.masterFields, function (field) {
				if (field.type != "LABEL" && field.isDisplay != 0)
					data.masterFields.push(field);
			});

			return $productDetailService.updateProductAllInfo(data);
		}

		/**
		 * 切换商品的主类目
		 * @param data
         * @returns {*}
         */
		function changeCategory (data) {
			return $productDetailService.changeCategory(data);
		}

		/**
		 * 返回新对象
		 * @param {key: value} data
		 * @private
		 */
		function _returnNew (data, list, object) {
			var result = [];
			var cnData = object.cnAtts;
			var cnDataShow = object.cnAttsShow;
			for(var key in data) {
				var cnValue = '';
				var cnKey =  '';
				if (!_.isUndefined(cnDataShow[key]) || !_.isUndefined(cnData[key])) {
					cnKey = _.isUndefined(cnDataShow[key]) ? key : cnDataShow[key][0];
					cnValue = _.isUndefined(cnDataShow[key]) ? cnData[key] : cnDataShow[key][1];
				}

				if (list != null)
					result.push({key: key, value: data[key], selected: _.contains(list, key), cnKey: cnKey, cnValue: cnValue});
				else
					result.push({key: key, value: data[key], cnKey: cnKey, cnValue: cnValue});
			}
			return $filter('orderBy')(result, "selected", true);
		}

		///**
		// * 返回keys
		// * @param data
		// * @returns {Array}
         //* @private
         //*/
		//function _returnKeys (data) {
		//	var result = [];
		//	for(var key in data) {
		//		result.push(key);
		//	}
		//	return result;
		//}

	}

});