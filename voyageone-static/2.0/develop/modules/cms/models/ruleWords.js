define([
    'modules/cms/enums/WordTypes'
], function (WordTypes) {

    function TextWord() {
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
        this.wordType = WordTypes.DICT;
        /**
         * @type {string}
         */
        this.name = null;
        /**
         * @type {boolean}
         */
        this.isUrl = false;
        /**
         * @type {RuleExpression}
         */
        this.expression = null;
    }

    function MasterWord() {
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
        this.wordType = WordTypes.SKU;
        /**
         * @type {string}
         */
        this.value = null;
    }

    return {
        TextWord: TextWord,
        DictWord: DictWord,
        MasterWord: MasterWord,
        FeedCnWord: FeedCnWord,
        FeedOrgWord: FeedOrgWord,
        SkuWord: SkuWord
    };
});