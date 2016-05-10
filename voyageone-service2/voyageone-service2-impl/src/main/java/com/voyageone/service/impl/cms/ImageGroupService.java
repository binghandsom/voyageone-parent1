package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.bean.cms.task.stock.StockExcelBean;
import com.voyageone.service.bean.cms.task.stock.StockIncrementExcelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * ImageGroup Service
 *
 * @author jeff.duan 16/6/6
 * @version 2.0.0
 */
@Service
public class ImageGroupService extends BaseService {
    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;
    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence

    /**
     * 新建ImageGroupInfo
     */
    public void save(String channelId, String cartId, String imageGroupName, String imageType, String viewType,
                     List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        CmsBtImageGroupModel model = new CmsBtImageGroupModel();
        model.setChannelId(channelId);
        model.setCartId(Integer.parseInt(cartId));
        model.setImageGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_GROUP_ID));
        model.setImageGroupName(imageGroupName);
        model.setImageType(Integer.parseInt(imageType));
        model.setViewType(Integer.parseInt(viewType));
        // 什么都不选的情况下，要设置成"All"
        if (brandNameList.size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setBrandName(lst);
        } else {
            model.setBrandName(brandNameList);
        }
        if (productTypeList.size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setProductType(lst);
        } else {
            model.setProductType(productTypeList);
        }
        if (sizeTypeList.size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setSizeType(lst);
        } else {
            model.setSizeType(sizeTypeList);
        }
        model.setActive(1);
        cmsBtImageGroupDao.insert(model);
    }

    /**
     * 逻辑删除Image项目
     */
    public void logicDeleteImage(String imageGroupId, String originUrl) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            List<CmsBtImageGroupModel_Image> images = model.getImage();
            if(images != null) {
                for (CmsBtImageGroupModel_Image image : images) {
                    if (image.getOriginUrl().equals(originUrl)) {
                        images.remove(image);
                    }
                }
                cmsBtImageGroupDao.update(model);
            }
        }
    }

    /**
     * 更新ImageGroupInfo
     */
    public void update(String imageGroupId, String cartId, String imageGroupName, String imageType, String viewType,
                     List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            model.setCartId(Integer.parseInt(cartId));
            model.setImageGroupName(imageGroupName);
            model.setImageType(Integer.parseInt(imageType));
            model.setViewType(Integer.parseInt(viewType));
            if (brandNameList.size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setBrandName(lst);
            } else {
                model.setBrandName(brandNameList);
            }
            if (productTypeList.size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setProductType(lst);
            } else {
                model.setProductType(productTypeList);
            }
            if (sizeTypeList.size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setSizeType(lst);
            } else {
                model.setSizeType(sizeTypeList);
            }
            cmsBtImageGroupDao.update(model);
        }
    }

    /**
     * 逻辑删除ImageGroupInfo
     */
    public void logicDelete(String imageGroupId) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            model.setActive(0);
            cmsBtImageGroupDao.update(model);
        }
    }

    /**
     * 根据检索条件取得ImageGroupInfo
     */
    public List<CmsBtImageGroupModel> getList(String channelId, List<Integer> platFormChangeList, String imageType, String beginModified,
                                              String endModified, List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(channelId, platFormChangeList, imageType, beginModified,
                endModified, brandNameList, productTypeList, sizeTypeList));
        return cmsBtImageGroupDao.select(queryObject);
    }

    /**
     * 根据imageGroupId取得ImageGroupInfo
     */
    public CmsBtImageGroupModel getImageGroupModel(String imageGroupId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageGroupId\":" + imageGroupId + "}");
        return cmsBtImageGroupDao.selectOneWithQuery(queryObject);
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    private String getSearchQuery(String channelId, List<Integer> platFormChangeList, String imageType, String beginModified,
                                  String endModified, List brandNameList, List productTypeList, List sizeTypeList) {
        StringBuilder result = new StringBuilder();

        // 获取Platform
        if (platFormChangeList.size() > 0) {
            Integer[] platFormArray = platFormChangeList.toArray(new Integer[platFormChangeList.size()]);
            result.append(MongoUtils.splicingValue("cartId", platFormArray));
            result.append(",");
        }

        // Image Type
        if (!StringUtils.isEmpty(imageType)) {
            result.append(MongoUtils.splicingValue("imageType", Integer.parseInt(imageType)));
            result.append(",");
        }

        // Update Time
        if (!StringUtils.isEmpty(beginModified) || !StringUtils.isEmpty(endModified)) {
            result.append("\"modified\":{" );
            // 获取Update Time Start
            if (!StringUtils.isEmpty(beginModified)) {
                result.append(MongoUtils.splicingValue("$gte", beginModified + " 00.00.00"));
            }
            // 获取Update Time End
            if (!StringUtils.isEmpty(endModified)) {
                if (!StringUtils.isEmpty(beginModified)) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", endModified + " 23.59.59"));
            }
            result.append("},");
        }

        // brandName
        if (brandNameList.size() > 0) {
            // 带上"All"
            brandNameList.add("All");
            result.append(MongoUtils.splicingValue("brandName", brandNameList.toArray(new String[brandNameList.size()])));
            result.append(",");
        }

        // productType
        if (productTypeList.size() > 0) {
            // 带上"All"
            productTypeList.add("All");
            result.append(MongoUtils.splicingValue("productType", productTypeList.toArray(new String[productTypeList.size()])));
            result.append(",");
        }

        // sizeType
        if (sizeTypeList.size() > 0) {
            // 带上"All"
            sizeTypeList.add("All");
            result.append(MongoUtils.splicingValue("sizeType", sizeTypeList.toArray(new String[sizeTypeList.size()])));
            result.append(",");
        }

        // channelId
        result.append(MongoUtils.splicingValue("channelId", channelId));
        result.append(",");

        // active
        result.append(MongoUtils.splicingValue("active", 1));

        return "{" + result.toString() + "}";
    }
}
