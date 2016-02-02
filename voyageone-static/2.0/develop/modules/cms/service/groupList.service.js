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

	cms.service("groupListService", groupListService);

	function groupListService ($q, $groupListService) {

		this.getProductList = getProductList;
		this.init = init;
		this.setMainProduct = setMainProduct;

		function init(id, pagination) {
			var defer = $q.defer();
			var data = {
				pageNum : pagination.curr,
				pageSize :pagination.size,
				id : id
			};
			$groupListService.init(data).then(function (res) {
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
		function getProductList(id, pagination) {
			var defer = $q.defer();
			var data = {
				pageNum : pagination.curr,
				pageSize :pagination.size,
				id : id
			};
			$groupListService.getProductList(data).then(function (res) {
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
			return $groupListService.setMainProduct(data);
		}

		/**
		 * 设置product list
		 * @param data
		 * @returns {*}
		 * @private
		 */
		function _resetProductList (data) {
			_.forEach(data.productList, function (productInfo) {

				// 设置Inventory Detail
				productInfo.inventoryDetail = _setInventoryDetail(productInfo.skus);

				// 设置price detail
				productInfo.groups.priceDetail = _setPriceDetail(productInfo.fields);

				// 设置time detail
				productInfo.groups.platforms[0].timeDetail = _setTimeDetail(productInfo.groups.platforms[0]);
			});

			var tempProductIds = [];
			_.forEach(data.productIds, function (productInfo) {
				tempProductIds.push({id: productInfo.prodId});
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
		function _setPriceDetail(object) {
			var result = [];
			var tempMsrpDetail = _setOnePriceDetail("MSRP", object.priceMsrpSt, object.priceMsrpEd);
			if (!_.isNull(tempMsrpDetail))
				result.push(tempMsrpDetail);

			// 设置retail price
			var tempRetailPriceDetail = _setOnePriceDetail("Retail Price", object.priceRetailSt, object.priceRetailEd);
			if (!_.isNull(tempRetailPriceDetail))
				result.push(tempRetailPriceDetail);

			return result;
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
			if (_.isNumber(priceStart)
					&& _.isNumber(priceEnd)) {
				result = _.isEqual(priceStart, priceEnd)
						? priceStart
						: priceStart + " ~ " + priceEnd;
			} else {
				result = _.isNumber(priceStart)
						? priceStart
						: ((_.isNumber(priceEnd)
						? priceEnd
						: null));
			}

			return _.isNull(result) ? null : title + ": " + result;
		}

		/**
		 * 设置time detail
		 * @param platforms
		 * @private
		 */
		function _setTimeDetail(platforms) {
			var result = [];
			if(!_.isEmpty(platforms.publishTime))
				result.push("Publish Time: " + platforms.publishTime);

			if(!_.isEmpty(platforms.instockTime))
				result.push("On Sale Time: " + platforms.instockTime);

			return result;
		}
	}

});