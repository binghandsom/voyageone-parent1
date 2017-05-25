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
                		var tblData = resp.data.data;
                		countTotalStock(tblData);
                		resetHeaderView(tblData.header);
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
                	if (tblData.stocks.length > 0) {
                    	var copyHeader = angular.copy(tblData.header);
                    	var stockHeader = angular.copy(tblData.header);
                    	// 删除不需要统计的表头
                    	copyHeader.base.splice(0, 3);
                    	// 统计库存信息
                    	var totalStock = [];
                    	angular.forEach(copyHeader, function(items, key) {
                    		totalStock[key] = {};
                    		angular.forEach(items, function(item) {
                    			var countTotal = [];
                    			var totalAvailableQty = 0; // 当前项总的可用库存
                    			var totalOccupiedQty = 0;  // 当前项总的占用库存
                    			angular.forEach(tblData.stocks, function(stock) {
                    				if (!angular.isDefined(stock[key][item])) {
                    					stock[key][item] = [0,0];
                    				}
                                    totalAvailableQty += stock[key][item][0];
                                    totalOccupiedQty += stock[key][item][1];
                    				// countTotal += stock[key][item];
                    				// angular.forEach(stock[key][item], function (qty) {
                                     //    countTotal += qty;
                                    // })
                    			});
                                countTotal.push(totalAvailableQty);
                                countTotal.push(totalOccupiedQty);


                    			// 保存库存为0的总库存，删除其他库存为0的仓库
                    			if (key != 'order' && item != 'total' && countTotal[0] == 0 && countTotal[1] == 0) {
                    				stockHeader[key].splice(stockHeader[key].indexOf(item), 1);
                    			} else {
                    				totalStock[key][item] = countTotal;
                    			}
                    		});
                    	});
                    	
                    	tblData.header = stockHeader;
                    	tblData.stocks.push(totalStock);
                	}
                }
                
                function resetHeaderView(header) {
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