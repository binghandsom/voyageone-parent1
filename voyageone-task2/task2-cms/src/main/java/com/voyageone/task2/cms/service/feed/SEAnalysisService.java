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

import java.io.File;
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

    private final static String FILE_PATH = "/usr/web/contents/other/third_party/016/Feed/";

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

        // 打开目录
        File feedFileDir = new File(FILE_PATH);
        // 过滤文件
        File[] feedFiles = feedFileDir.listFiles(i -> i.getName().contains(".csv"));

        if (feedFiles.length < 1) {
            $info("木有找到文件");
            return;
        }

        List<ShoeCityFeedBean> beanList = new ArrayList<>();
        CsvReader reader = new CsvReader(feedFiles[0].getPath());
        while (reader.readRecord()) {
            try {
                ShoeCityFeedBean bean = new ShoeCityFeedBean(reader);
                if (!ALL_ZERO.matcher(bean.getUpc()).matches())
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
