package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
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
 */
@Service
public class SizeChartService extends BaseService {
    @Autowired
    private CmsBtSizeChartDao cmsBtSizeChartDao;
    @Autowired
    private MongoSequenceService commSequenceMongoService;
    /**
     * 按照填写的条件去数据库检索记录
     * @param channelId
     * @param sizeChartName
     * @param finishFlag
     * @param startTime
     * @param endTime
     * @param brandNameList
     * @param productTypeList
     * @param sizeTypeList
     * @return sizeChartList
     */
    public List<CmsBtSizeChartModel> getSizeChartSearch(
            String channelId,String sizeChartName,String finishFlag,String startTime,String endTime
            ,List<String> brandNameList,List<String>  productTypeList,List<String> sizeTypeList){
        //取得数据Model
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        //公司平台销售渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码名称
        cmsBtSizeChartModel.setSizeChartName(sizeChartName);
        //尺码标志
        cmsBtSizeChartModel.setFinish(finishFlag);
        //更新开始时间
        cmsBtSizeChartModel.setCreated(startTime);
        //更新结束时间
        cmsBtSizeChartModel.setModified(endTime);
        //产品品牌
        cmsBtSizeChartModel.setBrandName(brandNameList);
        //产品类型
        cmsBtSizeChartModel.setProductType(productTypeList);
        //产品性别
        cmsBtSizeChartModel.setSizeType(sizeTypeList);
        //尺码关系一览检索
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.selectSearchSizeChartInfo(cmsBtSizeChartModel);
        //返回数据的类型
        return  sizeChartList;
    }
    /**
     * 逻辑删除选中的记录
     * @param channelId
     * @param sizeChartId
     */
    public void sizeChartUpdate(String channelId,int sizeChartId){
        CmsBtSizeChartModel cmsBtSizeChartModel = getCmsBtSizeChartModel(sizeChartId);
        //标志位
        cmsBtSizeChartModel.setActive(0);
        //尺码关系一览检索
        cmsBtSizeChartDao.update(cmsBtSizeChartModel);
    }

    /**
     * 根据尺码关系一览编辑的数据插入数据库
     * @param channelId
     * @param userName
     * @param sizeChartName
     * @param brandNameList
     * @param productTypeList
     * @param sizeTypeList
     */
    public void insert(String channelId,String userName,String sizeChartName
            ,List<String> brandNameList,List<String>  productTypeList,List<String> sizeTypeList) {
        //取得数据Model
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        //更新者
        cmsBtSizeChartModel.setModifier(userName);
        //创建者
        cmsBtSizeChartModel.setCreater(userName);
        //店铺渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码表自增键取得
        Long sizeChartId =commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_SIZE_CHART_ID);
        //尺码自增键
        cmsBtSizeChartModel.setSizeChartId(Integer.parseInt(String.valueOf(sizeChartId)));
        //尺码名称
        cmsBtSizeChartModel.setSizeChartName(sizeChartName);
        //是否编辑
        cmsBtSizeChartModel.setFinish("0");
        //产品品牌
        if (brandNameList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setBrandName(lst);
        } else {
            cmsBtSizeChartModel.setBrandName(brandNameList);
        }
        //产品类型
        if(productTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setProductType(lst);
        } else {
            cmsBtSizeChartModel.setProductType(productTypeList);
        }
        //产品性别
        if(sizeTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
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
     * 尺码关系一览编辑详情检索画面
     * @param channelId
     * @param sizeChartId
     * @return sizeChartList
     */
    public List<CmsBtSizeChartModel> sizeChartDetailSearch(String channelId,int sizeChartId){
        //取得数据Model
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        //店铺渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码表自增键
        cmsBtSizeChartModel.setSizeChartId(sizeChartId);
        //尺码表自增键取得当前的记录
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.initSizeChartDetailSearch(cmsBtSizeChartModel);
        //返回数据的类型
        return  sizeChartList;
    }

    /**
     * 尺码关系一览编辑详情编辑画面
     * @param channelId
     * @param userName
     * @param sizeChartId
     * @param sizeChartName
     * @param finishFlag
     * @param brandNameList
     * @param productTypeList
     * @param sizeTypeList
     */
    public void sizeChartDetailUpdate(String channelId, String userName,int sizeChartId, String sizeChartName, String finishFlag
            , List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList){
        CmsBtSizeChartModel cmsBtSizeChartModel = getCmsBtSizeChartModel(sizeChartId);
        //更新者
        cmsBtSizeChartModel.setModifier(userName);
        //创建者
        cmsBtSizeChartModel.setCreater(userName);
        //店铺渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码自增键
        cmsBtSizeChartModel.setSizeChartId(sizeChartId);
        //尺码名称
        cmsBtSizeChartModel.setSizeChartName(sizeChartName);
        //是否编辑
        cmsBtSizeChartModel.setFinish(finishFlag);
        //产品品牌
        if (brandNameList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setBrandName(lst);
        } else {
            cmsBtSizeChartModel.setBrandName(brandNameList);
        }
        //产品类型
        if(productTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setProductType(lst);
        } else {
            cmsBtSizeChartModel.setProductType(productTypeList);
        }
        //产品性别
        if(sizeTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setSizeType(lst);
        } else {
            cmsBtSizeChartModel.setSizeType(sizeTypeList);
        }
        //跟据尺码关系一览编辑详情编辑的数据更新数据库
        cmsBtSizeChartDao.update(cmsBtSizeChartModel);
    }

    /**
     * 尺码关系一览编辑详情编辑画面(编辑尺码表)
     * @param channelId
     * @param userName
     * @param sizeChartId
     * @param sizeMapList
     */
    public void sizeChartDetailSizeMapSave(String channelId, String userName ,int sizeChartId,List<CmsBtSizeChartModelSizeMap> sizeMapList ){
        CmsBtSizeChartModel cmsBtSizeChartModel = getCmsBtSizeChartModel(sizeChartId);
        //更新者
        cmsBtSizeChartModel.setModifier(userName);
        //创建者
        cmsBtSizeChartModel.setCreater(userName);
        //SizeMap
        cmsBtSizeChartModel.setSizeMap(sizeMapList);
        //跟据尺码关系一览编辑详情编辑的数据更新数据库
        cmsBtSizeChartDao.update(cmsBtSizeChartModel);
    }
    /**
     * 根据sizeChartId取得sizeChartInfo
     * @param sizeChartId
     */
    public CmsBtSizeChartModel getCmsBtSizeChartModel(int sizeChartId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"sizeChartId\":" + sizeChartId + "},{\"active\":1}");
        return cmsBtSizeChartDao.selectOneWithQuery(queryObject);
    }
}
