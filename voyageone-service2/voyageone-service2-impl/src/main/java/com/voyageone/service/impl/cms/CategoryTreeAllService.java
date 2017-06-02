package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.bean.cms.CmsMtCategoryTreeAllBean;
import com.voyageone.service.dao.cms.mongo.CmsMtCategoryTreeAllDao;
import com.voyageone.service.enums.cms.SkuSplit;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtChannelCategoryConfigModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel_Platform;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Service
public class CategoryTreeAllService extends BaseService {

    @Autowired
    private ChannelCategoryService channelCategoryService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CmsMtCategoryTreeAllDao cmsMtCategoryTreeAllDao;

    /**
     * 取得Category Tree 根据channelId
     */
    public List<CmsMtCategoryTreeAllBean> getCategoriesByChannelId(String channelId, String lang) {
        List<CmsMtCategoryTreeAllBean> result = new ArrayList<>();

        boolean isChildren = false;
        List<TypeChannelBean> typeChannelBeans = TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, "cn");
        for(TypeChannelBean typeChannelBean : typeChannelBeans){
            if (Channels.isUsJoi(typeChannelBean.getValue())) {
                isChildren = true;
                break;
            }
        };
        // 根据channelId取得channel与Category的对应关系
        List<CmsMtChannelCategoryConfigModel> mappings = null;
        if(isChildren){
            mappings = channelCategoryService.getByChannelId("928");
        }else{
            mappings = channelCategoryService.getByChannelId(channelId);
        }
        $debug("mappings size = " + mappings.size());
        //Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> applyPlatformCategoryMap = null;

