/**
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 2.0.0
 */

angular.module('voyageone.angular.factories.notify', [])
  .factory('notify', function ($filter) {
    function notify(options) {

      if (!options) return;
      if (_.isString(options)) options = {message: options};
      if (!_.isObject(options)) return;

      var values;

      // 为 translate 的格式化提供支持，检测类型并转换
      if (_.isObject(options.message)) {

        values = options.message.values;
        options.message = options.message.id;
      }

      options.message = $filter('translate')(options.message, values);

      return $.notify(options.message, options);
    }

    notify.success = function (message) {
      return notify({message: message, status: "success"});
    };

    notify.warning = function (message) {
      return notify({message: message, status: "warning"});
    };

    notify.danger = function (message) {
      return notify({message: message, status: "danger"});
    };

    return notify;
  });