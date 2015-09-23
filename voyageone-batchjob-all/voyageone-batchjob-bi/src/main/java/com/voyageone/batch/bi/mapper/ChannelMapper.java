package com.voyageone.batch.bi.mapper;

import org.springframework.stereotype.Repository;

import com.voyageone.batch.bi.bean.modelbean.ChannelBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/7/15.
 */
@Repository
public interface ChannelMapper {

    String select_code_vm_channel(Map<String, Object> mapParameter);

    int replace_vm_channel(Map<String, String> mapParameter);

    int update_vm_channel(Map<String, String> mapColumn);

    int insert_vm_channel(Map<String, String> mapColumn);

    int vm_channel_count(Map<String, String> mapColumn);

    List<ChannelBean> select_list_vm_channel(Map<String, String> mapParameter);
}
