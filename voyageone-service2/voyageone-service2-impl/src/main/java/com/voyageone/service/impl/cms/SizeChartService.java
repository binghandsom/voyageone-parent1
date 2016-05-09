package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsBtSizeChartDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/5/6.
 */
@Service
public class SizeChartService extends BaseService {
    @Autowired
    private CmsBtSizeChartDao cmsBtSizeChartDao;
    @Autowired
    private MongoSequenceService commSequenceMongoService;
    public void insert(String channelId,Map param) {
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        cmsBtSizeChartModel.setModifier(param.get("userName").toString());
        cmsBtSizeChartModel.setCreater(param.get("userName").toString());
        cmsBtSizeChartModel.setChannelId(channelId);
        Long sizeChartId =commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_SIZE_CHART_ID);
        cmsBtSizeChartModel.setSizeChartId(Integer.parseInt(String.valueOf(sizeChartId)));
        cmsBtSizeChartModel.setSizeChartName(param.get("sizeChartName").toString());
        cmsBtSizeChartModel.setFinish("0");
        List<String> brandNameList= (List<String>) param.get("brandNameList");
        if (brandNameList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setBrandName(lst);
        } else {
            cmsBtSizeChartModel.setBrandName((List<String>) param.get("brandNameList"));
        }
        List<String> productTypeList= (List<String>) param.get("productTypeList");
        if(productTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setProductType(lst);
        } else {
            cmsBtSizeChartModel.setProductType((List<String>) param.get("productTypeList"));
        }
        List<String> sizeTypeList= (List<String>) param.get("sizeTypeList");
        if(sizeTypeList.size()==0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setSizeType(lst);
        } else {
            cmsBtSizeChartModel.setSizeType((List<String>) param.get("sizeTypeList"));
        }
        cmsBtSizeChartModel.setActive(1);
        cmsBtSizeChartDao.insert(cmsBtSizeChartModel);
    }
    public List<CmsBtSizeChartModel> getSizeChartSearch(String channelId,Map param){
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        //公司平台销售渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码名称
        cmsBtSizeChartModel.setSizeChartName((String) param.get("sizeChartName"));
        //尺码标志
        cmsBtSizeChartModel.setFinish((String) param.get("finishFlag"));
        //更新开始时间
        cmsBtSizeChartModel.setCreated((String) param.get("startTime"));
        //更新结束时间
        cmsBtSizeChartModel.setModified((String) param.get("endTime"));
        //产品品牌
        cmsBtSizeChartModel.setBrandName((List<String>) param.get("brandNameList"));
        //产品类型
        cmsBtSizeChartModel.setProductType((List<String>) param.get("productTypeList"));
        //产品性别
        cmsBtSizeChartModel.setSizeType((List<String>) param.get("sizeTypeList"));
        //尺码关系一览检索
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.selectSearchSizeChartInfo(channelId, cmsBtSizeChartModel);
        return  sizeChartList;
    }
    public void sizeChartUpdate(String channelId,Map param){
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        //公司平台销售渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //自增主键
        cmsBtSizeChartModel.setSizeChartId((int)param.get("sizeChartId"));
        //尺码关系一览检索
        cmsBtSizeChartDao.sizeChartUpdate(channelId,cmsBtSizeChartModel);
    }
    public void sizeChartDetailUpdate(String channelId,Map param){
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        cmsBtSizeChartModel.setModifier(param.get("userName").toString());
        cmsBtSizeChartModel.setCreater(param.get("userName").toString());
        cmsBtSizeChartModel.setChannelId(channelId);
        cmsBtSizeChartModel.setSizeChartId(Integer.parseInt(param.get("sizeChartId").toString()));
        cmsBtSizeChartModel.setSizeChartName(param.get("sizeChartName").toString());
        cmsBtSizeChartModel.setFinish(param.get("finishFlag").toString());
        List<String> brandNameList= (List<String>) param.get("brandNameList");
        if (brandNameList.size()>0) {
            cmsBtSizeChartModel.setBrandName((List<String>) param.get("brandNameList"));
        }
        List<String> productTypeList= (List<String>) param.get("productTypeList");
        if(productTypeList.size()>0) {
            cmsBtSizeChartModel.setProductType((List<String>) param.get("productTypeList"));
        }
        List<String> sizeTypeList= (List<String>) param.get("sizeTypeList");
        if(sizeTypeList.size()>0) {
            cmsBtSizeChartModel.setSizeType((List<String>) param.get("sizeTypeList"));
        }
        List<String> sizeMapList= (List<String>) param.get("sizeMap");
        if(sizeMapList.size()>0) {
            cmsBtSizeChartModel.setSizeType((List<String>) param.get("sizeMap"));
        }
        cmsBtSizeChartModel.setActive(1);
        cmsBtSizeChartDao.insert(cmsBtSizeChartModel);
    }
}
