package com.voyageone.task2.cms.service;

import com.voyageone.category.match.FeedQuery;
import com.voyageone.category.match.Searcher;
import com.voyageone.category.match.Tokenizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 2017/3/23
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class SetMainPropServiceTest {

    @Autowired
    SetMainPropService setMainPropService;
    @Autowired
    private Searcher searcher;

    @Test
    public void getMainCatInfoTest() {
//        (String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand)

        // 取得查询条件
        FeedQuery query = getFeedQuery("Men's Athletic Shoes", "fashion-sneakers", "mens", "Teva Mens Kimtah Cut-Out Fisherman Athletic Shoes Black 8", "Teva");

        // 调用主类目匹配接口，取得匹配度最高的一个主类目和sizeType
        com.voyageone.category.match.MatchResult searchResult = searcher.search(query, false);
        if (searchResult == null) {
//            String errMsg = String.format("调用Feed到主数据的匹配程序匹配主类目失败！[feedCategoryPath:%s] [productType:%s] " +
//                    "[sizeType:%s] [productNameEn:%s] [brand:%s]", feedCategoryPath, productType, sizeType, productNameEn, brand);
//            $error(errMsg);
//            return null;
        }

        // 取得匹配度最高的主类目
//        return searchResult;
    }

    /**
     * 取得查询条件
     *
     * @param feedCategoryPath feed类目Path
     * @param productType      产品分类
     * @param sizeType         适合人群(英文)
     * @param productNameEn    产品名称（英文）
     * @param brand            产品品牌
     * @return FeedQuer 查询条件
     */
    private FeedQuery getFeedQuery(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
        // 调用Feed到主数据的匹配程序匹配主类目
        // 子店feed类目path分隔符(由于导入feedInfo表时全部替换成用"-"来分隔了，所以这里写固定值就可以了)
        List<String> categoryPathSplit = new ArrayList<>();
        categoryPathSplit.add("-");
        Tokenizer tokenizer = new Tokenizer(categoryPathSplit);

        FeedQuery query = new FeedQuery(feedCategoryPath, null, tokenizer);
        query.setProductType(productType);
        query.setSizeType(sizeType);
        query.setProductName(productNameEn, brand);

        return query;
    }

}