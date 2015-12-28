/**
 * 条件操作枚举,对应 com.voyageone.cms.feed.Operation 枚举
 * @author Jonas, 2015-12-26 18:03:12
 */

define(function () {
    /**
     * 条件操作
     * @param name 描述名称
     * @param isSingle 是否是单值操作
     * @constructor
     */
    function Operation(name, isSingle) {
        this.name = name;
        this.isSingle = !!isSingle;
    }

    return {
        IS_NULL: new Operation("为空", true),
        IS_NOT_NULL: new Operation("不为空", true),
        EQUALS: new Operation("等于"),
        NOT_EQUALS: new Operation("不等于")
    }
});