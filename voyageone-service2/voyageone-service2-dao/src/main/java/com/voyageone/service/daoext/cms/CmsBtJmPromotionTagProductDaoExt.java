package com.voyageone.service.daoext.cms;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dell on 2016/6/27.
 */
@Repository
public interface CmsBtJmPromotionTagProductDaoExt {
    int deleteByCmsBtJmPromotionProductId(int cmsBtJmPromotionProductId);

    int batchDeleteTag(@Param("listPromotionProductId") List<Integer> listPromotionProductId);


    int deleteByTagId(int tagId);

    int deleteByTagIdJmPromotionProductId(@Param("tagId") int tagId, @Param("jmPromotionProductId") int jmPromotionProductId);

    int batchDeleteByCodes(@Param("codeList") List<String> codeList, @Param("jmPromotionId") int jmPromotionId);
}
