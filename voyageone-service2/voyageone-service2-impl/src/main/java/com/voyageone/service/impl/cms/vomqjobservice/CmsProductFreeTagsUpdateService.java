package com.voyageone.service.impl.cms.vomqjobservice;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchQueryService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductFreeTagsUpdateMQMessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * CmsProductFreeTagsUpdateService    高级搜索-设置自由标签 消息处理服务类
 *
 * @author sunpt on 2017/01/12
 * @version 2.0.0
 */
@Service
public class CmsProductFreeTagsUpdateService extends BaseService {

    @Autowired
    CmsMqSenderService cmsMqSenderService;
    @Autowired
    private CmsAdvSearchQueryService advSearchQueryService;
    @Autowired
    private ProductTagService productTagService;

    /**
     * 全量 高级查询条件 发送mq
     *
     * @param chanelId
     * @param searchValue
     * @param tagPathList
     * @param orgDispTagList
     * @param sender
     * @throws MQMessageRuleException
     */
    public void sendMessage(String chanelId, CmsSearchInfoBean2 searchValue,List<String> tagPathList ,List<String> orgDispTagList, String sender) throws MQMessageRuleException {
        CmsProductFreeTagsUpdateMQMessageBody mqMessageBody = new CmsProductFreeTagsUpdateMQMessageBody();
        mqMessageBody.setChannelId(chanelId);
        mqMessageBody.setIsSelAll(true);
        mqMessageBody.setSearchValue(searchValue);
        mqMessageBody.setTagPathList(tagPathList);
        mqMessageBody.setOrgDispTagList(orgDispTagList);
        mqMessageBody.setSender(sender);

        cmsMqSenderService.sendMessage(mqMessageBody);
    }

    /**
     *
     * @param chanelId
     * @param prodCodeList
     * @param tagPathList
     * @param orgDispTagList
     * @param sender
     * @throws MQMessageRuleException
     */
    public void sendMessage(String chanelId, List<String> prodCodeList,List<String> tagPathList, List<String> orgDispTagList, String sender) throws MQMessageRuleException {
        CmsProductFreeTagsUpdateMQMessageBody mqMessageBody = new CmsProductFreeTagsUpdateMQMessageBody();
        mqMessageBody.setChannelId(chanelId);
        mqMessageBody.setIsSelAll(false);
        mqMessageBody.setProdCodeList(prodCodeList);
        mqMessageBody.setTagPathList(tagPathList);
        mqMessageBody.setOrgDispTagList(orgDispTagList);
        mqMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(mqMessageBody);
    }

    /**
     * 设置产品free tag，同时添加该tag的所有上级tag
     */
    public List<String> setProductFreeTags(CmsProductFreeTagsUpdateMQMessageBody messageMap) throws Exception {

        List<String> tagPathList = messageMap.getTagPathList();
        if (tagPathList == null || tagPathList.isEmpty()) {
            $info("CmsAdvanceSearchService：setProdFreeTag 未选择标签,将清空所有自由标签");
        }
        List<String> orgDispTagList = messageMap.getOrgDispTagList();

        boolean isSelAll = messageMap.getIsSelAll();

        List<String> prodCodeList = null;
        if (isSelAll) {
            // 从高级检索重新取得查询结果
            prodCodeList = advSearchQueryService.getProductCodeList(messageMap.getSearchValue(), messageMap.getChannelId(), false);
            if (prodCodeList == null || prodCodeList.isEmpty()) {
                $warn("CmsProductFreeTagsUpdateMQMessageBody 未查询到商品");
                throw new BusinessException("全量检索 未查询到商品!");
            }
        } else {
            prodCodeList = messageMap.getProdCodeList();
        }
        //设置自由标签
        productTagService.setProdFreeTag(messageMap.getChannelId(), tagPathList, prodCodeList, orgDispTagList, messageMap.getSender());

        return prodCodeList;
    }
}
