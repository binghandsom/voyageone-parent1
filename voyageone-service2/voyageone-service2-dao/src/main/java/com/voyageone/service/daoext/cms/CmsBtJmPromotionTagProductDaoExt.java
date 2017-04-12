package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.businessmodel.CmsBtTag.TagCodeCountInfo;
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

    int deleteByTagIdJmPromotionProductId(@Param("jmPromotionProductId") int jmPromotionProductId,@Param("tagId") int tagId);

    int batchDeleteByCodes(@Param("codeList") List<String> codeList, @Param("jmPromotionId") int jmPromotionId);

    List<TagCodeCountInfo> selectListTagCodeCount(@Param("jmPromotionId") int jmPromotionId,@Param("parentTagId") int parentTagId, @Param("codeList") List<String> codeList);

    int updatePromotionTagName(@Param("tagName") String tagName, @Param("modifier") String modifier, @Param("cmsBtTagId") int tagId);
}
