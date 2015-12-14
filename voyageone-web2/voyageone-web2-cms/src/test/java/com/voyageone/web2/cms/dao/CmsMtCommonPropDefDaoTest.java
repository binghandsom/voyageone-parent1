package com.voyageone.web2.cms.dao;

import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.rule.RequiredRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubuchun 15/12/11
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsMtCommonPropDefDaoTest {

    @Autowired
    private CmsMtCommonPropDefDao cmsMtCommonPropDefDao;

    @Test
    public void insertField() {
        List<Field> comFields = new ArrayList<>();

        // 1
        SingleCheckField productStatus1 = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
        productStatus1.setId("item_status");
        productStatus1.setName("商品状态");
        List<Rule> rules1 = new ArrayList<>();
        Rule rule1 = new RequiredRule("true");
        rules1.add(rule1);
        productStatus1.setRules(rules1);

        productStatus1.setInputLevel(0);
        productStatus1.setIsDisplay(1);
        productStatus1.initDefaultField();

        List<Option> options = new ArrayList<>();
        Option option1 = new Option();
        option1.setDisplayName("出售中");
        option1.setValue("0");
        options.add(option1);

        Option option2 = new Option();
        option2.setDisplayName("定时上架");
        option2.setValue("1");
        options.add(option2);

        Option option3 = new Option();
        option3.setDisplayName("仓库中");
        option3.setValue("2");
        options.add(option3);
        productStatus1.setOptions(options);

        comFields.add(productStatus1);

        // 2
        SingleCheckField productStatus2 = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
        productStatus2.setId("hsCodeCrop");
        productStatus2.setName("税号集货");
        productStatus2.setIsDisplay(1);
        productStatus2.setInputLevel(0);
        productStatus2.setDataSource("optConfig");

        List<Rule> rules2 = new ArrayList<>();
        Rule rule2 = new RequiredRule("true");
        rules2.add(rule2);
        productStatus2.setRules(rules2);

        comFields.add(productStatus2);

        // 3
        SingleCheckField productStatus3 = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
        productStatus3.setId("hsCodePrivate");
        productStatus3.setName("税号个人");
        productStatus3.setIsDisplay(1);
        productStatus3.setInputLevel(0);
        productStatus3.setDataSource("optConfig");

        List<Rule> rules3 = new ArrayList<>();
        Rule rule3 = new RequiredRule("true");
        rules3.add(rule3);
        productStatus3.setRules(rules3);

        comFields.add(productStatus3);

        cmsMtCommonPropDefDao.insertWithList(comFields);

        assert true;
    }
}