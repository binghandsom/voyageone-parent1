package com.voyageone.web2.cms.views.channel.listing;

import com.mchange.lang.IntegerUtils;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.ConvertUtil;
import com.voyageone.common.util.IntUtils;
import com.voyageone.common.util.LongUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtImageGroupBean;
import com.voyageone.service.impl.cms.CmsBtSizeChartImageGroupService;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.web2.base.BaseAppService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.stream.Collectors.toList;


/**
 * Created by jeff.duan on 2016/5/5.
 */
@Service
public class CmsImageGroupService extends BaseAppService {

    @Autowired
    private ImageGroupService imageGroupService;
@Autowired
    CmsBtSizeChartImageGroupService cmsBtSizeChartImageGroupService;

    @Autowired
    SizeChartService sizeChartService;
    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init (Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();

        // 取得当前channel, 有多少个platform(Approve平台)
        result.put("platformList", TypeChannels.getTypeListSkuCarts((String)param.get("channelId"), "A", (String)param.get("lang")));
        // 品牌下拉列表
        result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String)param.get("channelId"), (String)param.get("lang")));
        // 产品类型下拉列表
        result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String)param.get("channelId"), (String)param.get("lang")));
        // 尺寸类型下拉列表
        result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String)param.get("channelId"), (String)param.get("lang")));
        // 图片类型
        result.put("imageTypeList", Types.getTypeList(71, (String)param.get("lang")));

        return result;
    }

    /**
     * 检索
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public Map<String, Object> search(Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> platFormList = ((List)param.get("platformList"));
        // 取得check的平台列表
        List<Integer> platFormChangeList = platFormList.stream().filter((platfrom)->((Map) platfrom).get("show") != null && (boolean)((Map) platfrom).get("show") == true).map((platfrom)->Integer.parseInt((String)platfrom.get("value"))).collect(toList());
        String channelId = (String)param.get("channelId");
        String imageType = (String)param.get("imageType");
        String beginModified = (String)param.get("beginModified");
        String endModified = (String)param.get("endModified");
        List<String> brandNameList = (List<String>)param.get("brandName");
        List<String> productTypeList = (List<String>)param.get("productType");
        List<String> sizeTypeList = (List<String>)param.get("sizeType");
        int curr = (int) param.get("curr");
        int size = (int) param.get("size");
        // 根据条件取得检索结果
        List<CmsBtImageGroupModel> imageGroupList = imageGroupService.getList(channelId , platFormChangeList, imageType,
                                                        beginModified, endModified,brandNameList, productTypeList, sizeTypeList, curr, size);
        result.put("total", imageGroupService.getCount(channelId , platFormChangeList, imageType,
                beginModified, endModified,brandNameList, productTypeList, sizeTypeList));

        // 检索结果转换
        result.put("imageGroupList",  changeToBeanList(imageGroupList, (String)param.get("channelId"), (String)param.get("lang")));

        return result;

    }

    /**
     * 检索结果转换
     *
     * @param imageGroupList 检索结果（Model）
     * @param channelId 渠道id
     * @param lang 语言
     * @return 检索结果（bean）
     */
    private List<CmsBtImageGroupBean> changeToBeanList(List<CmsBtImageGroupModel> imageGroupList, String channelId, String lang) {
        List<CmsBtImageGroupBean> imageGroupBeanList = new ArrayList<>();

        for (CmsBtImageGroupModel imageGroup : imageGroupList) {
            CmsBtImageGroupBean dest = new CmsBtImageGroupBean();
            try {
                BeanUtils.copyProperties(dest, imageGroup);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            editImageGroupBean(dest, channelId, lang);
            imageGroupBeanList.add(dest);
        }


        return imageGroupBeanList;
    }

    /**
     * 检索结果编辑
     *
     * @param bean 检索结果（bean）
     * @param channelId 渠道id
     * @param lang 语言
     */
    private void editImageGroupBean(CmsBtImageGroupBean bean, String channelId, String lang) {

        // ImageType
        bean.setImageTypeName(Types.getTypeName(71, lang, String.valueOf(bean.getImageType())));

        // Platform
        TypeChannelBean typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, String.valueOf(bean.getCartId()), lang);
        if (typeChannelBean != null) {
            bean.setCartName(typeChannelBean.getName());
        }
         // ViewType
         if (bean.getViewType() == 1) {
             bean.setViewTypeName("PC");
         } else if (bean.getViewType() == 2) {
             bean.setViewTypeName("APP");
         }

        // Related Brand Name
        List<String> brandNameTrans = new ArrayList<>();
        for (String brandName : bean.getBrandName()) {
            if ("All".equals(brandName)) {
                brandNameTrans.add("All");
            } else {
                typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.BRAND_41, channelId, brandName, lang);
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
                typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.PROUDCT_TYPE_57, channelId, productType, lang);
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
                typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.PROUDCT_TYPE_58, channelId, sizeType, lang);
                if (typeChannelBean != null) {
                    sizeTypeTrans.add(typeChannelBean.getName());
                }
            }
        }
        bean.setSizeTypeTrans(sizeTypeTrans);
    }

    /**
     * 新建ImageGroup信息
     *
     * @param param 客户端参数
     */
    public void save(Map<String, Object> param) {
        long imageGroupId = LongUtils.parseLong(param.get("imageGroupId"));
        String channelId = (String) param.get("channelId");
        String userName = (String) param.get("userName");
        String cartId = (String) param.get("platform");
        String imageGroupName = (String) param.get("imageGroupName");
        String imageType = (String) param.get("imageType");
        String viewType = (String) param.get("viewType");
        List<String> brandNameList = (List<String>) param.get("brandName");
        List<String> productTypeList = (List<String>) param.get("productType");
        List<String> sizeTypeList = (List<String>) param.get("sizeType");
        int sizeChartId = ConvertUtil.toInt(param.get("sizeChartId"));;
        String sizeChartName = ConvertUtil.toString( param.get("sizeChartName"));

        CmsBtImageGroupModel model = saveCmsBtImageGroupModel(imageGroupId, channelId, userName, cartId, imageGroupName, imageType, viewType, brandNameList, productTypeList, sizeTypeList, sizeChartId, sizeChartName);


        if (sizeChartId > 0) {
            //更新尺码表
            CmsBtSizeChartModel cmsBtSizeChartModel = sizeChartService.getCmsBtSizeChartModel(sizeChartId, channelId);
            cmsBtSizeChartModel.setBrandName(brandNameList);
            cmsBtSizeChartModel.setProductType(productTypeList);
            cmsBtSizeChartModel.setSizeType(sizeTypeList);
            cmsBtSizeChartModel.setModifier(userName);
            sizeChartService.update(cmsBtSizeChartModel);

        } else if (!StringUtils.isEmpty(sizeChartName)) {
            //新增尺码表
            CmsBtSizeChartModel cmsBtSizeChartModel = sizeChartService.insert(channelId, userName, sizeChartName, brandNameList, productTypeList, sizeTypeList, model.getImageGroupId(), model.getImageGroupName());
            sizeChartId = cmsBtSizeChartModel.getSizeChartId();
            model.setSizeChartId(cmsBtSizeChartModel.getSizeChartId());
            model.setSizeChartName(cmsBtSizeChartModel.getSizeChartName());
            imageGroupService.update(model);
        }
        if (sizeChartId > 0) {
            //保存 尺码表 图片组关系表
            cmsBtSizeChartImageGroupService.save(channelId, sizeChartId, model.getImageGroupId(), userName);
        }
    }

    private CmsBtImageGroupModel saveCmsBtImageGroupModel(long imageGroupId, String channelId, String userName, String cartId, String imageGroupName, String imageType, String viewType, List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList, int sizeChartId, String sizeChartName) {
        // 必须输入check
        if (StringUtils.isEmpty(cartId) || StringUtils.isEmpty(imageGroupName)
                || StringUtils.isEmpty(imageType) || StringUtils.isEmpty(viewType)) {
            // 请输入必填项目
            throw new BusinessException("7000080");
        }
        CmsBtImageGroupModel model = null;
        if (imageGroupId > 0) {
            // 如果存在图片那么平台不能变更
            model = imageGroupService.getImageGroupModel(String.valueOf(imageGroupId));
            if (model != null && model.getImage() != null
                    && model.getImage().size() > 0
                    && model.getCartId() != Integer.parseInt(cartId)) {
                // 图片已经存在，不能修改平台
                throw new BusinessException("7000088");
            }
            if (sizeChartId != model.getSizeChartId() && model.getSizeChartId() > 0) {
                //删除尺码图和尺码表关联关系
                cmsBtSizeChartImageGroupService.delete(model.getChannelId(), model.getSizeChartId(), model.getImageGroupId());
            }
            //更新
            imageGroupService.update(userName, String.valueOf(imageGroupId), cartId, imageGroupName, imageType, viewType,
                    brandNameList, productTypeList, sizeTypeList);

        } else {
            //新增
            model = imageGroupService.save(channelId, userName, cartId, imageGroupName, imageType, viewType,
                    brandNameList, productTypeList, sizeTypeList, sizeChartId, sizeChartName);
        }
        return model;
    }

    /**
     * 逻辑删除ImageGroup信息
     *
     * @param param 客户端参数
     */
    public void delete(Map<String, Object> param,String channelId) {
        boolean isDelSizeChart = ConvertUtil.toBoolean(param.get("isDelSizeChart "));
        int sizeChartId = ConvertUtil.toInt(param.get("sizeChartId"));
        long imageGroupId = ConvertUtil.toLong(param.get("imageGroupId"));
        String userName = (String) param.get("userName");
        if (sizeChartId > 0) {
            cmsBtSizeChartImageGroupService.delete(channelId, sizeChartId, imageGroupId);
            if (isDelSizeChart) {
                sizeChartService.delete(sizeChartId, userName, channelId);
            }
        }
        imageGroupService.logicDelete(String.valueOf(imageGroupId), userName);
    }
}