package com.voyageone.service.impl.cms.prices;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsMtFeeCommissionDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeeCommissionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            {FIELD_CARTID},
            {FIELD_PLATFORMID},
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

        // 先进行第一次匹配尝试
        Map<String, Object> queryMap = commissionQueryBuilder.getQueryMap();

        while (queryMap != null) {

            CmsMtFeeCommissionModel feeCommissionModel = feeCommissionDao.selectOne(queryMap);

            // 如果有结果, 就返回
            if (feeCommissionModel != null)
                return feeCommissionModel;

            // 否则获取更低优先级的条件
            queryMap = commissionQueryBuilder.getLowerPriorityQueryMap();
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
    public class CommissionQueryBuilder {

        private Map<String, Object> queryMap = new HashMap<>();

        private int currentLevel = 0;

        private int lastLevel = -1;

        public CommissionQueryBuilder withChannel(String channelId) {
            queryMap.put(FIELD_CHANNELID, channelId);
            return this;
        }

        public CommissionQueryBuilder withPlatform(Integer platformId) {
            queryMap.put(FIELD_PLATFORMID, platformId);
            return this;
        }

        public CommissionQueryBuilder withCart(Integer cartId) {
            queryMap.put(FIELD_CARTID, cartId);
            return this;
        }

        public CommissionQueryBuilder withCategory(String categoryId) {
            queryMap.put(FIELD_CATID, categoryId);
            return this;
        }

        public CommissionQueryBuilder resetPriority() {
            this.currentLevel = 0;
            return this;
        }

        public Double getCommission(String commissionType) {

            setCommissionType(commissionType);

            CmsMtFeeCommissionModel commission = CmsMtFeeCommissionService.this.getCommission(this);

            if (commission == null)
                return null;

            return commission.getCommissonRate();
        }

        private String getCommissionType() {
            if (!queryMap.containsKey(FIELD_COMMISSION_TYPE))
                return null;
            return (String) queryMap.get(FIELD_COMMISSION_TYPE);
        }

        private void setCommissionType(String commissionType) {

            if (StringUtils.isEmpty(commissionType))
                return;

            String lastType = getCommissionType();

            if (commissionType.equals(lastType))
                return;

            // 切换当前类型, 说明要重新查询其他类型的佣金比例了
            // 所以需要重置优先级
            // 让查询重新查找
            this.currentLevel = 0;

            queryMap.put(FIELD_COMMISSION_TYPE, commissionType);
        }

        /**
         * 获取当前优先级更低优先级的参数字典
         *
         * @return 查询参数字典
         */
        private Map<String, Object> getLowerPriorityQueryMap() {

            // 移动优先级
            this.currentLevel++;

            // 如果优先级已经超越边界
            // 则尝试回归
            if (this.currentLevel >= PRIORITY.length) {
                // 如果在这之前没有调用过 getQueryMap 的话, 则 currentLevel 必然是 0
                // 所以直接调用该方法时, 此处 currentLevel 必然是 1
                // 所以也是不会超越边界的
                // 这里也就不用担心 lastLevel 初始是 -1 可能产生的问题
                this.currentLevel = this.lastLevel;
                return null;
            }

            // 否则从当前优先级尝试计算
            return getQueryMap();
        }

        /**
         * 获取当前优先级的参数字典
         *
         * @return 查询参数字典
         */
        private Map<String, Object> getQueryMap() {

            // 尝试计算当前优先级
            // 并获取优先级内容
            String[] priority = getPriority();

            if (priority == null)
                return null;

            // 根据优先级内容, 克隆新的参数配置返回
            return getQueryMap(priority);
        }

        /**
         * 根据指定的优先级内容, 创建并返回新的参数字典
         *
         * @param keyArray 包含字段名的优先级实例
         * @return 查询参数字典
         */
        private Map<String, Object> getQueryMap(String[] keyArray) {

            Map<String, Object> queryMap = new HashMap<>();

            for (String key : keyArray) {
                queryMap.put(key, this.queryMap.get(key));
            }

            return queryMap;
        }

        /**
         * 计算匹配优先级, 并记录优先级变动, 如果匹配成功, 就返回优先级内容
         *
         * @return 包含字段名的优先级实例
         */
        private String[] getPriority() {

            // 如果当前优先级和上一次使用的优先级相同
            // 则直接返回优先级
            if (this.currentLevel == this.lastLevel)
                return PRIORITY[this.currentLevel];

            this.lastLevel = this.currentLevel;

            String[] currentPriority = null;

            // 从当前优先级开始查找
            // 尝试使用优先级预定义字段匹配 MAP 中已存字段
            // 如果匹配成功
            // 则返回, 并记录当前优先级
            for (int i = this.currentLevel; i < PRIORITY.length; i++) {

                currentPriority = PRIORITY[i];

                boolean match = true;

                for (String fieldName : currentPriority) {
                    // 不包含 KEY
                    // 则说明不匹配
                    if (!this.queryMap.containsKey(fieldName)) {
                        match = false;
                        break;
                    }

                    // 有 Key 但没值, 不匹配
                    Object value = this.queryMap.get(fieldName);
                    if (value == null) {
                        match = false;
                        break;
                    }

                    // 有值, 但是空字符串, 也不匹配
                    if (value instanceof String && StringUtils.isEmpty((String) value)) {
                        match = false;
                        break;
                    }
                }

                if (!match)
                    continue;

                this.currentLevel = i;
            }

            // 如果这次查找失败
            // 没有找到匹配的内容
            // 则重置优先级到 0
            // 促使下次查找从头开始
            this.currentLevel = 0;

            return currentPriority;
        }
    }
}
