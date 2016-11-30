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
import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadRequest;
import com.voyageone.components.sneakerhead.bean.SneakerHeadSkuModel;
import com.voyageone.components.sneakerhead.service.SneakerHeadFeedService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SN;

/**
 * Created by gjl on 2016/11/15.
 */
@Service
public class SneakerHeadAnalysisService extends BaseAnalysisService {
    @Autowired
    SneakerHeadFeedDao sneakerHeadFeedDao;
    @Autowired
    SneakerHeadFeedService sneakerHeadFeedService;

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
        int cnt = 0;
        if("1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))){
            cnt = fullCopyTemp();
        }else {
            $info("产品信息插入开始");
            cnt = superFeedImport();
        }
        $info("产品信息插入完成 共" + cnt + "条数据");
        if (cnt > 0) {
            if(!"1".equalsIgnoreCase(TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.feed_full_copy_temp))) {
                transformer.new Context(channel, this).transform();
            }
            postNewProduct();
        }
    }
    @Override
    protected int superFeedImport() {
        $info("SneakerHead产品api调用开始");
        List<SuperFeedSneakerHeadBean> superFeed = new ArrayList<>();
        int cnt = 0;
        try {
            $info("SneakerHead取得full表里最新的时间");
            Date getFeedDate = sneakerHeadFeedDao.selectSuperFeedModelDate();
            if (getFeedDate == null) {
                getFeedDate = new Date(0);
            }
            //取得sneakerHead的Feed的总数
            int anInt = sneakerHeadFeedService.sneakerHeadFeedCount(getFeedDate);
            //根据feed取得总数取得对应的SKU并进行解析
            if (anInt > 0) {
                for (int i = 1; i <= (anInt / 100 + 1); i++) {
                    SneakerHeadRequest sneakerHeadRequest = new SneakerHeadRequest();
                    sneakerHeadRequest.setPageNumber(i);
                    sneakerHeadRequest.setPageSize(100);
                    sneakerHeadRequest.setTime(getFeedDate);
                    List<SneakerHeadCodeModel> feedList = null;
                    int tried = 0;
                    do {
                        try {
                            feedList = sneakerHeadFeedService.sneakerHeadResponse(sneakerHeadRequest);
                        } catch (Exception e) {
                            tried++;
                            e.printStackTrace();
                        }
                    } while (tried < 3 && null == feedList);

                    if (null == feedList) throw new BusinessException("调用api时间超时");

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
                                superFeed.add(superFeedSneakerHeadBean);
                                cnt++;
                                if (superFeed.size() > 1000) {
                                    transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                                    superFeed.clear();
                                }
                            }
                        }

                    }
                }
            }
            if (superFeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                superFeed.clear();
            }
            $info("SneakerHead产品文件读入完成");
        } catch (Exception e) {
            $info("SneakerHead产品文件读入失败" + e.getMessage());
        }
        $info("SneakerHead产品的sku个数:" + cnt);
        return cnt;
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
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map colums = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = new ArrayList<>();
        for (FeedBean feedConfig : feedBeans) {
            if (!StringUtil.isEmpty(feedConfig.getCfg_val1())) {
                attList.add(feedConfig.getCfg_val1());
            }
        }

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, colums.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        colums.put("keyword", where);
        colums.put("tableName", table);
        if (attList.size() > 0) {
            colums.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoSneakerHeadModel> sneakerHeadModelBeans = sneakerHeadFeedDao.selectSuperFeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoSneakerHeadModel modelBean : sneakerHeadModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(modelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;
                List<String> values = new ArrayList<>();

                if(key.equals("boximages")){
                    attribute.put(key, Arrays.asList(((String) temp.get(key)).split(",")));
                }else {
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
        $info("取得 [ %s ] 的 Product 数 %s", categorPath, modelBeans.size());

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