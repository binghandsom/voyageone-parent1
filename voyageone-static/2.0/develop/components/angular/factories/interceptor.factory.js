/**
 * @Date:    2015-11-16 20:51:05
 * @User:    Jonas
 * @Version: 0.2.0
 */

angular.module('voyageone.angular.factories.interceptor', [])
  .factory('interceptorFactory', InterceptorFactory);

function InterceptorFactory() {
  return {
    request: function(config) {
      return config;
    },
    response: function(res) {
      return res;
    },
    requestError: function(config) {
      return config;
    },
    responseError: function(res) {}
  };
}
