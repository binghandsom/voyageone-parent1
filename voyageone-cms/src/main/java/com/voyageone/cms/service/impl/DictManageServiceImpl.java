package com.voyageone.cms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.dao.CustomWordDao;
import com.voyageone.cms.dao.DictDao;
import com.voyageone.cms.modelbean.CustomWord;
import com.voyageone.cms.service.DictManageService;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.bean.DictMasterPropBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.enums.DictMasterProp;
import com.voyageone.ims.modelbean.DictWordBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.voyageone.core.MessageConstants.ComMsg.NOT_FOUND_CHANNEL;
import static com.voyageone.core.MessageConstants.ComMsg.UPDATE_BY_OTHER;
import static com.voyageone.cms.CmsMsgConstants.DictManage.*;

/**
 * 字典（表达式）的增删改查管理
 * <p>
 * Created by Jonas on 9/11/15.
 */
@Service
public class DictManageServiceImpl implements DictManageService {

    @Autowired
    private DictDao dictDao;

    @Autowired
    private CustomWordDao customWordDao;

    /**
     * 获取渠道的所有定义好的字典
     *
     * @param request    dt 请求参数
     * @param channel_id 渠道
     * @return 字典集合
     */
    @Override
    public DtResponse<List<DictWordBean>> dtGetAllDict(DtRequest request, String channel_id) {

        int start = request.getStart();
        int length = request.getLength();

        List<DictWordBean> dictWordBeans = dictDao.selectAll(channel_id, start, length);

        int count = dictDao.selectAllCount(channel_id);

        DtResponse<List<DictWordBean>> response = new DtResponse<>();
        response.setDraw(request.getDraw());
        response.setData(dictWordBeans);
        response.setRecordsFiltered(count);
        response.setRecordsTotal(count);

        return response;
    }

    /**
     * 添加一个字典项
     *
     * @param dictWordBean 字典项
     * @param user         当前用户
     * @return 更新的字典项
     */
    @Override
    public DictWordBean addDict(DictWordBean dictWordBean, UserSessionBean user) {

        dictWordBean.setOrder_channel_id(user.getSelChannel());

        checkDict(dictWordBean);

        dictWordBean.setCreater(user.getUserName());
        dictWordBean.setModifier(user.getUserName());

        if (dictDao.insertDict(dictWordBean) < 1)
            throw new BusinessException(UPDATE_BY_OTHER);

        List<DictWordBean> byName = dictDao.selectByName(dictWordBean);
        return byName.get(0);
    }

    /**
     * 更新一个字典项
     *
     * @param dictWordBean 字典项
     * @param user         当前用户
     * @return 更新结果
     */
    @Override
    public int setDict(DictWordBean dictWordBean, UserSessionBean user) {
        checkDict(dictWordBean);

        dictWordBean.setModifier(user.getUserName());

        return dictDao.updateDict(dictWordBean);
    }

    /**
     * 删除一个字典项
     *
     * @param dictWordBean 字典项
     * @return 更新结果
     */
    @Override
    public int delDict(DictWordBean dictWordBean) {

        return dictDao.deleteDict(dictWordBean);
    }

    /**
     * 获取可用的 CMS 属性
     *
     * @return CmsModelEnum 集合
     */
    @Override
    public List<CmsFieldEnum.CmsModelEnum> getCmsValues() {
        return Arrays.asList(CmsFieldEnum.CmsModelEnum.values());
    }

    /**
     * 获取可用的主数据属性
     *
     * @return 属性集合
     */
    @Override
    public List<DictMasterPropBean> getMasterProps() {

        List<DictMasterPropBean> beans = new ArrayList<>();

        for (DictMasterProp prop : DictMasterProp.values()) {
            beans.add(prop.toBean());
        }

        return beans;
    }

    /**
     * 获取可用的自定义逻辑
     *
     * @return 逻辑集合
     */
    @Override
    public List<CustomWord> getCustomWords() {
        return customWordDao.selectWithParam();
    }

    /**
     * 获取渠道所有字典的简单信息
     *
     * @param channel_id 当前所选渠道
     * @return 字典集合
     */
    @Override
    public List<DictWordBean> getDictList(String channel_id) {
        return dictDao.selectSimpleDict(channel_id);
    }

    private void checkDict(DictWordBean dictWordBean) {

        Channel channel = Channel.valueOfId(dictWordBean.getOrder_channel_id());

        if (channel == null)
            throw new BusinessException(NOT_FOUND_CHANNEL);

        if (StringUtils.isEmpty(dictWordBean.getName()))
            throw new BusinessException(NO_NAME);

        if (StringUtils.isEmpty(dictWordBean.getValue()))
            throw new BusinessException(NO_EXPRESSION);

        if (dictDao.selectByName(dictWordBean).size() > 1)
            throw new BusinessException(DUP_NAME);
    }
}
