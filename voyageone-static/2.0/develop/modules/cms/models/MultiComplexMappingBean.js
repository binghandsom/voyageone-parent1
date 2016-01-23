define([
    'modules/cms/enums/MappingTypes'
], function (MappingTypes) {

    /**
     * 构造一个和 Java 端 com.voyageone.cms.service.bean.ComplexMappingBean 一样的实例
     * @constructor
     * @extends {MappingBean}
     */
    function ComplexMappingBean() {
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
         * @type {MultiComplexCustomMappingValue[]}
         */
        this.values = null;
    }

    ComplexMappingBean.MultiComplexCustomMappingValue = function() {
        /**
         * 子匹配
         * @type {MappingBean[]}
         */
        this.subMappings = null;
    };

    return ComplexMappingBean;

});