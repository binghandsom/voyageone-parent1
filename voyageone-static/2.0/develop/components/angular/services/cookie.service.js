(function () {
    /**
     * @Date:    2015-11-16 20:30:37
     * @User:    Jonas
     * @Version: 0.2.0
     */
    angular.module("voyageone.angular.services.cookie", []).service("cookieService", CookieService);
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

    function CookieService($cookies) {
        this.$cookies = $cookies;
    }

    CookieService.prototype.get = function (key) {
        var result = this.$cookies.get(key);
        if (result === undefined || result === null)
            return '';
        return JSON.parse(result);
    };

    CookieService.prototype.set = function (key, value) {
        return this.$cookies.put(key, angular.toJson(value));
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
})();