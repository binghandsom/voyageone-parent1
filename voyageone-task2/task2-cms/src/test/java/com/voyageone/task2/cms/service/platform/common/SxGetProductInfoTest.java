package com.voyageone.task2.cms.service.platform.common;

import com.google.common.collect.Lists;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.service.model.ims.ImsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.text.DateFormat.*;

import java.text.DateFormat;
import java.util.*;

/**
 * Created by dell on 2016/4/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class SxGetProductInfoTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private SxGetProductInfo sxGetProductInfo;

    @Autowired
    private ImsBtProductDao imsBtProductDao;

    @Autowired
    private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;

    @Test
    public void testFuc() throws Exception {
        System.out.println();
        System.out.println("start:"+ DateTimeUtil.getNow());

        // constructMappingPlatformProps start
        String schema = "<itemRule>\n" +
                "<field id=\"infos\" name=\"品牌信息\" type=\"input\">\n" +
                "<rules>\n" +
                "<rule name=\"requiredRule\" value=\"true\"/>\n" +
                "</rules>\n" +
                "</field>\n" +
                "<field id=\"colors\" name=\"尺码\" type=\"multiCheck\">\n" +
                "<rules>\n" +
                "<rule name=\"requiredRule\" value=\"true\"/>\n" +
                "</rules>\n" +
                "<options>\n" +
                "<option displayName=\"蓝\" value=\"b\"/>\n" +
                "<option displayName=\"红\" value=\"r\"/>\n" +
                "</options>\n" +
                "</field>\n" +
                "<field id=\"title\" name=\"牌\" type=\"singleCheck\">\n" +
                "<rules>\n" +
                "<rule name=\"requiredRule\" value=\"true\"/>\n" +
                "</rules>\n" +
                "</field>\n" +
                "</itemRule>";
        List<Field> fields = SchemaReader.readXmlForList(schema);
//        Map<String, Field> fieldMap = schemaToIdPropMap(schema);
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.selectMappingByMainCatId("066", 23, "cid001");

        SxData sxData = sxProductService.getSxProductDataByGroupId("066", Long.valueOf("333"));
        ExpressionParser exp = new ExpressionParser(sxData);

        ShopBean shopBean = new ShopBean();
//        shopBean.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());
        shopBean.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());

        Map<String, Object> res = sxProductService.constructMappingPlatformProps(fields,cmsMtPlatformMappingModel,shopBean,exp,"morse");
//        String res = sxProductService.resolveDict("无线商品图片-1", exp, shopBean, "morse");
        System.out.println(res);
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
//        Integer[] arr = new Integer[]{1,2,6};
//        List<Integer> intList = new ArrayList<>(Arrays.asList(arr));
//        List<Integer> intList = new ArrayList<>();
//        Collections.addAll(intList, arr);
//        List<Integer> intList = Lists.newArrayList(arr);
//        intList.add(5);

//        List<Integer> intLiList = new LinkedList<>();
////        intLiList
//        intList.sort(Comparator.<Integer, Double>comparing(a -> a.doubleValue()).thenComparing(a -> a));
//        intList.sort(Comparator.comparing((Integer a) -> a).thenComparing(b -> b));
//        Comparator<Integer> c = ((a,b)->a.compareTo(b));
//        intList.sort(((a,b)->a.compareTo(b)));
//        int sum = 0;
//        for (Iterator<Integer> iter = intList.iterator(); iter.hasNext(); ) {
//            int v = iter.next().intValue();
//            sum += v;
//        }
//        System.out.println(sum);
//        getInstance();


//        String ss1 = "5.5cm";
//        String ss2 = "15.5cm";
//        String ss3 = "25.5cm1";
//        String ss4 = "25.5cm12";
//        String ss5 = "25525cm";
//        System.out.println(ss1.lastIndexOf("cm")==ss1.length()-2);
//        System.out.println(ss2.lastIndexOf("cm")==ss2.length()-2);
//        System.out.println(ss3.lastIndexOf("cm")==ss3.length()-2);
//        System.out.println(ss4.lastIndexOf("cm")==ss4.length()-2);
//        System.out.println(ss5.lastIndexOf("cm")==ss5.length()-2);
//
//        System.out.println(ss1.substring(0, ss1.length()-2));
//        System.out.println(ss2.substring(0, ss2.length()-2));
//        System.out.println(ss5.substring(0, ss5.length()-2));

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

}