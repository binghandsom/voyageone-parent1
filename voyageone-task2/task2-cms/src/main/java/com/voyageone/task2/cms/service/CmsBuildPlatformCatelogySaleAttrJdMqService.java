package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdCategoryService;
import com.voyageone.service.impl.cms.CmsMtPlatformSkusService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 取得京东类目销售属性(颜色和尺码)MQ服务
 *
 * 取得指定渠道id和平台id下指定叶子类目或京东类目tree表中中所有叶子类目的销售(颜色和尺码)属性,并回写到cms_mt_platform_skus表中
 * 当京东平台上指定类目的颜色或者尺码的属性值不存在时，会往平台上补足属性值(颜色补足24个属性值，尺码补足30个属性值(尺码属性值上限54个，30个不够可以再加))
 *
 * @author desmond on 2016/12/14
 * @version 2.10.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_TASK_CatelogySaleAttrJdJob)
public class CmsBuildPlatformCatelogySaleAttrJdMqService extends BaseMQCmsService {

    // 颜色和尺码销售属性都不存在
    private final static String None_Color_Size = "0";
    // 颜色和尺码销售属性都存在时
    private final static String Both_Color_Size = "1";
    // 只有颜色没有尺码销售属性时
    private final static String Only_Color = "2";
    // 没有颜色只有尺码销售属性时
    private final static String Only_Size = "3";
    // SKU属性类型(颜色)
    private final static String AttrType_Color = "c";
    // SKU属性类型(尺寸)
    private final static String AttrType_Size = "s";

    // 24个颜色列表(跟京东平台上的顺序一致)
    private final static Map<String, String> colorAttrValueMap = new LinkedHashMap() {{
        put("红色", "#FF0000");
        put("深红色", "#990000");
        put("橙色", "#FF9900");
        put("黄色", "#FFFF00");
        put("浅黄色", "#FFFF99");
        put("草绿色", "#66CC33");
        put("绿色", "#339933");
        put("浅蓝色", "#0099FF");
        put("蓝色", "#0033CC");
        put("深蓝色", "#000066");
        put("浅紫色", "#CC99FF");
        put("紫色", "#660066");
        put("玫瑰红色", "#FF6666");
        put("粉红色", "#FF0066");
        put("卡其色", "#CCCC66");
        put("茶色", "#996600");
        put("褐色", "#663300");
        put("军绿色", "#336600");
        put("天蓝色", "#CCFFFF");
        put("荧光色", "#CCFF00");
        put("白色", "#FFFFFF");
        put("浅灰色", "#CCCCCC");
        put("灰色", "#666666");
        put("黑色", "#000000");
    }};

    @Autowired
    private CmsMtPlatformSkusService cmsMtPlatformSkusService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private JdCategoryService jdCategoryService;

    @Autowired
    public CmsBuildPlatformCatelogySaleAttrJdMqService(CmsMtPlatformSkusService cmsMtPlatformSkusService, PlatformCategoryService platformCategoryService, JdCategoryService jdCategoryService) {
        this.cmsMtPlatformSkusService = cmsMtPlatformSkusService;
        this.platformCategoryService = platformCategoryService;
        this.jdCategoryService = jdCategoryService;
    }

