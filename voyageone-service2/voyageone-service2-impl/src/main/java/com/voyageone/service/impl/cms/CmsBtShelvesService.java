package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.cms.CmsBtShelvesDao;
import com.voyageone.service.dao.cms.CmsBtShelvesTemplateDao;
import com.voyageone.service.daoext.cms.CmsBtShelvesProductDaoExt;
import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesExample;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@Service
public class CmsBtShelvesService extends BaseService {
    private final CmsBtShelvesDao cmsBtShelvesDao;
    private final CmsBtShelvesTemplateDao cmsBtShelvesTemplateDao;
    private final CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt;

    @Autowired
    public CmsBtShelvesService(CmsBtShelvesDao cmsBtShelvesDao, CmsBtShelvesTemplateDao cmsBtShelvesTemplateDao, CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt) {
        this.cmsBtShelvesDao = cmsBtShelvesDao;
        this.cmsBtShelvesTemplateDao = cmsBtShelvesTemplateDao;
        this.cmsBtShelvesProductDaoExt = cmsBtShelvesProductDaoExt;
    }

    public CmsBtShelvesModel getId(Integer id) {
        return cmsBtShelvesDao.select(id);
    }

    public int update(CmsBtShelvesModel cmsBtShelvesModel) {
        return cmsBtShelvesDao.update(cmsBtShelvesModel);
    }

    public Integer insert(CmsBtShelvesModel cmsBtShelvesModel) {
        cmsBtShelvesModel.setActive(CmsBtShelvesModelActive.ACTIVATE);
        cmsBtShelvesModel.setCreated(new Date());
        cmsBtShelvesModel.setModified(new Date());

        if (!checkName(cmsBtShelvesModel)) {
            throw new BusinessException("该货架名称已存在");
        }

        cmsBtShelvesDao.insert(cmsBtShelvesModel);
        return cmsBtShelvesModel.getId();
    }

    public List<CmsBtShelvesModel> selectList(Map map) {
        return cmsBtShelvesDao.selectList(map);
    }

    public List<CmsBtShelvesModel> selectList(CmsBtShelvesModel exampleModel) {
        return cmsBtShelvesDao.selectList(exampleModel);
    }

    public boolean checkName(CmsBtShelvesModel exampleModel) {

        CmsBtShelvesExample example = new CmsBtShelvesExample();

        // where channelId = ? and cartId = ? and shelvesName = ?
        CmsBtShelvesExample.Criteria criteria = example.createCriteria()
                .andChannelIdEqualTo(exampleModel.getChannelId())
                .andCartIdEqualTo(exampleModel.getCartId())
                .andShelvesNameEqualTo(exampleModel.getShelvesName());

        Integer id = exampleModel.getId();

        // and id != ?
        if (id != null)
            criteria.andIdNotEqualTo(id);

        return cmsBtShelvesDao.countByExample(example) < 1;
    }

    public List<CmsBtShelvesModel> selectByChannelIdCart(String channelId, Integer cartId) {
        CmsBtShelvesModel byChannelAndCart = new CmsBtShelvesModel();
        byChannelAndCart.setChannelId(channelId);
        byChannelAndCart.setCartId(cartId);
        byChannelAndCart.setActive(CmsBtShelvesModelActive.ACTIVATE);
        return selectList(byChannelAndCart);
    }

    /**
     * 根据货架生成HTML代码
     *
     * @param shelvesId
     * @return
     * @author rex.wu
     */
    public String generateHtml(Integer shelvesId) {
        CmsBtShelvesModel shelves = null;
        if (shelvesId != null && (shelves = cmsBtShelvesDao.select(shelvesId)) != null) {
            CmsBtShelvesTemplateModel layoutTemplate = cmsBtShelvesTemplateDao.select(shelves.getLayoutTemplateId());
            CmsBtShelvesTemplateModel singleTemplate = cmsBtShelvesTemplateDao.select(shelves.getSingleTemplateId());
            List<CmsBtShelvesProductModel> products = cmsBtShelvesProductDaoExt.selectByShelvesId(shelvesId);
            if (layoutTemplate == null || singleTemplate == null || CollectionUtils.isEmpty(products)) {
                throw new BusinessException("货架没有关联布局模板或单品模板或商品！");
            }
            StringBuffer htmlBuffer = new StringBuffer(); // html容器
            htmlBuffer.append(layoutTemplate.getHtmlHead()); // 布局模板：头部
            htmlBuffer.append(layoutTemplate.getHtmlClearfix1()); // 布局模板：清除浮动

            int len = products.size();
            int numPerLine = layoutTemplate.getNumPerLine();
            CmsBtShelvesProductModel currentProduct = null;
            for (int i = 1; i <= len; i++) {
                currentProduct = products.get(i - 1);
                String singleHtml = "";
                if (i % numPerLine == 0) { // 追加最后一个小图模块
                    singleHtml = singleTemplate.getHtmlLastImage();
                } else { // 追加小图模块
                    singleHtml = singleTemplate.getHtmlSmallImage();
                }
                // TODO 暂时测试，天猫平台商品详情页链接
                singleHtml.replace("@link", "https://detail.tmall.hk/hk/item.htm?id=" + currentProduct.getNumIid()); // 根据商品在平台ID拼接商品详情页
                singleHtml.replace("@imglink", currentProduct.getPlatformImageUrl()); // 单品模板生成图片在平台的地址
                htmlBuffer.append(singleHtml);
            }

            htmlBuffer.append(layoutTemplate.getHtmlClearfix2()); // 布局模板：清除浮动
            htmlBuffer.append(layoutTemplate.getHtmlFoot()); // 布局模板：尾部
            return htmlBuffer.toString();
        }
        return null;
    }

    public void delete(CmsBtShelvesModel cmsBtShelvesModel) {
        CmsBtShelvesModel example = new CmsBtShelvesModel();
        example.setId(cmsBtShelvesModel.getId());
        example.setActive(CmsBtShelvesModelActive.DEACTIVATE);
        example.setModifier(cmsBtShelvesModel.getModifier());
        example.setModified(new Date());
        update(example);
    }
}
