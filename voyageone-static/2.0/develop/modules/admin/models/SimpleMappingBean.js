define([
    'modules/cms/enums/MappingTypes'
], function (MappingTypes) {

    /**
     * @name MappingBean
     * @class
     * @description
     * 各类型 MappingBean 的父类, 位于 com.voyageone.cms.service.bean.MappingBean
     */

    /**
     * com.voyageone.cms.service.bean.SimpleMappingBean
     * @constructor
     * @extends {MappingBean}
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
        this.expression = null;
    }

    return SimpleMappingBean;

});