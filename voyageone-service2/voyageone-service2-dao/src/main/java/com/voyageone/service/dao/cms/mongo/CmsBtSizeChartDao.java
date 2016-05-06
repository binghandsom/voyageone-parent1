package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjl on 2016/5/5.
 */
@Repository
public class CmsBtSizeChartDao extends BaseMongoChannelDao<CmsBtSizeChartModel> {
    /**
     * 尺码关系一览初始化画面
     * @param channelId
     * @return List<CmsBtSizeChartModel>
     */
    public List<CmsBtSizeChartModel> selectInitSizeChartInfo(String channelId) {
        return selectAll(channelId);
    }

    /**
     * 尺码关系一览检索画面
     * @param channelId
     * @param cmsBtSizeChartModel
     * @return List<CmsBtSizeChartModel>
     */
    public List<CmsBtSizeChartModel> selectSearchSizeChartInfo(String channelId, CmsBtSizeChartModel cmsBtSizeChartModel) {

        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("{");
        //sizeChartName
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getSizeChartName())){
            sbQuery.append("sizeChartName:"+cmsBtSizeChartModel.getSizeChartName()+",");
        }
        //finish
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getFinish())){
            sbQuery.append("finish:"+cmsBtSizeChartModel.getFinish()+",");
        }
        //UpdateTimeStr
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getCreated())){
            sbQuery.append("modified:"+cmsBtSizeChartModel.getCreated() + ",");
        }
        //UpdateTimeEnd
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getModified())){
            sbQuery.append("modified:"+cmsBtSizeChartModel.getModified()+",");
        }
        //BrandName
        if(cmsBtSizeChartModel.getBrandName().size()>0){
            sbQuery.append(MongoUtils.splicingValue("brandName", cmsBtSizeChartModel.getBrandName()));
        }
        //ProductType
        if(cmsBtSizeChartModel.getProductType().size()>0){
            sbQuery.append(MongoUtils.splicingValue("productType", cmsBtSizeChartModel.getProductType()));
        }
        //SizeType
        if(cmsBtSizeChartModel.getSizeType().size()>0){
            sbQuery.append(MongoUtils.splicingValue("sizeType", cmsBtSizeChartModel.getSizeType()));
        }
        sbQuery.append("}");
        return select(sbQuery.toString(),channelId);
    }

    /**
     *
     * @param channelId
     * @param cmsBtSizeChartModel
     * @return List<CmsBtSizeChartModel>
     */
    public List<CmsBtSizeChartModel> sizeChartDelete(String channelId, CmsBtSizeChartModel cmsBtSizeChartModel) {
        return selectAll(channelId);
    }
}
