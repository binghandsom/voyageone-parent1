package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPlatformNumiidDaoExt;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 取得京东上商品属性，回写数据库
 *
 * @author morse on 2016/11/08
 * @version 2.6.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_JDFieldsImportCms2Job)
public class CmsPlatformProductImportJdFieldsService extends BaseMQCmsService {

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private JdWareService jdWareService;
    @Autowired
    private JdShopService jdShopService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtPlatformNumiidDao cmsBtPlatformNumiidDao;
    @Autowired
    private CmsBtPlatformNumiidDaoExt cmsBtPlatformNumiidDaoExt;
//    @Autowired
//    private CmsBtSellerCatDao cmsBtSellerCatDao;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }
        String cartId = null;
        if (messageMap.containsKey("cartId")) {
            cartId = String.valueOf(messageMap.get("cartId"));
            if (!CartEnums.Cart.isJdSeries(CartEnums.Cart.getValueByID(cartId))) {
                $error("入参的平台id不是京东系!");
                return;
            }
        }
        String numIId = null;
        if (messageMap.containsKey("numIId")) {
            numIId = String.valueOf(messageMap.get("numIId"));
        }
        String code = null;
        if (messageMap.containsKey("code")) {
            code = String.valueOf(messageMap.get("code"));
        }

        String runType = null; // runType=2 从cms_bt_platform_numiid表里抽出numIId去做
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        doMain(channelId, cartId, numIId, code, runType);
    }

    private void doMain(String channelId, String cartId, String numIId, String code, String runType) throws Exception {
        JongoQuery queryObject = new JongoQuery();
//        String query = "cartId:" + cartId + ",numIId:{$nin:[\"\",null]}";
        String query = "cartId:" + cartId;
        List<String> listAllNumiid = null;
        List<String> listSuccessNumiid = new ArrayList<>();
        List<String> listErrorNumiid = new ArrayList<>();
        if ("2".equals(runType)) {
            // 从cms_bt_platform_numiid表里抽出numIId去做
            Map<String, Object> seachParam = new HashMap<>();
            seachParam.put("channelId", channelId);
            seachParam.put("cartId", cartId);
            seachParam.put("status", "0");
            List<CmsBtPlatformNumiidModel> listModel = cmsBtPlatformNumiidDao.selectList(seachParam);
            if (ListUtils.isNull(listModel)) {
                $warn("cms_bt_platform_numiid表未找到符合的数据!");
                return;
            }
            listAllNumiid = listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.toList());
            // 表的数据都是自己临时加的，一次处理多少件自己决定，因此暂时不分批处理了，尽量别一次处理太多，不然sql可能撑不住
            query = query + "," + "numIId:{$in:[\"" + listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.joining("\",\"")) + "\"]}";
        } else {
            if (!StringUtils.isEmpty(numIId)) {
                query = query + "," + "numIId:\"" + numIId + "\"";
            } else {
                query = query + ",numIId:{$nin:[\"\",null]}";
            }
            if (!StringUtils.isEmpty(code)) {
                query = query + "," + "productCodes:\"" + code + "\"";
            }
        }
        queryObject.setQuery("{" + query + "}");
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId, queryObject);
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        List<ShopCategory> shopCategories = jdShopService.getShopCategoryList(shopBean);
        List<CmsBtSellerCatModel> listSellerCat = formatJdModel(shopCategories, channelId, Integer.valueOf(cartId), getTaskName());
        convert2Tree(listSellerCat);

        for (int i = 0; i < cmsBtProductGroupModels.size(); i++) {
            CmsBtProductGroupModel item = cmsBtProductGroupModels.get(i);
            if ("2".equals(runType)) {
                listAllNumiid.remove(item.getNumIId());
            }
            try {
                $info(String.format("%s-%s-%s京东属性取得 %d/%d", channelId, cartId, item.getNumIId(), i + 1, cmsBtProductGroupModels.size()));
                doSetProduct(shopBean, item, channelId, Integer.valueOf(cartId), listSellerCat);
                listSuccessNumiid.add(item.getNumIId());
                $info(String.format("channelId:%s, cartId:%s, numIId:%s 取得成功!", channelId, cartId, item.getNumIId()));
            } catch (Exception e) {
                listErrorNumiid.add(item.getNumIId());
                if (e instanceof BusinessException) {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s 取得失败!" + e.getMessage(), channelId, cartId, item.getNumIId()));
                } else {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s 取得失败!", channelId, cartId, item.getNumIId()));
                    e.printStackTrace();
                }
            }

            if ((i + 1) % 300 == 0) {
                // 怕中途断掉,300一更新
                if ("2".equals(runType)) {
                    updateCmsBtPlatformNumiid(channelId, cartId, listSuccessNumiid, listErrorNumiid);
                }
                $info(String.format("京东属性取得,成功%d个,失败%d个!", listSuccessNumiid.size(), listErrorNumiid.size()));
                listSuccessNumiid.clear();
                listErrorNumiid.clear();
            }

            // 做好一个稍微停一会，JD的响应不够快
            Thread.sleep(1000);
        }

        $info(String.format("京东属性取得,成功%d个,失败%d个!", listSuccessNumiid.size(), listErrorNumiid.size()));
        if ("2".equals(runType)) {
            updateCmsBtPlatformNumiid(channelId, cartId, listSuccessNumiid, listErrorNumiid);
            if (ListUtils.notNull(listAllNumiid)) {
                // 存在没有搜到的numIId
                cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "3", getTaskName(), listSuccessNumiid);
            }
        }
    }

    private void updateCmsBtPlatformNumiid(String channelId, String cartId, List<String> listSuccessNumiid, List<String> listErrorNumiid) {
        if (listSuccessNumiid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "1", getTaskName(), listSuccessNumiid);
        }
        if (listErrorNumiid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "2", getTaskName(), listErrorNumiid);
        }
    }

    /**
     * 将JD店铺自定义分类Model转换成CmsBtSellerCatModel
     */
    private List<CmsBtSellerCatModel> formatJdModel(List<ShopCategory> shopCategories, String channelId, int cartId, String creator) {
        List<CmsBtSellerCatModel> result = new ArrayList<>();

        for (ShopCategory model : shopCategories) {
            CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel();
            cmsBtSellerCatModel.setCatId(String.valueOf(model.getCid()));
            cmsBtSellerCatModel.setCatName(model.getName());
            cmsBtSellerCatModel.setParentCatId(String.valueOf(model.getParentId()));
            cmsBtSellerCatModel.setIsParent(model.getParent() ? 1 : 0);
            cmsBtSellerCatModel.setChannelId(channelId);
            cmsBtSellerCatModel.setCartId(cartId);
            String now = DateTimeUtil.getNow();
            cmsBtSellerCatModel.setCreated(now);
            cmsBtSellerCatModel.setModified(now);
            cmsBtSellerCatModel.setCreater(creator);
            cmsBtSellerCatModel.setModifier(creator);
            result.add(cmsBtSellerCatModel);
        }

        return result;
    }

    /**
     * 将店铺自定义分类列转成一组树
     */
    private List<CmsBtSellerCatModel> convert2Tree(List<CmsBtSellerCatModel> sellCatList) {
        List<CmsBtSellerCatModel> roots = findRoots(sellCatList);
        List<CmsBtSellerCatModel> notRoots = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(sellCatList, roots);
        for (CmsBtSellerCatModel root : roots) {
            List<CmsBtSellerCatModel> children = findChildren(root, notRoots);
            root.setChildren(children);
        }
        return roots;
    }

    /**
     * 查找所有根节点
     */
    private List<CmsBtSellerCatModel> findRoots(List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> results = new ArrayList<>();
        for (CmsBtSellerCatModel node : allNodes) {
            if ("0".equals(node.getParentCatId())) {
                results.add(node);
                node.setCatPath(node.getCatName());
                node.setFullCatId(node.getCatId());
            }
        }
        return results;
    }

    /**
     * 查找所有子节点
     */
    private List<CmsBtSellerCatModel> findChildren(CmsBtSellerCatModel root, List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> children = new ArrayList<>();

        for (CmsBtSellerCatModel comparedOne : allNodes) {
            if (comparedOne.getParentCatId().equals(root.getCatId())) {
                children.add(comparedOne);
                comparedOne.setCatPath(root.getCatPath() + ">" + comparedOne.getCatName());
                comparedOne.setFullCatId(root.getFullCatId() + "-" + comparedOne.getCatId());
            }
        }
        root.setChildren(children);

        List<CmsBtSellerCatModel> notChildren = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(allNodes, children);

        for (CmsBtSellerCatModel child : children) {
            List<CmsBtSellerCatModel> tmpChildren = findChildren(child, notChildren);

            child.setChildren(tmpChildren);
        }

        return children;
    }

    private void doSetProduct(ShopBean shopBean, CmsBtProductGroupModel cmsBtProductGroup, String channelId, int cartId, List<CmsBtSellerCatModel> listSellerCat) {
        String wareId = cmsBtProductGroup.getNumIId();
        // 获取商品信息(360buy.ware.get)
//        Ware ware = jdWareService.getWareInfo(shopBean, wareId, null);
        Ware ware = jdWareService.getWareInfo(shopBean, wareId, "ware_id,skus,cid,ware_status,attributes,shop_categorys,title,ad_content,cubage,weight,is_pay_first,is_can_vat,item_num");
        upProductPlatform(cmsBtProductGroup, channelId, cartId, ware, listSellerCat);
    }

    private void upProductPlatform(CmsBtProductGroupModel cmsBtProductGroup, String channelId, int cartId, Ware ware, List<CmsBtSellerCatModel> listSellerCat) {
        String wareId = cmsBtProductGroup.getNumIId();
        // deleted by morse.lu 2016/11/09 start
        // 貌似不需要都设置，只要attributes和一些共通项目
//        Map<String, Object> mapBean = JsonUtil.jsonToMap(JsonUtil.getJsonString(ware));
        // deleted by morse.lu 2016/11/09 end
        final boolean[] hasErr = {false};
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        cmsBtProductGroup.getProductCodes().forEach(code -> {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", code);
            CmsBtProductModel product = cmsBtProductDao.selectByCode(code, channelId);
            // modified by morse.lu 2016/11/18 start
            // 全小写比较skuCode
//            List<String> listSkuCode = product.getCommon().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList());
            List<String> listSkuCode = product.getCommon().getSkus().stream().map(sku -> sku.getSkuCode().toLowerCase()).collect(Collectors.toList());
            // modified by morse.lu 2016/11/18 end

            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("platforms.P" + cartId + ".modified", DateTimeUtil.getNowTimeStamp());

            // deleted by morse.lu 2016/11/09 start
            // 貌似不需要都设置，只要attributes和一些共通项目
//            mapBean.forEach((id, value)-> {
//                if ("skus".equals(id)) {
//                    // 后面处理
//                } else if ("attributes".equals(id)) {
//                    // 后面处理
//                } else {
//                    updateMap.put("platforms.P" + cartId + ".fields." + id, value);
//                }
//            });
            // deleted by morse.lu 2016/11/09 end

            // 共通项目
            setCommonInfo(ware, cartId, updateMap);

            // sku
            boolean hasPublishSku = false;
            List<Sku> listSku = ware.getSkus();
            if (ListUtils.isNull(listSku)) {
                // 空的话，去外部商家编码去取
                // 取不到- -! 暂时扔个错出来，理论上不会进这个if，真碰到再单独调查，完善代码
                throw new BusinessException(String.format("外部商家编码取得失败! [numIId:%s]", wareId));
            } else {
                // sku信息暂时没什么需要回写的
                for (Sku sku : listSku) {
                    // modified by morse.lu 2016/11/18 start
                    // 全小写比较skuCode
//                    if (listSkuCode.contains(sku.getOuterId())) {
                    if (StringUtils.isEmpty(sku.getOuterId())) {
                        hasErr[0] = true;
                    } else
                    if (listSkuCode.contains(sku.getOuterId().toLowerCase())) {
                        // modified by morse.lu 2016/11/18 end
                        hasPublishSku = true;
                        break;
                    }
                }
            }
            // 回写jdSkuId START
            Map<String, String> skuIdList = new HashMap<>();
            for (Sku sku : ware.getSkus()) {
                if (!StringUtils.isEmpty(sku.getOuterId())) {
                    skuIdList.put(sku.getOuterId(), String.valueOf(sku.getSkuId()));
                }
            }
            for (int i = 0; i < product.getPlatformNotNull(cartId).getSkus().size(); i++) {
                BaseMongoMap<String, Object> sku = product.getPlatformNotNull(cartId).getSkus().get(i);
                if (skuIdList.containsKey(sku.getAttribute("skuCode"))) {
                    updateMap.put("platforms.P" + cartId + ".skus." + i + ".jdSkuId", skuIdList.get(sku.getAttribute("skuCode")));
                }
            }
            // 回写jdSkuId END

            Long longCatId = ware.getCategoryId(); // 类目ID
            if (longCatId != null) {
                String catId = Long.toString(longCatId);
                // 取到了再回写
                updateMap.put("platforms.P" + cartId + ".pCatId", catId);
                CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema(catId, cartId);
                if (cmsMtPlatformCategorySchemaModel != null) {
                    updateMap.put("platforms.P" + cartId + ".pCatPath", cmsMtPlatformCategorySchemaModel.getCatFullPath());
                    // 处理attributes
                    setAttributes(ware, cartId, updateMap, cmsMtPlatformCategorySchemaModel);
                } else {
                    updateMap.put("platforms.P" + cartId + ".pCatPath", "");
                }
            }

            // 店铺内分类
            List<Map<String, Object>> sellerCats = new ArrayList<>();
            if (!StringUtils.isEmpty(ware.getShopCategorys())) {
                String[] shopCategorys = ware.getShopCategorys().split(";"); // 用";"来分隔出多个店铺内分类
                for (String shopCategory : shopCategorys) {
                    String[] cats = shopCategory.split("-"); // 用"-"来分隔出类目层级结构
                    String catId = cats[cats.length - 1]; // 用最后一级就可以了

                    CmsBtSellerCatModel leaf = listSellerCat.stream().filter(w -> catId.equals(w.getCatId())).findFirst().get();
                    Map<String, Object> model = new HashMap<>();
                    model.put("cId", leaf.getCatId());
                    model.put("cName", leaf.getCatPath());
                    model.put("cIds", leaf.getFullCatId().split("-"));
                    model.put("cNames", leaf.getCatPath().split(">"));

                    sellerCats.add(model);
                }
            }
            updateMap.put("platforms.P" + cartId + ".sellerCats", sellerCats);

            if (hasPublishSku) {
                // 有sku上新过
                updateMap.put("platforms.P" + cartId + ".pNumIId", cmsBtProductGroup.getNumIId());
                String wareStatus = ware.getWareStatus(); // 商品状态
                if ("ON_SALE".equals(wareStatus) || "AUDIT_AWAIT".equals(wareStatus)) {
                    // ON_SALE:在售 AUDIT_AWAIT: 待审核
                    // 出售中
                    updateMap.put("platforms.P" + cartId + ".pStatus", CmsConstants.PlatformStatus.OnSale.name());
                    updateMap.put("platforms.P" + cartId + ".pReallyStatus", CmsConstants.PlatformStatus.OnSale.name());
                } else {
                    // NEVER_UP:从未上架, CUSTORMER_DOWN:自主下架 SYSTEM_DOWN:系统下架 AUDIT_FAIL: 审核不通过
                    // 仓库中
                    updateMap.put("platforms.P" + cartId + ".pStatus", CmsConstants.PlatformStatus.InStock.name());
                    updateMap.put("platforms.P" + cartId + ".pReallyStatus", CmsConstants.PlatformStatus.InStock.name());
                }
            }

            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        });

        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");

        if (hasErr[0]) {
            $warn(String.format("channelId:%s, cartId:%s, numIId:%s 存在outer_id为空的sku!", channelId, cartId, cmsBtProductGroup.getNumIId()));
        }

        // 回写group表
        String wareStatus = ware.getWareStatus(); // 商品状态
        if ("ON_SALE".equals(wareStatus) || "AUDIT_AWAIT".equals(wareStatus)) {
            // ON_SALE:在售 AUDIT_AWAIT: 待审核
            // 出售中
            cmsBtProductGroup.setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
            cmsBtProductGroup.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
        } else {
            // NEVER_UP:从未上架, CUSTORMER_DOWN:自主下架 SYSTEM_DOWN:系统下架 AUDIT_FAIL: 审核不通过
            // 仓库中
            cmsBtProductGroup.setPlatformStatus(CmsConstants.PlatformStatus.InStock);
            cmsBtProductGroup.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
        }
        productGroupService.update(cmsBtProductGroup);

    }

    /**
     * 处理共通项目
     */
    private void setCommonInfo(Ware ware, int cartId, Map<String, Object> updateMap) {
        String fieldsPath = "platforms.P" + cartId + ".fields.";
        // 标题
        updateMap.put(fieldsPath + "productTitle", ware.getTitle());

        // 商品标语
        String content = ware.getAdContent();
        if (!StringUtils.isEmpty(content)) {
            // 貌似可能拿不到，套个壳子吧，拿到了再回写
            updateMap.put(fieldsPath + "productAdContent", content);
        }

        // 长:宽:高
        String[] cubage = ware.getCubage().split(":");
        // 长(单位:mm)
        updateMap.put(fieldsPath + "productLengthMm", cubage[0]);
        // 宽(单位:mm)
        updateMap.put(fieldsPath + "productWideMm", cubage[1]);
        // 高(单位:mm)
        updateMap.put(fieldsPath + "productHighMm", cubage[2]);

        // 重量
        updateMap.put(fieldsPath + "productWeightKg", ware.getWeight());

        // 是否先款后货
        Boolean isPayFirst = ware.getPayFirst();
        if (isPayFirst != null) {
            // 貌似可能拿不到，套个壳子吧，拿到了再回写
            updateMap.put(fieldsPath + "productIsPayFirst", isPayFirst.toString());
        }

        // 是否限制开增值税发票
        Boolean isCanVat = ware.getCanVat();
        if (isCanVat != null) {
            // 貌似可能拿不到，套个壳子吧，拿到了再回写
            updateMap.put(fieldsPath + "productIsCanVat", isCanVat.toString());
        }

        // 货号
        String model = ware.getItemNum();
        if (!StringUtils.isEmpty(model)) {
            // 貌似可能拿不到，套个壳子吧，拿到了再回写
            updateMap.put(fieldsPath + "productModel", model);
        }
    }

    /**
     * 处理attributes
     */
    private void setAttributes(Ware ware, int cartId, Map<String, Object> updateMap, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel) {
        String propsItem = cmsMtPlatformCategorySchemaModel.getPropsItem();
        String wareAttributes = ware.getAttributes();
        if (!StringUtils.isEmpty(propsItem) && !StringUtils.isEmpty(wareAttributes)) {
            // 将取出的propsItem转换为字段列表
            Map<String, Field> mapFields = SchemaReader.readXmlForMap(propsItem);
            // 取得attributes
            String[] attributes = ware.getAttributes().split("\\^");
            for (String attribute : attributes) {
                String[] prop = attribute.split(":");
                String fieldId = prop[0];
                String fieldVal = prop[1];

                Field field = mapFields.get(fieldId);

                if (field == null) {
                    // 没找到，大概新出来的属性，姑且先回写成String类型
                    updateMap.put("platforms.P" + cartId + ".fields." + fieldId, fieldVal);
                } else {
                    switch (field.getType()) {
                        case INPUT:
                            updateMap.put("platforms.P" + cartId + ".fields." + fieldId, fieldVal);
                            break;
                        case MULTIINPUT:
                            // 没有
                            break;
                        case LABEL:
                            return;
                        case SINGLECHECK:
                            updateMap.put("platforms.P" + cartId + ".fields." + fieldId, fieldVal);
                            break;
                        case MULTICHECK:
                            updateMap.put("platforms.P" + cartId + ".fields." + fieldId, Arrays.asList(fieldVal.split(",")));
                            break;
                        case COMPLEX:
                            // 没有
                            break;
                        case MULTICOMPLEX:
                            // 没有
                            break;
                    }
                }
            }
        }
    }
}
