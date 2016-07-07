package com.voyageone.task2.cms.service.platform.common;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsMtPlatformDictDao;
import com.voyageone.service.dao.cms.CmsMtPlatformPropMappingCustomDao;
import com.voyageone.service.dao.cms.mongo.*;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformSchemaService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.sx.sku_field.SkuFieldBuilderService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtPlatformPropMappingCustomModel;
import com.voyageone.service.model.cms.enums.CustomMappingType;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryInvisibleFieldModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryInvisibleFieldModel_Field;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.Context;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.dao.PlatformSkuInfoDao;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadTmItemService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadTmMqService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadTmProductService;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import com.voyageone.task2.cms.service.putaway.SkuFieldBuilderFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static java.text.DateFormat.getInstance;

//import com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder;

/**
 * Created by dell on 2016/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class SxGetProductInfoTest {

    @Autowired
    private SkuFieldBuilderFactory skuFieldBuilderFactory;

    @Autowired
    private ProductService productService;

    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private SkuFieldBuilderService skuFieldBuilderService;
    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CmsBuildPlatformProductUploadTmItemService cmsBuildPlatformProductUploadTmItemService;

    @Autowired
    private CmsBuildPlatformProductUploadTmMqService cmsBuildPlatformProductUploadTmMqService;
    @Autowired
    private CmsBuildPlatformProductUploadTmProductService uploadTmProductService;

    @Autowired
    private PlatformSchemaService platformSchemaService;

    @Autowired
    private SxGetProductInfo sxGetProductInfo;

    @Autowired
    private ImsBtProductDao imsBtProductDao;

    @Autowired
    private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;

    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    protected PlatformSkuInfoDao platformSkuInfoDao;

    @Autowired
    private CmsMtPlatformDictDao cmsMtPlatformDictDao;

    @Autowired
    private CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Autowired
    private CmsMtPlatformCategoryInvisibleFieldDao cmsMtPlatformCategoryInvisibleFieldDao;

    @Autowired
    private CmsMtPlatformCategoryExtendFieldDao cmsMtPlatformCategoryExtendFieldDao;

    @Autowired
    private CmsMtPlatformPropMappingCustomDao cmsMtPlatformPropMappingCustomDao;

    @Test
    public void testFuc() throws Exception {

        System.out.println();
        System.out.println("start:" + DateTimeUtil.getNow());
        System.out.println(getInstance().format(new Date()));

//        SxData sxData = new SxData();
//        boolean isD = uploadTmProductService.getIsDarwin(sxData, getShop("018", 23), "1205", "21466766");
//        System.out.println(isD);

//        {
//            // 无线描述
//            String schema = "<itemRule>\n" +
//                    "\t<field id=\"wireless_desc\" name=\"新商品无线描述\" type=\"complex\">\n" +
//                    "\t\t<fields>\n" +
//                    "\t\t\t<field id=\"item_info\" name=\"商品信息\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"devTipRule\" value=\"该模块直接读取您已填写的商品信息，不需要编辑。如需查看实际效果，请通过发布后查看。\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"item_info_enable\" name=\"是否启用\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"启用\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"不启用\" value=\"false\"/>\n" +
//                    "\t\t\t\t\t\t</options>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"item_picture\" name=\"商品图片\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"devTipRule\" value=\"该模块最多可上传20张图片，图片类型支持png,jpg,jpeg。单个图片建议宽度为750px~1242px，高度≤1546px。\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"item_picture_enable\" name=\"是否启用\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"启用\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"不启用\" value=\"false\"/>\n" +
//                    "\t\t\t\t\t\t</options>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_0\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_1\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_2\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_3\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_4\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_5\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_6\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_7\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_8\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_9\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_10\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_11\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_12\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_13\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_14\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_15\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_16\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_17\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_18\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"image_hot_area_19\" type=\"complex\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"item_picture_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"item_picture_image\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t<field id=\"hot_area\" name=\"图片热区\" type=\"multiComplex\">\n" +
//                    "\t\t\t\t\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"start_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区左上角的坐标，数据范围是0-1，最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_x\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1，最多2位小数点，相对X轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"end_y\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"商品外链热区右下角的坐标，数据范围是0-1,最多2位小数点，相对Y轴的偏移百分比\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minInputNumRule\" value=\"0\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxInputNumRule\" value=\"1\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t\t<field id=\"item_id\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"输入热区关联的商品ID\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"long\"/>\n" +
//                    "\t\t\t\t\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t\t</fields>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"coupon\" name=\"优惠\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"devTipRule\" value=\"您可以选择下方已经设置好的店铺优惠券模板，也可以去商家中心设置店铺优惠券模板\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"coupon_enable\" name=\"是否启用\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"启用\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"不启用\" value=\"false\"/>\n" +
//                    "\t\t\t\t\t\t</options>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"coupon_id\" name=\"选择模板\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"coupon_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"hot_recommanded\" name=\"同店推荐\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"devTipRule\" value=\"您可以选择下方已经设置好的同店推荐模板，也可以去商家中心设置同店推荐模板\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"hot_recommanded_enable\" name=\"是否启用\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"启用\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"不启用\" value=\"false\"/>\n" +
//                    "\t\t\t\t\t\t</options>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"hot_recommanded_id\" name=\"选择模板\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"hot_recommanded_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"商品推荐\" value=\"520277\"/>\n" +
//                    "\t\t\t\t\t\t</options>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"shop_discount\" name=\"店铺活动\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"devTipRule\" value=\"您可以选择下方已经设置好的店铺活动模板，也可以去商家中心设置店铺活动模板\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"shop_discount_enable\" name=\"是否启用\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"启用\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"不启用\" value=\"false\"/>\n" +
//                    "\t\t\t\t\t\t</options>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"shop_discount_id\" name=\"选择模板\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"shop_discount_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"user_define\" name=\"自定义\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"devTipRule\" value=\"该模块需要您自己命名，可上传两张图片，图片格式支持png,jpg,jpeg。单个图片建议宽度为750px~1242px，高度≤1546px。\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"user_define_enable\" name=\"是否启用\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"启用\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t<option displayName=\"不启用\" value=\"false\"/>\n" +
//                    "\t\t\t\t\t\t</options>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"user_define_name\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"maxValueRule\" value=\"12\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"devTipRule\" value=\"仅支持中文英、数字，长度不可超过12个字符。\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"user_define_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"user_define_image_0\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"user_define_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"user_define_image_1\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t\t\t<depend-express fieldId=\"user_define_enable\" symbol=\"!=\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t</fields>\n" +
//                    "\t</field>\n" +
//                    "</itemRule>";
//
//            List<CmsMtPlatformPropMappingCustomModel> cmsMtPlatformPropMappingCustomModels = cmsMtPlatformPropMappingCustomDao.selectList(new HashMap<String, Object>(){{put("cartId", 23);}});
//
//            List<Field> listField = SchemaReader.readXmlForList(schema);
//            SxData sxData = sxProductService.getSxProductDataByGroupId("018", 662419L);
//            ExpressionParser exp = new ExpressionParser(sxProductService, sxData);
//
//            sxProductService.setWirelessDescriptionFieldValue(listField.get(0), exp, getShop("018", 23), "morse");
//
////            String descriptionValue = sxProductService.resolveDict("无线描述", exp, getShop("018", 23), "morse", null);
////            Map map = JacksonUtil.jsonToMap("{\"k1\":{\"k1-1\":\"v1\",\"k1-2\":\"v2\"},\"k2\":2}");
////            Map map = JacksonUtil.jsonToMap(descriptionValue);
////            Object objVal = ((Map) map.get("coupon")).get("coupon_enable");
////            if (objVal instanceof Boolean) {
////                System.out.println(String.valueOf(objVal));
////            }
////            objVal = ((Map) map.get("coupon")).get("coupon_id");
////            if (objVal instanceof Number) {
////                System.out.println(String.valueOf(objVal));
////            }
////            objVal = ((Map) map.get("hot_recommanded")).get("hot_recommanded_id");
////            if (objVal instanceof String) {
////                System.out.println(String.valueOf(objVal));
////            }
//            System.out.println();
//        }

//        {
//            // 商品描述
//            String schema = "<itemRule>\n" +
//                    "\t<field id=\"description\" name=\"商品描述\" type=\"complex\">\n" +
//                    "\t\t<rules>\n" +
//                    "\t\t\t<rule exProperty=\"include\" name=\"minLengthRule\" unit=\"character\" value=\"5\"/>\n" +
//                    "\t\t\t<rule exProperty=\"include\" name=\"maxLengthRule\" unit=\"character\" value=\"25000\"/>\n" +
//                    "\t\t\t<rule name=\"tipRule\" value=\"PC版描述最多支持发布20个有内容的描述模块\"/>\n" +
//                    "\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t</rules>\n" +
//                    "\t\t<fields>\n" +
//                    "\t\t\t<field id=\"desc_module_28_cat_mod\" name=\"视频推介\" type=\"complex\">\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_28_cat_mod_content\" name=\"视频推介内容\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"html\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minLengthRule\" unit=\"character\" value=\"5\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_28_cat_mod_order\" name=\"视频推介排序值\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"integer\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"desc_module_30_cat_mod\" name=\"促销专区\" type=\"complex\">\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_30_cat_mod_content\" name=\"促销专区内容\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"html\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minLengthRule\" unit=\"character\" value=\"5\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_30_cat_mod_order\" name=\"促销专区排序值\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"integer\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"desc_module_5_cat_mod\" name=\"商品参数\" type=\"complex\">\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_5_cat_mod_content\" name=\"商品参数内容\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"html\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minLengthRule\" unit=\"character\" value=\"5\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_5_cat_mod_order\" name=\"商品参数排序值\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"integer\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"desc_module_127_cat_mod\" name=\"商品展示\" type=\"complex\">\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_127_cat_mod_content\" name=\"商品展示内容\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"html\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minLengthRule\" unit=\"character\" value=\"5\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_127_cat_mod_order\" name=\"商品展示排序值\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"integer\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"desc_module_18_cat_mod\" name=\"功能展示\" type=\"complex\">\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_18_cat_mod_content\" name=\"功能展示内容\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"html\"/>\n" +
//                    "\t\t\t\t\t\t\t<rule exProperty=\"include\" name=\"minLengthRule\" unit=\"character\" value=\"5\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t\t<field id=\"desc_module_18_cat_mod_order\" name=\"功能展示排序值\" type=\"input\">\n" +
//                    "\t\t\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"integer\"/>\n" +
//                    "\t\t\t\t\t\t</rules>\n" +
//                    "\t\t\t\t\t</field>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t</fields>\n" +
//                    "\t</field>\n" +
//                    "</itemRule>";
//
//            List<Field> listField = SchemaReader.readXmlForList(schema);
//            SxData sxData = sxProductService.getSxProductDataByGroupId("018", 662419L);
//            ExpressionParser exp = new ExpressionParser(sxProductService, sxData);
//
//            sxProductService.setDescriptionFieldValue(listField.get(0), exp, getShop("018", 23), "morse");
//        }

        {
//            CmsMtPlatformCategoryInvisibleFieldModel model = new CmsMtPlatformCategoryInvisibleFieldModel();
////            model.set_id("576cdbc3983b011e18f0066e");
//            model.setCartId(23);
//            model.setCatId("123");
//            List<CmsMtPlatformCategoryInvisibleFieldModel_Field> propsProduct = new ArrayList<>();
//            CmsMtPlatformCategoryInvisibleFieldModel_Field invisibleField = new CmsMtPlatformCategoryInvisibleFieldModel_Field();
//            invisibleField.setFieldId("product_s_s>sell_p_s>sell_p_1");
//            propsProduct.add(invisibleField);
//            model.setPropsProduct(propsProduct);
//            cmsMtPlatformCategoryInvisibleFieldDao.insert(model);
////            cmsMtPlatformCategoryInvisibleFieldDao.update(model);
//            {
//                CmsMtPlatformCategorySchemaModel platformCatSchemaModel = platformCategoryService.getPlatformCatSchema("23232323", 23);
//                Map<String, Field> mapProductField = SchemaReader.readXmlForMap(platformCatSchemaModel.getPropsProduct());
//                Map<String, Field> mapItemField = SchemaReader.readXmlForMap(platformCatSchemaModel.getPropsItem());
//
//                CmsMtPlatformCategoryExtendFieldModel model = new CmsMtPlatformCategoryExtendFieldModel();
//                model.setCartId(23);
//                model.setCatId("23232323");
//                List<CmsMtPlatformCategoryExtendFieldModel_Field> propsProduct = new ArrayList<>();
//                {
//                    CmsMtPlatformCategoryExtendFieldModel_Field extendField = new CmsMtPlatformCategoryExtendFieldModel_Field();
//                    extendField.setParentFieldId("product_s_s>sell_p_s");
//                    Field ff = sxProductService.getFieldById(mapProductField, "product_s_s>sell_p_s>sell_p_1", ">", false);
//                    ff.setName("new sale point2");
//                    extendField.setField(ff);
//                    propsProduct.add(extendField);
//                }
//                {
//                    CmsMtPlatformCategoryExtendFieldModel_Field extendField = new CmsMtPlatformCategoryExtendFieldModel_Field();
//                    extendField.setParentFieldId("product_s_s>sell_p_s");
//                    Field ff = sxProductService.getFieldById(mapProductField, "product_s_s>sell_p_s>sell_p_2", ">", false);
//                    ff.setName("new sale point3");
//                    extendField.setField(ff);
//                    propsProduct.add(extendField);
//                }
//                {
//                    CmsMtPlatformCategoryExtendFieldModel_Field extendField = new CmsMtPlatformCategoryExtendFieldModel_Field();
//                    extendField.setParentFieldId("product_s_s");
//                    Field ff = sxProductService.getFieldById(mapProductField, "product_s_s>product_s", ">", false);
//                    ff.setName("new 商品 name");
//                    extendField.setField(ff);
//                    propsProduct.add(extendField);
//                }
//                model.setPropsProduct(propsProduct);
//
//                List<CmsMtPlatformCategoryExtendFieldModel_Field> propsItem = new ArrayList<>();
//                {
//                    CmsMtPlatformCategoryExtendFieldModel_Field extendField = new CmsMtPlatformCategoryExtendFieldModel_Field();
//                    extendField.setParentFieldId("");
//                    Field ff = sxProductService.getFieldById(mapItemField, "detail", ">", false);
//                    ff.setName("new details");
//                    extendField.setField(ff);
//                    propsItem.add(extendField);
//                }
//
//                model.setPropsItem(propsItem);
//                cmsMtPlatformCategoryExtendFieldDao.insert(model);
//            }

//            Map<String, Field> map1 = platformSchemaService.getMapFieldForMappingImage("23232323", 23);
//            Map<String, List<Field>> map2 = platformSchemaService.getFieldForProductImage("23232323", 23);
//
//            System.out.println();
        }

//        {
//            List<String> list = new ArrayList<>();
//            list.add("sk2");
//            list.add("sk3");
//            list.add("sk1");
//
//            System.out.println("start sort");
////            System.out.println(list.indexOf("sk2"));
////            System.out.println(list.indexOf("sk3"));
////            System.out.println(list.indexOf("sk4"));
////            System.out.println(list.indexOf("sk1"));
//
//            List<BaseMongoMap<String, Object>> listSku = new ArrayList<>();
//            for (int i = 1; i <= 4; i++) {
//                BaseMongoMap<String, Object> map = new BaseMongoMap<>();
//                map.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), "sk" + i);
//                if (i == 1) {
//                    map.put(CmsBtProductConstants.Platform_SKU_COM.size.name(), 3);
//                } else {
//                    if (i != 4)
//                    map.put(CmsBtProductConstants.Platform_SKU_COM.size.name(), i - 1);
//                }
//                listSku.add(map);
//            }
//
//            sxProductService.sortListBySkuCode(listSku, list);
////            sxProductService.sortSkuInfo(listSku);
//
//            System.out.println();
//        }

//        {
//            String schema = "<itemRule>\n" +
//                    "<field id=\"infos\" name=\"品牌信息\" type=\"input\">\n" +
//                    "\t<rules>\n" +
//                    "\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t</rules>\n" +
//                    "</field>\n" +
//                    "<field id=\"title\" name=\"牌\" type=\"singleCheck\">\n" +
//                    "\t<rules>\n" +
//                    "\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t</rules>\n" +
//                    "</field>\n" +
//                    "<field id=\"product_s_s\" name=\"商品\" type=\"multiComplex\">\n" +
//                    "<fields>\n" +
//                    "\t<field id=\"code_f\" name=\"Code\" type=\"input\">\n" +
//                    "\t</field>\n" +
//                    "\t<field id=\"orgin_f\" name=\"Code\" type=\"input\">\n" +
//                    "\t</field>\n" +
//                    "\t<field id=\"product_s\" name=\"商品\" type=\"multiComplex\">\t\t\n" +
//                    "\t\t<fields>\n" +
//                    "\t\t\t<field id=\"code_sf\" name=\"Code\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t</rules>\t\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"orgin_sf\" name=\"产地\" type=\"input\">\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t</fields>\n" +
//                    "\t</field>\n" +
//                    "\t<field id=\"sell_p_s\" name=\"卖点\" type=\"complex\">\n" +
//                    "\t\t<fields>\n" +
//                    "\t\t\t<field id=\"sell_p_0\" name=\"卖点1\" type=\"input\">\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"sell_p_1\" name=\"卖点2\" type=\"input\">\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"sell_p_2\" name=\"卖点3\" type=\"input\">\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t</fields>\n" +
//                    "\t</field>\n" +
//                    "\t<field id=\"col\" name=\"颜色\" type=\"multiCheck\">\n" +
//                    "\t\t<rules>\n" +
//                    "\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t</rules>\n" +
//                    "\t\t<options>\n" +
//                    "\t\t<option displayName=\"蓝\" value=\"b\"/>\n" +
//                    "\t\t<option displayName=\"红\" value=\"r\"/>\n" +
//                    "\t\t</options>\n" +
//                    "\t</field>\n" +
//                    "</fields>\n" +
//                    "</field>\n" +
//                    "</itemRule>";
//            List<Field> listField = SchemaReader.readXmlForList(schema);
//            Map<String, Field> mapField = SchemaReader.readXmlForMap(schema);
//
//            String search_field_id = "product_s_s>product_s>code_sf";
//            String add_field_id = "product_s_s>product_s";
////            String search_field_id = "product_s_s>product_s";
////            String add_field_id = "product_s_s";
//            InputField addField = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
//            addField.setId("code_sf");
//            addField.setValue("new");
//
////            Field retField = sxProductService.getFieldById(mapField, search_field_id, ">", true);
//            sxProductService.addExtendField(mapField,add_field_id,">",addField);
//
//            System.out.println();
//        }

//        {
//            CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema("121418010", 23);
//            String itemSchema = cmsMtPlatformCategorySchemaModel.getPropsItem();
//
//            List<Field> fields;
//            Map<String, Field> map;
//            try {
//                fields = SchemaReader.readXmlForList(itemSchema);
//                map = SchemaReader.readXmlForMap(itemSchema);
//            } catch (TopSchemaException e) {
//                System.out.println("error");
//                return;
//            }
//            try {
//                ((ComplexField) fields.get(1)).getFieldMap();
//            } catch (ClassCastException e) {
//                System.out.println("Cast error");
//            }
//            System.out.println("end");
//        }

//        Map<String, String> mapSize = sxProductService.getSizeMap("010", "", "Bracelets", "women");
//        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("335"));

        {
            // TM 上新
//            Context context = Context.getContext();
//            ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
//            context.putAttribute("springContext", ctx);
//
//            String channel_id = "002";
//            int cart_id = 23;
//            ShopBean shopBean = getShop(channel_id, cart_id);
//
//            channel_id = "010";
//            CmsBtSxWorkloadModel cmsBtSxWorkloadModel = new CmsBtSxWorkloadModel();
//            cmsBtSxWorkloadModel.setChannelId(channel_id);
//            cmsBtSxWorkloadModel.setCartId(cart_id);
//            cmsBtSxWorkloadModel.setGroupId(20913L);
//            cmsBtSxWorkloadModel.setPublishStatus(0);
//
//            System.out.println("TM 上新 start");
//            cmsBuildPlatformProductUploadTmMqService.uploadProduct(cmsBtSxWorkloadModel, shopBean);
//            System.out.println("TM 上新 end");
        }

//        String url = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?req=imageprops", "tomzhu-image-2016053101903_02");
//        String result = HttpUtils.get(url, null);
//        result = result.substring(result.indexOf("image"));
//        String[] args = result.split("image\\.");
//        Map<String, String> responseMap = new HashMap<>();
//        for (String param : args) {
//            if (param.indexOf("=") > 0) {
//                String[] keyVal = param.split("=");
//                if (keyVal.length > 1) {
//                    responseMap.put(keyVal[0], keyVal[1]);
//                } else {
//                    responseMap.put(keyVal[0], "");
//                }
//            }
//        }
//        url = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&rgn=0,0,%s,%s", "tomzhu-image-2016053101903_02", responseMap.get("width"), responseMap.get("height"));
//        if (result != null) {
//            System.out.println(url);
//            return;
//        }

//        try {
//            System.out.println(" -> 启动中......");
//            Context context = Context.getContext();
//            ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
//            context.putAttribute("springContext", ctx);
//            System.out.println(" -> 启动完成......");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            System.out.println(ex.getMessage());
//        }
//
//        DictValueFactory dictValueFactory = new DictValueFactory();
//        Set<DictWord> dictWords = dictValueFactory.getDictWords("010");
//        int index = 0;
//        for (DictWord iterDictWord : dictWords) {
//            if (!iterDictWord.getName().equals(null)) {
//                System.out.println(String.valueOf(index) + ":" + iterDictWord.getName());
//            }
//            index++;
//        }

//        {
//            String completeImageUrl;
//            try {
//                String url = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?req=imageprops", "018-10756195-41");
//                System.out.println("[CustomWordModuleGetAllImages]取得图片大小url:" + url);
//                String result = HttpUtils.get(url, null);
//                result = result.substring(result.indexOf("image"));
//                String[] args = result.split("image\\.");
//                Map<String, String> responseMap = new HashMap<>();
//                for (String param : args) {
//                    if (param.indexOf("=") > 0) {
//                        String[] keyVal = param.split("=");
//                        if (keyVal.length > 1) {
//                            responseMap.put(keyVal[0], keyVal[1]);
//                        } else {
//                            responseMap.put(keyVal[0], "");
//                        }
//                    }
//                }
//                completeImageUrl = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&rgn=0,0,%s,%s", "018-10756195-41", responseMap.get("width"), responseMap.get("height"));
//                System.out.println("[CustomWordModuleGetAllImages]取得原始图片url:" + completeImageUrl);
//            } catch (Exception e) {
//                throw new BusinessException("[CustomWordModuleGetAllImages]取得原始图片url失败!");
//            }
//        }
//
//        SxData sxData = sxProductService.getSxProductDataByGroupId("018", 445666L);
////        SxData sxData = sxProductService.getSxProductDataByGroupId("018", 662419L);
//        ExpressionParser exp = new ExpressionParser(sxProductService, sxData);
//
//        String channelId = sxData.getChannelId();
//        int cartId = sxData.getCartId();
//        int imageType = 3;  // 品牌故事图
//        int viewType = 1; // pc
//        String brandName = sxData.getMainProduct().getFields().getBrand();
//        String productType = sxData.getMainProduct().getFields().getProductType();
//        String sizeType = sxData.getMainProduct().getFields().getSizeType();

//        List<String> listUrls = sxProductService.getImageUrls(channelId, cartId, imageType, viewType, brandName, productType, sizeType, false);

//        ShopBean shopBean1 = Shops.getShop("010", 23);
//        System.out.println("shop");

//        // 商品上新
//        {
//            SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
//            ExpressionParser exp = new ExpressionParser(sxProductService, sxData);
//            CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid001");
//            CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(cmsMtPlatformMappingModel.getPlatformCategoryId(), sxData.getCartId());
//            ShopBean shopBean = new ShopBean();
//            shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());
//            String numIId = cmsBuildPlatformProductUploadTmItemService.uploadItem(exp, "ppid001", cmsMtPlatformCategorySchemaModel, cmsMtPlatformMappingModel, shopBean, "morse");
//            System.out.println(numIId);
//        }

        // sku属性设置 start
//        String skuPro;
//        {
//            skuPro = "<itemRule>\n" +
//                    "\t<field id=\"sku\" name=\"SKU\" type=\"multiComplex\">\n" +
//                    "\t\t<rules>\n" +
//                    "\t\t\t<rule name=\"950280534_1\" value=\"SKU与类目销售属性必须匹配\"/>\n" +
//                    "\t\t\t<rule name=\"529052199_1\" value=\"如果销售属性存在套餐且存在官方标配，官方标配必选\"/>\n" +
//                    "\t\t</rules>\n" +
//                    "\t\t<fields>\n" +
//                    "\t\t\t<field id=\"prop_1627207\" name=\"颜色\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t<option displayName=\"军绿色\" value=\"3232483\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"天蓝色\" value=\"3232484\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"巧克力色\" value=\"3232481\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"桔色\" value=\"90554\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"浅灰色\" value=\"28332\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"浅绿色\" value=\"30156\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"浅黄色\" value=\"60092\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深卡其布色\" value=\"3232482\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深灰色\" value=\"3232478\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深紫色\" value=\"3232479\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深蓝色\" value=\"28340\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"白色\" value=\"28320\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"粉红色\" value=\"3232480\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"紫罗兰\" value=\"80882\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"紫色\" value=\"28329\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"红色\" value=\"28326\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"绿色\" value=\"28335\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"花色\" value=\"130164\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"蓝色\" value=\"28338\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"褐色\" value=\"132069\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"透明\" value=\"107121\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"酒红色\" value=\"28327\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"黄色\" value=\"28324\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"黑色\" value=\"28341\"/>\n" +
//                    "\t\t\t\t</options>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"std_size_prop_20518_-1\" name=\"“自定义”尺码\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"tipRule\" value=\"自定义尺码值只能输入以下格式：【数字/字母/数字；字母/字母，字母+数字 字母/数字；数字/字母；数字+字母；数字/数字；数字/数字/字母；字母；数字/数字+字母/字母；数字/数字+字母；数字】，并支持在上述格式前添加性别如“男/女/男童/女童”。若无支持的格式，可最多新增一个不在上述格式范围内的尺码值\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t<depend-express fieldId=\"std_size_group\" value=\"-1:自定义:-1\" symbol=\"!=\"/>\n" +
//                    "\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"sku_price\" name=\"价格\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"decimal\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"minValueRule\" value=\"0.00\" exProperty=\"not include\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"maxValueRule\" value=\"100000000.00\" exProperty=\"not include\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"86848434_1\" value=\"SKU价格受类目限制\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"sku_quantity\" name=\"库存\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"integer\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"minValueRule\" value=\"0\" exProperty=\"include\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"maxValueRule\" value=\"1000000\" exProperty=\"not include\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"sku_outerId\" name=\"商家编码\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"maxLengthRule\" value=\"64\" exProperty=\"include\" unit=\"byte\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"sku_barcode\" name=\"条形码\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"1958629116_1\" value=\"条形码必须符合EAN和UPC编码规则\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"hscode\" name=\"HS海关代码\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t</fields>\n" +
//                    "\t</field>\n" +
//                    "\t<field id=\"prop_extend_1627207\" name=\"颜色扩展\" type=\"multiComplex\">\n" +
//                    "\t\t<fields>\n" +
//                    "\t\t\t<field id=\"prop_1627207\" name=\"颜色\" type=\"singleCheck\">\n" +
//                    "\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t<option displayName=\"军绿色\" value=\"3232483\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"天蓝色\" value=\"3232484\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"巧克力色\" value=\"3232481\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"桔色\" value=\"90554\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"浅灰色\" value=\"28332\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"浅绿色\" value=\"30156\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"浅黄色\" value=\"60092\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深卡其布色\" value=\"3232482\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深灰色\" value=\"3232478\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深紫色\" value=\"3232479\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"深蓝色\" value=\"28340\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"白色\" value=\"28320\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"粉红色\" value=\"3232480\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"紫罗兰\" value=\"80882\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"紫色\" value=\"28329\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"红色\" value=\"28326\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"绿色\" value=\"28335\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"花色\" value=\"130164\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"蓝色\" value=\"28338\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"褐色\" value=\"132069\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"透明\" value=\"107121\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"酒红色\" value=\"28327\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"黄色\" value=\"28324\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"黑色\" value=\"28341\"/>\n" +
//                    "\t\t\t\t</options>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"alias_name\" name=\"别名\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"text\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"byte\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"prop_image\" name=\"属性图片\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"valueTypeRule\" value=\"url\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"basecolor\" name=\"色系\" type=\"multiCheck\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"minInputNumRule\" value=\"0\" exProperty=\"not include\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"maxInputNumRule\" value=\"3\" exProperty=\"include\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<options>\n" +
//                    "\t\t\t\t\t<option displayName=\"蓝色\" value=\"28338\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"黑色\" value=\"28341\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"透明\" value=\"107121\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"粉红色\" value=\"3232480\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"白色\" value=\"28320\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"花色\" value=\"130164\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"橙色\" value=\"90554\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"黄色\" value=\"28324\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"红色\" value=\"28326\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"紫色\" value=\"28329\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"灰色\" value=\"28332\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"棕色\" value=\"132069\"/>\n" +
//                    "\t\t\t\t\t<option displayName=\"绿色\" value=\"28335\"/>\n" +
//                    "\t\t\t\t</options>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t</fields>\n" +
//                    "\t</field>\n" +
//                    "\t<field id=\"std_size_extends_20518\" name=\"尺码扩展\" type=\"multiComplex\">\n" +
//                    "\t\t<rules>\n" +
//                    "\t\t\t<rule name=\"tipRule\" value=\"尺码表中的字段至少两个维度必填。\"/>\n" +
//                    "\t\t\t<rule name=\"tipRule\" value=\"尺码表中的字段若选择填写某个字段，则所有尺码对应的该字段信息均需填写。\"/>\n" +
//                    "\t\t\t<rule name=\"tipRule\" value=\"因尺码表结构调整，您填写的数据项可能被调整为自定义项，不影响信息展示。\"/>\n" +
//                    "\t\t</rules>\n" +
//                    "\t\t<fields>\n" +
//                    "\t\t\t<field id=\"std_size_prop_20518_-1\" name=\"“自定义”尺码\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"tipRule\" value=\"自定义尺码值只能输入以下格式：【数字/字母/数字；字母/字母，字母+数字 字母/数字；数字/字母；数字+字母；数字/数字；数字/数字/字母；字母；数字/数字+字母/字母；数字/数字+字母；数字】，并支持在上述格式前添加性别如“男/女/男童/女童”。若无支持的格式，可最多新增一个不在上述格式范围内的尺码值\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t<depend-express fieldId=\"std_size_group\" value=\"-1:自定义:-1\" symbol=\"!=\"/>\n" +
//                    "\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"size_tip\" name=\"尺码备注\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"minLengthRule\" value=\"1\" exProperty=\"include\" unit=\"byte\"/>\n" +
//                    "\t\t\t\t\t<rule name=\"maxLengthRule\" value=\"30\" exProperty=\"include\" unit=\"byte\"/>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"size_mapping_shengao\" name=\"身高（cm）\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t<depend-group operator=\"or\">\n" +
//                    "\t\t\t\t\t\t\t<depend-express fieldId=\"std_size_prop_20518_-1\" value=\"均码\" symbol=\"==\"/>\n" +
//                    "\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"size_mapping_shengao_range\" name=\"身高（cm）\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t<depend-express fieldId=\"std_size_prop_20518_-1\" value=\"均码\" symbol=\"!=\"/>\n" +
//                    "\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"size_mapping_shengao_from\" name=\"最小值\" type=\"input\"/>\n" +
//                    "\t\t\t\t\t<field id=\"size_mapping_shengao_to\" name=\"最大值\" type=\"input\"/>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"size_mapping_yaowei\" name=\"腰围（cm）\" type=\"input\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t<depend-group operator=\"or\">\n" +
//                    "\t\t\t\t\t\t\t<depend-express fieldId=\"std_size_prop_20518_-1\" value=\"均码\" symbol=\"==\"/>\n" +
//                    "\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t\t<field id=\"size_mapping_yaowei_range\" name=\"腰围（cm）\" type=\"complex\">\n" +
//                    "\t\t\t\t<rules>\n" +
//                    "\t\t\t\t\t<rule name=\"disableRule\" value=\"true\">\n" +
//                    "\t\t\t\t\t\t<depend-group operator=\"and\">\n" +
//                    "\t\t\t\t\t\t\t<depend-express fieldId=\"std_size_prop_20518_-1\" value=\"均码\" symbol=\"!=\"/>\n" +
//                    "\t\t\t\t\t\t</depend-group>\n" +
//                    "\t\t\t\t\t</rule>\n" +
//                    "\t\t\t\t</rules>\n" +
//                    "\t\t\t\t<fields>\n" +
//                    "\t\t\t\t\t<field id=\"size_mapping_yaowei_from\" name=\"最小值\" type=\"input\"/>\n" +
//                    "\t\t\t\t\t<field id=\"size_mapping_yaowei_to\" name=\"最大值\" type=\"input\"/>\n" +
//                    "\t\t\t\t</fields>\n" +
//                    "\t\t\t</field>\n" +
//                    "\t\t</fields>\n" +
//                    "\t</field>\n" +
//                    "</itemRule>";
//        }
//
//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid003"); // 模板1
//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid002"); // 模板2
//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid004"); // 模板3
//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid005"); // 模板4
//
//        Map<String, Integer> skuInventoryMap = new HashMap<>();
//        skuInventoryMap.put("c001-1", 1100);
//        skuInventoryMap.put("c001-2", 1200);
//        skuInventoryMap.put("c001-4", 1400);
//        skuInventoryMap.put("c004-1", 4100);
//
//        // sku属性设置 new Src start
//        List<Field> skuInfoFields;
//        List<Field> fields = SchemaReader.readXmlForList(skuPro);
//        List<Field> allSkuFields = new ArrayList<>();
//        {
//            recursiveGetFields(fields, allSkuFields);
//            AbstractSkuFieldBuilder skuFieldService = skuFieldBuilderService.getSkuFieldBuilder(23, allSkuFields);
//
//            CmsMtPlatformDictModel cmsMtPlatformDictModel = cmsMtPlatformDictDao.selectOne(new HashMap<String, Object>(){{
//                put("orderChannelId", "066");
//                put("cartId", 23);
//                put("name", "属性图片模板");
//            }});
//
//            if (skuFieldService == null) {
//                System.out.println("null");
//            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl1) {
//                skuFieldService.setCodeImageTemplate(cmsMtPlatformDictModel.getValue());
//                System.out.println("TmallGjSkuFieldBuilderImpl1");
//            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl2) {
//                System.out.println("TmallGjSkuFieldBuilderImpl2");
//            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl3) {
//                skuFieldService.setCodeImageTemplate(cmsMtPlatformDictModel.getValue());
//                System.out.println("TmallGjSkuFieldBuilderImpl3");
//            } else if (skuFieldService instanceof TmallGjSkuFieldBuilderImpl4) {
//                skuFieldService.setCodeImageTemplate(cmsMtPlatformDictModel.getValue());
//                System.out.println("TmallGjSkuFieldBuilderImpl4");
//            } else {
//                System.out.println("not exists");
//            }
//
//            SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
//            ExpressionParser exp = new ExpressionParser(sxProductService, sxData);
//
//            ShopBean shopBean = new ShopBean();
////        shopBean.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());
//            shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());
//
//            skuInfoFields = skuFieldService.buildSkuInfoField(allSkuFields, exp, cmsMtPlatformMappingModel, skuInventoryMap, shopBean, "morse");
//        }
        // sku属性设置 new Src end

        // sku属性设置 old Src start
//        List<com.taobao.top.schema.field.Field> skuInfoFields;
//        {
//            List<com.taobao.top.schema.field.Field> fields = com.taobao.top.schema.factory.SchemaReader.readXmlForList(skuPro);
//            List<com.taobao.top.schema.field.Field> allSkuFields = new ArrayList<>();
//            recursiveGetTaobaoFields(fields, allSkuFields);
//            com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder skuFieldBuilder = null;
//
//            CmsMtPlatformDictModel cmsMtPlatformDictModel = cmsMtPlatformDictDao.selectOne(new HashMap<String, Object>(){{
//                put("orderChannelId", "066");
//                put("cartId", 23);
//                put("name", "属性图片模板");
//            }});
//
//            try {
//                skuFieldBuilder = skuFieldBuilderFactory.getSkuFieldBuilder(23, allSkuFields);
//            } catch (TaskSignal taskSignal) {
//                taskSignal.printStackTrace();
//            }
//            if (skuFieldBuilder == null) {
//                System.out.println("null");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_0) {
//                skuFieldBuilder.setCodeImageTemplete(cmsMtPlatformDictModel.getValue());
//                System.out.println("TmallGjSkuFieldBuilderImpl_0");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_1) {
//                System.out.println("TmallGjSkuFieldBuilderImpl_1");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_2) {
//                skuFieldBuilder.setCodeImageTemplete(cmsMtPlatformDictModel.getValue());
//                System.out.println("TmallGjSkuFieldBuilderImpl_2");
//            } else if (skuFieldBuilder instanceof TmallGjSkuFieldBuilderImpl_3) {
//                skuFieldBuilder.setCodeImageTemplete(cmsMtPlatformDictModel.getValue());
//                System.out.println("TmallGjSkuFieldBuilderImpl_3");
//            } else {
//                System.out.println("not exists");
//            }
//
//            List<CmsBtProductModel> cmsBtProductModels = productService.getProductByGroupId("066", 333L, false);
//            List<SxProductBean> sxProductBeans = new ArrayList<>();
//            CmsBtProductModel mainProductModel = null;
//            CmsBtProductGroupModel mainProductPlatform = null;
//            SxProductBean mainSxProduct = null;
//
//            for (CmsBtProductModel cmsBtProductModel : cmsBtProductModels) {
//                CmsBtProductGroupModel productPlatform = cmsBtProductModel.getGroups();
//                String prodCode = cmsBtProductModel.getFields().getCode();
//                // tom 获取feed info的数据 START
//                CmsBtFeedInfoModel feedInfo = cmsBtFeedInfoDao.selectProductByCode("066", prodCode);
//                // tom 获取feed info的数据 END
//                SxProductBean sxProductBean = new SxProductBean(cmsBtProductModel, productPlatform, feedInfo);
//                if (filtProductsByPlatform(sxProductBean)) {
//                    sxProductBeans.add(sxProductBean);
//                    if (prodCode.equals(productPlatform.getMainProductCode())) {
//                        mainProductModel = cmsBtProductModel;
//                        mainProductPlatform = productPlatform;
//                        mainSxProduct = sxProductBean;
//                    }
//                }
//            }
//
//            Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap = new HashMap<>();
//            for (SxProductBean sxProductBean : sxProductBeans) {
//                for (CmsBtProductModel_Sku sku : sxProductBean.getCmsBtProductModel().getSkus()) {
//                    skuProductMap.put(sku, sxProductBean);
//                }
//            }
//
//            TmallUploadRunState.TmallContextBuildCustomFields tmallContextBuildCustomFields = new TmallUploadRunState(null).new TmallContextBuildCustomFields();
//
//            skuFieldBuilder.setExpressionParser(new com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser("066", 23, mainSxProduct, sxProductBeans));
//
//            try {
//                skuInfoFields = skuFieldBuilder.buildSkuInfoField(23, null, allSkuFields, sxProductBeans, skuProductMap, cmsMtPlatformMappingModel, skuInventoryMap, tmallContextBuildCustomFields, new HashSet<>());
//            } catch (TaskSignal taskSignal) {
//                return;
//            }
//        }
        // sku属性设置 old Src end
        // sku属性设置 end

//        List<PlatformSkuInfoModel> tmallSkuInfos = platformSkuInfoDao.selectPlatformSkuInfoByCartId(23);
//        tmallSkuInfos.forEach(model->{
//            System.out.print(model.getSeq());
//            System.out.print("\t");
//            System.out.print(model.getCart_id());
//            System.out.print("\t");
//            System.out.print(model.getProp_id());
//            System.out.print("\t");
//            System.out.print(SkuTemplateSchema.decodeFieldTypes(model.getSku_type()));
//            System.out.print("\t");
//            System.out.print(Long.toBinaryString(model.getSku_type()));
//            System.out.println();
//        });


        // constructMappingPlatformProps start
//        String schema = "<itemRule><field id=\"infos\" name=\"信息\" type=\"label\"><label-group name=\"\"><label-group name=\"sys_infos\"><label name=\"欢迎\" value=\"欢迎使用Schema新商品发布体系\" desc=\"\"/></label-group><label-group name=\"dev_infos\"><label name=\"开发者说明\" value=\"1.此为消息机制，并且本条消息为开发者消息；2.DevInfo类型消息在第三方开发者有UI对客户展示时应主动屏蔽此类消息，比如这条消息不该被非开发者看到；3.新接口接入初期，有任何疑问或者发现Bug请到旺旺群（群号：836280177）交流\" desc=\"\"/></label-group></label-group></field><field id=\"title\" name=\"商品标题\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"minLengthRule\" value=\"1\" exProperty=\"include\" unit=\"byte\"/><rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/></rules></field><field id=\"sell_points\" name=\"商品卖点\" type=\"complex\"><rules><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"character\"/><rule name=\"maxLengthRule\" value=\"20\" exProperty=\"include\" unit=\"character\"/><rule name=\"tipRule\" value=\"最多5个卖点短语,每个卖点短语最多6个字,总字数不超过20个字\"/></rules><fields><field id=\"sell_point_0\" name=\"商品卖点\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"character\"/><rule name=\"maxLengthRule\" value=\"6\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"sell_point_1\" name=\"商品卖点\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"character\"/><rule name=\"maxLengthRule\" value=\"6\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"sell_point_2\" name=\"商品卖点\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"character\"/><rule name=\"maxLengthRule\" value=\"6\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"sell_point_3\" name=\"商品卖点\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"character\"/><rule name=\"maxLengthRule\" value=\"6\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"sell_point_4\" name=\"商品卖点\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"character\"/><rule name=\"maxLengthRule\" value=\"6\" exProperty=\"include\" unit=\"character\"/></rules></field></fields></field><field id=\"item_type\" name=\"发布类型\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"一口价\" value=\"b\"/></options><default-value>b</default-value></field><field id=\"stuff_status\" name=\"宝贝类型\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"全新\" value=\"5\"/></options><default-value>5</default-value></field><field id=\"sub_stock\" name=\"拍下减库存\" type=\"singleCheck\"><rules><rule name=\"tipRule\" value=\"秒杀商品或超低价抢购促销商品或热卖商品请选择拍下减库存，可以防止超卖情况！\" url=\"https://service.taobao.com/support/knowledge-1110945.htm\"/></rules><options><option displayName=\"是\" value=\"true\"/><option displayName=\"否\" value=\"false\"/></options><default-value>false</default-value></field><field id=\"second_kill\" name=\"商品秒杀\" type=\"multiCheck\"><rules><rule name=\"tipRule\" value=\"若此商品参加秒杀活动，在此期间内必须设为秒杀商品，以防止作弊\"/><rule name=\"tipRule\" value=\"秒杀商品的商品详情页面将不出现“加入购物车”按钮\"/></rules><options><option displayName=\"电脑用户\" value=\"web\"/><option displayName=\"手机用户\" value=\"wap\"/></options></field><field id=\"prop_21299\" name=\"产地\" type=\"singleCheck\"><options><option displayName=\"中国大陆\" value=\"27772\"/><option displayName=\"丹麦\" value=\"39486\"/><option displayName=\"加拿大\" value=\"27410\"/><option displayName=\"印尼\" value=\"3301027\"/><option displayName=\"台湾省\" value=\"52865\"/><option displayName=\"墨西哥\" value=\"52992\"/><option displayName=\"奥地利\" value=\"3909606\"/><option displayName=\"德国\" value=\"27413\"/><option displayName=\"意大利\" value=\"39865108\"/><option displayName=\"捷克\" value=\"3281850\"/><option displayName=\"新加坡\" value=\"43446\"/><option displayName=\"新西兰\" value=\"43444\"/><option displayName=\"日本\" value=\"27023\"/><option displayName=\"比利时\" value=\"3620977\"/><option displayName=\"法国\" value=\"27412\"/><option displayName=\"波兰\" value=\"3857691\"/><option displayName=\"泰国\" value=\"27024\"/><option displayName=\"澳大利亚\" value=\"27015\"/><option displayName=\"澳门特别行政区\" value=\"3296648\"/><option displayName=\"爱尔兰\" value=\"39487\"/><option displayName=\"瑞典\" value=\"27415\"/><option displayName=\"瑞士\" value=\"3255100\"/><option displayName=\"美国\" value=\"27409\"/><option displayName=\"英国\" value=\"27411\"/><option displayName=\"荷兰\" value=\"39274\"/><option displayName=\"西班牙\" value=\"27424\"/><option displayName=\"韩国\" value=\"27019\"/><option displayName=\"香港特别行政区\" value=\"3296647\"/><option displayName=\"马来西亚\" value=\"3286452\"/></options></field><field id=\"prop_20021\" name=\"材质\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"PPSU\" value=\"14149693\"/><option displayName=\"紫砂\" value=\"79921\"/><option displayName=\"密胺\" value=\"8744353\"/><option displayName=\"有机玻璃\" value=\"3806732\"/><option displayName=\"骨瓷\" value=\"79920\"/><option displayName=\"锦纶\" value=\"112997\"/><option displayName=\"纸\" value=\"4181807\"/><option displayName=\"AS\" value=\"3248790\"/><option displayName=\"铁\" value=\"3226633\"/><option displayName=\"木\" value=\"3260684\"/><option displayName=\"铝\" value=\"3612252\"/><option displayName=\"PE\" value=\"3737061\"/><option displayName=\"玻璃\" value=\"42750\"/><option displayName=\"其他\" value=\"20213\"/><option displayName=\"ABS\" value=\"3379565\"/><option displayName=\"陶瓷\" value=\"42749\"/><option displayName=\"不锈钢\" value=\"42752\"/><option displayName=\"pet\" value=\"3271543\"/><option displayName=\"tritan\" value=\"52318998\"/><option displayName=\"PC\" value=\"3239306\"/><option displayName=\"硅胶\" value=\"109098\"/><option displayName=\"聚丙烯(pp)\" value=\"50664865\"/><option displayName=\"PS\" value=\"3332292\"/></options></field><field id=\"prop_31172572\" name=\"奶嘴品类\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"奶嘴(单卖)\" value=\"201514191\"/><option displayName=\"安抚奶嘴\" value=\"42625\"/></options></field><field id=\"sku\" name=\"SKU\" type=\"multiComplex\"><rules><rule name=\"775943417_1\" value=\"SKU与类目销售属性必须匹配\"/><rule name=\"1237615532_1\" value=\"如果销售属性存在套餐且存在官方标配，官方标配必选\"/></rules><fields><field id=\"prop_1627207\" name=\"颜色分类\" type=\"singleCheck\"><options><option displayName=\"透明\" value=\"107121\"/><option displayName=\"花色\" value=\"130164\"/><option displayName=\"白色\" value=\"28320\"/><option displayName=\"黄色\" value=\"28324\"/><option displayName=\"红色\" value=\"28326\"/><option displayName=\"酒红色\" value=\"28327\"/><option displayName=\"紫色\" value=\"28329\"/><option displayName=\"浅灰色\" value=\"28332\"/><option displayName=\"绿色\" value=\"28335\"/><option displayName=\"蓝色\" value=\"28338\"/><option displayName=\"深蓝色\" value=\"28340\"/><option displayName=\"黑色\" value=\"28341\"/><option displayName=\"桔色\" value=\"90554\"/><option displayName=\"浅黄色\" value=\"60092\"/><option displayName=\"浅绿色\" value=\"30156\"/><option displayName=\"深灰色\" value=\"3232478\"/><option displayName=\"深紫色\" value=\"3232479\"/><option displayName=\"粉红色\" value=\"3232480\"/><option displayName=\"巧克力色\" value=\"3232481\"/><option displayName=\"深卡其布色\" value=\"3232482\"/><option displayName=\"军绿色\" value=\"3232483\"/><option displayName=\"天蓝色\" value=\"3232484\"/><option displayName=\"褐色\" value=\"132069\"/><option displayName=\"紫罗兰\" value=\"80882\"/></options></field><field id=\"prop_6477422\" name=\"规格\" type=\"singleCheck\"><options><option displayName=\"S\" value=\"28314\"/><option displayName=\"M\" value=\"28315\"/><option displayName=\"L\" value=\"28316\"/><option displayName=\"XL\" value=\"28317\"/><option displayName=\"其它\" value=\"10122\"/></options></field><field id=\"sku_price\" name=\"价格\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"decimal\"/><rule name=\"minValueRule\" value=\"0.00\" exProperty=\"not include\"/><rule name=\"maxValueRule\" value=\"100000000.00\" exProperty=\"not include\"/><rule name=\"460299884_1\" value=\"SKU价格受类目限制\"/></rules></field><field id=\"sku_quantity\" name=\"库存\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/><rule name=\"minValueRule\" value=\"0\" exProperty=\"include\"/><rule name=\"maxValueRule\" value=\"1000000\" exProperty=\"not include\"/></rules></field><field id=\"sku_outerId\" name=\"商家编码\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"maxLengthRule\" value=\"64\" exProperty=\"include\" unit=\"byte\"/></rules></field><field id=\"sku_barcode\" name=\"条形码\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"1478960951_1\" value=\"条形码必须符合EAN和UPC编码规则\"/></rules></field><field id=\"hscode\" name=\"HS海关代码\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field><field id=\"sku_scProductId\" name=\"货品Id\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/></rules></field></fields></field><field id=\"prop_extend_1627207\" name=\"颜色分类扩展\" type=\"multiComplex\"><fields><field id=\"prop_1627207\" name=\"颜色分类\" type=\"singleCheck\"><options><option displayName=\"透明\" value=\"107121\"/><option displayName=\"花色\" value=\"130164\"/><option displayName=\"白色\" value=\"28320\"/><option displayName=\"黄色\" value=\"28324\"/><option displayName=\"红色\" value=\"28326\"/><option displayName=\"酒红色\" value=\"28327\"/><option displayName=\"紫色\" value=\"28329\"/><option displayName=\"浅灰色\" value=\"28332\"/><option displayName=\"绿色\" value=\"28335\"/><option displayName=\"蓝色\" value=\"28338\"/><option displayName=\"深蓝色\" value=\"28340\"/><option displayName=\"黑色\" value=\"28341\"/><option displayName=\"桔色\" value=\"90554\"/><option displayName=\"浅黄色\" value=\"60092\"/><option displayName=\"浅绿色\" value=\"30156\"/><option displayName=\"深灰色\" value=\"3232478\"/><option displayName=\"深紫色\" value=\"3232479\"/><option displayName=\"粉红色\" value=\"3232480\"/><option displayName=\"巧克力色\" value=\"3232481\"/><option displayName=\"深卡其布色\" value=\"3232482\"/><option displayName=\"军绿色\" value=\"3232483\"/><option displayName=\"天蓝色\" value=\"3232484\"/><option displayName=\"褐色\" value=\"132069\"/><option displayName=\"紫罗兰\" value=\"80882\"/></options></field><field id=\"alias_name\" name=\"别名\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"byte\"/><rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/></rules></field><field id=\"prop_image\" name=\"属性图片\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/></rules></field></fields></field><field id=\"prop_extend_6477422\" name=\"规格扩展\" type=\"multiComplex\"><fields><field id=\"prop_6477422\" name=\"规格\" type=\"singleCheck\"><options><option displayName=\"S\" value=\"28314\"/><option displayName=\"M\" value=\"28315\"/><option displayName=\"L\" value=\"28316\"/><option displayName=\"XL\" value=\"28317\"/><option displayName=\"其它\" value=\"10122\"/></options></field><field id=\"alias_name\" name=\"别名\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"minLengthRule\" value=\"0\" exProperty=\"include\" unit=\"byte\"/><rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/></rules></field></fields></field><field id=\"item_sc_product_id\" name=\"商品货品Id\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/></rules></field><field id=\"item_status\" name=\"商品状态\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"出售中\" value=\"0\"/><option displayName=\"定时上架\" value=\"1\"/><option displayName=\"仓库中\" value=\"2\"/></options><default-value>0</default-value></field><field id=\"start_time\" name=\"开始时间\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"time\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_status\" value=\"1\" symbol=\"!=\"/></depend-group></rule></rules><default-value>2016-05-28 14:57:54</default-value></field><field id=\"lang\" name=\"商品文字的字符集\" type=\"singleCheck\"><options><option displayName=\"zh_CN\" value=\"zh_CN\"/><option displayName=\"zh_HK\" value=\"zh_HK\"/></options></field><field id=\"quantity\" name=\"商品数量\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"regexRule\" value=\"^[0-9]*$\"/><rule name=\"minValueRule\" value=\"0\" exProperty=\"include\"/><rule name=\"maxValueRule\" value=\"1000000\" exProperty=\"not include\"/><rule name=\"tipRule\" value=\"请认真填写。无货空挂，可能引起投诉与退款\" url=\"https://bangpai.taobao.com/group/thread/145336-9509231.htm\"/></rules></field><field id=\"price\" name=\"商品价格\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"decimal\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"regexRule\" value=\"^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(\\.[0-9]{0,2})?$\"/><rule name=\"tipRule\" value=\"商品价格 应在 销售属性表中所填 最高与最低价格 范围区间内。\"/><rule name=\"minValueRule\" value=\"1.00\" exProperty=\"not include\"/><rule name=\"maxValueRule\" value=\"100000000.00\" exProperty=\"not include\"/><rule name=\"1574394358_1\" value=\"商品价格必须在销售属性表中所填最高与最低价格范围区间内\"/><rule name=\"tipRule\" value=\"为避免价格变动引发的违规，请谨慎输入价格。\" url=\"https://rule.tmall.com/tdetail-2695.htm\"/></rules></field><field id=\"barcode\" name=\"条形码\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"tipRule\" value=\"如果规格（如颜色，容量，尺码等）区域已填写条形码，则此处不用填写。\"/><rule name=\"tipRule\" value=\"请严格按照外包装填写条形码信息。\"/></rules></field><field id=\"has_discount\" name=\"是否支持会员折扣\" type=\"singleCheck\"><options><option displayName=\"支持会员打折\" value=\"true\"/><option displayName=\"不支持会员打折\" value=\"false\"/></options><default-value>false</default-value></field><field id=\"inner_shop\" name=\"页面模板-淘宝店铺\" type=\"singleCheck\"><options><option displayName=\"默认宝贝详情页\" value=\"1259738344\"/></options></field><field id=\"delivery_way\" name=\"提取方式\" type=\"multiCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"邮寄\" value=\"2\"/></options></field><field id=\"locality_life.expirydate\" name=\"有效期\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"delivery_way\" value=\"1\" symbol=\"not contains\"/></depend-group></rule></rules><options><option displayName=\"按时间区间\" value=\"1\"/><option displayName=\"按购买日起时间区间\" value=\"2\"/><option displayName=\"按购买日起有效天数区间\" value=\"3\"/></options></field><field id=\"locality_life.expirydate.startend\" name=\"有效期按时间区间\" type=\"complex\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"locality_life.expirydate\" value=\"1\" symbol=\"!=\"/></depend-group></rule></rules><fields><field id=\"locality_life.expirydate.startend.start\" name=\"开始时间\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"date\"/></rules></field><field id=\"locality_life.expirydate.startend.end\" name=\"结束时间\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"date\"/></rules></field></fields></field><field id=\"locality_life.expirydate.end\" name=\"有效期按购买日起时间区间\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"date\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"locality_life.expirydate\" value=\"2\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"locality_life.expirydate.severalDays\" name=\"有效期按购买日起有效天数区间\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"locality_life.expirydate\" value=\"3\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"location\" name=\"所在地\" type=\"complex\"><fields><field id=\"prov\" name=\"省份\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field><field id=\"city\" name=\"城市\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field></fields></field><field id=\"item_source\" name=\"货源地\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field><field id=\"freight_payer\" name=\"运费承担方式\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"delivery_way\" value=\"2\" symbol=\"not contains\"/></depend-group></rule></rules><options><option displayName=\"卖家承担运费\" value=\"2\"/><option displayName=\"买家承担运费\" value=\"1\"/></options><default-value>1</default-value></field><field id=\"freight_by_buyer\" name=\"买家承担运费\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"freight_payer\" value=\"1\" symbol=\"!=\"/></depend-group></rule></rules><options><option displayName=\"使用运费模板\" value=\"postage\"/></options></field><field id=\"postage_id\" name=\"运费模板ID\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"long\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"freight_by_buyer\" value=\"postage\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"white_bg_image\" name=\"透明素材图\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/></rules></field><field id=\"after_sale_id\" name=\"售后说明模板ID\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"long\"/><rule name=\"devTipRule\" value=\"请使用taobao.aftersale.get接口获取售后说明模板信息\" url=\"http://api.taobao.com/apidoc/api.htm?path=cid:4-apiId:10448\"/></rules></field><field id=\"has_warranty\" name=\"保修\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"有\" value=\"true\"/><option displayName=\"无\" value=\"false\"/></options><default-value>false</default-value></field><field id=\"sell_promise\" name=\"退换货服务\" type=\"singleCheck\"><options><option displayName=\"是\" value=\"true\"/><option displayName=\"否\" value=\"false\"/></options><default-value>true</default-value></field><field id=\"has_showcase\" name=\"橱窗推荐\" type=\"singleCheck\"><options><option displayName=\"橱窗推荐\" value=\"true\"/><option displayName=\"橱窗不推荐\" value=\"false\"/></options><default-value>false</default-value></field><field id=\"outer_id\" name=\"商家外部编码\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"maxLengthRule\" value=\"64\" exProperty=\"include\" unit=\"byte\"/></rules></field><field id=\"description\" name=\"商品描述\" type=\"complex\"><rules><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/><rule name=\"maxLengthRule\" value=\"25000\" exProperty=\"include\" unit=\"character\"/><rule name=\"tipRule\" value=\"PC版描述最多支持发布20个有内容的描述模块\"/><rule name=\"requiredRule\" value=\"true\"/></rules><fields><field id=\"desc_module_28_cat_mod\" name=\"视频推介\" type=\"complex\"><fields><field id=\"desc_module_28_cat_mod_content\" name=\"视频推介内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_28_cat_mod_order\" name=\"视频推介排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_115_cat_mod\" name=\"店铺活动\" type=\"complex\"><fields><field id=\"desc_module_115_cat_mod_content\" name=\"店铺活动内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_115_cat_mod_order\" name=\"店铺活动排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_49_cat_mod\" name=\"关联推荐\" type=\"complex\"><fields><field id=\"desc_module_49_cat_mod_content\" name=\"关联推荐内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_49_cat_mod_order\" name=\"关联推荐排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_5_cat_mod\" name=\"商品参数\" type=\"complex\"><fields><field id=\"desc_module_5_cat_mod_content\" name=\"商品参数内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_5_cat_mod_order\" name=\"商品参数排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_127_cat_mod\" name=\"商品展示\" type=\"complex\"><fields><field id=\"desc_module_127_cat_mod_content\" name=\"商品展示内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field><field id=\"desc_module_127_cat_mod_order\" name=\"商品展示排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field></fields></field><field id=\"desc_module_25_cat_mod\" name=\"商品细节\" type=\"complex\"><fields><field id=\"desc_module_25_cat_mod_content\" name=\"商品细节内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field><field id=\"desc_module_25_cat_mod_order\" name=\"商品细节排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field></fields></field><field id=\"desc_module_7_cat_mod\" name=\"颜色选择\" type=\"complex\"><fields><field id=\"desc_module_7_cat_mod_content\" name=\"颜色选择内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_7_cat_mod_order\" name=\"颜色选择排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_23_cat_mod\" name=\"品质保障\" type=\"complex\"><fields><field id=\"desc_module_23_cat_mod_content\" name=\"品质保障内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_23_cat_mod_order\" name=\"品质保障排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_120_cat_mod\" name=\"适用年龄\" type=\"complex\"><fields><field id=\"desc_module_120_cat_mod_content\" name=\"适用年龄内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_120_cat_mod_order\" name=\"适用年龄排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_14_cat_mod\" name=\"包装图示\" type=\"complex\"><fields><field id=\"desc_module_14_cat_mod_content\" name=\"包装图示内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_14_cat_mod_order\" name=\"包装图示排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_13_cat_mod\" name=\"安装说明\" type=\"complex\"><fields><field id=\"desc_module_13_cat_mod_content\" name=\"安装说明内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_13_cat_mod_order\" name=\"安装说明排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_19_cat_mod\" name=\"使用说明\" type=\"complex\"><fields><field id=\"desc_module_19_cat_mod_content\" name=\"使用说明内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_19_cat_mod_order\" name=\"使用说明排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_1_cat_mod\" name=\"赠品介绍\" type=\"complex\"><fields><field id=\"desc_module_1_cat_mod_content\" name=\"赠品介绍内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_1_cat_mod_order\" name=\"赠品介绍排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_56_cat_mod\" name=\"买家须知\" type=\"complex\"><fields><field id=\"desc_module_56_cat_mod_content\" name=\"买家须知内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_56_cat_mod_order\" name=\"买家须知排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_10_cat_mod\" name=\"物流快递\" type=\"complex\"><fields><field id=\"desc_module_10_cat_mod_content\" name=\"物流快递内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_10_cat_mod_order\" name=\"物流快递排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_15_cat_mod\" name=\"售后\" type=\"complex\"><fields><field id=\"desc_module_15_cat_mod_content\" name=\"售后内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_15_cat_mod_order\" name=\"售后排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_17_cat_mod\" name=\"品牌介绍\" type=\"complex\"><fields><field id=\"desc_module_17_cat_mod_content\" name=\"品牌介绍内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_17_cat_mod_order\" name=\"品牌介绍排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field><field id=\"desc_module_user_mods\" name=\"自定义模块列表\" type=\"multiComplex\"><rules><rule name=\"tipRule\" value=\"PC版描述最多支持添加18个自定义描述模块\"/></rules><fields><field id=\"desc_module_user_mod_name\" name=\"自定义模块名称\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"maxLengthRule\" value=\"10\" exProperty=\"include\" unit=\"byte\"/></rules></field><field id=\"desc_module_user_mod_content\" name=\"自定义模块内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"html\"/><rule name=\"minLengthRule\" value=\"5\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"desc_module_user_mod_order\" name=\"自定义模块排序值\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"integer\"/></rules></field></fields></field></fields></field><field id=\"item_images\" name=\"商品图片\" type=\"complex\"><fields><field id=\"item_image_0\" name=\"商品图片\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/></rules></field><field id=\"item_image_1\" name=\"商品图片\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/></rules></field><field id=\"item_image_2\" name=\"商品图片\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/></rules></field><field id=\"item_image_3\" name=\"商品图片\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/></rules></field><field id=\"item_image_4\" name=\"商品图片\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/></rules></field></fields></field><field id=\"wap_desc\" name=\"无线商品描述\" type=\"complex\"><rules><rule name=\"maxLengthRule\" value=\"5000\" exProperty=\"include\" unit=\"character\"/></rules><fields><field id=\"wap_desc_summary\" name=\"无线商品描述摘要\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"textarea\"/><rule name=\"maxLengthRule\" value=\"140\" exProperty=\"include\" unit=\"character\"/></rules></field><field id=\"wap_desc_audio\" name=\"无线商品音频描述\" type=\"complex\"><fields><field id=\"wap_desc_audio_title\" name=\"无线商品描述音频标题\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/></rules></field><field id=\"wap_desc_audio_url\" name=\"无线商品描述音频文件地址\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"regexRule\" value=\"^.*\\.mp3$\"/><rule name=\"maxInputNumRule\" value=\"1\" exProperty=\"include\"/><rule name=\"maxTargetSizeRule\" value=\"204800\" exProperty=\"include\" unit=\"byte\"/></rules></field></fields></field><field id=\"wap_desc_content\" name=\"无线商品描述内容（文本或图片）\" type=\"multiComplex\"><fields><field id=\"wap_desc_content_type\" name=\"无线商品描述类型\" type=\"singleCheck\"><options><option displayName=\"图片\" value=\"image\"/><option displayName=\"文本\" value=\"text\"/></options></field><field id=\"wap_desc_content_content\" name=\"无线商品描述内容\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"textarea\"><depend-group operator=\"and\"><depend-express fieldId=\"wap_desc_content_type\" value=\"text\" symbol=\"==\"/></depend-group></rule><rule name=\"valueTypeRule\" value=\"url\"><depend-group operator=\"and\"><depend-express fieldId=\"wap_desc_content_type\" value=\"image\" symbol=\"==\"/></depend-group></rule><rule name=\"maxLengthRule\" value=\"500\" exProperty=\"include\" unit=\"character\"><depend-group operator=\"and\"><depend-express fieldId=\"wap_desc_content_type\" value=\"text\" symbol=\"==\"/></depend-group></rule><rule name=\"minImageSizeRule\" value=\"0x480\" exProperty=\"include\"><depend-group operator=\"and\"><depend-express fieldId=\"wap_desc_content_type\" value=\"image\" symbol=\"==\"/></depend-group></rule><rule name=\"maxImageSizeRule\" value=\"960x620\" exProperty=\"include\"><depend-group operator=\"and\"><depend-express fieldId=\"wap_desc_content_type\" value=\"image\" symbol=\"==\"/></depend-group></rule></rules></field></fields></field></fields></field><field id=\"item_size_weight\" name=\"商品物流重量体积\" type=\"complex\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><fields><field id=\"item_size\" name=\"商品物流体积(立方米)\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/></rules></field><field id=\"item_weight\" name=\"商品物流重量(千克)\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"regexRule\" value=\"^\\d+(\\.\\d+)?$\"/></rules></field></fields></field><field id=\"valid_thru\" name=\"有效期\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><options><option displayName=\"7天\" value=\"7\"/><option displayName=\"14天\" value=\"14\"/></options></field><field id=\"item_extend_structure_buy_quantity_limit_property\" name=\"倍数购买设置\" type=\"complex\"><fields><field id=\"field-916\" name=\"单品单位购买数量\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"decimal\"/></rules></field></fields></field><field id=\"is_taobao\" name=\"是否在淘宝和天猫显示\" type=\"singleCheck\"><rules><rule name=\"tipRule\" value=\"商品如果设置为在淘宝和天猫不显示，在淘宝、天猫的主站和后台均不显示该商品。（不设置默认为显示）\"/></rules><options><option displayName=\"是\" value=\"true\"/><option displayName=\"否\" value=\"false\"/></options></field><field id=\"is_ex\" name=\"是否在外店显示\" type=\"singleCheck\"><options><option displayName=\"是\" value=\"true\"/><option displayName=\"否\" value=\"false\"/></options></field><field id=\"is_3D\" name=\"是否是3D\" type=\"singleCheck\"><options><option displayName=\"是\" value=\"true\"/><option displayName=\"否\" value=\"false\"/></options></field><field id=\"service_version\" name=\"天猫系统服务版本\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"long\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"932552053_1\" value=\"系统提交的版本号需要和服务端服务版本号一致\"/><rule name=\"readOnlyRule\" value=\"true\"/></rules><default-value>11100</default-value></field><field id=\"viewschema_separateDelivery\" name=\"单独发货\" type=\"multiCheck\"><options><option displayName=\"不能和其它商品一起发货，加入购物车下单后将单独拆单\" value=\"83202\"/></options></field><field id=\"industrial_prd_qs\" name=\"工业产品生产许可证编号\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/></rules></field><field id=\"customsClearanceWay\" name=\"入关方案\" type=\"singleCheck\"><rules><rule name=\"tipRule\" value=\"邮关的商品，如遇海关抽检产生行邮税，需要商户承担;跨境申报的商品，根据海关新政，税金需要用户支持，请不要设置含税价\"/></rules><options><option displayName=\"跨境\" value=\"true\"/><option displayName=\"邮关\" value=\"false\"/></options><default-value>false</default-value></field><field id=\"hscode\" name=\"HS海关代码\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"tipRule\" value=\"当商品有sku的话，请设置sku维度的hscode\"/></rules></field><field id=\"original_location\" name=\"商品原产地\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"text\"/><rule name=\"requiredRule\" value=\"true\"/></rules></field><field id=\"global_arrive\" name=\"是否支持环球必达\" type=\"singleCheck\"><rules><rule name=\"tipRule\" value=\"跨境申报的商品，必须支持环球必达\"/></rules><options><option displayName=\"支持环球必达\" value=\"true\"/><option displayName=\"不支持环球必达\" value=\"false\"/></options><default-value>false</default-value></field><field id=\"wireless_desc\" name=\"新商品无线描述\" type=\"complex\"><fields><field id=\"item_info\" name=\"商品信息\" type=\"complex\"><rules><rule name=\"devTipRule\" value=\"该模块直接读取您已填写的商品信息，不需要编辑。如需查看实际效果，请通过发布后查看。\"/></rules><fields><field id=\"item_info_enable\" name=\"是否启用\" type=\"singleCheck\"><options><option displayName=\"启用\" value=\"true\"/><option displayName=\"不启用\" value=\"false\"/></options></field></fields></field><field id=\"item_picture\" name=\"商品图片\" type=\"complex\"><rules><rule name=\"devTipRule\" value=\"该模块最多可上传20张图片，图片类型支持png,jpg,jpeg。单个图片建议宽度为750px~1242px，高度≤1546px。\"/></rules><fields><field id=\"item_picture_enable\" name=\"是否启用\" type=\"singleCheck\"><options><option displayName=\"启用\" value=\"true\"/><option displayName=\"不启用\" value=\"false\"/></options></field><field id=\"item_picture_image_0\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_1\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_2\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_3\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_4\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_5\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_6\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_7\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_8\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_9\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_10\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_11\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_12\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_13\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_14\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_15\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_16\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_17\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_18\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"item_picture_image_19\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"item_picture_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field></fields></field><field id=\"coupon\" name=\"优惠\" type=\"complex\"><rules><rule name=\"devTipRule\" value=\"您可以选择下方已经设置好的店铺优惠券模板，也可以去商家中心设置店铺优惠券模板\"/></rules><fields><field id=\"coupon_enable\" name=\"是否启用\" type=\"singleCheck\"><options><option displayName=\"启用\" value=\"true\"/><option displayName=\"不启用\" value=\"false\"/></options></field><field id=\"coupon_id\" name=\"选择模板\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"coupon_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field></fields></field><field id=\"hot_recommanded\" name=\"同店推荐\" type=\"complex\"><rules><rule name=\"devTipRule\" value=\"您可以选择下方已经设置好的同店推荐模板，也可以去商家中心设置同店推荐模板\"/></rules><fields><field id=\"hot_recommanded_enable\" name=\"是否启用\" type=\"singleCheck\"><options><option displayName=\"启用\" value=\"true\"/><option displayName=\"不启用\" value=\"false\"/></options></field><field id=\"hot_recommanded_id\" name=\"选择模板\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"hot_recommanded_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field></fields></field><field id=\"shop_discount\" name=\"店铺活动\" type=\"complex\"><rules><rule name=\"devTipRule\" value=\"您可以选择下方已经设置好的店铺活动模板，也可以去商家中心设置店铺活动模板\"/></rules><fields><field id=\"shop_discount_enable\" name=\"是否启用\" type=\"singleCheck\"><options><option displayName=\"启用\" value=\"true\"/><option displayName=\"不启用\" value=\"false\"/></options></field><field id=\"shop_discount_id\" name=\"选择模板\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"shop_discount_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field></fields></field><field id=\"user_define\" name=\"自定义\" type=\"complex\"><rules><rule name=\"devTipRule\" value=\"该模块需要您自己命名，可上传两张图片，图片格式支持png,jpg,jpeg。单个图片建议宽度为750px~1242px，高度≤1546px。\"/></rules><fields><field id=\"user_define_enable\" name=\"是否启用\" type=\"singleCheck\"><options><option displayName=\"启用\" value=\"true\"/><option displayName=\"不启用\" value=\"false\"/></options></field><field id=\"user_define_name\" type=\"input\"><rules><rule name=\"maxValueRule\" value=\"12\" exProperty=\"include\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"devTipRule\" value=\"仅支持中文英、数字，长度不可超过12个字符。\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"user_define_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"user_define_image_0\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"user_define_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field><field id=\"user_define_image_1\" type=\"input\"><rules><rule name=\"valueTypeRule\" value=\"url\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"user_define_enable\" value=\"true\" symbol=\"!=\"/></depend-group></rule></rules></field></fields></field></fields></field></itemRule>";
//        String schema = "<itemRule>\n" +
//                "<field id=\"infos\" name=\"品牌信息\" type=\"input\">\n" +
//                "\t<rules>\n" +
//                "\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                "\t</rules>\n" +
//                "</field>\n" +
//                "<field id=\"title\" name=\"牌\" type=\"singleCheck\">\n" +
//                "\t<rules>\n" +
//                "\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                "\t</rules>\n" +
//                "</field>\n" +
//                "<field id=\"product\" name=\"商品\" type=\"complex\">\n" +
//                "<fields>\n" +
//                "\t<field id=\"sell_p_s\" name=\"卖点\" type=\"complex\">\n" +
//                "\t\t<fields>\n" +
//                "\t\t\t<field id=\"sell_p_0\" name=\"卖点1\" type=\"input\">\n" +
//                "\t\t\t</field>\n" +
//                "\t\t\t<field id=\"sell_p_1\" name=\"卖点2\" type=\"input\">\n" +
//                "\t\t\t</field>\n" +
//                "\t\t\t<field id=\"sell_p_2\" name=\"卖点3\" type=\"input\">\n" +
//                "\t\t\t</field>\n" +
//                "\t\t</fields>\n" +
//                "\t</field>\n" +
//                "\t<field id=\"col\" name=\"颜色\" type=\"multiCheck\">\n" +
//                "\t\t<rules>\n" +
//                "\t\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                "\t\t</rules>\n" +
//                "\t\t<options>\n" +
//                "\t\t<option displayName=\"蓝\" value=\"b\"/>\n" +
//                "\t\t<option displayName=\"红\" value=\"r\"/>\n" +
//                "\t\t</options>\n" +
//                "\t</field>\n" +
//                "</fields>\n" +
//                "</field>\n" +
//                "<field id=\"prop_13618191\" name=\"价格区间\" type=\"singleCheck\">\n" +
//                "\t<rules>\n" +
//                "\t<rule name=\"requiredRule\" value=\"true\"/>\n" +
//                "\t</rules>\n" +
//                "\t<options>\n" +
//                "\t<option displayName=\"500元以下\" value=\"51851\"/>\n" +
//                "\t<option displayName=\"500-1000元\" value=\"127685\"/>\n" +
//                "\t<option displayName=\"1001-5000元\" value=\"19577505\"/>\n" +
//                "\t<option displayName=\"5001-10000元\" value=\"111341\"/>\n" +
//                "\t<option displayName=\"10001-20000元\" value=\"129668\"/>\n" +
//                "\t<option displayName=\"20001-50000元\" value=\"831438256\"/>\n" +
//                "\t<option displayName=\"50001-100000元\" value=\"831404596\"/>\n" +
//                "\t<option displayName=\"100000元以上\" value=\"831462044\"/>\n" +
//                "\t</options>\n" +
//                "</field>\n" +
//                "</itemRule>\n";
//        List<Field> fields = SchemaReader.readXmlForList(schema);
//////        Map<String, Field> fieldMap = schemaToIdPropMap(schema);
//        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("002", 23, "DFS3028");
////
//        SxData sxData = sxProductService.getSxProductDataByGroupId("002", Long.valueOf("663055"));
//        ExpressionParser exp = new ExpressionParser(sxProductService, sxData);
////
//        ShopBean shopBean = new ShopBean();
////        shopBean.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());
//        shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());
////
//        Map<String, Field> res = sxProductService.constructMappingPlatformProps(fields, cmsMtPlatformMappingModel, shopBean, exp, "morse", true);
//        res.forEach((key, val) -> System.out.println(key + "=" + val.getValue()));
////        String res =
////                sxProductService.resolveDict("详情页描述", exp, shopBean, "morse", null);
////                "";
//        System.out.println(res);
        // constructMappingPlatformProps end


//        productService.bathUpdateWithSXResult("066",23,Long.valueOf("333"),Arrays.asList("c001", "c002", "c101"),"num001", "pro001","2016-04-25 11:12:13","2016-04-25 12:13:14","2016-04-25 13:14:15", CmsConstants.PlatformStatus.Instock);

//        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));

////        System.out.println(sxProductService.changeSize(2, "5.5"));
//        Set<String> imageUrlSet = new HashSet<String>();
//        imageUrlSet.add("Ori4.jsp");
//        imageUrlSet.add("Ori5.jsp");
//        sxProductService.uploadImage("066", 23, "333", null, imageUrlSet, "morse");

//        CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
//        model.setSeq(2323);
//        model.setCartId(8);
//        sxProductService.updateSxWorkload(model, 9, "morse");

//        productService.getLogicQty("066", Arrays.asList("skuL1", "skuL2", "skuL3"));

//        ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode("005",20, "74-721");

//        SxData sxData = new SxData();
//        sxData.setChannelId("066");
//        sxData.setCartId(23);
//        sxData.setGroupId(Long.valueOf(99));
//
//        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();
//        platform.setNumIId("num099-3");
//        sxData.setPlatform(platform);
//
//        List<CmsBtProductModel> listProduct = new ArrayList<>();
//        for (int index = 0; index < 3; index++) {
//            CmsBtProductModel model = new CmsBtProductModel();
//            CmsBtProductModel_Field fields = new CmsBtProductModel_Field();
//            fields.setCode("C" + String.valueOf(index));
//            model.setFields(fields);
//            listProduct.add(model);
//        }
//        sxData.setProductList(listProduct);
//
//        sxProductService.updateImsBtProduct(sxData, "p", "ljj");


        System.out.println("end");

//        List<CmsBtProductModel_Sku> skuSourceList = new ArrayList<>();
//
//        CmsBtProductModel_Sku sku13 = new CmsBtProductModel_Sku();
//        sku13.setSize("O/S");
//        sku13.setSkuCode("sku-O/S");
//        skuSourceList.add(sku13);
//
//        CmsBtProductModel_Sku sku11 = new CmsBtProductModel_Sku();
//        sku11.setSize("21.5cm");
//        sku11.setSkuCode("sku-21.5cm");
//        skuSourceList.add(sku11);
//
//        CmsBtProductModel_Sku sku15 = new CmsBtProductModel_Sku();
//        sku15.setSize("26");
//        sku15.setSkuCode("sku-26");
//        skuSourceList.add(sku15);
//
//        CmsBtProductModel_Sku sku10 = new CmsBtProductModel_Sku();
//        sku10.setSize("25.5");
//        sku10.setSkuCode("sku-25.5");
//        skuSourceList.add(sku10);
//
//        CmsBtProductModel_Sku sku1 = new CmsBtProductModel_Sku();
//        sku1.setSize("L");
//        sku1.setSkuCode("sku-l");
//        skuSourceList.add(sku1);
//
//        CmsBtProductModel_Sku sku12 = new CmsBtProductModel_Sku();
//        sku12.setSize("88.5cm1");
//        sku12.setSkuCode("sku-88.5cm1");
//        skuSourceList.add(sku12);
//
//        CmsBtProductModel_Sku sku8 = new CmsBtProductModel_Sku();
//        sku8.setSize("35");
//        sku8.setSkuCode("sku-35");
//        skuSourceList.add(sku8);
//
//        CmsBtProductModel_Sku sku4 = new CmsBtProductModel_Sku();
//        sku4.setSize("aaa");
//        sku4.setSkuCode("sku-aaa");
//        skuSourceList.add(sku4);
//
//        CmsBtProductModel_Sku sku5 = new CmsBtProductModel_Sku();
//        sku5.setSize("15");
//        sku5.setSkuCode("sku-15");
//        skuSourceList.add(sku5);
//
//        CmsBtProductModel_Sku sku9 = new CmsBtProductModel_Sku();
//        sku9.setSize("22cm");
//        sku9.setSkuCode("sku-22cm");
//        skuSourceList.add(sku9);
//
//        CmsBtProductModel_Sku sku2 = new CmsBtProductModel_Sku();
//        sku2.setSize("XXX");
//        sku2.setSkuCode("sku-xxx");
//        skuSourceList.add(sku2);
//
//        CmsBtProductModel_Sku sku7 = new CmsBtProductModel_Sku();
//        sku7.setSize("25");
//        sku7.setSkuCode("sku-25");
//        skuSourceList.add(sku7);
//
//        CmsBtProductModel_Sku sku3 = new CmsBtProductModel_Sku();
//        sku3.setSize("M");
//        sku3.setSkuCode("sku-m");
//        skuSourceList.add(sku3);
//
//        CmsBtProductModel_Sku sku6 = new CmsBtProductModel_Sku();
//        sku6.setSize("20cm");
//        sku6.setSkuCode("sku-20cm");
//        skuSourceList.add(sku6);
//
//        CmsBtProductModel_Sku sku14 = new CmsBtProductModel_Sku();
//        sku14.setSize("XS/S");
//        sku14.setSkuCode("sku-XS/S");
//        skuSourceList.add(sku14);

//        System.out.println("before sort");
//        for (CmsBtProductModel_Sku sku : skuSourceList) {
//            System.out.println("sku=" + sku.getSkuCode() + " ,  size=" + sku.getSize());
//        }

        // 大数据量
//        int max = 5000;
//        for (SkuSort skuSort : SkuSort.values()) {
//            if (skuSort == SkuSort.DIGIT) {
//                for (int index = 1; index <= max; index++) {
//                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//                    sku.setSize(String.valueOf(index));
//                    sku.setSkuCode("sku-" + index);
//                    skuSourceList.add(sku);
//                }
//            } else if (skuSort == SkuSort.DIGIT_UNITS) {
//                for (int index = 1; index <= max; index++) {
//                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//                    sku.setSize(String.valueOf(index) + "cm");
//                    sku.setSkuCode("sku-" + String.valueOf(index) + "cm");
//                    skuSourceList.add(sku);
//                }
//            } else {
//                for (int index = 1; index <= max; index++) {
//                    CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//                    sku.setSize(skuSort.getSize());
//                    sku.setSkuCode("sku-" + skuSort.name() + "-" + String.valueOf(index));
//                    skuSourceList.add(sku);
//                }
//            }
//        }

//        Map<String, Integer> mapSort = new HashMap<>();
//        for (SkuSort s : SkuSort.values()) {
//            mapSort.put(s.getSize(), Integer.valueOf(s.getSort()));
//        }
//
//        System.out.println("start:" + DateTimeUtil.getNowTimeStamp() + "  listSize=" + skuSourceList.size());
//        sortSkuInfo(skuSourceList);
//
//        System.out.println("");
//        System.out.println("after sort");
//        for (CmsBtProductModel_Sku sku : skuSourceList) {
////            System.out.println("sku=" + sku.getSkuCode() + " ,  size=" + sku.getSize() + " ,  sort=" + getSizeSort(sku.getSize()));
//            System.out.println("sku=" + sku.getSkuCode() + " ,  size=" + sku.getSize() + " ,  sort=" + getSizeSort(sku.getSize(), mapSort));
//        }
//
//        System.out.println("end:" + DateTimeUtil.getNowTimeStamp());

//        sxGetProductInfo.sortSkuInfo(skuSourceList);
//        System.out.println("end:" + DateTimeUtil.getNowTimeStamp());
    }

    public Map<String, Field> schemaToIdPropMap(String schema) throws TopSchemaException {
        Map<String, Field> fieldsMap = new HashMap<>();
        List<Field> fields = SchemaReader.readXmlForList(schema);

        for (Field field : fields)
        {
            fieldsMap.put(field.getId(), field);
        }
        return fieldsMap;
    }

    private void recursiveGetTaobaoFields(List<com.taobao.top.schema.field.Field> fields, List<com.taobao.top.schema.field.Field> resultFields) {
        for (com.taobao.top.schema.field.Field field : fields) {
            switch (field.getType()) {
                case COMPLEX:
                    recursiveGetTaobaoFields(((com.taobao.top.schema.field.ComplexField)field).getFieldList(), resultFields);
                    resultFields.add(field);
                    break;
                case MULTICOMPLEX:
                    recursiveGetTaobaoFields(((com.taobao.top.schema.field.MultiComplexField)field).getFieldList(), resultFields);
                    resultFields.add(field);
                    break;
                default:
                    resultFields.add(field);
            }
        }
    }

    private void recursiveGetFields(List<Field> fields, List<Field> resultFields) {
        for (Field field : fields) {
            switch (field.getType()) {
                case COMPLEX:
                    recursiveGetFields(((ComplexField)field).getFields(), resultFields);
                    resultFields.add(field);
                    break;
                case MULTICOMPLEX:
                    recursiveGetFields(((MultiComplexField)field).getFields(), resultFields);
                    resultFields.add(field);
                    break;
                default:
                    resultFields.add(field);
            }
        }
    }

//    /**
//     * 如果sxProductBean中含有要在该平台中上新的sku, 返回true
//     * 如果没有sku要上新，那么返回false
//     */
//    private boolean filtProductsByPlatform(SxProductBean sxProductBean) {
//        CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();
//        CmsBtProductGroupModel cmsBtProductModelGroupPlatform = sxProductBean.getCmsBtProductModelGroupPlatform();
//        List<CmsBtProductModel_Sku> cmsBtProductModelSkus = cmsBtProductModel.getSkus();
//        int cartId = cmsBtProductModelGroupPlatform.getCartId();
//
//        if (cmsBtProductModelSkus == null) {
//            return false;
//        }
//
//        for (Iterator<CmsBtProductModel_Sku> productSkuIterator = cmsBtProductModelSkus.iterator(); productSkuIterator.hasNext();) {
//            CmsBtProductModel_Sku cmsBtProductModel_sku = productSkuIterator.next();
//            if (!cmsBtProductModel_sku.isIncludeCart(cartId)) {
//                productSkuIterator.remove();
//            }
//        }
//        return !cmsBtProductModelSkus.isEmpty();
//    }


    @Test
    public void testDict() throws Exception {
        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        ShopBean shopBean = new ShopBean();
        shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());

        String[] extParameter = {"c001"};
        String val = sxProductService.resolveDict("京东产品图片-1", expressionParser, shopBean, "tom", extParameter);
        System.out.println(val);

        extParameter[0] = "c004";
        val = sxProductService.resolveDict("京东产品图片-1", expressionParser, shopBean, "tom", extParameter);
        System.out.println(val);
    }

	/**
     * 只是用来测试, 真实逻辑不是这样的
     */
    @Test
    public void testShopCustomCategory() throws Exception {
        String Separtor_Semicolon = ";";
        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id("010");
        shop.setCart_id("23");

        // 多个条件表达式用分号分隔用
        StringBuilder builder = new StringBuilder();
        // 条件表达式表platform_prop_id字段的检索条件为"seller_cids"加cartId
        String platformPropId = "seller_cids_" + shop.getCart_id();

        // 根据channelid和platformPropId取得cms_bt_condition_prop_value表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(shop.getOrder_channel_id(), platformPropId);

        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 优先使用条件表达式
        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                // ===================expressionParser会被共通函数替换掉================================
                String propValue = expressionParser.parse(conditionExpression, shop, "tom", null);  // TODO No.8 调用共通函数
//                String propValue = "";
                // 多个表达式(2392231-4345291格式)用分号分隔
                if (propValue != null) {
                    builder.append(propValue);
                    builder.append(Separtor_Semicolon);   // 用分号(";")分隔
                }
            }
        }
        // 移除最后的分号
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        // 店铺种类
        System.out.println(builder.toString());

    }

    @Test
    public void testJdPriceSection() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());

        SxData sxData = new SxData();
        sxData.setMaxPrice(499D);

        SingleCheckField field = new SingleCheckField();
        field.setId("13479");
        field.setName("价格");
        field.addOption("0-99元", "178345");
        field.addOption(" 100-199  ", "178346");
        field.addOption(" 200-299", "178347");
        field.addOption(" 300-499 ", "178348");
        field.addOption("500元以上", "178349");

        boolean blnRet = sxProductService.resolveJdPriceSection_before(shopBean, field);
        System.out.println(blnRet);

        Map<String, Field> mapRet = sxProductService.resolveJdPriceSection(field, sxData);
        Value value = (Value)mapRet.get(field.getId()).getValue();
        System.out.println(value.getValue());

    }


    public Map<String, Field> constructMappingPlatformProps(List<Field> fields, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, ShopBean shopBean, ExpressionParser expressionParser, String user, boolean isItem) throws Exception {

        return null;
    }

}