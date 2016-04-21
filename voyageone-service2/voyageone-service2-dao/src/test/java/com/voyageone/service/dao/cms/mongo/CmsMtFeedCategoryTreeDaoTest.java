package com.voyageone.service.dao.cms.mongo;

import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModelx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jonasvlag on 16/3/28.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtFeedCategoryTreeDaoTest {

    @Autowired
    private CmsMtFeedCategoryTreeDao feedCategoryTreeDao;

    @Test
    public void testConcat() {
//        CmsMtFeedCategoryTreeModelx treeModelx = feedCategoryTreeDao.selectTopCategory("010", "Bracelets");
//        CmsMtFeedCategoryModel feedCategoryModel = treeModelx.getCategoryTree().get(0);
//
//        List<CmsMtFeedCategoryModel> b = useStream(feedCategoryModel);
//
//        List<CmsMtFeedCategoryModel> a = useDefaultList(feedCategoryModel);
//
//        assert a.size() == b.size();
    }

    private List<CmsMtFeedCategoryModel> useStream(CmsMtFeedCategoryModel feedCategoryModel) {
        long start = System.currentTimeMillis();
        Stream<CmsMtFeedCategoryModel> feedCategoryModelStream = flattenByStream(feedCategoryModel);
        List<CmsMtFeedCategoryModel> flattenList = feedCategoryModelStream.collect(Collectors.toList());
        long end = System.currentTimeMillis();
        System.out.println("用流拍平的毫秒数: " + (end - start));
        return flattenList;
    }

    private Stream<CmsMtFeedCategoryModel> flattenByStream(CmsMtFeedCategoryModel feedCategoryModel) {
        Stream<CmsMtFeedCategoryModel> feedCategoryModelStream = Stream.of(feedCategoryModel);
        if (!feedCategoryModel.getChild().isEmpty())
            feedCategoryModelStream = Stream.concat(feedCategoryModelStream, feedCategoryModel.getChild().stream().flatMap(this::flattenByStream));
        return feedCategoryModelStream;
    }

    private List<CmsMtFeedCategoryModel> useDefaultList(CmsMtFeedCategoryModel feedCategoryModel) {
        long start = System.currentTimeMillis();
        List<CmsMtFeedCategoryModel> flattenList = new ArrayList<>();
        flattenByList(feedCategoryModel, flattenList);
        long end = System.currentTimeMillis();
        System.out.println("默认拍平的毫秒数: " + (end - start));
        return flattenList;
    }

    private void flattenByList(CmsMtFeedCategoryModel feedCategoryModel, List<CmsMtFeedCategoryModel> result) {
        result.add(feedCategoryModel);
        for (CmsMtFeedCategoryModel model: feedCategoryModel.getChild()) {
            flattenByList(model, result);
        }
    }
}