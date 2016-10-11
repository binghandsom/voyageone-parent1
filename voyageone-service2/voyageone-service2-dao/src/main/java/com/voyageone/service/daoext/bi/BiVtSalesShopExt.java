package com.voyageone.service.daoext.bi;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface BiVtSalesShopExt {

    int insertWithList(Map<String, Object> mapColumnValue);

}
