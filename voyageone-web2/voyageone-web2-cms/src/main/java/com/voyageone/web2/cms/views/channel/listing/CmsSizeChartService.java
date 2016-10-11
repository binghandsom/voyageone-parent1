package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.ConvertUtil;
import com.voyageone.common.util.IntUtils;
import com.voyageone.common.util.LongUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.task.CmsBtSizeChartBean;
import com.voyageone.service.impl.cms.CmsBtSizeChartImageGroupService;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.model.cms.CmsBtSizeChartImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
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
    @Autowired
    CmsBtSizeChartImageGroupService cmsBtSizeChartImageGroupService;
    @Autowired
    ImageGroupService imageGroupService;
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
        //当前页数
        int curr = (int) param.get("curr");
        //每页显示显示记录
        int size = (int) param.get("size");
        //按照填写的条件去数据库检索记录
        List<CmsBtSizeChartModel> sizeChartList=sizeChartService.getSizeChartSearch(channelId
                ,sizeChartName,finishFlag,startTime,endTime,brandNameList,productTypeList,sizeTypeList,curr,size);
        //尺码关系一览检索
        param.put("sizeChartList", changeToBeanList(sizeChartList, channelId, lang));
        //取得总件数
        param.put("total",sizeChartService.getCount(channelId,sizeChartName,finishFlag,startTime,endTime,brandNameList,productTypeList,sizeTypeList));
        //返回数据的类型
        return param;
    }
//    public Map<String, Object>   getNoMatchList() {
//        List<CmsBtSizeChartModel> sizeChartList = sizeChartService.getSizeCharts()
//    }
    /**
     * 尺码关系一览初删除
     * @param param
     * @return data
     */
    public void sizeChartDelete(String channelId,Map param) {
        //用户名称
        String userName = param.get("userName").toString();
        //取得自增键
        int sizeChartId = ConvertUtil.toInt(param.get("sizeChartId"));
        //逻辑删除选中的记录
        sizeChartService.sizeChartUpdate(sizeChartId, userName, channelId);
        //删除尺码表组图关系
        List<CmsBtSizeChartImageGroupModel> listCmsBtSizeChartImageGroupModel = cmsBtSizeChartImageGroupService.getListByCmsBtSizeChartId(channelId, sizeChartId);
        for (CmsBtSizeChartImageGroupModel model : listCmsBtSizeChartImageGroupModel) {
            CmsBtImageGroupModel imageGroupModel = imageGroupService.getImageGroupModel(String.valueOf(model.getCmsBtImageGroupId()));
            imageGroupModel.setSizeChartName("");
            imageGroupModel.setSizeChartId(0);
            imageGroupService.update(imageGroupModel);
        }
        cmsBtSizeChartImageGroupService.deleteByCmsBtSizeChartId(channelId, sizeChartId);
    }

    /**
     * 尺码关系一览编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public void sizeChartEditInsert(String channelId,Map param) {
        //用户名称
        String userName = param.get("userName").toString();
        //尺码名称
        String sizeChartName = (String) param.get("sizeChartName");

        String finishFlag = (String) param.get("finishFlag");
        //产品品牌
        List<String> brandNameList = (List<String>) param.get("brandNameList");
        //产品类型
        List<String> productTypeList = (List<String>) param.get("productTypeList");
        //产品性别
        List<String> sizeTypeList = (List<String>) param.get("sizeTypeList");
        List<Map<String, Object>> listImageGroup = (List<Map<String, Object>>) param.get("listImageGroup");//[{cartId:0,imageGroupName:"22",imageGroupId:1}]

        int sizeChartId = ConvertUtil.toInt(param.get("sizeChartId"));
        // 必须输入check
        if (StringUtils.isEmpty(sizeChartName)) {
            throw new BusinessException("7000080");
        }
        if (sizeChartService.EXISTSName(channelId,sizeChartName, sizeChartId)) {
            //名称已经存在
            throw new BusinessException("4000009");
        }
        CmsBtSizeChartModel model = null;
        if (sizeChartId > 0) {
            model = sizeChartService.getCmsBtSizeChartModel(sizeChartId, channelId);
            //删除尺码表 图片组关系表
            cmsBtSizeChartImageGroupService.deleteByCmsBtSizeChartId(channelId, sizeChartId);
            //更新
            sizeChartService.Update(channelId,
                    userName, sizeChartId, sizeChartName, finishFlag, brandNameList, productTypeList, sizeTypeList);
        } else {
            //插入
            model = sizeChartService.insert(channelId, userName, sizeChartName, brandNameList, productTypeList, sizeTypeList);
        }
        for (Map<String, Object> mapImageGroup : listImageGroup) {//[{cartId:0,imageGroupName:"22",imageGroupId:1}]
            long imageGroupId = ConvertUtil.toLong(mapImageGroup.get("imageGroupId"));
            String imageGroupName = ConvertUtil.toString(mapImageGroup.get("imageGroupName"));
            int imageGroup_CartId = ConvertUtil.toInt(mapImageGroup.get("cartId"));
            //根据尺码关系一览编辑的数据插入数据库
            if (imageGroupId > 0) {
                //更新组图
                CmsBtImageGroupModel cmsBtImageGroupModel = imageGroupService.getImageGroupModel(String.valueOf(imageGroupId));
                cmsBtImageGroupModel.setBrandName(brandNameList);
                cmsBtImageGroupModel.setProductType(productTypeList);
                cmsBtImageGroupModel.setSizeType(sizeTypeList);
                cmsBtImageGroupModel.setModifier(userName);
                imageGroupService.update(cmsBtImageGroupModel);
            } else if (!StringUtils.isEmpty(imageGroupName)) {
                if (imageGroupService.EXISTSName(channelId, imageGroup_CartId, imageGroupName, 0)) {
                    //名称已经存在
                    throw new BusinessException("组图名称重复");
                }
                //新增组图
                CmsBtImageGroupModel cmsBtImageGroupModel = imageGroupService.save(channelId, userName, String.valueOf(imageGroup_CartId), imageGroupName, null, null, brandNameList, productTypeList, sizeTypeList, model.getSizeChartId(), model.getSizeChartName());
                imageGroupId = cmsBtImageGroupModel.getImageGroupId();
            }
            if (imageGroupId > 0) {
                //保存 尺码表 图片组关系表
                cmsBtSizeChartImageGroupService.save(channelId, imageGroup_CartId, model.getSizeChartId(), imageGroupId, userName);
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
     * 检索结果转换
     *
     * @param imageGroupList 检索结果（Model）
     * @param channelId 渠道id
     * @param lang 语言
     * @return 检索结果（bean）
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
     * @param bean 检索结果（bean）
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
    public List<Map<String,Object>> getListImageGroupBySizeChartId(String channelId,int sizeChartId) {
        List<CmsBtSizeChartImageGroupModel> list = cmsBtSizeChartImageGroupService.getListByCmsBtSizeChartId(channelId, sizeChartId);
        List<Map<String, Object>> listImageGroup = new ArrayList<>();
        CmsBtImageGroupModel groupModel = null;
        Map<String, Object> map=null;
        for (CmsBtSizeChartImageGroupModel m : list) {
            map = new HashMap<>();
            groupModel = imageGroupService.getImageGroupModel(String.valueOf(m.getCmsBtImageGroupId()));
            if(groupModel!=null) {
                map.put("imageGroupName", groupModel.getImageGroupName());
                map.put("imageGroupId", groupModel.getImageGroupId());
                map.put("cartId", m.getCartId());
                listImageGroup.add(map);
            }
        }
        return listImageGroup;
    }
}
