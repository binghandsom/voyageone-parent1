package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.CmsBtImageTemplateBean;
import com.voyageone.service.bean.cms.imagetemplate.GetDownloadUrlParamter;
import com.voyageone.service.dao.cms.mongo.CmsBtImageTemplateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.imagecreate.LiquidFireImageService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jeff.duan on 2016/5/5.
 */
@Service
public class ImageTemplateService extends BaseService {
    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence
    @Autowired
    private CmsBtImageTemplateDao dao;
    @Autowired
    LiquidFireImageService serviceLiquidFireImage;

    public Object getPage(Map<String, Object> map) {
        int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        String parameter = getSearchQuery(map);
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("");
        queryObject.setQuery(parameter);
        queryObject.setSort("{imageTemplateId:-1}");
        queryObject.setLimit(pageSize);
        queryObject.setSkip((pageIndex - 1) * pageSize);
        List<CmsBtImageTemplateModel> list = dao.select(queryObject);
        return changeToBeanList(list, (String) map.get("channelId"), (String) map.get("lang"));
    }

    public Object getCount(Map<String, Object> mapQuery) {
        String parameter = getSearchQuery(mapQuery);
        return dao.countByQuery(parameter);
    }

    public void update(CmsBtImageTemplateModel model) {
        dao.update(model);
    }

    public List<CmsBtImageTemplateModel> getList(JomgoQuery queryObject) {
        return dao.select(queryObject);
    }

