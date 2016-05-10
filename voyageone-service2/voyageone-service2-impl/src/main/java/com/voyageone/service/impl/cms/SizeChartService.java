package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsBtSizeChartDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
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

    public List<CmsBtSizeChartModel> getSizeChartSearch(
            String channelId,String sizeChartName,String finishFlag,String startTime,String endTime
            ,List<String> brandNameList,List<String>  productTypeList,List<String> sizeTypeList){
        //取得数据bean
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
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.selectSearchSizeChartInfo(channelId, cmsBtSizeChartModel);
        return  sizeChartList;
    }
    public void sizeChartUpdate(String channelId,int sizeChartId){
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        //公司平台销售渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //自增主键
        cmsBtSizeChartModel.setSizeChartId(sizeChartId);
        //标志位
        cmsBtSizeChartModel.setActive(Integer.valueOf("0"));
        //尺码关系一览检索
        cmsBtSizeChartDao.sizeChartUpdate(channelId,cmsBtSizeChartModel);
    }
    public void insert(String channelId,String userName,String sizeChartName
            ,List<String> brandNameList,List<String>  productTypeList,List<String> sizeTypeList) {
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        cmsBtSizeChartModel.setModifier(userName);
        cmsBtSizeChartModel.setCreater(userName);
        cmsBtSizeChartModel.setChannelId(channelId);
        Long sizeChartId =commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_SIZE_CHART_ID);
        cmsBtSizeChartModel.setSizeChartId(Integer.parseInt(String.valueOf(sizeChartId)));
        cmsBtSizeChartModel.setSizeChartName(sizeChartName);
        cmsBtSizeChartModel.setFinish("0");
        if (brandNameList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setBrandName(lst);
        } else {
            cmsBtSizeChartModel.setBrandName(brandNameList);
        }
        if(productTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setProductType(lst);
        } else {
            cmsBtSizeChartModel.setProductType(productTypeList);
        }
        if(sizeTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setSizeType(lst);
        } else {
            cmsBtSizeChartModel.setSizeType(sizeTypeList);
        }
        cmsBtSizeChartModel.setActive(1);
        cmsBtSizeChartDao.insert(cmsBtSizeChartModel);
    }
    public void sizeChartDetailUpdate(String channelId, String userName,String sizeChartId, String sizeChartName, String finishFlag
            , List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList, List<String> sizeMapList){
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        cmsBtSizeChartModel.setModifier(userName);
        cmsBtSizeChartModel.setCreater(userName);
        cmsBtSizeChartModel.setChannelId(channelId);
        cmsBtSizeChartModel.setSizeChartId(Integer.parseInt(sizeChartId));
        cmsBtSizeChartModel.setSizeChartName(sizeChartName);
        cmsBtSizeChartModel.setFinish(finishFlag);

        if (brandNameList.size()>0) {
            cmsBtSizeChartModel.setBrandName(brandNameList);
        }

        if(productTypeList.size()>0) {
            cmsBtSizeChartModel.setProductType(productTypeList);
        }

        if(sizeTypeList.size()>0) {
            cmsBtSizeChartModel.setSizeType(sizeTypeList);
        }
        if(sizeMapList.size()>0) {
            cmsBtSizeChartModel.setSizeType(sizeMapList);
        }
        cmsBtSizeChartModel.setActive(0);
        cmsBtSizeChartDao.insert(cmsBtSizeChartModel);
    }
}
