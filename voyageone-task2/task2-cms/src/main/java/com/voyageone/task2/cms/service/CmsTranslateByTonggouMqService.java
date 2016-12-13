package com.voyageone.task2.cms.service;

import com.mongodb.BulkWriteResult;
import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.InputField;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbSimpleItemService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 天猫同购共通标题描述翻译
 *
 * 利用天猫官网同购更新商品方式，将标题或长描述等英文翻译成中文,并回写到mongoDB产品表中
 * 可以翻译的项目：标题， 描述， 使用方法等;
 * 注意：只翻译common下面的标题描述，不需要对各个平台的标题描述进行翻译。
 *      如果需要翻译的时候，可以强制翻译标题和所有的描述项目
 *      如果不需要翻译标题的时候，只能强制翻译标题以外的描述项目
 *
 * @author desmond on 2016/12/06
 * @version 2.10.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_TranslateByTonggouJob)
public class CmsTranslateByTonggouMqService extends BaseMQCmsService {

    // 项目ID_标题
    private final static String Item_title = "title";
    // 项目ID_描述
    private final static String Item_description = "description";
    // 项目ID_类目
    private final static String Item_category = "category";
    // 项目ID_主图
    private final static String Item_main_images = "main_images";
    // 项目ID_skus
    private final static String Item_skus = "skus";
    // 项目ID_物流模板id
    private final static String Item_template_id = "template_id";
    // 翻译前标题英文名
    private final static String Original_Title_En = "productNameEn";
    // 翻译后标题中文名
    private final static String Original_Title_Cn = "originalTitleCn";

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private TbSimpleItemService tbSimpleItemService;

    @Autowired
    public CmsTranslateByTonggouMqService(CmsBtProductDao cmsBtProductDao, TbSimpleItemService tbSimpleItemService) {
        this.cmsBtProductDao = cmsBtProductDao;
        this.tbSimpleItemService = tbSimpleItemService;
    }

