package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.GetFieldMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 为属性匹配画面提供功能
 *
 * @author Jonas, 12/24/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsFeedPropMappingService extends BaseAppService {

    @Autowired
    private CmsMtCategorySchemaDao categorySchemaDao;

    @Autowired
    private CmsFeedMappingService feedMappingService;

    @Autowired
    private com.voyageone.cms.service.CmsFeedMappingService com$feedMappingService;

    /**
     * 通过 Feed 类目,获取其默认匹配的主类目
     *
     * @param feedCategoryPath Feed 类目路径
     * @param userSessionBean  当前用户及配置
     * @return 主类目
     */
    public CmsMtCategorySchemaModel getCategoryPropsByFeed(String feedCategoryPath, UserSessionBean userSessionBean) throws UnsupportedEncodingException {

        CmsMtFeedCategoryTreeModelx treeModel = feedMappingService.getFeedCategoryTree(userSessionBean);

        CmsFeedCategoryModel feedCategoryModel = feedMappingService.findByPath(feedCategoryPath, treeModel);

        if (feedCategoryModel == null)
            throw new BusinessException("根据路径没找到类目");

        CmsFeedMappingModel feedMappingModel = feedMappingService.findMapping(feedCategoryModel, m -> m.getDefaultMapping() == 1);

        if (feedMappingModel == null)
            throw new BusinessException("类目没有默认的类目匹配");

        String categoryId = convertPathToId(feedMappingModel.getMainCategoryPath());

        return categorySchemaDao.getMasterSchemaModelByCatId(categoryId);
    }

    /**
     * 获取具体到字段的 Mapping 设定
     *
     * @param getFieldMappingBean 查询参数
     * @param user                当前用户
     * @return 属性匹配设定
     */
    public CmsBtFeedMappingModel.Prop getFieldMapping(GetFieldMappingBean getFieldMappingBean, UserSessionBean user) {

        CmsBtFeedMappingModel mappingModel = com$feedMappingService.getMapping(user.getSelChannel(),
                getFieldMappingBean.getFeedCategoryPath(), getFieldMappingBean.getMainCategoryPath());

        List<CmsBtFeedMappingModel.Prop> props = mappingModel.getProps();

        if (props == null)
            return null;

        return flattenFinalProp(props)
                .filter(p -> p.getProp().equals(getFieldMappingBean.getField()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取类目匹配中已匹配的主类目属性
     *
     * @param feedCategoryPath Feed 类目路径
     * @param mainCategoryPath Main 类目路径
     * @param userSessionBean  当前用户
     * @return 属性名集合
     */
    public List<String> getMatched(String feedCategoryPath, String mainCategoryPath, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel btFeedMappingModel = com$feedMappingService.getMapping(userSessionBean.getSelChannel(),
                feedCategoryPath, mainCategoryPath);

        if (btFeedMappingModel == null)
            throw new BusinessException("没有找到匹配");

        List<CmsBtFeedMappingModel.Prop> props = btFeedMappingModel.getProps();

        if (props == null)
            return new ArrayList<>(0);

        return flattenFinalProp(props)
                .filter(p -> p.getMappings() != null && p.getMappings().size() > 0)
                .map(CmsBtFeedMappingModel.Prop::getProp)
                .collect(toList());
    }

    /**
     * 通过 categoryPath 转换获取 categoryId
     *
     * @param categoryPath 类目路径
     * @return String
     */
    public String convertPathToId(String categoryPath) throws UnsupportedEncodingException {

        // 当前为 Path 的 Base64 码
        // 有可能未来更改为 MD5
        return new String(Base64.encodeBase64(categoryPath.getBytes("UTF-8")), "UTF-8");
    }

    /**
     * 按 feedCategoryPath 查找类目并返回类目的属性
     *
     * @param feedCategoryPath 类目路径
     * @param userSessionBean  当前用户
     * @return 类目的属性
     */
    public Map<String, List<String>> getFeedAttributes(String feedCategoryPath, UserSessionBean userSessionBean) {

        CmsMtFeedCategoryTreeModelx treeModelx = feedMappingService.getFeedCategoryTree(userSessionBean);

        CmsFeedCategoryModel feedCategoryModel = feedMappingService.findByPath(feedCategoryPath, treeModelx);

        return feedCategoryModel == null ? null : feedCategoryModel.getAttribute();
    }

    private Stream<CmsBtFeedMappingModel.Prop> flattenFinalProp(List<CmsBtFeedMappingModel.Prop> props) {

        return props.stream().flatMap(this::flattenFinalProp);
    }

    private Stream<CmsBtFeedMappingModel.Prop> flattenFinalProp(CmsBtFeedMappingModel.Prop prop) {

        List<CmsBtFeedMappingModel.Prop> children = prop.getChildren();

        if (children == null || children.size() < 1)
            return Stream.of(prop);

        return flattenFinalProp(children);
    }
}
