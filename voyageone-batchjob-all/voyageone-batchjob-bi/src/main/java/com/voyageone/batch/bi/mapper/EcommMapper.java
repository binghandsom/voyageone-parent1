package com.voyageone.batch.bi.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/7/15.
 */
@Repository
public interface EcommMapper {

    String select_code_vm_ecomm(Map<String, Object> mapParameter);

    int replace_vm_ecomm(Map<String, String> mapParameter);

    int update_vm_ecomm(Map<String, String> mapColumn);

    int insert_vm_ecomm(Map<String, String> mapColumn);

    int vm_ecomm_count(Map<String, String> mapColumn);

    List<Map<String,String>> select_list_vm_ecomm(Map<String, String> mapParameter);
}
