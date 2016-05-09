package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtImageTemplateBean;
import com.voyageone.service.dao.cms.CmsBtImagesDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
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
public class CmsImageTemplateService extends BaseService {

    @Autowired
    private ImageTemplateService service;

    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence
    @Autowired
    private CmsBtImagesDao cmsBtImagesDao;

    public List<CmsBtImagesModel> getImageList (CmsBtImagesModel model) {
        return cmsBtImagesDao.selectList(JacksonUtil.jsonToMap(JacksonUtil.bean2Json(model)));
    }

    public int insert(CmsBtImagesModel model) {
        return cmsBtImagesDao.insert(model);
    }

    public int update(CmsBtImagesModel model) {
        return cmsBtImagesDao.update(model);
    }
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
    public List<CmsBtImageTemplateBean> search(Map<String, Object> param) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(param));
        List<CmsBtImageTemplateModel> list = service.getList(queryObject);
        return changeToBeanList(list, (String)param.get("channelId"), (String)param.get("lang"));
    }

    /**
     * 检索结果转换
     *
     * @param imageGroupList 检索结果（Model）
     * @param channelId 渠道id
     * @param lang 语言
     * @return 检索结果（Bean）
     */
    private List<CmsBtImageTemplateBean> changeToBeanList(List<CmsBtImageTemplateModel> imageGroupList, String channelId, String lang) {
        List<CmsBtImageTemplateBean> imageGroupBeanList = new ArrayList<>();

        for (CmsBtImageTemplateModel imageGroup : imageGroupList) {
            CmsBtImageTemplateBean dest = new CmsBtImageTemplateBean();
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
    private void editImageGroupBean(CmsBtImageTemplateBean bean, String channelId, String lang) {
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
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    private String getSearchQuery(Map<String, Object> param) {
        StringBuilder result = new StringBuilder();

        // 获取Platform
        List<Map<String, Object>> platFormList = ((List)param.get("platformList"));
        List<Integer> platFormChangeList = platFormList.stream().filter((platfrom)->((Map) platfrom).get("show") != null && (boolean)((Map) platfrom).get("show") == true).map((platfrom)->Integer.parseInt((String)platfrom.get("value"))).collect(toList());
        if (platFormChangeList.size() > 0) {
            Integer[] platFormArray = platFormChangeList.toArray(new Integer[platFormList.size()]);
            result.append(MongoUtils.splicingValue("cartId", platFormArray));
            result.append(",");
        }

        // Image Type
        if (!StringUtils.isEmpty((String)param.get("imageType"))) {
            result.append(MongoUtils.splicingValue("imageType", Integer.parseInt((String)param.get("imageType"))));
            result.append(",");
        }

        // Update Time
        if (!StringUtils.isEmpty((String)param.get("beginModified")) || !StringUtils.isEmpty((String)param.get("endModified"))) {
            result.append("\"modified\":{" );
            // 获取Update Time Start
            if (!StringUtils.isEmpty((String)param.get("beginModified"))) {
                result.append(MongoUtils.splicingValue("$gte", (String)param.get("beginModified") + " 00.00.00"));
            }
            // 获取Update Time End
            if (!StringUtils.isEmpty((String)param.get("endModified"))) {
                if (!StringUtils.isEmpty((String)param.get("beginModified"))) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", (String)param.get("endModified") + " 23.59.59"));
            }
            result.append("},");
        }

        // brandName
        List brandNameList = (List)param.get("brandName");
        if (brandNameList.size() > 0) {
            // 带上"All"
            brandNameList.add("All");
            result.append(MongoUtils.splicingValue("brandName", brandNameList.toArray(new String[brandNameList.size()])));
            result.append(",");
        }

        // productType
        List productTypeList = (List)param.get("productType");
        if (productTypeList.size() > 0) {
            // 带上"All"
            productTypeList.add("All");
            result.append(MongoUtils.splicingValue("productType", productTypeList.toArray(new String[productTypeList.size()])));
            result.append(",");
        }

        // sizeType
        List sizeTypeList = (List)param.get("sizeType");
        if (sizeTypeList.size() > 0) {
            // 带上"All"
            sizeTypeList.add("All");
            result.append(MongoUtils.splicingValue("sizeType", sizeTypeList.toArray(new String[sizeTypeList.size()])));
            result.append(",");
        }

        // channelId
        result.append(MongoUtils.splicingValue("channelId", param.get("channelId")));
        result.append(",");

        // active
        result.append(MongoUtils.splicingValue("active", 1));

        return "{" + result.toString() + "}";
    }

    /**
     * 新加/编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void save(Map<String, Object> param) {
        CmsBtImageTemplateModel model = new CmsBtImageTemplateModel();
        model.setChannelId((String)param.get("channelId"));
        model.setCartId(Integer.parseInt((String)param.get("platform")));
        model.setImageTemplateId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_TEMPLATE_ID));
        model.setImageTemplateName((String)param.get("imageTemplateName"));
        model.setViewType(Integer.parseInt((String)param.get("viewType")));
        if (((List)param.get("brandName")).size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setBrandName(lst);
        } else {
            model.setBrandName((List) param.get("brandName"));
        }
        if (((List)param.get("productType")).size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setProductType(lst);
        } else {
            model.setProductType((List) param.get("productType"));
        }
        if (((List)param.get("sizeType")).size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setSizeType(lst);
        } else {
            model.setSizeType((List) param.get("sizeType"));
        }
        model.setActive(1);
        service.save(model);
    }

    /**
     * 逻辑删除ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void delete(Map<String, Object> param) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageTemplateId\":" + param.get("imageTemplateId") + "}");
        CmsBtImageTemplateModel model = service.getOne(queryObject);
        if (model != null) {
            model.setActive(0);
            service.update(model);
        }
    }

}