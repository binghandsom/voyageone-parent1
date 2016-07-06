package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/7/6.
 */
@Repository
public interface CmsBtDataAmountDaoExt {

  List<CmsBtDataAmountModel> selectByListDataAmountTypeId(@Param("channelId") String channelId, @Param("list")List<Integer> listDataAmountTypeId);
}
