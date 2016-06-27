/**
 * @Date:    2015-11-16 20:30:37
 * @User:    Jonas
 * @Version: 0.2.0
 */
angular.module("vo.services").service("cookieService", CookieService);

var keys = {
    language: "voyageone.user.language",
    company: "voyageone.user.company",
    channel: "voyageone.user.channel",
    application: "voyageone.user.application"
};

function makeProps(key) {
    return function (val) {
        if (arguments.length === 1) {
            return this.set(key, val);
        } else if (arguments.length > 1) {
            return this.set(key, arguments);
        }
        return this.get(key);
    };
}

/**
 * cookie 模型, 用来包装传入的数据
 * @param key
 * @param value
 */
function Cookie(key, value) {
    this.key = key;
    this.value = value;
}

function CookieService($cookies) {
    this.$cookies = $cookies;
}

CookieService.prototype.get = function (key) {
    var result = this.$cookies.get(key);
    if (result === undefined || result === null)
        return '';

    // 为了兼容老数据
    // 不是以 { 起始的认为不是 json, 直接返回
    if (result.indexOf('{') !== 0)
        return result;

    // 否则转换输出
    var item = JSON.parse(result);
    return item.value;
};

CookieService.prototype.set = function (key, value) {
    // 统一使用 Cookie 类包装后存储
    var item = new Cookie(key, value);
    return this.$cookies.put(key, angular.toJson(item));
};

CookieService.prototype.removeAll = function () {
    this.$cookies.remove(keys.language);
    this.$cookies.remove(keys.company);
    this.$cookies.remove(keys.channel);
    this.$cookies.remove(keys.application);
};

CookieService.prototype.language = makeProps(keys.language);

CookieService.prototype.company = makeProps(keys.company);

CookieService.prototype.channel = makeProps(keys.channel);

CookieService.prototype.application = makeProps(keys.application);
