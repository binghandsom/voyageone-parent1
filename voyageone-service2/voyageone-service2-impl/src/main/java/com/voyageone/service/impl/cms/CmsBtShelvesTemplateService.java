package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.dao.cms.CmsBtShelvesTemplateDao;
import com.voyageone.service.daoext.cms.CmsBtShelvesTemplateDaoExt;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelActive;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelClientType;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelTemplateType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by rex.wu on 2016/11/11.
 */
@Service
public class CmsBtShelvesTemplateService extends BaseService {

    @Autowired
    private CmsBtShelvesTemplateDao cmsBtShelvesTemplateDao;
    @Autowired
    private CmsBtShelvesTemplateDaoExt cmsBtShelvesTemplateDaoExt;

    public void insert(CmsBtShelvesTemplateModel template, String user, String channelId) {
        checkModel(template, "add");
        template.setChannelId(channelId);
        template.setCreater(user);
        template.setCreated(new Date());
        cmsBtShelvesTemplateDao.insert(template);

    }

    public void update(CmsBtShelvesTemplateModel template, String user) {
        checkModel(template, "update");
        template.setModifier(user);
        template.setModified(new Date());
        cmsBtShelvesTemplateDao.update(template);
    }

    public CmsBtShelvesTemplateModel select(Integer id) {
        if (id != null)
            return cmsBtShelvesTemplateDao.select(id);
        return null;
    }

    public void delete(Integer id) {
        if (id != null)
            cmsBtShelvesTemplateDao.delete(id);
    }

    /**
     * 新增或修改时校验对象
     * @param template
     * @param operType
     */
    public void checkModel(CmsBtShelvesTemplateModel template, String operType) {
        Integer id = template.getId();
        String templateName = template.getTemplateName();
        Integer templateType = template.getTemplateType();
        Integer clientType = template.getClientType();
        Integer cartId = template.getCartId();
        String channelId = template.getChannelId();
        if (templateType == null || !CmsBtShelvesTemplateModelTemplateType.KV.containsKey(templateType)) {
            throw new BusinessException("请选择模板类型！");
        }
        if (clientType == null || !CmsBtShelvesTemplateModelClientType.KV.containsKey(clientType)) {
            throw new BusinessException("请选择客户端类型！");
        }
        if (cartId == null) { // TODO 校验平台类型ID是否存在
            throw new BusinessException("请选择平台类型！");
        }
        if (StringUtils.isBlank(templateName) || templateName.length() > 255) {
            throw new BusinessException("模板名称为空或输入值过长！");
        }
        if ("add".equals(operType)) {
            template.setId(null);
        } else if ("update".equals(operType)) {
            CmsBtShelvesTemplateModel targetTemplate = null;
            if (id == null || (targetTemplate = cmsBtShelvesTemplateDao.select(id)) == null) {
                throw new BusinessException("查询不到待编辑模板，请先选择正确的模板！");
            }
        }
        // 模板类型不同，字段值不同
        if (templateType.intValue() == CmsBtShelvesTemplateModelTemplateType.LAYOUT) {
            template.setHtmlLastImage("");
            template.setHtmlImageTemplate("");
        }else {
            template.setHtmlHead("");
            template.setHtmlModuleTitle("");
            template.setHtmlModuleSearch("");
            template.setHtmlClearfix1("");
            template.setHtmlClearfix2("");
            template.setHtmlBigImage("");
            template.setHtmlFoot("");
        }
    }

    /**
     * 查询货架模板
     * @param searchBean
     * @return
     */
    public List<CmsBtShelvesTemplateModel> search(CmsBtShelvesTemplateBean searchBean) {
        return cmsBtShelvesTemplateDaoExt.search(searchBean);
    }

    /**
     * 逻辑删除货架模板
     * @param templateId
     */
    public void inactive(String templateId, String user, String channelId) {
        Integer id = null;
        CmsBtShelvesTemplateModel theone = null;
        if (StringUtils.isNotBlank(templateId)) {
            id = Integer.parseInt(templateId);
        }
        if (id == null || (theone = cmsBtShelvesTemplateDao.select(id)) == null) {
            throw new BusinessException("要删除的模板不存在！");
        }
        String channel = theone.getChannelId();
        if (!channel.equals(channelId)) {
            throw new BusinessException("要删除的模板并不在当前店铺中！");
        }
        CmsBtShelvesTemplateModel target = new CmsBtShelvesTemplateModel();
        target.setId(theone.getId());
        target.setModified(new Date());
        target.setModifier(user);
        target.setActive(CmsBtShelvesTemplateModelActive.DEACTIVATE);
        cmsBtShelvesTemplateDao.update(target);
    }

    public CmsBtShelvesTemplateModel selectById(Integer templateId){
        CmsBtShelvesTemplateModel target = null;
        if (templateId == null || (target = cmsBtShelvesTemplateDao.select(templateId)) == null) {
            throw new BusinessException("模板不存在！");
        }
        return target;
    }

}
