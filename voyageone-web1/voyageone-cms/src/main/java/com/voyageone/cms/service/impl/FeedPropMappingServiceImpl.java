package com.voyageone.cms.service.impl;

import com.voyageone.base.BaseAppService;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.dao.FeedPropMappingDao;
import com.voyageone.cms.feed.Condition;
import com.voyageone.cms.feed.Operation;
import com.voyageone.cms.formbean.FeedMappingProp;
import com.voyageone.cms.modelbean.*;
import com.voyageone.cms.service.FeedPropMappingService;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.ajax.dt.DtSearch;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.voyageone.cms.CmsMsgConstants.FeedPropMappingMsg.*;
import static com.voyageone.core.MessageConstants.ComMsg.NOT_FOUND_CHANNEL;
import static com.voyageone.core.MessageConstants.ComMsg.UPDATE_BY_OTHER;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * 第三方品牌数据，和主数据，进行属性和值的匹配
 * <p>
 * Created by Jonas on 9/1/15.
 */
@Service
public class FeedPropMappingServiceImpl extends BaseAppService implements FeedPropMappingService {

    @Autowired
    private FeedPropMappingDao feedPropMappingDao;

    /**
     * 获取目录的完整目录名称
     *
     * @param categoryId 目录主键
     * @return 完整的目录名称
     */
    @Override
    public String getCategoryPath(int categoryId) {
        return feedPropMappingDao.selectCategoryPath(categoryId);
    }

    /**
     * 获取目录下所有的属性
     *
     * @param params     带有目录主键的 DataTable 请求参数
     * @param channel_id 当前渠道
     * @return 返回给 DataTable 的数据
     */
    @Override
    public DtResponse<List<FeedMappingProp>> getProps(DtRequest<Integer> params, String channel_id) {

        $info("准备为属性匹配设定获取属性");

        Integer categoryId = params.getParam();

        $info(1, "类目:%s", categoryId);

        if (categoryId == null) throw new BusinessException(NO_PARAM);

        Channel channel = Channel.valueOfId(channel_id);

        $info(1, "渠道:%s", channel);

        if (channel == null) throw new BusinessException(NOT_FOUND_CHANNEL);

        // 获取过滤参数
        String byRequired = params.getColumns().get(3).getSearch().getValue();
        String byIgnored = params.getColumns().get(4).getSearch().getValue();

        DtSearch search = params.getSearch();

        // 获取搜索参数
        String value = search.getValue();

        $info(1, "必填:%s", byRequired);
        $info(1, "忽略:%s", byIgnored);
        $info(1, "模糊:%s", value);

        // 抽取数据
        List<FeedMappingProp> masterProperties = feedPropMappingDao.selectProps(value, categoryId, channel.getId(),
                params.getStart(), params.getLength(), byIgnored, byRequired);

        $info("属性匹配已完成属性获取");

        // 抽取不分页的总数量
        int total = feedPropMappingDao.selectPropsCount(value, categoryId, channel.getId(), byIgnored, byRequired);

        // 创建返回结果
        DtResponse<List<FeedMappingProp>> response = new DtResponse<>();
        response.setData(masterProperties);
        response.setRecordsTotal(total);
        response.setRecordsFiltered(total);
        response.setDraw(params.getDraw());

        return response;
    }

    /**
     * 获取一个主数据属性下的所有值映射
     *
     * @param prop_id    主数据属性
     * @param channel_id 当前渠道
     * @return 所有映射
     */
    @Override
    public List<FeedPropMapping> getPropMappings(int prop_id, String channel_id) {
        return feedPropMappingDao.selectMappings(prop_id, channel_id);
    }

    /**
     * 获取可绑定的 cms 属性
     *
     * @return CmsModelEnum 的集合
     */
    @Override
    public List<CmsFieldEnum.CmsModelEnum> getCmsProps() {
        return Arrays.asList(CmsFieldEnum.CmsModelEnum.values());
    }

    /**
     * 获取第三方品牌的属性
     *
     * @param channel_id 渠道
     * @return FeedConfig 集合
     */
    @Override
    public List<FeedConfig> getFeedProps(String channel_id) {
        return feedPropMappingDao.selectFeedProps(channel_id);
    }

