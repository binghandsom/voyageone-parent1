/**
 * 条件操作枚举,对应 com.voyageone.cms.feed.Operation 枚举
 * @author Jonas, 2015-12-26 18:03:12
 */

define(function () {
    /**
     * 条件操作
     * @param name 名称(必须同步Java,否则无法构造)
     * @param desc 描述名称
     * @param isSingle 是否是单值操作
     * @constructor
     */
    function Operation(name, desc, isSingle) {
        this.name = name;
        this.desc = desc;
        this.isSingle = !!isSingle;
    }

    return {
        IS_NULL: new Operation("IS_NULL", "为空", true),
        IS_NOT_NULL: new Operation("IS_NOT_NULL", "不为空", true),
        EQUALS: new Operation("EQUALS", "等于"),
        NOT_EQUALS: new Operation("NOT_EQUALS", "不等于")
    };
});