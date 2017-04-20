package com.voyageone.task2.cms.dictSellerCids;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Charis on 2017/4/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Jingdong_928_Liking_SellerCids_DictTest extends BaseSellerCidsDictTest{

    private String C_MASTERCATEGORY = "catPathEn";
    private String C_BRAND = "brand";
    private String C_SIZE_TYPE = "sizeType";

    @Autowired
    private SxProductService sxProductService;

    @Test
    public void do随便测测() {
        String dict = "{\"ruleWordList\":[{\"type\":\"CUSTOM\",\"value\":{\"moduleName\":\"If\",\"userParam\":{\"condition\":{\"ruleWordList\":[{\"type\":\"CUSTOM\",\"value\":{\"moduleName\":\"ConditionAnd\",\"userParam\":{\"conditionListExpression\":{\"ruleWordList\":[{\"type\":\"CUSTOM\",\"value\":{\"moduleName\":\"ConditionEq\",\"userParam\":{\"firstParam\":{\"ruleWordList\":[{\"type\":\"MASTER\",\"value\":\"catPathEn\",\"extra\":null,\"defaultExpression\":null}]},\"secondParam\":{\"ruleWordList\":[{\"type\":\"TEXT\",\"value\":\"Clothing>Kids' & Baby>Boys (2-20)>Coats & Jackets\",\"url\":false}]},\"ignoreCaseFlg\":{\"ruleWordList\":[{\"type\":\"TEXT\",\"value\":\"1\",\"url\":false}]}}}}]}}}}]},\"propValue\":{\"ruleWordList\":[{\"type\":\"TEXT\",\"value\":\"5925030|5860203,5925030|服装>外套|服装,外套\",\"url\":false}]}}}}]}";

        SxData sxData = sxProductService.getSxDataByCodeWithoutCheck("928", 31, "1135013319", false);

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        ConditionPropValueModel conditionPropValueModel = new ConditionPropValueModel();
        conditionPropValueModel.setChannel_id("928");
        conditionPropValueModel.setCondition_expression(dict);

        String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
        RuleExpression conditionExpression = null;
        String propValue;
        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

        try {
            // 带名字字典解析
            if (conditionExpressionStr.startsWith("{\"type\":\"DICT\"")) {
                DictWord conditionDictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(conditionExpressionStr);
                conditionExpression = conditionDictWord.getExpression();
            } else if (conditionExpressionStr.startsWith("{\"ruleWordList\"")) {
                // 不带名字，只有字典表达式字典解析
                conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
            } else {
                System.out.println("出错了A");
            }

            // 店铺内分类字典的值（"cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"）
            // 例："1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"
            propValue = expressionParser.parse(conditionExpression, null, "taskname", null);
            System.out.println("propValue:" + propValue);
        } catch (Exception e) {
            // 因为店铺内分类即使这里不设置，运营也可以手动设置的，所以这里如果解析字典异常时，不算feed->master导入失败
            System.out.println("出错了B");
        }


    }

    @Test
    public void do京东各种() {


        List<String> cidList = readFromDisk();
        String cartId = "31";
        Map<String, SellerCids> sellerCidsMap = new LinkedHashMap<>();

        if ("28".equals(cartId)) {
            ShopBean shopBean = Shops.getShop("928", cartId);
            sellerCidsMap = getJdSellerCatList(shopBean);
        } else {
            ShopBean shopBean = Shops.getShop("928", cartId);
            shopBean.setShop_name("LIKING海外旗舰店");
            sellerCidsMap = getTmSellerCatList(shopBean);
        }



        for (String data : cidList) {
            //0.主类目 1.sizeType 2.brand 3.叶子类目（店铺内分类）
            String[] datas = data.split(",");

            if (datas.length != 4) continue;
            if (StringUtils.isEmpty(datas[3])) continue;

            List<SimpleCase> simpleCaseList = new ArrayList<>();
            simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_MASTERCATEGORY, datas[0], true));
            if (!StringUtils.isEmpty(datas[1])) simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, datas[1], true));
            if (!StringUtils.isEmpty(datas[2])) simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, datas[2], true));

            if (sellerCidsMap.containsKey(datas[3])) {
                doCreateJson(doCreateSimpleIf(simpleCaseList, sellerCidsMap.get(datas[3])));
            } else {
//                System.out.println(String.format("店铺内分类可能已被删除[category:%s][sizeType:%s][brand:%s][sellerCid:%s]", datas[0], datas[1], datas[2], datas[3]));
            }
        }
    }


    public static List<String> readFromDisk(){
        String data;
        ArrayList<String> dataList = new ArrayList<>();
        //读取文件
        File csv = new File("C:\\temp\\liking_sellerCids.txt");
        BufferedReader buf;
        try {
            buf = new BufferedReader(new FileReader(csv));
            for (String strs = buf.readLine(); strs != null; strs = buf.readLine()){

                if("".equalsIgnoreCase(strs)){
                    continue;
                }
                data = strs.trim();
                dataList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
