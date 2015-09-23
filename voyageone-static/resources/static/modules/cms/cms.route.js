/**
 * @Name:    route.js
 * @Date:    2015/3/22
 * @User:    Jonas
 * @Version: 1.0.0

 * WMS 系统
 */
(function () {

    define(["angularAMD", "components/app", "underscore"], function (angularAMD, mainApp) {

        mainApp.constant("cmsRoute", {
        	//james add
            'cms_edit_category': {
                'hash': '/cms/edit/category', 
                'page': 'modules/cms/edit/category.tpl.html', 
                'controller': 'modules/cms/edit/category.ctl'
            },
        	//eric add
            
        });
        
        mainApp.config(["$routeProvider", "cmsRoute", function ($routeProvider, cmsRoute) {
            var commonUtil = require("components/util/commonUtil");

            // 循环 Route 配置，并加载
            return _.each(cmsRoute, function (value) {
                return $routeProvider.when(value.hash, angularAMD.route({
                    templateUrl: value.page,
                    resolve: {
                        load: ["$q", "$rootScope", function ($q, $rootScope) {
                            return commonUtil.loadController($q, $rootScope, value.controller);
                        }]
                    }
                }));
            });
        }]);
        
        return mainApp;
    });

}).call(this);