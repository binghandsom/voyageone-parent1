package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.*;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.CmsBtImageTemplateBean;
import com.voyageone.service.bean.cms.imagetemplate.GetDownloadUrlParamter;
import com.voyageone.service.bean.cms.imagetemplate.ImageTempateParameter;
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
    public  List<CmsBtImageTemplateBean> getPage(ImageTempateParameter param, String channelId, String lang) {
        int pageIndex = param.getPageIndex();
        int pageSize = param.getPageSize();
        String parameter = getSearchQuery(param, channelId);
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("");
        queryObject.setQuery(parameter);
        queryObject.setSort("{imageTemplateId:-1}");
        queryObject.setLimit(pageSize);
        queryObject.setSkip((pageIndex - 1) * pageSize);
        List<CmsBtImageTemplateModel> list = dao.select(queryObject);
        return changeToBeanList(list, channelId, lang);
    }
    public Object getCount(ImageTempateParameter param, String channelId) {
        String parameter = getSearchQuery(param, channelId);
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
            editImageTemplateBean(dest, channelId, lang);
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
    private void editImageTemplateBean(CmsBtImageTemplateBean bean, String channelId, String lang) {
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

    private String getSearchQuery(ImageTempateParameter param, String channelId) {
        StringBuilder result = new StringBuilder();
        if (ListUtils.notNull(param.getCartIdList())) {
            result.append(MongoUtils.splicingValue("cartId", param.getCartIdList().toArray()));
            result.append(",");
        }
        if (param.getImageTemplateType() > 0) {
            result.append(MongoUtils.splicingValue("imageTemplateType", param.getImageTemplateType()));
            result.append(",");
        }
        if (param.getViewType() > 0) {
            result.append(MongoUtils.splicingValue("viewType", param.getViewType()));
            result.append(",");
        }
        if (!StringUtils.isEmpty(param.getImageTemplateName())) {
            result.append("imageTemplateName:" + "{ $regex:\"" + param.getImageTemplateName() + "\"}");  //Regex."/"+ (String) param.get("imageTemplateName")+"/"));
            result.append(",");
        }
        // Update Time
        if (!StringUtils.isEmpty(param.getBeginModified()) || !StringUtils.isEmpty(param.getEndModified())) {
            result.append("\"modified\":{");
            // 获取Update Time Start
            if (!StringUtils.isEmpty(param.getBeginModified())) {
                result.append(MongoUtils.splicingValue("$gte", param.getBeginModified() + " 00.00.00"));
            }
            // 获取Update Time End
            if (!StringUtils.isEmpty(param.getEndModified())) {
                if (!StringUtils.isEmpty(param.getBeginModified())) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", param.getEndModified() + " 23.59.59"));
            }
            result.append("},");
        }
        // brandName
        if (ListUtils.notNull(param.getBrandName())) {
            // 带上"All"
            param.getBrandName().add("All");
            result.append(MongoUtils.splicingValue("brandName", param.getBrandName().toArray(new String[param.getBrandName().size()])));
            result.append(",");
        }

        // productType
        if (ListUtils.notNull(param.getProductType())) {
            // 带上"All"
            param.getProductType().add("All");
            result.append(MongoUtils.splicingValue("productType", param.getProductType().toArray(new String[param.getProductType().size()])));
            result.append(",");
        }

        // sizeType
        if (ListUtils.notNull(param.getSizeType())) {
            // 带上"All"
            param.getSizeType().add("All");
            result.append(MongoUtils.splicingValue("sizeType", param.getSizeType().toArray(new String[param.getSizeType().size()])));
            result.append(",");
        }
        // channelId
        result.append(MongoUtils.splicingValue("channelId", channelId));
        result.append(",");

        // active
        result.append(MongoUtils.splicingValue("active", 1));

        return "{" + result.toString() + "}";
    }

    public boolean isNull(List list) {
        return list != null && list.size() == 0;
    }

    /**
     * 保存方法
     *
     * @param model 客户端参数
     * @return 检索结果
     */
    public void save(CmsBtImageTemplateModel model, String userName) {

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
        String prefix = "http://";
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

    public boolean EXISTSName(String ImageTemplateName, long ImageTemplateId) {
        long count = dao.countByQuery("{\"imageTemplateName\":\"" + ImageTemplateName + "\"" + ",\"imageTemplateId\": { $ne:" + ImageTemplateId + "}}");
        return count > 0;
    }
}