    /**
     * 入口
     * 输入参数:
     *     channelId：渠道ID
     *     codeList：需要翻译的产品code列表（不设置的场合就是该渠道下所有code都翻译）
     *     blnNeedTransTitle: 是否翻译标题(默认为不翻译标题,即使这里设成了需要翻译标题，但如果已经有中文标题了还是会不翻译标题的，没有强制翻译标题)
     *     transSrcDesMap: 需要翻译的产品common源项目和要回写目标项目对应关系map(源项目名，翻译后要回写的项目名)
     *     blnRunType: 是否强制翻译false或true（false的场合， 如果未翻译的场合才会进行翻译，true的场合，不管是否已翻译，都会进行翻译;如果不需要翻译标题，不会强制翻译标题）
     *
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        $info("CmsTranslateByTonggouMqService start 参数 " + JacksonUtil.bean2Json(messageMap));

        // 检查输入参数
        // 参数: 渠道id
        String channelId;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        } else {
            $error("天猫同购共通标题描述翻译(MQ): 输入参数不存在: channelId");
            return;
        }

        // 参数: 需要翻译的产品code列表（不设置的场合就是该渠道下所有code都翻译）
        List<String> codeList = null;
        if (messageMap.containsKey("codeList")) {
            codeList = (List<String>) messageMap.get("codeList");
        }

        // 参数: 是否翻译标题(默认为不翻译标题)
        boolean blnNeedTransTitle;
        if (messageMap.containsKey("blnNeedTransTitle")) {
            blnNeedTransTitle = (Boolean) (messageMap.get("blnNeedTransTitle"));
        } else {
            blnNeedTransTitle = false;
        }

        // 参数: 需要翻译的产品common源项目和要回写目标项目对应关系map(源项目名，翻译后要回写的项目名)
        BaseMongoMap<String, String> transSrcDesMap = null;
        if (messageMap.containsKey("transSrcDesMap")) {
            transSrcDesMap = (BaseMongoMap<String, String>) messageMap.get("transSrcDesMap");
        } else {
            // 这里不能报错，因为可能它只想翻译标题，不想翻译其他项目
        }

        // 参数: 执行方式 false或true（对于标题以外的翻译项目，false的场合，如果未翻译的场合才会进行翻译; true的场合，不管是否已翻译，都会进行翻译；默认为false）
        boolean blnRunType;
        if (messageMap.containsKey("blnRunType")) {
            blnRunType = (Boolean) (messageMap.get("blnRunType"));
        } else {
            blnRunType = false;
        }

        try {
            // 多个产品官网同购翻译主处理
            doMain(channelId, codeList, blnNeedTransTitle, transSrcDesMap, blnRunType);
        } catch (Exception e) {
            if (StringUtils.isEmpty(e.getMessage())) {
                $error("CmsTranslateByTonggouMqService 异常发生!");
                e.printStackTrace();
            } else {
                $error("CmsTranslateByTonggouMqService 异常发生!" + e.getMessage());
            }
        }
        $info("CmsTranslateByTonggouMqService success end");
    }

    /**
     * 天猫同购标题描述翻译主处理
     * 读取共通配置信息，循环产品codel列表，通过天猫同购方式更新测试商品，将取得的翻译后标题描述批量回写到mongoDB产品表中
     *
     * @param channelId 渠道id
     * @param codeList 翻译对象产品code列表
     * @param blnNeedTransTitle 是否需要翻译标题(true:需要翻译标题，false:不要翻译标题)
     * @param transSrcDesMap 需要翻译的项目和翻译后需要更新的项目对应map
     * @param blnRunType 执行方式 false或true（对于标题以外的翻译项目，false的场合，如果未翻译的场合才会进行翻译; true的场合，不管是否已翻译，都会进行翻译；默认为false）
     */
    public void doMain(String channelId, List<String> codeList, boolean blnNeedTransTitle, BaseMongoMap<String, String> transSrcDesMap, boolean blnRunType) throws Exception {
        $info("天猫同购共通标题描述翻译开始!");

        // 取得synship.com_mt_third_party_config表中配置的同购APPKEY，翻译专用商品id等信息
        String configChannelId_000 = "000";
        String tm_tt_trans = "tm_tt_sx";
        ThirdPartyConfigBean config = ThirdPartyConfigs.getThirdPartyConfig(configChannelId_000, tm_tt_trans);
        if (config == null) {
            String warnMsg = String.format("取得synship.com_mt_third_party_config表中配置的同购APPKEY，翻译专用商品id等信息失败! " +
                    "[channel_id:%s] [prop_name:%s]", configChannelId_000, tm_tt_trans);
            $warn(warnMsg);
            return;
        }

        // 天猫同购共通标题描述翻译专用商品id
        String numIIdForTransOnly = "543165814485"; // 翻译专用默认商品id(024 OverStock店铺)
        if (!StringUtils.isEmpty(config.getProp_val5())) {
            numIIdForTransOnly = config.getProp_val5();
        }
        if (StringUtils.isEmpty(numIIdForTransOnly)) {
            String warnMsg = String.format("在synship.com_mt_third_party_config表中没找到翻译专用商品id信息! " +
                    "[channel_id:%s] [prop_name:%s]", configChannelId_000, tm_tt_trans);
            $warn(warnMsg);
            return;
        }

        // 官网同购更新商品时，其他必填项目和值的json串如下，但synship.com_mt_third_party_config表"000"渠道的Prop_Val6里面不一定都要设：
        // 1.类目         (category,    非必须，有默认值)
        // 2.主图地址      (main_images, 非必须，有默认值)
        // 3.物流模板id    (template_id, 非必须，有默认值)
        // 4.skus的json串 (skus,        非必须，有默认值)
        // 例：{"main_images":"http://img.alicdn.com/imgextra/i4/2939402618/TB2yzhLal0kpuFjy1XaXXaFkVXa-2939402618.jpg","category":{"cat_id":"121454006"},"template_id":"8421670741","skus":{"sale_prop":{"color":"白色","size":"OneSize"},"price":"9879.0","outer_id":"024-15765904-000-000","quantity":0,"image":"http://img.alicdn.com/imgextra/i1/2939402618/TB2ttpPaa8lpuFjy0FpXXaGrpXa-2939402618.jpg"}}
        String otherItemValueJson = config.getProp_val6();
        Map<String, Object> otherItemMap = null;
        if (!StringUtils.isEmpty(otherItemValueJson)) {
            try {
                otherItemMap = JacksonUtil.jsonToMap(otherItemValueJson);

                // 把skus的Fields列表再转回json
                Iterator<Map.Entry<String, Object>> it = otherItemMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    switch (entry.getKey()) {
                        case Item_skus:
                            String strSkus = JacksonUtil.bean2Json(entry.getValue());
                            // json前后不加中括号，天猫会报错"商家输入的sku字段json转换错误"
                            if (!strSkus.startsWith("[")) strSkus = "[" + strSkus;
                            if (!strSkus.endsWith("]")) strSkus += "]";
                            entry.setValue(strSkus);
                            break;
                        case Item_category:
                            String strCategory = JacksonUtil.bean2Json(entry.getValue());
                            entry.setValue(strCategory);
                            break;
                    }
                }
            } catch (Exception e) {
                String errMsg = String.format("解析synship.com_mt_third_party_config表\"000\"渠道的prop_val6中配置的项目json失败! " +
                        "[channel_id:%s] [prop_val6:%s]", configChannelId_000, otherItemValueJson);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

        }

        // 天猫官网同购专用ShopBean对象(注意：里面没有设置channelId和cartId哦)
        ShopBean transShop = new ShopBean();
        transShop.setOrder_channel_id(configChannelId_000);  // 调用同购接口的时候，它又会去synship.com_mt_third_party_config表里面找一个000对应的APPKEY
        transShop.setApp_url(config.getProp_val1());
        transShop.setAppKey(config.getProp_val2());
        transShop.setAppSecret(config.getProp_val3());
        transShop.setSessionKey(config.getProp_val4());
        transShop.setShop_name("天猫同购共通标题描述翻译专用店");
        transShop.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

        // 翻译产品code列表中的标题描述，如果产品code列表为空，则翻译该channel下所有产品的标题和描述
        if (ListUtils.isNull(codeList)) {
            // 不设置的场合就是所有code
            JongoQuery queryObj = new JongoQuery();
            queryObj.setQuery("{}");
            queryObj.setProjectionExt("common.fields.code");
            List<CmsBtProductModel> productList = cmsBtProductDao.select(queryObj, channelId);
            if (ListUtils.notNull(productList)) {
                codeList.addAll(productList.stream().map(model -> model.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList()));
            }
        }

        if (ListUtils.isNull(codeList)) {
            String warnMsg = String.format("没有需要翻译的产品code，直接结束! " +
                    "[channel_id:%s] [prop_name:%s]", configChannelId_000, tm_tt_trans);
            $warn(warnMsg);
            return;
        }

        // 设置商品更新共通属性
        BaseMongoMap<String, String> productInfoMap = getProductCommonInfo();

        // 循环取得的产品code列表，把要翻译的中文信息批量更新到mongoDB产品表中
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
        BulkWriteResult rs;
        for (String code : codeList) {
            // 单个code
            try {
                JongoUpdate updObj = doTranslateByTonggouSingle(transShop, channelId, code, blnNeedTransTitle,
                        transSrcDesMap, blnRunType, numIIdForTransOnly, otherItemMap, productInfoMap);
                if (updObj != null) {
                    // 如果没有一个需要翻译的字段时，返回null,也没有必要回写了
                    rs = bulkList.addBulkJongo(updObj);
                    if (rs != null) {
                        $debug("CmsTranslateByTonggouMqService channelId=%s, code=%s, product更新结果=%s", channelId, code, rs.toString());
                    }
                }
            } catch (Exception ex) {
                // 错误都在doTranslateByTonggouSingle里面打印了,这边只是catch一下，让程序正常结束
            }
        }

        rs = bulkList.execute();
        if (rs != null) {
            $debug("天猫同购共通标题描述翻译之后批量回写处理 channelId=%s, jdSkuId更新结果=%s", channelId, rs.toString());
        }

        $info("天猫同购共通标题描述翻译结束! 翻译好的内容成功插入mongoDB!");
    }

