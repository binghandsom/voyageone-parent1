package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.CmsBtProductStatusHistoryDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtProductStatusHistoryDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtProductStatusHistoryModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * @author sunpeitao
 * @version 2.3.0
 * @since 2.3.0
 */
@Service
public class ProductStatusHistoryService extends BaseService {
    @Autowired
    private CmsBtProductStatusHistoryDao dao;
    @Autowired
    private CmsBtProductStatusHistoryDaoExt daoExt;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    public List<MapModel> getPage(PageQueryParameters parameters) {

        List<MapModel> list = daoExt.selectPage(parameters.getSqlMapParameter());

        List<TypeBean> typeBeanList = TypeConfigEnums.MastType.productStatus.getList(Constants.LANGUAGE.CN);

        Map<String, String> typeMap = typeBeanList.stream().collect(toMap(TypeBean::getValue, TypeBean::getName));

        list.forEach(mapModel -> {

            String status = String.valueOf(mapModel.get("status"));

            Date modified = (Date) mapModel.get("modified");

            modified = DateTimeUtilBeijing.toBeiJingDate(modified);

            mapModel.put("modified", modified);

            String statusCn = typeMap.get(status);

            mapModel.put("statusCn", statusCn);

            mapModel.put("operationTypeName", EnumProductOperationType.getNameById(mapModel.get("operationType")));
        });

        return list;
    }

    public long getCount(PageQueryParameters parameters) {
        return daoExt.selectCount(parameters.getSqlMapParameter());
    }

    @VOTransactional
    public void insertList(String channelId, List<String> codes, String status, int cartId, EnumProductOperationType enumProductOperationType, String comment, String modifier) {
        List<CmsBtProductStatusHistoryModel> list = new ArrayList<>();
        CmsBtProductStatusHistoryModel productStatusHistory;
        for (String code : codes) {
            productStatusHistory = get(channelId, code, status, cartId, enumProductOperationType, comment, modifier);
            list.add(productStatusHistory);
        }
        List<List<CmsBtProductStatusHistoryModel>> pageList = ListUtils.getPageList(list, 100);//分隔数据源 每页100
        pageList.forEach(daoExt::insertList);
    }

    @VOTransactional
    public void insertList(String channelId, List<String> codes, int cartId, EnumProductOperationType operationType, String comment, String modifier) {
        List<CmsBtProductStatusHistoryModel> list = new ArrayList<>();
        CmsBtProductStatusHistoryModel productStatusHistory;

        JomgoQuery query = new JomgoQuery();
        CmsBtProductModel prodObj = null;
        String prodSts = null;
        for (String code : codes) {
            // 先取得商品状态
            if (cartId > 0) {
                // 必须指定平台才保存商品状态
                query.setQuery("{'common.fields.code':#}");
                query.setParameters(code);
                query.setProjection("{'platforms.P" + cartId + ".status':1}");
                prodObj = cmsBtProductDao.selectOneWithQuery(query, channelId);
                if (prodObj == null) {
                    $warn("ProductStatusHistoryService.insertList 指定的商品不存在 code=%s, channelId=%s", code, channelId);
                    continue;
                }
                prodSts = StringUtils.trimToNull(prodObj.getPlatformNotNull(cartId).getStatus());
            }
            if (prodSts == null) {
                prodSts = "0";
            }

            productStatusHistory = get(channelId, code, prodSts, cartId, operationType, comment, modifier);
            list.add(productStatusHistory);
        }
        List<List<CmsBtProductStatusHistoryModel>> pageList = ListUtils.getPageList(list, 100);//分隔数据源 每页100
        pageList.forEach(daoExt::insertList);
    }

    public void insert(String channelId, String code, String status, int cartId, EnumProductOperationType enumProductOperationType, String Comment, String modifier) {
        CmsBtProductStatusHistoryModel productStatusHistory = get(channelId, code, status, cartId, enumProductOperationType, Comment, modifier);
        dao.insert(productStatusHistory);
    }

    private CmsBtProductStatusHistoryModel get(String channelId, String code, String status, int cartId, EnumProductOperationType enumProductOperationType, String comment, String modifier) {
        CmsBtProductStatusHistoryModel productStatusHistory = new CmsBtProductStatusHistoryModel();
        productStatusHistory.setChannelId(channelId);
        productStatusHistory.setCode(code);
        productStatusHistory.setStatus(status);
        productStatusHistory.setCartId(cartId);
        productStatusHistory.setOperationType(enumProductOperationType.getId());
        productStatusHistory.setModifier(modifier);
        productStatusHistory.setCreater(modifier);
        productStatusHistory.setCreated(new Date());
        productStatusHistory.setComment(comment);
        return productStatusHistory;
    }
}
