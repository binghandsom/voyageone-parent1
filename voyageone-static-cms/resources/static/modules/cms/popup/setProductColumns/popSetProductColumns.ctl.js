/**
 * Created by edward-pc1 on 2015/9/18.
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');

    require ('modules/cms/popup/setProductColumns/popSetProductColumns.service');
    require ('modules/cms/common/common.service');

    cmsApp.controller('popSetProductColumns', ['$scope', '$modalInstance', '$q', 'popSetProductColumnsService', 'cmsCommonService', 'parameters', 'userService', 'notify',
        function ($scope, $modalInstance, $q, popSetProductColumnsService, cmsCommonService, parameters, userService, notify) {

            var commonUtil = require('components/util/commonUtil');

            /**
             * 获得初始化数据.
             */
            $scope.initialize = function () {

                $scope.baseColumns = [];
                $scope.priceColumns = [];
                $scope.salesColumns = [];
                $scope.currentColumns = [];

                // 取得当前用户被选中的属性列表.
                var currentProductAttribute = {};
                switch (parameters.type) {
                    case 'us':
                        currentProductAttribute = userService.getUserConfig().cmsUsProductAttributes;
                        break;
                    case 'cn':
                        currentProductAttribute = userService.getUserConfig().cmsCnProductAttributes;
                        break;
                }

                popSetProductColumnsService.doGetProductColumns(parameters.type).then(function(data) {
                    _.forEach(data, function (productColumnInfo) {

                        //设置默认值该属性在列表中显示与否
                        productColumnInfo.showFlag = true;

                        // 获取用户当前的属性列表对应的属性对象.
                        if (_.indexOf(currentProductAttribute, productColumnInfo.attributeValueId) > -1) {
                            productColumnInfo.showFlag = false;
                            $scope.currentColumns.push(productColumnInfo);
                        }

                        // 取得数据库中现在保存的所有的书信，并将其分类.
                        switch (productColumnInfo.attributeValue3) {
                            case '1':
                                $scope.baseColumns.push(productColumnInfo);
                                break;
                            case '2':
                                $scope.priceColumns.push(productColumnInfo);
                                break;
                            case '3':
                                $scope.salesColumns.push(productColumnInfo);
                                break;
                            default :
                                break;
                        }
                    });
                })
            };

            /**
             * 关闭当前窗口.
             */
            $scope.close = function () {
                $modalInstance.dismiss('close');
            };

            /**
             * 将该用户选择的columns保存到数据，并重新刷新主页面.
             */
            $scope.save = function () {

                var data = {};
                var currentAttributeValueIdList = [];
                switch (parameters.type) {
                    case 'us':
                        data.cfgName = 'cms_us_product_attributes';
                        break;
                    case 'cn':
                        data.cfgName = 'cms_cn_product_attributes';
                        break;
                }

                $scope.currentColumns = _.sortBy($scope.currentColumns, 'attributeValueId');
                _.forEach($scope.currentColumns, function (object) {
                    currentAttributeValueIdList.push(object.attributeValueId);
                });
                data.cfgVal1 = currentAttributeValueIdList.toString();

                popSetProductColumnsService.doSaveProductColumns(data).then(function () {
                    switch (parameters.type) {
                        case 'us':
                            userService.getUserConfig().cmsUsProductAttributes = currentAttributeValueIdList;
                            break;
                        case 'cn':
                            userService.getUserConfig().cmsCnProductAttributes = currentAttributeValueIdList;
                            break;
                    }
                    $modalInstance.close('');
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                });
            };

            /**
             * 取消被选中的Column.
             * @param currentColumnInfo
             */
            $scope.doCancelSelect = function (currentColumnInfo) {
                $scope.currentColumns = _.filter($scope.currentColumns, function (v) {
                    return v != currentColumnInfo;
                });
                // 将取消掉的column放回到原来的list中.
                if (_.isEqual(currentColumnInfo.attributeValue3, '1')) {
                    findTheColumn($scope.baseColumns, currentColumnInfo).then(function(index) {
                        $scope.baseColumns[index].showFlag = true;
                    })
                }
                else if (_.isEqual(currentColumnInfo.attributeValue3, '2')) {
                    findTheColumn($scope.priceColumns, currentColumnInfo).then(function(index) {
                        $scope.priceColumns[index].showFlag = true;
                    });
                }
                else if (_.isEqual(currentColumnInfo.attributeValue3, '3')) {
                    findTheColumn($scope.salesColumns, currentColumnInfo).then(function(index) {
                        $scope.salesColumns[index].showFlag = true;
                    });
                }
            };

            /**
             * 将被选中的column放入到下面将要显示的list中.
             * @param $event
             * @param $data
             * @param array
             */
            $scope.onDrop = function ($event,$data,array) {
                $data.showFlag = false;
                array.push($data);
            };

            /**
             * 被选中的column从展示list中隐藏.
             * @param $event
             * @param index
             * @param array
             */
            $scope.dropSuccessHandler = function ($event,index,array) {
                array[index].showFlag = false;
            };

            /**
             * 返回当前选中column在原list中的index.
             * @param array
             * @param object
             */
            function findTheColumn (array, object) {
                var defer = $q.defer();
                _.forEach(array, function(currentObject, index) {
                    if (_.isEqual(currentObject.attributeValueId, object.attributeValueId)) {
                        defer.resolve(index);
                    }
                });
                return defer.promise;
            }

        }]);

});
