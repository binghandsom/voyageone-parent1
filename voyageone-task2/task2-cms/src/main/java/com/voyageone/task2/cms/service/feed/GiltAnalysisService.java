package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.gilt.bean.GiltCategory;
import com.voyageone.components.gilt.bean.GiltImage;
import com.voyageone.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.components.gilt.bean.GiltSku;
import com.voyageone.components.gilt.service.GiltSkuService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.SuperFeedGiltBean;
import com.voyageone.task2.cms.dao.SuperFeed2Dao;
import com.voyageone.task2.cms.dao.feed.GiltFeedDao;
import com.voyageone.task2.cms.model.WmsBtClientSkuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.GILT;
import static java.util.stream.Collectors.joining;

/**
 * @author james.li
 * @version 0.0.1, 16/3/4
 */
@Service
public class GiltAnalysisService extends BaseTaskService {

    private static int pageIndex = 0;
    //允许webSericce请求超时的连续最大次数
    private static int ALLOWLOSEPAGECOUNT = 10;
    @Autowired
    private SuperFeed2Dao superfeeddao;
    @Autowired
    private Transformer transformer;
    @Autowired
    private GiltFeedDao giltFeedDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private GiltInsert insertService;
    @Autowired
    private GiltSkuService giltSkuService;
    private Long lastExecuteTime = 0L;

    private static ThirdPartyConfigBean getFeedGetConfig() {
        return ThirdPartyConfigs.getThirdPartyConfig(GILT.getId(), "feed_get_config");
    }

    private static int getPageSize() {
        return Integer.valueOf(getFeedGetConfig().getProp_val2());
    }

    private static int getDelaySecond() {
        return Integer.valueOf(getFeedGetConfig().getProp_val1());
    }

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
        return "Cms2GiltAnalysisJob";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        Long scheduled = 24 * 60 * 60 * 1000L;
        String val1 = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.scheduled_time);
        TaskControlBean taskControlBean = TaskControlUtils.getVal1s(taskControlList,TaskControlEnums.Name.run_flg).get(0);
        if(!StringUtils.isEmpty(taskControlBean.getEnd_time())){
            lastExecuteTime = Long.parseLong(taskControlBean.getEnd_time());
        }
        if (!StringUtils.isEmpty(val1)) {
            scheduled = Integer.parseInt(val1) * 60 * 60 * 1000L;
        }
        if((Calendar.getInstance().getTimeInMillis() - lastExecuteTime) > scheduled){

    //        // 清表
    //        $info("产品信息清表开始");
    //        giltFeedDao.clearTemp();
    //        $info("产品信息清表结束");

            // 插入数据库
            $info("产品信息插入开始");
            superFeedImport(taskControlList);
            $info("产品信息插入完成");

            taskControlBean.setEnd_time(lastExecuteTime.toString());
            taskDao.updateTaskControl(taskControlBean);

    //        $info("transform开始");
    //        transformer.new Context(GILT, this).transform();
    //        $info("transform结束");

    //        insertService.new Context(GILT).postNewProduct();
        }else{
            $info("间隔时间未定不许要执行");
        }
    }


    private void onStartupInThread() throws Exception {
        //int delay = getDelaySecond();
        while(true) {
            giltFeedDao.clearTemp();

            $info("准备获取第 %s 页", pageIndex);

            List<GiltSku> skuList = getSkus(pageIndex);

            $info("取得 SKU: %s", skuList.size());

            if (skuList.isEmpty()){
                pageIndex = 0;
                lastExecuteTime = Calendar.getInstance().getTimeInMillis();
                break;
            }

            doFeedData(skuList);

            if (skuList.size() < getPageSize()) {
                pageIndex = 0;
                lastExecuteTime = Calendar.getInstance().getTimeInMillis();
                break;
            }

//            if (delay > 0) {
//                $info("阶段结束等待 %s 秒", delay);
//                Thread.sleep(delay * 1000);
//            }

            transformer.new Context(GILT, this).transform();
            insertService.new Context(GILT).postNewProduct();
            pageIndex++;
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
                    $info("----------" + msg + "----------");
                    throw new RuntimeException(e);
                }
                losePageCount ++;
                continue;
            }
        }

        return giltSkus;
    }

    /**
     * 产品文件读入
     */
    private void superFeedImport(List<TaskControlBean> taskControlList) throws Exception {
        List<Runnable> runnables = new ArrayList<>();

        runnables.add(() -> {
            try {
                onStartupInThread();
            } catch (Exception e) {
                e.printStackTrace();
                $error(e);
            }
        });

        runWithThreadPool(runnables, taskControlList);
    }
    private void doFeedData(List<GiltSku> skuList) throws Exception {

        prepareData(skuList);

    }

    private void prepareData(List<GiltSku> skuList) {

        List<SuperFeedGiltBean> feedGiltBeanList = new ArrayList<>();

        for (GiltSku giltSku : skuList){
            SuperFeedGiltBean superFeedGiltBean = toMySqlBean(giltSku);
            if(superFeedGiltBean != null){
                feedGiltBeanList.add(superFeedGiltBean);
            }

        }

        int count = giltFeedDao.insertListTemp(feedGiltBeanList);

        insertClientSku(feedGiltBeanList);

        $info("插入 TEMP SKU: %s", count);
    }

    private void insertClientSku(List<SuperFeedGiltBean> feedGiltBeanList){

        List<WmsBtClientSkuModel> clientSkuModels = new ArrayList<>();
        for(SuperFeedGiltBean feedGiltBean : feedGiltBeanList) {
            String[] codes = feedGiltBean.getProduct_codes().split(",");

            if (codes.length > 1) {
                // 如果 Code 有多个, 则需要将第一位和第二位的内容记录到 wms_bt_client_sku 表中
                WmsBtClientSkuModel clientSkuModel = new WmsBtClientSkuModel();
                clientSkuModel.setOrder_channel_id(ChannelConfigEnums.Channel.GILT.getId());
                clientSkuModel.setBarcode(codes[1]);
                clientSkuModel.setItem_code(feedGiltBean.getProduct_look_id());
                clientSkuModel.setColor(feedGiltBean.getAttributes_color_name());
                clientSkuModel.setSize(feedGiltBean.getAttributes_size_value());
                clientSkuModel.setUpc(codes[0]);
                clientSkuModel.setActive(true);
                String now = DateTimeUtil.getNow();
                clientSkuModel.setCreatedStr(now);
                clientSkuModel.setModifiedStr(now);
                clientSkuModel.setCreater(getTaskName());
                clientSkuModel.setModifier(getTaskName());
                clientSkuModels.add(clientSkuModel);
            }
        }
        if (clientSkuModels.size() > 0) {
            int count = giltFeedDao.insertIgnoreList(clientSkuModels);
            $info("\t插入 wms_bt_client_sku 表: %s", count);
        }
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

        if(categories == null || categories.size() == 0)
        {
            $info("categorie为空"+ JacksonUtil.bean2Json(giltSku));
            return null;
        }
        String catPath ="";
        for(GiltCategory categorie : categories){
            if(catPath.length() != 0) catPath+="-";
            catPath += categorie.getName().replaceAll("-","－");
        }
        GiltCategory giltCategory = categories.get(categories.size() - 1);

        superFeedGiltBean.setCategories_id(String.valueOf(giltCategory.getId()));
        superFeedGiltBean.setCategories_key(catPath);
        superFeedGiltBean.setCategories_name(giltCategory.getName());

        return superFeedGiltBean;
    }
}