    /**
     * 单个产品code的天猫同购翻译
     *
     * @param transShop          店铺信息
     * @param channelId          渠道id
     * @param code               产品code
     * @param blnNeedTransTitle  是否需要翻译标题(true:翻译标题，false:不翻译标题)
     * @param transSrcDesMap     需要利用描述(description)字段翻译的项目英文和中文名对应关系(如：longDesEn <-> longDesCn)
     * @param blnRunType         执行方式 false或true（对于标题以外的翻译项目，false的场合，如果未翻译的场合才会进行翻译; true的场合，不管是否已翻译，都会进行翻译；默认为0）
     * @param numIIdForTransOnly 翻译专用测试用商品id
     * @param otherItemMap       更新商品时必须要填的一些项目的值(都有默认值，也可以不填)，如类目，主图地址，物流模板id，skus的json串等
     * @param productInfoMap     商品更新共通属性
     * @return JongoUpdate 翻译成功之后回写JongoUpdateSql，更新失败时返回null
     */
    public JongoUpdate doTranslateByTonggouSingle(ShopBean transShop, String channelId, String code, boolean blnNeedTransTitle,
                                                  BaseMongoMap<String, String> transSrcDesMap, boolean blnRunType, String numIIdForTransOnly,
                                                  Map<String, Object> otherItemMap, BaseMongoMap<String, String> productInfoMap) throws Exception {
        if (transShop == null || StringUtils.isEmpty(channelId) || StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(numIIdForTransOnly)) return null;

        // 获取product信息
        CmsBtProductModel prodObj = cmsBtProductDao.selectByCode(code, channelId);
        if (prodObj == null) {
            String warnMsg = String.format("根据code未能取得对应的产品信息! [channelId:%s] [code:%s]", channelId, code);
            $warn(warnMsg);
            return null;
        }

        // 判断是否需要翻译标题
        String strTransTitleEn = prodObj.getCommonNotNull().getFieldsNotNull().getProductNameEn();
        String strBrand = prodObj.getCommonNotNull().getFieldsNotNull().getBrand();
        if (!StringUtils.isEmpty(strTransTitleEn)) {
            // 翻译前先替换掉品牌，由于common.field.brand有可能被转成小写了，所以这里替换的时候要在正则表达式前面添加(?i)
            strTransTitleEn = strTransTitleEn.replaceAll("(?i)"+strBrand, "");
        }
        // 如果不需要翻译标题，则blnNeedTransTitle=false;如果需要翻译标题时，再看是否强制翻译
        if (blnNeedTransTitle      // 需要翻译标题时
                && !blnRunType     // 非强制翻译时
                && !StringUtils.isEmpty(prodObj.getCommonNotNull().getFieldsNotNull().getStringAttribute(Original_Title_Cn))) {
            // 如果产品中已经有了中文标题，则不需要再翻译中文标题了
            blnNeedTransTitle = false;
        }

        // 用于存放最终翻译结果的map
        BaseMongoMap<String, String> transResultMap = new BaseMongoMap<>();
        String result;
        if (!MapUtils.isEmpty(transSrcDesMap)) {
            // 遍历需要翻译的项目和要设置的项目对应关系map，逐个更新测试商品取得翻译后的内容
            for (Map.Entry<String, String> entry : transSrcDesMap.entrySet()) {
                // transSrcDesMap里面应该不回填标题相关项目的，但万一设置了标题中英文项目，则直接跳过
                if (Original_Title_En.equals(entry.getKey()) || Original_Title_Cn.equals(entry.getValue())) {
                    continue;
                }

                // 每个项目都要做一次同购更新测试商品翻译
                // 取得产品信息中的翻译前的项目值，然后更新翻译专用商品，翻译想要翻译的项目英文内容
                String strTransEn = prodObj.getCommonNotNull().getFieldsNotNull().getStringAttribute(entry.getKey());
                // 如果翻译前的项目值为空，直接跳过
                if (StringUtils.isEmpty(strTransEn)) {
                    continue;
                }

                // 如果不是强制翻译时，看产品中对应的中文字段是否已经有值，若已经有值了就不用再翻译了
                if (!blnRunType && !StringUtils.isEmpty(prodObj.getCommonNotNull().getFieldsNotNull().getStringAttribute(entry.getValue()))) {
                    continue;
                }

                // 更新翻译专用商品，翻译想要翻译的项目英文内容
                result = updateTransWare(numIIdForTransOnly, strTransTitleEn, strTransEn, otherItemMap, productInfoMap, transShop);
                if (StringUtils.isEmpty(result)) {
                    // 如果没有返回值为空，则继续翻译下一个待翻译项目
                    continue;
                }

                // 如果更新出错，则输入错误跳过回写更新
                if (result.startsWith("ERROR:")) {
                    // 错误例子：ERROR:15:isv.invalid-parameter:cid:商品类目映射出错请稍后重试
                    $error(String.format("天猫同购共通标题描述翻译当前产品code(%s)翻译出错. [errMsg:%s]", code, result));
                    continue;
                }

                // 从天猫平台上取得翻译之后的标题和描述
                Map<String, String> platformResultMap = getTransResult(transShop, numIIdForTransOnly, entry.getValue());
                // 将翻译后的标题和描述中文添加到结果map中
                addTransResultMap(transResultMap, platformResultMap);
            }
        }
        // 如果没有翻译过，更新翻译专用商品，翻译标题
        if (blnNeedTransTitle
                && !transResultMap.keySet().contains(Original_Title_Cn)) {
            // 更新翻译专用商品，翻译想要翻译的项目英文内容
            result = updateTransWare(numIIdForTransOnly, strTransTitleEn, strTransTitleEn, otherItemMap, productInfoMap, transShop);
            if (!StringUtils.isEmpty(result)) {
                // 如果更新出错，则输入错误跳过回写更新
                if (result.startsWith("ERROR:")) {
                    // 错误例子：ERROR:15:isv.invalid-parameter:cid:商品类目映射出错请稍后重试
                    $error(String.format("天猫同购共通标题描述翻译当前产品code(%s)翻译出错. [errMsg:%s]", code, result));
                } else {
                    // 从天猫平台上取得翻译之后的标题和描述
                    Map<String, String> platformResultMap = getTransResult(transShop, numIIdForTransOnly, Original_Title_Cn);
                    // 将翻译后的标题和描述中文添加到结果map中
                    addTransResultMap(transResultMap, platformResultMap);
                }
            }
        }

        // 如果没有需要回写的内容，返回null
        if (MapUtils.isEmpty(transResultMap)) return null;

        // 编辑中文标题内容
        Iterator<Map.Entry<String, String>> it = transResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Original_Title_Cn.equals(entry.getKey())) {
                if (blnNeedTransTitle) {
                    // 如果需要翻译就在翻译出来的中文标题掐面加上品牌(品牌不用翻译)
                    if (!StringUtils.isEmpty(entry.getValue())) {
                        entry.setValue(strBrand + " " + entry.getValue());
                    }
                } else {
                    // 如果不需要翻译标题的话就删除标题
                    it.remove();
                }
            }
        }

        // 生成更新SQL
        if (!MapUtils.isEmpty(transResultMap)) {
            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setQueryParameters(code);
            // 遍历结果map逐个添加回写更新用字段
            StringBuilder sbUpdate = new StringBuilder("{$set:{");
            int index = 0;
            for (Map.Entry<String, String> entry : transResultMap.entrySet()) {
                if (index == 0) {
                    sbUpdate.append("'common.fields." + entry.getKey() + "':'" + entry.getValue() + "'");
                } else {
                    sbUpdate.append(", 'common.fields." + entry.getKey() + "':'" + entry.getValue() + "'");
                }
                index++;
            }
            // 更新翻译状态，翻译者，翻译时间等项目
            sbUpdate.append(", 'common.fields.translateStatus':'1', 'common.fields.translator':#, 'common.fields.translateTime':#, 'common.fields.priorTranslateDate':''}}");

            updObj.setUpdate(sbUpdate.toString());
            updObj.setUpdateParameters(getTaskName(), DateTimeUtil.getNow());
            return updObj;
        }

        // 如果没有一个需要更新的中文项目值返回null
        return null;
    }

    /**
     * 将平台上返回的利用天猫同购方式翻译的结果放入到回写DB用最终结果集合中
     *
     * @param transResultMap    最终结果集合(去掉重复的标题等项目)
     * @param platformResultMap 平台上返回的翻译结果
     */
    protected void addTransResultMap(Map<String, String> transResultMap, Map<String, String> platformResultMap) {

        if (transResultMap == null || platformResultMap == null) return;

        for (Map.Entry<String, String> entry : platformResultMap.entrySet()) {
            if (!transResultMap.keySet().contains(entry.getKey())) {
                transResultMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 利用天猫同购上新方式更新商品属性
     *
     * @param numIIdForTransOnly  翻译专用商品id
     * @param titleEn        翻译前英文标题
     * @param strTransSrcEn  翻译前英文描述项目值
     * @param otherItemMap   更新商品时必须要填的一些项目的值(都有默认值，也可以不填)，如类目，主图地址，物流模板id，skus的json串等
     * @param productInfoMap 商品更新共通属性
     * @return String 更新成功之后返回商品id，更新失败时返回"ERROR:"开始的错误信息
     */
    protected String updateTransWare(String numIIdForTransOnly, String titleEn, String strTransSrcEn, Map<String, Object> otherItemMap, BaseMongoMap<String, String> productInfoMap, ShopBean shop) {

        String result;
        try {
            // 编辑天猫国际官网同购共通属性
            updateProductInfo(productInfoMap, titleEn, strTransSrcEn, otherItemMap);

            // 构造Field列表
            List<com.taobao.top.schema.field.Field> itemFieldList = new ArrayList<>();
            productInfoMap.entrySet().forEach(p -> {
                com.taobao.top.schema.field.InputField inputField = new com.taobao.top.schema.field.InputField();
                inputField.setId(p.getKey());
                inputField.setValue(p.getValue());
                itemFieldList.add(inputField);
            });

            // 转换成XML格式(root元素为<itemRule>)
            String productInfoXml = "";
            if (ListUtils.notNull(itemFieldList))
                productInfoXml = SchemaWriter.writeParamXmlString(itemFieldList);

            // 测试用输入XML内容
            $debug(productInfoXml);

            // 更新翻译专用商品
            result = tbSimpleItemService.updateSimpleItem(shop, NumberUtils.toLong(numIIdForTransOnly), productInfoXml);
        } catch (Exception e) {
            return null;
        }

        return result;
    }

    /**
     * 根据天猫官网同购返回的NumIId取得翻译之后的标题和描述
     *
     * @param shopProp 店铺信息
     * @param numIId 天猫官网同购上新成功之后返回的numIId
     * @param descriptionCnName 本次要翻译的描述的真实中文项目名
     * @return Map<String, String> 天猫官网同购翻译之后的标题和描述
     */
    protected Map<String, String> getTransResult(ShopBean shopProp, String numIId, String descriptionCnName) throws ApiException, GetUpdateSchemaFailException, TopSchemaException {
        // 调用官网同购编辑商品的get接口取得商品信息
        TbItemSchema tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, NumberUtils.toLong(numIId));
        if (tbItemSchema == null || ListUtils.isNull(tbItemSchema.getFields())) {
            // 天猫官网同购取得商品信息失败
            String errMsg = "天猫官网同购取得商品信息失败! ";
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        Map<String, String> pCatInfoMap = new HashMap<>(2, 1f);
        // 取得翻译后的标题
        InputField inputFieldTitle = (InputField)tbItemSchema.getFieldMap().get(Item_title);
        if (!StringUtils.isEmpty(inputFieldTitle.getDefaultValue())) {
            pCatInfoMap.put(Original_Title_Cn, inputFieldTitle.getDefaultValue());
        }

        // 取得翻译后的描述
        InputField inputFieldDescription = (InputField)tbItemSchema.getFieldMap().get(Item_description);
        if (!StringUtils.isEmpty(inputFieldDescription.getDefaultValue())
                && !pCatInfoMap.keySet().contains(descriptionCnName)) {
            pCatInfoMap.put(descriptionCnName, inputFieldDescription.getDefaultValue());
        }

        return pCatInfoMap;
    }

    /**
     * 设置天猫同购上新产品用共通属性
     *
     * @param titleEn  翻译前英文标题
     * @param strTransSrcEn  翻译前英文描述项目值
     * @param otherItemMap  更新商品时必须要填的一些项目的值(都有默认值，也可以不填)，如类目，品牌，主图地址，物流模板id，skus的json串等
     * @return BaseMongoMap<String, String> 有顺序的更新商品用字段和值的键值对
     * @throws BusinessException
     */
    public void updateProductInfo(BaseMongoMap<String, String> productInfo, String titleEn, String strTransSrcEn, Map<String, Object> otherItemMap) throws BusinessException {
        if (productInfo == null) return;

        productInfo.entrySet().forEach(p -> {
            switch (p.getKey()) {
                case Item_title:
                    // 更新标题
                    p.setValue(titleEn);
                    break;
                case Item_category:
                    // 更新类目
                    if (!MapUtils.isEmpty(otherItemMap) && otherItemMap.keySet().contains(Item_category)) {
                        p.setValue(StringUtils.toString(otherItemMap.get(Item_category)));
                    }
                    break;
                case Item_main_images:
                    // 更新主图
                    if (!MapUtils.isEmpty(otherItemMap) && otherItemMap.keySet().contains(Item_main_images)) {
                        p.setValue(StringUtils.toString(otherItemMap.get(Item_main_images)));
                    }
                    break;
                case Item_description:
                    // 更新描述(可能是longDesEn,shortDesEn或usageEn等待翻译英文项目的值)
                    p.setValue(strTransSrcEn);
                    break;
                case Item_template_id:
                    // 更新物流模板id
                    if (!MapUtils.isEmpty(otherItemMap) && otherItemMap.keySet().contains(Item_template_id)) {
                        p.setValue(StringUtils.toString(otherItemMap.get(Item_template_id)));
                    }
                    break;
                case Item_skus:
                    // 更新skus
                    if (!MapUtils.isEmpty(otherItemMap) && otherItemMap.keySet().contains(Item_skus)) {
                        p.setValue(StringUtils.toString(otherItemMap.get(Item_skus)));
                    }
                    break;
            }
        });

    }

    /**
     * 设置天猫同购上新产品用共通属性
     *
     * @return BaseMongoMap<String, String> 有顺序的更新商品用字段和值的键值对
     * @throws BusinessException
     */
    public BaseMongoMap<String, String> getProductCommonInfo() throws BusinessException {
        // 上新产品信息保存map
        BaseMongoMap<String, String> productInfoMap = new BaseMongoMap<>();

        // 标题(必填)
        // 商品标题支持英文到中文，韩文到中文的自动翻译，可以在extends字段里面进行设置是否需要翻译
        productInfoMap.put(Item_title, "Fossil Women's White Resin Bracelet Watch"); // 默认值，后面会更新成具体的值

        // 子标题(卖点)(非必填)
//        String valSubTitle = ""
//        productInfoMap.put("sub_title", valSubTitle);

        // 类目(必填)
        // 注意：使用天猫授权类目ID发布时，必须使用叶子类目的ID
        // 注意：使用商家自有系统类目路径发布时，不同层级的类目，使用&gt;进行分隔；使用系统匹配时，会有一定的badcase,
        //      商品上架前，建议商家做自查，查看商品是否被匹配到了正确的类目
//        String valCategory = "tmall category 1";
        String valCategory = "{\"cat_id\":\"121454006\"}";
        productInfoMap.put(Item_category, valCategory);   // 天猫平台一级类目

        // 商品属性(非必填)
        // 商品的一些属性，可以通过property字段来进行填写，该字段为非必填字段，如果商家有对商品的更为具体的属性信息，
        // 可以都进行填写。格式为"Key":"Value"
        // 注意：所有的属性字段，天猫国际系统会进行自动匹配，如果是天猫对应的该类目的标准属性，则会显示在商品的detail页面
//        String valProperty = "";
//        productInfoMap.put("property", valProperty);

        // 品牌(必填)
        // 商品品牌的值只支持英文，中文
        // 注意：天猫国际系统会进行品牌的匹配，部分品牌因在天猫品牌库中不存在，因为不一定全部品牌都能成功匹配。
        //      匹配成功的品牌，会出现在商品详情页面;
        //      如果无法匹配，且商家希望显示品牌的，建议商家通过商家后台申请新品牌。
        // 不用填品牌，如果这里填了品牌的话，翻译出来的标题里面天猫会自动在后面加上品牌
//        String valBrand = "dolan bullock";
//        if (!MapUtils.isEmpty(otherItemMap) && otherItemMap.keySet().contains("brand")) {
//            valBrand = StringUtils.toString(otherItemMap.get("brand"));
//        }
//        productInfoMap.put("brand", valBrand);

        // 主图(必填)
        // 最少1张，最多5张。多张图片之间，使用英文的逗号进行分割。需要使用alicdn的图片地址。建议尺寸为800*800像素。
//        String valMainImages = "http://img.alicdn.com/imgextra/i3/2640015666/TB2RySHaGnyQeBjy1zkXXXmyXXa_!!2640015666.jpg";
        String valMainImages = "http://img.alicdn.com/imgextra/i4/2939402618/TB2yzhLal0kpuFjy1XaXXaFkVXa-2939402618.jpg";
        productInfoMap.put(Item_main_images, valMainImages);

        // 描述(必填)
        // 商品描述支持HTML格式，但是需要将内容变成XML格式。
        // 为了更好的用户体验，建议全部使用图片来做描述内容。描述的图片宽度不超过800像素.
        productInfoMap.put(Item_description, "");  // 这里填入的时候其实就是要翻译的英文内容，更新之后就变成了翻译后内容

        // 物流信息(必填)
        // 物流字段：weight值为重量，用于计算运费，单位是千克;
        //         volumn值为体积，由于运费都是基于重量，这个值可以随便填写;
        //         templete_id为物流模板;
        //         province&city值，非港澳台的地区，直接填写中文的国家即可;
        //         start_from为货源地;
        // 格式:<value>{"weight":"1.5","volume":"0.0001","template_id":"243170100","province":"美国","city":"美国"}</value>
        Map<String, Object> paramLogistics = new HashMap<>();
        // 物流重量
        paramLogistics.put("weight", "1");
        // 物流体积
        paramLogistics.put("volume", "1");
        // 物流模板ID
//        String templateId = "5170259220";  // 运费模板必须要设置
        String templateId = "8421670741";  // 运费模板必须要设置
        paramLogistics.put(Item_template_id, templateId);
        // 物流模板是否是包邮模板(只能整个店铺共通设置一个，画面上不需要,不用加在共通schema里面)
//        if ("true".equalsIgnoreCase(shipFlag)) {
//            // 是否包邮设置为包邮("2":包邮)
//            // 参加活动天猫给付费广告位的话，还需要商品收藏数达到一定量才可以.
//            paramLogistics.put("ship", "2");
//        }
        // 省(国家)
        paramLogistics.put("province", "美国");
        // 城市
        paramLogistics.put("city", "美国");
        // 货源地
        paramLogistics.put("start_from", "美国");

        productInfoMap.put("logistics", JacksonUtil.bean2Json(paramLogistics));

        // skus(必填)
//        String skus = "{\"sale_prop\":{\"color\":\"SCL020400\",\"size\":\"OneSize\"},\"price\":\"2026.0\",\"outer_id\":\"SCL020400\",\"quantity\":1,\"image\":\"http://img.alicdn.com/imgextra/i4/2640015666/TB2JlduXhaK.eBjSZFAXXczFXXa_!!2640015666.jpg\",\"hscode\":\"9404909000\"}";
        String skus = "[{\"sale_prop\":{\"color\":\"白色\",\"size\":\"OneSize\"},\"price\":\"9879.0\",\"outer_id\":\"024-15765904-000-000\",\"quantity\":0,\"image\":\"http://img.alicdn.com/imgextra/i1/2939402618/TB2ttpPaa8lpuFjy0FpXXaGrpXa-2939402618.jpg\"}]";
        productInfoMap.put(Item_skus, skus);

        // 扩展(部分必填)
        // 该字段主要控制商品的部分备注信息等，其中部分字段是必须填写的，非必填的字段部分可以完全不用填写。
        // 且其中的部分字段，可以做好统一配置，做好配置的，不需要每个商品发布时都提交.
        Map<String, Object> paramExtends = new HashMap<>();
        // 官网来源代码(必填)   商品详情中会显示来源的国家旗帜
        // 主要国家代码：美国/US 英国/UK 澳大利亚/AU 加拿大/CA 德国/DE 西班牙/ES 法国/FR 香港/HK 意大利/IT 日本/JP
        //             韩国/KR 荷兰/NL 新西兰/NZ 台湾/TW 新加坡/SG
        paramExtends.put("nationality", "US");
        // 币种代码(必填)      用于区分币种，选择后会根据price值和币种，根据支付宝的汇率计算人民币价格
        // 主要币种代码：人民币/CNY 港币/HKD 新台币/TWD 美元/USD 英镑/GBP 日元/JPY 韩元/KRW 欧元/EUR 加拿大元/CAD
        //             澳元/AUD 新西兰元/NZD
        paramExtends.put("currency_type", "CNY");
        // 是否需要自动翻译(必填)  (如果有配置优先使用配置项目，没有配置的时候如果标题是中文，那么就是false，否则就是true)
        paramExtends.put("translate", "true");
        // 商品原始语言(必填)
        // 主要语言代码为：中文/zh 中文繁体/zt 英文/en 韩文/ko
        paramExtends.put("source_language", "en");
        // 官网名称(必填)     网站的名称，如果美国某某网站，可以做配置，配置后，可以不需要发布的时候填写
        paramExtends.put("website_name", "OverStock.com 美国官网发货");
        // 官网商品地址(必填)  商品在海外网址的地址，如果无法确保一一对应，可以先填写网站url
        paramExtends.put("website_url", "https://www.overstock.com");
        // 参考价格(非必填)    商品的参考价格，如果大于现在的价格，则填写
//        paramExtends.put("foreign_origin_price", getValueFromPageOrCondition("extends_foreign_origin_price", "", mainProductPlatformCart, sxData, shopProp));
        // 是否使用原标题(false自动插入商品关键词）(非必填)  填写true表示使用原始标题，false表示需要插入关键词，不填写默认为不需要插入关键词
        // 这里如果设置false的话，天猫就会自动在翻译后的标题后面加上"美国官网直邮进口"之类的关键词
        paramExtends.put("original_title", "true");
        // 店铺内分类id(非必填)  格式："shop_cats":"111111,222222,333333"
//        paramExtends.put("shop_cats", extends_shop_cats);
        // 是否包税(非必填)     标识是否要报税，true表示报税，false表示不报税，不填写默认为不报税
        // 注意：跨境申报的商品，是不允许报税的，即便设置了报税，商品在交易的过程中，也需要支付税费
        paramExtends.put("tax_free", "false");
        // 尺码表图片(非必填)    暂不设置
//        paramExtends.put("international_size_table", getValueFromPageOrCondition("extends_international_size_table", "", mainProductPlatformCart, sxData, shopProp));
        // 是否单独发货(非必填)   true表示单独发货，false表示需要不做单独发货，不填写默认为不单独发货
        paramExtends.put("delivery_separate", "false");
        // 是否支持退货
        paramExtends.put("support_refund", "false");
        // 官网是否热卖(非必填)    表示是否在官网是热卖商品，true表示热卖，false表示非热卖，不填写默认为非热卖
        paramExtends.put("hot_sale", "false");
        // 在官网是否是新品(非必填) 表示是否在官网是新品，true表示新品，false表示非新品，不填写默认为非新品
        paramExtends.put("new_goods", "false");
        // 入关方式 跨境申报/邮关(必填)     true表示跨境申报，false表示邮关申报
        // 根据中国海关4月8日的最新规定，天猫国际的商品必须设置入关方式，入关方式有2种：
        // 1.跨境申报，即每单交易都向海关申报，单单交税；
        // 2.邮关申报，即通过万国邮联的方式快递包裹，有几率抽检；
        // 说明：设置成跨境申报后，商品的每个SKU必须带有HSCODE才可以上架，设置成邮关申报后，商品不需要设置HSCODE
//        paramExtends.put("cross_border_report", "true");   // 这里设成true了之后，前面的skus里面就必须要设置hsCode
        paramExtends.put("cross_border_report", "false");

        productInfoMap.put("extends", JacksonUtil.bean2Json(paramExtends));

        // 无线描述(选填)
        // 解析cms_mt_platform_dict表中的数据字典
//        if (mainProduct.getCommon().getFields().getAppSwitch() != null &&
//                mainProduct.getCommon().getFields().getAppSwitch() == 1) {
//            productInfoMap.put("wireless_desc", getValueByDict("天猫同购无线描述", expressionParser, shopProp));
//        }

        // 商品上下架
        productInfoMap.put("status", "2");   // 商品在库

        return productInfoMap;
    }

}
