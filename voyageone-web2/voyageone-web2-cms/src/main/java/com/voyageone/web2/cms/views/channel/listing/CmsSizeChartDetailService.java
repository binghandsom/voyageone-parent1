package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.impl.cms.feed.CmsBtFeedImportSizeService;
import com.voyageone.service.model.cms.CmsBtFeedImportSizeModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModelSizeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by gjl on 2016/5/11.
 */
@Service
public class CmsSizeChartDetailService extends BaseService {
    @Autowired
    CmsBtFeedImportSizeService cmsBtFeedImportSizeService;
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private CmsSizeChartService cmsSizeChartService;
    /**
     * 尺码关系一览编辑详情检索画面
     * @param channelId
     * @param param
     */
    public void sizeChartDetailSearch(String channelId,Map param,String lang) {
        List<CmsBtSizeChartModel> sizeChartList = new ArrayList<>();
        //取得自增键
        int sizeChartId = (int) param.get("sizeChartId");
        //尺码表自增键取得当前的记录
        CmsBtSizeChartModel cmsBtSizeChartModel =sizeChartService.getCmsBtSizeChartModel(sizeChartId,channelId);

        //添加feed导入的尺码
        addFeddImportSize(cmsBtSizeChartModel);
        sizeChartList.add(cmsBtSizeChartModel);
        //尺码关系一览检索
        param.put("sizeChartList", cmsSizeChartService.changeToBeanList(sizeChartList, channelId, lang));
    }
    public void  addFeddImportSize( CmsBtSizeChartModel cmsBtSizeChartModel) {
        List<String> listBrandName = cmsBtSizeChartModel.getBrandName();
        List<String> listProductType = cmsBtSizeChartModel.getProductType();
        List<String> listSizeType = cmsBtSizeChartModel.getSizeType();
        String channelId = cmsBtSizeChartModel.getChannelId();

        //获取feed导入的尺码
        List<CmsBtFeedImportSizeModel> listCmsBtFeedImportSizeModel = cmsBtFeedImportSizeService.getList(channelId, listBrandName, listProductType, listSizeType);


        HashSet<String> hsSize = new HashSet<>();
        List<CmsBtSizeChartModelSizeMap> listSizeMap = cmsBtSizeChartModel.getSizeMap();
        for (CmsBtSizeChartModelSizeMap sizeMap : listSizeMap) {
            hsSize.add(sizeMap.getOriginalSize());
        }

        //把feed导入的尺码加进尺码表
        for (CmsBtFeedImportSizeModel importSize : listCmsBtFeedImportSizeModel) {
            //在尺码表中不存在    添加进尺码表
            if (!hsSize.contains(importSize.getOriginalSize())) {
                CmsBtSizeChartModelSizeMap cmsBtSizeChartModelSizeMap = new CmsBtSizeChartModelSizeMap();
                cmsBtSizeChartModelSizeMap.setOriginalSize(importSize.getOriginalSize());
                listSizeMap.add(cmsBtSizeChartModelSizeMap);
                hsSize.add(importSize.getOriginalSize());
            }
        }
    }
    /**
     * 尺码关系一览编辑详情编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartDetailUpdate(String channelId,Map param) {
        int sizeChartId =(int)param.get("sizeChartId");
        //用户名称
        String userName =param.get("userName").toString();
        //尺码名称
        String sizeChartName=(String) param.get("sizeChartName");
        //尺码标志
        String finishFlag=(String) param.get("finishFlag");
        //产品品牌
        List<String> brandNameList=(List<String>) param.get("brandNameList");
        //产品类型
        List<String> productTypeList=(List<String>) param.get("productTypeList");
        //产品性别
        List<String> sizeTypeList=(List<String>) param.get("sizeTypeList");
        // 必须输入check
        if (StringUtils.isEmpty(sizeChartName)) {
            throw new BusinessException("7000080");
        }
        //插入数据库
        sizeChartService.sizeChartDetailUpdate(channelId,
                userName, sizeChartId, sizeChartName, finishFlag, brandNameList, productTypeList, sizeTypeList);
    }

    /**
     * 尺码关系一览编辑详情编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartDetailSizeMapSave(String channelId,Map param) {
        int sizeChartId =(int)param.get("sizeChartId");
        //用户名称
        String userName =param.get("userName").toString();
        //sizeMapList
        List<CmsBtSizeChartModelSizeMap> sizeMapList=(List<CmsBtSizeChartModelSizeMap>) param.get("sizeMap");
        if(sizeMapList!=null&&sizeMapList.size()>0){
            //取得sizeMapList对象
            Set<String> originalSizeSet = new HashSet<>();
            for(int i=0;i<sizeMapList.size();i++){
                Map sizeMap = (Map)sizeMapList.get(i);
                String originalSize=(String)sizeMap.get("originalSize");
                String adjustSize=(String)sizeMap.get("adjustSize");
                //如果两个字段都是空，删除当前记录
                if (StringUtils.isEmpty(originalSize)&&StringUtils.isEmpty(adjustSize)) {
                    sizeMapList.remove(i);
                    continue;
                }
                //判断是否为空check
                if (StringUtils.isEmpty(originalSize)||StringUtils.isEmpty(adjustSize)) {
                    throw new BusinessException("7000080");
                }
                originalSizeSet.add(originalSize);
            }
            //重复check
            if(originalSizeSet.size() != sizeMapList.size()){
                throw new BusinessException("7000086");
            }
        }
        //插入数据库
        sizeChartService.sizeChartDetailSizeMapSave(channelId, userName, sizeChartId, sizeMapList);
    }
}
