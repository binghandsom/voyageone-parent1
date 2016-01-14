package com.voyageone.cms.service;

import com.voyageone.cms.modelbean.CustomWord;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.bean.DictMasterPropBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.modelbean.DictWordBean;

import java.util.List;

/**
 * 字典（表达式）的增删改查管理
 * <p>
 * Created by Jonas on 9/11/15.
 */
public interface DictManageService {

    /**
     * 获取渠道的所有定义好的字典
     *
     * @param channel_id 渠道
     * @return 字典集合
     */
    DtResponse<List<DictWordBean>> dtGetAllDict(DtRequest request, String channel_id);

    /**
     * 添加一个字典项
     *
     * @param dictWordBean 字典项
     * @param user         当前用户
     * @return 更新的字典项
     */
    DictWordBean addDict(DictWordBean dictWordBean, UserSessionBean user);

    /**
     * 更新一个字典项
     *
     * @param dictWordBean 字典项
     * @param user         当前用户
     * @return 更新结果
     */
    int setDict(DictWordBean dictWordBean, UserSessionBean user);

    /**
     * 删除一个字典项
     *
     * @param dictWordBean 字典项
     * @return 更新结果
     */
    int delDict(DictWordBean dictWordBean);

    /**
     * 获取可用的 CMS 属性
     *
     * @return CmsModelEnum 集合
     */
    List<CmsFieldEnum.CmsModelEnum> getCmsValues();

    /**
     * 获取可用的主数据属性
     *
     * @return 属性集合
     */
    List<DictMasterPropBean> getMasterProps();

    /**
     * 获取可用的自定义逻辑
     *
     * @return 逻辑集合
     */
    List<CustomWord> getCustomWords();

    /**
     * 获取渠道所有字典的简单信息
     *
     * @param channel_id 当前所选渠道
     * @return 字典集合
     */
    List<DictWordBean> getDictList(String channel_id);
}
