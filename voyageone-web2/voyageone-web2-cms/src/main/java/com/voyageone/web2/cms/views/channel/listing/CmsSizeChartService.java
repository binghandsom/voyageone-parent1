package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtSizeChartDao;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/5/5.
 */
@Service
public class CmsSizeChartService extends BaseAppService {
    @Autowired
    private CmsBtSizeChartDao cmsBtSizeChartDao;
    @Autowired
    private MongoSequenceService commSequenceMongoService;
    @Autowired
    private SizeChartService sizeChartService;
    /**
     * 尺码关系一览初始化画面
     * @param language
     * @param channelId
     * @return data
     */
    public Map<String, Object>sizeChartInit(String language,String channelId) {
        Map<String, Object> data =new HashMap<>();
        //取得产品品牌
        List<TypeChannelBean> brandNameList= TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, language);
        //取得产品类型
        List<TypeChannelBean> productTypeList= TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, channelId, language);
        //取得产品性别
        List<TypeChannelBean> sizeTypeList= TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, channelId, language);
        //取得尺码关系一览数据
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.selectInitSizeChartInfo(channelId);
        //取得产品品牌
        data.put("brandNameList",brandNameList);
        //取得产品类型
        data.put("productTypeList",productTypeList);
        //取得产品性别
        data.put("sizeTypeList",sizeTypeList);
        //取得尺码关系一览数据
        data.put("sizeChartList",sizeChartList);
        //返回数据的类型
        return data;
    }
    /**
     * 尺码关系一览检索画面
     * @param channelId
     * @param param
     * @return
     */
    public Map<String, Object>sizeChartSearch(String channelId,Map param) {
        Map<String, Object> data =new HashMap<>();
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
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.selectSearchSizeChartInfo(channelId,cmsBtSizeChartModel);
        //尺码关系一览检索
        param.put("sizeChartList",sizeChartList);
        //返回数据的类型
        return data;
    }
    /**
     * 尺码关系一览初删除
     * @param channelId
     * @param param
     * @return data
     */
    public Map<String, Object>sizeChartUpdate(String channelId,Map param) {
        Map<String, Object> data =new HashMap<>();
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        //公司平台销售渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //自增主键
        cmsBtSizeChartModel.setSizeChartId((int)param.get("sizeChartId"));
        //尺码关系一览检索
        cmsBtSizeChartDao.sizeChartUpdate(channelId,cmsBtSizeChartModel);
        //返回数据的类型
        return data;
    }

    /**
     * 尺码关系一览编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartEditInsert(String channelId,Map param) {
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        cmsBtSizeChartModel.setModifier(param.get("userName").toString());
        cmsBtSizeChartModel.setCreater(param.get("userName").toString());
        cmsBtSizeChartModel.setChannelId(channelId);
        Long sizeChartId =commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_SIZE_CHART_ID);
        cmsBtSizeChartModel.setSizeChartId(Integer.parseInt(String.valueOf(sizeChartId)));
        cmsBtSizeChartModel.setSizeChartName(param.get("sizeChartName").toString());
        cmsBtSizeChartModel.setFinish("0");
        if ((param.get("brandNameList")) instanceof String) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setBrandName(lst);
        } else {
            cmsBtSizeChartModel.setBrandName((List<String>) param.get("brandNameList"));
        }
        if ((param.get("productTypeList")) instanceof String) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setProductType(lst);
        } else {
            cmsBtSizeChartModel.setProductType((List<String>) param.get("productTypeList"));
        }
        if ((param.get("sizeTypeList")) instanceof String) {
            List lst = new ArrayList<String>();
            lst.add("All");
            cmsBtSizeChartModel.setSizeType(lst);
        } else {
            cmsBtSizeChartModel.setSizeType((List<String>) param.get("sizeTypeList"));
        }
        cmsBtSizeChartModel.setActive(1);
        //插入数据库
        sizeChartService.insert(cmsBtSizeChartModel);
    }
    /**
     * 尺码关系一览编辑详情编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public Map<String, Object>sizeChartDetailUpdate(String channelId,Map param) {
        Map<String, Object> data =new HashMap<>();
        return data;
    }
}
