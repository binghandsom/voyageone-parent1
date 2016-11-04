package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */

@RestController
@RequestMapping(
        value  = CmsUrlConstants.POP.FIELD_EDIT.ROOT,
        method = RequestMethod.POST
)
public class CmsFieldEditController extends CmsController {

    @Autowired
    private CmsFieldEditService fieldEditService;

    /**
     * 获取pop画面options.
     */
    @RequestMapping(CmsUrlConstants.POP.FIELD_EDIT.GET_POP_OPTIONS)
    public AjaxResponse getPopOptions(){
        List<CmsMtCommonPropDefModel> result = fieldEditService.getPopOptions(getLang(), getUser().getSelChannelId());
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.POP.FIELD_EDIT.GET_PLATFROM_POP_OPTIONS)
    public AjaxResponse getPlatfromPopOptions(){
        List<CmsMtCommonPropDefModel> result = fieldEditService.getPopOptions(getLang(), getUser().getSelChannelId());
        return success(result);
    }

    /**
     * 批量修改属性.
     */
    @RequestMapping(CmsUrlConstants.POP.FIELD_EDIT.SET_PRODUCT_FIELDS)
    public AjaxResponse setProductFields(@RequestBody Map<String, Object> params) {
        CmsSessionBean cmsSession = getCmsSession();
        String prop = (String) params.get("_option");
        if (prop != null) {
            if ("approval".equals(prop)) {
                // 商品审批
                Map<String, Object> rs = fieldEditService.setProductApproval(params, getUser(), cmsSession);
                return success(rs);
            } else if ("putonoff".equals(prop)) {
                // 商品上下架
                Map<String, Object> rs = fieldEditService.setProductOnOff(params, getUser(), cmsSession);
                return success(rs);
            } else if ("saleprice".equals(prop)) {
                // 修改最终售价
                Map<String, Object> rs = fieldEditService.setProductSalePrice(params, getUser(), cmsSession);
                return success(rs);
            } else if ("retailprice".equals(prop)) {
                // 指导价变更批量确认
                Map<String, Object> rs = fieldEditService.confirmRetailPrice(params, getUser(), cmsSession);
                return success(rs);
            } else if ("refreshRetailPrice".equals(prop)) {
                // 重新计算指导价
                Map<String, Object> rs = fieldEditService.confirmRetailPrice(params, getUser(), cmsSession);
                return success(rs);
            }
            return success(null);
        }
        Integer cartId = (Integer) params.get("cartId");
        int cartIdVal = 0;
        if (cartId == null) {
            cartIdVal = Integer.valueOf(cmsSession.getPlatformType().get("cartId").toString());
        } else {
            cartIdVal = cartId;
        }

        Map<String, Object> rs = fieldEditService.setProductFields(params, getUser(), cartIdVal, cmsSession);
        return success(rs);
    }

    /**
     * 修改最终售价时，下载未处理的商品code列表
     */
    @RequestMapping(CmsUrlConstants.POP.FIELD_EDIT.DLD_PRODUCT_PROCESALE)
    public ResponseEntity<byte[]> doExport(@RequestParam Map params) {
        String data = null;
        try {
            data = fieldEditService.getCodeFile(getUser());
        } catch (Exception e) {
            $error("创建文件时出错", e);
            throw new BusinessException("4001");
        }
        if (data == null) {
            $warn("创建文件时出错,文件内容为空");
            throw new BusinessException("4000");
        }
        byte[] byteData = {(byte)0xFF, (byte)0xFE};
        try {
            byteData = ArrayUtils.addAll(byteData, data.getBytes("utf-16le"));
        } catch (UnsupportedEncodingException e) {
            $error("转换编码时出错", e);
            byteData = data.getBytes();
        }
        String fileName = getUser().getUserName() + "_" + DateTimeUtil.getLocalTime(getUserTimeZone()) + ".csv";
        return genResponseEntityFromBytes(MediaType.valueOf("application/csv"), fileName, byteData);
    }

    @RequestMapping(CmsUrlConstants.POP.FIELD_EDIT.BULK_SET_CATEGORY)
    public AjaxResponse bulkSetCategory(@RequestBody Map<String, Object> params) {

        fieldEditService.bulkSetCategory(params,getUser(),getCmsSession());

        return success(true);
    }
}
