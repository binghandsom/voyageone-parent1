package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtSizeChartDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModelSizeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjl on 2016/5/6.
 *
 * @author gjl
 * @version 2.1.0
 */
@Service
public class SizeChartService extends BaseService {

    public final static String VALUE_ALL = "All";

    @Autowired
    private CmsBtSizeChartDao cmsBtSizeChartDao;
    @Autowired
    private MongoSequenceService commSequenceMongoService;

    /**
     * 按照填写的条件去数据库检索记录
     */
    public List<CmsBtSizeChartModel> getSizeChartSearch(String channelId, String sizeChartName, String finishFlag
            , String startTime, String endTime, List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList, int curr, int size) {
        JomgoQuery queryObject = new JomgoQuery();
        //取得收索的条件
        queryObject.setQuery(getSearchQuery(channelId, sizeChartName, finishFlag, startTime, endTime, brandNameList, productTypeList, sizeTypeList));
        queryObject.setSort("{sizeChartId:-1}");
        queryObject.setLimit(size);
        queryObject.setSkip((curr - 1) * size);
        //返回数据的类型
        return cmsBtSizeChartDao.select(queryObject);
    }

    /**
     * 按照填写的条件去数据库检索记录
     *
     * @param queryObject JomgoQuery
     * @return List
     */
    public List<CmsBtSizeChartModel> getSizeCharts(JomgoQuery queryObject) {
        return cmsBtSizeChartDao.select(queryObject);
    }

    /**
     * 取得的总件数
     */
    public long getCount(String channelId, String sizeChartName, String finishFlag
            , String startTime, String endTime, List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        String parameter = getSearchQuery(channelId, sizeChartName, finishFlag, startTime, endTime, brandNameList, productTypeList, sizeTypeList);
        return cmsBtSizeChartDao.countByQuery(parameter);
    }

    /**
     * 取得收索的条件
     */
    private String getSearchQuery(String channelId, String sizeChartName, String finishFlag
            , String startTime, String endTime, List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        StringBuilder sbQuery = new StringBuilder();
        //sizeChartName
        if (!StringUtils.isEmpty(sizeChartName)) {
            sbQuery.append("\"sizeChartName\":{$regex:\"").append(sizeChartName).append("\"},");
        }
        //finish
        // modified by morse.lu 2016/06/14 start
        // 誰が作ったバグですね,修正修正
//        if(!StringUtils.isEmpty(String.valueOf(finishFlag))){
        if (!StringUtils.isEmpty(finishFlag)) {
            // modified by morse.lu 2016/06/14 end
            sbQuery.append(MongoUtils.splicingValue("finish", finishFlag));
            sbQuery.append(",");
        }
        // Update Time
        if (!StringUtils.isEmpty(startTime) || !StringUtils.isEmpty(endTime)) {
            sbQuery.append("\"modified\":{");
            // 获取Update Time Start
            if (!StringUtils.isEmpty(startTime)) {
                sbQuery.append(MongoUtils.splicingValue("$gte", startTime + " 00.00.00"));
            }
            // 获取Update Time End
            if (!StringUtils.isEmpty(endTime)) {
                if (!StringUtils.isEmpty(startTime)) {
                    sbQuery.append(",");
                }
                sbQuery.append(MongoUtils.splicingValue("$lte", endTime + " 23.59.59"));
            }
            sbQuery.append("},");
        }
        //BrandName
        if (!brandNameList.isEmpty()) {
            brandNameList.add(VALUE_ALL);
            sbQuery.append(MongoUtils.splicingValue("brandName", brandNameList.toArray(new String[brandNameList.size()])));
            sbQuery.append(",");
        }
        //ProductType
        if (!productTypeList.isEmpty()) {
            productTypeList.add(VALUE_ALL);
            sbQuery.append(MongoUtils.splicingValue("productType", productTypeList.toArray(new String[productTypeList.size()])));
            sbQuery.append(",");
        }
        //SizeType
        if (!sizeTypeList.isEmpty()) {
            sizeTypeList.add(VALUE_ALL);
            sbQuery.append(MongoUtils.splicingValue("sizeType", sizeTypeList.toArray(new String[sizeTypeList.size()])));
            sbQuery.append(",");
        }

        // channelId
        sbQuery.append(MongoUtils.splicingValue("channelId", channelId));
        sbQuery.append(",");

        // active
        sbQuery.append(MongoUtils.splicingValue("active", 1));

        return "{" + sbQuery.toString() + "}";
    }

    /**
     * 逻辑删除选中的记录
     */
    public void sizeChartUpdate(int sizeChartId, String userName, String channelId) {
        CmsBtSizeChartModel cmsBtSizeChartModel = getCmsBtSizeChartModel(sizeChartId, channelId);
        //更新者
        cmsBtSizeChartModel.setModifier(userName);
        //标志位
        cmsBtSizeChartModel.setActive(0);
        //尺码关系一览检索
        cmsBtSizeChartDao.update(cmsBtSizeChartModel);
    }

