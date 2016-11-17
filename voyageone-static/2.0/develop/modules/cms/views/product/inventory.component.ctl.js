/**
 * @author sofia
 * @description sku库存
 * @version 2.9.0
 * @datetime 2016/10/24.
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.directive("inventorySchema", function (productDetailService) {

        var _inventConfig = {
            orgTh: {
                baseColumns: '3',
                inOwnColumns: '1',
                inNOwnColumns: '1',
                gbOwnColumns: '1',
                gbNOwnColumns: '1'
            },
            expandTh: {
                baseColumns: '4',
                inOwnColumns: '6',
                inNOwnColumns: '4',
                gbOwnColumns: '2',
                gbNOwnColumns: '8'
            }
        };

        return {
            restrict: "E",
            templateUrl: "views/product/inventory.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope) {

                initialize();
                scope.count = count;

                function initialize() {
                    scope.showDetail = false;
                    scope.thConfig = _inventConfig.orgTh;
                	productDetailService.getSkuStockInfo(scope.productInfo.productId)
                	.then(function(resp) {
                		var tblData = resp.data;
                		countTotalStock(tblData);
                		resetHeader(tblData.header);
                		scope.tblData = tblData;
                		// 重新设置表头的合并列
                        angular.extend(_inventConfig.expandTh, {
                            inOwnColumns: tblData.header.inOwn.length,
                            inNOwnColumns: tblData.header.inNOwn.length,
                            gbOwnColumns: tblData.header.gbOwn.length,
                            gbNOwnColumns: tblData.header.gbNOwn.length
                        });
                	});
                }
                
                function countTotalStock(tblData) {
                	var copyHeader = angular.copy(tblData.header);
                	// 删除不需要统计的表头
                	copyHeader.base.splice(0, 3);
                	// 统计库存信息
                	var totalStock = {};
                	angular.forEach(copyHeader, function(items, key) {
                		totalStock[key] = {};
                		angular.forEach(items, function(item) {
                			var countTotal = 0;
                			angular.forEach(tblData.stocks, function(stock) {
                				countTotal = stock[key][item] ? stock[key][item] : 0;
                			});
            				totalStock[key][item] = countTotal;
                		});
                	});
                	tblData.stocks.push(totalStock);
                }
                
                function resetHeader(header) {
                	var copyHeader = angular.copy(header);
                	// 重置表头信息，以便页面显示
                	angular.forEach(copyHeader, function(items, key) {
                		var newItems = [];
                		angular.forEach(items, function(item) {
                			newItems.push({
                				name: item,
                				title: item == 'total' ? '库存总量' : item
                			});
                		});
                		header[key] = newItems;
                	});
                }

                function count(value) {
                    if (value == true) {
                        scope.thConfig = _inventConfig.expandTh;	
                    } else {
                        scope.thConfig = _inventConfig.orgTh;
                    }
                }
            }
        };
    });
});