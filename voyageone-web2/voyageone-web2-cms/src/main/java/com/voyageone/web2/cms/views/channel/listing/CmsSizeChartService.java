package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtSizeChartDao;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //取得产品品牌
        data.put("brandNameList",brandNameList);
        //取得产品类型
        data.put("productTypeList",productTypeList);
        //取得产品性别
        data.put("sizeTypeList",sizeTypeList);
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
        List<CmsBtSizeChartModel> sizeChartList=sizeChartService.getSizeChartSearch(channelId, param);
        //第一页取得前一页记录
        int staIdx;
        if((int)param.get("curr") ==1){
            staIdx = 0 ;
        }else{
            staIdx = ((int)param.get("curr") - 1)* (int)param.get("size");
        }
        int endIdx = staIdx + (int)param.get("size");
        int sizeChartListTotal = sizeChartList.size();
        if (endIdx > sizeChartListTotal) {
            endIdx = sizeChartListTotal;
        }
        List<CmsBtSizeChartModel> pageSizeChartList = sizeChartList.subList(staIdx, endIdx);
        //尺码关系一览检索
        param.put("sizeChartList",pageSizeChartList);
        param.put("total",sizeChartList.size());
        //返回数据的类型
        return data;
    }
    /**
     * 尺码关系一览初删除
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartUpdate(String channelId,Map param) {
        sizeChartService.sizeChartUpdate(channelId,param);
    }

    /**
     * 尺码关系一览编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartEditInsert(String channelId,Map param) {
        //插入数据库
        sizeChartService.insert(channelId,param);
    }
    /**
     * 尺码关系一览编辑详情编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartDetailUpdate(String channelId,Map param) {
        //插入数据库
        sizeChartService.sizeChartDetailUpdate(channelId,param);
    }
}
