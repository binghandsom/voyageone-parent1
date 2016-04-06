package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.feed.SearsFeedDao;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.sears.bean.*;
import com.voyageone.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SEARS;
import static java.util.stream.Collectors.groupingBy;

/**
 * Bcbg 的 Feed 数据分析服务
 * <p>
 * Created by Jonas on 10/10/15.
 */
@Service
public class SearsAnalysisService extends BaseTaskService {

    @Autowired
    private SearsFeedDao searsSuperFeedDao;

    @Autowired
    private SearsService searsService;

    @Autowired
    private SearsWsdlInsert insertService;

    @Autowired
    private Transformer transformer;

    @Autowired
    private SearsWsdlUpdate updateService;

    private static Integer PageSize = 500;

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
        return "SearsAnalysis";
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
        transformer.new Context(SEARS, this).transform();
        $info("数据处理阶段结束");

        insertService.new Context(SEARS).postNewProduct();
//        updateService.new Context(SEARS).postUpdatedProduct();
    }

    /**
     * 获取变更的feed列表
     *
     * @throws Exception
     */
    private void getSearsFeedList() throws Exception {

        PaginationBean paginationBean = searsService.getProductsTotal();
        int productsTotal = paginationBean.getTotalProducts();
        int pageCount = productsTotal / PageSize + (productsTotal % PageSize > 0 ? 1 : 0);

        $info("feed总数 " + productsTotal + " 总页数 " + pageCount);
        ExecutorService executor = Executors.newFixedThreadPool(ThreadPoolCnt);
        for (int i = 1; i < pageCount + 1; i++) {
            final int finalI = i;
            executor.execute(() -> feedListInsertTask(finalI));
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        $info("获取变更的feed列表结束了！");
    }

    /**
     * 获取feed的详细数据
     *
     * @throws Exception
     */
    private void getSearsFeedData() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(ThreadPoolCnt);

        // 获取追加和更新的数据
        List<String> feedlist = searsSuperFeedDao.getFeedList("A");
        feedlist.addAll(searsSuperFeedDao.getFeedList("U"));

        // 取得详细信息 25个SKU请求一次
        CommonUtil.splitList(feedlist, 25).forEach(productBeans -> {
            executor.execute(() -> feedDetailsInsertTask(productBeans));
        });
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        $info("获取feed数据结束了！");
        $info(String.format("失败的feed(%d)件进行单个导入", failurepageList.size()));

        // 失败的数据再给一次机会
        if (failurepageList.size() > 0) {
            ExecutorService executor2 = Executors.newFixedThreadPool(ThreadPoolCnt);
            List<String> temp = new ArrayList<>(Arrays.asList(new String[failurepageList.size()]));
            Collections.copy(temp, failurepageList);
            failurepageList.clear();

            // 一个个SKU单独取得数据
            CommonUtil.splitList(temp, 1).forEach(productBeans -> {
                executor2.execute(() -> feedDetailsInsertTask(productBeans));
            });
            executor2.shutdown();
            executor2.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            $info(String.format("最后feed(%d)件读入失败", failurepageList.size()));
            if (failurepageList.size() > 0) {
                issueLog.log("SearsFeed导入", "Sears读入失败", ErrorType.BatchJob, SubSystem.CMS, failurepageList.stream().collect(Collectors.joining(", ")));
            }
        }
    }

    /**
     * 删除上次数据
     */
    private void clearLastData() {
        // 删除所有
        searsSuperFeedDao.delete();
        searsSuperFeedDao.deleteAattribute();
        searsSuperFeedDao.deleteList();
    }


    /**
     * feed列表取得并插入数据库
     */
    private void feedListInsertTask(int page) {
        $info(page + "");
        try {
            ProductResponse product = searsService.getAllProducts(page, PageSize);
            searsSuperFeedDao.insertFeedList(product);
        } catch (Exception e) {
            $error(e);
        }
    }

    /**
     * feed详细信息取得并插入数据库
     */
    private void feedDetailsInsertTask(List<String> products) {
//        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><products><product><itemId>000W245107170001" + page + "</itemId><product_details><productId>99900600ZK552000P</productId><brand>Vassarette</brand><description>The Tailored Camisole is a great layering piece under your tailored shirts. With soft, breathable fabric and built-up straps, you get the all-day comfort you desire. Wear with our matching Tailored Half Slip for an all over smooth look!</description><manufacturerName>MAYFAIR</manufacturerName><manufacturerPartNumber>24510718</manufacturerPartNumber><imageUrl>http://c.shld.net/rpx/i/s/i/spin/image/spin_prod_101591801</imageUrl><upc>090649160883</upc><weight>2</weight><category_id>113</category_id><categorization><id>113</id><vertical>Clothing</vertical><category>Women's Apparel</category><subcategory>Intimates</subcategory></categorization><mailable>false</mailable><storePickupEligible>true</storePickupEligible><salesRanking><verticalRanking /><categoryRanking /><subCategoryRanking /></salesRanking><productSpecifications><specification><label>Product Overview</label><key>Fabric Care</key><value>Machine wash</value></specification><specification><label>Product Overview</label><key>Material</key><value>Synthetics</value></specification><specification><key>Band Size</key><value>S</value></specification><specification><key>Fit</key><value>Women's</value></specification><specification><key>Color</key><value>White</value></specification></productSpecifications><htcCode /><modelNumber /><countryGroups /><countryOfOrigin /><imageWidth>0</imageWidth><imageHeight>0</imageHeight><price><sellPrice>10.0</sellPrice></price><availability><available>true</available><quantity>10</quantity></availability></product_details></product><product><itemId>000W245107170002" + page + "</itemId><product_details><productId>99900600ZK552001P</productId><brand>Vassarette</brand><description>The Tailored Camisole is a great layering piece under your tailored shirts. With soft, breathable fabric and built-up straps, you get the all-day comfort you desire. Wear with our matching Tailored Half Slip for an all over smooth look!</description><manufacturerName>MAYFAIR</manufacturerName><manufacturerPartNumber>24510718</manufacturerPartNumber><imageUrl>http://c.shld.net/rpx/i/s/i/spin/image/spin_prod_101591801</imageUrl><upc>090649160883</upc><weight>2</weight><category_id>113</category_id><categorization><id>113</id><vertical>Clothing</vertical><category>Women's Apparel</category><subcategory>Intimates</subcategory></categorization><mailable>true</mailable><storePickupEligible>true</storePickupEligible><salesRanking><verticalRanking /><categoryRanking /><subCategoryRanking /></salesRanking><productSpecifications><specification><label>Product Overview</label><key>Fabric Care</key><value>Machine wash</value></specification><specification><label>Product Overview</label><key>Material</key><value>Synthetics</value></specification><specification><key>Band Size</key><value>S</value></specification><specification><key>Fit</key><value>Women's</value></specification><specification><key>Color</key><value>White</value></specification></productSpecifications><htcCode /><modelNumber /><countryGroups /><countryOfOrigin /><imageWidth>0</imageWidth><imageHeight>0</imageHeight><price><sellPrice>10.0</sellPrice></price><availability><available>true</available><quantity>10</quantity></availability></product_details></product></products>";

        $info(products.stream().collect(Collectors.joining(", ")));
        try {
            ProductResponse product = searsService.getProductsBySku(products, true, true, false);
            searsSuperFeedDao.insert(product);
            searsSuperFeedDao.insertAattribute(product);

        } catch (Exception e) {
            $error(e);
            // 追加到错误的list中 下次启动后再访问
            failurepageList.addAll(products);
        }
    }
}
