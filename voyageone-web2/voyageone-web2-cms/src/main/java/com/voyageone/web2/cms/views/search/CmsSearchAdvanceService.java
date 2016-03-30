package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtFeedCustomPropDao;
import com.voyageone.service.dao.cms.CmsMtCommonPropDao;
import com.voyageone.service.dao.cms.CmsMtCustomWordDao;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
import com.voyageone.web2.cms.views.channel.CmsFeedCustPropService;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsSearchAdvanceService extends BaseAppService{

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;
    @Autowired
    private CmsMtCustomWordDao customWordDao;
    @Autowired
    private ChannelCategoryService channelCategoryService;
    @Autowired
    private CmsMtCommonPropDao cmsMtCommonPropDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;
    @Autowired
    private CmsBtFeedCustomPropDao cmsBtFeedCustomPropDao;

    // 查询产品信息时的缺省输出列
    private final String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;" +
            "modifier;groups.msrpStart;groups.msrpEnd;groups.retailPriceStart;groups.retailPriceEnd;" +
            "groups.salePriceStart;groups.salePriceEnd;groups.platforms.$;skus;" +
            "fields.longTitle;fields.productNameEn;fields.brand;fields.status;fields.code;fields.images1;fields.quantity;fields.productType;fields.sizeType;" +
            "fields.priceSaleSt;fields.priceSaleEd;fields.priceRetailSt;fields.priceRetailEd;fields.priceMsrpSt;fields.priceMsrpEd;";

    // DB检索页大小
    private int SELECT_PAGE_SIZE = 2000;

    // Excel 文件最大行数
    private int MAX_EXCEL_REC_COUNT = 10000;

    // Sku 文件单线程用
    ReentrantLock lock = new ReentrantLock();

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, CmsSessionBean cmsSession, String language) throws IOException{

        Map<String, Object> masterData = new HashMap<>();

        // 获取product status
        masterData.put("productStatusList", TypeConfigEnums.MastType.productStatus.getList(language));

        // 获取publish status
        masterData.put("platformStatusList", TypeConfigEnums.MastType.platFormStatus.getList(language));

        // 获取label
        masterData.put("tagList", selectTagList(userInfo.getSelChannelId()));

        // 获取price type
        masterData.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));

        // 获取compare type
        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));

        // 获取brand list
        masterData.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, userInfo.getSelChannelId(), language));

        // 获取sort list
        masterData.put("sortList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SORT_ATTRIBUTES_61, userInfo.getSelChannelId(), language));

        // 获取category list
        masterData.put("categoryList", channelCategoryService.getAllCategoriesByChannelId(userInfo.getSelChannelId()));

        // 获取promotion list
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", userInfo.getSelChannelId());
        masterData.put("promotionList", cmsPromotionService.queryByCondition(params));

        // 获取自定义查询用的属性
        masterData.put("custAttsList", cmsSession.getAttribute("_adv_search_props_custAttsQueryList"));

        return masterData;
    }

    /**
     * 返回当前页的group列表
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public List<CmsBtProductModel> getGroupList(List<CmsBtProductModel> productList, CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(searchValue, cmsSessionBean, true));
        queryObject.setProjection(searchItems.concat((String) cmsSessionBean.getAttribute("_adv_search_props_searchItems")).split(";"));
        queryObject.setSort(setSortValue(searchValue));

        List<CmsBtProductModel> grpList =  productService.getList(userInfo.getSelChannelId(), queryObject);

        if (productList.size() > 0) {
            // 再找出其主商品
            List<Long> grpIdList = new ArrayList<Long>();
            for (CmsBtProductModel prodObj : productList) {
                CmsBtProductModel_Group gpList = prodObj.getGroups();
                long grpId = 0;
                if (gpList != null) {
                    List<CmsBtProductModel_Group_Platform> pltList = gpList.getPlatforms();
                    if (pltList != null && pltList.size() > 0) {
                        for (CmsBtProductModel_Group_Platform pltObj : pltList) {
                            if (pltObj.getCartId() == (int) cmsSessionBean.getPlatformType().get("cartId")) {
                                grpIdList.add(pltObj.getGroupId());
                                break;
                            }
                        }
                    }
                }
            }
            if (grpIdList.size() > 0) {
                StringBuilder inStr = new StringBuilder("{'$in':[");
                for (int i = 0, leng = grpIdList.size(); i < leng; i ++) {
                    if (i == 0) {
                        inStr.append(grpIdList.get(i));
                    } else {
                        inStr.append(",");
                        inStr.append(grpIdList.get(i));
                    }
                }
                inStr.append("]}");

                JomgoQuery queryObj = new JomgoQuery();
                queryObj.setQuery("{'groups.platforms':{'$elemMatch':{'isMain':1,'cartId':" + (int) cmsSessionBean.getPlatformType().get("cartId") + ",'groupId':" + inStr.toString() + "}}}");

                List<CmsBtProductModel> grpList2 = productService.getList(userInfo.getSelChannelId(), queryObj);
                for (CmsBtProductModel prodModel : grpList2) {
                    long prodId = prodModel.getProdId();
                    boolean hasGrp = false;
                    for (CmsBtProductModel grpObj : grpList) {
                        if (grpObj.getProdId() == prodId) {
                            // 已经存在该主商品，则不追加
                            hasGrp = true;
                            break;
                        }
                    }
                    if (!hasGrp) {
                        grpList.add(prodModel);
                    }
                }
            }
        }
        return grpList;
    }

    /**
     * 取得当前主商品所在组的所有商品的图片
     * @param groupsList
     * @return
     */
    public List<List<Map<String, String>>> getGroupImageList(List<CmsBtProductModel> groupsList, String channelId, int cartId) {
        JomgoQuery queryObj = new JomgoQuery();
        queryObj.setProjection("{'fields.images1':1,'_id':0}");

        List<List<Map<String, String>>> rslt = new ArrayList<List<Map<String, String>>>();
        for (CmsBtProductModel groupObj : groupsList) {
            long grpId = 0;
            List<CmsBtProductModel_Group_Platform> ptmList = groupObj.getGroups().getPlatforms();
            if (ptmList != null) {
                for (CmsBtProductModel_Group_Platform ptmObj : ptmList) {
                    if (ptmObj.getCartId() == cartId && ptmObj.getIsMain()) {
                        grpId = ptmObj.getGroupId();
                        break;
                    }
                }
            }
            if (grpId == 0) {
                // 当前主商品所在组没有其他商品
                logger.info("当前主商品所在组没有其他商品 prodId=" + groupObj.getProdId());
                rslt.add(new ArrayList<Map<String, String>>(0));
                continue;
            }

            queryObj.setQuery("{'groups.platforms':{'$elemMatch':{'isMain':0,'cartId':" + cartId + ",'groupId':" + grpId + "}}}");
            List<CmsBtProductModel> imgList =  productService.getList(channelId, queryObj);
            if (imgList == null || imgList.isEmpty()) {
                logger.info("当前主商品所在组没有其他商品的图片 groupId=" + grpId);
                rslt.add(new ArrayList<Map<String, String>>(0));
                continue;
            }

            List<Map<String, String>> images1Arr = new ArrayList<Map<String, String>>();
            for (CmsBtProductModel imgObj : imgList) {
                CmsBtProductModel_Field fields = imgObj.getFields();
                if (fields != null) {
                    List<CmsBtProductModel_Field_Image> imgaes = fields.getImages1();
                    if (imgaes != null && imgaes.size() > 0) {
                        Map<String, String> map = new HashMap<String, String>(1);
                        map.put("value", imgaes.get(0).getName());
                        images1Arr.add(map);
                    }
                }
            }
            rslt.add(images1Arr);
        }
        return rslt;
    }

    /**
     * 取得当前主商品所在组的其他信息：所有商品的价格变动信息，子商品图片
     * @param groupsList
     * @return
     */
    public List[] getGroupExtraInfo(List<CmsBtProductModel> groupsList, String channelId, int cartId, boolean hasImgFlg) {
        List[] rslt = null;
        List<List<Map<String, String>>> imgList = new ArrayList<List<Map<String, String>>>();
        List<Map<String, Integer>> chgFlgList = new ArrayList<Map<String, Integer>>();

        JomgoQuery queryObj = new JomgoQuery();
        if (hasImgFlg) {
            queryObj.setProjection("{'skus.priceChgFlg':1,'fields.images1':1,'_id':0}");
            rslt = new List[2];
            rslt[0] = chgFlgList;
            rslt[1] = imgList;
        } else {
            queryObj.setProjection("{'fields.images1':1,'_id':0}");
            rslt = new List[1];
            rslt[0] = chgFlgList;
        }

        for (CmsBtProductModel groupObj : groupsList) {
            long grpId = 0;
            List<CmsBtProductModel_Group_Platform> ptmList = groupObj.getGroups().getPlatforms();
            if (ptmList != null) {
                for (CmsBtProductModel_Group_Platform ptmObj : ptmList) {
                    if (ptmObj.getCartId() == cartId && ptmObj.getIsMain()) {
                        grpId = ptmObj.getGroupId();
                        break;
                    }
                }
            }

            boolean hasChg = false;
            List<CmsBtProductModel> infoList = null;
            if (grpId == 0) {
                // 当前主商品所在组没有其他商品
                logger.info("当前主商品所在组没有其他商品 prodId=" + groupObj.getProdId());
            } else {
                queryObj.setQuery("{'groups.platforms':{'$elemMatch':{'isMain':0,'cartId':" + cartId + ",'groupId':" + grpId + "}}}");
                infoList =  productService.getList(channelId, queryObj);
                if (infoList == null || infoList.isEmpty()) {
                    logger.info("当前主商品所在组没有其他商品的信息 groupId=" + grpId);
                }
            }
            if (grpId == 0 || infoList == null || infoList.isEmpty()) {
                List<CmsBtProductModel_Sku> skus = groupObj.getSkus();
                if (skus != null) {
                    for (CmsBtProductModel_Sku skuObj : skus) {
                        String chgFlg = org.apache.commons.lang3.StringUtils.trimToEmpty(skuObj.getAttribute("priceChgFlg"));
                        if (chgFlg.startsWith("U") || chgFlg.startsWith("D") || chgFlg.startsWith("X")) {
                            hasChg = true;
                            break;
                        } else {
                            hasChg = false;
                        }
                    }
                }

                Map<String, Integer> map = new HashMap<String, Integer>(1);
                if (hasChg) {
                    map.put("_chgFlg", 1);
                } else {
                    map.put("_chgFlg", 0);
                }
                chgFlgList.add(map);
                imgList.add(new ArrayList<Map<String, String>>(0));
                continue;
            }

            List<Map<String, String>> images1Arr = new ArrayList<Map<String, String>>();
            for (CmsBtProductModel itemObj : infoList) {
                List<CmsBtProductModel_Sku> skus = itemObj.getSkus();
                if (skus != null) {
                    for (CmsBtProductModel_Sku skuObj : skus) {
                        String chgFlg = org.apache.commons.lang3.StringUtils.trimToEmpty((String) (skuObj).get("priceChgFlg"));
                        if (chgFlg.startsWith("U") || chgFlg.startsWith("D") || chgFlg.startsWith("X")) {
                            hasChg = true;
                            break;
                          } else {
                            hasChg = false;
                        }
                    }
                }
                if (hasImgFlg) {
                    CmsBtProductModel_Field fields = itemObj.getFields();
                    if (fields != null) {
                        List<CmsBtProductModel_Field_Image> imgaes = fields.getImages1();
                        if (imgaes != null && imgaes.size() > 0) {
                            Map<String, String> map = new HashMap<String, String>(1);
                            map.put("value", imgaes.get(0).getName());
                            images1Arr.add(map);
                        }
                    }
                }
            }
            imgList.add(images1Arr);

            Map<String, Integer> map = new HashMap<String, Integer>(1);
            if (hasChg) {
                map.put("_chgFlg", 1);
            } else {
                map.put("_chgFlg", 0);
            }
            chgFlgList.add(map);
        }
        return rslt;
    }

    /**
     * 获取当前页的product列表
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public List<CmsBtProductModel> getProductList(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjection(searchItems.concat((String) cmsSessionBean.getAttribute("_adv_search_props_searchItems")).split(";"));
        queryObject.setSort(setSortValue(searchValue));
        queryObject.setSkip((searchValue.getProductPageNum() - 1) * searchValue.getProductPageSize());
        queryObject.setLimit(searchValue.getProductPageSize());
        return productService.getList(userInfo.getSelChannelId(), queryObject);
    }

    /**
     * 获取当前页的product列表Cnt
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public long getProductCnt(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        return productService.getCnt(userInfo.getSelChannelId(), getSearchQuery(searchValue, cmsSessionBean, false));
    }

    /**
     * 获取数据文件内容
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public byte[] getCodeExcelFile(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean)
            throws IOException, InvalidFormatException {

        String templatePath = Properties.readValue(CmsConstants.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE);

        long recCount = productService.getCnt(userInfo.getSelChannelId(), getSearchQuery(searchValue, cmsSessionBean, false));

        int pageCount = 0;
        if ((int) recCount % SELECT_PAGE_SIZE > 0) {
            pageCount =(int) recCount / SELECT_PAGE_SIZE + 1;
        } else {
            pageCount =(int) recCount / SELECT_PAGE_SIZE;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            for (int i = 0; i < pageCount; i++) {

                JomgoQuery queryObject = new JomgoQuery();
                queryObject.setQuery(getSearchQuery(searchValue, cmsSessionBean, false));
                queryObject.setProjection(searchItems.concat((String) cmsSessionBean.getAttribute("_adv_search_props_searchItems")).split(";"));
                queryObject.setSort(setSortValue(searchValue));
                queryObject.setSkip(i*SELECT_PAGE_SIZE);
                queryObject.setLimit(SELECT_PAGE_SIZE);
                List<CmsBtProductModel> items = productService.getList(userInfo.getSelChannelId(), queryObject);

                if (items.size() == 0) {
                    break;
                }

                // 每页开始行
                int startRowIndex =  i * SELECT_PAGE_SIZE + 1;
                boolean isContinueOutput = writeRecordToFile(book, items, cmsSessionBean.getPlatformType().get("cartId").toString(), startRowIndex);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
            }

            $info("文档写入完成");

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     * @param searchValue
     * @param cmsSessionBean
     * @return
     */
    private String getSearchQuery(CmsSearchInfoBean searchValue, CmsSessionBean cmsSessionBean, boolean isMain) {

        StringBuffer result = new StringBuffer();

        // 设置platform检索条件
        StringBuffer resultPlatforms = new StringBuffer();

        // 添加platform cart
        resultPlatforms.append(MongoUtils.splicingValue("cartId", Integer.valueOf(cmsSessionBean.getPlatformType().get("cartId").toString())));
        resultPlatforms.append(",");

        // 获取platform status
        if (searchValue.getPlatformStatus() != null
                && searchValue.getPlatformStatus().length > 0) {
            // 获取platform status
            resultPlatforms.append(MongoUtils.splicingValue("platformStatus", searchValue.getPlatformStatus()));
            resultPlatforms.append(",");
        }

        // 获取publishTime start
        if (searchValue.getPublishTimeStart() != null ) {
            resultPlatforms.append(MongoUtils.splicingValue("publishTime", searchValue.getPublishTimeStart() + " 00.00.00", "$gte"));
            resultPlatforms.append(",");
        }

        // 获取publishTime End
        if (searchValue.getPublishTimeTo() != null) {
            resultPlatforms.append(MongoUtils.splicingValue("publishTime", searchValue.getPublishTimeTo() + " 23.59.59", "$lte"));
            resultPlatforms.append(",");
        }

        if (isMain) {
            // 设置查询主商品
            resultPlatforms.append(MongoUtils.splicingValue("isMain", 1));
            resultPlatforms.append(",");
        }

        result.append(MongoUtils.splicingValue("groups.platforms"
                , "{" + resultPlatforms.toString().substring(0, resultPlatforms.toString().length() - 1) + "}"
                , "$elemMatch"));
        result.append(",");

        // 获取其他检索条件
        result.append(getSearchValueForMongo(searchValue));

        if (!StringUtils.isEmpty(result.toString())) {
            return "{" + result.toString().substring(0, result.toString().length() - 1) + "}";
        }
        else {
            return "";
        }
    }

    /**
     * 获取其他检索条件
     * @param searchValue
     * @return
     */
    private String getSearchValueForMongo (CmsSearchInfoBean searchValue) {
        StringBuffer result = new StringBuffer();

        // 获取category Id
        if (searchValue.getCatPath() != null) {
            result.append(MongoUtils.splicingValue("catPath", searchValue.getCatPath(), "$regex"));
            result.append(",");
        }

        // 获取product status
        if (searchValue.getProductStatus() != null
                && searchValue.getProductStatus().length > 0) {
            result.append(MongoUtils.splicingValue("fields.status", searchValue.getProductStatus()));
            result.append(",");
        }

        // 获取price start
        if(searchValue.getPriceType() != null
                && searchValue.getPriceStart() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getPriceType() + "St", searchValue.getPriceStart(), "$gte"));
            result.append(",");
        }

        // 获取price end
        if (searchValue.getPriceType() != null
                && searchValue.getPriceEnd() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getPriceType() + "Ed", searchValue.getPriceEnd(), "$lte"));
            result.append(",");
        }

        // 获取createdTime start
        if (searchValue.getCreateTimeStart() != null) {
            result.append(MongoUtils.splicingValue("created", searchValue.getCreateTimeStart() + " 00.00.00", "$gte"));
            result.append(",");
        }

        // 获取createdTime End
        if (searchValue.getCreateTimeTo() != null) {
            result.append(MongoUtils.splicingValue("created", searchValue.getCreateTimeTo() + " 23.59.59", "$lte"));
            result.append(",");
        }

        // 获取inventory
        if (searchValue.getCompareType() != null
                && searchValue.getInventory() != null) {
            result.append(MongoUtils.splicingValue("fields.quantity", searchValue.getInventory(), searchValue.getCompareType()));
            result.append(",");
        }

        // 获取brand
        if (searchValue.getBrand() != null) {
            result.append(MongoUtils.splicingValue("fields.brand", searchValue.getBrand()));
            result.append(",");
        }

        // 获取promotion
        if (searchValue.getTags() != null && searchValue.getTags().length > 0) {
            result.append(MongoUtils.splicingValue("tags", searchValue.getTags()));
            result.append(",");
        }

        // 获取code list用于检索code,model,productName,longTitle
        if (searchValue.getCodeList() != null
                && searchValue.getCodeList().length > 0) {
            List<String> orSearch = new ArrayList<>();
            orSearch.add(MongoUtils.splicingValue("fields.code", searchValue.getCodeList()));
            orSearch.add(MongoUtils.splicingValue("fields.model", searchValue.getCodeList()));

            if (searchValue.getCodeList().length == 1) {
                // 原文查询内容
                orSearch.add(MongoUtils.splicingValue("fields.productNameEn", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.longDesEn", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.shortDesEn", searchValue.getCodeList()[0], "$regex"));
                // 中文查询内容
                orSearch.add(MongoUtils.splicingValue("fields.longTitle", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.shortTitle", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.middleTitle", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.longDesCn", searchValue.getCodeList()[0], "$regex"));
            }
            result.append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append(",");
        }

        // 获取自定义查询条件
        List<Map<String, String>> custList = searchValue.getCustAttrMap();
        if (custList != null  && custList.size() > 0) {
            List<String> inputList = new ArrayList<String>();
            for (Map<String, String> item : custList) {
                String inputVal = org.apache.commons.lang3.StringUtils.trimToNull(item.get("inputVal"));
                if (inputVal != null) {
                    // 字符型和数字要分开比较
                    if (org.apache.commons.lang3.math.NumberUtils.isNumber(inputVal)) {
                        String qStr = "{'$or':[{'{0}':{'$type':1},'{0}':{1}},{'{0}':{'$type':16},'{0}':{1}},{'{0}':{'$type':18},'{0}':{1}},{'{0}':{'$type':2},'{0}':'{1}'}]}";
                        inputList.add(com.voyageone.common.util.StringUtils.format(qStr, item.get("inputOpts"), inputVal));
                    } else {
                        String qStr = "{'{0}':'{1}'}";
                        inputList.add(com.voyageone.common.util.StringUtils.format(qStr, item.get("inputOpts"), inputVal));
                    }
                }
            }
            if (inputList.size() > 0) {
                result.append("'$and':[");
                for (int i = 0, leng = inputList.size(); i < leng; i ++) {
                    if (i == 0) {
                        result.append(inputList.get(i));
                    } else {
                        result.append(",");
                        result.append(inputList.get(i));
                    }
                }
                result.append("],");
            }
        }

        // 查询价格比较（建议销售价和实际销售价）
        if (searchValue.getPriceDiffFlg() == 1) {
            // 建议销售价等于实际销售价
            result.append(MongoUtils.splicingValue("$where", "function(){ var skuArr = this.skus; for (var ind in skuArr) {if(skuArr[ind].priceRetail == skuArr[ind].priceSale){return true;}}}"));
            result.append(",");
        } else if (searchValue.getPriceDiffFlg() == 2) {
            // 建议销售价小于实际销售价
            result.append(MongoUtils.splicingValue("$where", "function(){ var skuArr = this.skus; for (var ind in skuArr) {if(skuArr[ind].priceRetail < skuArr[ind].priceSale){return true;}}}"));
            result.append(",");
        } else if (searchValue.getPriceDiffFlg() == 3) {
            // 建议销售价大于实际销售价
            result.append(MongoUtils.splicingValue("$where", "function(){ var skuArr = this.skus; for (var ind in skuArr) {if(skuArr[ind].priceRetail > skuArr[ind].priceSale){return true;}}}"));
            result.append(",");
        }

        // 查询价格变动
        if (searchValue.getPriceChgFlg() == 1) {
            // 涨价
            result.append("'skus':{'$elemMatch':{'priceChgFlg':{'$regex':'^U'}}},");
        } else if (searchValue.getPriceChgFlg() == 2) {
            // 降价
            result.append("'skus':{'$elemMatch':{'priceChgFlg':{'$regex':'^D'}}},");
        } else if (searchValue.getPriceChgFlg() == 3) {
            // 击穿
            result.append("'skus':{'$elemMatch':{'priceChgFlg':{'$regex':'^X'}}},");
        }

        // 获取翻译状态
        String transFlg = org.apache.commons.lang3.StringUtils.trimToNull(searchValue.getTransStsFlg());
        if (transFlg != null) {
            result.append(MongoUtils.splicingValue("fields.translateStatus", transFlg));
            result.append(",");
        }

        return result.toString();
    }

    /**
     * Code单位，文件输出
     *
     * @param book 输出Excel文件对象
     * @param items 待输出DB数据
     * @param cartId
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFile(Workbook book, List<CmsBtProductModel> items, String cartId, int startRowIndex) {
        boolean isContinueOutput = true;

        CellStyle unlock = FileUtils.createUnLockStyle(book);

            /*
             * 现有表格的列:
             * 0: No
             * 1: productId
             * 2: num_iid
             * 3: Code
             * 4: Brand
             * 5: product_type
             * 6: size_type
             * 7: Product_Name
             * 8: Product_Name_Cn
             * 9: Qty
             * 10: msrp
             * 11: retail_price
             * 12: Sale_Price
             * 13: 类目Path
             */
        Sheet sheet = book.getSheetAt(0);

        for (int i = 0; i < items.size(); i++) {

            CmsBtProductModel item = items.get(i);

            Row row = FileUtils.row(sheet, startRowIndex);

            // 最大行限制
            if ( startRowIndex + 1 > MAX_EXCEL_REC_COUNT -1) {
                isContinueOutput = false;

                FileUtils.cell(row, 0, unlock).setCellValue("未完，存在未抽出数据！");

                break;
            }

            // 内容输出
            FileUtils.cell(row, 0, unlock).setCellValue(startRowIndex);

            FileUtils.cell(row, 1, unlock).setCellValue(item.getProdId());

            FileUtils.cell(row, 2, unlock).setCellValue(item.getGroups().getPlatformByCartId(Integer.valueOf(cartId)).getNumIId());

            FileUtils.cell(row, 3, unlock).setCellValue(item.getFields().getCode());

            FileUtils.cell(row, 4, unlock).setCellValue(item.getFields().getBrand());

            FileUtils.cell(row, 5, unlock).setCellValue(item.getFields().getProductType());

            FileUtils.cell(row, 6, unlock).setCellValue(item.getFields().getSizeType());

            FileUtils.cell(row, 7, unlock).setCellValue(item.getFields().getProductNameEn());

            FileUtils.cell(row, 8, unlock).setCellValue(item.getFields().getLongTitle());

            FileUtils.cell(row, 9, unlock).setCellValue(StringUtils.null2Space2(String.valueOf(item.getFields().getQuantity())));

            FileUtils.cell(row, 10, unlock).setCellValue(getOutputPrice(item.getFields().getPriceMsrpSt(), item.getFields().getPriceMsrpEd()));

            FileUtils.cell(row, 11, unlock).setCellValue(getOutputPrice(item.getFields().getPriceRetailSt(), item.getFields().getPriceRetailEd()));

            FileUtils.cell(row, 12, unlock).setCellValue(getOutputPrice(item.getFields().getPriceSaleSt(), item.getFields().getPriceSaleEd()));

            FileUtils.cell(row, 13, unlock).setCellValue(item.getCatPath());

            startRowIndex = startRowIndex + 1;
        }

        return isContinueOutput;
    }

    /**
     * 金额输出
     *
     * @param strPrice 最小金额
     * @param endPrice 最大金额
     * @return String 输出金额
     */
    private String getOutputPrice(Double strPrice, Double endPrice) {
        String output = "";

        if(strPrice != null && endPrice != null) {
            if (strPrice.equals(endPrice)) {
                output = String.valueOf(strPrice);
            } else {
                output = strPrice + "～" + endPrice;
            }
        }
        return output;
    }

    /**
     * 获取二级Tag
     */
    private List<CmsBtTagModel> selectTagList(String channelId) {
        return tagService.getListByChannelId(channelId);
    }

    /**
     * 获取排序规则
     * @param searchValue
     * @return
     */
    private String setSortValue (CmsSearchInfoBean searchValue) {
        StringBuffer result = new StringBuffer();

        // 获取排序字段1
        if (searchValue.getSortOneName() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getSortOneName(),
                    searchValue.getSortOneType() == null ? -1 : Integer.valueOf(searchValue.getSortOneType())));
            result.append(",");
        }

        // 获取排序字段2
        if (searchValue.getSortTwoName() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getSortTwoName()
                    , searchValue.getSortTwoType() == null ? -1 : Integer.valueOf(searchValue.getSortTwoType())));
            result.append(",");
        }

        // 获取排序字段3
        if (searchValue.getSortThreeName() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getSortThreeName(),
                    searchValue.getSortThreeType() == null ? -1 : Integer.valueOf(searchValue.getSortThreeType())));
            result.append(",");
        }

        return result.toString().length() > 0 ? "{" + result.toString().substring(0, result.toString().length()-1) + "}" : null;
    }

    // 取得自定义显示列设置
    public List<Map<String, Object>> getCustColumns() {
        return  cmsMtCommonPropDao.selectCustColumns();
    }

    // 取得用户自定义显示列设置
    public Map<String, Object> getUserCustColumns(int userId) {
        Map<String, Object> rsMap = new HashMap<String, Object>();

        List<Map<String, Object>> rsList = cmsMtCommonPropDao.selectUserCustColumns(userId);
        if (rsList == null || rsList.isEmpty()) {
            rsMap.put("custAttrList", new String[]{});
            rsMap.put("commList", new String[]{});
            return rsMap;
        }
        String custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val1"));
        String commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val2"));
        rsMap.put("custAttrList", custAttrStr.split(","));
        rsMap.put("commList", commStr.split(","));
        return rsMap;
    }

    // 根据类目路径查询已翻译的属性信息
    // 只查询feed_prop_original和feed_prop_translation
    public List<Map<String, Object>> selectAttrs(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsBtFeedCustomPropDao.selectAttrs(params);
    }

    // 取得用户自定义显示列设置
    public void getUserCustColumns(String channelId, int userId, CmsSessionBean cmsSession) {
        List<Map<String, Object>> rsList = cmsMtCommonPropDao.selectUserCustColumns(userId);
        String custAttrStr = null;
        String commStr = null;
        if (rsList == null || rsList.isEmpty()) {
            logger.debug("该用户还未设置自定义查询列 userId=" + userId + " channelId=" + channelId);
            custAttrStr = "";
            commStr = "";
        } else {
            custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val1"));
            commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val2"));
        }

        // 设置自定义查询用的属性
        List<Map<String, String>> custAttsQueryList = new ArrayList<Map<String, String>>();

        List<Map<String, Object>> customProps2 = new ArrayList<Map<String, Object>>();
        String[] custAttrList = custAttrStr.split(",");
        StringBuilder customPropsStr = new StringBuilder();
        if (custAttrList.length > 0) {
            Map<String, Object> params = new HashMap<String, Object>(2);
            params.put("channelId", channelId);
            params.put("feedCatPath", "0");
            List<Map<String, Object>> customProps = cmsBtFeedCustomPropDao.selectAttrs(params);
            for (Map<String, Object> props : customProps) {
                String propId = (String) props.get("feed_prop_original");
                Map atts = new HashMap<>(2);
                atts.put("configCode", "feed.cnAtts." + propId);
                atts.put("configValue1", (String) props.get("feed_prop_translation"));
                custAttsQueryList.add(atts);

                if (ArrayUtils.contains(custAttrList, propId)) {
                    customProps2.add(props);
                    customPropsStr.append("feed.cnAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                }
            }
        }
        List<Map<String, Object>> commonProp2 = new ArrayList<Map<String, Object>>();
        String[] commList = commStr.split(",");
        StringBuilder commonPropsStr = new StringBuilder();
        if (commList.length > 0) {
            List<Map<String, Object>> commonProps = cmsMtCommonPropDao.selectCustColumns();
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
                Map atts = new HashMap<>(2);
                atts.put("configCode", "fields." + propId);
                atts.put("configValue1", (String) props.get("propName"));
                custAttsQueryList.add(atts);

                if (ArrayUtils.contains(commList, propId)) {
                    commonProp2.add(props);
                    commonPropsStr.append("fields.");
                    commonPropsStr.append(propId);
                    commonPropsStr.append(";");
                }
            }
        }

        cmsSession.putAttribute("_adv_search_props_custAttsQueryList", custAttsQueryList);
        cmsSession.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSession.putAttribute("_adv_search_customProps", customProps2);
        cmsSession.putAttribute("_adv_search_commonProps", commonProp2);
    }

    // 保存用户自定义显示列设置
    public void saveCustColumnsInfo(UserSessionBean userInfo, CmsSessionBean cmsSessionBean, String[] param1, String[] param2) {
        String customStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(param1, ","));
        String commonStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(param2, ","));

        List<Map<String, Object>> customProps2 = new ArrayList<Map<String, Object>>();
        StringBuilder customPropsStr = new StringBuilder();
        if (param1 != null && param1.length > 0) {
            Map<String, Object> params = new HashMap<String, Object>(2);
            params.put("channelId", userInfo.getSelChannelId());
            params.put("feedCatPath", "0");
            List<Map<String, Object>> customProps = cmsBtFeedCustomPropDao.selectAttrs(params);
            for (Map<String, Object> props : customProps) {
                String propId = (String) props.get("feed_prop_original");
                if (ArrayUtils.contains(param1, propId)) {
                    customProps2.add(props);
                    customPropsStr.append("feed.cnAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                }
            }
        }

        List<Map<String, Object>> commonProp2 = new ArrayList<Map<String, Object>>();
        StringBuilder commonPropsStr = new StringBuilder();
        if (param2 != null && param2.length > 0) {
            List<Map<String, Object>> commonProps = cmsMtCommonPropDao.selectCustColumns();
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
                if (ArrayUtils.contains(param2, propId)) {
                    commonProp2.add(props);
                    commonPropsStr.append("fields.");
                    commonPropsStr.append(propId);
                    commonPropsStr.append(";");
                }
            }
        }
        cmsSessionBean.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSessionBean.putAttribute("_adv_search_customProps", customProps2);
        cmsSessionBean.putAttribute("_adv_search_commonProps", commonProp2);

        List<Map<String, Object>> rsList = cmsMtCommonPropDao.selectUserCustColumns(userInfo.getUserId());
        int rs = 0;
        if (rsList == null || rsList.isEmpty()) {
            rs = cmsMtCommonPropDao.insertUserCustColumns(userInfo.getUserId(), userInfo.getUserName(), customStrs, commonStrs);
        } else {
            rs = cmsMtCommonPropDao.updateUserCustColumns(userInfo.getUserId(), userInfo.getUserName(), customStrs, commonStrs);
        }
        if (rs == 0) {
            logger.error("保存自定义显示列设置不成功 userid=" + userInfo.getUserId());
        }
    }

    /**
     * 检查翻译状态
     */
    public void checkProcStatus(List<CmsBtProductModel> productList, String lang) {
        if (productList == null || productList.isEmpty()) {
            return;
        }
        List<TypeBean> transStatusList = TypeConfigEnums.MastType.translationStatus.getList(lang);
        Map<String, String> transStatusMap = new HashMap<>(transStatusList.size());
        for (TypeBean beanObj : transStatusList) {
            transStatusMap.put(beanObj.getValue(), beanObj.getName());
        }
        List<TypeBean> editStatusList = TypeConfigEnums.MastType.editStatus.getList(lang);
        Map<String, String> editStatusMap = new HashMap<>(editStatusList.size());
        for (TypeBean beanObj : editStatusList) {
            editStatusMap.put(beanObj.getValue(), beanObj.getName());
        }
        List<TypeBean> lockStatusList = TypeConfigEnums.MastType.procLockStatus.getList(lang);
        Map<String, String> lockStatusMap = new HashMap<>(lockStatusList.size());
        for (TypeBean beanObj : lockStatusList) {
            lockStatusMap.put(beanObj.getValue(), beanObj.getName());
        }

        for (CmsBtProductModel prodObj : productList) {
            CmsBtProductModel_Field fieldsObj = prodObj.getFields();
            if (fieldsObj != null) {
                String stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(fieldsObj.getTranslateStatus());
                if (stsFlg != null) {
                    String stsValueStr = transStatusMap.get(stsFlg);
                    if (stsValueStr == null) {
                        fieldsObj.setTranslateStatus("");
                    } else {
                        fieldsObj.setTranslateStatus(stsValueStr);
                    }
                } else {
                    fieldsObj.setTranslateStatus("");
                }

                stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(fieldsObj.getEditStatus());
                if (stsFlg != null) {
                    String stsValueStr = editStatusMap.get(stsFlg);
                    if (stsValueStr == null) {
                        fieldsObj.setEditStatus("");
                    } else {
                        fieldsObj.setEditStatus(stsValueStr);
                    }
                } else {
                    fieldsObj.setEditStatus("");
                }

                stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(fieldsObj.getLock());
                if (stsFlg != null) {
                    String stsValueStr = lockStatusMap.get(stsFlg);
                    if (stsValueStr == null) {
                        fieldsObj.setLock("");
                    } else {
                        fieldsObj.setLock(stsValueStr);
                    }
                } else {
                    fieldsObj.setLock("");
                }
            }
        }
    }
}
