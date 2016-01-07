/**
 * 字段类型
 */

define(function () {
    /**
     * @typedef {string} FieldType 枚举实例的类型
     */
    return {
        /**
         * @description
         *
         * 文本输入
         * @type {FieldType}
         */
        "input": "INPUT",
        /**
         * @description
         *
         * 多行文本输入
         * @type {FieldType}
         */
        "multiInput": "MULTIINPUT",
        /**
         * @description
         *
         * 单选
         * @type {FieldType}
         */
        "singleCheck": "SINGLECHECK",
        /**
         * @description
         *
         * 多选
         * @type {FieldType}
         */
        "multiCheck": "MULTICHECK",
        /**
         * @description
         *
         * 组合属性(属性与值一对一)
         * @type {FieldType}
         */
        "complex": "COMPLEX",
        /**
         * @description
         *
         * 复合属性(属性与值一对多)
         * @type {FieldType}
         */
        "multiComplex": "MULTICOMPLEX",
        /**
         * @description
         *
         * 文本
         * @type {FieldType}
         */
        "label": "LABEL"
    };
});