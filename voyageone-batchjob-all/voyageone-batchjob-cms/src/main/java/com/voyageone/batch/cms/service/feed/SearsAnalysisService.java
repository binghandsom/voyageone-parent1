package com.voyageone.batch.cms.service.feed;

import com.google.gson.Gson;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.batch.cms.dao.feed.SearsFeedDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.JaxbUtil;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.sears.bean.*;
import com.voyageone.common.components.sears.bean.ProductBean;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.Enums.FeedEnums.Name;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.BCBG;
import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SEARS;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
    private SearsWsdlInsert updateService;

    private static Integer PageSize = 500;

    private static List<Integer> failurepageList = new ArrayList<>();

    private static List<ProductBean> productAll = new ArrayList<>();


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
        clearLastData();

        // 取得feedList
        getSearsFeedList();

        // 取得feed数据
        getSearsFeedData();

        // 开始数据分析处理阶段
        transformer.new Context(SEARS, this).transform();
        $info("数据处理阶段结束");

        insertService.new Context(SEARS).postNewProduct();
    }

    private void getSearsFeedList() throws Exception {

        PaginationBean paginationBean = searsService.getProductsTotalPages(PageSize);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < paginationBean.getTotalPages(); i++) {
            final int finalI = i;
            executor.execute(() -> productDetailsRequest_Test(finalI));
        }
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                System.out.println("getSearsFeedList结束了！");
                break;
            }
            Thread.sleep(200);
        }

    }

    private void getSearsFeedData() throws Exception {


        ExecutorService executor = Executors.newFixedThreadPool(3);
        CommonUtil.splitList(productAll, 25).forEach(productBeans -> {
            executor.execute(() -> productDetailsRequest_Test2(productBeans));
        });
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            Thread.sleep(200);
        }

    }

    private void clearLastData() {
        // 删除所有
        searsSuperFeedDao.delete();
        searsSuperFeedDao.deleteAattribute();
    }


    private void productDetailsRequest_Test(int page) {
        $info(page + "");
//        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><products><product><itemId>000W245107170001" + page + "</itemId><product_details><productId>99900600ZK552000P</productId><brand>Vassarette</brand><description>The Tailored Camisole is a great layering piece under your tailored shirts. With soft, breathable fabric and built-up straps, you get the all-day comfort you desire. Wear with our matching Tailored Half Slip for an all over smooth look!</description><manufacturerName>MAYFAIR</manufacturerName><manufacturerPartNumber>24510718</manufacturerPartNumber><imageUrl>http://c.shld.net/rpx/i/s/i/spin/image/spin_prod_101591801</imageUrl><upc>090649160883</upc><weight>2</weight><category_id>113</category_id><categorization><id>113</id><vertical>Clothing</vertical><category>Women's Apparel</category><subcategory>Intimates</subcategory></categorization><mailable>false</mailable><storePickupEligible>true</storePickupEligible><salesRanking><verticalRanking /><categoryRanking /><subCategoryRanking /></salesRanking><productSpecifications><specification><label>Product Overview</label><key>Fabric Care</key><value>Machine wash</value></specification><specification><label>Product Overview</label><key>Material</key><value>Synthetics</value></specification><specification><key>Band Size</key><value>S</value></specification><specification><key>Fit</key><value>Women's</value></specification><specification><key>Color</key><value>White</value></specification></productSpecifications><htcCode /><modelNumber /><countryGroups /><countryOfOrigin /><imageWidth>0</imageWidth><imageHeight>0</imageHeight><price><sellPrice>10.0</sellPrice></price><availability><available>true</available><quantity>10</quantity></availability></product_details></product><product><itemId>000W245107170002" + page + "</itemId><product_details><productId>99900600ZK552001P</productId><brand>Vassarette</brand><description>The Tailored Camisole is a great layering piece under your tailored shirts. With soft, breathable fabric and built-up straps, you get the all-day comfort you desire. Wear with our matching Tailored Half Slip for an all over smooth look!</description><manufacturerName>MAYFAIR</manufacturerName><manufacturerPartNumber>24510718</manufacturerPartNumber><imageUrl>http://c.shld.net/rpx/i/s/i/spin/image/spin_prod_101591801</imageUrl><upc>090649160883</upc><weight>2</weight><category_id>113</category_id><categorization><id>113</id><vertical>Clothing</vertical><category>Women's Apparel</category><subcategory>Intimates</subcategory></categorization><mailable>true</mailable><storePickupEligible>true</storePickupEligible><salesRanking><verticalRanking /><categoryRanking /><subCategoryRanking /></salesRanking><productSpecifications><specification><label>Product Overview</label><key>Fabric Care</key><value>Machine wash</value></specification><specification><label>Product Overview</label><key>Material</key><value>Synthetics</value></specification><specification><key>Band Size</key><value>S</value></specification><specification><key>Fit</key><value>Women's</value></specification><specification><key>Color</key><value>White</value></specification></productSpecifications><htcCode /><modelNumber /><countryGroups /><countryOfOrigin /><imageWidth>0</imageWidth><imageHeight>0</imageHeight><price><sellPrice>10.0</sellPrice></price><availability><available>true</available><quantity>10</quantity></availability></product_details></product></products>";


        try {
            ProductResponse product = searsService.getAllProducts(page, PageSize, false, false, false);
            productAll.addAll(product.getProduct());
//            searsSuperFeedDao.insert(product);
//            searsSuperFeedDao.insertAattribute(product);
//            // 从上次访问出错的pageList中删除
//            if (failurepageList.indexOf(page) >= 0) {
//                failurepageList.remove(failurepageList.indexOf(page));
//            }
        } catch (Exception e) {
            // 追加到错误的list中 下次启动后再访问
            failurepageList.add(page);
            e.printStackTrace();
        }
    }

    private void productDetailsRequest_Test2(List<ProductBean> products) {
//        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><products><product><itemId>000W245107170001" + page + "</itemId><product_details><productId>99900600ZK552000P</productId><brand>Vassarette</brand><description>The Tailored Camisole is a great layering piece under your tailored shirts. With soft, breathable fabric and built-up straps, you get the all-day comfort you desire. Wear with our matching Tailored Half Slip for an all over smooth look!</description><manufacturerName>MAYFAIR</manufacturerName><manufacturerPartNumber>24510718</manufacturerPartNumber><imageUrl>http://c.shld.net/rpx/i/s/i/spin/image/spin_prod_101591801</imageUrl><upc>090649160883</upc><weight>2</weight><category_id>113</category_id><categorization><id>113</id><vertical>Clothing</vertical><category>Women's Apparel</category><subcategory>Intimates</subcategory></categorization><mailable>false</mailable><storePickupEligible>true</storePickupEligible><salesRanking><verticalRanking /><categoryRanking /><subCategoryRanking /></salesRanking><productSpecifications><specification><label>Product Overview</label><key>Fabric Care</key><value>Machine wash</value></specification><specification><label>Product Overview</label><key>Material</key><value>Synthetics</value></specification><specification><key>Band Size</key><value>S</value></specification><specification><key>Fit</key><value>Women's</value></specification><specification><key>Color</key><value>White</value></specification></productSpecifications><htcCode /><modelNumber /><countryGroups /><countryOfOrigin /><imageWidth>0</imageWidth><imageHeight>0</imageHeight><price><sellPrice>10.0</sellPrice></price><availability><available>true</available><quantity>10</quantity></availability></product_details></product><product><itemId>000W245107170002" + page + "</itemId><product_details><productId>99900600ZK552001P</productId><brand>Vassarette</brand><description>The Tailored Camisole is a great layering piece under your tailored shirts. With soft, breathable fabric and built-up straps, you get the all-day comfort you desire. Wear with our matching Tailored Half Slip for an all over smooth look!</description><manufacturerName>MAYFAIR</manufacturerName><manufacturerPartNumber>24510718</manufacturerPartNumber><imageUrl>http://c.shld.net/rpx/i/s/i/spin/image/spin_prod_101591801</imageUrl><upc>090649160883</upc><weight>2</weight><category_id>113</category_id><categorization><id>113</id><vertical>Clothing</vertical><category>Women's Apparel</category><subcategory>Intimates</subcategory></categorization><mailable>true</mailable><storePickupEligible>true</storePickupEligible><salesRanking><verticalRanking /><categoryRanking /><subCategoryRanking /></salesRanking><productSpecifications><specification><label>Product Overview</label><key>Fabric Care</key><value>Machine wash</value></specification><specification><label>Product Overview</label><key>Material</key><value>Synthetics</value></specification><specification><key>Band Size</key><value>S</value></specification><specification><key>Fit</key><value>Women's</value></specification><specification><key>Color</key><value>White</value></specification></productSpecifications><htcCode /><modelNumber /><countryGroups /><countryOfOrigin /><imageWidth>0</imageWidth><imageHeight>0</imageHeight><price><sellPrice>10.0</sellPrice></price><availability><available>true</available><quantity>10</quantity></availability></product_details></product></products>";

        List<String> skus = new ArrayList<>();
        products.forEach(productBean -> {
            skus.add(productBean.getItemId());
        });
        $info(skus.stream().collect(Collectors.joining(", ")));
        try {
            ProductResponse product = searsService.getProductsBySku(skus, true, true, true);
            searsSuperFeedDao.insert(product);
            searsSuperFeedDao.insertAattribute(product);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
