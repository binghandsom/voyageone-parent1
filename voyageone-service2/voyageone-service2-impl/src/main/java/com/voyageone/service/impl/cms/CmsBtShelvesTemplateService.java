package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.dao.cms.CmsBtShelvesTemplateDao;
import com.voyageone.service.daoext.cms.CmsBtShelvesDaoExt;
import com.voyageone.service.daoext.cms.CmsBtShelvesProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtShelvesTemplateDaoExt;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelActive;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelClientType;
import com.voyageone.service.fields.cms.CmsBtShelvesTemplateModelTemplateType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by rex.wu on 2016/11/11.
 */
@Service
public class CmsBtShelvesTemplateService extends BaseService {

    private static final String SHELVES_ADD = "add";
    private static final String SHELVES_EDIT = "edit";

    private final CmsBtShelvesTemplateDao cmsBtShelvesTemplateDao;
    private final CmsBtShelvesTemplateDaoExt cmsBtShelvesTemplateDaoExt;
    private final CmsBtShelvesDaoExt cmsBtShelvesDaoExt;
    private final CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt;
    private final MqSender sender;

    @Autowired
    public CmsBtShelvesTemplateService(CmsBtShelvesTemplateDao cmsBtShelvesTemplateDao, CmsBtShelvesTemplateDaoExt cmsBtShelvesTemplateDaoExt, CmsBtShelvesDaoExt cmsBtShelvesDaoExt, CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt, MqSender sender) {
        this.cmsBtShelvesTemplateDao = cmsBtShelvesTemplateDao;
        this.cmsBtShelvesTemplateDaoExt = cmsBtShelvesTemplateDaoExt;
        this.cmsBtShelvesDaoExt = cmsBtShelvesDaoExt;
        this.cmsBtShelvesProductDaoExt = cmsBtShelvesProductDaoExt;
        this.sender = sender;
    }

    public void insert(CmsBtShelvesTemplateModel template, String user, String channelId) {
        template.setChannelId(channelId);
        checkModel(template, SHELVES_ADD);
        template.setCreater(user);
        template.setCreated(new Date());
        cmsBtShelvesTemplateDao.insert(template);

    }

    public void update(CmsBtShelvesTemplateModel template, String user) {
        template.setModifier(user);
        template.setModified(new Date());
        checkModel(template, SHELVES_EDIT);
        template.setTemplateType(null); // 模板类型不可更改
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
        String channelId = template.getChannelId();
        String templateName = template.getTemplateName();
        Integer templateType = template.getTemplateType();
        Integer clientType = template.getClientType();
        Integer cartId = template.getCartId();
        Integer numPerLin = template.getNumPerLine();
        String htmlHead = template.getHtmlHead();
        String htmlFoot = template.getHtmlFoot();
        String htmlClearfix1 = template.getHtmlClearfix1();
        String htmlClearfix2 = template.getHtmlClearfix2();
        String htmlSmallImage = template.getHtmlSmallImage();
        String htmlLastImage = template.getHtmlLastImage();
        String htmlImageTemplate = template.getHtmlImageTemplate();
        if (StringUtils.isBlank(templateName) || templateName.length() > 255) {
            throw new BusinessException("模板名称为空或输入值过长！");
        }
        if (clientType == null || !CmsBtShelvesTemplateModelClientType.KV.containsKey(clientType)) {
            throw new BusinessException("请选择客户端类型！");
        }
        if (cartId == null) { // TODO 校验平台类型ID是否存在
            throw new BusinessException("请选择平台类型！");
        }
        Map<String,Object> param = new HashedMap();
        param.put("channelId", channelId);
        param.put("templateName", templateName);
        CmsBtShelvesTemplateModel existent = cmsBtShelvesTemplateDaoExt.selectByChannelIdAndName(param);
        CmsBtShelvesTemplateModel targetTemplate = null;
        if (SHELVES_ADD.equals(operType)) {
            if (templateType == null || !CmsBtShelvesTemplateModelTemplateType.KV.containsKey(templateType)) {
                throw new BusinessException("请选择模板类型！");
            }
            if (existent != null) {
                throw new BusinessException("模板名称已被占用！");
            }
        } else if (SHELVES_EDIT.equals(operType)) {
            if (id == null || (targetTemplate = cmsBtShelvesTemplateDao.select(id)) == null) {
                throw new BusinessException("模板不存在，请先选择正确的模板！");
            }
            if (existent != null && existent.getId().intValue() != id) {
                throw new BusinessException("模板名称已被占用！");
            }
            template.setTemplateType(targetTemplate.getTemplateType());
        }
        // 根据不同的模板类型，校验不同的属性值
        if (templateType.intValue() == CmsBtShelvesTemplateModelTemplateType.LAYOUT) {
            if (numPerLin == null || numPerLin.intValue() < 1) {
                throw new BusinessException("请输入布局模板每行个数!");
            }
            if (StringUtils.isBlank(htmlHead) || StringUtils.isBlank(htmlFoot) || StringUtils.isBlank(htmlClearfix1) || StringUtils.isBlank(htmlClearfix2)) {
                throw new BusinessException("请输入布局模板的必填项!");
            }
            template.setHtmlLastImage("");
            template.setHtmlImageTemplate("");
        }else {
            if (StringUtils.isBlank(htmlSmallImage) || StringUtils.isBlank(htmlLastImage) || StringUtils.isBlank(htmlImageTemplate)) {
                throw new BusinessException("请输入单品模板的必填项!");
            }
            template.setHtmlHead("");
            template.setHtmlModuleTitle("");
            template.setHtmlModuleSearch("");
            template.setHtmlClearfix1("");
            template.setHtmlClearfix2("");
            template.setHtmlBigImage("");
            template.setHtmlFoot("");
        }
        if (SHELVES_EDIT.equals(operType)) {
            String targetHtmlImageTemplate = targetTemplate.getHtmlImageTemplate() == null ? "" : targetTemplate.getHtmlImageTemplate();
            String thisHtmlImageTemplate = template.getHtmlImageTemplate() == null ? "" : template.getHtmlImageTemplate();
            if (!targetHtmlImageTemplate.equals(thisHtmlImageTemplate)) {
                List<CmsBtShelvesModel> shelvesModels = cmsBtShelvesDaoExt.selectByTemplateId(id);
                List<Integer> shelvesIds = null;
                if (CollectionUtils.isNotEmpty(shelvesModels)) {
                    shelvesIds = new ArrayList<Integer>();
//                    param.clear();
//                    for (CmsBtShelvesModel shelves:shelvesModels) {
//                        shelvesIds.add(shelves.getId());
//                        param.put("shelvesId",shelves.getId());
//                        sender.sendMessage(MqRoutingKey.CMS_BATCH_ShelvesImageUploadJob, param);
//                    }
                    if (CollectionUtils.isNotEmpty(shelvesIds)) {
                        param.clear();
                        param.put("shelvesIds", shelvesIds);
                        param.put("modified", template.getModified());
                        param.put("modifier", template.getModifier());
                        cmsBtShelvesProductDaoExt.clearImageByShelvesIds(param);
                    }
                }
            }
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
