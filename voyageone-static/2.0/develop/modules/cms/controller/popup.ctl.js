/**
 * Created by linanbin on 15/12/7.
 */

define([
    'cms',
    'underscore'
], function (cms, _) {
    cms
        .constant('popActions', {
            "column_define": {
                "templateUrl": "views/pop/column_define/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/column_define/index.ctl"
            },
            "new": {
                "templateUrl": "views/pop/new/promotion.tpl.html",
                "controllerUrl": "modules/cms/views/pop/new/promotion.ctl"
            },
            "other": {
                "platform": {
                    "templateUrl": "views/pop/other/platform.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/other/platform.ctl"
                },
                "progress": {
                    "templateUrl": "views/pop/other/progress.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/other/progress.ctl"
                }
            },
            "tag":{
                "promotion": {
                    "templateUrl": "views/pop/tag/promotion/index.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/tag/promotion/index.ctl"
                },
                "shop_category": {
                    "templateUrl": "views/pop/tag/shop_category/index.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/tag/shop_category/index.ctl"
                }
            },
            "prop_change": {
                "templateUrl": "views/pop/prop_change/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/prop_change/index.ctl"
            },
            "category": {
                "templateUrl": "views/pop/category/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/category/index.ctl"
            },
            "feed": {
                "templateUrl": "views/pop/feed/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/feed/index.ctl"
            },
            "feed_list": {
                "templateUrl": "views/pop/feed_list/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/feed_list/index.ctl"
            },
            "import": {
                "templateUrl": "views/pop/import/index.tpl.html",
                "controllerUrl": "modules/views/cms/pop/import/index.ctl"
            },
            "product": {
                "price": {
                    "templateUrl": "views/pop/product/price/history.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/product/price/history.ctl"
                },
                "promotion": {
                    "templateUrl": "views/pop/product/promotion/history.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/product/promotion/history.ctl"
                }
            }
        })
        .controller('popupCtrl', popupCtrl);

    function popupCtrl($scope, $modal, popActions) {

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
        function openupdateProperties(viewSize,data) {
            require([popActions.prop_change.controllerUrl], function(){
                $modal.open({
                    templateUrl: popActions.prop_change.templateUrl,
                    controller: 'popPropChangeCtl',
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * pop出promotion选择页面,用于设置
         * @type {openTagPromotion}
         */
        $scope.openTagPromotion = openTagPromotion;
        function openTagPromotion(viewSize, promotion, selList) {
            require([popActions.tag.promotion.controllerUrl], function () {
                if (selList.length) {
                    $modal.open({
                        templateUrl: popActions.tag.promotion.templateUrl,
                        controller: 'popTagPromotionCtl',
                        size: viewSize,
                        resolve: {
                            promotion: function () {
                                var productIds = [];
                                _.forEach(selList, function (object) {
                                    productIds.push(object.id);
                                });
                                return {"promotion": promotion, "productIds": productIds};
                            }
                        }
                    });
                }
            });
        }
        $scope.openNewcategory = openNewcategory;
        function openNewcategory(viewSize, context) {
            require([popActions.category.controllerUrl], function() {
                $modal.open({
                    templateUrl: popActions.category.templateUrl,
                    controller: 'categoryPopupController as ctrl',
                    size: viewSize,
                    resolve: {
                        context: function () {
                            return context;
                        }
                    }
                });
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
        function openpromotion(viewSize,data) {
            require([popActions.new.controllerUrl], function(){
                $modal.open({
                    templateUrl: popActions.new.templateUrl,
                    controller: 'popNewPromotionCtl',
                    size: viewSize,
                    resolve: {
                        items: function () {
                            return data;
                        }
                    }
                });
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
        $scope.openImport = openImport;
        function openImport(viewSize) {
            $modal.open({
                templateUrl: popActions.import.templateUrl,
                controllerUrl: popActions.import.controllerUrl,
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