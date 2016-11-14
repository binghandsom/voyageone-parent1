package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.dao.cms.CmsBtShelvesTemplateDao;
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

    public void insert(CmsBtShelvesTemplateModel template) {
        checkModel(template, "insert");
        template.setCreated(new Date()); // 设置creater
        cmsBtShelvesTemplateDao.insert(template);

    }

    public void update(CmsBtShelvesTemplateModel template) {
        checkModel(template, "update");
        template.setModified(new Date()); // 设置modifier
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
        if ("update".equals(operType)) {
            CmsBtShelvesTemplateModel targetTemplate = null;
            if (id == null || (targetTemplate = cmsBtShelvesTemplateDao.select(id)) == null) {
                throw new BusinessException("查询不到待编辑模板，请先选择正确的模板！");
            }
        }
        if (StringUtils.isBlank(templateName) || templateName.length() > 255) {
            throw new BusinessException("模板名称为空或输入值过长！");
        }
        if (templateType == null) {
            throw new BusinessException("请选择模板类型！");
        }
        if (clientType == null) {
            throw new BusinessException("请选择客户端类型！");
        }
        if (cartId == null) {
            throw new BusinessException("请选择平台类型！");
        }
        // TODO 校验......
    }

    /**
     * 查询货架模板
     * @param searchBean
     * @return
     */
    private List<CmsBtShelvesTemplateModel> search(CmsBtShelvesTemplateBean searchBean) {
        return cmsBtShelvesTemplateDao.selectList(searchBean);

    }

}
