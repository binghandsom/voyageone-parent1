package com.voyageone.web2.cms.views.channel;

import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.configs.dao.TypeChannelDao;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.cms.CmsMtPlatformBrandService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmMasterBrandService;
import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import com.voyageone.service.model.cms.CmsMtPlatformBrandsModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.channel.CmsBlackBrandParamBean;
import com.voyageone.web2.cms.bean.channel.CmsBlackBrandViewBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by jonas on 9/7/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
class CmsBlackBrandViewService extends BaseViewService {

    private final CmsMtChannelValuesService channelValuesService;

    private final CmsMtPlatformBrandService platformBrandService;

    private final CmsBtBrandBlockService brandBlockService;

    private final CmsBtJmMasterBrandService jmMasterBrandService;

    private final TypeChannelDao typeChannelDao;

    @Autowired
    public CmsBlackBrandViewService(CmsMtChannelValuesService channelValuesService, CmsMtPlatformBrandService platformBrandService,
                                    CmsBtBrandBlockService brandBlockService, CmsBtJmMasterBrandService jmMasterBrandService, TypeChannelDao typeChannelDao) {
        this.channelValuesService = channelValuesService;
        this.platformBrandService = platformBrandService;
        this.brandBlockService = brandBlockService;
        this.jmMasterBrandService = jmMasterBrandService;
        this.typeChannelDao = typeChannelDao;
    }

    Map<String, Object> searchBrandListPage(CmsBlackBrandParamBean blackBrandParamBean, String langId, UserSessionBean user) {

        Map<String, Object> result = new HashMap<>();

        int limit = blackBrandParamBean.getPageSize();
        int pageNumber = blackBrandParamBean.getPageNumber();
        int offset = limit * (pageNumber - 1);

        String channelId = user.getSelChannelId();
        int brandType = blackBrandParamBean.getBrandType();
        Boolean status = blackBrandParamBean.getStatus();
        String brand = blackBrandParamBean.getBrand();
        List<Integer> cartIdList = blackBrandParamBean.getCartIdList();

        Stream<CmsBlackBrandViewBean> stream;

        int count = 0;
        List<CmsBlackBrandViewBean> data;
        switch (brandType) {
            case CmsBtBrandBlockService.BRAND_TYPE_FEED:
                channelId = StringUtil.isEmpty(blackBrandParamBean.getChannelId()) ? user.getSelChannelId() : blackBrandParamBean.getChannelId();
                stream = getFeedBrandStream(channelId);
                break;
            case CmsBtBrandBlockService.BRAND_TYPE_MASTER:
                count = typeChannelDao.getByChannelTypeIdCnt(channelId,TypeConfigEnums.MastType.brand.getId(), brand, status);
                stream = getMasterBrandStream(channelId,brand, status, offset, limit);
                break;
            case CmsBtBrandBlockService.BRAND_TYPE_PLATFORM:
                stream = getPlatformBrandStream(channelId, cartIdList);
                break;
            default:
                result.put("count", 0);
                result.put("data", new Object[0]);
                return result;
        }

        if(brandType != CmsBtBrandBlockService.BRAND_TYPE_MASTER) {
            if (status != null)
                stream = stream.filter(i -> status.equals(i.isBlocked()));

            if (!StringUtils.isEmpty(brand)) {
                String lowerCase = brand.toLowerCase();
                stream = stream.filter(i -> i.getBrandName().toLowerCase().contains(lowerCase));
            }

            List<CmsBlackBrandViewBean> filtered = stream.collect(toList());

            count = filtered.size();

            data = filtered
                    .stream()
                    .skip(offset)
                    .limit(limit)
                    .collect(toList());
        }else {
            data = stream.collect(toList());
        }

        if (count == 0) {
            result.put("count", 0);
            result.put("data", new Object[0]);
            return result;
        }



        result.put("count", count);
        result.put("data", data);
        return result;
    }

    boolean switchBrandBlock(CmsBlackBrandParamBean blackBrandParamBean, UserSessionBean user) throws IllegalAccessException {

        Boolean status = blackBrandParamBean.getStatus();

        if (status == null)
            return true;

        List<CmsBlackBrandViewBean> blackBrandViewBeanList =blackBrandParamBean.getBrandList();

        if (blackBrandViewBeanList == null || blackBrandViewBeanList.isEmpty())
            return true;

        String channelId = user.getSelChannelId();
        if(blackBrandParamBean.getBrandType() == CmsBtBrandBlockService.BRAND_TYPE_FEED){
            channelId = StringUtil.isEmpty(blackBrandParamBean.getChannelId()) ? user.getSelChannelId() : blackBrandParamBean.getChannelId();
        }
        String username = user.getUserName();

        for (CmsBlackBrandViewBean blackBrandViewBean : blackBrandViewBeanList)
            switchBrandBlock(status, channelId, blackBrandViewBean, username);

        return true;
    }

    private void switchBrandBlock(boolean blocked, String channelId, CmsBlackBrandViewBean blackBrandViewBean, String username) throws IllegalAccessException {
        if (blocked)
            brandBlockService.block(channelId, blackBrandViewBean.getCartId(), blackBrandViewBean.getType(),
                    blackBrandViewBean.getBrand(), username);
        else
            brandBlockService.unblock(channelId, blackBrandViewBean.getCartId(), blackBrandViewBean.getType(),
                    blackBrandViewBean.getBrand(),username);
    }

