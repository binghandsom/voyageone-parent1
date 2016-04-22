package com.voyageone.service.daoext.synship;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/22 11:44
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Repository
public interface SynshipComMtValueChannelDao {

     /**
     * 注意,这里读取的是synship中的相同表不是cms2库中的
     *
     * @return
     */
     @Select("select name from Synship.com_mt_value_channel\n" +
             "        where value=#{value} and lang_id=#{lang} and type_id=#{typeId} and channel_id=#{channelId}")
     String selectName(@Param("value") String value, @Param("typeId") Integer typeId, @Param("lang") String lang, @Param("channelId") String channelId);


}