    /**
     * 入口
     * 输入参数:
     *     channelId：渠道ID（必须）
     *     cartId：   平台ID (必须，只能是京东的平台：24(JD),26(JG),28(JGJ),29(JGY))
     *     catIdList: 叶子类目ID列表(非必须，不输入时取得全部叶子类目)
     *
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        $info("CmsBuildPlatformCatelogySaleAttrJdMqService start 参数 " + JacksonUtil.bean2Json(messageMap));

        // 参数1: 渠道id(必须)
        String channelId = null;
        // 参数2: 平台id(必须)
        String cartId = null;
        // 参数3: 叶子类目id列表(非必须)
        List<String> catIdList = null;
        if (MapUtils.isNotEmpty(messageMap)) {
            // 有参数时
            // 参数1:渠道id
            if (messageMap.containsKey("channelId")) {
                channelId = String.valueOf(messageMap.get("channelId"));
            }
            // 参数2:平台id
            if (messageMap.containsKey("cartId")) {
                cartId = String.valueOf(messageMap.get("cartId"));
            }
            // 参数3:叶子类目id (输入叶子类目id时，必须也输入渠道id和平台id；如果没有输入叶子类目ID,取得全部叶子类目的销售属性)
            if (messageMap.containsKey("catIdList")) {
                catIdList = (List<String>) messageMap.get("catIdList");
            }
        }

        // 检查输入参数
        // 参数1: 渠道id（输入值为空时，报错）
        if (StringUtils.isEmpty(channelId)) {
            $error("取得京东类目销售属性(颜色和尺码)(MQ): 输入参数渠道id(channelId:%s)为空!", channelId);
            return;
        }
        // 参数2: 平台id（如果不是京东系的平台，报错）
        if (!StringUtils.isEmpty(cartId)) {
            CartEnums.Cart jdCart = CartEnums.Cart.getValueByID(cartId);
            if (jdCart == null) {
                $error("取得京东类目销售属性(颜色和尺码)(MQ): 输入参数平台id(cartId:%s)不是合法的平台!", cartId);
                return;
            }

            // 检查是不是京东系的平台"24(JD),26(JG),28(JGJ),29(JGY)"
            if (!CartEnums.Cart.isJdSeries(jdCart)) {
                $error("取得京东类目销售属性(颜色和尺码)(MQ): 输入参数平台id(cartId:%s)不是京东系的平台!", cartId);
                return;
            }
        } else {
            $error("取得京东类目销售属性(颜色和尺码)(MQ): 输入参数平台id(cartId:%s)为空!", cartId);
            return;
        }

        // 获取店铺信息
        ShopBean shop = Shops.getShop(channelId, cartId);
        if (shop == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        try {
            // 取得当前指定渠道和平台的京东销售属性(颜色和尺码)信息，并回写到并回写到cms_mt_platform_skus表中
            doGetJdCategorySaleAttr(shop, channelId, cartId, catIdList);
        } catch (Exception e) {
            if (StringUtils.isEmpty(e.getMessage())) {
                $error("CmsBuildPlatformCatelogySaleAttrJdMqService 异常发生!");
                e.printStackTrace();
            } else {
                $error("CmsBuildPlatformCatelogySaleAttrJdMqService 异常发生!" + e.getMessage());
            }
        }

        $info("CmsBuildPlatformCatelogySaleAttrJdMqService success end");
    }

    /**
     * 取得指定渠道和平台的京东类目销售属性(颜色和尺码)回写到cms_mt_platform_skus表中
     *
     * @param channelId 渠道id
     * @param cartId    平台id
     * @param catIdList 叶子类目id列表(如果为空，则取得所有该平台类目schema表里所有的叶子类目id)
     */
    protected void doGetJdCategorySaleAttr(ShopBean shop, String channelId, String cartId, List<String> catIdList) throws Exception {
        if (shop == null || StringUtils.isEmpty(channelId) || StringUtils.isEmpty(cartId)) return;
        $info("取得京东平台叶子类目的销售属性开始! [channelId:%s] [cartId:%s] [catIdList:%s]", channelId, cartId,
                ListUtils.isNull(catIdList) ? "空" : Joiner.on(",").join(catIdList));

        // 取得voyageone_cms2.cms_mt_platform_skus表中当前渠道平台已经追加过的颜色和尺码件数信息(件数放在idx中返回)
        List<CmsMtPlatformSkusModel> existsPlatfomSkus = cmsMtPlatformSkusService.getCategorySaleAttrCount(channelId,
                NumberUtils.toInt(cartId));

        // 如果指定MQ消息里指定了叶子类目id列表，则使用指定的叶子类目id列表;没有指定叶子类目id列表时，则从类目tree表中取得全部的类目id
        if (ListUtils.isNull(catIdList)) {
            if (catIdList == null) catIdList = new ArrayList<>();
            // 从类目tree表中取得全部去掉重复的叶子类目
            // 注意：不能从类目schema表中取得叶子类目id，因为类目schema表中的旧的(在京东平台上已经不存在的)叶子类目id不会被删掉
            List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves =
                    platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(NumberUtils.toInt(cartId));
            // 不能从类目schema表中取得叶子类目id，因为类目schema表中的旧的(在京东平台上已经不存在的)叶子类目id不会被删掉
            if (ListUtils.notNull(allCategoryTreeLeaves)) {
                catIdList.addAll(allCategoryTreeLeaves.stream().map(model -> model.getCatId()).collect(Collectors.toList()));
            }
        }

        // 如果没有指定叶子类目id或没有在平台类目schema表中找到类目Id时候，直接返回
        if (ListUtils.isNull(catIdList)) {
            $warn("取得京东平台叶子类目的销售属性时，从输入参数或者类目tree表中取得的对象叶子类目id列表为空，不需要取得销售属性!");
            return;
        }

        // 循环叶子类目id看是否需要取得销售属性
        for (String catIdObj : catIdList) {
            // cms_mt_platform_skus表颜色和尺码现状(0:都不存在 1:颜色和尺码都存在 2:只有颜色没有尺码 3:没有颜色只有尺码 null:叶子类目id为空)
            String skusSaleAttrStatus = getSaleAttrExistsStatus(catIdObj, existsPlatfomSkus);
            // 如果该类目在cms_mt_platform_skus表中颜色和尺码属性值都存在，跳过
            if (StringUtils.isEmpty(skusSaleAttrStatus) || Both_Color_Size.equals(skusSaleAttrStatus)) {
                continue;
            }

            // 调用京东API取得类目销售(颜色和尺码)属性信息,如果"类目不存在"或者"没有销售属性"时跳过(例如：6166 珠宝首饰>钻石>钻石DIY定制 不存在)
            Map<String, CategoryAttributeSearchResponse.Attribute> salePropAttrMap = getJdSaleAttrByCatId(shop, catIdObj);
            if (MapUtils.isEmpty(salePropAttrMap)) {
                continue;
            }

            // 如果京东平台上该类目只有颜色且cms_mt_platform_skus表也只有颜色，或者，平台上只有尺码且skus表中也只有尺码时，跳过
            if ((salePropAttrMap.containsKey(AttrType_Color) && !salePropAttrMap.containsKey(AttrType_Size) && Only_Color.equals(skusSaleAttrStatus))
                    || (!salePropAttrMap.containsKey(AttrType_Color) && salePropAttrMap.containsKey(AttrType_Size) && Only_Size.equals(skusSaleAttrStatus))) {
                continue;
            }

            // 查询颜色和尺寸属性id在平台上有没有属性值，如果没有属性值就补足相应的颜色或尺码属性值
            for (Map.Entry<String, CategoryAttributeSearchResponse.Attribute> entry : salePropAttrMap.entrySet()) {
                // 取得类目属性id下面的属性值列表
                List<AttValue> jdCategoryAttrValueList = getJdSaleAttrValueList(shop, entry.getValue().getAid());
                // 如果没有成功取得属性值列表，就向平台追加相应的颜色或尺码属性值
                if (ListUtils.isNull(jdCategoryAttrValueList)) {
                    // 补足颜色或尺码属性值到平台(颜色添加为24个，尺码添加30个(尺码最多能加54个，30个不够的时候再加))
                    addJdCategoryColorAttrValue(shop, catIdObj, entry.getKey(), entry.getValue().getAid());
                }
            }

            // 如果cms_mt_platform_skus原本不是颜色和尺码记录都没有时(颜色和尺码都有时前面已经跳过)，先删除该类目对应的记录再插入
            if (!None_Color_Size.equals(skusSaleAttrStatus)) {
                // 删除原有的错误记录
                cmsMtPlatformSkusService.deleteCategorySaleAttr(channelId, NumberUtils.toInt(cartId), catIdObj);
            }

            // 取得颜色或尺码属性值列表并回写到voyageone_cms2.cms_mt_platform_skus表中
            for (Map.Entry<String, CategoryAttributeSearchResponse.Attribute> entry : salePropAttrMap.entrySet()) {
                // 取得类目属性id下面的属性值列表(按IndexId升序排列)
                List<AttValue> jdCategoryAttrValueList = getJdSaleAttrValueList(shop, entry.getValue().getAid());
                if (ListUtils.notNull(jdCategoryAttrValueList)) {
                    // 将平台上取得的颜色和尺码属性值回写到回写到voyageone_cms2.cms_mt_platform_skus表
                    addCategoryAttrValueToSkus(channelId, cartId, catIdObj, entry.getKey(), entry.getValue().getAid(),
                            jdCategoryAttrValueList);
                }
            }
        }

        $info("取得京东平台叶子类目的销售属性结束! 取得的颜色和尺码信息成功插入voyageone_cms2.cms_mt_platform_skus表!");
    }

