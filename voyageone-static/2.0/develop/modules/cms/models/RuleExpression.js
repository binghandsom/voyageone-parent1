define(function () {

    /**
     * com.voyageone.ims.rule_expression.RuleExpression
     * @constructor
     */
    function RuleExpression() {
        /**
         * @type {Array.<TextWord|DictWord|MasterWord|FeedCnWord|FeedOrgWord|SkuWord>}
         */
        this.ruleWordList = null;
    }

    return RuleExpression;

});