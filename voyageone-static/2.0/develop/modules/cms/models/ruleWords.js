define([
    'modules/cms/enums/WordTypes'
], function (WordTypes) {

    function TextWord() {
        /**
         * @readonly
         * @type {string}
         */
        this.wordType = WordTypes.TEXT;
        /**
         * @type {string}
         */
        this.value = null;
        /**
         * @type {boolean}
         */
        this.isUrl = false;
    }

    function DictWord() {
        /**
         * @readonly
         * @type {string}
         */
        this.wordType = WordTypes.DICT;
        /**
         * Java 端为 name
         * @type {string}
         */
        this.value = null;
    }

    function MasterWord() {
        /**
         * @readonly
         * @type {string}
         */
        this.wordType = WordTypes.MASTER;
        /**
         * @type {string}
         */
        this.value = null;
        /**
         * @type {Map}
         */
        this.extra = null;
    }

    function FeedCnWord() {
        /**
         * @readonly
         * @type {string}
         */
        this.wordType = WordTypes.FEED_CN;
        /**
         * @type {string}
         */
        this.value = null;
        /**
         * @type {Map}
         */
        this.extra = null;
    }

    function FeedOrgWord() {
        /**
         * @readonly
         * @type {string}
         */
        this.wordType = WordTypes.FEED_ORG;
        /**
         * @type {string}
         */
        this.value = null;
        /**
         * @type {Map}
         */
        this.extra = null;
    }

    function SkuWord() {
        /**
         * @readonly
         * @type {string}
         */
        this.wordType = WordTypes.SKU;
        /**
         * @type {string}
         */
        this.value = null;
    }

    /**
     * @class RuleWord
     */

    return {
        TextWord: TextWord,
        DictWord: DictWord,
        MasterWord: MasterWord,
        FeedCnWord: FeedCnWord,
        FeedOrgWord: FeedOrgWord,
        SkuWord: SkuWord
    };
});