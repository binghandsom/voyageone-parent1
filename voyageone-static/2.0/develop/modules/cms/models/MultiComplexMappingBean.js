define([
    'modules/cms/enums/MappingTypes'
], function (MappingTypes) {

    /**
     * 构造一个和 Java 端 com.voyageone.cms.service.bean.MultiComplexMappingBean 一样的实例
     * @constructor
     * @extends {MappingBean}
     */
    function MultiComplexMappingBean() {
        /**
         * 平台属性 ID
         * @type {string}
         */
        this.platformPropId = null;
        /**
         * 当前匹配类型, 对应 MappingTypes 的 MULTI_COMPLEX_MAPPING
         * @type {string}
         */
        this.mappingType = MappingTypes.MULTI_COMPLEX_MAPPING;
        /**
         * 实际匹配
         * @type {MultiComplexMappingBean.MultiComplexCustomMappingValue[]}
         */
        this.values = null;
    }

    /**
     * @constructor
     */
    MultiComplexMappingBean.MultiComplexCustomMappingValue = function() {
        /**
         * 子匹配
         * @type {MappingBean[]}
         */
        this.subMappings = null;
    };

    return MultiComplexMappingBean;

});