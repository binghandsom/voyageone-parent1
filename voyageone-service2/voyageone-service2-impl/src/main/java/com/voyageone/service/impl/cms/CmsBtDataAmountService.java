package com.voyageone.service.impl.cms;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtDataAmount.*;
import com.voyageone.service.dao.cms.CmsBtDataAmountDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtDataAmountDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CmsBtDataAmountService extends BaseService {
    @Autowired
    CmsBtFeedInfoDao daoCmsBtFeedInfo;
    @Autowired
    CmsBtProductDao daoCmsBtProduct;
    @Autowired
    CmsBtDataAmountDao dao;
    @Autowired
    CmsBtDataAmountDaoExt daoExt;
    @Autowired
    CmsBtBrandBlockService cmsBtBrandBlockService;

    //String feedBrand, int masterBrandId, String platformBrand
    public void sumByChannelId(String channelId) {
        //1.汇总FEED信息
        sumCmsBtFeedInfo(channelId);
        //2.主数据编辑信息
        sumMaster(channelId);
        List<TypeChannelBean> listCarts = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, "cn");
        for (TypeChannelBean chanelBean : listCarts) {
            if (!StringUtils.isEmpty(chanelBean.getValue())) {
                int cartId = Integer.parseInt(chanelBean.getValue());
                if (cartId == 1 || cartId == 0) {
                    continue;
                }
                //3.汇总平台价格信息
                sumPrice(channelId, cartId);
                //4.平台信息
                sumPlatInfo(channelId, cartId);
                sumBrandNoMatch(channelId, cartId);

            }
        }
    }

    //1.FEED信息
    private void sumCmsBtFeedInfo(String channelId) {
        //CmsBtFeedInfo表统计项
        List<EnumFeedSum> list = EnumFeedSum.getList(0);
        CmsBtDataAmountModel model = null;
        for (EnumFeedSum enumFeed : list) {
            long count = daoCmsBtFeedInfo.countByQuery(enumFeed.getStrQuery(), channelId);
            saveCmsBtDataAmount(channelId, 0, enumFeed, count);
        }
        //feed品牌黑名单统计
        int count = cmsBtBrandBlockService.getBrandCount(channelId, null, 0);
        saveCmsBtDataAmount(channelId, 0, EnumFeedSum.CMS_FEED_feedBrand_block, count);
    }

    // 2.主数据编辑信息
    private void sumMaster(String channelId) {
        List<EnumMasterSum> list = EnumMasterSum.getList(0);
        CmsBtDataAmountModel model = null;
        for (EnumMasterSum enumFeed : list) {
            if(enumFeed == EnumMasterSum.CMS_MASTER_NO_SALE && channelId != "001"){
                saveCmsBtDataAmount(channelId, 0, enumFeed, 0);
            }else{
                long count = daoCmsBtProduct.countByQuery(enumFeed.getStrQuery(), channelId);
                saveCmsBtDataAmount(channelId, 0, enumFeed, count);
            }
        }
        //master品牌黑名单统计
        int count = cmsBtBrandBlockService.getBrandCount(channelId, null, 1);
        saveCmsBtDataAmount(channelId, 0, EnumMasterSum.CMS_MASTER_Brand_block, count);
    }

    // 3.价格信息
    private void sumPrice(String channelId, int cartId) {
        List<EnumPlatformPriceSum> list = EnumPlatformPriceSum.getList();
        CmsBtDataAmountModel model = null;
        for (EnumPlatformPriceSum enumFeed : list) {
            String strQuery = "";
            if (enumFeed.getFunFormat() != null) {
                QueryStrFormatParam param = new QueryStrFormatParam();
                param.setCartId(cartId);
                param.setQueryStr(enumFeed.getStrQuery());
                strQuery = enumFeed.getFunFormat().apply(param);
            } else {
                strQuery = String.format(enumFeed.getStrQuery(), cartId);
            }
            $info(String.format("channel=%s name = %s start",channelId, enumFeed.getAmountName()));
            long count = daoCmsBtProduct.countByQuery(strQuery, channelId);
            $info(String.format("channel=%s name = %s count = %d end",channelId, enumFeed.getAmountName(), count));
            saveCmsBtDataAmount(channelId, cartId, enumFeed, count);
        }
    }

    // 4.各平台信息
    private void sumPlatInfo(String channelId, int cartId) {
        List<EnumPlatformInfoSum> list = EnumPlatformInfoSum.getList(0);
        CmsBtDataAmountModel model = null;
        for (EnumPlatformInfoSum enumFeed : list) {
            String strQuery = "";
            if (enumFeed.getFunFormat() != null) {
                QueryStrFormatParam param = new QueryStrFormatParam();
                param.setCartId(cartId);
                param.setQueryStr(enumFeed.getStrQuery());
                strQuery = enumFeed.getFunFormat().apply(param);
            } else {
                strQuery = String.format(enumFeed.getStrQuery(), cartId);
            }
            long count = daoCmsBtProduct.countByQuery(strQuery, channelId);
            saveCmsBtDataAmount(channelId, cartId, enumFeed, count);
        }
        //master platform品牌黑名单统计
        int count = cmsBtBrandBlockService.getBrandCount(channelId, String.valueOf(cartId), 2);
        saveCmsBtDataAmount(channelId, cartId, EnumPlatformInfoSum.CMS_PLATFORM_BRAND_BLOCK, count);
    }

    //获取品牌未匹配数量
    private void sumBrandNoMatch(String channelId, int cartId) {
        int count = daoExt.selectBrandNoMatchCount(channelId, cartId);
        saveCmsBtDataAmount(channelId, cartId, EnumPlatformInfoSum_BrandNoMatch.CMS_BRAND_NO_MATCH_COUNT, count);
    }

    //保存
    private void saveCmsBtDataAmount(String channelId, int cartId, IEnumDataAmountSum enumFeed, long count) {
        CmsBtDataAmountModel model;
        model = get(channelId, cartId, enumFeed.getAmountName());
        if (model == null) {
            model = new CmsBtDataAmountModel();
            model.setId(0);
            model.setCreated(new Date());
            model.setCreater("system");
            model.setAmountName(enumFeed.getAmountName());
            model.setChannelId(channelId);
            model.setCartId(cartId);
            model.setComment(enumFeed.getComment());
            model.setLinkParameter(enumFeed.getLinkParameter());
            model.setLinkUrl(enumFeed.getLinkUrl());
            model.setDataAmountTypeId(enumFeed.getDataAmountTypeId());
        }
        model.setModified(new Date());
        model.setAmountVal(Long.toString(count));
        if (model.getId() == 0) {
            dao.insert(model);
        } else {
            dao.update(model);
        }
    }

    private CmsBtDataAmountModel get(String channelId, int cartId, String amountName) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("amountName", amountName);
        map.put("cartId", cartId);
        return dao.selectOne(map);
    }

    //获取Home汇总信息数据源
    public Object getHomeSumData(String channelId, String Lang) {
        List<TypeChannelBean> listCarts = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, Lang);

        Map<String, Object> result = new HashMap<>();
        List<Integer> listDataAmountTypeId = new ArrayList<>();
        listDataAmountTypeId.add(EnumDataAmountType.FeedSum.getId());
        listDataAmountTypeId.add(EnumDataAmountType.MasterSum.getId());
        listDataAmountTypeId.add(EnumDataAmountType.PlatformInfoSum.getId());
        listDataAmountTypeId.add(EnumDataAmountType.PlatformPriceSum.getId());
        List<CmsBtDataAmountModel> list = daoExt.selectByListDataAmountTypeId(channelId, listDataAmountTypeId);

        result.put("feedSum", getSumModel(list, EnumDataAmountType.FeedSum));
        result.put("masterSum", getSumModel(list, EnumDataAmountType.MasterSum));
        result.put("listPlatformInfoSum", getPlatformSumModel(list, listCarts, EnumDataAmountType.PlatformInfoSum));
        result.put("listPlatformPriceSum", getPlatformSumModel(list, listCarts, EnumDataAmountType.PlatformPriceSum));

        return result;
    }

    private Map<String, Object> getSumModel(List<CmsBtDataAmountModel> list, EnumDataAmountType enumDataAmountType) {
        //1获取该分类所有数据
        List<CmsBtDataAmountModel> listAmount = getByDataAmountTypeId(list, enumDataAmountType);

        //2.行转列
        Map<String, Object> map = new HashMap<>();
//       listAmount.stream().forEach((m) -> {
//           map.put(m.getAmountName(), m);
//       });
        for (CmsBtDataAmountModel m : listAmount) {
            map.put(m.getAmountName(), m);
        }
        return map;
    }

    private List<Map<String, Object>> getPlatformSumModel(List<CmsBtDataAmountModel> list, List<TypeChannelBean> listCarts, EnumDataAmountType enumDataAmountType) {
        List<Map<String, Object>> listResult = new ArrayList();
        //1获取该分类所有数据
        List<CmsBtDataAmountModel> listAmount = getByDataAmountTypeId(list, enumDataAmountType);
        for (TypeChannelBean chanelBean : listCarts) {
            if (!StringUtils.isEmpty(chanelBean.getValue())) {
                int cartId = Integer.parseInt(chanelBean.getValue());
                if (cartId == 1 || cartId == 0) {
                    continue;
                }
                //2获取该分类下 单个平台的数据
                List<CmsBtDataAmountModel> cardlistAmount = getBycartId(listAmount, cartId);
                //3行转列   列名字是AmountName
                Map<String, Object> model = new HashMap<>();
//                cardlistAmount.stream().forEach((m) -> {
//                    model.put(m.getAmountName(), m);
//                });
                for (CmsBtDataAmountModel m : cardlistAmount) {
                    model.put(m.getAmountName(), m);
                }
                model.put("platformName", chanelBean.getName());
                model.put("add_name2", chanelBean.getAdd_name2());
                listResult.add(model);
            }
        }
        return listResult;
    }

    private List<CmsBtDataAmountModel> getByDataAmountTypeId(List<CmsBtDataAmountModel> list, EnumDataAmountType enumType) {
        List<CmsBtDataAmountModel> newList = new ArrayList<>();
        for (CmsBtDataAmountModel m : list) {
            if (m.getDataAmountTypeId() == enumType.getId()) {
                newList.add(m);
            }
        }
//        List<CmsBtDataAmountModel> newList= list.stream().filter((m) -> {
//            return m.getDataAmountTypeId() == enumType.getId();
//        }).collect(Collectors.toList());
        return newList;
    }

    private List<CmsBtDataAmountModel> getBycartId(List<CmsBtDataAmountModel> list, int cartId) {
        List<CmsBtDataAmountModel> newList = new ArrayList<>();
//        list.stream().filter((m) -> {
//            return m.getCartId() == cartId;
//        }).forEach(newList::add);
        for (CmsBtDataAmountModel m : list) {
            if (m.getCartId() == cartId) {
                newList.add(m);
            }
        }
        return newList;
    }
}