    private Stream<CmsBlackBrandViewBean> getPlatformBrandStream(String channelId, List<Integer> cartIdList) {

        Stream<CmsBlackBrandViewBean> otherPlatformBrandStream, jmBrandStream = null;

        if (cartIdList == null || cartIdList.isEmpty()) {
            otherPlatformBrandStream = platformBrandService.getAll(channelId, null)
                    .stream()
                    .map(this::toBlackBrandViewBean);
            jmBrandStream = getJmBrandStream(channelId);
        } else {
            otherPlatformBrandStream = cartIdList
                    .stream()
                    .map(cartId -> platformBrandService.getAll(channelId, cartId))
                    .flatMap(Collection::stream)
                    .map(this::toBlackBrandViewBean);
            if (cartIdList.contains(27))
                jmBrandStream = getJmBrandStream(channelId);
        }

        if (jmBrandStream == null)
            return otherPlatformBrandStream;

        return Stream.concat(otherPlatformBrandStream, jmBrandStream);
    }

    private Stream<CmsBlackBrandViewBean> getJmBrandStream(String channelId) {
        return jmMasterBrandService.selectAll()
                .stream()
                .map(i -> {
                    CmsBlackBrandViewBean viewBean = toBlackBrandViewBean(i);
                    viewBean.setChannelId(channelId);
                    return viewBean;
                });
    }

    private CmsBlackBrandViewBean toBlackBrandViewBean(CmsBtJmMasterBrandModel jmMasterBrandModel) {

        CmsBlackBrandViewBean viewBean = new CmsBlackBrandViewBean();

        viewBean.setCartId(27);
        viewBean.setType(CmsBtBrandBlockService.BRAND_TYPE_PLATFORM);
        viewBean.setBrand(jmMasterBrandModel.getJmMasterBrandId().toString());
        viewBean.setBrandName(jmMasterBrandModel.getName());
        viewBean.setBlocked(brandBlockService.isBlocked(viewBean));

        return viewBean;
    }

    private Stream<CmsBlackBrandViewBean> getMasterBrandStream(String channelId, String value, Boolean status, Integer start, Integer length) {
        List<Map<String, Object>> typeChannelBeanList = typeChannelDao.getByChannelTypeId(channelId, TypeConfigEnums.MastType.brand.getId(), value, status, start, length);
        return typeChannelBeanList.stream().map(this::toBlackBrandViewBean);
    }

    private Stream<CmsBlackBrandViewBean> getFeedBrandStream(String channelId) {

        List<CmsMtChannelValuesModel> brandList = channelValuesService.getCmsMtChannelValuesListByChannelIdType(channelId, CmsMtChannelValuesService.BRAND);

        return brandList.stream().map(this::toBlackBrandViewBean);
    }

    private CmsBlackBrandViewBean toBlackBrandViewBean(CmsMtPlatformBrandsModel platformBrandsModel) {

        CmsBlackBrandViewBean viewBean = new CmsBlackBrandViewBean();

        viewBean.setChannelId(platformBrandsModel.getChannelId());
        viewBean.setCartId(platformBrandsModel.getCartId());
        viewBean.setType(CmsBtBrandBlockService.BRAND_TYPE_PLATFORM);
        viewBean.setBrand(platformBrandsModel.getBrandId());
        viewBean.setBrandName(platformBrandsModel.getBrandName());
        viewBean.setBlocked(brandBlockService.isBlocked(viewBean));

        return viewBean;
    }

    private CmsBlackBrandViewBean toBlackBrandViewBean(Map<String, Object> typeChannelBean) {

        CmsBlackBrandViewBean viewBean = new CmsBlackBrandViewBean();

        viewBean.setChannelId((String) typeChannelBean.get("channel_id"));
        viewBean.setCartId(0);
        viewBean.setType(CmsBtBrandBlockService.BRAND_TYPE_MASTER);
        viewBean.setBrand((String) typeChannelBean.get("value"));
        viewBean.setBrandName((String) typeChannelBean.get("name"));
        Integer blocked = (Integer) typeChannelBean.get("blocked");
        viewBean.setBlocked(blocked == 1? true:false);

        return viewBean;
    }

    private CmsBlackBrandViewBean toBlackBrandViewBean(TypeChannelBean typeChannelBean) {

        CmsBlackBrandViewBean viewBean = new CmsBlackBrandViewBean();

        viewBean.setChannelId(typeChannelBean.getChannel_id());
        viewBean.setCartId(0);
        viewBean.setType(CmsBtBrandBlockService.BRAND_TYPE_MASTER);
        viewBean.setBrand(typeChannelBean.getValue());
        viewBean.setBrandName(typeChannelBean.getName());
        viewBean.setBlocked(brandBlockService.isBlocked(viewBean));

        return viewBean;
    }

    private CmsBlackBrandViewBean toBlackBrandViewBean(CmsMtChannelValuesModel channelValuesModel) {

        CmsBlackBrandViewBean viewBean = new CmsBlackBrandViewBean();

        viewBean.setChannelId(channelValuesModel.getChannelId());
        viewBean.setCartId(1);
        viewBean.setType(CmsBtBrandBlockService.BRAND_TYPE_FEED);
        viewBean.setBrand(channelValuesModel.getKey());
        viewBean.setBrandName(channelValuesModel.getValue());
        viewBean.setBlocked(brandBlockService.isBlocked(viewBean));

        return viewBean;
    }
}
