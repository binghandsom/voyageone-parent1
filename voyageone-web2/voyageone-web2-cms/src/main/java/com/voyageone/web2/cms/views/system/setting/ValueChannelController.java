package com.voyageone.web2.cms.views.system.setting;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.cms.CmsMtEtkHsCodeService;
import com.voyageone.service.impl.cms.product.ProductPlatformService;
import com.voyageone.service.model.cms.CmsMtEtkHsCodeModel;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author james.li on 2016/8/15.
 * @version 2.0.0
 */
@RestController
@RequestMapping(value = "/cms/system/valueChannel", method = {RequestMethod.POST})
public class ValueChannelController  extends BaseController {
    @Autowired
    private ValueChannelService valueChannelService;
    @Autowired
    private CmsMtEtkHsCodeService cmsMtEtkHsCodeService;
    @Autowired
    private ProductPlatformService productPlatformService;
    @RequestMapping("addHsCode")
    public AjaxResponse addHsCode(@RequestBody Map params) {
        String hsCodes = params.get("hsCodes").toString();
        Integer typeId = (Integer) params.get("typeId");
        String[] hsCodeList = hsCodes.split("\n");
        return success(valueChannelService.addHsCodes(getUser().getSelChannelId(), Arrays.asList(hsCodeList),typeId,getUser().getUserName()));
    }

    @RequestMapping("addEtkHsCode")
    public AjaxResponse addEtkHsCode(@RequestBody Map params) {
        String hsCodes = params.get("hsCodes").toString();
        String[] hsCodeList = hsCodes.split("\n");
        for (int i = 0; i < hsCodeList.length; i++) {
            String  temp[] = hsCodeList[i].split(" ");
            if(temp.length != 2){
                throw new BusinessException("格式不正确");
            }
            String etk[] = temp[1].split(",");
            if(etk.length != 3) throw new BusinessException("格式不正确");
            CmsMtEtkHsCodeModel cmsMtEtkHsCodeModel = new CmsMtEtkHsCodeModel();
            cmsMtEtkHsCodeModel.setHsCode(temp[0].trim());
            cmsMtEtkHsCodeModel.setEtkHsCode(etk[0].trim());
            cmsMtEtkHsCodeModel.setEtkDescription(etk[1].trim());
            cmsMtEtkHsCodeModel.setEtkUnit(etk[2].trim());
            CmsMtEtkHsCodeModel old = cmsMtEtkHsCodeService.getEdcHsCodeByHsCode(cmsMtEtkHsCodeModel.getHsCode());
            if(old == null){
                cmsMtEtkHsCodeModel.setCreated(new Date());
                cmsMtEtkHsCodeModel.setModified(new Date());
                cmsMtEtkHsCodeModel.setCreater(getUser().getUserName());
                cmsMtEtkHsCodeModel.setModifier(getUser().getUserName());
                cmsMtEtkHsCodeService.insertEdcHsCodeByHsCode(cmsMtEtkHsCodeModel);
            }else{
                old.setEtkHsCode(etk[0].trim());
                old.setEtkDescription(etk[1].trim());
                old.setEtkUnit(etk[2].trim());
                old.setModifier(getUser().getUserName());
                old.setModified(new Date());
                cmsMtEtkHsCodeService.updateEdcHsCodeByHsCode(old);
            }
        }
        return success(true);
    }
    @RequestMapping("updateKaoLaNumiid")
    public AjaxResponse updateKaoLaNumiid(@RequestBody Map params) {
        String hsCodes = params.get("hsCodes").toString();
        String[] hsCodeList = hsCodes.split("\n");
        for (int i = 0; i < hsCodeList.length; i++) {
            String  temp[] = hsCodeList[i].split("\t");
            if(temp.length != 2){
                throw new BusinessException("格式不正确");
            }
            String mainProductCode = temp[0];
            String numIId = temp[1];
            //获取到了这两个参数,现在需要将其更新到mongodb数据库
            productPlatformService.updateProductPlatformByMainProductCode(getUser().getSelChannelId(),getUser().getUserName(),mainProductCode,numIId);
        }
        return success(true);
    }
}
