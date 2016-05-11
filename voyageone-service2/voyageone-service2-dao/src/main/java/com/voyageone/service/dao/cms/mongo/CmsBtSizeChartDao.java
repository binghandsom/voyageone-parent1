package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjl on 2016/5/5.
 */
@Repository
public class CmsBtSizeChartDao extends BaseMongoDao<CmsBtSizeChartModel> {
    /**
     * 尺码关系一览检索画面
     * @param cmsBtSizeChartModel
     * @return List<CmsBtSizeChartModel>
     */
    public List<CmsBtSizeChartModel> selectSearchSizeChartInfo(CmsBtSizeChartModel cmsBtSizeChartModel) {
        StringBuilder sbQuery = new StringBuilder();
        //sizeChartName
        if(!StringUtils.isEmpty(cmsBtSizeChartModel.getSizeChartName())){
            sbQuery.append("\"sizeChartName\":{$regex:\"").append(cmsBtSizeChartModel.getSizeChartName()).append("\"},");
        }
        //finish
        if(!StringUtils.isEmpty(String.valueOf(cmsBtSizeChartModel.getFinish()))){
            sbQuery.append(MongoUtils.splicingValue("finish", cmsBtSizeChartModel.getFinish()));
            sbQuery.append(",");
        }
        // Update Time
        if (!StringUtils.isEmpty(cmsBtSizeChartModel.getCreated()) || !StringUtils.isEmpty(cmsBtSizeChartModel.getModified())) {
            sbQuery.append("\"modified\":{" );
            // 获取Update Time Start
            if (!StringUtils.isEmpty(cmsBtSizeChartModel.getCreated())) {
                sbQuery.append(MongoUtils.splicingValue("$gte", cmsBtSizeChartModel.getCreated() + " 00.00.00"));
            }
            // 获取Update Time End
            if (!StringUtils.isEmpty(cmsBtSizeChartModel.getModified())) {
                if (!StringUtils.isEmpty(cmsBtSizeChartModel.getCreated())) {
                    sbQuery.append(",");
                }
                sbQuery.append(MongoUtils.splicingValue("$lte", cmsBtSizeChartModel.getModified() + " 23.59.59"));
            }
            sbQuery.append("},");
        }
        //BrandName
        List brandNameList = cmsBtSizeChartModel.getBrandName();
        if(brandNameList.size()>0){
            cmsBtSizeChartModel.getBrandName().add("All");
            sbQuery.append(MongoUtils.splicingValue("brandName", brandNameList.toArray(new String[cmsBtSizeChartModel.getBrandName().size()])));
            sbQuery.append(",");
        }
        //ProductType
        List productTypeList = cmsBtSizeChartModel.getProductType();
        if(productTypeList.size()>0){
            cmsBtSizeChartModel.getProductType().add("All");
            sbQuery.append(MongoUtils.splicingValue("productType", productTypeList.toArray(new String[cmsBtSizeChartModel.getProductType().size()])));
            sbQuery.append(",");
        }
        //SizeType
        List sizeTypeList = cmsBtSizeChartModel.getSizeType();
        if(sizeTypeList.size()>0){
            cmsBtSizeChartModel.getSizeType().add("All");
            sbQuery.append(MongoUtils.splicingValue("sizeType",  sizeTypeList.toArray(new String[cmsBtSizeChartModel.getSizeType().size()])));
            sbQuery.append(",");
        }
        sbQuery.append(MongoUtils.splicingValue("active", 1));
        return select("{" + sbQuery.toString() + "}");
    }
    /**
     * 尺码关系一览编辑检索画面
     * @return List<CmsBtSizeChartModel>
     */
    public List<CmsBtSizeChartModel> initSizeChartDetailSearch(CmsBtSizeChartModel cmsBtSizeChartModel) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append(MongoUtils.splicingValue("sizeChartId", cmsBtSizeChartModel.getSizeChartId()));
        sbQuery.append(",");
        sbQuery.append(MongoUtils.splicingValue("channelId", cmsBtSizeChartModel.getChannelId()));
        return select("{" + sbQuery.toString() + "}");
    }
}