    public CmsBtImageTemplateModel getOne(JomgoQuery queryObject) {
        return dao.selectOneWithQuery(queryObject);
    }

    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();
        // 取得当前channel, 有多少个platform(Approve平台)
        result.put("platformList", TypeChannels.getTypeListSkuCarts((String) param.get("channelId"), "A", (String) param.get("lang")));
        // 品牌下拉列表
        result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String) param.get("channelId"), (String) param.get("lang")));
        // 产品类型下拉列表
        result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String) param.get("channelId"), (String) param.get("lang")));
        // 尺寸类型下拉列表
        result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String) param.get("channelId"), (String) param.get("lang")));

        result.put("imageTemplateList", Types.getTypeList(Constants.comMtTypeChannel.Image_Template_Type.toString(), (String) param.get("lang")));
        return result;
    }

    /**
     * 检索结果转换
     *
     * @param imageGroupList 检索结果（Model）
     * @param channelId      渠道id
     * @param lang           语言
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
     * @param bean      检索结果（Bean）
     * @param channelId 渠道id
     * @param lang      语言
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
        if (bean.getProductType() != null) {
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
        }
        // Related Size Type
        if (bean.getSizeType() != null) {
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
        if (bean.getImageTemplateType() != 0) {
            String name = Types.getTypeName(73, lang, bean.getImageTemplateType().toString());
            bean.setImageTemplateTypeName(name);
        }
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    private String getSearchQuery(Map<String, Object> param) {
        StringBuilder result = new StringBuilder();
        List cartIdList = (List) param.get("cartIdList");
        if (cartIdList != null && cartIdList.size() > 0) {
            result.append(MongoUtils.splicingValue("cartId", cartIdList.toArray(new Integer[cartIdList.size()])));
            result.append(",");
        }

        if (!StringUtils.isEmpty((String) param.get("imageTemplateType"))) {
            result.append(MongoUtils.splicingValue("imageTemplateType", Integer.parseInt((String) param.get("imageTemplateType"))));
            result.append(",");
        }
        if (!StringUtils.isEmpty((String) param.get("viewType"))) {
            result.append(MongoUtils.splicingValue("viewType", Integer.parseInt((String) param.get("viewType"))));
            result.append(",");
        }
        if (!StringUtils.isEmpty((String) param.get("imageTemplateName"))) {
            result.append("imageTemplateName:" + "{ $regex:\"" + (String) param.get("imageTemplateName") + "\"}");  //Regex."/"+ (String) param.get("imageTemplateName")+"/"));
            result.append(",");
        }
        // Update Time
        if (!StringUtils.isEmpty((String) param.get("beginModified")) || !StringUtils.isEmpty((String) param.get("endModified"))) {
            result.append("\"modified\":{");
            // 获取Update Time Start
            if (!StringUtils.isEmpty((String) param.get("beginModified"))) {
                result.append(MongoUtils.splicingValue("$gte", (String) param.get("beginModified") + " 00.00.00"));
            }
            // 获取Update Time End
            if (!StringUtils.isEmpty((String) param.get("endModified"))) {
                if (!StringUtils.isEmpty((String) param.get("beginModified"))) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", (String) param.get("endModified") + " 23.59.59"));
            }
            result.append("},");
        }

        // brandName
        List brandNameList = (List) param.get("brandName");
        if (brandNameList.size() > 0) {
            // 带上"All"
            brandNameList.add("All");
            result.append(MongoUtils.splicingValue("brandName", brandNameList.toArray(new String[brandNameList.size()])));
            result.append(",");
        }

        // productType
        List productTypeList = (List) param.get("productType");
        if (productTypeList.size() > 0) {
            // 带上"All"
            productTypeList.add("All");
            result.append(MongoUtils.splicingValue("productType", productTypeList.toArray(new String[productTypeList.size()])));
            result.append(",");
        }

        // sizeType
        List sizeTypeList = (List) param.get("sizeType");
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

    public boolean isNull(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 保存方法
     *
     * @param model 客户端参数
     * @return 检索结果
     */
    public void save(CmsBtImageTemplateModel model, String userName) {
        check(model);
        //设置默认值
        if (isNull(model.getBrandName())) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setBrandName(lst);
        }
        if (isNull(model.getProductType())) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setProductType(lst);
        }
        if (isNull(model.getSizeType())) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setSizeType(lst);
        }
        if (model.getImageTemplateId() != null && model.getImageTemplateId() > 0) {
            //更新
            CmsBtImageTemplateModel oldModel = dao.selectById(model.get_id());
            if (!oldModel.getImageTemplateContent().equals(model.getImageTemplateContent())) {
                model.setTemplateModified(DateTimeUtil.getNow());
            }
            dao.update(model);
        } else {
            //新增
            model.setActive(1);
            model.setImageTemplateId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_TEMPLATE_ID));
            model.setCreater(userName);
            model.setTemplateModified(DateTimeUtil.getNow());
            model.setModifier(userName);
            model.setCreated(DateTimeUtil.getNow());
            dao.insert(model);
        }
    }
    void check(CmsBtImageTemplateModel model) {
        CallResult result = new CallResult();
        if (model.getBrandName() == null || model.getBrandName().size() == 0) {
            throw new BusinessException("7000080");
        }
        //7000080  必填
        if (StringUtils.isEmpty(model.getImageTemplateName())) {
            throw new BusinessException("7000080");
        }
        if (model.getCartId() == null || model.getCartId() == 0) {
            throw new BusinessException("7000080");
        }
        if (model.getViewType() == null || model.getViewType() == 0) {
            throw new BusinessException("7000080");
        }
        if (model.getImageTemplateType() == null || model.getImageTemplateType() == 0) {
            throw new BusinessException("7000080");
        }
        if (StringUtils.isEmpty(model.getImageTemplateContent())) {
            throw new BusinessException("7000080");
        }
        long count = dao.countByQuery("{\"imageTemplateName\":\"" + model.getImageTemplateName() + "\"" + ",\"imageTemplateId\": { $ne:" + model.getImageTemplateId() + "}");
        if (count > 0) {
            result.setMsg(model.getImageTemplateName() + "已经存在");
            result.setResult(false);
        }
    }
    /**
     * 逻辑删除ImageGroup信息
     *
     * @param imageTemplateId 客户端参数
     * @return 检索结果
     */
    public void delete(long imageTemplateId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageTemplateId\":" + imageTemplateId + "}");
        CmsBtImageTemplateModel model = getOne(queryObject);
        if (model != null) {
            model.setActive(0);
            dao.update(model);
        }
    }
    public CmsBtImageTemplateModel get(long imageTemplateId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageTemplateId\":" + imageTemplateId + "}");
        CmsBtImageTemplateModel model = getOne(queryObject);
        return model;
    }
    public String[] getTemplateParameter(String templateContent) {
      //  String prefix = "ftp://images@xpairs.com:voyageone5102@ftp.xpairs.com";//待加入配置项
        //String prefix="http://mce042-fs.nexcess.net:81/voyageone_image";
        String prefix="http://";
        String[] strList = templateContent.split("%s");
        String[] paramList = new String[strList.length - 1];
        for (int i = 0; i < strList.length - 1; i++) {
            if (strList[i].indexOf(prefix) > 0) {
                paramList[i] = "test1.png";
            } else {
                paramList[i] = "test中国&" + i;
            }
        }
        return paramList;
    }
    public String getDownloadUrl(GetDownloadUrlParamter paramter) throws Exception {
        return serviceLiquidFireImage.getDownloadUrl(paramter.getTemplateContent(), JacksonUtil.bean2Json(paramter.getTemplateParameter()));
    }
}