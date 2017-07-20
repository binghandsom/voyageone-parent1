package com.voyageone.service.impl.cms.vomqjobservice;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchQueryService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductFreeTagsUpdateMQMessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


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
    @Autowired
    private CmsProductSearchQueryService cmsProductSearchQueryService;

    /**
     * 全量 高级查询条件 发送mq
     *
     * @param chanelId       chanelId
     * @param searchValue    searchValue
     * @param tagPathList    tagPathList
     * @param orgDispTagList orgDispTagList
     * @param sender         sender
     */
    public void sendMessage(String chanelId, CmsSearchInfoBean2 searchValue, List<String> tagPathList, List<String> orgDispTagList, String sender) {
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
     * @param chanelId       chanelId
     * @param prodCodeList   prodCodeList
     * @param tagPathList    tagPathList
     * @param orgDispTagList orgDispTagList
     * @param sender         sender
     */
    public void sendMessage(String chanelId, List<String> prodCodeList, List<String> tagPathList, List<String> orgDispTagList, String sender) {
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
     *
     * @param messageMap messageMap
     * @return List<String>
     * @throws Exception
     */
    public List<String> setProductFreeTags(CmsProductFreeTagsUpdateMQMessageBody messageMap) throws Exception {

        List<String> tagPathList = messageMap.getTagPathList();
        if (tagPathList == null || tagPathList.isEmpty()) {
            $info("CmsAdvanceSearchService：setProdFreeTag 未选择标签,将清空所有自由标签");
        }
        List<String> orgDispTagList = messageMap.getOrgDispTagList();

        boolean isSelAll = messageMap.getIsSelAll();

        List<String> prodCodeList;
        if (isSelAll) {
            // 从高级检索重新取得查询结果
            messageMap.getSearchValue().setProductPageNum(0);
            prodCodeList = advSearchQueryService.getProductCodeList(messageMap.getSearchValue(), messageMap.getChannelId(), false, true);
            if (prodCodeList == null || prodCodeList.isEmpty()) {
                $warn("CmsProductFreeTagsUpdateMQMessageBody 未查询到商品");
                throw new BusinessException("全量检索 未查询到商品!");
            }
        } else {
            prodCodeList = messageMap.getProdCodeList();
        }

        $info("productCnt=" + prodCodeList.size());
        //设置自由标签
        productTagService.setProdFreeTag(messageMap.getChannelId(), tagPathList, prodCodeList, orgDispTagList, EnumProductOperationType.BatchSetFreeTag, messageMap.getSender());


        return prodCodeList;
    }

    public List<String> setUsProductFreeTags(CmsProductFreeTagsUpdateMQMessageBody messageMap) throws Exception {

        List<String> tagPathList = messageMap.getTagPathList();
        if (tagPathList == null || tagPathList.isEmpty()) {
            $info("CmsAdvanceSearchService：setProdFreeTag 未选择标签,将清空所有自由标签");
        }
        List<String> orgDispTagList = messageMap.getOrgDispTagList();

        boolean isSelAll = messageMap.getIsSelAll();

        List<String> prodCodeList = null;
        if (isSelAll) {
            // 从高级检索重新取得查询结果

            //勾选了全部,需要通过检索条件,查询出所有信息
            CmsSearchInfoBean2 queryParams = messageMap.getSearchValue();
            CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(queryParams, messageMap.getChannelId());
            long productListTotal = cmsProductCodeListBean.getTotalCount();
            //要根据查询出来的总页数设置分页
            long pageNumber = 0;
            if (productListTotal % 100 == 0) {
                //整除
                pageNumber = productListTotal / 100;
            } else {
                //不整除
                pageNumber = (productListTotal / 100) + 1;
            }

            for (int i = 1; i <= pageNumber; i++) {
                queryParams.setProductPageSize(100);
                queryParams.setProductPageNum(i);
                CmsProductCodeListBean cmsProductCodeListBean1 = cmsProductSearchQueryService.getProductCodeList(queryParams, messageMap.getChannelId());
                prodCodeList = cmsProductCodeListBean1.getProductCodeList();
                $info("productCnt=" + prodCodeList.size());
                productTagService.setProdUsFreeTag(messageMap.getChannelId(), tagPathList, prodCodeList, orgDispTagList, EnumProductOperationType.BatchSetFreeTag, messageMap.getSender());
            }
        } else {
            prodCodeList = messageMap.getProdCodeList();
            $info("productCnt=" + prodCodeList.size());
            productTagService.setProdUsFreeTag(messageMap.getChannelId(), tagPathList, prodCodeList, orgDispTagList, EnumProductOperationType.BatchSetFreeTag, messageMap.getSender());
        }



        return prodCodeList;
    }

}
