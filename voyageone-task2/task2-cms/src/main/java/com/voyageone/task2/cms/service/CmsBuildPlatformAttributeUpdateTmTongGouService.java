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
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
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
import com.voyageone.service.model.cms.CmsBtTmTonggouFeedAttrModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.avro.data.Json;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Charis on 2017/3/24.
 */
@SuppressWarnings("ALL")
@Service
public class CmsBuildPlatformAttributeUpdateTmTongGouService extends BaseCronTaskService{

    // 线程数(synship.tm_task_control中设置的当前job的最大线程数"thread_count", 默认为5)
    private int threadCount = 5;

    // 抽出件数(synship.tm_task_control中设置的当前job的最大线程数"row_count", 默认为500)
    private int rowCount = 500;

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
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
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

        doUpdateMain(taskControlList);

    }

    public void doUpdateMain(List<TaskControlBean> taskControlList) {
        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        channelConditionConfig = new HashMap<>();

        // 由于这个方法可能会自己调用自己循环很多很多次， 不一定会跳出循环， 但又希望能获取到最新的TaskControl的信息， 所以不使用基类里的这个方法了
        // 为了调试方便， 允许作为参数传入， 但是理想中实际运行中， 基本上还是自主获取的场合比较多
        if (taskControlList == null) {
            taskControlList = taskDao.getTaskControlList(getTaskName());

            if (taskControlList.isEmpty()) {
//                $info("没有找到任何配置。");
                logIssue("没有找到任何配置！！！", getTaskName());
                return;
            }

            // 是否可以运行的判断
            if (!TaskControlUtils.isRunnable(taskControlList)) {
                $info("Runnable is false");
                return;
            }

        }

        // 线程数(默认为5)
        threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));
        // 抽出件数(默认为500)
        rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

        // 获取该任务可以运行的销售渠道
        List<TaskControlBean> taskControlBeanList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 准备按组分配线程（相同的组， 会共用相同的一组线程通道， 不同的组， 线程通道互不干涉）
        Map<String, List<String>> mapTaskControl = new HashMap<>();
        taskControlBeanList.forEach((l)->{
            String key = l.getCfg_val2();
            if (StringUtils.isEmpty(key)) {
                key = "0";
            }
            if (mapTaskControl.containsKey(key)) {
                mapTaskControl.get(key).add(l.getCfg_val1());
            } else {
                List<String> channelList = new ArrayList<>();
                channelList.add(l.getCfg_val1());
                mapTaskControl.put(key, channelList);
            }
            // 再次获取一下配置项
            channelConditionConfig.put(l.getCfg_val1(), conditionPropValueRepo.getAllByChannelId(l.getCfg_val1()));
        });

        Map<String, ExecutorService> mapThread = new HashMap<>();

        while (true) {

            mapTaskControl.forEach((k, v)->{
                boolean blnCreateThread = false;

                if (mapThread.containsKey(k)) {
                    ExecutorService t = mapThread.get(k);
                    if (t.isTerminated()) {
                        // 可以新做一个线程
                        blnCreateThread = true;
                    }
                } else {
                    // 可以新做一个线程
                    blnCreateThread = true;
                }

                if (blnCreateThread) {
                    ExecutorService t = Executors.newSingleThreadExecutor();

                    List<String> channelIdList = v;
                    if (channelIdList != null) {
                        for (String channelId : channelIdList) {
                            t.execute(() -> {
                                try {
                                    if (ChannelConfigEnums.Channel.USJGJ.getId().equals(channelId)) {
                                        doProductUpdate(channelId, CartEnums.Cart.LTT.getValue(), threadCount, rowCount);
                                    } else {
                                        doProductUpdate(channelId, CartEnums.Cart.TT.getValue(), threadCount, rowCount);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                    t.shutdown();

                    mapThread.put(k, t);

                }
            });

            boolean blnAllOver = true;
            for (Map.Entry<String, ExecutorService> entry : mapThread.entrySet()) {
                if (!entry.getValue().isTerminated()) {
                    blnAllOver = false;
                    break;
                }
            }
            if (blnAllOver) {
                break;
            }
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // TODO: 所有渠道处理总件数为0的场合， 就跳出不继续做了。 以外的场合， 说明可能还有别的未完成的数据， 继续自己调用自己一下
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doUpdateMain(null);

    }

    /**
     * 平台产品更新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     * @param threadCount int 线程数
     * @param rowCount int 每个渠道最大抽出件数
     */
    public void doProductUpdate(String channelId, int cartId, int threadCount, int rowCount) throws Exception {

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdListCartIdList(
                rowCount, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) {
            $error("增量更新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 根据上新任务列表中的groupid循环上新处理
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> doTmTongGouAttibuteUpdate(cmsBtSxWorkloadModel, shopProp));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        $info("当前渠道的天猫官网同购更新任务执行完毕！[channelId:%s] [cartId:%s] [更新对象group件数:%s] ", channelId, cartId, sxWorkloadModels.size());
    }

    public void doTmTongGouAttibuteUpdate(CmsBtSxWorkloadModel work, ShopBean shop){
        String channelId = work.getChannelId();
        int cartId_shop = work.getCartId();
        Long groupId = work.getGroupId();
        String workloadName = work.getWorkloadName();
        SxData sxData = null;
        String numIId = "";
        // 开始时间
        long prodStartTime = System.currentTimeMillis();
        work.setModified(new Date(prodStartTime));
        try {

            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId, true);

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
                String errorMsg = String.format("取得该平台numIId失败！[ChannelId:%s] [CartId；%s] [GroupId:%s]", channelId, cartId, groupId);
                logger.error(errorMsg);
                throw new BusinessException(errorMsg);
            }
            // 处理标题或描述中不合理的字符
            String errorWord = getConditionPropValue(sxData, "updateErrorWord", shop);

            // tmall.item.update.simpleschema.get (官网同购编辑商品的get接口)
            TbItemSchema tbItemSchema = tbSimpleItemService.getSimpleItem(shop, Long.parseLong(numIId));

            BaseMongoMap<String, String> productInfoMap = getProductInfoMap(mainProductPlatformCart, mainProduct, sxData, shop, errorWord);
            // 构造Field列表
            List<InputField> itemFieldList = new ArrayList<>();
            productInfoMap.entrySet().forEach(p -> {
                InputField inputField = new InputField();
                inputField.setId(p.getKey().toLowerCase());
                inputField.setValue(p.getValue());
                itemFieldList.add(inputField);
            });
            Map<String, InputField> fieldMap = itemFieldList.stream().collect(Collectors.toMap(InputField::getId, field -> field));
            List<Field> fieldList = tbItemSchema.getFields();
            fieldList.stream()
                    .forEach( tmField -> {
                        if (fieldMap.get(tmField.getId().toLowerCase()) != null &&
                            fieldMap.get(tmField.getId().toLowerCase()).getId().equalsIgnoreCase(tmField.getId())) {
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
                                        m.setValue(fieldMap.get(tmField.getId()).getValue());
                                    }
                                });
                                try {
                                    String fieldValue = objectMapper.writeValueAsString(map);
                                    field.setValue(fieldValue);
                                } catch (JsonProcessingException e) {
//                                    e.printStackTrace();
                                }
                            } else {
                                field.setValue(fieldMap.get(tmField.getId()).getValue());
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
            } else {
                // 回写workload表(成功1)
                sxProductService.updatePlatformWorkload(work, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());
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


    private BaseMongoMap<String, String> getProductInfoMap(CmsBtProductModel_Platform_Cart mainProductPlatformCart,
                                                           CmsBtProductModel mainProduct, SxData sxData, ShopBean shop, String errorWord) {

        // 表达式解析子
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        // 上新产品信息保存map
        BaseMongoMap<String, String> productInfoMap = new BaseMongoMap<>();
        // 扩展(部分必填)
        // 该字段主要控制商品的部分备注信息等，其中部分字段是必须填写的，非必填的字段部分可以完全不用填写。
        // 且其中的部分字段，可以做好统一配置，做好配置的，不需要每个商品发布时都提交.
        Map<String, Object> paramExtends = new HashMap<>();

        // 店铺级标题禁用词 20161216 tom START
        String valTitle = getTitleForTongGou(mainProduct, sxData, errorWord);

        // 标题(必填)
        // 商品标题支持英文到中文，韩文到中文的自动翻译，可以在extends字段里面进行设置是否需要翻译
        // 注意：使用测试账号的APPKEY测试时，标题应包含中文"测试请不要拍"
        // 店铺级标题禁用词 20161216 tom END
        // 官网同购是会自动把超长的字符截掉的， 为了提示运营， 报个错吧 20170509 tom START
        if (!StringUtils.isEmpty(valTitle)
                && ChannelConfigEnums.Channel.Coty.equals(shop.getOrder_channel_id()) ) {
            int titleLength = 0;

            try {
                titleLength = valTitle.getBytes("GB2312").length;
            } catch (UnsupportedEncodingException ignored) {
            }

            if (titleLength > 60) {
                throw new BusinessException(String.format("标题超长:%s", valTitle));
            }

        }
        // 店铺级标题禁用词 20161216 tom END
        productInfoMap.put(PlatformWorkloadAttribute.TITLE.getValue(), valTitle);

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
        if (!StringUtils.isEmpty(errorWord)) {
            if (!StringUtils.isEmpty(valDescription)) {
//                String[] splitWord = notAllowList.split(",");
//                for (String notAllow : splitWord) {
//                    // 直接删掉违禁词
//                    valDescription = valDescription.replaceAll(notAllow, "");
//                }
                sxProductService.deleteErrorWord(valDescription, errorWord);
            }
        }
        // 店铺级标题禁用词 20161216 tom END
        productInfoMap.put(PlatformWorkloadAttribute.DESCRIPTION.getValue(), valDescription);

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

        // 无线描述(选填)
        // 解析cms_mt_platform_dict表中的数据字典
        if (mainProduct.getCommon().getFields().getAppSwitch() != null &&
                mainProduct.getCommon().getFields().getAppSwitch() == 1) {

            String valWirelessDetails;
            RuleExpression ruleWirelessDetails = new RuleExpression();
            MasterWord masterWordWirelessDetails = new MasterWord("wirelessDetails");
            ruleWirelessDetails.addRuleWord(masterWordWirelessDetails);
            String wirelessDetails = null;
            try {
                wirelessDetails = expressionParser.parse(ruleWirelessDetails, shop, getTaskName(), null);
            } catch (Exception e) {
            }
            if (!StringUtils.isEmpty(wirelessDetails)) {
                valWirelessDetails = getValueByDict(wirelessDetails, expressionParser, shop);
            } else {
                valWirelessDetails = getValueByDict("天猫同购无线描述", expressionParser, shop);
            }

            productInfoMap.put("wireless_desc", valWirelessDetails);
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

    public String getTitleForTongGou(CmsBtProductModel mainProduct, SxData sxData, String errorWord) {
        // 天猫国际官网同购平台（或USJOI天猫国际官网同购平台）
        CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());
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
//        productInfoMap.put("title", "测试请不要拍 " + valTitle);

        // 店铺级标题禁用词 20161216 tom START
//        // 先临时这样处理
//        String notAllowList = getConditionPropValue(sxData, "notAllowTitleList", shopProp);
        if (!StringUtils.isEmpty(errorWord)) {
            if (!StringUtils.isEmpty(valTitle)) {
//                String[] splitWord = notAllowList.split(",");
//                for (String notAllow : splitWord) {
//                    // 直接删掉违禁词
//                    valTitle = valTitle.replaceAll(notAllow, "");
//                }
                valTitle = sxProductService.deleteErrorWord(valTitle, errorWord);
            }
        }
        return valTitle;
    }
}
