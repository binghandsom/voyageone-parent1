package com.voyageone.service.impl.cms.vomqjobservice;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductFreeTagsUpdateMQMessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by dell on 2017/1/12.
 */
@Service
public class CmsProductFreeTagsUpdateService extends BaseService {

    @Autowired
    CmsMqSenderService cmsMqSenderService;

//    @Autowired
//    CmsAdvanceSearchService cmsAdvanceSearchService;

    public void sendMessage(String chanelId, CmsSearchInfoBean2 searchValue, String sender) throws MQMessageRuleException {
        CmsProductFreeTagsUpdateMQMessageBody mqMessageBody = new CmsProductFreeTagsUpdateMQMessageBody();
        mqMessageBody.setChannelId(chanelId);
        mqMessageBody.setIsSelAll(true);
        mqMessageBody.setSearchValue(searchValue);
        mqMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(mqMessageBody);
    }


    public void sendMessage(String chanelId, List<String> prodCodeList, String sender) throws MQMessageRuleException {
        CmsProductFreeTagsUpdateMQMessageBody mqMessageBody = new CmsProductFreeTagsUpdateMQMessageBody();
        mqMessageBody.setChannelId(chanelId);
        mqMessageBody.setIsSelAll(false);
        mqMessageBody.setProdCodeList(prodCodeList);
        mqMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(mqMessageBody);
    }

    /**
     * 设置产品free tag，同时添加该tag的所有上级tag
     */

    public void onStartup(CmsProductFreeTagsUpdateMQMessageBody messageMap) throws Exception {


            List<String> tagPathList = messageMap.getTagPathList();
            if (tagPathList == null || tagPathList.isEmpty()) {
                $info("CmsAdvanceSearchService：setProdFreeTag 未选择标签,将清空所有自由标签");
            }

            List<String> orgDispTagList = null;
//            if (params.get("orgDispTagList") != null) {
//                orgDispTagList = (List<String>) params.get("orgDispTagList");
//            }

            boolean isSelAll = messageMap.getIsSelAll();

            List<String> prodCodeList=null;
            if (isSelAll) {
                // 从高级检索重新取得查询结果（根据session中保存的查询条件）
               // prodCodeList = getProductCodeList(channelId, cmsSession);
                if (prodCodeList == null || prodCodeList.isEmpty()) {
                    $warn("CmsAdvanceSearchService：addProdTag 缺少参数 未查询到商品");
                    throw new BusinessException("缺少参数，未选择商品!");
                }
            } else {
                prodCodeList = messageMap.getProdCodeList();
                if (prodCodeList == null || prodCodeList.isEmpty()) {
                    $warn("CmsAdvanceSearchService：addProdTag 缺少参数 未选择商品");
                    throw new BusinessException("缺少参数，未选择商品!");
                }
            }
          //  productTagService.setProdFreeTag(channelId, tagPathList, prodCodeList, orgDispTagList, modifier);

    }

}
