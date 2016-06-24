/**
 * angular component head file。
 * 声明各个组件的父模块
 * 
 * create by Jonas on 2016-06-01 14:00:39
 */

// 各级子模块, 子系统可以单独引入

angular.module('vo.controllers', []);

angular.module('vo.directives', []);

angular.module('vo.factories', []);

angular.module('vo.services', []);

// 总模块, 供子系统一次性引入

angular.module('vo.ng', [
    'vo.controllers',
    'vo.directives',
    'vo.factories',
    'vo.services'
]);