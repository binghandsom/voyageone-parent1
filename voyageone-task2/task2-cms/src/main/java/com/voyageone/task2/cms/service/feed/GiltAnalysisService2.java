package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.feed.GiltFeedDao2;
import com.voyageone.common.components.gilt.GiltSkuService;
import com.voyageone.common.components.gilt.bean.GiltCategory;
import com.voyageone.common.components.gilt.bean.GiltImage;
import com.voyageone.common.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.common.components.gilt.bean.GiltSku;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.cms.bean.SuperFeedGiltBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.GILT;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @author JiangJujsheng, 2016/3/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltAnalysisService2 extends BaseTaskService {

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "GiltAnalysis";
    }

    @Autowired
    private GiltFeedDao2 giltFeedDao;
    @Autowired
    private GiltSkuService giltSkuService;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static int pageIndex = 0;

    //允许webSericce请求超时的连续最大次数
    private static int ALLOWLOSEPAGECOUNT = 10;

    private static ThirdPartyConfigBean getFeedGetConfig() {
        return ThirdPartyConfigs.getThirdPartyConfig(GILT.getId(), "feed_get_config");
    }

    private static int getPageSize() {
        return Integer.valueOf(getFeedGetConfig().getProp_val2());
    }

    private static int getDelaySecond() {
        return Integer.valueOf(getFeedGetConfig().getProp_val1());
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        List<Runnable> runnables = new ArrayList<>();
        runnables.add(() -> {
            try {
                onStartupInThread();
            } catch (Exception e) {
                logger.error("执行job时发生错误", e);
            }
        });
        try {
            runWithThreadPool(runnables, taskControlList);
        } catch (InterruptedException exp) {
            logger.error("执行job线程时发生错误", exp);
        }
    }

    private void onStartupInThread() throws Exception {
        int delay = getDelaySecond();
        while(true) {
            $info("准备获取第 %s 页", pageIndex);
            List<GiltSku> skuList = getSkus(pageIndex);
            $info("取得 SKU: %s", skuList.size());
            if (skuList.isEmpty()){
                pageIndex = 0;
                break;
            }

            doFeedData(skuList);
            if (skuList.size() < getPageSize()) {
                pageIndex = 0;
                break;
            }

            if (delay > 0) {
                $info("阶段结束等待 %s 秒", delay);
                Thread.sleep(delay * 1000);
            }
            pageIndex++;
        }
    }

    private void doFeedData(List<GiltSku> skuList) throws Exception {
        // 准备数据/数据格式转换
        List<SuperFeedGiltBean> feedGiltBeanList = new ArrayList<SuperFeedGiltBean>(skuList.size());
        for (GiltSku giltSku : skuList) {
            feedGiltBeanList.add(toMySqlBean(giltSku));
        }
        $info("转换 SKU: %s", feedGiltBeanList.size());
        giltFeedDao.clearTemp();

        // 插入数据到gilt商品临时表
        int count = giltFeedDao.insertListTemp(feedGiltBeanList);
        $info("插入 TEMP SKU: %s", count);

        // 更新数据到gilt商品一览总表
        update(feedGiltBeanList);

        attribute();
    }

    // 更新到mongo db
    private void attribute() {
        //// TODO: 2016/3/3 gilt商品数据格式与mongo中feed的数据格式转换关系未知


    }

    private void update(List<SuperFeedGiltBean> feedGiltBeanList) throws Exception {
        $info("进入更新");
        List<SuperFeedGiltBean> addList = new ArrayList<SuperFeedGiltBean>();
        List<SuperFeedGiltBean> updList = new ArrayList<SuperFeedGiltBean>();
        for (SuperFeedGiltBean giltObj : feedGiltBeanList) {
            if (giltFeedDao.selectBySku(giltObj.getId())) {
                updList.add(giltObj);
            } else {
                addList.add(giltObj);
            }
        }
        int rslt1 = giltFeedDao.insertGiltList(addList);
        $info("\t新增gilt数据 -> %s", rslt1);
        int rslt2 = giltFeedDao.updateGiltList(updList);
        $info("\t更新gilt数据 -> %s", rslt2);

        if (feedGiltBeanList.size() != rslt1 + rslt2) {
            logger.error("更新结果与期望不符，预期更新条数为" + feedGiltBeanList.size());
        }
    }


    private List<GiltSku> getSkus(int index) throws Exception {
        GiltPageGetSkusRequest request = new GiltPageGetSkusRequest();
        request.setOffset(index * getPageSize());
        request.setLimit(getPageSize());

        List<GiltSku> giltSkus = new ArrayList<>();
        int losePageCount = 1;
        while (true) {
            try {
                giltSkus =  giltSkuService.pageGetSkus(request);
                break;
            } catch (Exception e) {
                if(losePageCount == ALLOWLOSEPAGECOUNT){
                    String msg = "已经连续【" + ALLOWLOSEPAGECOUNT + "】次请求webService库存数据失败！" + e;
                    logger.info("----------" + msg + "----------");
                    throw new RuntimeException(e);
                }
                losePageCount ++;
                continue;
            }
        }

        return giltSkus;
    }


    private SuperFeedGiltBean toMySqlBean(GiltSku giltSku) {

        SuperFeedGiltBean superFeedGiltBean = new SuperFeedGiltBean();

        // TODO UPC 貌似没处理

        superFeedGiltBean.setId(String.valueOf(giltSku.getId()));
        superFeedGiltBean.setProduct_id(String.valueOf(giltSku.getProduct_id()));
        superFeedGiltBean.setProduct_look_id(String.valueOf(giltSku.getProduct_look_id()));
        superFeedGiltBean.setLocale(String.valueOf(giltSku.getLocale()));
        superFeedGiltBean.setName(String.valueOf(giltSku.getName()));
        superFeedGiltBean.setDescription(String.valueOf(giltSku.getDescription()));
        superFeedGiltBean.setCountry_code(String.valueOf(giltSku.getCountry_code()));
        superFeedGiltBean.setBrand_id(String.valueOf(giltSku.getBrand().getId()));
        superFeedGiltBean.setBrand_name(String.valueOf(giltSku.getBrand().getName()));
        superFeedGiltBean.setImages_url(giltSku.getImages().stream().map(GiltImage::getUrl).collect(joining(",")));

        List<Object> attributes = giltSku.getAttributes();

        for (Object object : attributes) {

            Map<String, Map<String, Object>> map1 = (Map<String, Map<String, Object>>) object;
            Map<String, Object> map2;

            if (map1.containsKey("color")) {
                map2 = map1.get("color");
                superFeedGiltBean.setAttributes_color_nfr_code(String.valueOf(map2.get("nrf_code")));
                superFeedGiltBean.setAttributes_color_name(String.valueOf(map2.get("name")));
            } else if (map1.containsKey("style")) {
                map2 = map1.get("style");
                superFeedGiltBean.setAttributes_style_name(String.valueOf(map2.get("name")));
            } else if (map1.containsKey("material")) {
                map2 = map1.get("material");
                superFeedGiltBean.setAttributes_material_value(String.valueOf(map2.get("value")));
            } else if (map1.containsKey("size")) {
                map2 = map1.get("size");
                superFeedGiltBean.setAttributes_size_size_chart_id(String.valueOf(map2.get("size_chart_id")));
                superFeedGiltBean.setAttributes_size_type(String.valueOf(map2.get("type")));
                superFeedGiltBean.setAttributes_size_value(String.valueOf(map2.get("value")));
            }
        }

        superFeedGiltBean.setPrices_retail_currency(String.valueOf(giltSku.getPrices().getRetail().getCurrency()));
        superFeedGiltBean.setPrices_retail_value(String.valueOf(giltSku.getPrices().getRetail().getValue()));
        superFeedGiltBean.setPrices_sale_currencty(String.valueOf(giltSku.getPrices().getSale().getCurrency()));
        superFeedGiltBean.setPrices_sale_value(String.valueOf(giltSku.getPrices().getSale().getValue()));
        superFeedGiltBean.setPrices_cost_currency(String.valueOf(giltSku.getPrices().getCost().getCurrency()));
        superFeedGiltBean.setPrices_cost_value(String.valueOf(giltSku.getPrices().getCost().getValue()));
        superFeedGiltBean.setProduct_codes(StringUtils.join(giltSku.getProduct_codes(), ","));
        superFeedGiltBean.setProduct_codes_first(String.valueOf(giltSku.getProduct_codes().get(0)));

        List<GiltCategory> categories = giltSku.getCategories();
        GiltCategory giltCategory = categories.get(categories.size() - 1);

        superFeedGiltBean.setCategories_id(String.valueOf(giltCategory.getId()));
        superFeedGiltBean.setCategories_key(giltCategory.getKey().replaceAll("_", "-"));
        superFeedGiltBean.setCategories_name(giltCategory.getName());

        return superFeedGiltBean;
    }
}