    /**
     * 取得指定类目在cms_mt_platform_skus表中的颜色和尺码记录存在状态
     *
     * @param catId    叶子类目id
     * @param existsPlatfomSkus cms_mt_platform_skus中已经存在的颜色和尺码件数列表
     * @return String 颜色和尺码现状(0:都不存在 1:颜色和尺码都存在 2:只有颜色没有尺码 3:没有颜色只有尺码 null:叶子类目id为空)
     */
    protected String getSaleAttrExistsStatus(String catId, List<CmsMtPlatformSkusModel> existsPlatfomSkus) {

        if (StringUtils.isEmpty(catId)) return null;

        if (ListUtils.isNull(existsPlatfomSkus)
                || existsPlatfomSkus.stream().filter(s -> catId.equals(s.getPlatformCategoryId())).count() == 0) {
            return None_Color_Size;
        }

        // 该叶子类目id在cms_mt_platform_skus中已经存在的颜色件数
        int colorAttrCnt = existsPlatfomSkus
                .stream()
                .filter(s -> catId.equals(s.getPlatformCategoryId()) && AttrType_Color.equals(s.getAttrType()))
                .findFirst()
                .map(s -> s.getIdx())
                .orElse(0);

         // 该叶子类目id在cms_mt_platform_skus中已经存在的尺码件数
        int sizeAttrCnt = existsPlatfomSkus
                .stream()
                .filter(s -> catId.equals(s.getPlatformCategoryId()) && AttrType_Size.equals(s.getAttrType()))
                .findFirst()
                .map(s -> s.getIdx())
                .orElse(0);

        if (colorAttrCnt > 0 && sizeAttrCnt > 0) {
            // 颜色和尺码记录都存在
            return Both_Color_Size;
        } else if (colorAttrCnt > 0 && sizeAttrCnt == 0) {
            // 只有颜色记录
            return Only_Color;
        } else if (colorAttrCnt == 0 && sizeAttrCnt > 0) {
            // 只有尺码记录
            return Only_Size;
        }

        return None_Color_Size;
    }

