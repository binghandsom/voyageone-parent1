package com.voyageone.task2.cms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.*;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbSimpleItemService;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformMappingDeprecatedService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import org.apache.avro.data.Json;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Charis on 2017/3/24.
 */
@SuppressWarnings("ALL")
@Service
public class CmsBuildPlatformAttributeUpdateTmTongGouService extends BaseCronTaskService{

    // 官网同购系cartID
    private final static List<String> cartList = Lists.newArrayList("30","31");

    private Map<String, Map<String, List<ConditionPropValueModel>>> channelConditionConfig;
    // 分隔符(,)
    private final static String Separtor_Coma = ",";

    @Autowired
    private TbSimpleItemService tbSimpleItemService;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private PlatformMappingDeprecatedService platformMappingDeprecatedService;
    @Autowired
    private SxProductService sxProductService;

    @Override
    protected String getTaskName() {
        return "CmsBuildPlatformAttributeUpdateTmTongGouJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }


    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        //获取Workload列表
        List<CmsBtSxWorkloadModel> groupList = new ArrayList<>();
        // 获取该任务可以运行的销售渠道
        List<String> channels = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdListCartIdList(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channels, cartList);
        if (sxWorkloadModels == null || sxWorkloadModels.size() == 0) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channels, cartList);
            return;
        }
        for(CmsBtSxWorkloadModel workloadModel : groupList) {
            doTmTongGouAttibuteUpdate(workloadModel);
        }
    }

    public void doTmTongGouAttibuteUpdate(CmsBtSxWorkloadModel work) throws Exception{
        String channelId = work.getChannelId();
        int cartId_shop = work.getCartId();
        Long groupId = work.getGroupId();
        String workloadName = work.getWorkloadName();
        SxData sxData = null;
        String numIId = "";
        // 开始时间
        long prodStartTime = System.currentTimeMillis();
        //读店铺信息
        ShopBean shop = new ShopBean();
        try {
            //读店铺信息
            shop = Shops.getShop(channelId, cartId_shop);

            if (shop == null) {
                $error("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId_shop);
                throw new Exception(String.format("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId_shop));
            }

            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);

            int cartId = sxData.getCartId();
            if (sxData == null) {
                String errorMsg = String.format("(SxData)信息失败！[sxData=null][workloadId:%s][groupId:%s]:", work.getId(), work.getGroupId());
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            if (mainProduct == null) {
                String errMsg = String.format("取得主商品信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
                $error(errMsg);
                sxData.setErrorMessage(errMsg);
                throw new BusinessException(errMsg);
            }

            CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(cartId);
            numIId = mainProductPlatformCart.getpNumIId();
            if (StringUtils.isEmpty(numIId)) {
                String errorMsg = String.format("取得该平台wareId失败！[ChannelId:%s] [CartId；%s] [GroupId:%s]", channelId, cartId, groupId);
                logger.error(errorMsg);
                throw new BusinessException(errorMsg);
            }

            // tmall.item.update.simpleschema.get (官网同购编辑商品的get接口)
            TbItemSchema tbItemSchema = tbSimpleItemService.getSimpleItem(shop, Long.parseLong(numIId));

            BaseMongoMap<String, String> productInfoMap = getProductInfoMap(mainProductPlatformCart, workloadName, mainProduct, sxData, shop);
            // 构造Field列表
//            List<Field> itemFieldList = new ArrayList<>();
            InputField inputField = new InputField();
            productInfoMap.entrySet().forEach(p -> {
                inputField.setId(p.getKey());
                inputField.setValue(p.getValue());
//                itemFieldList.add(inputField);
            });

            List<Field> fieldList = tbItemSchema.getFields();
            fieldList.stream()
                    .forEach( tmField -> {
                        if (inputField.getId().equals(tmField.getId())) {
                            InputField field = (InputField) tmField;
                            if ("extends".equals(tmField.getId())) {
                                Map<String,String> map = new HashMap<String, String>();
                                ObjectMapper objectMapper = new ObjectMapper();
                                try{
                                    map = objectMapper.readValue(field.getDefaultValue(), new TypeReference<HashMap<String,String>>(){});
                                }catch(Exception e){
//                                    e.printStackTrace();
                                }
                                map.entrySet().stream().forEach(m -> {
                                    if ("shop_cats".equals(m.getKey())) {
                                        m.setValue(inputField.getValue());
                                    }
                                });
                                try {
                                    String fieldValue = objectMapper.writeValueAsString(map);
                                    field.setValue(fieldValue);
                                } catch (JsonProcessingException e) {
//                                    e.printStackTrace();
                                }
                            } else {
                                field.setValue(inputField.getValue());
                            }
                        } else {
                            if (tmField.getType().toString().equalsIgnoreCase("input")) {
                                InputField field = (InputField) tmField;
                                field.setValue(field.getDefaultValue());
                            }
                        }
                    });
            String productInfoXml = SchemaWriter.writeParamXmlString(fieldList);
            String result = tbSimpleItemService.updateSimpleItem(shop, NumberUtils.toLong(numIId), productInfoXml);

            if (!StringUtils.isEmpty(result) && result.startsWith("ERROR:")) {
                // 天猫官网同购新增/更新商品失败时
                String errMsg = "天猫官网同购更新商品时出现错误!";
                errMsg += result;
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

        }catch (Exception e) {
            if (sxData == null) {
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId_shop);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(String.format("天猫同购取得商品数据为空！[ChannelId:%s] [GroupId:%s]", channelId, groupId));
            }
            String errMsg = String.format("天猫同购平台更新商品异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WorkloadName:%s] [%s]",
                    channelId, cartId_shop, groupId, workloadName, e.getMessage());
            $error(errMsg);
            e.printStackTrace();
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(errMsg);
            }
            // 回写workload表(失败2)
            sxProductService.updatePlatformWorkload(work, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            $error(String.format("天猫同购平台更新商品信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [耗时:%s]",
                    channelId, cartId_shop, groupId, (System.currentTimeMillis() - prodStartTime)));
            return;
        }
    }


    private BaseMongoMap<String, String> getProductInfoMap(CmsBtProductModel_Platform_Cart mainProductPlatformCart, String workloadName,
                                                           CmsBtProductModel mainProduct, SxData sxData, ShopBean shop) {

        // 表达式解析子
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        // 上新产品信息保存map
        BaseMongoMap<String, String> productInfoMap = new BaseMongoMap<>();
        // 扩展(部分必填)
        // 该字段主要控制商品的部分备注信息等，其中部分字段是必须填写的，非必填的字段部分可以完全不用填写。
        // 且其中的部分字段，可以做好统一配置，做好配置的，不需要每个商品发布时都提交.
        Map<String, Object> paramExtends = new HashMap<>();

        // 店铺级标题禁用词 20161216 tom START
        // 先临时这样处理
        String notAllowList = getConditionPropValue(sxData, "notAllowTitleList", shop);
        if (PlatformWorkloadAttribute.TITLE.getValue().equals(workloadName)) {
            // 标题(必填)
            // 商品标题支持英文到中文，韩文到中文的自动翻译，可以在extends字段里面进行设置是否需要翻译
            // 注意：使用测试账号的APPKEY测试时，标题应包含中文"测试请不要拍"
            String valTitle = "";
            if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                    && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute("title"))) {
                // 画面上输入的platform的fields中的标题 (格式：<value>测试请不要拍 title</value>)
                valTitle = mainProductPlatformCart.getFields().getStringAttribute("title");
            } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn"))) {
                // common中文长标题
                valTitle = mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn");
            } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("productNameEn"))) {
                // common英文长标题
                valTitle = mainProduct.getCommon().getFields().getStringAttribute("productNameEn");
            }

            if (!StringUtils.isEmpty(notAllowList)) {
                if (!StringUtils.isEmpty(valTitle)) {
                    String[] splitWord = notAllowList.split(",");
                    for (String notAllow : splitWord) {
                        // 直接删掉违禁词
                        valTitle = valTitle.replaceAll(notAllow, "");
                    }
                }
            }
            // 店铺级标题禁用词 20161216 tom END
            productInfoMap.put(PlatformWorkloadAttribute.TITLE.getValue(), valTitle);
        } else if (PlatformWorkloadAttribute.SELLER_CIDS.getValue().equals(workloadName)) {
            // 店铺内分类id(非必填)  格式："shop_cats":"111111,222222,333333"
            String extends_shop_cats = "";
            if (mainProductPlatformCart != null
                    && ListUtils.notNull(mainProductPlatformCart.getSellerCats())) {
                List<String> sellerCatIdList = new ArrayList<>();
                for (CmsBtProductModel_SellerCat sellerCat : mainProductPlatformCart.getSellerCats()) {
                    if (!StringUtils.isEmpty(sellerCat.getcId())) {
                        sellerCatIdList.add(sellerCat.getcId());
                    }
                }
                if (ListUtils.notNull(sellerCatIdList)) {
                    extends_shop_cats = Joiner.on(Separtor_Coma).join(sellerCatIdList);
                }
            }
            productInfoMap.put("extends", extends_shop_cats);
//            productInfoMap.put("extends", JacksonUtil.bean2Json(paramExtends));
        } else if (PlatformWorkloadAttribute.DESCRIPTION.getValue().equals(workloadName)) {
            // 描述(必填)
            // 商品描述支持HTML格式，但是需要将内容变成XML格式。
            // 为了更好的用户体验，建议全部使用图片来做描述内容。描述的图片宽度不超过800像素.
            // 格式：<value>&lt;img align="middle" src="http://img.alicdn.com/imgextra/i1/2640015666/TB2islBkXXXXXXBXFXXXXXXXXXX_!!2640015666.jpg"
            //      /&gt; &lt;br&gt;&lt;img align="middle" src="http://img.alicdn.com/imgextra/i1/2640015666/~~</value>
            // 解析cms_mt_platform_dict表中的数据字典
            // modified by morse.lu 2016/12/23 start
            // 画面上可以选
//        String valDescription = getValueByDict("天猫同购描述", expressionParser, shopProp);
            String valDescription;
            RuleExpression ruleDetails = new RuleExpression();
            MasterWord masterWord = new MasterWord("details");
            ruleDetails.addRuleWord(masterWord);
            String details = null;
            try {
                details = expressionParser.parse(ruleDetails, shop, getTaskName(), null);
            } catch (Exception e) {
            }
            if (!StringUtils.isEmpty(details)) {
                valDescription = getValueByDict(details, expressionParser, shop);
            } else {
                valDescription = getValueByDict("天猫同购描述", expressionParser, shop);
            }
            // modified by morse.lu 2016/12/23 end
            // 店铺级标题禁用词 20161216 tom START
            // 先临时这样处理
            if (!StringUtils.isEmpty(notAllowList)) {
                if (!StringUtils.isEmpty(valDescription)) {
                    String[] splitWord = notAllowList.split(",");
                    for (String notAllow : splitWord) {
                        // 直接删掉违禁词
                        valDescription = valDescription.replaceAll(notAllow, "");
                    }
                }
            }
            // 店铺级标题禁用词 20161216 tom END
            productInfoMap.put(PlatformWorkloadAttribute.DESCRIPTION.getValue(), valDescription);
        } else if (PlatformWorkloadAttribute.ITEM_IMAGES.getValue().equals(workloadName)) {
            // 主图(必填)
            // 最少1张，最多5张。多张图片之间，使用英文的逗号进行分割。需要使用alicdn的图片地址。建议尺寸为800*800像素。
            // 格式：<value>http://img.alicdn.com/imgextra/i1/2640015666/TB2PTFYkXXXXXaUXpXXXXXXXXXX_!!2640015666.jpg,
            //      http://img.alicdn.com/imgextra/~~</value>
            String valMainImages = "";
            // 解析cms_mt_platform_dict表中的数据字典
            String mainPicUrls = getValueByDict("天猫同购商品主图5张", expressionParser, shop);
            if (!StringUtils.isNullOrBlank2(mainPicUrls)) {
                // 去掉末尾的逗号
                valMainImages = mainPicUrls.substring(0, mainPicUrls.lastIndexOf(Separtor_Coma));
            }
            productInfoMap.put("main_images", valMainImages);
        }


        return productInfoMap;
    }

    /**
     * 从cms_mt_channel_condition_config表中取得指定类型的值
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 产品对象
     * @param prePropId String 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
     * @return String 指定类型(如：运费模板id或关联版式id等)对应的值
     */
    private String getConditionPropValue(SxData sxData, String prePropId, ShopBean shop) {

        // 运费模板id或关联版式id返回用
        String  resultStr = "";
        // 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
        // 条件表达式表platform_prop_id字段的检索条件为条件表达式前缀加cartId
        String platformPropId = prePropId + "_" + sxData.getCartId();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = null;
        if (channelConditionConfig != null && channelConditionConfig.containsKey(sxData.getChannelId())) {
            if (channelConditionConfig.get(sxData.getChannelId()).containsKey(platformPropId)) {
                conditionPropValueModels = channelConditionConfig.get(sxData.getChannelId()).get(platformPropId);
            }
        }

        // 如果根据channelid和platformPropId取得的条件表达式为空
        if (ListUtils.isNull(conditionPropValueModels))
            return resultStr;

        try {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression().trim();
                RuleExpression conditionExpression;
                String propValue;

                // 带名字字典解析
                if (conditionExpressionStr.startsWith("{\"type\":\"DICT\"")) {
                    DictWord conditionDictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(conditionExpressionStr);
                    conditionExpression = conditionDictWord.getExpression();
                } else if (conditionExpressionStr.startsWith("{\"ruleWordList\"")) {
                    // 不带名字，只有字典表达式字典解析
                    conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                } else {
                    String errMsg = String.format("cms_mt_channel_condition_config表中数据字典的格式不对 [ChannelId:%s]" +
                            " [CartId:%s] [DictName:%s]", sxData.getChannelId(), sxData.getCartId(), prePropId);
//                        logIssue(getTaskName(), errMsg);
                    $info(errMsg);
                    continue;
                }

                // 解析出字典对应的值
                if (shop != null) {
                    propValue = expressionParser.parse(conditionExpression, shop, getTaskName(), null);
                } else {
                    propValue = expressionParser.parse(conditionExpression, null, getTaskName(), null);
                }

                // 找到字典对应的值则跳出循环
                if (!StringUtils.isEmpty(propValue)) {
                    resultStr = propValue;
                    break;
                }
            }
        } catch (Exception e) {
            String errMsg = String.format("cms_mt_channel_condition_config表中数据字典解析出错 [ChannelId:%s]" +
                            " [CartId:%s] [DictName:%s] [errMsg:%s]", sxData.getChannelId(), sxData.getCartId(), prePropId,
                    StringUtils.isEmpty(e.getMessage()) ? "出现不可预知的错误，请跟管理员联系" : e.getMessage());
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isNullOrBlank2(sxData.getErrorMessage())) {
                e.printStackTrace();
            }
            $info(errMsg);
            throw new BusinessException(errMsg);
        }

        // 指定类型(如：运费模板id或关联版式id等)对应的值
        return resultStr;
    }

    /**
     * 读取字典对应的值，如果返回空字符串则抛出异常
     *
     * @param dictName 字典名字(如："商品主图5张")
     * @param expressionParser 解析子
     * @param shopProp 店铺信息
     * @return 解析出来值（如果为多张图片URL，用逗号分隔)
     * @throws Exception
     */
    private String getValueByDict(String dictName, ExpressionParser expressionParser, ShopBean shopProp)  {
        String result = "";
        try {
            // 解析字典，取得对应的值
            result = sxProductService.resolveDict(dictName, expressionParser, shopProp, getTaskName(), null);
            if(StringUtils.isNullOrBlank2(result))
            {
                String errorMsg = String.format("字典解析的结果为空! (猜测有可能是字典不存在或者素材管理里的共通图片没有一张图片成功上传到平台) " +
                        "[dictName:%s]", dictName);
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = "";
            // 如果字典解析异常的errorMessage为空
            if (StringUtils.isNullOrBlank2(e.getMessage())) {
                // nullpoint错误的处理
                errorMsg = "天猫同购上新字典解析时出现不可预知的错误，请跟管理员联系. " + e.getStackTrace()[0].toString();
                e.printStackTrace();
            } else {
                errorMsg = e.getMessage();
            }
            throw new BusinessException(errorMsg);
        }

        return result;
    }
}
