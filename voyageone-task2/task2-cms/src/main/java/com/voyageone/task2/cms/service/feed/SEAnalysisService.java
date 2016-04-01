package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.ShoeCityFeedBean;
import com.voyageone.task2.cms.dao.feed.ShoeCityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Shoe City 的 Feed 数据解析
 *
 * @author jonasvlag. 16/3/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class SEAnalysisService extends BaseTaskService {

    private final static Pattern ALL_ZERO = Pattern.compile("^0+$");

    @Autowired
    private ShoeCityDao shoeCityDao;

    @Autowired
    private Transformer transformer;

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "ShoeCityAnalysis";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // /opt/app-shared/voyageone_web/contents/other/third_party/016/Feed/shoecity-product-feed.csv

        List<ShoeCityFeedBean> beanList = new ArrayList<>();
        // TODO 测试文件地址
        CsvReader reader = new CsvReader("/Users/jonasvlag/Desktop/ShoeCity/shoecity-product-feed-20160328_20160328130020.csv");
        while (reader.readRecord()) {
            try {
                ShoeCityFeedBean bean = new ShoeCityFeedBean(reader);
                if (ALL_ZERO.matcher(bean.getUpc()).matches())
                    beanList.add(bean);
            } catch (Exception e) {
                logIssue(e, reader.getValues());
            }
        }

        $info("读取数据: " + beanList.size());

        shoeCityDao.clearTemp();

        $info("清空数据");

        int count = shoeCityDao.insertList(beanList);

        $info("插入数据: " + count);

        // 更新标识位和数据
        transformer.new Context(ChannelConfigEnums.Channel.SHOE_CITY, this).transform();

        // 开始处理
        List<ShoeCityFeedBean> unsaved = shoeCityDao.selectListUnsaved();

        SEAnalysisContext context = new SEAnalysisContext();

        unsaved.forEach(context::put);

        List<CmsBtFeedInfoModel> feedInfoModels = context.getCodeList();

        Map<String, List<CmsBtFeedInfoModel>> resultMap = feedToCmsService.updateProduct(ChannelConfigEnums.Channel.SHOE_CITY.getId(), feedInfoModels, getTaskName());

        List<CmsBtFeedInfoModel> succeed = resultMap.get("succeed");

        $info("成功数: " + succeed.size());

        if (!succeed.isEmpty())
            shoeCityDao.updateSucceed(succeed);
    }
}
