package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtImageGroupBean;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.StockSeparateService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.views.promotion.task.CmsTaskStockIncrementDetailService;
import com.voyageone.web2.cms.views.promotion.task.CmsTaskStockService;
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
    MongoSequenceService commSequenceMongoService; // DAO: Sequence

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

        return result;
    }

    /**
     * 检索
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public List<CmsBtImageGroupBean> search(Map<String, Object> param) {

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
        // 根据条件取得检索结果
        List<CmsBtImageGroupModel> imageGroupList = imageGroupService.getList(channelId , platFormChangeList, imageType,
                                                        beginModified, endModified,brandNameList, productTypeList, sizeTypeList);

        // 检索结果转换
        return changeToBeanList(imageGroupList, (String)param.get("channelId"), (String)param.get("lang"));
    }

    /**
     * 检索结果转换
     *
     * @param imageGroupList 检索结果（Model）
     * @param channelId 渠道id
     * @param lang 语言
     * @return 检索结果（Bean）
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
     * 检索结果转换
     *
     * @param bean 检索结果（Bean）
     * @param channelId 渠道id
     * @param lang 语言
     * @return 检索结果（Bean）
     */
    private void editImageGroupBean(CmsBtImageGroupBean bean, String channelId, String lang) {
        if ("cn".equals(lang)) {
            // ImageType
            if (bean.getImageType() == 2) {
                bean.setImageTypeName("尺码图");
            } else if (bean.getImageType() == 3) {
                bean.setImageTypeName("品牌故事图");
            } else if (bean.getImageType() == 4) {
                bean.setImageTypeName("物流介绍图");
            }

        } else {
            // ImageType
            if (bean.getImageType() == 2) {
                bean.setImageTypeName("Size Chart Image");
            } else if (bean.getImageType() == 3) {
                bean.setImageTypeName("Brand Story Image");
            } else if (bean.getImageType() == 4) {
                bean.setImageTypeName("Shipping Description Image");
            }
        }
        // Platform
        TypeChannelBean typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, String.valueOf(bean.getCartId()), lang);
        if (typeChannelBean != null) {
            bean.setCartName(typeChannelBean.getName());
        }
         // ViewType
         if (bean.getViewType() == 1) {
             bean.setViewTypeName("PC");
         } else if (bean.getImageType() == 2) {
             bean.setViewTypeName("APP");
         }
    }

    /**
     * 新加/编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void save(Map<String, Object> param) {
        String channelId = (String)param.get("channelId");
        int cartId = Integer.parseInt((String)param.get("platform"));
        String imageGroupName = (String)param.get("imageGroupName");
        int imageType = Integer.parseInt((String)param.get("imageType"));
        int viewType = Integer.parseInt((String)param.get("viewType"));
        List<String> brandNameList = (List<String>)param.get("brandName");
        List<String> productTypeList = (List<String>)param.get("productType");
        List<String> sizeTypeList = (List<String>)param.get("sizeType");
        imageGroupService.save(channelId, cartId, imageGroupName, imageType, viewType,
                brandNameList, productTypeList, sizeTypeList);
    }

    /**
     * 逻辑删除ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void delete(Map<String, Object> param) {
        String imageGroupId = String.valueOf(param.get("imageGroupId"));
        imageGroupService.logicDelete(imageGroupId);
    }

}