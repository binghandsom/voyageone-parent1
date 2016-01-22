define([
    'modules/cms/enums/MappingTypes'
], function (MappingTypes) {

    /**
     * 构造一个和 Java 端 com.voyageone.cms.service.bean.ComplexMappingBean 一样的实例
     * @constructor
     * @extends {MappingBean}
     * @param {string} platformPropId
     * @param {string} masterPropId
     * @param {MappingBean[]} subMapping
     */
    function ComplexMappingBean(platformPropId, masterPropId, subMapping) {
        /**
         * 平台属性 ID
         * @type {string}
         */
        this.platformPropId = platformPropId;
        /**
         * 当前匹配类型, 对应 MappingTypes 的 SIMPLE
         * @type {string}
         */
        this.mappingType = MappingTypes.COMPLEX_MAPPING;
        /**
         * 主数据属性 ID
         * @type {string}
         */
        this.masterPropId = masterPropId;
        /**
         * 继承 MappingBean 的 Mapping 关系集合
         * @type {MappingBean[]}
         */
        this.subMapping = subMapping;
    }

    return ComplexMappingBean;

});