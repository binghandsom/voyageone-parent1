package com.voyageone.service.impl.cms;

import com.voyageone.common.util.MD5;
import com.voyageone.service.dao.cms.CmsBtUsWorkloadDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.daoext.cms.CmsBtUsWorkloadDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsBtUsWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CmsBtUsWorkloadDao usWorkloadDao;
    @Autowired
    private CmsBtUsWorkloadDaoExt usWorkloadDaoExt;

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

    /**
     * 获取美国平台对应的更新数据
     *
     * @param recordCount  int     更新的任务表中一次数据抽出最大件数
     * @param channelId    String  渠道ID
     * @param cartId       String  店铺ID
     * @param publishTime String  上新时间
     * @return List<CmsBtSxWorkloadModel>    上新任务模型列表
     */
    public List<CmsBtUsWorkloadModel> getListUsWorkload(int recordCount, String channelId, int cartId, Date publishTime) {
        return usWorkloadDaoExt.selectListUsWorkload(channelId, cartId, publishTime, recordCount);
    }

    /**
     * 保存CmsBtUsWorkloadModel，表里code有更新，没有insert
     *
     * @param channelId 渠道ID
     * @param cartId 店铺ID
     * @param code productCode
     * @param publishTime 上新时间,传null的话，insert时用当前时间，update时不更新
     * @param status 状态(常量定义CmsConstants.SxWorkloadPublishStatusNum) 0:等待上新 1:上新成功 2:上新失败
     * @param modifier 更新者
     */
    public int saveCmsBtUsWorkloadModel(String channelId, int cartId, String code, Date publishTime, int status, String modifier) {
        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("channelId", channelId);
        searchParam.put("cartId", cartId);
        searchParam.put("code", code);
        CmsBtUsWorkloadModel usWorkloadModel = usWorkloadDao.selectOne(searchParam);
        if (usWorkloadModel == null) {
            // insert
            usWorkloadModel = new CmsBtUsWorkloadModel();
            usWorkloadModel.setChannelId(channelId);
            usWorkloadModel.setCartId(cartId);
            usWorkloadModel.setCode(code);
            usWorkloadModel.setPublishStatus(status);
            usWorkloadModel.setPublishTime(publishTime == null ? new Date() : publishTime);
            usWorkloadModel.setCreater(modifier);
            return usWorkloadDao.insert(usWorkloadModel);
        } else {
            // update
            usWorkloadModel.setPublishStatus(status);
            if (publishTime != null) usWorkloadModel.setPublishTime(publishTime);
            usWorkloadModel.setModifier(modifier);
            usWorkloadModel.setModified(new Date());
            return usWorkloadDao.update(usWorkloadModel);
        }

    }

}
