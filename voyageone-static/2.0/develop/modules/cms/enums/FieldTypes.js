/**
 * 字段类型
 */

define(function () {
    return {
        /**
         * 文本输入
         * @type {string}
         */
        "input": "INPUT",
        /**
         * 多行文本输入
         * @type {string}
         */
        "multiInput": "MULTIINPUT",
        /**
         * 单选
         * @type {string}
         */
        "singleCheck": "SINGLECHECK",
        /**
         * 多选
         * @type {string}
         */
        "multiCheck": "MULTICHECK",
        /**
         * 组合属性(属性与值一对一)
         * @type {string}
         */
        "complex": "COMPLEX",
        /**
         * 复合属性(属性与值一对多)
         * @type {string}
         */
        "multiComplex": "MULTICOMPLEX",
        /**
         * 文本
         * @type {string}
         */
        "label": "LABEL"
    };
});