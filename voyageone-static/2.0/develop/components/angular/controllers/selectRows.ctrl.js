/**
 * @Description:
 * select tables items
 * @User: linanbin
 * @Version: 2.0.0, 15/12/16
 */

angular.module('voyageone.angular.controllers.selectRows', [])
    .controller('selectRowsCtrl', function ($scope) {

        $scope.selectAll = selectAll;
        $scope.selectOne = selectOne;

        /**
         * 全部选中当前页数据
         * @param objectList
         */
        function selectAll(objectList,id) {

            objectList.selAllFlag = !objectList.selAllFlag;
            if(!id){
                id="id";
            }

            // 循环处理全选中的数据
            angular.forEach(objectList.currPageRows, function (object) {
                //if(objectList.selFlag.hasOwnProperty(object[id])) {

                    // 单签页面所有产品选中flag被标示
                    objectList.selFlag[object[id]] = objectList.selAllFlag;

                    if (objectList.hasOwnProperty('selList')) {
                        if (objectList.selAllFlag && objectList.selList.indexOf(object) < 0) {
                            objectList.selList.push(object);
                        } else if (!objectList.selAllFlag && objectList.selList.indexOf(object) > -1) {
                            objectList.selList.splice(objectList.selList.indexOf(object), 1);
                        }
                    }
                //}
            });
        }

        /**
         * 选中单条数据
         * @param currentId
         * @param objectList
         */
        function selectOne(currentId, objectList,id) {
            if(!id){
                id="id";
            }
            if (objectList.hasOwnProperty('selList')) {

                angular.forEach(objectList.currPageRows, function(object) {

                    if (_.isEqual(object[id], currentId)) {
                        if (objectList.selList.indexOf(object) > -1) {
                            objectList.selList.splice(objectList.selList.indexOf(object), 1);
                        } else {
                            objectList.selList.push(object);
                        }
                    }
                });
            }

            objectList.selAllFlag = true;
            angular.forEach(objectList.currPageRows, function(object) {
                //if (!objectList.selFlag[object.id]) {
                //    objectList.selAllFlag = false;
                //}
                if (objectList.selList.indexOf(object) == -1) {
                    objectList.selAllFlag = false;
                }
            })
        }
    });