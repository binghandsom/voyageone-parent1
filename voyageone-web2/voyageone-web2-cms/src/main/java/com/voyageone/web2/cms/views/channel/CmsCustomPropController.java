package com.voyageone.web2.cms.views.channel;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.impl.cms.CmsBtCustomPropService;
import com.voyageone.service.impl.cms.CommonSchemaService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/2/23.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.ROOT, method = RequestMethod.POST)
public class CmsCustomPropController extends CmsController {

    final
    CmsBtCustomPropService cmsBtCustomPropService;

    final
    CommonSchemaService commonSchemaService;

    @Autowired
    public CmsCustomPropController(CmsBtCustomPropService cmsBtCustomPropService, CommonSchemaService commonSchemaService) {
        this.cmsBtCustomPropService = cmsBtCustomPropService;
        this.commonSchemaService = commonSchemaService;
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.INIT)
    public AjaxResponse init(){
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("commonFields",commonSchemaService.getCommonFields());

        if(getUser().getSelChannelId().equals(ChannelConfigEnums.Channel.USJGJ.getId())){
            List<TypeChannelBean> typeChannelBeenList = TypeChannels.getTypeChannelBeansByTypeValueLang(Constants.comMtTypeChannel.SKU_CARTS_53, getUser().getSelChannelId(), "cn");

            List<OrderChannelBean> channelList = new ArrayList<>();

            for (TypeChannelBean typeBean : typeChannelBeenList) {
                OrderChannelBean channelBean = Channels.getChannel(typeBean.getChannel_id());
                if (channelBean != null) {
                    channelList.add(channelBean);
                }
            }

            resultMap.put("channelList", channelList);
        }

        return success(resultMap);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.SEARCH)
    public AjaxResponse search(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.getCustomPropByCatChannelExtend(getUser().getSelChannelId(),orgChannelId,cat);
        return success(cmsBtCustomPropModel);
    }

    // 设置打勾
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.SET_CUSTOMSH_IS_DISPPLAY)
    public AjaxResponse doSetCustomshIsDispPlay(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel.Entity entity = new CmsBtCustomPropModel.Entity((Map) param.get("entity")) ;
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.setCustomshIsDispPlay(getUser().getSelChannelId(),orgChannelId,cat, entity);
        return success(cmsBtCustomPropModel);
    }

    // 更新一个entity的value
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.UPDATE_ENTITY)
    public AjaxResponse doUpdateEntity(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel.Entity entity = new CmsBtCustomPropModel.Entity((Map) param.get("entity")) ;
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.updateEntity(getUser().getSelChannelId(),orgChannelId,cat, entity);
        return success(cmsBtCustomPropModel);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.SET_SORT)
    public AjaxResponse doSetSort(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        List<String> sort = (List<String>) param.get("sort");
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.setSort(getUser().getSelChannelId(),orgChannelId,cat, sort);
        return success(cmsBtCustomPropModel);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.DELETE)
    public AjaxResponse doRemoveEntity(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel.Entity entity = new CmsBtCustomPropModel.Entity((Map) param.get("entity")) ;
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.removeEntity(getUser().getSelChannelId(),orgChannelId,cat, entity);
        return success(cmsBtCustomPropModel);
    }
}
