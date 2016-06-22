/**
 * @description
 *
 * 自动创建基于地址定义的数据访问 service.
 * 传入的定义必须是 {object}, 并且至少有 root 属性
 *
 * @User: Jonas
 * @Date: 2015-12-10 19:32:37
 * @Version: 2.0.0
 */
//define(function () {
     angular.module("voyageone.angular.vresources", []).provider("$vresources", function ($provide) {
        /**
         * @description 构建请求地址的 url
         * @param {string} root 请求的根路径
         * @param {string} action 请求的名称
         * @returns {string}
         */
        function getActionUrl(root, action) {
            return root + (root.lastIndexOf("/") === root.length - 1 ? "" : "/") + action;
        }

        /**
         * 闭包声明一个数据访问的 Service
         * @param {string} name Service 的名称
         * @param {object} actions 方法和地址定义
         * @param {AjaxService} ajaxService 封装的 ajax 发送服务
         */
        function closureDataService(name, actions, ajaxService) {
            /**
             * @description 与 actions 一同闭包的构造函数
             * @constructor
             */
            function DataResource() {
                if (!actions) {
                    return;
                }
                if (typeof actions !== "object") {
                    console.log("Failed to new DataResource: [" + actions + "] is not a object");
                    return;
                }
                if (!actions.root) {
                    console.log("Failed to new DataResource: no root prop" + (JSON && JSON.stringify ? ": " + JSON.stringify(actions) : ""));
                    return;
                }
                // 遍历配置
                for (var name in actions) {
                    // 不对 root 进行创建
                    if (name === "root") continue;
                    // 额外的检查
                    if (actions.hasOwnProperty(name)) {
                        // 闭包创建函数
                        this[name] = function (actionUrl) {
                            return function (data) {
                                return ajaxService.post(actionUrl, data);
                            };
                        }(getActionUrl(actions.root, actions[name]));
                    }
                }
            }

            $provide.service(name, DataResource);
        }

        this.$get = function (ajaxService) {
            return {
                register: function (name, actions) {
                    if (!actions) return;
                    if (typeof actions !== "object") return;
                    // 如果有 root 这个属性,就创建 service
                    if (actions.root) {
                        closureDataService(name, actions, ajaxService);
                        return;
                    }
                    // 否则继续访问子属性
                    for (var childName in actions) {
                        // 额外的检查
                        if (actions.hasOwnProperty(childName)) {
                            this.register(childName, actions[childName]);
                        }
                    }
                }
            };
        };
    }).run(function ($vresources, $actions) {
        $vresources.register(null, $actions);
    });
//});