    /**
     * 取得指定类目在京东平台上销售属性中的颜色和尺码的属性分组信息
     *
     * @param shop 店铺信息
     * @param catId    叶子类目id
     * @return Map<String, CategoryAttributeSearchResponse.Attribute> 颜色和尺码分组信息
     */
    protected Map<String, CategoryAttributeSearchResponse.Attribute> getJdSaleAttrByCatId(ShopBean shop, String catId) {

        Map<String, CategoryAttributeSearchResponse.Attribute> salePropAttrMap = new HashMap<>();
        // 从京东平台上取得当前叶子类目id的销售属性信息
        List<CategoryAttributeSearchResponse.Attribute> jdCategoryAttrList;

        try {
            // 调用京东商家类目属性信息API查询销售属性信息(jingdong.category.read.findValuesByAttrId)
            jdCategoryAttrList = jdCategoryService.getCategoryAttrInfo(shop, catId, "true");
            if (ListUtils.notNull(jdCategoryAttrList)) {
                // 将类目属性按颜色和尺寸分组
                CategoryAttributeSearchResponse.Attribute colorAttr = jdCategoryAttrList
                        .stream()
                        .filter(s -> s.getSaleProp() && s.isColorProp())
                        .findFirst()
                        .orElse(null);
                if (colorAttr != null) salePropAttrMap.put(AttrType_Color, colorAttr);

                CategoryAttributeSearchResponse.Attribute sizeAttr = jdCategoryAttrList
                        .stream()
                        .filter(s -> s.getSaleProp() && s.isSizeProp())
                        .findFirst()
                        .orElse(null);
                if (sizeAttr != null) salePropAttrMap.put(AttrType_Size, sizeAttr);
            }
        } catch (Exception e) {
            String errMsg = String.format("取得京东类目属性失败！可能是catId(%s)在京东平台不存在. [errMsg:%s]", catId, e.getMessage());
            $error(errMsg);
            return null;
        }

        return salePropAttrMap;
    }