    /**
     * 根据尺码关系一览编辑的数据插入数据库
     */
    public void insert(String channelId, String userName, String sizeChartName
            , List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        //取得数据Model
        CmsBtSizeChartModel cmsBtSizeChartModel = new CmsBtSizeChartModel();
        //更新者
        cmsBtSizeChartModel.setModifier(userName);
        //创建者
        cmsBtSizeChartModel.setCreater(userName);
        //店铺渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码表自增键取得
        Long sizeChartId = commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_SIZE_CHART_ID);
        //尺码自增键
        cmsBtSizeChartModel.setSizeChartId(Integer.parseInt(String.valueOf(sizeChartId)));
        //尺码名称
        cmsBtSizeChartModel.setSizeChartName(sizeChartName);
        //是否编辑
        cmsBtSizeChartModel.setFinish("0");
        //产品品牌
        if (brandNameList.isEmpty()) {
            List<String> lst = new ArrayList<>();
            lst.add(VALUE_ALL);
            cmsBtSizeChartModel.setBrandName(lst);
        } else {
            cmsBtSizeChartModel.setBrandName(brandNameList);
        }
        //产品类型
        if (productTypeList.isEmpty()) {
            List<String> lst = new ArrayList<>();
            lst.add(VALUE_ALL);
            cmsBtSizeChartModel.setProductType(lst);
        } else {
            cmsBtSizeChartModel.setProductType(productTypeList);
        }
        //产品性别
        if (sizeTypeList.isEmpty()) {
            List<String> lst = new ArrayList<>();
            lst.add(VALUE_ALL);
            cmsBtSizeChartModel.setSizeType(lst);
        } else {
            cmsBtSizeChartModel.setSizeType(sizeTypeList);
        }
        //是否逻辑删除
        cmsBtSizeChartModel.setActive(1);
        //根据尺码关系一览编辑的数据插入数据库
        cmsBtSizeChartDao.insert(cmsBtSizeChartModel);
    }

    /**
     * 尺码关系一览编辑详情编辑画面
     */
    public void sizeChartDetailUpdate(String channelId, String userName, int sizeChartId, String sizeChartName, String finishFlag
            , List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        CmsBtSizeChartModel cmsBtSizeChartModel = getCmsBtSizeChartModel(sizeChartId, channelId);
        //更新者
        cmsBtSizeChartModel.setModifier(userName);
        //店铺渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码自增键
        cmsBtSizeChartModel.setSizeChartId(sizeChartId);
        //尺码名称
        cmsBtSizeChartModel.setSizeChartName(sizeChartName);
        //是否编辑
        cmsBtSizeChartModel.setFinish(finishFlag);
        //产品品牌
        if (brandNameList.isEmpty()) {
            List<String> lst = new ArrayList<>();
            lst.add(VALUE_ALL);
            cmsBtSizeChartModel.setBrandName(lst);
        } else {
            cmsBtSizeChartModel.setBrandName(brandNameList);
        }
        //产品类型
        if (productTypeList.isEmpty()) {
            List<String> lst = new ArrayList<>();
            lst.add(VALUE_ALL);
            cmsBtSizeChartModel.setProductType(lst);
        } else {
            cmsBtSizeChartModel.setProductType(productTypeList);
        }
        //产品性别
        if (sizeTypeList.isEmpty()) {
            List<String> lst = new ArrayList<>();
            lst.add(VALUE_ALL);
            cmsBtSizeChartModel.setSizeType(lst);
        } else {
            cmsBtSizeChartModel.setSizeType(sizeTypeList);
        }
        //跟据尺码关系一览编辑详情编辑的数据更新数据库
        cmsBtSizeChartDao.update(cmsBtSizeChartModel);
    }

    /**
     * 尺码关系一览编辑详情编辑画面(编辑尺码表)
     */
    public void sizeChartDetailSizeMapSave(String channelId, String userName, int sizeChartId, List<CmsBtSizeChartModelSizeMap> sizeMapList) {
        CmsBtSizeChartModel cmsBtSizeChartModel = getCmsBtSizeChartModel(sizeChartId, channelId);
        cmsBtSizeChartModel.setChannelId(channelId);
        //更新者
        cmsBtSizeChartModel.setModifier(userName);
        //SizeMap
        cmsBtSizeChartModel.setSizeMap(sizeMapList);
        //跟据尺码关系一览编辑详情编辑的数据更新数据库
        cmsBtSizeChartDao.update(cmsBtSizeChartModel);
    }

    /**
     * 根据sizeChartId取得sizeChartInfo
     */
    public CmsBtSizeChartModel getCmsBtSizeChartModel(int sizeChartId, String channelId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"sizeChartId\":" + sizeChartId + "},{\"channelId\":" + channelId + "},{\"active\":1}");
        return cmsBtSizeChartDao.selectOneWithQuery(queryObject);
    }
}
