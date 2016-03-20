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
    function CookieService($cookieStore) {
        this.$cookieStore = $cookieStore;
    }
    CookieService.prototype.get = function(key) {
        var result = this.$cookieStore.get(key);
        return result == undefined || result == null ? "" : this.$cookieStore.get(key);
    };
    CookieService.prototype.set = function(key, value) {
        return this.$cookieStore.put(key, value);
    };
    CookieService.prototype.language = gentProps(keys.language);
    CookieService.prototype.company = gentProps(keys.company);
    CookieService.prototype.channel = gentProps(keys.channel);
    CookieService.prototype.application = gentProps(keys.application);
    CookieService.prototype.removeAll = function() {
        this.$cookieStore.remove(keys.language);
        this.$cookieStore.remove(keys.company);
        this.$cookieStore.remove(keys.channel);
        this.$cookieStore.remove(keys.application);
    };
})();