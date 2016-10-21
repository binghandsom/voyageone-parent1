package com.voyageone.service.impl.cms.jumei;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.mongo.CmsBtJmImageTemplateDao;
import com.voyageone.service.model.cms.mongo.CmsBtJmImageTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    private final CmsBtJmPromotionService cmsBtJmPromotionService;

    @Autowired
    public CmsBtJmImageTemplateService(CmsBtJmImageTemplateDao cmsBtJmImageTemplateDao, CmsBtJmPromotionService cmsBtJmPromotionService) {
        this.cmsBtJmImageTemplateDao = cmsBtJmImageTemplateDao;
        this.cmsBtJmPromotionService = cmsBtJmPromotionService;
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
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(jmPromotionId, true);
        return getUrl(imageName, imageType, cmsBtJmPromotionSaveBean);
    }

    /**
     * 根据jmPromotionId 和图片类型 返回 带模板的url地址
     */
    public String getUrl(String imageName, String imageType, CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean) {
        CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = getJMImageTemplateByType(imageType);
        String paramString = "\"" + imageName + "\"";
        if (cmsBtJmImageTemplateModel.getParameters() != null && cmsBtJmImageTemplateModel.getParameters().size() > 0) {
            paramString += "," + cmsBtJmImageTemplateModel.getParameters().stream().collect(Collectors.joining(","));
        }
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("new Object[]{" + paramString + "}");

        StandardEvaluationContext context = new StandardEvaluationContext(cmsBtJmPromotionSaveBean);

        try {
            Object[] paramsObject = expression.getValue(context, Object[].class);
            for (int i = 0; i < paramsObject.length; i++) {
                if (paramsObject[i] instanceof Date) {
                    paramsObject[i] = DateTimeUtil.format((Date) paramsObject[i], "M.d");
                }
            }
            return String.format(cmsBtJmImageTemplateModel.getTemplateUrls().get(0), paramsObject);
        } catch (SpelEvaluationException ignored) {
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
}
