/**
 * @User: sky
 * @Date: 2015-04-09 17:16:50
 * @LastUpdate: 2015-05-29
 * @Version: 0.0.1
 */
define([
    "modules/wms/wms.module",
    "modules/wms/directives/popReservationLog/popReservationLog",
    "modules/wms/directives/popSkuHisList/popSkuHisList",
	"../directives/popInventoryInfo/popInventoryInfo",
	"../directives/popChangeReservation/popChangeReservation",
    "modules/wms/reservation/reservationService",
	"components/directives/dialogs/dialogs",
    "components/directives/paging"
], function (wms) {
    wms
        .controller("reservationListCtrl", [
            "$scope",
            "reservationService",
			"vConfirm",
			"$filter",
			"wmsChangeReservation",
            function ($scope, reservationService, confirm, $filter, wmsChangeReservation) {

				//根据列表每一行的内容设置按钮是否显示
				$scope.setValue = setValue;

				//变更仓库
				$scope.changeStore = changeStore;

				//根据所选种类更新物品
				$scope.changeKind = changeKind;

                // 检索条件结构体
                $scope.search = {
                	reservation_id: String(),
                	order_number: String(),
                	page:1,
        			rows: 20 //pageSize
                };

            	//检索方法
				$scope.doSearch = function(page){
            		reqSearch($scope.pageOption.curr = page);
            	};

            	//翻页用结构体
				$scope.pageOption = {
            		curr: 1,
            		total: 1,
            		size: 20,
            		fetch: reqSearch
            	};

				var liViewCtl = $scope.liViewCtl = {};

            	//页面初始化数据
            	$scope.initialize = function(){
            		reservationService.doInit({"rsvStatus":"14",
            									"ship_channel_id":"13",
            									"isLock":"7",
            									"hasIdCard":"16",
            									"order_status":"5"}, $scope).then(function(response){
                    	$scope.propertyPermissions = response.data.propertyPermissions;
                    	$scope.shopList = response.data.shopList;
                    	$scope.rsvStatus = response.data.rsvStatus;
                    	$scope.ship_channel_id = response.data.ship_channel_id;
                    	$scope.isLock = response.data.isLock;
                    	$scope.hasIdCard = response.data.hasIdCard;
                    	$scope.order_status = response.data.order_status;
                        $scope.userStore = response.data.userStore;
                        $scope.search.store_id = response.data.userStore[0].store_id;
						$scope.search.order_channel_id = response.data.propertyPermissions[0].propertyId;
						$scope.search.orderDateTime_s = response.data.fromDate;
						$scope.search.orderDateTime_e = response.data.toDate;
						$scope.search.cart_id = response.data.shopList[0].shop_id;
						//设置ResStatus为Open状态
						$scope.search.res_status_id = response.data.defaultStatus;
						//初始化页面的时候按照默认条件搜索
						reqSearch(1);
            		});
            	};

				//查询及翻页调用函数
				function reqSearch(page) {
					$scope.search.page = page;
					var dateFilter = $filter("date");
					$scope.search.orderDateTime_s = dateFilter($scope.search.orderDateTime_s, 'yyyy-MM-dd');
					$scope.search.orderDateTime_e = dateFilter($scope.search.orderDateTime_e, 'yyyy-MM-dd');
            		reservationService.doSearch($scope.search, $scope).then(function(response) {
                    	$scope.reservationList = response.data.reservationInfo;
                    	$scope.pageOption.total = response.data.total;
                    });
            	}

				//将列表中被点击的对象传递到后台
				function setValue(reservationInfo){
					$scope.reservationList.selected = reservationInfo;
					operationButtonShowCtl(reservationInfo);
				}

				//将列表中被点击的需要变更仓库的对象传递到后台
				function changeStore(reservationInfo){
					$scope.reservationList.selected = reservationInfo;
					changeKind("1","");
				}

				//表格操作按钮显示控制
				function operationButtonShowCtl(reservationInfo){
					liViewCtl.cancel = false;
					liViewCtl.backOrder = false;
					liViewCtl.open = false;
					liViewCtl.backOrderConfirmed = false;
					//状态为11(open)的时候才展示【取消】、【超卖】按钮。并且可变更仓库
					if(reservationInfo.res_status_id == '11'){
						liViewCtl.cancel = true;
						liViewCtl.backOrder = true;
					}
					//状态为98(backOrder)的时候才展示【打开】、【取消】、【确认】按钮
					if(reservationInfo.res_status_id == '98'){
						liViewCtl.open  = true;
						liViewCtl.cancel = true;
						liViewCtl.backOrderConfirmed  = true;
					}
					//状态为96(backOrderConfirmed)的时候允许【取消】按钮
					if(reservationInfo.res_status_id == '96'){
						liViewCtl.cancel = true;
					}

				}

				//根据所选状态更新物品
				function changeKind(changeKind,processContent){
					var reservation = $scope.reservationList.selected;

					reservation.changeKind = changeKind;
					reservation.processContent = processContent;

					wmsChangeReservation($scope,reservation);

				}
            }

        ]);
});
