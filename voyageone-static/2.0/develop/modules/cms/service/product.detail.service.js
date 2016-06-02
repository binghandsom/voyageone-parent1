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
		this.updateProductDetail = updateProductDetail;
		this.changeCategory = changeCategory;
		//this._setProductStatus = _setProductStatus;

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
						//if (result.data.productInfo.productStatus) {
						//	_setProductStatus(result.data.productInfo.productStatus);
						//}

						defer.resolve(result);
					});

			return defer.promise;
		}

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

			// 设定status
			//var status = formData.productStatus.approveStatus;
			//if (formData.productStatus.statusInfo.isApproved)
			//	status = Status.APPROVED;
			//else if (formData.productStatus.statusInfo.isWaitingApprove)
			//	status = Status.READY;

			var data = {
				categoryId: formData.categoryId,
				categoryFullPath: formData.categoryFullPath,
				productId: formData.productId,
				modified: formData.modified,
				masterFields: [],
				customAttributes: formData.customAttributes,
				productStatus: {
					approveStatus: formData.productStatus.approveStatus,
					translateStatus: formData.productStatus.translateStatus ? "1" : "0"
				},
				skuFields: formData.skuFields
			};



			angular.forEach(formData.masterFields, function (field) {
				if (field.type != "LABEL" && field.isDisplay != 0)
					data.masterFields.push(field);
			});

			var defer = $q.defer();
			$productDetailService.updateProductAllInfo(data).then(function (res) {

				defer.resolve(res.data);
			});
			return defer.promise;
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
		// * 转换成画面上能用项目
		// * @param productStatus
		// * @private
		// */
		//function _setProductStatus (productStatus) {
        //
		//	switch (productStatus.approveStatus) {
		//		case Status.NEW:
		//		case Status.PENDING:
		//			productStatus.statusInfo = {
		//				isWaitingApprove: false,
		//				isApproved: false,
		//				isDisable: false
		//			};
		//			break;
		//		case Status.READY:
		//			productStatus.statusInfo = {
		//				isWaitingApprove: true,
		//				isApproved: false,
		//				isDisable: false
		//			};
		//			break;
		//		case Status.APPROVED:
		//			productStatus.statusInfo = {
		//				isWaitingApprove: true,
		//				isApproved: true,
		//				isDisable: true
		//			};
		//			break;
		//	}
        //
		//}
	}

});