    /**
     * 获取第三方品牌属性的值
     *
     * @param attr_name  属性名
     * @param channel_id 渠道
     * @return FeedValue 集合
     */
    @Override
    public List<FeedValue> getFeedValues(String attr_name, String channel_id) {
        return feedPropMappingDao.selectFeedValues(attr_name, channel_id);
    }

    /**
     * 可用的品牌属性比较操作
     *
     * @return Operation 集合
     */
    @Override
    public Operation[] getConditionOperations() {
        return Operation.values();
    }

    /**
     * 获取主数据的可选项
     *
     * @param prop_id 主数据属性
     * @return PropertyOption 集合
     */
    @Override
    public List<PropertyOption> getPropOptions(long prop_id) {
        return feedPropMappingDao.selectPropOptions(prop_id);
    }

    /**
     * 更新主数据属性的可忽略属性
     * 注意!! 根据数组的数量进行判断,如果数组只有一个元素,则为切换状态.多个元素则视为统一变更为忽略状态
     *
     * @param props 主数据属性
     * @param user  当前用户
     * @return 影响的行数
     */
    @Override
    public int updateIgnore(FeedMappingProp[] props, UserSessionBean user) {

        if (props == null || props.length < 1)
            return 0;

        String channel_id = user.getSelChannel();
        String userName = user.getUserName();

        List<FeedMappingProp> propList = Arrays.asList(props);

        // 一次性获取这些属性的忽略属性
        // 获取的这些对象只包含 prop_id 和 is_ignore
        List<FeedMappingProp> propsWithIgnore = feedPropMappingDao.selectIgnoreValue(propList, channel_id);

        int count;

        // 根据数组的数量进行判断,如果数组只有一个元素,则为切换状态.多个元素则视为统一变更为忽略状态
        if (props.length == 1) {

            FeedMappingProp prop = propList.get(0);

            // 查询不到则为插入
            // 单值处理时, is_ignore 会由前台切换并传回到这里.所以不用在这里具体处理 is_ignore
            if (propsWithIgnore.isEmpty()) {
                count = feedPropMappingDao.insertIgnoreValue(prop, channel_id, userName);
            } else {
                count = feedPropMappingDao.updateIgnoreValue(prop, channel_id, userName);
            }

            completeCategory(prop.getCategory_id(), channel_id, user);

            return count;
        }

        // 批量处理
        if (propsWithIgnore.isEmpty()) {
            // 如果是空,则批量插入
            count = feedPropMappingDao.insertIgnoreValues(propList, channel_id, userName);
        } else if (propsWithIgnore.size() == props.length) {
            // 否则是批量更新
            count = feedPropMappingDao.updateIgnoreValues(propList, channel_id, userName);
        } else {
            // 区分开忽略是插入还是更新
            Map<Integer, List<FeedMappingProp>> map = Stream
                    .concat(propList.stream(), propsWithIgnore.stream())
                    .collect(groupingBy(FeedMappingProp::getProp_id, toList()));

            List<FeedMappingProp> inserting = map.entrySet().stream()
                    .filter(e -> e.getValue().size() == 1)
                    .map(e -> e.getValue().get(0))
                    .collect(toList());

            List<FeedMappingProp> updating = map.entrySet().stream()
                    .filter(e -> e.getValue().size() == 2)
                    .map(e -> e.getValue().get(0))
                    .collect(toList());

            count = feedPropMappingDao.insertIgnoreValues(inserting, channel_id, userName);
            count += feedPropMappingDao.updateIgnoreValues(updating, channel_id, userName);
        }

        completeCategory(propList.get(0).getCategory_id(), channel_id, user);

        return count;
    }

