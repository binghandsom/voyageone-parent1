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
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductTopModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Map<String, Object> init(String channelId,String catId, String language) throws IOException {

        Map<String, Object> data = new HashMap<>();
        CmsBtProductTopModel topModel = dao.selectByCatId(catId, channelId);

        if (topModel != null) {
            data.put("sortColumnName", topModel.getSortColumnName());
            data.put("sortType",topModel.getSortType());
        }
        // 获取brand list
        data.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, language));

        return data;
    }
    //加入置顶区
    public  void addTopProduct(AddTopProductParameter param, String channelId, String userName) {

        CmsBtProductTopModel topModel = dao.selectByCatId(param.getpCatId(), channelId);

        if (param.isSeachAdd()) {
            //全量加入


        }
        boolean isAdd = false;
        if (topModel == null) {
            isAdd = true;
            topModel = new CmsBtProductTopModel();
            topModel.setProductTopId(mongoSequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CmsBtProductTopID));
            topModel.setCreated(DateTimeUtil.getNow());
            topModel.setCreater(userName);

            topModel.setChannelId(channelId);
            topModel.setCatId(param.getpCatId());
        }
        if (topModel.getProductCodeList() == null) topModel.setProductCodeList(new ArrayList<>());

        //加入code
        final CmsBtProductTopModel saveTopModel = topModel;
        param.getCodeList().stream().forEach(code -> {
            if (!saveTopModel.getProductCodeList().contains(code)) {
                saveTopModel.getProductCodeList().add(code);
            }
        });
        topModel.setProductCodeList(param.getCodeList());
        if (isAdd) {
            dao.insert(topModel);
        } else {
            dao.update(topModel);
        }

    }

    //保存置顶区
    public  void saveTopProduct(SaveTopProductParameter param, String channelId, String userName) {
        CmsBtProductTopModel topModel = dao.selectByCatId(param.getpCatId(), channelId);
        topModel.setProductCodeList(param.getCodeList());
        dao.update(topModel);
    }

    //普通区查询 获取指定页
    public List<ProductInfo> getPage(ProductPageParameter param, String channelId,String userName) {

        CmsBtProductTopModel topModel = dao.selectByCatId(param.getpCatId(), channelId);
        //保存排序字段
        topModel=saveSortColumnName(param, topModel, channelId, userName);

        int pageIndex = param.getPageIndex();
        int pageSize = param.getPageSize();
        JongoQuery queryObject = getJongoQuery(param, topModel);
        queryObject.setProjection("");
        queryObject.setLimit(pageSize);
        queryObject.setSkip((pageIndex - 1) * pageSize);
        //排序字段
        if (topModel != null && !com.voyageone.common.util.StringUtils.isEmpty(topModel.getSortColumnName())) {
            queryObject.setSort(String.format("{%s:%s}", topModel.getSortColumnName(), topModel.getSortType()));
        } else {
            queryObject.setSort("{prodId:-1}");
        }

        List<CmsBtProductModel> list = cmsBtProductDao.select(queryObject, channelId);
        List<ProductInfo> listResult = list.stream().map(f -> mapProductInfo(f, param.getCartId())).collect(Collectors.toList());
        return listResult;
    }

    //保存排序字段
    public  CmsBtProductTopModel  saveSortColumnName(ProductPageParameter param, CmsBtProductTopModel topModel,String channelId,String userName) {
        if (!StringUtils.isEmpty(param.getSortColumnName())) {
            boolean isAdd = false;
            if (topModel == null) {
                isAdd = true;
                topModel = new CmsBtProductTopModel();
                topModel.setProductTopId(mongoSequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CmsBtProductTopID));

                topModel.setChannelId(channelId);
                topModel.setCatId(param.getpCatId());
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
        return  topModel;
    }


    //普通区查询 获取总数量
    public Object getCount(ProductPageParameter param, String channelId) {
        CmsBtProductTopModel topModel = dao.selectByCatId(param.getpCatId(), channelId);

        JongoQuery queryObject = getJongoQuery(param, topModel);

        return cmsBtProductDao.countByQuery(queryObject.getJongoQueryStr(), channelId);
    }

    //获取置顶区 列表
    public List<ProductInfo> getTopList(GetTopListParameter parameter,String channelId) {
        CmsBtProductTopModel topModel = dao.selectByCatId(parameter.getpCatId(),channelId);
        if (topModel == null || topModel.getProductCodeList() == null || topModel.getProductCodeList().size() == 0)
            return new ArrayList<>();

        JongoQuery jongoQuery = getTopListJongoQuery(parameter, topModel);
        List<CmsBtProductModel> list = cmsBtProductDao.select(jongoQuery, channelId);
        List<ProductInfo> listResult = list.stream().map(f -> mapProductInfo(f, parameter.getCartId())).collect(Collectors.toList());
        return listResult;
    }

    JongoQuery getTopListJongoQuery(GetTopListParameter param,CmsBtProductTopModel topModel) {
        JongoQuery queryObject = new JongoQuery();

        //平台cartId    商品分类
        queryObject.addQuery("{'platforms.P#.pCatId':#}");
        queryObject.addParameters(param.getCartId(), param.getpCatId());

        // 获取code list用于检索code
        if (topModel.getProductCodeList() != null
                && topModel.getProductCodeList().size() > 0) {
            List<String> inputCodeList = topModel.getProductCodeList();
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                Object inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);
                queryObject.addQuery("{'common.fields.code':{$in:#}}");
                queryObject.addParameters(inputCodeArr);
            }
        }
        return queryObject;
    }


    ProductInfo mapProductInfo(CmsBtProductModel f,int cartId)
    {
        ProductInfo info = new ProductInfo();
        info.setBrand(f.getCommon().getFields().getBrand());
        info.setCode(f.getCommon().getFields().getCode());
        info.setModel(f.getCommon().getFields().getModel());
        info.setProductName(f.getCommon().getFields().getProductNameEn());
        info.setQuantity(f.getCommon().getFields().getQuantity());
        //图片
        List<CmsBtProductModel_Field_Image> imgList = f.getCommonNotNull().getFieldsNotNull().getImages6();
        if (!imgList.isEmpty()) {
            info.setImage1(imgList.get(0).getName());
        }
        if(StringUtil.isEmpty(info.getImage1())){
            imgList = f.getCommonNotNull().getFieldsNotNull().getImages1();
            if (!imgList.isEmpty()) {
                info.setImage1(imgList.get(0).getName());
            }
        }
        CmsBtProductModel_Platform_Cart platform_Cart= f.getPlatform(cartId);
        if(platform_Cart!=null) {
            if(platform_Cart.getSkus()!=null) {
                info.setSkuCount(platform_Cart.getSkus().size());
            }
        }
        return info;
    }

     JongoQuery getJongoQuery(ProductPageParameter param, CmsBtProductTopModel topModel) {
        JongoQuery queryObject = new JongoQuery();

        //平台cartId    商品分类
        queryObject.addQuery("{'platforms.P#.pCatId':#}");
        queryObject.addParameters(param.getCartId(), param.getpCatId());

        // 获取code list用于检索code  not in
         if (topModel!=null&&topModel.getProductCodeList() != null
                 && topModel.getProductCodeList().size() > 0) {
             List<String> inputCodeList = topModel.getProductCodeList();
             inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
             if (inputCodeList.size() > 0) {
                 Object inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);
                 queryObject.addQuery("{'common.fields.code':{$nin:#}}");
                 queryObject.addParameters(inputCodeArr);
             }
         }
        //品牌
         if (param.getBrandList() !=  null && param.getBrandList().size() > 0) {
             if (param.getIsInclude()) {
                 queryObject.addQuery("{'common.fields.brand':{$in:#}}");
                 queryObject.addParameters(param.getBrandList());
             } else {
                 // 不在指定范围
                 queryObject.addQuery("{'common.fields.brand':{$nin:#}}");
                 queryObject.addParameters(param.getBrandList());
             }
         }


        //库存 quantity
        if (StringUtils.isNotEmpty(param.getCompareType()) && param.getQuantity() != null) {
            queryObject.addQuery("{'common.fields.quantity':{#:#}}");
            queryObject.addParameters(param.getCompareType(), param.getQuantity());
        }

        // 获取code list用于检索code,model,sku
        if (param.getCodeList() != null
                && param.getCodeList().size() > 0) {
            List<String> inputCodeList = param.getCodeList();
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                Object inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);
                queryObject.addQuery("{$or:[{'common.fields.code':{$in:#}},{'common.fields.model':{$in:#}},{'common.skus.skuCode':{$in:#}}]}");
                queryObject.addParameters(inputCodeArr, inputCodeArr, inputCodeArr);
            }
        }
        return queryObject;
    }
}