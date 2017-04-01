package com.voyageone.category.match;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
/**
 * Created by jonas on 2016/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ExcelImporterTest extends TestCase {
    private static final String TEST_FILEPATH1 = "/Users/linanbin/Documents/3.files/0.SVN/2.ProductDesignManage/96.亚马逊主类目数据整理/1.Master Category文档/主数据结构资料-Master Category & Key Word.xlsx";
    private static final String TEST_FILEPATH2 = "/Users/linanbin/Documents/3.files/0.SVN/2.ProductDesignManage/96.亚马逊主类目数据整理/1.Master Category文档/主数据结构资料-Master Data Frame（系统导入版）.xlsx";

    @Autowired
    ExcelImporter excelImporter;
    @Test
    public void importDataToDb() throws Exception {
        excelImporter.importDataToDb(TEST_FILEPATH1, TEST_FILEPATH2);
    }
    @Test
    public void testFormat() {
        NumberFormat formatter = new DecimalFormat("#");
        System.out.println(formatter.format(1.01));
    }
}