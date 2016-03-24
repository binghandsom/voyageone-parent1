(function() {
    /**
     * @Date:    2015-11-19 14:35:25
     * @User:    Jonas
     * @Version: 0.2.0
     */
    angular.module("voyageone.angular.services.translate", []).service("translateService", TranslateService);
    function TranslateService($translate) {
        this.$translate = $translate;
    }
    TranslateService.prototype = {
        languages: {
            en: "en",
            zh: "zh"
        },
        /**
         * set the web side language type.
         * @param key
         */
        setLanguage: function(language) {
            if (!_.contains(this.languages, language)) {
                language = this.getBrowserLanguage();
            }
            this.$translate.use(language);
            return language;
        },
        /**
         * get the browser language type.
         * @returns {string}
         */
        getBrowserLanguage: function() {
            var currentLang = navigator.language;
            if (!currentLang) currentLang = navigator.browserLanguage;
            return currentLang.substr(0, 2);
        }
    };
})();