        // 根据channel与Category的对应关系(多个catId),取得CategoryTree
        for (CmsMtChannelCategoryConfigModel mapping : mappings) {
            String catId = mapping.getCategoryId();
            $debug("catId = " + catId);
            CmsMtCategoryTreeAllModel model = cmsMtCategoryTreeAllDao.selectByCatId(catId);
            CmsMtCategoryTreeAllBean bean = null;
            if (model != null) {
                // ModelToBean，将主类目对应的平台类目信息进行转换（platformId转换成对应的cartId），并且根据channelId对应的平台类目信息，加上这个平台类目是否申请的标志位
                bean = changeModelToBean(model, channelId, null, lang);
            }
            if (bean != null) {
                result.add(bean);
            }
        }
        return result;
    }

    /**
     * ModelToBean，将主类目对应的平台类目信息进行转换（platformId转换成对应的cartId），并且根据channelId对应的平台类目信息，加上这个平台类目是否申请的标志位
     */
    public CmsMtCategoryTreeAllBean changeModelToBean(CmsMtCategoryTreeAllModel model, String channelId, Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> applyPlatformCategoryMap,  String lang) {

        CmsMtCategoryTreeAllBean bean = new CmsMtCategoryTreeAllBean();
        bean.setCatId(model.getCatId());
        bean.setCatPath(model.getCatPath());
        bean.setCatName(model.getCatName());
        bean.setIsParent(model.getIsParent());
        bean.setParentCatId(model.getParentCatId());
        bean.setSingleSku(model.getSingleSku());
        bean.setSkuSplit(SkuSplit.valueOf(model.getSkuSplit()));
        bean.setCatNameEn(model.getCatNameEn());
        bean.setCatPathEn(model.getCatPathEn());
        bean.setProductTypeEn(model.getProductTypeEn());
        bean.setProductTypeCn(model.getProductTypeCn());
        bean.setSizeTypeEn(model.getSizeTypeEn());
        bean.setSizeTypeCn(model.getSizeTypeCn());
        bean.setHscode8(model.getHscode8());
        bean.setHscode10(model.getHscode10());
        bean.setHscodeName8(model.getHscodeName8());
        bean.setHscodeName10(model.getHscodeName10());


//        // 取得这个主类目对应的平台类目信息，Map<platformId，CmsMtCategoryTreeAllModel_Platform>
//        List<CmsMtCategoryTreeAllModel_Platform> categoryTreePlatformList = model.getPlatformCategory();
//        Map<String, CmsMtCategoryTreeAllModel_Platform> categoryTreePlatformMap =
//                categoryTreePlatformList.stream().collect(toMap(CmsMtCategoryTreeAllModel_Platform::getPlatformId, item -> item));
//
//        // 获取当前channel, 有多少个cartId
//        List<Map> platformCategoryList = new ArrayList<>();
//        List<TypeChannelBean> typeChannelBeanListApprove = TypeChannels.getTypeListSkuCarts(channelId, "A", lang); // 取得允许Approve的数据
//        if (typeChannelBeanListApprove != null) {
//            // 根据渠道和平台取得已经申请的平台类目
//            if (applyPlatformCategoryMap == null) {
//                applyPlatformCategoryMap = getApplyPlatformCategory(channelId, typeChannelBeanListApprove);
//            }
//
//            // 生成这个主类目下，以对应的各个cartId为单位的平台类目信息
//            for(TypeChannelBean typeChannelBean : typeChannelBeanListApprove) {
//                Map<String, Object> platformCategory = new HashMap<>();
//                platformCategory.put("cartId", String.valueOf(typeChannelBean.getValue()));
//                platformCategory.put("cartName", String.valueOf(typeChannelBean.getName()));
//                CartBean cartBean = Carts.getCart(Integer.parseInt(typeChannelBean.getValue()));
//                if (cartBean !=null && categoryTreePlatformMap.get(cartBean.getPlatform_id()) != null) {
//                    platformCategory.put("catId", categoryTreePlatformMap.get(cartBean.getPlatform_id()).getCatId());
//                    platformCategory.put("catPath", categoryTreePlatformMap.get(cartBean.getPlatform_id()).getCatPath());
//                } else {
//                    platformCategory.put("catId", "");
//                    platformCategory.put("catPath", "");
//                }
//
//                // 根据channelId对应的平台类目信息，加上这个平台类目是否申请的标志位
//                if (cartBean != null
//                        && categoryTreePlatformMap.get(cartBean.getPlatform_id()) != null
//                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()) != null
//                        && applyPlatformCategoryMap.get(typeChannelBean.getValue()).get(categoryTreePlatformMap.get(cartBean.getPlatform_id()).getCatId()) == null) {
//                    platformCategory.put("isNotApply", "1");
//                } else {
//                    platformCategory.put("isNotApply", "0");
//                }
//                platformCategoryList.add(platformCategory);
//            }
//        }
//        bean.setPlatformCategory(platformCategoryList);
//
        // 这个类目的子类目也要这样做
        if (model.getChildren() != null && !model.getChildren().isEmpty()) {
            for (CmsMtCategoryTreeAllModel child : model.getChildren()) {
                CmsMtCategoryTreeAllBean childBean = changeModelToBean(child, channelId, applyPlatformCategoryMap, lang);
                bean.getChildren().add(childBean);
            }
        }

        return bean;
    }

    /**
     * 根据渠道和平台取得已经申请的平台类目
     */
    public Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> getApplyPlatformCategory( String channelId, List<TypeChannelBean> typeChannelBeanListApprove) {
        Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> applyPlatformCategoryMap = new HashMap<>();
        for(TypeChannelBean typeChannelBean : typeChannelBeanListApprove) {
            // --> 取平台所有类目
            List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModels = platformCategoryService.getPlatformCategories(channelId, Integer.parseInt(typeChannelBean.getValue()));

            // --> 所有平台类目 --> 取所有叶子 --> 拍平
            Stream<CmsMtPlatformCategoryTreeModel> platformCategoryTreeModelStream =
                    platformCategoryTreeModels.stream().flatMap(this::flattenFinal);

            // --> 所有平台类目 --> 取所有叶子 --> 拍平 --> 转 Map, id 为键, path 为值
//            Map<String, CmsMtPlatformCategoryTreeModel> platformMap = platformCategoryTreeModelStream.collect(toMap(CmsMtPlatformCategoryTreeModel::getCatId,model -> model));
            Map<String, CmsMtPlatformCategoryTreeModel> platformMap = new HashMap<>();
            platformCategoryTreeModelStream.forEach(item->platformMap.put(item.getCatId(),item));
            applyPlatformCategoryMap.put(typeChannelBean.getValue(), platformMap);
        }

        return applyPlatformCategoryMap;
    }

    /**
     * 拍平叶子类目,不包含父级
     *
     * @param platformCategoryTreeModel 平台类目模型
     * @return 叶子类目数据流
     */
    private Stream<CmsMtPlatformCategoryTreeModel> flattenFinal(CmsMtPlatformCategoryTreeModel platformCategoryTreeModel) {

        if (platformCategoryTreeModel.getIsParent() == 0)
            return Stream.of(platformCategoryTreeModel);

        List<CmsMtPlatformCategoryTreeModel> children = platformCategoryTreeModel.getChildren();

        if (children == null) children = new ArrayList<>(0);

        return children.stream().flatMap(this::flattenFinal);
    }

    /**
     * 根据类目id获取一级类目下的类目
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public CmsMtCategoryTreeAllModel getCategoryByCatId(String catId) {
        return cmsMtCategoryTreeAllDao.selectByCatId(catId);
    }

    /**
     * 根据类目Path获取一级类目下的类目
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public CmsMtCategoryTreeAllModel getFirstLevelCategoryByCatPath(String catPath) {
        return cmsMtCategoryTreeAllDao.selectByCatPath(catPath);
    }

    /**
     * 获取类目名称对应的类目的一级类目对象
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public CmsMtCategoryTreeAllModel getFirstLevelCategoryObjectByCatPath(String catPath) {
        List<CmsMtCategoryTreeAllModel> categoryTreeList = getMasterCategory();
        for (CmsMtCategoryTreeAllModel categoryTree : categoryTreeList) {
            CmsMtCategoryTreeAllModel model = findCategory(categoryTree, catPath);
            if (model != null) {
                return  categoryTree;
            }
        }
        return  null;
    }

    /**
     * 获取类目名称对应的类目对象
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public CmsMtCategoryTreeAllModel getCategoryByCatPath(String catPath) {
        List<CmsMtCategoryTreeAllModel> categoryTreeList = getMasterCategory();
       for (CmsMtCategoryTreeAllModel categoryTree : categoryTreeList) {
           CmsMtCategoryTreeAllModel model = findCategory(categoryTree, catPath);
           if (model != null) {
               return  model;
           }
       }
        return  null;
    }

    /**
     * 取得一级类目列表（主数据）
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public List<CmsMtCategoryTreeAllModel> getFstLvlMasterCategory() {
        JongoQuery queryObject = new JongoQuery();
        queryObject.setProjection("{'catId':1,'catName':1,'catPath':1,'isParent':1}");
        queryObject.setSort("{'catName':1}");
        return cmsMtCategoryTreeAllDao.select(queryObject);
    }

    /**
     * 取得一级类目列表（所有主数据）
     *
     * @return CmsMtCategoryTreeAllModel
     */
    public List<CmsMtCategoryTreeAllModel> getMasterCategory() {
        return cmsMtCategoryTreeAllDao.select(new JongoQuery());
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeAllModel findCategory(CmsMtCategoryTreeAllModel tree, String catPath) {
        for (CmsMtCategoryTreeAllModel CmsMtCategoryTreeAllModel : tree.getChildren()) {
            if (CmsMtCategoryTreeAllModel.getCatPath().equalsIgnoreCase(catPath)) {
                return CmsMtCategoryTreeAllModel;
            }
            if (!CmsMtCategoryTreeAllModel.getChildren().isEmpty()) {
                CmsMtCategoryTreeAllModel category = findCategory(CmsMtCategoryTreeAllModel, catPath);
                if (category != null) return category;
            }
        }
        return null;
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeAllModel findCategorySingleSku(CmsMtCategoryTreeAllModel tree, String catPath, List<String> result) {
        for (CmsMtCategoryTreeAllModel CmsMtCategoryTreeAllModel : tree.getChildren()) {
            if (CmsMtCategoryTreeAllModel.getCatPath().equalsIgnoreCase(catPath)) {
                if ("1".equals(CmsMtCategoryTreeAllModel.getSingleSku())) {
                    result.add("1");
                }
                return CmsMtCategoryTreeAllModel;
            }
            if (!CmsMtCategoryTreeAllModel.getChildren().isEmpty()) {
                CmsMtCategoryTreeAllModel category = findCategorySingleSku(CmsMtCategoryTreeAllModel, catPath, result);
                if (category != null) {
                    if ("1".equals(CmsMtCategoryTreeAllModel.getSingleSku())) {
                        result.add("1");
                    }
                    return category;
                }
            }
        }
        return null;
    }

    /**
     * 根据category从tree中找到节点
     */
    public List<CmsMtCategoryTreeAllModel> findCategoryListByCatId(String rootCatId, int catLevel, String catId) {
        CmsMtCategoryTreeAllModel treeModel = cmsMtCategoryTreeAllDao.selectByCatId(rootCatId == null ? catId : rootCatId);
        if (catLevel > 0) {
            treeModel = findCategoryByCatId(treeModel, catId);
        }
        if (treeModel == null) {
            return new ArrayList<>(0);
        }
        return treeModel.getChildren();
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeAllModel findCategoryByCatId(CmsMtCategoryTreeAllModel tree, String catId) {
        if (tree == null) {
            return null;
        }
        for (CmsMtCategoryTreeAllModel catTreeModel : tree.getChildren()) {
            if (catTreeModel.getCatId().equalsIgnoreCase(catId)) {
                return catTreeModel;
            }
            if (!catTreeModel.getChildren().isEmpty()) {
                CmsMtCategoryTreeAllModel category = findCategoryByCatId(catTreeModel, catId);
                if (category != null) return category;
            }
        }
        return null;
    }

    /**
     * 根据平台种类Id及类目名称返回对应的类目信息.
     * @param channelId 店铺Id
     * @param catPath 类目路径
     * @param platformId 平台种类Id
     * @return
     */
    public CmsMtCategoryTreeAllModel findCategoryByPlatformId(String channelId, String catPath, String platformId) {
        CmsMtCategoryTreeAllModel result = new CmsMtCategoryTreeAllModel();
        // 根据channelId取得channel与Category的对应关系
        List<CmsMtChannelCategoryConfigModel> mappings = channelCategoryService.getByChannelId(channelId);

        // 根据channel与Category的对应关系(多个catId),取得CategoryTree
        for (CmsMtChannelCategoryConfigModel mapping : mappings) {
            String catId = mapping.getCategoryId();
            CmsMtCategoryTreeAllModel model = cmsMtCategoryTreeAllDao.selectByCatId(catId);
            if (model != null) {
                result = findCategoryByPlatform (model, catPath, platformId);
            }
        }

        return result;
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtCategoryTreeAllModel findCategoryByPlatform(CmsMtCategoryTreeAllModel tree, String catPath, String platformId) {

        if (tree.getatIdByPlatformInfo(platformId, catPath) != null) {
            return tree;
        }

        for (CmsMtCategoryTreeAllModel CmsMtCategoryTreeAllModel : tree.getChildren()) {
            return findCategoryByPlatform(CmsMtCategoryTreeAllModel, catPath, platformId);
        }

        return null;
    }
}
