package com.voyageone.service.impl.cms;

import com.voyageone.common.util.MD5;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 京东平台产品上新用数据库相关服务
 *
 * @author desmond 2016/4/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PlatformProductUploadService extends BaseService {

    @Autowired
    private CmsBtSxWorkloadDaoExt sxWorkloadDao;

    @Autowired
    private CmsMtPlatformCategorySchemaDao platformCategorySchemaDao;

    /**
     * 获取平台ID对应的上新数据模型列表
     *
     * @param recordCount int     上新的任务表中一次数据抽出最大件数
     * @param channelId   String  渠道ID
     * @param cartId      int     平台ID
     * @return List<CmsBtSxWorkloadModel>    上新任务模型列表
     */
    public List<CmsBtSxWorkloadModel> getSxWorkloadWithChannelIdCartId(int recordCount, String channelId, int cartId) {
        return sxWorkloadDao.selectSxWorkloadModelWithChannelIdCartId(recordCount, channelId, cartId);
    }

    /**
     * 获取产品CategoryPath对应的平台类目schema列表
     *
     * @param categoryPath String  类目Path
     * @param cartId       int     平台ID
     * @return CmsMtPlatformCategorySchemaModel    平台schema模型
     */
    public CmsMtPlatformCategorySchemaModel getSxWorkloadWithChannelIdCartId(String categoryPath, int cartId) {
        return platformCategorySchemaDao.selectPlatformCatSchemaModel(MD5.getMD5(categoryPath), cartId);
    }

//    /**
//     * 获取产品CategoryPath对应的平台类目schema列表
//     *
//     * @param categoryPath String  类目paths
//     * @param cartId       int     平台ID
//     * @return List<CmsBtSxWorkloadModel>    上新任务模型列表
//     */
//    public CmsMtPlatformCategorySchemaModel getSxWorkloadWithChannelIdCartId(String categoryPath, int cartId) {
//        return platformCategorySchemaDao.selectPlatformCatSchemaModel(MD5.getMD5(categoryPath), cartId);
//    }
    /**
     * 获取平台ID对应的更新数据
     *
     * @param recordCount  int     更新的任务表中一次数据抽出最大件数
     * @param channelId    String  店铺ID
     * @param cartId       String  渠道ID
     * @return List<CmsBtSxWorkloadModel>    上新任务模型列表
     */
    public List<CmsBtSxWorkloadModel> getSxWorkloadWithChannelIdListCartIdList(int recordCount, String channelId, int cartId) {
        return sxWorkloadDao.selectSxWorkloadModelWithChannelIdListCartIdList(recordCount, channelId, cartId);
    }

}
