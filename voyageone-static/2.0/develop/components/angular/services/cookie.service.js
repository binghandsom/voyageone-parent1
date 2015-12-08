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
  channel: 'voyageone.user.channel',
  menu: "voyageone.user.menu",
  name: "voyageone.user.name"
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
  this.set(keys.language, '');
  this.set(keys.company, '');
  this.set(keys.channel, '');
  this.set(keys.menu, '');
}

CookieService.prototype.get = function(key) {
  return this.$cookieStore.get(key);
};

CookieService.prototype.set = function(key, value) {
  return this.$cookieStore.put(key, value);
};

CookieService.prototype.language = gentProps(keys.language);

CookieService.prototype.company = gentProps(keys.company);

// todo: channel 是否需要的是一个对象(id,name),在menu画面中需要显示channel的名称(edward.lin)
CookieService.prototype.channel = gentProps(keys.channel);

CookieService.prototype.menu = gentProps(keys.menu);

CookieService.prototype.name = gentProps(keys.name);