    /**
     * 新增一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @param user    当前用户
     * @return 更新的实例
     */
    @Override
    public FeedPropMapping addMapping(FeedPropMapping mapping, UserSessionBean user) {

        mapping.setChannel_id(user.getSelChannel());

        // 先检查提交内容
        // 检查失败会直接抛 BusinessException
        checkMapping(mapping);

        // 补全属性
        mapping.setModifier(user.getUserName());
        mapping.setCreater(user.getUserName());

        if (feedPropMappingDao.insertMapping(mapping) < 1)
            throw new BusinessException(UPDATE_BY_OTHER);

        // 检查目录是否完全匹配完成
        completeCategory(mapping.getMain_category_id(), mapping.getChannel_id(), user);

        return feedPropMappingDao.selectMappingByFinger(mapping);
    }

    /**
     * 修改一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @param user    当前用户
     * @return 更新的实例
     */
    @Override
    public FeedPropMapping updateMapping(FeedPropMapping mapping, UserSessionBean user) {

        checkMapping(mapping);

        mapping.setModifier(user.getUserName());

        if (feedPropMappingDao.updateMapping(mapping) < 1)
            throw new BusinessException(UPDATE_BY_OTHER);

        // 检查目录是否完全匹配完成
        completeCategory(mapping.getMain_category_id(), mapping.getChannel_id(), user);

        return feedPropMappingDao.selectMappingById(mapping.getId());
    }

    /**
     * 删除一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @return 影响行数
     */
    @Override
    public int deleteMapping(FeedPropMapping mapping, UserSessionBean user) {
        if (mapping == null)
            throw new BusinessException(NO_PARAM);

        int count = feedPropMappingDao.deleteMapping(mapping.getId());

        if (count == 1)
            // 检查目录是否完全匹配完成
            completeCategory(mapping.getMain_category_id(), mapping.getChannel_id(), user);

        return count;
    }

    /**
     * 查找这个属性的默认设定值。
     *
     * @param prop       主数据属性
     * @param channel_id 渠道
     * @return 属性的默认设定值
     */
    @Override
    public String getDefaultValue(FeedMappingProp prop, String channel_id) {
        if (prop == null)
            throw new BusinessException(NO_PARAM);

        return feedPropMappingDao.selectDefaultValue(prop, channel_id);
    }

    private void completeCategory(int category_id, String channel_id, UserSessionBean user) {
        // 先获取统计数量
        CategoryPropCount propCount = feedPropMappingDao.selectCountsByCategory(channel_id, category_id);

        // 获取目录记录
        ImsCategoryExtend categoryExtend = feedPropMappingDao.selectCategoryExtend(channel_id, category_id);

        // 如果两数量不相同，说明有属性还没有忽略或设置 mapping，并且没有默认值
        int isSetAttr = propCount.getProps() != propCount.getHasValue() ? 0 : 1;

        // 如果取出的 extend 没有值，则插入，否则更新
        if (categoryExtend != null) {
            categoryExtend.setIs_set_attr(isSetAttr);
            categoryExtend.setModifier(user.getUserName());
            feedPropMappingDao.isSetAttr(categoryExtend);
            return;
        }

        categoryExtend = new ImsCategoryExtend();
        categoryExtend.setIs_set_attr(isSetAttr);
        categoryExtend.setChannel_id(channel_id);
        categoryExtend.setMain_category_id(category_id);
        categoryExtend.setCreater(user.getUserName());
        categoryExtend.setModifier(user.getUserName());
        feedPropMappingDao.insertCategoryExtend(categoryExtend);
    }

    private void checkMapping(FeedPropMapping mapping) {
        if (mapping == null)
            throw new BusinessException(NO_PARAM);

        Channel channel = Channel.valueOfId(mapping.getChannel_id());

        if (channel == null)
            throw new BusinessException(NOT_FOUND_CHANNEL);

        if (mapping.getEType() == null)
            throw new BusinessException(ERR_MAPPING_TYPE);

        if (mapping.getProp_id() == 0)
            throw new BusinessException(NO_PARAM);

        String condition = mapping.getConditions();

        // 条件是可以为空的，但是如果不为空，则格式不能错
        if (!Condition.valid(condition))
            throw new BusinessException(ERR_CONDITION_FORMAT);

        if (StringUtils.isEmpty(mapping.getValue()))
            throw new BusinessException(NO_MAPPING_VALUE);
    }
}
