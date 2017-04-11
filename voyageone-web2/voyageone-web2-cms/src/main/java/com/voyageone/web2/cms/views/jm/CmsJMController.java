package com.voyageone.web2.cms.views.jm;

import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模块 jm  针对资源的rest服务
 * <p>
 * create by holysky on 2016/4/20 15:46
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 *
 * @author holysky
 */
@RestController
@RequestMapping(
        value = "/cms/jm/",
        method = RequestMethod.POST
)
public class CmsJMController extends CmsController {
    private final TagService tagService;
    private final CmsJmPromotionService jmPromotionService;

    @Autowired
    public CmsJMController(TagService tagService, CmsJmPromotionService jmPromotionService) {
        this.tagService = tagService;
        this.jmPromotionService = jmPromotionService;
    }

    public static final class AddPromotionParam {
        CmsBtJmPromotionModel promotion = new CmsBtJmPromotionModel();
        List<Map<String, String>> products;
        Double discount = 1.0; //正折扣不是 xxx% off
        Integer priceType = 1; //默认用官方销售价计算
        String tagName;
        String tagId;
        private Integer isSelAll = null;

        Map<String,Object> searchInfo;

        public Map<String, Object> getSearchInfo() {
            return searchInfo;
        }

        public void setSearchInfo(Map<String, Object> searchInfo) {
            this.searchInfo = searchInfo;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public void setPriceType(Integer priceType) {
            this.priceType = priceType;
        }

        public void setPromotion(CmsBtJmPromotionModel p) {
            this.promotion = p;
        }

        public Boolean hasDiscount() {
            return discount != null;
        }

        public CmsBtJmPromotionModel getPromotion() {
            return promotion;
        }

        public List<Map<String, String>> getProducts() {
            return products;
        }

        public Double getDiscount() {
            return discount;
        }

        public Integer getPriceType() {
            return priceType;
        }

        public void setDiscount(Double discount) {
            this.discount = discount;
        }

        public void setProducts(List<Map<String, String>> products) {
            this.products = products;
        }

        public List<Long> getProductIds() {
            return products.stream().map(bean -> {
                return Long.valueOf(bean.get("id"));
            }).collect(Collectors.toList());
        }

        public Integer getIsSelAll() {
            return isSelAll;
        }

        public void setIsSelAll(Integer isSelAll) {
            this.isSelAll = isSelAll;
        }
    }

    /**
     * 高级检索画面，批量添加到JM活动
     */
    @RequestMapping("promotion/product/add")
    public AjaxResponse doProductAdd(@RequestBody AddPromotionParam param) {
        UserSessionBean user = getUser();
        Map<String, Object> response = jmPromotionService.addProductionToPromotion(param.getProductIds(), param.promotion, user.getSelChannelId(),
                param.discount, param.priceType, param.tagName, param.tagId, param.getIsSelAll(), param.getSearchInfo());
        return success(response);
    }

    @RequestMapping("discounts")
    public AjaxResponse getDiscount() {
        return success(true);
    }

    @RequestMapping("promotion/product/getPromotionTags")
    public AjaxResponse getPromotionTags(@RequestBody Map<String, Object> params) {

        //fix error by holysky
        int tag_id = Integer.parseInt(String.valueOf(params.get("refTagId")));

        return success(tagService.getListByParentTagId(tag_id));
    }
}
