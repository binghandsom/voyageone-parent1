package com.voyageone.service.bean.cms.producttop;

/**
 * Created by dell on 2016/11/29.
 */
public enum  EnumSort {
    created("created", "FeedSum"),
    quantity("quantity", "common.fields.quantity"),
    pPriceSaleEd("pPriceSaleEd", "platforms.p$.pPriceSaleEd"),
    codeSum7("codeSum7", "codeSum7.cartId$"),
    codeSum30("codeSum30", "codeSum30.cartId$"),
    codeSumYear("codeSumYear", "codeSumYear.cartId$");
    EnumSort(String name, String value) {
        this.name = name;
        this.value = value;
    }
    private String value;
    private String name;

//    创建时间        created
//    库存            common.fields.quantity
//    最终售价        platforms.p27.pPriceSaleEd
//7天销售排序     codeSum7.cartId27
//30天销售排序    codeSum30.cartId27
//    总销售降序      codeSumYear.cartId27
}
