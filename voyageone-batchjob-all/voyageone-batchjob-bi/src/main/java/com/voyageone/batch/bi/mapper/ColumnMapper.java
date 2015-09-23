package com.voyageone.batch.bi.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/6/9.
 * vm_column
 */
@Repository
public interface ColumnMapper {

    String select_record_vm_column(Map<String, String> mapParameter);

    int replace_vm_column(Map<String, String> mapParameter);

    int update_vm_column(Map<String, String> mapColumn);

    int insert_vm_column(Map<String, String> mapColumn);

    List<Map<String,String>> select_list_jd_vm_column(Map<String, String> mapParameter);
}
