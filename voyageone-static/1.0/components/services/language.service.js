/**
 * @Name:    language.service.js
 * @Date:    2015/4/29
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ('components/app');

    mainApp.constant ("languageType", {
        "en": "en",
        "zh": "zh"
    });
});