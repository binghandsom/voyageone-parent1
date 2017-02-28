package com.voyageone.task2.cms.service.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadFeedInfoRequest;
import com.voyageone.components.sneakerhead.bean.SneakerHeadSkuModel;
import com.voyageone.components.sneakerhead.service.SneakerheadApiService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.SuperFeedSneakerHeadBean;
import com.voyageone.task2.cms.dao.feed.SneakerHeadFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoSneakerHeadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SN;
import static com.voyageone.components.sneakerhead.SneakerHeadBase.DEFAULT_DOMAIN;

/**
 * Created by gjl on 2016/11/15.
 */
@Service
public class SneakerHeadAnalysisService extends BaseAnalysisService {
    @Autowired
    SneakerHeadFeedDao sneakerHeadFeedDao;
    @Autowired
    SneakerheadApiService sneakerheadApiService;

    private static Boolean isErr;
    private static Integer sumCnt;
    private static final int pageSize = 200;
    private Long lastExecuteTime = 0L;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                sneakerHeadFeedDao.delFullBySku(strings);
                sneakerHeadFeedDao.insertFullBySku(strings);
                sneakerHeadFeedDao.updateFlagBySku(strings);
            });
        }
    }

    @Override
    protected void zzWorkClear() {
        sneakerHeadFeedDao.delete();
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        init();
        zzWorkClear();
        int cnt;
        Long nowDate = Calendar.getInstance().getTimeInMillis();
        //取得上次Job上次执行的时间
        TaskControlBean taskControlBean =  TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.run_flg).get(0);
        if (!StringUtils.isEmpty(taskControlBean.getEnd_time())) {
            lastExecuteTime = Long.parseLong(taskControlBean.getEnd_time());
        }

        if ("1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))) {
            cnt = fullCopyTemp();
        } else {
            $info("产品信息插入开始");
            cnt = superFeedImport();
        }
        $info("产品信息插入完成 共" + cnt + "条数据");
        if (cnt > 0) {
            if (!"1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))) {
                transformer.new Context(channel, this).transform();
            }
            postNewProduct();
            //Job执行结束时间设置
            taskControlBean.setEnd_time(nowDate.toString());
            taskDao.updateTaskControl(taskControlBean);
        }
    }



    @Override
    protected int superFeedImport() {
        $info("SneakerHead产品api调用开始");
        isErr = false;
        sumCnt = 0;
        try {
            $info("SneakerHead取得full表里最新的时间");
            final Date getFeedDate = lastExecuteTime == null ? new Date(0) : new Date(lastExecuteTime);
            $info("最新的时间" + getFeedDate.getTime());
            //取得sneakerHead的Feed的总数
            int anInt = sneakerheadApiService.getFeedCount(getFeedDate, DEFAULT_DOMAIN);
            int pageCnt = anInt / pageSize + (anInt % pageSize > 0 ? 1 : 0);
            $info("共" + pageCnt + "页");
            //根据feed取得总数取得对应的SKU并进行解析
            if (anInt > 0) {
                ExecutorService es = Executors.newFixedThreadPool(5);
                for (int i = 1; i <= pageCnt; i++) {
                    int finalI = i;
                    es.execute(() ->
                            getSku(finalI, getFeedDate)
                    );
                }
                es.shutdown();
                es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
                if (isErr) {
                    sumCnt = 0;
                    throw new BusinessException("调用api时间超时");
                }
            }
            $info("SneakerHead产品文件读入完成");
        } catch (Exception e) {
            $info("SneakerHead产品文件读入失败" + e.getMessage());
        }
        $info("SneakerHead产品的sku个数:" + sumCnt);
        return sumCnt;
    }


    public void getSku(int pageNum, Date lastDate) {
        long threadNo = Thread.currentThread().getId();
        synchronized (isErr) {
            if (isErr) return;
        }
        List<SuperFeedSneakerHeadBean> superFeed = new ArrayList<>();
        $info(String.format("thread-" + threadNo + " 正在取第%d页", pageNum));
        SneakerHeadFeedInfoRequest sneakerHeadRequest = new SneakerHeadFeedInfoRequest();
        sneakerHeadRequest.setPageNumber(pageNum);
        sneakerHeadRequest.setPageSize(pageSize);
        sneakerHeadRequest.setTime(lastDate);
        List<SneakerHeadCodeModel> feedList = null;
        int tried = 0;
        do {
            try {
                feedList = sneakerheadApiService.getFeedInfo(sneakerHeadRequest, DEFAULT_DOMAIN);
            } catch (Exception e) {
                tried++;
                e.printStackTrace();
            }
        } while (tried < 3 && null == feedList);

        if (null == feedList) {
            synchronized (isErr) {
                isErr = true;
                throw new BusinessException("pageNum:" + pageNum + " 调用api时间超时");
            }
        }

        if (feedList.size() > 0) {
            for (SneakerHeadCodeModel model : feedList) {
                List<SneakerHeadSkuModel> skuList = model.getSkus();
                for (SneakerHeadSkuModel skuModel : skuList) {
                    SuperFeedSneakerHeadBean superFeedSneakerHeadBean = new SuperFeedSneakerHeadBean();
                    superFeedSneakerHeadBean.setSku(skuModel.getSku());
                    superFeedSneakerHeadBean.setCode(model.getCode());
                    superFeedSneakerHeadBean.setRelationshiptype(model.getRelationshipType());
                    superFeedSneakerHeadBean.setVariationtheme(model.getVariationTheme());
                    superFeedSneakerHeadBean.setTitle(model.getTitle());
                    superFeedSneakerHeadBean.setBarcode(skuModel.getBarcode());
                    superFeedSneakerHeadBean.setPrice(model.getPrice());
                    superFeedSneakerHeadBean.setMsrp(model.getMsrp());
                    superFeedSneakerHeadBean.setCnMsrp(model.getCnMsrp());
                    superFeedSneakerHeadBean.setCnRetailPrice(model.getCnRetailPrice());
                    superFeedSneakerHeadBean.setQuantity(skuModel.getQuantity());
                    superFeedSneakerHeadBean.setWeight(model.getWeight());
                    superFeedSneakerHeadBean.setImages(model.getImages());
                    superFeedSneakerHeadBean.setDescription(model.getDescription());
                    superFeedSneakerHeadBean.setShortdescription(model.getShortDescription());
                    superFeedSneakerHeadBean.setProductorigin(model.getProductOrigin());
                    superFeedSneakerHeadBean.setCategory(model.getCategory());
                    superFeedSneakerHeadBean.setBrand(model.getBrand());
                    superFeedSneakerHeadBean.setMaterials(model.getMaterials());
                    superFeedSneakerHeadBean.setVendorproducturl(model.getVendorProductUrl());
                    superFeedSneakerHeadBean.setSize(skuModel.getSize());
                    superFeedSneakerHeadBean.setModel(model.getModel());
                    superFeedSneakerHeadBean.setProducttype(model.getProductType());
                    superFeedSneakerHeadBean.setSizetype(model.getSizeType());
                    superFeedSneakerHeadBean.setColor(model.getColor());
                    superFeedSneakerHeadBean.setBoximages(model.getBoxImages());
                    superFeedSneakerHeadBean.setColormap(model.getColorMap());
                    superFeedSneakerHeadBean.setAbstractdescription(model.getAbstractDescription());
                    superFeedSneakerHeadBean.setAccessory(model.getAccessory());
                    superFeedSneakerHeadBean.setUnisex(model.getUnisex());
                    superFeedSneakerHeadBean.setSizeoffset(model.getSizeOffset());
                    superFeedSneakerHeadBean.setBlogurl(model.getBlogUrl());
                    superFeedSneakerHeadBean.setIsrewardeligible(model.getIsRewardEligible());
                    superFeedSneakerHeadBean.setIsdiscounteligible(model.getIsDiscountEligible());
                    superFeedSneakerHeadBean.setOrderlimitcount(model.getOrderLimitCount());
                    superFeedSneakerHeadBean.setApproveddescriptions(model.getApprovedDescriptions());
                    superFeedSneakerHeadBean.setUrlkey(model.getUrlKey());
                    superFeedSneakerHeadBean.setCreated(skuModel.getCreated());
                    superFeedSneakerHeadBean.setLastReceivedOn(model.getLastReceivedOn());
                    superFeed.add(superFeedSneakerHeadBean);
                }
            }
            transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
            synchronized (sumCnt) {
                sumCnt += superFeed.size();
            }
            superFeed.clear();
        }
    }

    /**
     * SneakerHead产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedSneakerHeadBean> superFeedList) {

        for (SuperFeedSneakerHeadBean superFeed : superFeedList) {

            if (sneakerHeadFeedDao.insertSelective(superFeed) <= 0) {
                $info("SneakerHead产品信息插入失败sku = " + superFeed.getSku());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return SN;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categoryPath) {
        Map column = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = feedBeans.stream()
                .filter(feedConfig -> !StringUtil.isEmpty(feedConfig.getCfg_val1()))
                .map(FeedBean::getCfg_val1).collect(Collectors.toList());

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, column.get("category").toString(),
                categoryPath.replace("'", "\\\'"));

        column.put("keyword", where);
        column.put("tableName", table);
        if (attList.size() > 0) {
            column.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoSneakerHeadModel> sneakerHeadModelBeans = sneakerHeadFeedDao.selectSuperFeedModel(column);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoSneakerHeadModel modelBean : sneakerHeadModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(modelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;
                List<String> values = new ArrayList<>();

                if (key.equals("boximages")) {
                    attribute.put(key, Arrays.asList(((String) temp.get(key)).split(",")));
                } else {
                    values.add((String) temp.get(key));
                    attribute.put(key, values);
                }

            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = modelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //设置重量开始
            List<CmsBtFeedInfoModel_Sku> skus = modelBean.getSkus();
            for (CmsBtFeedInfoModel_Sku sku : skus) {
                String Weight = sku.getWeightOrg().trim();
                Pattern pattern = Pattern.compile("[^0-9.]");
                Matcher matcher = pattern.matcher(Weight);
                if (matcher.find()) {
                    int index = Weight.indexOf(matcher.group());
                    if (index != -1) {
                        String weightOrg = Weight.substring(0, index);
                        sku.setWeightOrg(weightOrg);
                    }
                }
                sku.setWeightOrgUnit(sku.getWeightOrgUnit());
            }
            cmsBtFeedInfoModel.setSkus(skus);
            //设置重量结束
            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setImage(beforeFeed.getImage().stream().distinct().collect(Collectors.toList()));
                beforeFeed.setAttribute(attributeMerge(beforeFeed.getAttribute(), cmsBtFeedInfoModel.getAttribute()));
            } else {
                modelBeans.add(cmsBtFeedInfoModel);
                codeMap.put(cmsBtFeedInfoModel.getCode(), cmsBtFeedInfoModel);
            }

        }
        $info("取得 [ %s ] 的 Product 数 %s", categoryPath, modelBeans.size());

        return modelBeans;
    }

    @Override
    protected String getTaskName() {
        return "CmsSneakerHeadAnalysisJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
