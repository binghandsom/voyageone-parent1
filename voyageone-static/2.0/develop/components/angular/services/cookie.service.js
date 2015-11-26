/**
 * @Date:    2015-11-16 20:30:37
 * @User:    Jonas
 * @Version: 0.2.0
 */

angular.module('voyageone.angular.services.cookie', [])
  .service('cookieService', CookieService);

var keys = {
  language: 'voyageone.user.language',
  company: 'voyageone.user.company',
  channel: 'voyageone.user.channel'
};

function gentProps(key) {
  return function(val) {
    if (arguments.length === 1) {
      return this.set(key, val);
    } else if (arguments.length > 1) {
      return this.set(key, arguments);
    }
    return this.get(key);
  };
}

function CookieService($cookieStore) {
  this.$cookieStore = $cookieStore;
  // init
  this.set(this.keys.language, '');
  this.set(this.keys.company, '');
  this.set(this.keys.channel, '');
}

CookieService.prototype.get = function(key) {
  return this.$cookieStore.get(key);
};

CookieService.prototype.set = function(key, value) {
  return this.$cookieStore.put(key, value);
};

CookieService.prototype.language = gentProps(keys.language);

CookieService.prototype.company = gentProps(keys.company);

CookieService.prototype.channel = gentProps(keys.channel);
