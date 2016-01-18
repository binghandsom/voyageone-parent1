package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.dao.feed.SearsFeedDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.sears.bean.PaginationBean;
import com.voyageone.common.components.sears.bean.ProductResponse;
import com.voyageone.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.JEWELRY;
import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SEARS;

/**
 * Bcbg 的 Feed 数据分析服务
 * <p>
 * Created by Jonas on 10/10/15.
 */
@Service
public class JewelryAnalysisService extends BaseTaskService {

    @Autowired
    private SearsFeedDao searsSuperFeedDao;

    @Autowired
    private SearsService searsService;

    @Autowired
    private JewelryWsdlInsert insertService;

    @Autowired
    private Transformer transformer;

    private static List<String> failurepageList = new ArrayList<>();

    private static final int ThreadPoolCnt = 10;


    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "CmsJEAnalySisJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 插入数据库
//        clearLastData();
//
//        // 取得feedList
//        getSearsFeedList();
//
//        // 取得feed数据
//        getSearsFeedData();
//
//        // 开始数据分析处理阶段
//        transformer.new Context(JEWELRY, this).transform();
//        $info("数据处理阶段结束");

        insertService.new Context(JEWELRY).postNewProduct();
//        updateService.new Context(SEARS).postUpdatedProduct();
    }
}
