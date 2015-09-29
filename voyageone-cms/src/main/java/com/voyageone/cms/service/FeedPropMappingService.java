package com.voyageone.cms.service;

import com.voyageone.cms.feed.OperationBean;
import com.voyageone.cms.formbean.FeedMappingProp;
import com.voyageone.cms.modelbean.*;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.enums.CmsFieldEnum.CmsModelEnum;

import java.util.List;

/**
 * 第三方品牌数据，和主数据，进行属性和值的匹配
 * <p>
 * Created by Jonas on 9/1/15.
 */
public interface FeedPropMappingService {

    /**
     * 获取目录的完整目录名称
     *
     * @param categoryId 目录主键
     * @return 完整的目录名称
     */
    String getCategoryPath(int categoryId);

    /**
     * 获取目录下所有的属性
     *
     * @param params     带有目录主键的 DataTable 请求参数
     * @param channel_id 当前渠道
     * @return 返回给 DataTable 的数据
     */
    DtResponse<List<FeedMappingProp>> getProps(DtRequest<Integer> params, String channel_id);

    /**
     * 获取一个主数据属性下的所有值映射
     *
     * @param prop_id    主数据属性
     * @param channel_id 当前渠道
     * @return 所有映射
     */
    List<FeedPropMapping> getPropMappings(int prop_id, String channel_id);

    /**
     * 获取可绑定的 cms 属性
     *
     * @return CmsModelEnum 的集合
     */
    List<CmsModelEnum> getCmsProps();

    /**
     * 获取第三方品牌的属性
     *
     * @param channel_id 渠道
     * @return FeedConfig 集合
     */
    List<FeedConfig> getFeedProps(String channel_id);

    /**
     * 获取第三方品牌属性的值
     *
     * @param attr_name  属性名
     * @param channel_id 渠道
     * @return FeedValue 集合
     */
    List<FeedValue> getFeedValues(String attr_name, String channel_id);

    /**
     * 可用的品牌属性比较操作
     *
     * @return Operation 集合
     */
    List<OperationBean> getConditionOperations();

    /**
     * 获取主数据的可选项
     *
     * @param prop_id 主数据属性
     * @return PropertyOption 集合
     */
    List<PropertyOption> getPropOptions(long prop_id);

    /**
     * 更新主数据属性的可忽略属性
     *
     * @param prop 主数据属性
     * @param user 当前用户
     * @return 影响的行数
     */
    int updateIgnore(FeedMappingProp prop, UserSessionBean user);

    /**
     * 新增一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @param user    当前用户
     * @return 更新的实例
     */
    FeedPropMapping addMapping(FeedPropMapping mapping, UserSessionBean user);

    /**
     * 修改一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @param user    当前用户
     * @return 更新的实例
     */
    FeedPropMapping updateMapping(FeedPropMapping mapping, UserSessionBean user);

    /**
     * 删除一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @return 影响行数
     */
    int deleteMapping(FeedPropMapping mapping, UserSessionBean user);

    /**
     * 查找这个属性的默认设定值。
     *
     * @param prop       主数据属性
     * @param channel_id 渠道
     * @return 属性的默认设定值
     */
    String getDefaultValue(FeedMappingProp prop, String channel_id);
}
