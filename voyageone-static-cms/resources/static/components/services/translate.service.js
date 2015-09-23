/**
 * @Name:    translateService.js
 * @Date:    2015/1/31
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ('components/app');
    require ('components/services/language.service');
    require ('components/languages/translate.core.zh');
    require ('components/languages/translate.core.en');
    //require ('components/languages/translate.oms.zh');
    //require ('components/languages/translate.oms.en');
    //require ('components/languages/translate.wms.zh');
    //require ('components/languages/translate.wms.en');
    require ('components/languages/translate.cms.zh');
    require ('components/languages/translate.cms.en');

    // define translateService to set the browser language.
    mainApp.service ('translateService', ['$translate', 'languageType',
        function ($translate, languageType) {

            /**
             * set the web side language type.
             * @param key
             */
            this.setLanguage = function (key) {

                var _ = require ('underscore');
                if (!_.contains (languageType, key)) {
                    key = getBrowserLanguage ();
                }
                $translate.use (key);
                return key;
            };

            /**
             * get the browser language type.
             * @returns {string}
             */
            function getBrowserLanguage () {
                var currentLang = navigator.language;
                if (!currentLang)
                    currentLang = navigator.browserLanguage;
                return currentLang.substr (0, 2);
            }
        }]);
});