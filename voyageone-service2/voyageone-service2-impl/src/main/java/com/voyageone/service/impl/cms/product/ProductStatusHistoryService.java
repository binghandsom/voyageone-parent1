package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductStatusHistoryDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductStatusHistoryModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductStatusHistoryModel_History;
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
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductStatusHistoryDao productStatusHistoryDao;

    public List<MapModel> getPage(PageQueryParameters parameters) {

        Map<String, Object> param = parameters.getParameters();
        int pagSize = parameters.getPageRowCount();
        param.put("skip", (parameters.getPageIndex() - 1) * pagSize);
        param.put("pageRowCount", pagSize);
        param.put("orderBy", parameters.getOrderBy());
        List<MapModel> list = productStatusHistoryDao.selectPage(param);

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
        return productStatusHistoryDao.selectCount(parameters.getParameters());
    }

    @VOTransactional
    public void insertList(String channelId, List<String> codes, String status, int cartId, EnumProductOperationType enumProductOperationType, String comment, String modifier) {
        if (codes == null || codes.isEmpty()) {
            return;
        }
        Date sysTs = new Date();
        for (String code : codes) {
            CmsBtProductStatusHistoryModel productStatusHistory = get(channelId, code, status, cartId, enumProductOperationType, comment, modifier, sysTs);
            productStatusHistoryDao.insert(productStatusHistory);
        }
    }

    @VOTransactional
    public void insertList(String channelId, List<String> codes, int cartId, EnumProductOperationType operationType, String comment, String modifier) {
        if (codes == null || codes.isEmpty()) {
            return;
        }
        List<CmsBtProductStatusHistoryModel> list = new ArrayList<>();

        JongoQuery query = new JongoQuery();
        String prodSts = null;
        Date sysTs = new Date();
        for (String code : codes) {
            // 先取得商品状态
            if (cartId > 0) {
                // 必须指定平台才保存商品状态
                query.setQuery("{'common.fields.code':#}");
                query.setParameters(code);
                query.setProjection("{'platforms.P" + cartId + ".status':1}");
                CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(query, channelId);
                if (prodObj == null) {
                    $warn("ProductStatusHistoryService.insertList 指定的商品不存在 code=%s, channelId=%s", code, channelId);
                    continue;
                }
                prodSts = StringUtils.trimToNull(prodObj.getPlatformNotNull(cartId).getStatus());
            }
            if (prodSts == null) {
                prodSts = "0";
            }

            CmsBtProductStatusHistoryModel productStatusHistory = get(channelId, code, prodSts, cartId, operationType, comment, modifier, sysTs);
            productStatusHistoryDao.insert(productStatusHistory);
        }
    }

    public void insert(String channelId, String code, String status, int cartId, EnumProductOperationType enumProductOperationType, String Comment, String modifier) {
        Date sysTs = new Date();
        CmsBtProductStatusHistoryModel productStatusHistory = get(channelId, code, status, cartId, enumProductOperationType, Comment, modifier, sysTs);
        productStatusHistoryDao.insert(productStatusHistory);
    }

    private CmsBtProductStatusHistoryModel get(String channelId, String code, String status, int cartId, EnumProductOperationType enumProductOperationType, String comment, String modifier, Date sysTs) {
        CmsBtProductStatusHistoryModel statusHistoryModel = new CmsBtProductStatusHistoryModel();
        statusHistoryModel.setChannelId(channelId);
        statusHistoryModel.setCartId(cartId);
        statusHistoryModel.setCode(code);

        CmsBtProductStatusHistoryModel_History history = new CmsBtProductStatusHistoryModel_History();
        history.setOperationType(enumProductOperationType.getId());
        history.setStatus(status);
        history.setComment(comment);
        history.setCreater(modifier);
        history.setModifier(modifier);
        history.setCreated(sysTs);
        history.setModified(sysTs);
        statusHistoryModel.getList().add(history);
        return statusHistoryModel;
    }
}
