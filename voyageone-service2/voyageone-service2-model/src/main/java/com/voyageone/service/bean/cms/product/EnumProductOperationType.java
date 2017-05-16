package com.voyageone.service.bean.cms.product;

/**
 * Created by dell on 2016/7/15.
 */
public enum EnumProductOperationType {
    Add(0, "新增"),
    ChangeMastProduct(1, "切换主商品"),
    Delisting(2, "单品下架"),
    BatchUpdate(3, "批量更新"),  // 高级检索 批量更新
    ProductApproved(4, "商品审批"),  // 高级检索 商品审批
    BatchSetCats(5, "批量设置店铺内分类"),  // 高级检索 批量设置店铺内分类
    BatchSetFreeTag(6, "批量设置自由标签"),  // 高级检索 批量设置自由标签
    WebEdit(7, "页面编辑"),                     // 产品编辑页
    BatchConfirmRetailPrice(8, "指导价变更批量确认"),                     // 高级检索 指导价变更批量确认
    DelistinGroup(9, "Group下线"),
    BatchRefreshRetailPrice(10, "重新计算价格"),                     // 高级检索 重新计算指导售价
    MoveCode(11, "移动Code"),                     // 移动Code
    MoveSku(12, "移动Sku"),                     // 移动Sku
    IntelligentPublish(13, "智能上新"), // 商品智能上新
    BatchSetPlatformAttr(14, "高级检索 - 修改平台共通属性"),  // 高级检索 批量设置自由标签
    CotegoryDeleted(15, "店铺分类被删除"),  // 高级检索 批量设置自由标签
    CreateNewCart(16, "新建一个Cart对应的platform"),
    changeMainCategory(17, "修改主类目"),
    UpdatePlatformLock(18, "触发平台级锁操作"),
    UpdateCommonLock(19, "触发共通锁操作"),
    BatchUpdatePlatformLock(20, "触发批量平台级锁操作"),
    SingleProdSetFreeTag(21, "单商品自由变迁设置");

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    EnumProductOperationType(int id, String name) {
        this.setId(id);
        this.setName(name);
    }

    public static EnumProductOperationType get(Object id) {
        int value = Integer.parseInt(id.toString());
        EnumProductOperationType[] list = EnumProductOperationType.values();
        for (EnumProductOperationType operationType : list) {
            if (operationType.getId() == value) {
                return operationType;
            }
        }
        return null;
    }

    public static String getNameById(Object id) {
        EnumProductOperationType operationType = get(id);
        if (operationType != null) return operationType.getName();
        return "";
    }
}
