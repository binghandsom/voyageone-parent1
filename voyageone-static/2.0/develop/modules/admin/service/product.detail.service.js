/**
 * @Name:    productPropsEditService
 * @Date:    2015/12/18
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define([
	'admin',
	'underscore',
	'modules/admin/enums/Status'
],function (admin, _) {

	admin.service("productDetailService", productDetailService);

	function productDetailService ($q, $productDetailService, $filter) {

		this.getProductInfo = getProductInfo;
		this.updateProductDetail = updateProductDetail;
		this.changeCategory = changeCategory;
		this.getProductPlatform =  getProductPlatform;
		this.changePlatformCategory =  changePlatformCategory;
		this.updateProductPlatformChk = updateProductPlatformChk;
		this.updateProductPlatform = updateProductPlatform;
		this.updateProductFeed = updateProductFeed;
		this.getCommonProductInfo = getCommonProductInfo;
		this.updateCommonProductInfo = updateCommonProductInfo;
		this.updateLock = updateLock;
		this.updateProductAtts = updateProductAtts;
		this.checkCategory = checkCategory;
		this.getChangeMastProductInfo = getChangeMastProductInfo;
		this.setMastProduct = setMastProduct;
		this.delisting = delisting;
		this.delistinGroup = delistinGroup;
		this.hsCodeChg = hsCodeChg;
		this.copyProperty = copyProperty;

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
									, result.data.productInfo.customAttributes
									, result.data.customProps);

						}

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
		 * 同时保存产品详情,产品自定义,SKU列表信息
		 * @param productFormData
		 * @param skuFormData
		 * @returns {*|Promise.<T>}
		 */
		function updateProductDetail (formData) {

			var temp = {customIds: [], customIdsCn: []};
			_.forEach(formData.feedInfoModel, function (feedInfo) {

				if (feedInfo.selected) {
					temp.customIds.push(feedInfo.enKey);
					temp.customIdsCn.push(feedInfo.cnKey);
				}

				if(feedInfo.enKey) {
					formData.customAttributes.cnAtts[feedInfo.enKey] = feedInfo.cnValue;
					if (feedInfo.enKey == feedInfo.key)
						formData.customAttributes.orgAtts[feedInfo.enKey] = feedInfo.value;
				}
			});
			formData.customAttributes.customIds = temp.customIds;
			formData.customAttributes.customIdsCn = temp.customIdsCn;

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
		 * 保存feed产品编辑页信息
		 * @param formData
         */
		function updateProductFeed(formData){
			var temp = {customIds: [], customIdsCn: []};
			_.forEach(formData.feedInfoModel, function (feedInfo) {

				if (feedInfo.selected) {
					temp.customIds.push(feedInfo.enKey);
					temp.customIdsCn.push(feedInfo.cnKey);
				}

				if(feedInfo.enKey) {
					formData.customAttributes.cnAtts[feedInfo.enKey] = feedInfo.cnValue;
					if (feedInfo.enKey == feedInfo.key)
						formData.customAttributes.orgAtts[feedInfo.enKey] = feedInfo.value;
				}
			});
			formData.customAttributes.customIds = temp.customIds;
			formData.customAttributes.customIdsCn = temp.customIdsCn;

			var data = {
				productId: formData.productId,
				modified: formData.modified,
				customAttributes: formData.customAttributes,
			};

			var defer = $q.defer();
			$productDetailService.updateProductFeed(data).then(function (res) {
				defer.resolve(res.data);
			},function(res){
				defer.reject(res.data);
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
		function _returnNew (data, list, object, customs) {
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


				var temp = {key: key, value: data[key], cnKey: cnKey, cnValue: cnValue, exists: false};

				// 设置在custom中存在的数据
				var customInfo = _.findWhere(customs, {feed_prop_original: key});
				if (customInfo) {
					temp = {key: key, value: data[key], selected: _.contains(list, key), cnKey: customInfo.feed_prop_translation, cnValue: cnValue, enKey: customInfo.feed_prop_original, exists: true};
					customs.splice(_.indexOf(customs, customInfo), 1);
				}
				result.push(temp);
			}

			var newResult = $filter('orderBy')(result, "exists", true);
			// 添加在custom中存在但是无法匹配到feed数据的字段
			_.forEach(newResult, function (showInfo) {
				if (!showInfo.exists && customs.length) {
					showInfo.enKey = customs[0].feed_prop_original;
					showInfo.cnKey = customs[0].feed_prop_translation;
					showInfo.selected = _.contains(list, showInfo.enKey);
					showInfo.cnValue = _.isUndefined(cnDataShow[showInfo.enKey]) ? cnData[showInfo.enKey] : cnDataShow[showInfo.enKey][1];
					customs.splice(0,1);
				}
			});

			return newResult;
		}

		/**
		 * 获取产品的平台属性
		 * @param { prodId:"",cartId:""} 产品id，平台id
         * @returns
         */
		function getProductPlatform(req){
			var defer = $q.defer();
			$productDetailService.getProductPlatform(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});

			return defer.promise;
		}

		/**
		 * 切换类目接口
		 * @param { prodId:"",cartId:"",catId:""} 产品ID，平台ID,catId:平台类目ID
         * @returns {*}
         */
		function changePlatformCategory(req){
			var defer = $q.defer();
			$productDetailService.changePlatformCategory(req)
				.then (function (res) {
					defer.resolve(res);
				});

			return defer.promise;
		}

		/**
		 *检查sku sale price
		 * @param { prodId:"",cartId:"",platform} 产品id，平台id,platform
		 */
		function updateProductPlatformChk(req){
			var defer = $q.defer();
			$productDetailService.updateProductPlatformChk(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});

			return defer.promise;
		}

		/**
		 *保存产品的平台属性
		 * @param { prodId:"",cartId:"",platform} 产品id，平台id,platform
         */
		function updateProductPlatform(req){
			var defer = $q.defer();
			$productDetailService.updateProductPlatform(req)
				.then (function (res) {
					defer.resolve(res);
				});

			return defer.promise;
		}

		/**
		 * 获取产品的平台属性
		 * @param { prodId:"",cartId:""} 产品id，平台id
		 * @returns
		 */
		function getCommonProductInfo(req){
			var defer = $q.defer();
			$productDetailService.getCommonProductInfo(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});

			return defer.promise;
		}

		/**
		 * master保存操作
		 * @param { prodId:"",productComm:""} 产品id，productComm(产品共通属性)
		 * @returns
		 * */
		function updateCommonProductInfo(req){
			var defer = $q.defer();
			$productDetailService.updateCommonProductInfo(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * master锁定操作
		 * @param { prodId:"",lock:""} 产品id，lock:'0','1'
		 * @returns
		 * */
		function updateLock(req){
			var defer = $q.defer();
			$productDetailService.updateLock(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * feed修改操作
		 * @param { prodId:"",feedInfo:""} 产品id，feed产品信息
		 * @returns
		 * */
		function updateProductAtts(req){
			var defer = $q.defer();
			$productDetailService.updateProductAtts(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * @param { cartId:"",catpath:""} 平台id，类目path
		 * 检查类目操作
		 */
		function checkCategory(req){
			var defer = $q.defer();
			$productDetailService.checkCategory(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * 获取主商品信息
		 * @param req {cartId:27,productCode:"CRBT0003SP-"} 平台id，产品code
         * @returns {*}
         */
		function getChangeMastProductInfo(req){
			var defer = $q.defer();
			$productDetailService.getChangeMastProductInfo(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * 设置主商品
		 * @param req {cartId:27,productCode:"CRBT0003SP-"}  平台id，产品code
         * @returns {*}
         */
		function setMastProduct(req){
			var defer = $q.defer();
			$productDetailService.setMastProduct(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * 产品下线
		 * @param req {cartId:27,productCode:"CRBT0003SP-",comment:""}  平台id，产品code,备注
		 * @returns {*}
		 */
		function delisting(req){
			var defer = $q.defer();
			$productDetailService.delisting(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * 全group下线
		 * @param req {cartId:27,productCode:"CRBT0003SP-",comment:""}  平台id，产品code，备注
		 * @returns {*}
		 */
		function delistinGroup(req){
			var defer = $q.defer();
			$productDetailService.delistinGroup(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * 税号改变
		 * @param req {prodId,hsCode}  产品id，新的税号code
		 * @returns {*}
		 */
		function hsCodeChg(req){
			var defer = $q.defer();
			$productDetailService.hsCodeChg(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}

		/**
		 * 复制主数据field到平台编辑页
		 * @param req {prodId,cartId}  产品id，平台id
		 */
		function copyProperty(req){
			var defer = $q.defer();
			$productDetailService.copyProperty(req)
				.then (function (res) {
					defer.resolve(res);
				},function(res){
					defer.reject(res);
				});
			return defer.promise;
		}
	}


});