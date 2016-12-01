package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.onestop.bean.OneStopProduct;
import com.voyageone.components.onestop.service.OneStopClient;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedFryeBean;
import com.voyageone.task2.cms.dao.feed.FryeFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoFryeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.Frye;

/**
 * Created by gjl on 2016/7/5.
 */
@Service
public class FryeAnalysisService extends BaseAnalysisService {

    @Autowired
    FryeFeedDao fryeFeedDao;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                fryeFeedDao.delFullBySku(strings);
                fryeFeedDao.insertFullBySku(strings);
                fryeFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        fryeFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("Frye产品文件读入开始");
        List<SuperFeedFryeBean> superFeed = new ArrayList<>();
        int cnt = 0;
        Date getFeedDate = new Date(0);
        //取得产品productId
//        List<Long> productIdList = oneStopClient.catalogProductId(getFeedDate);
        List<Long> productIdList = new ArrayList<>();
        productIdList.add((long) 17195);
        if (productIdList.size() > 0) {
            for (Long productId : productIdList) {
                SuperFeedFryeBean fryeBean = new SuperFeedFryeBean();
                //根据产品productId取得对应的产品信息
                OneStopClient oneStopClient = new OneStopClient(null);
                OneStopProduct oneStopProduct = oneStopClient.catalog(productId);

                fryeBean.setProductId(String.valueOf(oneStopProduct.getProductId()));
                fryeBean.setIsactive(String.valueOf(oneStopProduct.isIsActive()));
                //Attributes处理开始
                List<OneStopProduct.AttributesBean> Attributes = oneStopProduct.getAttributes();
                StringBuilder sb = new StringBuilder();
                if(Attributes!=null){
                    Attributes.stream().filter(Attribute -> !StringUtil.isEmpty(Attribute.getName())).forEach(Attribute -> {
                        sb.append(Attribute.getName() + ":" + Attribute.getValues());
                        if ("Fabric".equals(Attribute.getName())) {
                            fryeBean.setFabric(String.valueOf(Attribute.getValues()));
                        }
                    });
                }
                fryeBean.setAttributes(sb.toString());
                //Attributes处理结束
                //Variants处理开始
                List<OneStopProduct.VariantsBean> variantsBean= oneStopProduct.getVariants();
                for(OneStopProduct.VariantsBean variant:variantsBean){
                    fryeBean.setVariantsUpc(variant.getUPC());
                    fryeBean.setVariantsName(variant.getName());
                    List<OneStopProduct.VariantsBean.MediasBean> mediasBean= variant.getMedias();
                    for(OneStopProduct.VariantsBean.MediasBean medias:mediasBean){

                        OneStopProduct.VariantsBean.MediasBean.MetaBean metaBean =medias.getMeta();
//                        metaBean.getClass().get
                    }


                    List<OneStopProduct.VariantsBean.OptionsBean> optionsBean= variant.getOptions();
                    for(OneStopProduct.VariantsBean.OptionsBean option:optionsBean){

                    }
                }
                //Variants处理结束


//
//                fryeBean.setVariantsMediasMetaExtraimagethumb();
//                fryeBean.setVariantsMediasMetaExtraimagethumburl();
//                fryeBean.setVariantsMediasMetaExtraimagelargethumb();
//                fryeBean.setVariantsMediasMetaExtraimagelargethumburl();
//                fryeBean.setVariantsOptionsColor();
//                fryeBean.setVariantsOptionsSize();
//                fryeBean.setIstaxable();
//                fryeBean.setMfgsku();
//                fryeBean.setTaxclassification();
//                fryeBean.setName();
//                fryeBean.setFirstlivedate();
//                fryeBean.setMetatags();
//                fryeBean.setOptions();
//                fryeBean.setCategoryid();
//                fryeBean.setMetadataCategoryextratext1();
//                fryeBean.setMetadataCategoryextratext2();
//                fryeBean.setMetadataCategoryextratext3();
//                fryeBean.setMetadataExtratext1();
//                fryeBean.setMetadataDescription2();
//                fryeBean.setMetadataDescription3();
//                fryeBean.setMetadataDescription4();
//                fryeBean.setMetadataDescription2withformatting();
//                fryeBean.setMetadataDescription3withformatting();
//                fryeBean.setMetadataDescription4withformatting();
//                fryeBean.setMetadataCategoryname();
//                fryeBean.setMetadataDetail();
//                fryeBean.setMetadataUnitcost();
//                fryeBean.setMetadataPreviouscost();
//                fryeBean.setMetadataMaxcost();
//                fryeBean.setMetadataMincost();
//                fryeBean.setMetadataMinregularprice();
//                fryeBean.setMetadataMaxregularprice();
//                fryeBean.setMetadataMinsaleprice();
//                fryeBean.setMetadataMaxsaleprice();
//                fryeBean.setMetadataSamemaxmin();
//                fryeBean.setMetadataShippingsurcharge();
//                fryeBean.setMetadataSizecharturl();
//                fryeBean.setMetadataWeight();
//                fryeBean.setMetadataPreorderavailabledate();
//                fryeBean.setMetadataDisplayoospopup();
//                fryeBean.setMetadataShowwhenoos();
//                fryeBean.setMetadataNonremovablefromcart();
//                fryeBean.setMetadataProducturl();

                superFeed.add(fryeBean);
                cnt++;
                if (superFeed.size() > 1000) {
                    transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                    superFeed.clear();
                }
            }
            if (superFeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                superFeed.clear();
            }
            $info("Frye产品文件读入完成");
        }
        return 0;
    }

    /**
     * Frye产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedFryeBean> superFeedList) {

        superFeedList.stream()
                .filter(superFeed -> fryeFeedDao.insertSelective(superFeed) <= 0)
                .forEach(superFeed -> {
                    $info("Frye产品信息插入失败 Sku = " + superFeed.getVariantsUpc());
                });
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return Frye;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map column = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = feedBeans.stream()
                .filter(feedConfig -> !StringUtil.isEmpty(feedConfig.getCfg_val1()))
                .map(FeedBean::getCfg_val1)
                .collect(Collectors.toList());

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, column.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        column.put("keyword", where);
        column.put("tableName", table);
        if (attList.size() > 0) {
            column.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

//        List<CmsBtFeedInfoFryeModel> vtmModelBeans = fryeFeedDao.selectSuperFeedModel(column);
        List<CmsBtFeedInfoFryeModel> vtmModelBeans =null;
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoFryeModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //设置重量开始
            List<CmsBtFeedInfoModel_Sku> skus = vtmModelBean.getSkus();
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
        return "CmsFryeAnalysisJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
