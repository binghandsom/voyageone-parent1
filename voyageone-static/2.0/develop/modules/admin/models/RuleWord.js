define(function () {

    /**
     * @param {string} type 对应 WordTypes
     * @constructor
     */
    function RuleWord(type) {
        /**
         * @type {string}
         */
        this.type = type;
        /**
         * @type {string}
         */
        this.value = null;
        /**
         * @type {boolean}
         */
        this.isUrl = false;
        /**
         * @type {Map}
         */
        this.extra = null;
    }

    return RuleWord;
});