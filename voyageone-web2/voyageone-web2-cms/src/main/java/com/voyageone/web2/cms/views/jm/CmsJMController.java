package com.voyageone.web2.cms.views.jm;

import com.google.gson.Gson;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模块 jm  针对资源的rest服务
 *
 * @description
 * @author: holysky
 * @date: 2016/4/20 15:46
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@RestController
@RequestMapping(
        value = "/cms/jm/",
        method = RequestMethod.POST
)
public class CmsJMController extends CmsController {
    private static final Logger log = LoggerFactory.getLogger(CmsJMController.class);
    @Resource
    CmsBtJmPromotionService service;

    @Autowired
    private TagService tagService;

    public static final class AddPromotionParam {


        CmsBtJmPromotionModel promotion = new CmsBtJmPromotionModel();
        List<Map<String, String>> products;
        Double discount=1.0; //正折扣不是 xxx% off
        Integer priceType=1; //默认用官方销售价计算
        String tagName;
        String tagId;

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

    }


    @RequestMapping("promotion/product/add")
    public AjaxResponse doProductAdd(@RequestBody AddPromotionParam param) {

        UserSessionBean user = getUser();
        try {
            service.addProductionToPromotion(param.getProductIds(), param.promotion, user.getSelChannelId(),
                    param.discount,
                    param.priceType,
                    param.tagName,
                    param.tagId,
                    user.getUserName());
            return success(true);
        } catch (Exception e) {
            log.error("LOG00030:添加产品到聚美活动失败:参数"+new Gson().toJson(param), e);
            throw new BusinessException("添加产品到聚美活动失败,原因为:"+e.getMessage(), e);
        }
    }


    @RequestMapping("discounts")
    public AjaxResponse getDiscount() {
        return success(true);
    }

    @RequestMapping("promotion/product/getPromotionTags")
    public AjaxResponse getPromotionTags(@RequestBody Map<String, Object> params){

        //fix error by holysky
        int tag_id = Integer.parseInt(String.valueOf(params.get("refTagId")));

        return success(tagService.getListByParentTagId(tag_id));
    }

}
