/**
 * @Name:    route.js
 * @Date:    2015/3/22
 * @User:    Jonas
 * @Version: 1.0.0

 * IMS 系统
 */
(function () {

    define(["angularAMD", "components/app", "underscore"], function (angularAMD, mainApp) {

        return mainApp.constant("imsRoute", {
        	
            'ims_default_index': {
                'hash': '/ims/default/index', 
                'page': 'modules/ims/dataMaintenance/modifyMainPic.tpl.html', 
                'controller': 'modules/ims/dataMaintenance/modifyMainPicController'
            },
            
            'ims_beat_icon': {
                'hash': '/ims/beatIcon/index',
                'page': 'modules/ims/beatIcon/beatIcon.tpl.html',
                'controller': 'modules/ims/beatIcon/beatIcon.ctl'
            },
            
        }).config(["$routeProvider", "imsRoute", function ($routeProvider, imsRoute) {
            var commonUtil = require("components/util/commonUtil");

            // 循环 Route 配置，并加载
            return _.each(imsRoute, function (value) {
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
    });

}).call(this);