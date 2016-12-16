package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.producttop.*;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductTopDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.mongo.product.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/11/28.
 */
@Service
public class ProductTopService extends BaseService {
    @Autowired
    CmsBtProductTopDao dao;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    MongoSequenceService mongoSequenceService;

    //获取初始化数据
    public Map<String, Object> init(String channelId, String sellerCatId, String language) throws IOException {

        Map<String, Object> data = new HashMap<>();
        CmsBtProductTopModel topModel = dao.selectBySellerCatId(sellerCatId, channelId);

        if (topModel != null) {
            data.put("sortColumnName", topModel.getSortColumnName());
            data.put("sortType", topModel.getSortType());
        } else {
            data.put("sortColumnName", "created");
            data.put("sortType", -1);
        }
        // 获取brand list
        data.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, language));

        return data;
    }

    //加入置顶区
    public void addTopProduct(AddTopProductParameter param, String channelId, String userName) {

        CmsBtProductTopModel topModel = dao.selectBySellerCatId(param.getSellerCatId(), channelId);

        if (param.getIsSeachAdd()) {
            //全量加入
            List<String> codeList = getSearchCodeList(param, channelId);
            if (codeList != null) {
                param.setCodeList(codeList);
            }
        }
        boolean isAdd = false;
        if (topModel == null) {
            isAdd = true;
            topModel = new CmsBtProductTopModel();
            topModel.setProductTopId(mongoSequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CmsBtProductTopID));
            topModel.setCreated(DateTimeUtil.getNow());
            topModel.setCreater(userName);

            topModel.setChannelId(channelId);
            topModel.setSellerCatId(param.getSellerCatId());
        }
        if (topModel.getProductCodeList() == null) topModel.setProductCodeList(new ArrayList<>());

        //加入code
        HashSet<String> codeSet = new HashSet<>();
        if (param.getCodeList() != null) {
            param.getCodeList().stream().forEach(code -> {
                codeSet.add(code);
            });
        }
        topModel.getProductCodeList().stream().forEach(code -> {
            codeSet.add(code);
        });
        topModel.setProductCodeList(codeSet.stream().collect(Collectors.toList()));

        if (isAdd) {
            dao.insert(topModel);
        } else {
            dao.update(topModel);
        }
    }

    public List<String> getSearchCodeList(AddTopProductParameter param, String channelId) {
        CmsBtProductTopModel topModel = dao.selectBySellerCatId(param.getSellerCatId(), channelId);
        JongoQuery queryObject = getJongoQuery(param.getSearchParameter(), topModel);
        queryObject.setProjectionExt("common.fields.code");
        List<CmsBtProductModel> list = cmsBtProductDao.select(queryObject, channelId);
        return list.stream().map(f -> f.getCommon().getFields().getCode()).collect(Collectors.toList());
    }

    //保存置顶区
    public void saveTopProduct(SaveTopProductParameter param, String channelId, String userName) {
        CmsBtProductTopModel topModel = dao.selectBySellerCatId(param.getSellerCatId(), channelId);
        topModel.setProductCodeList(param.getCodeList());
        dao.update(topModel);
    }

    //普通区查询 获取指定页
    public List<ProductInfo> getPage(ProductPageParameter param, String channelId, String userName) {

        CmsBtProductTopModel topModel = dao.selectBySellerCatId(param.getSellerCatId(), channelId);
        //保存排序字段
        topModel = saveSortColumnName(param, topModel, channelId, userName);

        int pageIndex = param.getPageIndex();
        int pageSize = param.getPageSize();
        JongoQuery queryObject = getJongoQuery(param, topModel);
        queryObject.setProjection("");
        queryObject.setLimit(pageSize);
        queryObject.setSkip((pageIndex - 1) * pageSize);
        //排序字段
        if (topModel != null && !com.voyageone.common.util.StringUtils.isEmpty(topModel.getSortColumnName())) {
            queryObject.setSort(String.format("{\"%s\":%s}", topModel.getSortColumnName(), topModel.getSortType()));
        } else {
            queryObject.setSort("{\"prodId\":-1}");
        }

        List<CmsBtProductModel> list = cmsBtProductDao.select(queryObject, channelId);
        List<ProductInfo> listResult = list.stream().map(f -> mapProductInfo(f, param.getCartId())).collect(Collectors.toList());
        return listResult;
    }

    //保存排序字段
    public CmsBtProductTopModel saveSortColumnName(ProductPageParameter param, CmsBtProductTopModel topModel, String channelId, String userName) {
        if (!StringUtils.isEmpty(param.getSortColumnName())) {
            boolean isAdd = false;
            if (topModel == null) {
                isAdd = true;
                topModel = new CmsBtProductTopModel();
                topModel.setProductTopId(mongoSequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CmsBtProductTopID));

                topModel.setChannelId(channelId);
                topModel.setSellerCatId(param.getSellerCatId());
                topModel.setCreated(DateTimeUtil.getNow());
                topModel.setCreater(userName);
            }
            topModel.setSortColumnName(param.getSortColumnName());
            topModel.setSortType(param.getSortType());
            if (isAdd) {
                dao.insert(topModel);
            } else {
                dao.update(topModel);
            }
        }
        return topModel;
    }


    //普通区查询 获取总数量
    public Object getCount(ProductPageParameter param, String channelId) {
        CmsBtProductTopModel topModel = dao.selectBySellerCatId(param.getSellerCatId(), channelId);

        JongoQuery queryObject = getJongoQuery(param, topModel);

        return cmsBtProductDao.countByQuery(queryObject.getJongoQueryStr(), channelId);
    }

    //获取置顶区 列表
    public List<ProductInfo> getTopList(GetTopListParameter parameter, String channelId) {
        CmsBtProductTopModel topModel = dao.selectBySellerCatId(parameter.getSellerCatId(), channelId);
        if (topModel == null || topModel.getProductCodeList() == null || topModel.getProductCodeList().size() == 0)
            return new ArrayList<>();

        JongoQuery jongoQuery = getTopListJongoQuery(parameter, topModel);
        List<CmsBtProductModel> list = cmsBtProductDao.select(jongoQuery, channelId);
        List<ProductInfo> listResult = list.stream().map(f -> mapProductInfo(f, parameter.getCartId())).collect(Collectors.toList());

        //排序
        List<ProductInfo> listSortResult = new ArrayList<>();
        topModel.getProductCodeList().forEach(f -> {
            Optional<ProductInfo> optional = listResult.stream().filter(ff -> f.equals(ff.getCode())).findFirst();
            if (optional != null) {
                listSortResult.add(optional.get());
            }
        });

        return listSortResult;
    }

    JongoQuery getTopListJongoQuery(GetTopListParameter param, CmsBtProductTopModel topModel) {
        JongoQuery queryObject = new JongoQuery();

        //平台cartId
        Criteria criteria = new Criteria("platforms.P" + param.getCartId()).exists(true);
        // 获取code list用于检索code
        if (topModel.getProductCodeList() != null
                && topModel.getProductCodeList().size() > 0) {
            List<String> inputCodeList = topModel.getProductCodeList();
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                criteria.and("common.fields.code").in(inputCodeList);
            }
        }
        queryObject.setQuery(criteria);
        return queryObject;
    }


    ProductInfo mapProductInfo(CmsBtProductModel f, int cartId) {
        ProductInfo info = new ProductInfo();
        info.setBrand(f.getCommon().getFields().getBrand());
        info.setCode(f.getCommon().getFields().getCode());
        info.setModel(f.getCommon().getFields().getModel());
        info.setProductName(f.getCommon().getFields().getProductNameEn());
        info.setQuantity(f.getCommon().getFields().getQuantity());
        info.setCreated(f.getCreated());
        CmsBtProductModel_Sales cmsBtProductModel_sales = f.getSales();
        if (cmsBtProductModel_sales != null) {
            info.setSalesSum7(cmsBtProductModel_sales.getCodeSum7(cartId));
            info.setSalesSum30(cmsBtProductModel_sales.getCodeSum30(cartId));
            info.setSalesSumYear(cmsBtProductModel_sales.getCodeSumYear(cartId));
            info.setSalesSumAll(cmsBtProductModel_sales.getCodeSumAll(cartId));
        }
        //图片
        List<CmsBtProductModel_Field_Image> imgList = f.getCommonNotNull().getFieldsNotNull().getImages6();
        if (!imgList.isEmpty()) {
            info.setImage1(imgList.get(0).getName());
        }
        if (StringUtil.isEmpty(info.getImage1())) {
            imgList = f.getCommonNotNull().getFieldsNotNull().getImages1();
            if (!imgList.isEmpty()) {
                info.setImage1(imgList.get(0).getName());
            }
        }
        CmsBtProductModel_Platform_Cart platform_Cart = f.getPlatform(cartId);
        if (platform_Cart != null) {
            info.setpNumIId(platform_Cart.getpNumIId());
            info.setpPriceSaleSt(platform_Cart.getpPriceSaleSt());
            info.setpPriceSaleEd(platform_Cart.getpPriceSaleEd());

            /**存储状态信息*/
            info.setStatus(platform_Cart.getStatus());
            info.setpStatus(Optional.ofNullable(platform_Cart.getpStatus())
                    .map(Enum::name)
                    .orElse(""));
            info.setpReallyStatus(Optional.ofNullable(platform_Cart.getpReallyStatus())
                    .orElse(""));
            info.setIsMain(Optional.ofNullable(platform_Cart.getpIsMain())
                    .orElse(0));
            info.setpPublishError(Optional.ofNullable(platform_Cart.getpPublishError())
                    .orElse(""));

            if (platform_Cart.getSkus() != null) {
                info.setSkuCount(platform_Cart.getSkus().size());
            }
        }
        return info;
    }

    JongoQuery getJongoQuery(ProductPageParameter param, CmsBtProductTopModel topModel) {

        JongoQuery queryObject = new JongoQuery();
        //平台cartId    商品分类
        Criteria criteria = new Criteria("platforms.P" + param.getCartId() + ".sellerCats.cName").regex("^" + param.getSellerCatPath());

        // 获取code list用于检索code  not in
        if (topModel != null && topModel.getProductCodeList() != null
                && topModel.getProductCodeList().size() > 0) {
            List<String> inputCodeList = topModel.getProductCodeList();
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                criteria.and("common.fields.code").nin(inputCodeList);
            }
        }
        //品牌
        if (param.getBrandList() != null && param.getBrandList().size() > 0) {
            if (param.getIsInclude()) {
                criteria.and("common.fields.brand").in(param.getBrandList());
            } else {
                criteria.and("common.fields.brand").nin(param.getBrandList());
            }
        }

        //库存 quantity
        if (StringUtils.isNotEmpty(param.getCompareType()) && param.getQuantity() != null) {
            switch (param.getCompareType()) {
                case "$gt":
                    criteria.and("common.fields.quantity").gt(param.getQuantity());
                    break;
                case "$lt":
                    criteria.and("common.fields.quantity").lt(param.getQuantity());
                    break;
                case "$eq":
                    criteria.and("common.fields.quantity").is(param.getQuantity());
                    break;
                default:
                    break;
            }
        }

        // 获取code list用于检索code,model,sku
        if (param.getCodeList() != null
                && param.getCodeList().size() > 0) {
            List<String> inputCodeList = param.getCodeList();
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                criteria.orOperator(new Criteria("common.fields.code").in(inputCodeList), new Criteria("common.fields.model").in(inputCodeList), new Criteria("common.skus.skuCode").in(inputCodeList));
            }
        }
        queryObject.setQuery(criteria);
        return queryObject;
    }
}