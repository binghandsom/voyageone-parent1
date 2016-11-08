package com.voyageone.service.impl.cms.jumei;

import com.mchange.v1.lang.BooleanUtils;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.mongo.CmsBtJmImageTemplateDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSpecialExtensionDaoExt;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.CmsBtJmImageTemplateModel;
import com.voyageone.service.model.cms.mongo.CmsBtJmImageTemplateModel_TemplateUrls;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsMtJmConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by james on 2016/10/18.
 *
 * @version 2.8.0
 * @since 2.8.0
 */
@Service
public class CmsBtJmImageTemplateService {
    private final CmsBtJmImageTemplateDao cmsBtJmImageTemplateDao;
    private final CmsBtJmPromotionDao cmsBtJmPromotionDao;
    private final TagService tagService;
    private final CmsBtJmPromotionSpecialExtensionDaoExt jmPromotionExtensionDaoExt;
    private final CmsMtJmConfigService cmsMtJmConfigService;

    @Autowired
    public CmsBtJmImageTemplateService(CmsBtJmImageTemplateDao cmsBtJmImageTemplateDao, CmsBtJmPromotionDao cmsBtJmPromotionDao, TagService tagService, CmsBtJmPromotionSpecialExtensionDaoExt jmPromotionExtensionDaoExt, CmsMtJmConfigService cmsMtJmConfigService) {
        this.cmsBtJmImageTemplateDao = cmsBtJmImageTemplateDao;
        this.cmsBtJmPromotionDao = cmsBtJmPromotionDao;
        this.tagService = tagService;
        this.jmPromotionExtensionDaoExt = jmPromotionExtensionDaoExt;
        this.cmsMtJmConfigService = cmsMtJmConfigService;
    }


    public CmsBtJmImageTemplateModel getJMImageTemplateByType(String imageType) {
        JongoQuery queryObject = new JongoQuery();
        Criteria criteria = Criteria.where("imageType").is(imageType);
        queryObject.setQuery(criteria);
        return cmsBtJmImageTemplateDao.selectOneWithQuery(queryObject);
    }

    public List<CmsBtJmImageTemplateModel> getAllJMImageTemplate() {
        return cmsBtJmImageTemplateDao.selectAll();
    }

    public WriteResult insert(CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel) {
        return cmsBtJmImageTemplateDao.insert(cmsBtJmImageTemplateModel);
    }


    /**
     * 根据jmPromotionId 和图片类型 返回 带模板的url地址
     *
     * @param imageName     底图的文件名
     * @param imageType     图片类型
     * @param jmPromotionId 活动Id
     */
    public String getUrl(String imageName, String imageType, Integer jmPromotionId) {
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = getEditModel(jmPromotionId, true);
        return getUrl(imageName, imageType, cmsBtJmPromotionSaveBean);
    }

