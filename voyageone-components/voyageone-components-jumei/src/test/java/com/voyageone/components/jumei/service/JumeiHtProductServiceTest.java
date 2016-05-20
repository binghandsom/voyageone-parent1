package com.voyageone.components.jumei.service;


import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtProductService;
import com.voyageone.components.jumei.bean.*;
import com.voyageone.components.jumei.reponse.HtProductAddResponse;
import com.voyageone.components.jumei.reponse.HtProductUpdateResponse;
import com.voyageone.components.jumei.request.HtProductAddRequest;
import com.voyageone.components.jumei.request.HtProductUpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtProductServiceTest {
    @Autowired
    JumeiHtProductService service;

    @Test
    public void addProductAndDealTest() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//        shopBean.setApp_url("http://182.138.102.82:8868/");
        shopBean.setApp_url("http://182.138.102.82:8823");

        HtProductAddRequest request = new HtProductAddRequest();
        JmProductBean jmProductBean = new JmProductBean();
        // 设置商品信息
        // 商品规格编号(必须)
        jmProductBean.setProduct_spec_number("10000");
        // 分类id(必须)
        jmProductBean.setCategory_v3_4_id(123456);
        // 品牌id(必须)
        jmProductBean.setBrand_id(110001);
        // 产品名(必须)
        // 注：100字以内，不能出现容量、规格、颜色等信息，这些信息在子型号中设置产品名上不能填写除了“（）”“/”“+”“*” 以外的特殊符号，
        // 如-，<>，· ，空格等符号必须是英文半角符号，套装产品名以“+”号连接
        jmProductBean.setName("产品名不能出现容量规格颜色等信息");
        // 外文名(必须)
        jmProductBean.setForeign_language_name("Product Foreign Language Name");
        // 产品功效ID(可选)，多个ID用 ","隔开
        jmProductBean.setFunction_ids("a001,a002");
        // 白底方图(必须)
        // 注：第一张必填,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开；
        // 图片上除了产品外不得出现任何其他信息（如水印、商标、优惠信息等)
        jmProductBean.setNormalImage("白底方图No1，白底方图No2");
        // 竖图(可选)
        // 注：可不传,最多10张，750*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
        jmProductBean.setVerticalImage("竖图1，竖图2");
        // 调性图片(可选)
        // 注：传一张,可不传，单张不超过1m，多张图片以","，1500*400格式jpg,jpeg,单张不超过1m
        jmProductBean.setDiaoxingImage("调性图片1");


        // 设置SPU(SKU)属性列表
        List<JmProductBean_Spus> jmSpus = new ArrayList<>();
        // spu1
        JmProductBean_Spus spu1 = new JmProductBean_Spus();
        // 商家spu_no(必须)
        spu1.setPartner_spu_no("1001");
        // 商品自带条码(可选)
        spu1.setUpc_code("111001");
        // 规格(必须): FORMAL 正装 MS 中小样 OTHER 其他
        spu1.setPropery("MS");
        // 容量/尺寸(必须)
        spu1.setSize("41");
        // 型号/颜色(可选)
        spu1.setAttribute("C01");
        // 海外价格(必须)
        spu1.setAbroad_price(50.0);
        // 货币符号Id(必须)
        spu1.setArea_code("01");
        // 海外地址(可不传)(可选)
        spu1.setAbroad_url("");
        // 白底方图(可选)
        // 注：可不传,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
//        spu1.setXXX();  // 不用上传SKU白底方图
        // 竖图(可选)
        // 注：可不传,最多10张，750*1000jpg,jpeg,单张不超过1m，多张图片以","隔开
        spu1.setVerticalImage("竖图1");

        // spu1的sku1信息
        // spu和sku是1对1的关系
        JmProductBean_Spus_Sku spu1_sku1 = new JmProductBean_Spus_Sku();
        // 商家自定义sku_no(必须)，请务必确保本次请求的sku_no唯一
        spu1_sku1.setPartner_sku_no("S002");
        // 海关备案商品编码(必须)
        // 注:(发货仓库为保税区仓库时，此处必填) 获取仓库接口　增加返回bonded_area_id字段 大于０　表示　保税
        spu1_sku1.setCustoms_product_number("S001001");
        // 商家商品编码(必须)  注:确保唯一
        spu1_sku1.setBusinessman_num("SJ001");
        // 库存(必须)  注：填写可供售卖的真实库存，无货超卖可能引起投诉与退款。无库存填写0
        spu1_sku1.setStocks("150");
        // 团购价(必须)  注：至少大于15元
        spu1_sku1.setDeal_price("310");
        // 市场价(必须)  注：必须大于等于团购价
        spu1_sku1.setMarket_price("500");
        // spu(sku)
        spu1.setSkuInfo(spu1_sku1);
        // 追加spu(sku)
        jmSpus.add(spu1);  // spu1(sku1)

        // spu2
        JmProductBean_Spus spu2 = new JmProductBean_Spus();
        // 商家spu_no(必须)
        spu2.setPartner_spu_no("1002");
        // 商品自带条码(可选)
        spu2.setUpc_code("111002");
        // 规格(必须): FORMAL 正装 MS 中小样 OTHER 其他
        spu2.setPropery("OTHER");
        // 容量/尺寸(必须)
        spu2.setSize("45");
        // 型号/颜色(可选)
        spu2.setAttribute("C02");
        // 海外价格(必须)
        spu2.setAbroad_price(60.0);
        // 货币符号Id(必须)
        spu2.setArea_code("01");
        // 海外地址(可不传)(可选)
        spu2.setAbroad_url("");
        // 白底方图(可选)
        // 注：可不传,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
//        spu1.setXXX();  // 不用上传SKU白底方图
        // 竖图(可选)
        // 注：可不传,最多10张，750*1000jpg,jpeg,单张不超过1m，多张图片以","隔开
        spu2.setVerticalImage("竖图2");

        // spu1的sku1信息
        // spu和sku是1对1的关系
        JmProductBean_Spus_Sku spu2_sku2 = new JmProductBean_Spus_Sku();
        // 商家自定义sku_no(必须)，请务必确保本次请求的sku_no唯一
        spu2_sku2.setPartner_sku_no("S002");
        // 海关备案商品编码(必须)
        // 注:(发货仓库为保税区仓库时，此处必填) 获取仓库接口　增加返回bonded_area_id字段 大于０　表示　保税
        spu2_sku2.setCustoms_product_number("S002002");
        // 商家商品编码(必须)  注:确保唯一
        spu2_sku2.setBusinessman_num("SJ002");
        // 库存(必须)  注：填写可供售卖的真实库存，无货超卖可能引起投诉与退款。无库存填写0
        spu2_sku2.setStocks("160");
        // 团购价(必须)  注：至少大于15元
        spu2_sku2.setDeal_price("330");
        // 市场价(必须)  注：必须大于等于团购价
        spu2_sku2.setMarket_price("550");
        // spu(sku)
        spu2.setSkuInfo(spu2_sku2);
        // 追加spu(sku)
        jmSpus.add(spu2);  // spu2(sku2)

        // spus设置到product里
        jmProductBean.setSpus(jmSpus);


        // 设置Deal属性列表
        JmProductBean_DealInfo dealInfo = new JmProductBean_DealInfo();
        // 商家自定义deal_id(必须)
        dealInfo.setPartner_deal_id("P0001");
        // Deal开始时间(必须)
        // 注：开始时间不能大于结束时间 售卖时间与当前deal对应的product下的所有deal的售卖时间对比，时间不能有交集
        dealInfo.setStart_time(new Date().getTime());
        // Deal结束时间(必须)
        // 注：开始时间不能大于结束时间 售卖时间与当前deal对应的product下的所有deal的售卖时间对比，时间不能有交集
        dealInfo.setEnd_time(new Date().getTime() + 100000000);
        // 限购数量(可选)
        dealInfo.setUser_purchase_limit(2);
        // 仓库ID(必须)
        dealInfo.setShipping_system_id(12001);
        // 产品长标题(必须)  注：130　用于详情页显示，商品名+功效特点描述，不能出现价格及促销信息
        dealInfo.setProduct_long_name("产品长标题");
        // 产品中标题(必须)  注：35字　用于首页、列表页显示，填写商品名+功效的一句话描述，不能出现价格及促销信息
        dealInfo.setProduct_medium_name("产品中标题");
        // 产品短标题(必须)  注：15字　用于购物车、移动客户端等，填写产品名如果字数超过，需酌情删减部分信息
        dealInfo.setProduct_short_name("产品短标题");
        // 生产地区(必须)    注：150字以内
        dealInfo.setAddress_of_produce("泰国");
        // 保质期限(必须)    注：150字以内
        dealInfo.setBefore_date("2018/01/01 00:00:00.000");
        // 适用人群(必须)    注：150字以内
        dealInfo.setSuit_people("老少咸宜");
        // 特殊说明(必须)    注：150字以内
        dealInfo.setSpecial_explain("特殊说明内容");
        // 自定义搜索词(必须)
        dealInfo.setSearch_meta_text_custom("潮人");
        // 本单详情(必须)
        // 注：富文本HTML代码,不能引用外链、不能用JS代码;
        //    1）所有图片不要使用中国模特，如有代言人必须是当年的，为避免后期旧品上架困难，也请尽量不要使用。图片宽660 - 790px，下同￿
        //    2）文字说明请放在本单详情底部，字体要求：黑色，16号，微软雅黑。
        dealInfo.setDescription_properties("本单详情富文本HTML代码");
        // 使用方法(必须)  富文本HTML代码
        // 注：富文本HTML代码,不能引用外链、不能用JS代码;
        // 1）简要描述产品的使用方法、使用步骤、使用中可能出现的不良反应等，最好有示意图。￿
        // 2）如果是套装需要逐条写上使用方法，每个产品名加粗。字体要求：黑色，12号，宋体。￿
        // 3）护肤品必须在最后附上“温馨提示：护肤品成分各有不同，敏感性肌肤请先在耳后测试后再使用哦！”，否则审核不能通过。
        dealInfo.setDescription_usage("使用方法 富文本HTML代码");
        // 商品实拍(必须)
        // 注：富文本HTML代码,不能引用外链、不能用JS代码；
        // 1）每一张实拍图下最好有一行文字说明，字体要求：黑色，16号， 微软雅黑。 实拍图模板下载（带尺子）》￿
        // 2）带包装和不带包装的正面、侧面、背面图都要有，包装上的文字必须清晰可辨。至少一张带尺子的实拍，让用户直观了解到商品尺寸。￿
        // 3）多个SKU时可合在一起拍也可分开拍摄，彩妆除外观实拍外还需要有近距离效果试用图。
        dealInfo.setDescription_images("商品实拍富文本HTML代码");
        // 商家自定义skuInfo下的partner_sku_no(必须)   (Json 多个sku_no 用 "," 隔开)
        dealInfo.setPartner_sku_nos("partner_sku_no1,partner_sku_no2");
        // 设置DealInfo
        jmProductBean.setDealInfo(dealInfo);

        // 设置request产品信息
        request.setJmProduct(jmProductBean);

        HtProductAddResponse response = service.addProductAndDeal(shopBean, request);
        if (response != null && response.getIs_Success()) {
            // 新增产品成功
            String reponseBody = response.getBody();

        } else {
            // 新增产品失败

        }
    }

    @Test
    public void updateTest() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//        shopBean.setApp_url("http://182.138.102.82:8868/");
        shopBean.setApp_url("http://182.138.102.82:8823");

        HtProductUpdateRequest request = new HtProductUpdateRequest();
        // 聚美商品ID(可选)
        // 大于0的整数。jumei_product_id 和 jumei_product_name 必须存在一个;都存在时，忽略jumei_product_name参数
        request.setJumei_product_id("222550619");
        // 聚美产品名
        request.setJumei_product_name("聚美商品名");
        // 修改数据(只传需要修改的字段)
        HtProductUpdate_ProductInfo update_data = new HtProductUpdate_ProductInfo();
        // 分类id(可选)  V3版四级分类
        update_data.setCategory_v3_4_id(3002);
        // 品牌id(可选)
        update_data.setBrand_id(120001);
        // 产品名(可选)
        // 100字以内，不能出现容量、规格、颜色等信息，这些信息在子型号中设置产品名上不能填写除了“（）”“/”“+”“*” 以外的特殊符号，
        // 如-，<>，· ，空格等符号必须是英文半角符号，套装产品名以“+”号连接
        update_data.setName("修改后产品名");
        // 外文名(可选) 注：请输入产品外文名称，支持中文繁体、英文、法文、德文、韩文、日文，100个字符以内
        update_data.setForeign_language_name("Foreign Language Name22");
        // 产品功效ID(可选)，多个ID用 ","隔开
        update_data.setFunction_ids("G01,G02");
        // 白底方图(全量修改)(可选)
        // 注：第一张必填,最多10张，1000*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开；
        // 图片上除了产品外不得出现任何其他信息（如水印、商标、优惠信息等)
        update_data.setNormalImage("白底方图22");
        // 竖图 (全量修改)(可选)
        // 注：可不传,最多10张，750*1000格式jpg,jpeg,单张不超过1m，多张图片以","隔开
        update_data.setVerticalImage("竖图22");

        request.setUpdate_data(update_data);

        HtProductUpdateResponse response = service.update(shopBean, request);

        if (response != null && response.getIs_Success()) {
            // 更新商品成功
            String reponseBody = response.getBody();

        } else {
            // 更新商品失败

        }

    }

//    @Test
//   public void copyDealTest() throws Exception {
//       ShopBean shopBean = new ShopBean();
//       shopBean.setAppKey("72");
//       shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
//       shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//       shopBean.setApp_url("http://182.138.102.82:8868/");
//       HtProductUpdateRequest request = new HtProductUpdateRequest();
//       request.setJumei_product_id("222550619");
//       request.setJumei_product_name("aa");
//       HtProductUpdate_ProductInfo productInfo=new HtProductUpdate_ProductInfo();
//       request.setUpdate_data(productInfo);
//
////       HtProductUpdateResponse response = service.copyDeal(shopBean, request);
//
//   }
}
