package com.voyageone.service.impl.cms.prices;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsMtFeeCommissionDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeeCommissionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 查询, 计算佣金率
 * <p>
 * Created by jonas on 8/4/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
public class CmsMtFeeCommissionService extends BaseService {

    public static final String COMMISSION_TYPE_VOYAGE_ONE = "VO";
    public static final String COMMISSION_TYPE_PLATFORM = "PF";
    public static final String COMMISSION_TYPE_RETURN = "RT";

    private static final List<String> COMMISSION_TYPE_LIST = new ArrayList<String>() {
        {
            add(COMMISSION_TYPE_VOYAGE_ONE);
            add(COMMISSION_TYPE_PLATFORM);
            add(COMMISSION_TYPE_RETURN);
        }
    };

    private static final String FIELD_COMMISSION_TYPE = "commissionType";
    private static final String FIELD_CHANNELID = "channelId";
    private static final String FIELD_PLATFORMID = "platformId";
    private static final String FIELD_CARTID = "cartId";
    private static final String FIELD_CATID = "catId";

    /**
     * 佣金查询的条件优先级
     */
    private static final String[][] PRIORITY = {
            {FIELD_CHANNELID, FIELD_CARTID, FIELD_CATID},
            {FIELD_CHANNELID, FIELD_PLATFORMID, FIELD_CATID},
            {FIELD_CHANNELID, FIELD_CARTID},
            {FIELD_CHANNELID, FIELD_PLATFORMID},
            {FIELD_CHANNELID},
            {FIELD_PLATFORMID, FIELD_CARTID},
            {FIELD_PLATFORMID},
            {} // 最低优先级, 表示仅包含 commission type
    };

    private final CmsMtFeeCommissionDao feeCommissionDao;

    @Autowired
    public CmsMtFeeCommissionService(CmsMtFeeCommissionDao feeCommissionDao) {
        this.feeCommissionDao = feeCommissionDao;
    }

    /**
     * 基于参数构造器, 逐级查询佣金比例
     *
     * @param commissionQueryBuilder 包含参数的参数构造器
     * @return 佣金数据模型
     */
    private CmsMtFeeCommissionModel getCommission(CommissionQueryBuilder commissionQueryBuilder) {

        // 判断佣金类型, 如果佣金类型不对, 则必然查不出数据, 所以直接返回
        if (!COMMISSION_TYPE_LIST.contains(commissionQueryBuilder.getCommissionType()))
            return null;

        for (String[] paramKeys: PRIORITY) {

            Map<String, Object> queryMap = commissionQueryBuilder.getQueryMap(paramKeys);

            if (queryMap == null)
                continue;

            CmsMtFeeCommissionModel feeCommissionModel = feeCommissionDao.selectOne(queryMap);

            if (feeCommissionModel != null)
                return feeCommissionModel;
        }

        return null;
    }

    /**
     * 逐级查询参数构建帮助器
     * <p>
     * 外部使用时, 使用链式进行参数填补
     * <p>
     * 内部使用获取参数时
     * <p>
     * 首次要使用 {@code getQueryMap()} 进行获取, 如果不行, 可以继续尝试使用 {@code getLowerPriorityQueryMap()}
     * <p>
     * 首次直接使用 {@code getLowerPriorityQueryMap()} 时, 相当于是从顶级优先级的下一次开始, 也是允许的
     */
    class CommissionQueryBuilder {

        private Map<String, Object> queryMap = new HashMap<>();

        CommissionQueryBuilder withChannel(String channelId) {
            queryMap.put(FIELD_CHANNELID, channelId);
            return this;
        }

        CommissionQueryBuilder withPlatform(Integer platformId) {
            queryMap.put(FIELD_PLATFORMID, platformId);
            return this;
        }

        CommissionQueryBuilder withCart(Integer cartId) {
            queryMap.put(FIELD_CARTID, cartId);
            return this;
        }

        CommissionQueryBuilder withCategory(String categoryId) {
            queryMap.put(FIELD_CATID, categoryId);
            return this;
        }

        private String getCommissionType() {
            if (!queryMap.containsKey(FIELD_COMMISSION_TYPE))
                return null;
            return (String) queryMap.get(FIELD_COMMISSION_TYPE);
        }

        private void setCommissionType(String commissionType) {
            queryMap.put(FIELD_COMMISSION_TYPE, commissionType);
        }

        Double getCommission(String commissionType) {

            setCommissionType(commissionType);

            CmsMtFeeCommissionModel commission = CmsMtFeeCommissionService.this.getCommission(this);

            if (commission == null)
                return null;

            return commission.getCommissonRate();
        }

        private Map<String, Object> getQueryMap(String[] paramKeys) {

            Map<String, Object> _queryMap = new HashMap<>();

            // 此处先初始化好相应的默认值
            // 这样防止出现 channelId = 1, commissionType = pf
            // 查询时匹配到 channelId = 1, commissionType = pf, 并且 cartId = 23
            // 类似的情况出现
            // 也就是要进行绝对精确的查询
            _queryMap.put(FIELD_COMMISSION_TYPE, getCommissionType());
            _queryMap.put(FIELD_CHANNELID, "");
            _queryMap.put(FIELD_PLATFORMID, 0);
            _queryMap.put(FIELD_CARTID, 0);
            _queryMap.put(FIELD_CATID, "");

            for (String fieldName : paramKeys) {
                // 不包含 KEY
                // 则说明不匹配
                if (!queryMap.containsKey(fieldName))
                    return null;

                // 有 Key 但没值, 不匹配
                Object value = queryMap.get(fieldName);
                if (value == null)
                    return null;

                // 有值, 但是空字符串, 也不匹配
                if (value instanceof String && StringUtils.isEmpty((String) value))
                    return null;

                _queryMap.put(fieldName, queryMap.get(fieldName));
            }

            return _queryMap;
        }
    }
}
