package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtSxCnProductSellercatModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtSxCnProductSellercatDaoExt {

    List<String> selectListWaitingUpload(@Param("channelId") String channelId);

    int insertByList(@Param("list") List<CmsBtSxCnProductSellercatModel> listData);

    int updateFlgByCatIds(@Param("channelId") String channelId, @Param("updFlg") String updFlg, @Param("modifier") String modifier, @Param("listCatId") List<String> listCatId);
}
