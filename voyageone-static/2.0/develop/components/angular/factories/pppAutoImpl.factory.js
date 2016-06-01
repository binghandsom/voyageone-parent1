
    /**
     * @ngdoc
     * @factory
     * @name pppAutoImpl
     * @description
     * 根据定义自动生成方法实现. 注意! 依赖 ui-bootstrap
     */
    angular.module("voyageone.angular.factories.pppAutoImpl", []).factory("pppAutoImpl", function($q, $modal) {
        return function(declares, viewBaseUrl, jsBaseUrl) {
            if (!declares.$$$ || !declares.$$$.impl) declares.$$$ = {
                impl: declarePopupMethods(declares, viewBaseUrl, jsBaseUrl, "")
            };
            return declares.$$$.impl;
        };
        function declarePopupMethods(declares, viewBaseUrl, jsBaseUrl, popupBaseKey) {
            var impl = {};
            if (popupBaseKey) popupBaseKey += "/";
            _.each(declares, function(declare, parentDir) {
                if (!declare.popupKey) {
                    if (_.isObject(declare) || _.isArray(declare)) _.extend(impl, declarePopupMethods(declare, viewBaseUrl, jsBaseUrl, popupBaseKey + parentDir, $q, $modal));
                    return;
                }
                var options = _.clone(declare.options) || {};
                var pathBase = "/" + popupBaseKey;
                if (_.isString(parentDir)) pathBase += parentDir + "/";
                pathBase += declare.popupKey;
                options.templateUrl = viewBaseUrl + pathBase + ".tpl.html";
                options.controllerUrl = jsBaseUrl + pathBase + ".ctl";
                if (declare.controllerAs || declare.controller) options.controller = getControllerName(declare.popupKey);
                if (declare.controllerAs) options.controller += " as " + (_.isString(declare.controllerAs) ? declare.controllerAs : "ctrl");
                impl[declare.popupKey] = function(context) {
                    if (context) options.resolve = {
                        context: function() {
                            return context;
                        }
                    };
                    var defer = $q.defer();
                    require([ options.controllerUrl ], function() {
                        defer.resolve($modal.open(options).result);
                    });
                    return defer.promise;
                };
            });
            return impl;
        }
        function getControllerName(key) {
            return key.replace(/\.(\w)/g, function(m, m1) {
                return m1.toUpperCase();
            }).replace(/^(\w)/, function(m, m1) {
                return m1.toLowerCase();
            }) + "PopupController";
        }
    });