    /**
     * 根据jmPromotionId 和图片类型 返回 带模板的url地址
     */
    public String getUrl(String imageName, String imageType, CmsBtJmPromotionSaveBean orgCmsBtJmPromotionSaveBean) {
        int index = 0;
        //取得模板参数信息
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = JacksonUtil.json2Bean(JacksonUtil.bean2Json(orgCmsBtJmPromotionSaveBean), CmsBtJmPromotionSaveBean.class);
        String paramString = "\"" + imageName + "\"";
        //根据imageType在cms_bt_jm_image_template取得图片的相应信息
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = getJMImageTemplateByType(imageType);
        //根据模板传出参数来选择模板
        //【品牌Logo】属性 & 【入口图专场导向文案】属性为空时
        if (!StringUtil.isEmpty(cmsBtJmPromotionSaveBean.getExtModel().getEnterGuide()) &&
                cmsBtJmPromotionSaveBean.getExtModel().getIsCheckedBrandLogo() &&
                StringUtil.isEmpty(cmsBtJmPromotionSaveBean.getExtModel().getBrandLogo())) {
            // 模板一
            index = 0;
        }
        //【品牌Logo】属性为空 &【入口图专场导向文案】不为空时 模板二
        if (StringUtil.isEmpty(cmsBtJmPromotionSaveBean.getExtModel().getEnterGuide()) &&
                cmsBtJmPromotionSaveBean.getExtModel().getIsCheckedBrandLogo() &&
                !StringUtil.isEmpty(cmsBtJmPromotionSaveBean.getExtModel().getBrandLogo())) {
            // 模板二
            index = 1;
        }
        //【品牌Logo】属性不为空 &【入口图专场导向文案】不为空时
        if (!StringUtil.isEmpty(cmsBtJmPromotionSaveBean.getExtModel().getEnterGuide()) &&
                cmsBtJmPromotionSaveBean.getExtModel().getIsCheckedBrandLogo() &&
                !StringUtil.isEmpty(cmsBtJmPromotionSaveBean.getExtModel().getBrandLogo())) {
            //活动类型是大促专场或资源位大促专场 模板四
            if ("3".equals(cmsBtJmPromotionSaveBean.getModel().getPromotionType())
                    || "4".equals(cmsBtJmPromotionSaveBean.getModel().getPromotionType())) {
                //模板三
                index = 3;
            } else {
                //模板四
                index = 2;
            }
        }
        if (!ListUtils.isNull(cmsBtJmImageTemplateModel.getTemplateUrls().get(index).getParameters())) {
            //根据imageType在cms_bt_jm_image_template取得相应的参数
            paramString += "," + cmsBtJmImageTemplateModel.getTemplateUrls().get(index).getParameters().stream().collect(Collectors.joining(","));
            //直邮类型信息
            if (cmsBtJmImageTemplateModel.getTemplateUrls().get(index).getParameters().contains("extModel.directmailType")) {
                CmsMtJmConfigModel cmsMtJmConfigModel = cmsMtJmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.directmailType);
                Map<String, Object> value = cmsMtJmConfigModel.getValues().stream().filter(objectObjectMap -> objectObjectMap.get("value").toString().equalsIgnoreCase(cmsBtJmPromotionSaveBean.getExtModel().getDirectmailType())).findFirst().orElse(null);
                if (value != null) {
                    cmsBtJmPromotionSaveBean.getExtModel().setDirectmailType(value.get("name").toString());
                }
            }
        }
        return String.format(cmsBtJmImageTemplateModel.getTemplateUrls().get(index).getUrl(), getParamsObject(cmsBtJmImageTemplateModel, cmsBtJmPromotionSaveBean, paramString, index));

    }

    /**
     * 取得模板传出对应的参数
     *
     * @param cmsBtJmImageTemplateModel
     * @param cmsBtJmPromotionSaveBean
     * @param paramString
     * @return paramsObject
     */
    public Object[] getParamsObject(CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel, CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean, String paramString, int index) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("new Object[]{" + paramString + "}");
        StandardEvaluationContext context = new StandardEvaluationContext(cmsBtJmPromotionSaveBean);
        Object[] paramsObject = expression.getValue(context, Object[].class);
        try {
            Integer activityEnd = cmsBtJmImageTemplateModel.getTemplateUrls().get(index).getParameters().indexOf("model.activityEnd");

            for (int i = 0; i < paramsObject.length; i++) {
                if (paramsObject[i] instanceof Date) {

                    // 结束时期如果是 10:00:00 或者是9:59:59 的场合图片模板里面的日期不带小时的场合 日期减一天
                    String dateFormat = StringUtil.isEmpty(cmsBtJmImageTemplateModel.getDateFormat()) ? "M.dd" : cmsBtJmImageTemplateModel.getDateFormat();
                    if (i - 1 == activityEnd && !dateFormat.contains("H")) {

                        Date temp = (Date) paramsObject[i];
                        String date = DateTimeUtil.format(temp, null);

                        if (date.contains("9:59:59") || date.contains("10:00:00")) {
                            temp.setTime(temp.getTime() - (24 * 3600 * 1000));
                            paramsObject[i] = temp;
                        }
                    }
                    paramsObject[i] = DateTimeUtil.format((Date) paramsObject[i], dateFormat);

                } else if (paramsObject[i] == null) {
                    paramsObject[i] = "";
                }
                paramsObject[i] = URLEncoder.encode(paramsObject[i].toString(), "UTF-8");
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return paramsObject;
    }

    /**
     * 根据promotionId在数据库中取得对应的Url-----专场飘窗图
     *
     * @return WindowTemplateUrl
     */
    public List<String> getBayWindowTemplateUrls() {
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = getJMImageTemplateByType("bayWindow");
        return cmsBtJmImageTemplateModel.getTemplateUrls().stream().map(template -> template.getUrl()).collect(Collectors.toList());
    }

    /**
     * 根据promotionId在数据库中取得对应的Url-----专场分隔栏图
     *
     * @param modeName
     * @return SeparatorBarUrl
     */
    public String getSeparatorBar(String modeName) {
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = getJMImageTemplateByType("separatorBar");
        try {
            modeName = URLEncoder.encode(modeName, "UTF-8");
        } catch (Exception ignored) {
        }
        return String.format(cmsBtJmImageTemplateModel.getTemplateUrls().get(0).getUrl(), modeName);
    }

    /**
     * 取得聚美活动信息
     *
     * @param jmPromotionId 聚美活动ID (对照表cms_bt_jm_promotion.id)
     * @param hasExtInfo    是否取得专场信息和促销信息
     * @return CmsBtJmPromotionSaveBean
     */
    public CmsBtJmPromotionSaveBean getEditModel(int jmPromotionId, boolean hasExtInfo) {
        CmsBtJmPromotionSaveBean info = new CmsBtJmPromotionSaveBean();
        CmsBtJmPromotionModel model = cmsBtJmPromotionDao.select(jmPromotionId);
        if (model == null) {
            return info;
        }
        info.setModel(model);
        if (model.getRefTagId() != null && model.getRefTagId() != 0) {
            List<CmsBtTagModel> tagModelList = tagService.getListByParentTagId(model.getRefTagId());
            List<CmsBtJmPromotionSaveBean.Tag> tagList = tagModelList.stream().map(CmsBtJmPromotionSaveBean.Tag::new).collect(Collectors.toList());
            info.setTagList(tagList);
        }

        // 取得扩展信息
        if (hasExtInfo) {
            // 活动详情编辑
            info.setExtModel(jmPromotionExtensionDaoExt.selectOne(jmPromotionId));
        }

        return info;
    }
}
