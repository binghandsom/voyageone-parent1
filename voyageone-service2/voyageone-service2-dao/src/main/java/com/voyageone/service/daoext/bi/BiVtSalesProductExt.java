package com.voyageone.service.daoext.bi;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BiVtSalesProductExt {

    int insertWithList(Map<String, Object> mapColumnValue);

    int deleteWithList(Map<String, Object> mapColumnValue);

    /*
     * 取得指定时间范围内的产品的bi信息 (目前只取：浏览量 访客数 加购件数 收藏人数)
     */
    List<Map<String, Object>> selectList(Map<String, Object> params);

}
