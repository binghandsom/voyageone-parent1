/**
 * Created by linanbin on 15/12/7.
 */

define([
    'angularAMD'
], function (angularAMD) {
    angularAMD
        .constant('popActions', {
            "column_define": {
                "templateUrl": "views/pop/column_define/index.tpl.html",
                "controllerUrl": "modules/views/cms/pop/column_define/index.ctl"
            },
            "new": {
                "templateUrl": "views/pop/new/promotion.tpl.html",
                "controllerUrl": "modules/views/cms/pop/new/promotion.ctl"
            },
            "other": {
                "platform": {
                    "templateUrl": "views/pop/other/platform.tpl.html",
                    "controllerUrl": "modules/views/cms/pop/other/platform.ctl"
                },
                "progress": {
                    "templateUrl": "views/pop/other/progress.tpl.html",
                    "controllerUrl": "modules/views/cms/pop/other/progress.ctl"
                }
            },
            "tag":{
                "promotion": {
                    "templateUrl": "views/pop/tag/promotion/index.tpl.html",
                    "controllerUrl": "modules/views/cms/pop/tag/promotion/index.ctl"
                },
                "shop_category": {
                    "templateUrl": "views/pop/tag/shop_category/index.tpl.html",
                    "controllerUrl": "modules/views/cms/pop/tag/shop_category/index.ctl"
                }
            },
            "prop_change": {
                "templateUrl": "views/pop/prop_change/index.tpl.html",
                "controllerUrl": "modules/views/cms/pop/prop_change/index.ctl"
            },
            "category": {
                "templateUrl": "views/pop/category/index.tpl.html",
                "controllerUrl": "modules/views/cms/pop/category/index.ctl"
            },
            "feed": {
                "templateUrl": "views/pop/feed/index.tpl.html",
                "controllerUrl": "modules/views/cms/pop/feed/index.ctl"
            },
            "feed_list": {
                "templateUrl": "views/pop/feed_list/index.tpl.html",
                "controllerUrl": "modules/views/cms/pop/feed_list/index.ctl"
            },
            "product": {
                "price": {
                    "templateUrl": "views/pop/product/price/history.tpl.html",
                    "controllerUrl": "modules/views/cms/pop/product/price/history.ctl"
                },
                "promotion": {
                    "templateUrl": "views/pop/product/promotion/history.tpl.html",
                    "controllerUrl": "modules/views/cms/pop/product/promotion/history.ctl"
                }
            }
        })
        .controller('popupCtrl', popupCtrl);

    function popupCtrl($scope, $modal, popActions) {
        var vm = this;

        $scope.openCustomBaseProperty = openCustomBaseProperty;
        function openCustomBaseProperty(viewSize) {
            $modal.open({
                templateUrl: popActions.column_define.templateUrl,
                controllerUrl: popActions.column_define.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openupdateProperties = openupdateProperties;
        function openupdateProperties(viewSize) {
            $modal.open({
                templateUrl: popActions.prop_change.templateUrl,
                controllerUrl: popActions.prop_change.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openNewpromotion = openNewpromotion;
        function openNewpromotion(viewSize) {
            $modal.open({
                templateUrl: popActions.tag.promotion.templateUrl,
                controllerUrl: popActions.tag.promotion.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openNewcategory = openNewcategory;
        function openNewcategory(viewSize) {
            $modal.open({
                templateUrl: popActions.category.templateUrl,
                controllerUrl: popActions.category.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openshop_category = openshop_category;
        function openshop_category(viewSize) {
            $modal.open({
                templateUrl: popActions.tag.shop_category.templateUrl,
                controllerUrl: popActions.tag.shop_category.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openhistorypromotion = openhistorypromotion;
        function openhistorypromotion(viewSize) {
            $modal.open({
                templateUrl: popActions.product.promotion.templateUrl,
                controllerUrl: popActions.product.promotion.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openpricepromotion = openpricepromotion;
        function openpricepromotion(viewSize) {
            $modal.open({
                templateUrl: popActions.product.price.templateUrl,
                controllerUrl: popActions.product.price.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openpromotion = openpromotion;
        function openpromotion(viewSize) {
            $modal.open({
                templateUrl: popActions.new.templateUrl,
                controllerUrl: popActions.new.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openOtherPlatform = openOtherPlatform;
        function openOtherPlatform(viewSize) {
            $modal.open({
                templateUrl: popActions.other.platform.templateUrl,
                controllerUrl: popActions.other.platform.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openOtherProgress = openOtherProgress;
        function openOtherProgress(viewSize) {
            $modal.open({
                templateUrl: popActions.other.progress.templateUrl,
                controllerUrl: popActions.other.progress.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openfeed = openfeed;
        function openfeed(viewSize) {
            $modal.open({
                templateUrl: popActions.feed.templateUrl,
                controllerUrl: popActions.feed.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
        $scope.openfeed_list = openfeed_list;
        function openfeed_list(viewSize) {
            $modal.open({
                templateUrl: popActions.feed_list.templateUrl,
                controllerUrl: popActions.feed_list.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }
    }
});