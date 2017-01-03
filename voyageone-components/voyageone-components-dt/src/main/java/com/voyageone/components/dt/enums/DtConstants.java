package com.voyageone.components.dt.enums;

public class DtConstants {

    // C_DT_RETURN_SUCCESS_OK
    public static final String C_DT_RETURN_SUCCESS_OK = "OK";

    // 调用分销平台的API Action
    public interface DtApiAction {
        String ON_SHELF_PRODUCT  = "/onShelfProduct";     // 分销上架(新增或更新)商品
        String OFF_SHELF_PRODUCT = "/offShelfProduct";    // 分销下架(删除)商品
    }
    
}
