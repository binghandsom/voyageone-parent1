package com.voyageone.service.impl.cms.jumei;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.CmsBtJmMasterBrandDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.mongo.CmsBtJmImageTemplateDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSpecialExtensionDaoExt;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.CmsBtJmImageTemplateModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsMtJmConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public CmsBtJmImageTemplateService(CmsBtJmImageTemplateDao cmsBtJmImageTemplateDao, CmsBtJmPromotionDao cmsBtJmPromotionDao,TagService tagService,CmsBtJmPromotionSpecialExtensionDaoExt jmPromotionExtensionDaoExt,CmsMtJmConfigService cmsMtJmConfigService) {
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
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = JacksonUtil.json2Bean(JacksonUtil.bean2Json(orgCmsBtJmPromotionSaveBean),CmsBtJmPromotionSaveBean.class);
        boolean isEnterGuide = true;
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = getJMImageTemplateByType(imageType);
        String paramString = "\"" + imageName + "\"";
        if (cmsBtJmImageTemplateModel.getParameters() != null && cmsBtJmImageTemplateModel.getParameters().size() > 0) {

            if(cmsBtJmImageTemplateModel.getParameters().contains("extModel.directmailType")) {
                CmsMtJmConfigModel cmsMtJmConfigModel = cmsMtJmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.directmailType);
                Map<String, Object> value = cmsMtJmConfigModel.getValues().stream().filter(objectObjectMap -> objectObjectMap.get("value").toString().equalsIgnoreCase(cmsBtJmPromotionSaveBean.getExtModel().getDirectmailType())).findFirst().orElse(null);
                if (value != null) {
                    cmsBtJmPromotionSaveBean.getExtModel().setDirectmailType(value.get("name").toString());
                }
            }

            if(StringUtil.isEmpty(cmsBtJmPromotionSaveBean.getExtModel().getEnterGuide())){
                cmsBtJmImageTemplateModel.getParameters().remove("extModel.enterGuide");
                isEnterGuide=false;
            }
            paramString += "," + cmsBtJmImageTemplateModel.getParameters().stream().collect(Collectors.joining(","));
        }
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("new Object[]{" + paramString + "}");

        StandardEvaluationContext context = new StandardEvaluationContext(cmsBtJmPromotionSaveBean);

        try {
            Integer activityEnd = cmsBtJmImageTemplateModel.getParameters().indexOf("model.activityEnd");
            Object[] paramsObject = expression.getValue(context, Object[].class);
            for (int i = 0; i < paramsObject.length; i++) {
                if (paramsObject[i] instanceof Date) {
                    String dateFormat = StringUtil.isEmpty(cmsBtJmImageTemplateModel.getDateFormat())?"M.dd":cmsBtJmImageTemplateModel.getDateFormat();
                    if(i-1 == activityEnd && dateFormat.indexOf("H") == -1){

                        Date temp =(Date) paramsObject[i];
                        String date = DateTimeUtil.format(temp, null);

                        if(date.indexOf("9:59:59")> -1 || date.indexOf("10:00:00") > -1){
                            temp.setTime(temp.getTime()-(24*3600*1000));
                            paramsObject[i] = temp;
                        }
                    }
                    paramsObject[i] = DateTimeUtil.format((Date) paramsObject[i], dateFormat);

                }else if(paramsObject[i]  == null){
                    paramsObject[i] = "";
                }
                paramsObject[i] = URLEncoder.encode(paramsObject[i].toString(),"UTF-8");
            }
            if(isEnterGuide){
                return String.format(cmsBtJmImageTemplateModel.getTemplateUrls().get(1), paramsObject);
            }else{
                return String.format(cmsBtJmImageTemplateModel.getTemplateUrls().get(0), paramsObject);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有飘窗模板地址
     * @since 2.8.0
     */
    public List<String> getBayWindowTemplateUrls() {
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = getJMImageTemplateByType("bayWindow");
        return cmsBtJmImageTemplateModel.getTemplateUrls();
    }

    public String getSeparatorBar(String modeName) {
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = getJMImageTemplateByType("separatorBar");
        return String.format(cmsBtJmImageTemplateModel.getTemplateUrls().get(0), modeName);
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
            List<CmsBtTagModel> tagList = tagService.getListByParentTagId(model.getRefTagId());
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
