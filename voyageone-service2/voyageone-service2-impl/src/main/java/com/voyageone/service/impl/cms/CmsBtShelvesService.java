package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.bean.cms.CmsBtShelvesProductBean;
import com.voyageone.service.dao.cms.CmsBtShelvesDao;
import com.voyageone.service.dao.cms.CmsBtShelvesTemplateDao;
import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtShelvesExample;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import com.voyageone.service.model.cms.enums.PlatformType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
    private final CmsBtShelvesProductService cmsBtShelvesProductService;
    @Autowired
    private ProductService productService;

    @Autowired
    public CmsBtShelvesService(CmsBtShelvesDao cmsBtShelvesDao, CmsBtShelvesTemplateDao cmsBtShelvesTemplateDao, CmsBtShelvesProductService cmsBtShelvesProductService) {
        this.cmsBtShelvesDao = cmsBtShelvesDao;
        this.cmsBtShelvesTemplateDao = cmsBtShelvesTemplateDao;
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
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
     * <p>
     * create by rex.wu
     */
    public String generateHtml(Integer shelvesId, boolean preview) {
        CmsBtShelvesModel shelves;
        if (shelvesId != null && (shelves = cmsBtShelvesDao.select(shelvesId)) != null) {
            CmsBtShelvesTemplateModel layoutTemplate = cmsBtShelvesTemplateDao.select(shelves.getLayoutTemplateId());
            CmsBtShelvesTemplateModel singleTemplate = cmsBtShelvesTemplateDao.select(shelves.getSingleTemplateId());

            CmsBtShelvesInfoBean cmsBtShelvesInfoBean = cmsBtShelvesProductService.getShelvesInfo(shelves, true);
            List<CmsBtShelvesProductModel> products = cmsBtShelvesInfoBean == null ? null : cmsBtShelvesInfoBean.getShelvesProductModels();
            if (CollectionUtils.isNotEmpty(products)) {
                List<CmsBtShelvesProductModel> filterModels = new ArrayList<CmsBtShelvesProductModel>();
                for (CmsBtShelvesProductModel product:products) {
                    if (org.apache.commons.lang.StringUtils.isBlank(product.getNumIid())) {
                        filterModels.add(product);
                    }
                }
                if (filterModels.size() > 0) {
                    products.removeAll(filterModels);
                }
            }

            if (layoutTemplate == null || singleTemplate == null || CollectionUtils.isEmpty(products)) {
                throw new BusinessException("货架没有关联布局模板或单品模板或商品！");
            }
            StringBuilder htmlBuffer = new StringBuilder(); // html容器
            htmlBuffer.append(layoutTemplate.getHtmlHead()); // 布局模板：头部
            htmlBuffer.append(layoutTemplate.getHtmlClearfix1()); // 布局模板：清除浮动

            int len = products.size();
            int numPerLine = layoutTemplate.getNumPerLine();
            CmsBtShelvesProductBean productBean = null;
            for (int i = 1; i <= len; i++) {
                productBean = (CmsBtShelvesProductBean) products.get(i - 1);
                String singleHtml = "";
                if (i % numPerLine == 0) { // 追加最后一个小图模块
                    singleHtml = singleTemplate.getHtmlLastImage();
                } else { // 追加小图模块
                    singleHtml = singleTemplate.getHtmlSmallImage();
                }
                // TODO 暂时测试，天猫平台商品详情页链接
                ShopBean shopBean = Shops.getShop(shelves.getChannelId(), shelves.getCartId());
                if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.TMALL.getPlatformId().toString())) {
                    singleHtml = singleHtml.replace("@link", "https://detail.tmall.hk/hk/item.htm?id=" + productBean.getNumIid()); // 根据商品在平台ID拼接商品详情页
                } else if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.JD.getPlatformId().toString())) {
                    String jdskuid = "";
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(shelves.getChannelId(), productBean.getProductCode());
                    if(cmsBtProductModel != null) {
                        CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getPlatform(shelves.getCartId());
                        BaseMongoMap<String, Object> jdsku = platform.getSkus().stream().filter(sku -> !StringUtil.isEmpty(sku.getStringAttribute("jdSkuId"))).findFirst().orElse(null);
                        if (jdsku != null) {
                            jdskuid = jdsku.getStringAttribute("jdSkuId");
                        }
                    }
                    singleHtml = singleHtml.replace("@link", "https://item.jd.hk/" + jdskuid+".html"); // 根据商品在平台ID拼接商品详情页
                }
                if (!preview) {

                    if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.JD.getPlatformId().toString())) {
                        singleHtml = singleHtml.replace("@imglink", "//img10.360buyimg.com/imgzone/"+ productBean.getPlatformImageUrl()); // 单品模板生成图片在平台的地址
                    }else{
                        singleHtml = singleHtml.replace("@imglink", productBean.getPlatformImageUrl()); // 单品模板生成图片在平台的地址
                    }
                } else {
                    // 单品模板的图片模板来生成图片html
                    String htmlImageTemplate = singleTemplate.getHtmlImageTemplate();
                    if (htmlImageTemplate.contains("@price")) {
                        htmlImageTemplate = htmlImageTemplate.replaceAll("@price", String.valueOf(productBean.getSalePrice().intValue()));
                    }
                    if (htmlImageTemplate.contains("@img")) {
                        htmlImageTemplate = htmlImageTemplate.replaceAll("@img", productBean.getImage());
                    }
                    if (htmlImageTemplate.contains("@name")) {
                        try {
                            htmlImageTemplate = htmlImageTemplate.replaceAll("@name", URLEncoder.encode(productBean.getProductName(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            throw new BusinessException("编码商品名称出错了！");
                        }
                    }
                    if (htmlImageTemplate.contains("@sale_price")) {
                        htmlImageTemplate = htmlImageTemplate.replaceAll("@sale_price", String.valueOf(productBean.getPromotionPrice().intValue()));
                    }
                    //singleHtml.replaceAll("@imglink", htmlImageTemplate); // Illegal group reference
                    int index = singleHtml.indexOf("@imglink");
                    int length = singleHtml.length();
                    singleHtml = singleHtml.substring(0, index) + htmlImageTemplate + singleHtml.substring(index + 8, length);
                }
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

        String fileName = String.format("%s/shelves%d", CmsBtShelvesProductService.getShelvesImagePath(), cmsBtShelvesModel.getId());
        try {
            FileUtils.deleteAllFilesOfDir(new File(fileName));
        } catch (Exception ignored) {
        }
    }
}
