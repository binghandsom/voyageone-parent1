package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.response.TmallItemSchemaAddResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by morse.lu on 2016/5/11.
 */
@Service
public class CmsBuildPlatformProductUploadTmItemService extends BaseService {

    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private TbProductService tbProductService;

    /**
     * TM上新商品
     *
     * @param expressionParser ExpressionParser (包含SxData)
     * @param platformProductId 平台产品id. 新建的场合：上传产品API调用后返回的产品id. 更新的场合：ProductGroup里platformPid（sxData.getPlatform().getPlatformPid()）
     * @param cmsMtPlatformCategorySchemaModel MongoDB  propsItem取得用
     * @param cmsMtPlatformMappingModel MongoDB 平台CategoryId取得，mapping设定
     * @param shopBean 店铺
     * @param modifier 更新者
     * @return numIId
     * @throws Exception
     */
    public String uploadItem(ExpressionParser expressionParser, String platformProductId, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, ShopBean shopBean, String modifier) throws Exception {
        SxData sxData = expressionParser.getSxData();
        String numIId = sxData.getPlatform().getNumIId();
        Long categoryCode = Long.valueOf(cmsMtPlatformMappingModel.getPlatformCategoryId());

        String itemSchema = cmsMtPlatformCategorySchemaModel.getPropsItem();
        $debug("itemSchema:" + itemSchema);

        List<Field> fields;
        try {
            fields = SchemaReader.readXmlForList(itemSchema);
        } catch (TopSchemaException e) {
            $error(e.getMessage(), e);
            throw new BusinessException("Can't convert schema to fields: " + e.getMessage());
        }

        sxProductService.constructMappingPlatformProps(fields, cmsMtPlatformMappingModel,shopBean,expressionParser, modifier);

        if (StringUtils.isEmpty(numIId)) {
            // add
            try {
                $debug("addTmallItem: [productCode:" + platformProductId + ", categoryCode:" + categoryCode + "]");
                numIId = addTmallItem(categoryCode, platformProductId, fields, shopBean);
            } catch (ApiException e) {
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                throw new BusinessException(e.getMessage());
            }
        } else {
            // update
            try {
                $debug("updateTmallItem: [productCode:" + platformProductId + ", categoryCode:" + categoryCode + ", numIId:" + numIId + "]");
                numIId = updateTmallItem(platformProductId, numIId, categoryCode, fields, shopBean);
            } catch (ApiException e) {
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                throw new BusinessException(e.getMessage());
            }
        }

        return numIId;
    }

    private String addTmallItem(Long categoryCode, String productCode, List<Field> itemFields, ShopBean shopBean) throws ApiException {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new BusinessException(e.getMessage());
        }
        StringBuffer failCause = new StringBuffer();
        $debug("tmall category code:" + categoryCode);
        $debug("xmlData:" + xmlData);
        TmallItemSchemaAddResponse addItemResponse = tbProductService.addItem(categoryCode, productCode, xmlData, shopBean);
        String numId;
        if (addItemResponse == null) {
            failCause.append("Tmall return null response when add item");
            $error(failCause + ", request:" + xmlData);
            throw new BusinessException(failCause.toString());
        } else if (addItemResponse.getErrorCode() != null) {
            $debug("errorCode:" + addItemResponse.getErrorCode());
            if (addItemResponse.getSubCode().contains("IC_CHECKSTEP_ALREADY_EXISTS_SAME_SPU")) {
                String subMsg = addItemResponse.getSubMsg();
                numId = getNumIdFromSubMsg(subMsg, failCause);
                if (failCause.length() == 0 && numId != null && !"".equals(numId)) {
                    $debug(String.format("find numId(%s) has been uploaded before", numId));
                    return numId;
                }
            } else {
                failCause.append(addItemResponse.getSubMsg());
            }

            //天猫系统服务异常
            if (failCause.indexOf("天猫商品服务异常") != -1
                    || failCause.indexOf("访问淘宝超时") != -1) {
                $debug("此处应该是下次启动任务仍需处理的错误--->" + failCause.toString());
                throw new BusinessException(failCause.toString());
            }
            else {
                throw new BusinessException(failCause.toString());
            }
        } else {
            numId = addItemResponse.getAddItemResult();
            $debug("numId: " + numId);
            return numId;
        }
    }

    private String updateTmallItem(String productId, String numId, Long categoryCode, List<Field> itemFields, ShopBean shopBean) throws ApiException {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new BusinessException(e.getMessage());
        }
        StringBuffer failCause = new StringBuffer();
        $debug("tmall category code:" + categoryCode);
        $debug("numId:" + numId);
        $debug("xmlData:" + xmlData);
        TmallItemSchemaUpdateResponse updateItemResponse = tbProductService.updateItem(productId, numId, categoryCode, xmlData, shopBean);
        if (updateItemResponse == null) {
            failCause.append("Tmall return null response when update item");
            $error(failCause + ", request:" + xmlData);
            throw new BusinessException(failCause.toString());
        } else if (updateItemResponse.getErrorCode() != null) {
            $debug("errorCode:" + updateItemResponse.getErrorCode());
            String subMsg = updateItemResponse.getSubMsg();
            numId = getNumIdFromSubMsg(subMsg, failCause);
            if (numId != null && !"".equals(numId)) {
                $debug(String.format("find numId(%s) has been uploaded before", numId));
                return numId;
            }
            //天猫系统服务异常
            if (failCause.indexOf("天猫商品服务异常") != -1
                    || failCause.indexOf("访问淘宝超时") != -1) {
                $debug("此处应该是下次启动任务仍需处理的错误--->" + failCause.toString());
                throw new BusinessException(failCause.toString());
            }
            throw new BusinessException(failCause.toString());
        } else {
            numId = updateItemResponse.getUpdateItemResult();
            $debug("numId: " + numId);
            return numId;
        }
    }

    private String getNumIdFromSubMsg(String subMsg, StringBuffer failCause) {
        //pattern: "您已发布过同类宝贝，不允许重复发布；已发布的商品ID列表为：521369504454"
        //pattern: 您已发布过相同类目(腰带/皮带/腰链)，品牌(BCBG)，货号(FLTBC155)的宝贝，不允许重复发布；已发布的商品ID列表为：524127233288,上架的数量必须大于0
        String numId = null;
        Pattern pattern = Pattern.compile("您已发布过.*已发布的商品ID列表为：\\d+");
        Matcher matcher = pattern.matcher(subMsg);
        if (matcher.find()) {
            String matchString = subMsg.substring(matcher.start(), matcher.end());
            Pattern numIdPattern = Pattern.compile("\\d+");
            Matcher numIdMatcher = numIdPattern.matcher(matchString);
            while (numIdMatcher.find()) {
                String matchInter = matchString.substring(numIdMatcher.start(), numIdMatcher.end());
                if (matchInter.length() == 12) {
                    numId = matchInter;
                    break;
                }
            }
            if (numId == null){
                failCause.append(subMsg);
                return null;
            }

            if (matcher.start() != 0) {
                failCause.append(subMsg.substring(0, matcher.start()));
            }

            if (matcher.end() != subMsg.length()) {
                failCause.append(subMsg.substring(matcher.end() + 1));
            }
        } else {
            failCause.append(subMsg);
        }
        return numId;
    }
    
}
