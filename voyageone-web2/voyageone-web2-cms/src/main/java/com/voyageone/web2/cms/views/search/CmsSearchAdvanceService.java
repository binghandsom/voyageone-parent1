package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsMtCommonPropDao;
import com.voyageone.service.dao.cms.CmsMtCustomWordDao;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
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

    private final String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;" +
            "modifier;fields;feed.cnAtts;groups.msrpStart;groups.msrpEnd;groups.retailPriceStart;groups.retailPriceEnd;" +
            "groups.salePriceStart;groups.salePriceEnd;groups.platforms.$;skus";

    // 未结束提示信息
    private final String not_End_Prompt_Message = "";

    // 其他用户使用提示信息
    private String other_User_Process_Message = "其他用户正在使用，请5分钟后再试！";

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
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) throws IOException{

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
        masterData.put("custAttsList", customWordDao.selectCustAttrs(userInfo.getSelChannelId(), language));

        return masterData;
    }

    /**
     * 返回当前页的group列表
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public List<CmsBtProductModel> getGroupList(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQueryForGroup(searchValue, cmsSessionBean));
        queryObject.setProjection(searchItems.split(";"));
        queryObject.setSort(setSortValue(searchValue));
        queryObject.setSkip((searchValue.getGroupPageNum()-1)*searchValue.getGroupPageSize());
        queryObject.setLimit(searchValue.getGroupPageSize());

        return productService.getList(userInfo.getSelChannelId(), queryObject);
    }

    /**
     * 返回当前页的group列表CNT
     * @param searchValue
     * @param userInfo
     * @param cmsSessionBean
     * @return
     */
    public long getGroupCnt(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        return productService.getCnt(userInfo.getSelChannelId(), getSearchQueryForGroup(searchValue, cmsSessionBean));
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
        queryObject.setQuery(getSearchQueryForProduct(searchValue, cmsSessionBean));
        queryObject.setProjection(searchItems.split(";"));
        queryObject.setSort(setSortValue(searchValue));
        queryObject.setSkip((searchValue.getGroupPageNum() - 1) * searchValue.getGroupPageSize());
        queryObject.setLimit(searchValue.getGroupPageSize());
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
        return productService.getCnt(userInfo.getSelChannelId(), getSearchQueryForProduct(searchValue, cmsSessionBean));
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

        long recCount = productService.getCnt(userInfo.getSelChannelId(), getSearchQueryForProduct(searchValue, cmsSessionBean));

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
                queryObject.setQuery(getSearchQueryForProduct(searchValue, cmsSessionBean));
                queryObject.setProjection(searchItems.split(";"));
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
     * 获取product的检索条件
     * @param searchValue
     * @param cmsSessionBean
     * @return
     */
    private String getSearchQueryForProduct (CmsSearchInfoBean searchValue, CmsSessionBean cmsSessionBean) {

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
     * 返回页面端的检索条件拼装成mongo使用的条件
     * @param searchValue
     * @param cmsSessionBean
     * @return
     */
    private String getSearchQueryForGroup (CmsSearchInfoBean searchValue, CmsSessionBean cmsSessionBean) {

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

        // 设置查询主商品
        resultPlatforms.append(MongoUtils.splicingValue("isMain", 1));
        resultPlatforms.append(",");

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
        if (searchValue.getCustAttrKey() != null  && searchValue.getCustAttrKey().length() > 0
                && searchValue.getCustAttrValue() != null  && searchValue.getCustAttrValue().length() > 0) {
            result.append(MongoUtils.splicingValue(searchValue.getCustAttrKey(), searchValue.getCustAttrValue()));
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
//        return result.toString().length() > 0 ? result.toString().substring(0, result.toString().length()-1) : null;

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

    // 保存用户自定义显示列设置
    public void saveCustColumnsInfo(int userId, String userName, String param1, String param2) {
        List<Map<String, Object>> rsList = cmsMtCommonPropDao.selectUserCustColumns(userId);
        int rs = 0;
        if (rsList == null || rsList.isEmpty()) {
            rs = cmsMtCommonPropDao.insertUserCustColumns(userId, userName, param1, param2);
        } else {
            rs = cmsMtCommonPropDao.updateUserCustColumns(userId, userName, param1, param2);
        }
        if (rs == 0) {
            logger.error("保存设置不成功 userid=" + userId);
        }
    }
}