    /**
     * 取得指定类目属性id下面的属性值列表
     *
     * @param shop 店铺信息
     * @param categoryAttrId    属性id
     * @return List<AttValue> 属性值列表(按IndexId升序排列)
     */
    protected List<AttValue> getJdSaleAttrValueList(ShopBean shop, Long categoryAttrId) {

        // 从京东平台上取得属性id的属性值列表
        List<AttValue> jdCategoryAttrValueList = null;

        try {
            // 调用京东商家类目属性信息API(jingdong.category.read.findValuesByAttrId)
            jdCategoryAttrValueList = jdCategoryService.getCategoryAttrValueInfo(shop, categoryAttrId);
            if (ListUtils.notNull(jdCategoryAttrValueList)) {
                // 按属性值结果列表按IndexId升序排列
                jdCategoryAttrValueList = jdCategoryAttrValueList
                        .stream()
                        .sorted(Comparator.comparing(a -> a.getIndexId()))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            String errMsg = String.format("取得京东类目属性id对应的属性值列表信息失败！[属性id:%s]", StringUtils.toString(categoryAttrId));
            $error(errMsg);
            return null;
        }

        return jdCategoryAttrValueList;
    }

    /**
     * 追加颜色或尺码属性值信息到京东平台销售属性
     * 说明：1.京东平台上每个类目可以添加的颜色和尺码销售属性上限为：颜色添加上限为24个，尺码添加上限为54个
     *      2.本方法实际添加的颜色和尺码销售属性件数为：颜色添加24个，尺码添加30个
     *        (关于尺码只添加30个的原因是因为，30个尺码对一般商品应该够用了，不够再个别加；每个都加54个的话，skus会有回写很多无用的数据)
     *
     * @param shop      店铺信息
     * @param catId     叶子类目id
     * @param attrType  销售属性类型（颜色c或尺码s）
     * @param attrId    类目属性id
     */
    protected void addJdCategoryColorAttrValue(ShopBean shop, String catId, String attrType, Long attrId) {

        try {
            // 追加颜色或尺码属性值信息到京东平台销售属性
            switch (attrType) {
                case AttrType_Color:
                    // 追加颜色属性值(24个)
                    Iterator<Map.Entry<String, String>> iterator = colorAttrValueMap.entrySet().iterator();
                    int colorIdx = 1; // 京东平台上所有销售属性值index都是从1开始的
                    while (iterator.hasNext()) {
                        Map.Entry entry = iterator.next();
                        // 追加颜色属性值到京东类目颜色销售属性
                        jdCategoryService.addWareVenderSellSku(shop, catId, colorIdx, StringUtils.toString(attrId),
                                StringUtils.toString(entry.getKey()), StringUtils.toString(entry.getValue()), "1");
                        colorIdx++;
                    }
                    break;
                case AttrType_Size:
                    // 追加尺码属性值(30个)
                    for (int sizeIdx = 1; sizeIdx <= 30; sizeIdx++) {
                        // 追加尺码属性值到京东类目尺码销售属性(不用填features)
                        jdCategoryService.addWareVenderSellSku(shop, catId, sizeIdx, StringUtils.toString(attrId),
                                "尺码" + sizeIdx, null, "1");
                    }
                    break;
            }
        } catch (Exception e) {
            String errMsg = String.format("追加颜色或尺码属性值到京东类目尺码销售属性失败！[errMsg:%s]", e.getMessage());
            $error(errMsg);
            return;
        }
    }

    /**
     * 将平台上取得的指定类目属性id下面的属性值列表信息回写到cms_mt_platform_skus表中
     *
     * @param channelId 渠道id
     * @param cartId    平台id
     * @param catId     叶子类目id
     * @param attrType  销售属性类型（颜色c或尺码s）
     * @param attrId    类目属性id
     * @param jdCategoryAttrValueList 类目属性值列表(按IndexId升序排列)
     */
    protected void addCategoryAttrValueToSkus(String channelId, String cartId, String catId, String attrType,
                                              Long attrId, List<AttValue> jdCategoryAttrValueList) {

        // 遍历类目属性值列表(按IndexId升序排列)，插入新的颜色和尺码记录到cms_mt_platform_skus表
        List<CmsMtPlatformSkusModel> skuModels = new ArrayList<>();
        String modifier = getTaskName();
        Date currentTime = DateTimeUtil.getDate();
        for (AttValue attValue : jdCategoryAttrValueList) {
            CmsMtPlatformSkusModel skuModel = new CmsMtPlatformSkusModel();
            skuModel.setChannelId(channelId);
            skuModel.setCartId(NumberUtils.toInt(cartId));
            skuModel.setPlatformCategoryId(catId);
            skuModel.setAttrType(attrType);
            skuModel.setIdx(NumberUtils.toInt(StringUtils.toString(attValue.getIndexId())));
            skuModel.setAttrName(attValue.getName());
            // 属性值(类目属性id + ":" + 类目属性值id)
            skuModel.setAttrValue(StringUtils.toString(attrId) + ":" + attValue.getVid());
            skuModel.setActive(true);
            skuModel.setCreated(currentTime);
            skuModel.setCreater(modifier);
            skuModel.setModified(currentTime);
            skuModel.setCreater(modifier);

            skuModels.add(skuModel);
        }

        // 插入cms_mt_platform_skus表
        cmsMtPlatformSkusService.insertCategorySaleAttrList(skuModels);
    }
}
