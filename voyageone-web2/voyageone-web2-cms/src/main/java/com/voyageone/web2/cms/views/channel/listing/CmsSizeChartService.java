package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.task.CmsBtSizeChartBean;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.web2.base.BaseAppService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
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
    public Map<String, Object>sizeChartSearch(String channelId,Map param,String lang) {
        //尺码名称
        String sizeChartName=(String) param.get("sizeChartName");
        //尺码标志
        String finishFlag=(String) param.get("finishFlag");
        //更新开始时间
        String startTime=(String) param.get("startTime");
        //更新结束时间
        String endTime=(String) param.get("endTime");
        //产品品牌
        List<String> brandNameList=(List<String>) param.get("brandNameList");
        //产品类型
        List<String> productTypeList=(List<String>) param.get("productTypeList");
        //产品性别
        List<String> sizeTypeList=(List<String>) param.get("sizeTypeList");
        //按照填写的条件去数据库检索记录
        List<CmsBtSizeChartModel> sizeChartList=sizeChartService.getSizeChartSearch(channelId
                ,sizeChartName,finishFlag,startTime,endTime,brandNameList,productTypeList,sizeTypeList);
        int staIdx;
        if((int)param.get("curr") ==1){
            staIdx = 0 ;
        }else{
            staIdx = ((int)param.get("curr") - 1)* (int)param.get("size");
        }
        //每页多少条记录
        int endIdx = staIdx + (int)param.get("size");
        //检索的总共记录
        int sizeChartListTotal = sizeChartList.size();
        if (endIdx > sizeChartListTotal) {
            endIdx = sizeChartListTotal;
        }
        List<CmsBtSizeChartModel> pageSizeChartList = sizeChartList.subList(staIdx, endIdx);
        //尺码关系一览检索
        param.put("sizeChartList", changeToBeanList(pageSizeChartList, channelId, lang));

        param.put("total",sizeChartList.size());
        //返回数据的类型
        return param;
    }
    /**
     * 尺码关系一览初删除
     * @param param
     * @return data
     */
    public void sizeChartUpdate(String channelId,Map param) {
        //用户名称
        String userName =param.get("userName").toString();
        //取得自增键
        int sizeChartId=(int) param.get("sizeChartId");
        //逻辑删除选中的记录
        sizeChartService.sizeChartUpdate(sizeChartId,userName,channelId);
    }

    /**
     * 尺码关系一览编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartEditInsert(String channelId,Map param) {
        //用户名称
        String userName =param.get("userName").toString();
        //尺码名称
        String sizeChartName=(String) param.get("sizeChartName");
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
        //根据尺码关系一览编辑的数据插入数据库
        sizeChartService.insert(channelId,userName,sizeChartName,brandNameList,productTypeList,sizeTypeList);
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
     * 检索结果转换
     *
     * @param imageGroupList 检索结果（Model）
     * @param channelId 渠道id
     * @param lang 语言
     * @return 检索结果（Bean）
     */
    public List<CmsBtSizeChartBean> changeToBeanList(List<CmsBtSizeChartModel> imageGroupList, String channelId, String lang) {
        List<CmsBtSizeChartBean> CmsBtSizeChartBeanList = new ArrayList<>();

        for (CmsBtSizeChartModel imageGroup : imageGroupList) {
            CmsBtSizeChartBean dest = new CmsBtSizeChartBean();
            try {
                BeanUtils.copyProperties(dest, imageGroup);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            editCmsBtSizeChartBean(dest, channelId, lang);
            CmsBtSizeChartBeanList.add(dest);
        }


        return CmsBtSizeChartBeanList;
    }
    /**
     * 检索结果编辑
     *
     * @param bean 检索结果（Bean）
     * @param channelId 渠道id
     * @param lang 语言
     */
    public void editCmsBtSizeChartBean(CmsBtSizeChartBean bean, String channelId, String lang) {
        List<String> brandNameTrans = new ArrayList<>();
        for (String brandName : bean.getBrandName()) {
            if ("All".equals(brandName)) {
                brandNameTrans.add("All");
            } else {
                TypeChannelBean  typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.BRAND_41, channelId, brandName, lang);
                if (typeChannelBean != null) {
                    brandNameTrans.add(typeChannelBean.getName());
                }
            }
        }
        bean.setBrandNameTrans(brandNameTrans);
        // Related Product Type
        List<String> productTypeTrans = new ArrayList<>();
        for (String productType : bean.getProductType()) {
            if ("All".equals(productType)) {
                productTypeTrans.add("All");
            } else {
                TypeChannelBean typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.PROUDCT_TYPE_57, channelId, productType, lang);
                if (typeChannelBean != null) {
                    productTypeTrans.add(typeChannelBean.getName());
                }
            }
        }
        bean.setProductTypeTrans(productTypeTrans);
        // Related Size Type
        List<String> sizeTypeTrans = new ArrayList<>();
        for (String sizeType : bean.getSizeType()) {
            if ("All".equals(sizeType)) {
                sizeTypeTrans.add("All");
            } else {
                TypeChannelBean typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.PROUDCT_TYPE_58, channelId, sizeType, lang);
                if (typeChannelBean != null) {
                    sizeTypeTrans.add(typeChannelBean.getName());
                }
            }
        }
        bean.setSizeTypeTrans(sizeTypeTrans);
    }
}
