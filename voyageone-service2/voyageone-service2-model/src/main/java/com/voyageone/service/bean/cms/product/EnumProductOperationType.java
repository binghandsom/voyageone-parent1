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
    BatchConfirmRetailPrice(8, "指导价变更批量确认"),                     // 指导价变更批量确认
    DelistinGroup(9, "Group下线");

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
