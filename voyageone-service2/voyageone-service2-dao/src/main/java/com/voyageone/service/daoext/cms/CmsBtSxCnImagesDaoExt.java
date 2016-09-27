package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtSxCnImagesModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtSxCnImagesDaoExt {

    // 检索等待上传的数据
    List<CmsBtSxCnImagesModel> selectListWaitingUpload(@Param("channelId") String channelId, @Param("cartId") int cartId);

    // 检索此code下正在使用的图片的数据
    List<CmsBtSxCnImagesModel> selectListByCodeWithUsing(@Param("channelId") String channelId, @Param("cartId") int cartId, @Param("code") String code, @Param("urlKey") String urlKey);

    int insertByList(@Param("list") List<CmsBtSxCnImagesModel> listData);

}
