/**
 * @Name:    cms.route.js
 * @Date:    2015/6/16
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var angularAMD = require ('angularAMD'),
        mainApp = require ('components/app');

    mainApp.constant ('cmsRoute', {
        //'oms_default_index': {
        //    'hash': '/oms/default/index',
        //    'page': 'modules/oms/default/index.tpl.html',
        //    'controller': 'modules/oms/default/omsIndex.ctl'
        //}
        'cms_edit_category': {
            'hash': '/cms/edit/category/:categoryId',
            'page': 'modules/cms/edit/category.tpl.html',
            'controller': 'modules/cms/edit/category.ctl'
            //'loadFiles': ['ui.select', 'modules/cms/edit/category.ctl.js']
        },
        'cms_edit_model': {
            'hash': '/cms/edit/model/:categoryId/:modelId',
            'page': '/modules/cms/edit/model.tpl.html',
            'controller': 'modules/cms/edit/model.ctl'
        },
        'cms_edit_product': {
            'hash': '/cms/edit/product/:categoryId/:modelId/:productId',
            'page': '/modules/cms/edit/product.tpl.html',
            'controller': 'modules/cms/edit/product.ctl'
        },
        'cms_edit_promotion': {
            'hash': '/cms/edit/promotion/:promotionType/:promotionId',
            'page': '/modules/cms/edit/promotion.tpl.html',
            'controller': 'modules/cms/edit/promotion.ctl'
        },
        'cms_quick_filter': {
            'hash': '/cms/common/index',
            'page': '/modules/cms/common/index.tpl.html',
            'controller': 'modules/cms/common/index.ctl'
        },
        //'cms_edit_model_withModelId': {
        //    'hash': '/cms/edit/model/:modelId',
        //    'page': '/modules/cms/edit/model.tpl.html',
        //    'controller': 'modules/cms/edit/model.ctl'
        //},
        //'cms_edit_product_withProductId': {
        //    'hash': '/cms/edit/product/:productId',
        //    'page': '/modules/cms/edit/product.tpl.html',
        //    'controller': 'modules/cms/edit/product.ctl'
        //},
        'cms_new_category_withCategoryId': {
            'hash': '/cms/new/category/:categoryId',
            'page': '/modules/cms/new/category.tpl.html',
            'controller': 'modules/cms/new/category.ctl'
        },
        'cms_new_category': {
            'hash': '/cms/new/category',
            'page': '/modules/cms/new/category.tpl.html',
            'controller': 'modules/cms/new/category.ctl'
        },
        'cms_new_model_withCategoryId': {
            'hash': '/cms/new/model/:categoryId',
            'page': '/modules/cms/new/model.tpl.html',
            'controller': 'modules/cms/new/model.ctl'
        },
        'cms_new_model': {
            'hash': '/cms/new/model',
            'page': '/modules/cms/new/model.tpl.html',
            'controller': 'modules/cms/new/model.ctl'
        },
        'cms_new_promotion': {
            'hash': '/cms/new/promotion',
            'page': '/modules/cms/new/promotion.tpl.html',
            'controller': 'modules/cms/new/promotion.ctl'
        },
        
        //add by lewis start
        'cms_masterPropValue_setting': {
            'hash': '/cms/setPropValue/masterProperty',
            'page': '/modules/cms/master/masterPropValueSetting/masterPropValueSetting.tpl.html',
            'controller': 'modules/cms/master/masterPropValueSetting/masterPropValueSetting.ctl'
        },
        //add by lewis end
        //add by lewis end
        //'cms_search_advance_us': {
        //    'hash': '/cms/search/advance/us',
        //    'page': '/modules/cms/search/advanceSearch.tpl.html',
        //    'controller': 'modules/cms/search/advanceSearch.ctl'
        //},
        //add by lewis end
        'cms_search_advance_cn': {
            'hash': '/cms/search/advance/cn',
            'page': '/modules/cms/search/advanceSearch.tpl.html',
            'controller': 'modules/cms/search/advanceSearch.ctl'
        },
        //add by lewis start 2015-09-01
        'cms_masterCategory_match': {
            'hash': '/cms/masterCategory/match',
            'page': '/modules/cms/master/masterCategoryMatch/masterCategoryMatch.tpl.html',
            'controller': 'modules/cms/master/masterCategoryMatch/masterCategoryMatch.ctl'
        },
        //add by lewis end 2015-09-01
        
        //add by lewis start 2015-09-10
        'cms_feedCommonProperty_setting': {
            'hash': '/cms/feedCommonProperty/setting',
            'page': '/modules/cms/master/feedDefaultPropSetting/feedDefaultPropSetting.tpl.html',
            'controller': 'modules/cms/master/feedDefaultPropSetting/feedDefaultPropSetting.ctl'
        },
        //add by lewis end 2015-09-01

        // 主数据属性匹配第三方品牌数据
        // add by jonas 2015-09-06 14:19:33
        'cms_feed_prop_match': {
            hash: "/cms/match/props/:categoryId",
            page: "modules/cms/master/matchProps/matchProps.tpl.html",
            controller: "modules/cms/master/matchProps/matchProps.ctl",
            path: function (categoryId) {
                return this.hash.replace(':categoryId', categoryId);
            }
        },

        'cms_dict': {
            hash: "/cms/dict",
            page: "modules/cms/master/dict/dict.list.tpl.html",
            controller: "modules/cms/master/dict/dict.list.ctl"
        },

        'cms_dict_item': {
            hash: "/cms/dict/item",
            page: "modules/cms/master/dict/dict.item.tpl.html",
            controller: "modules/cms/master/dict/dict.item.ctl"
        }
    });

    mainApp.config (["$routeProvider", "cmsRoute",
        function ($routeProvider, route) {

            var _ = require ("underscore");

            return _.each (route, function (value) {

                var angularAMD = require ("angularAMD");
                var commonUtil = require ('components/util/commonUtil');

                return $routeProvider.when (value.hash, angularAMD.route ({
                        templateUrl: value.page,
                        //controller: value.controller,
                        resolve: {
                            load: ["$q", "$rootScope", function ($q, $rootScope) {
                                return commonUtil.loadController ($q, $rootScope, value.controller);
                            }]
                            //load (value.loadFiles)
                        }
                    })
                )
                    ;
            });
            //
            ///**
            // * load the js file.
            // * @param srcs
            // * @param callback
            // * @returns {{deps: *[]}}
            // */
            //function load (srcs, callback) {
            //    return {
            //        deps: ['$ocLazyLoad', '$q',
            //            function ($ocLazyLoad, $q) {
            //                var deferred = $q.defer ();
            //                var promise = false;
            //                srcs = angular.isArray (srcs) ? srcs : srcs.split (/\s+/);
            //                if (!promise) {
            //                    promise = deferred.promise;
            //                }
            //                angular.forEach (srcs, function (src) {
            //                    console.log (src);
            //                    promise = promise.then (function () {
            //                        if (JQ_CONFIG[src]) {
            //                            return $ocLazyLoad.load (JQ_CONFIG[src]);
            //                        }
            //                        angular.forEach (MODULE_CONFIG, function (module) {
            //                            if (module.name == src) {
            //                                name = module.name;
            //                            } else {
            //                                name = src;
            //                            }
            //                        });
            //                        return $ocLazyLoad.load (name);
            //                    });
            //                });
            //                deferred.resolve ();
            //                return callback ? promise.then (function () {
            //                    return callback ();
            //                }) : promise;
            //            }]
            //    }
            //}
        }]);
})
;
