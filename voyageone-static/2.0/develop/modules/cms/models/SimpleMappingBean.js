define([
    'modules/cms/enums/MappingTypes'
], function (MappingTypes) {

    /**
     * com.voyageone.cms.service.bean.SimpleMappingBean
     * @constructor
     */
    function SimpleMappingBean() {
        /**
         * 平台属性 ID
         * @type {string}
         */
        this.platformPropId = null;

        this.mappingType = MappingTypes.SIMPLE_MAPPING;
        /**
         * 表达式
         * @type {RuleExpression}
         */
        this.expression = expression;
    }

    return SimpleMappingBean;

});