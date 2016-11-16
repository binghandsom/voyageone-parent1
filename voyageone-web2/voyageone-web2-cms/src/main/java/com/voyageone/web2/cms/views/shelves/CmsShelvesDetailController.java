package com.voyageone.web2.cms.views.shelves;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/15.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.SHELVES.DETAIL.ROOT)
public class CmsShelvesDetailController extends CmsController {
    private final CmsBtShelvesService cmsBtShelvesService;
    private final CmsShelvesDetailService cmsShelvesDetailService;
    private final CmsAdvanceSearchService advanceSearchService;
    private final CmsBtShelvesProductService cmsBtShelvesProductService;

    @Autowired
    public CmsShelvesDetailController(CmsShelvesDetailService cmsShelvesDetailService, CmsBtShelvesService cmsBtShelvesService, CmsAdvanceSearchService advanceSearchService, CmsBtShelvesProductService cmsBtShelvesProductService) {
        this.cmsShelvesDetailService = cmsShelvesDetailService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.advanceSearchService = advanceSearchService;
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.SEARCH)
    public AjaxResponse search(@RequestBody Map<String, Object> params) {
        params.put("channelId",getUser().getSelChannelId());
        params.put("active", CmsBtShelvesModelActive.ACTIVATE);
        return success(cmsBtShelvesService.selectList(params));
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.ADD_PRODUCT)
    public AjaxResponse addProduct(@RequestBody Map<String, Object> params){
        Integer shelvesId = (Integer) params.get("shelvesId");
        List<String> productCodes = (List<String>) params.get("productCodes");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(getUser().getSelChannelId(), getCmsSession());
        }
        if (productCodes == null || productCodes.isEmpty()) {

            new BusinessException("批量修改商品属性 没有code条件 params=" + params.toString());
        }
        cmsShelvesDetailService.addProducts(shelvesId, productCodes, getUser().getUserName());
        return success(true);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.GET_SHELVES_INFO)
    public AjaxResponse getShelvesInfo(@RequestBody  Map<String, Object> params){
        List<Integer> shelvesIds = (List<Integer>) params.get("shelvesIds");
        Boolean isLoadPromotionPrice = params.get("isLoadPromotionPrice") == null?false: (Boolean) params.get("isLoadPromotionPrice");
        return success(cmsShelvesDetailService.getShelvesInfo(getUser().getSelChannelId(), shelvesIds,isLoadPromotionPrice));
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.CREATE_SHELVES)
    public AjaxResponse createShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel){
        cmsBtShelvesModel.setChannelId(getUser().getSelChannelId());
        cmsBtShelvesModel.setActive(CmsBtShelvesModelActive.ACTIVATE);
        cmsBtShelvesModel.setCreater(getUser().getUserName());
        cmsBtShelvesModel.setModifier(getUser().getUserName());
        cmsBtShelvesModel.setCreated(new Date());
        cmsBtShelvesModel.setModified(new Date());

        Map<String,Object> map = new HashedMap();
        map.put("channelId",cmsBtShelvesModel.getChannelId());
        map.put("cartId", cmsBtShelvesModel.getCartId());
        map.put("shelvesName", cmsBtShelvesModel.getShelvesName());
        List<CmsBtShelvesModel> cmsBtShelvesModels = cmsBtShelvesService.selectList(map);
        if(cmsBtShelvesModels != null){
            throw new BusinessException("该货架名称已存在");
        }

        cmsBtShelvesService.insert(cmsBtShelvesModel);
        return success(cmsBtShelvesModel);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.UPDATE_SHELVES)
    public AjaxResponse updateShelves(@RequestBody CmsBtShelvesModel cmsBtShelvesModel){
        cmsBtShelvesModel.setModifier(getUser().getUserName());
        cmsBtShelvesModel.setModified(new Date());

        Map<String,Object> map = new HashedMap();
        map.put("channelId",cmsBtShelvesModel.getChannelId());
        map.put("cartId", cmsBtShelvesModel.getCartId());
        map.put("shelvesName", cmsBtShelvesModel.getShelvesName());
        List<CmsBtShelvesModel> cmsBtShelvesModels = cmsBtShelvesService.selectList(map);
        if(cmsBtShelvesModels != null){
            if(cmsBtShelvesModels.stream().filter(cmsBtShelvesModel1 -> cmsBtShelvesModel1.getId() != cmsBtShelvesModel.getId()).count() > 0L){
                throw new BusinessException("该货架名称已存在");
            }
        }
        cmsBtShelvesService.insert(cmsBtShelvesModel);
        return success(cmsBtShelvesModel);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.UPDATE_PRODUCT_SORT)
    public AjaxResponse updateProductSort(@RequestBody List<CmsBtShelvesProductModel> cmsBtShelvesProductModels){
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductModel -> cmsBtShelvesProductModel.setModifier(getUser().getUserName()));
        cmsBtShelvesProductService.updateSort(cmsBtShelvesProductModels);
        return success(true);
    }
}
