(function() {
    /**
     * @Description:
     * select tables items
     * @User: linanbin
     * @Version: 2.0.0, 15/12/16
     */
    angular.module("voyageone.angular.controllers.selectRows", []).controller("selectRowsCtrl", function($scope) {
        $scope.selectAll = selectAll;
        $scope.selectOne = selectOne;
        $scope.isAllSelected = isAllSelected;
        /**
         * 全部选中当前页数据
         * @param objectList
         */
        function selectAll(objectList, id) {
            objectList.selAllFlag = !objectList.selAllFlag;
            if (!id) {
                id = "id";
            }
            // 循环处理全选中的数据
            angular.forEach(objectList.currPageRows, function(object) {
                // 单签页面所有产品选中flag被标示
                if (object['_nonChkFlg'] == undefined) {
                    objectList.selFlag[object[id]] = objectList.selAllFlag;
                }

                if (objectList.hasOwnProperty("selList")) {
                    var tempList = _.pluck(objectList.selList, id);
                    if (objectList.selAllFlag && tempList.indexOf(object[id]) < 0) {
                        if (object['_nonChkFlg'] == 1) {
                            return;
                        }
                        objectList.selList.push(object);
                    } else if (!objectList.selAllFlag && tempList.indexOf(object[id]) > -1) {
                        objectList.selList.splice(tempList.indexOf(object[id]), 1);
                    }
                }
            });
        }
        /**
         * 选中单条数据
         * @param currentId
         * @param objectList
         */
        function selectOne(currentId, objectList, id) {
            if (!id) {
                id = "id";
            }
            if (objectList.hasOwnProperty("selList")) {
                angular.forEach(objectList.currPageRows, function(object) {
                    var tempList = _.pluck(objectList.selList, id);
                    if (_.isEqual(object[id], currentId)) {
                        if (tempList.indexOf(object[id]) > -1) {
                            objectList.selList.splice(tempList.indexOf(object[id]), 1);
                        } else {
                            objectList.selList.push(object);
                        }
                    }
                });
            }
            objectList.selAllFlag = true;
            tempList = _.pluck(objectList.selList, id);
            angular.forEach(objectList.currPageRows, function(object) {
                if (tempList.indexOf(object[id]) == -1 && object['_nonChkFlg'] == undefined) {
                    objectList.selAllFlag = false;
                }
            });
        }
        /**
         * 判断当前页是否为全选中
         * @param objectList
         * @param id
         */
        function isAllSelected(objectList, id) {
            if (!id) {
                id = "id";
            }
            if (objectList != undefined) {
                objectList.selAllFlag = true;
                var tempList = _.pluck(objectList.selList, id);
                angular.forEach(objectList.currPageRows, function(object) {
                    if (tempList.indexOf(object[id]) == -1 && object['_nonChkFlg'] == undefined) {
                        objectList.selAllFlag = false;
                    }
                });
                return objectList.selAllFlag;
            }
            return false;
        }
    });
})();