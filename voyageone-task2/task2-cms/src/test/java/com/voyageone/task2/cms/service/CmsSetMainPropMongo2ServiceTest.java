package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.category.match.FeedQuery;
import com.voyageone.category.match.MatchResult;
import com.voyageone.category.match.Searcher;
import com.voyageone.category.match.Tokenizer;
import com.voyageone.common.util.ListUtils;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by james on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSetMainPropMongo2ServiceTest {

    @Autowired
    CmsSetMainPropMongo2Service cmsSetMainPropMongo2Service;

    @Autowired
    private Searcher searcher;


    @Test
    public void onStartup() throws Exception {
        getMainCatInfo("Women's Sleepwear & Robes","pajama-sets","womens","Body Frosting Womens Cotton 2PC Pajama Set Tan S","Body Frosting");

    }
    public MatchResult getMainCatInfo(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
        // 取得查询条件
        FeedQuery query = getFeedQuery(feedCategoryPath, productType, sizeType, productNameEn, brand);

        // 调用主类目匹配接口，取得匹配度最高的一个主类目和sizeType
        MatchResult searchResult = searcher.search(query, false);
        if (searchResult == null) {
            String errMsg = String.format("调用Feed到主数据的匹配程序匹配主类目失败！[feedCategoryPath:%s] [productType:%s] " +
                    "[sizeType:%s] [productNameEn:%s] [brand:%s]", feedCategoryPath, productType, sizeType, productNameEn, brand);
            return null;
        }

        // 取得匹配度最高的主类目
        return searchResult;
    }
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
    @Test
    public void test(){

        List<String> categoryWhite = Arrays.asList("鞋靴","服饰");
        if (!ListUtils.isNull(categoryWhite)) {
            if (categoryWhite.stream().noneMatch(cat -> "服饰>服饰配件>手提包袋>手包".indexOf(cat) == 0)) {
                throw new BusinessException("主类目属于黑名单不能导入CMS：" );
            }
        }
    }

}