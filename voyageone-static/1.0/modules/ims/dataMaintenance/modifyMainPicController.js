/**
 * @Name:    dataMaintenanceController.js
 * @Date:    2015/04/13
 *
 * @User:    Bob.Chen
 * @Version: 1.0.0
 */

define(function(require) {
    var omsApp = require('modules/ims/module');

    require('modules/ims/dataMaintenance/dataMaintenanceService');

    omsApp.controller('modifyMainPicController', ['$scope', 'modifyMainPicService',
        function($scope, modifyMainPicService) {

            /**
             * get the orderDetail info when the page initialize.
             */
            $scope.initialize = function() {

                // get the orderNumberList from previous page.
                $scope.warehouses = [
                    {
                        "label": "天猫",
                        "value": "tm"
                    },
                    {
                        "label": "淘宝",
                        "value": "tb"
                    },
                    {
                        "label": "天猫国际",
                        "value": "tg"
                    }
                ];

                $scope.fromWare = "tm";
                $scope.searchInfo = {};

                $scope.popInfo = {};
                $scope.popInfo.aList = [];

                //$scope.popInfo.aList[0] = {};
                //$scope.popInfo.aList[0].collapseFlag = false;
                //$scope.popInfo.aList[0].moudleCode = "27450832520";
                //$scope.popInfo.aList[0].bList = [];
                //$scope.popInfo.aList[0].selectedIndex = -1;
                //$scope.popInfo.aList[0].bList[0] = {};
                //$scope.popInfo.aList[0].bList[0].icon = "pic/vans-authentic-vn-0njvlla.jpg";
                //$scope.popInfo.aList[0].bList[0].id = "vans-authentic-vn-0njvlla";
                //$scope.popInfo.aList[0].bList[0].code = "vn-0njvlla";
                //$scope.popInfo.aList[0].bList[0].moudleName = "Vans Authentic 万斯经典板鞋 男鞋女鞋情侣款 帆布休闲鞋 滑板鞋";
                //$scope.popInfo.aList[0].bList[0].price = 384.0;
                //$scope.popInfo.aList[0].bList[0].status = "OF";
                //$scope.popInfo.aList[0].bList[0].stockCHN = "1";
                //$scope.popInfo.aList[0].bList[0].stockUSA = "8572";
                //$scope.popInfo.aList[0].bList[1] = {};
                //$scope.popInfo.aList[0].bList[1].icon = "pic/vans-authentic-vn-0ee3w00.jpg";
                //$scope.popInfo.aList[0].bList[1].id = "vans-authentic-vn-0ee3w00";
                //$scope.popInfo.aList[0].bList[1].code = "vn-0ee3w00";
                //$scope.popInfo.aList[0].bList[1].moudleName = "Vans Authentic 万斯经典板鞋 男鞋女鞋情侣款 帆布休闲鞋 滑板鞋";
                //$scope.popInfo.aList[0].bList[1].price = 416.0;
                //$scope.popInfo.aList[0].bList[1].status = "SN";
                //$scope.popInfo.aList[0].bList[1].stockCHN = "1";
                //$scope.popInfo.aList[0].bList[1].stockUSA = "8572";
                //$scope.popInfo.aList[0].bList[2] = {};
                //$scope.popInfo.aList[0].bList[2].icon = "pic/vans-authentic-vn-0ee3blk.jpg";
                //$scope.popInfo.aList[0].bList[2].id = "vans-authentic-vn-0ee3blk";
                //$scope.popInfo.aList[0].bList[2].code = "vn-0ee3blk";
                //$scope.popInfo.aList[0].bList[2].moudleName = "Vans Authentic 万斯经典板鞋 男鞋女鞋情侣款 帆布休闲鞋 滑板鞋";
                //$scope.popInfo.aList[0].bList[2].price = 416.0;
                //$scope.popInfo.aList[0].bList[2].status = "SN";
                //$scope.popInfo.aList[0].bList[2].stockCHN = "1";
                //$scope.popInfo.aList[0].bList[2].stockUSA = "8572";
                //
                //$scope.popInfo.aList[1] = {};
                //$scope.popInfo.aList[1].collapseFlag = false;
                //$scope.popInfo.aList[1].moudleCode = "42687854811";
                //$scope.popInfo.aList[1].bList = [];
                //$scope.popInfo.aList[1].selectedIndex = 2;
                //$scope.popInfo.aList[1].bList[0] = {};
                //$scope.popInfo.aList[1].bList[0].icon = "pic/vans-authentic-vn-0ee3red.jpg";
                //$scope.popInfo.aList[1].bList[0].id = "vans-authentic-vn-0ee3red";
                //$scope.popInfo.aList[1].bList[0].code = "vn-0ee3red";
                //$scope.popInfo.aList[1].bList[0].moudleName = "Vans Authentic 万斯经典板鞋 男鞋女鞋情侣款 帆布休闲鞋 滑板鞋";
                //$scope.popInfo.aList[1].bList[0].price = 416.0;
                //$scope.popInfo.aList[1].bList[0].status = "SN";
                //$scope.popInfo.aList[1].bList[0].stockCHN = "1";
                //$scope.popInfo.aList[1].bList[0].stockUSA = "8572";
                //$scope.popInfo.aList[1].bList[1] = {};
                //$scope.popInfo.aList[1].bList[1].icon = "pic/10499.jpg";
                //$scope.popInfo.aList[1].bList[1].id = "10499";
                //$scope.popInfo.aList[1].bList[1].code = "0010499";
                //$scope.popInfo.aList[1].bList[1].moudleName = "Vans Authentic 万斯经典板鞋 男鞋女鞋情侣款 帆布休闲鞋 滑板鞋";
                //$scope.popInfo.aList[1].bList[1].price = 416.0;
                //$scope.popInfo.aList[1].bList[1].status = "OF";
                //$scope.popInfo.aList[1].bList[1].stockCHN = "1";
                //$scope.popInfo.aList[1].bList[1].stockUSA = "8572";
                //$scope.popInfo.aList[1].bList[2] = {};
                //$scope.popInfo.aList[1].bList[2].icon = "pic/vans-authentic-vn-0ee3bka.jpg";
                //$scope.popInfo.aList[1].bList[2].id = "vans-authentic-vn-0ee3bka";
                //$scope.popInfo.aList[1].bList[2].code = "vn-0ee3bka";
                //$scope.popInfo.aList[1].bList[2].moudleName = "Vans Authentic 万斯经典板鞋 男鞋女鞋情侣款 帆布休闲鞋 滑板鞋";
                //$scope.popInfo.aList[1].bList[2].price = 416.0;
                //$scope.popInfo.aList[1].bList[2].status = "SN";
                //$scope.popInfo.aList[1].bList[2].stockCHN = "1";
                //$scope.popInfo.aList[1].bList[2].stockUSA = "8572";
            };

            $scope.collapseMoudle = function(index) {
                $scope.popInfo.aList[index].collapseFlag = ! $scope.popInfo.aList[index].collapseFlag;
            };

            $scope.doSelectMainPic = function(moudle, index) {
                var se = confirm("确认要替换为这张主图吗？");
                if (se == true) {
                    moudle.selectedIndex = index;
                }
            };

            $scope.doSearch = function() {

                console.log('modifyMainPicController doSearch');

                var data = $scope.searchInfo;

                modifyMainPicService.doSearch(data, $scope)
                    .then(function(response) {
                        $scope.popInfo = response.data;
                    //$scope.orderNotesList = response.data.orderNotesList;
                    //$scope.orderNotesSelectIndex = -1;
                });
                //alert($scope.fromWare);
            };

        }])
});
