(function() {
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
    function CookieService($cookies) {
        this.$cookies = $cookies;
    }
    CookieService.prototype.get = function(key) {
        var result = this.$cookies.get(key);
        return result == undefined || result == null ? "" : this.$cookies.get(key);
    };
    CookieService.prototype.set = function(key, value) {
        return this.$cookies.put(key, value);
    };
    CookieService.prototype.language = gentProps(keys.language);
    CookieService.prototype.company = gentProps(keys.company);
    CookieService.prototype.channel = gentProps(keys.channel);
    CookieService.prototype.application = gentProps(keys.application);
    CookieService.prototype.removeAll = function() {
        this.$cookies.remove(keys.language);
        this.$cookies.remove(keys.company);
        this.$cookies.remove(keys.channel);
        this.$cookies.remove(keys.application);
    };
})();