package com.voyageone.service.impl.cms.sx;

import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.utils.JsonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.service.bean.cms.cn.CnCategoryBean;
import com.voyageone.service.dao.cms.CmsBtSxCnProductSellercatDao;
import com.voyageone.service.daoext.cms.CmsBtSxCnProductSellercatDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtSxCnProductSellercatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 独立域名类目Service
 *
 * @author morse on 2016/9/20
 * @version 2.6.0
 */
@Service
public class CnCategoryService extends BaseService {

    @Autowired
    private CnSchemaService cnSchemaService;

    @Autowired
    private CmsBtSxCnProductSellercatDao cmsBtSxCnProductSellercatDao;
    @Autowired
    private CmsBtSxCnProductSellercatDaoExt cmsBtSxCnProductSellercatDaoExt;

    /**
     * 创建CnCategoryBean(要推送的属性)
     *
     * @param catFullId 完整的类目路径，例:1>1.2>1.2.1
     * @param separator 类目路径的分隔符，例:>
     * @param name 类目名
     * @param description 类目描述(画面上显示内容)
     * @return
     */
    public CnCategoryBean createCnCategoryBean(String catFullId, String separator, String name, String description) {
        CnCategoryBean bean = new CnCategoryBean();

        String[] catIds = catFullId.split(separator);
        int length = catIds.length;

        bean.setId(catIds[length - 1]); // 类目id
        bean.setUrlKey(catIds[length - 1]); // 唯一（暂定用类目Id）
        if (length > 1) {
            bean.setParentId(catIds[length - 2]); // 父类目Id
        }
        StringBuilder catFullPath = new StringBuilder("");
        for (int i = 0; i < length; i++) {
            catFullPath.append(catIds[i]);
            if (i != length - 1) {
                // 不是最后一个，增加分隔符
                catFullPath.append("/");
            }
        }
        bean.setCategoryPath(catFullPath.toString()); // 类目的path
        bean.setName(name);
        bean.setHeaderTitle(description);

        return bean;
    }

    /**
     * 上传类目(单个)
     *
     * @param bean
     * @param isDelete
     * @return
     */
    public boolean uploadCnCategory(CnCategoryBean bean, boolean isDelete) {
        return uploadCnCategory(new ArrayList<CnCategoryBean>(){{this.add(bean);}}, isDelete);
    }

    /**
     * 上传类目(批量)
     *
     * @param listBean
     * @param isDelete
     * @return
     */
    public boolean uploadCnCategory(List<CnCategoryBean> listBean, boolean isDelete) {
        for (CnCategoryBean bean : listBean) {
            if (isDelete) {
                bean.setIsPublished("0");
                bean.setIsVisibleOnMenu("0");
            } else {
                bean.setIsPublished("1");
                bean.setIsVisibleOnMenu("1");
            }
        }

        return uploadCnCategory(listBean);
    }

    /**
     * 上传类目(批量)
     *
     * @param listBean
     * @return
     */
    public boolean uploadCnCategory(List<CnCategoryBean> listBean) {
        boolean isSuccess = false;

        List<List<Field>> listCatField = new ArrayList<>();
        for (CnCategoryBean bean : listBean) {
            Map<String, Object> mapBean;
            // modified by morse.lu 2016/09/20 start
            // BeanUtils.describe顺序会乱掉,key也会开头变成小写
            // 用json转换过渡下吧
//            try {
//                mapBean = BeanUtils.describe(bean);
//            } catch (Exception e) {
//                throw new BusinessException("类目Bean转换Map失败!请联系管理员!");
//            }
            mapBean = JsonUtil.jsonToMap(JsonUtil.getJsonString(bean));
            // modified by morse.lu 2016/09/20 end

            List<Field> listField = new ArrayList<>();
            listCatField.add(listField);

            mapBean.forEach((key, val) -> {
                InputField field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                listField.add(field);
                field.setId(key);
                if (val != null) {
                    if (key.equals("DisplayOrder")) {
                        // int型经过json转换后会变成double型，toString的话会带小数，暂时写死一下
                        field.setValue(Integer.toString(((Double) val).intValue()));
                    } else {
                        field.setValue(val.toString());
                    }
                }
            });
        }

        String xml = cnSchemaService.writeCategoryXmlString(listCatField);
        $info("独立域名上传类目xml:" + xml);

        // TODO:doPost

        return isSuccess;
    }

    /**
     * 更新cms_bt_sx_cn_product_sellercat，用于之后上传类目下code以及排序
     * 找到更新，找不到插入
     *
     * @param channelId 渠道id
     * @param catId 类目id
     * @return 更新件数
     */
    public int updateProductSellercatForUpload(String channelId, String catId, String modifier) {
        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("channelId", channelId);
        searchParam.put("catId", catId);
        CmsBtSxCnProductSellercatModel model = cmsBtSxCnProductSellercatDao.selectOne(searchParam);

        int updateCnt;
        if (model == null) {
            // insert
            model = new CmsBtSxCnProductSellercatModel();
            model.setChannelId(channelId);
            model.setCatId(catId);
            model.setUpdFlg("0");
            model.setCreater(modifier);
            updateCnt = cmsBtSxCnProductSellercatDao.insert(model);
        } else {
            // update
            model.setUpdFlg("0");
            model.setModifier(modifier);
            model.setModified(DateTimeUtil.getDate());
            updateCnt = cmsBtSxCnProductSellercatDao.update(model);
        }

        return updateCnt;
    }

    /**
     * 批量更新cms_bt_sx_cn_product_sellercat，用于之后上传类目下code以及排序
     * 找到更新，找不到插入
     *
     * @param channelId 渠道id
     * @param listCatId 类目id列表
     * @return 更新件数
     */
    public int updateProductSellercatForUpload(String channelId, Set<String> listCatId, String modifier) {
        int updateCnt = 0;

        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("channelId", channelId);

        List<CmsBtSxCnProductSellercatModel> listInsertData = new ArrayList<>();
        List<String> listUpdateData = new ArrayList<>();
        for (String catId : listCatId) {
            searchParam.put("catId", catId);
            CmsBtSxCnProductSellercatModel findModel = cmsBtSxCnProductSellercatDao.selectOne(searchParam);
            if (findModel == null) {
                // insert
                CmsBtSxCnProductSellercatModel model = new CmsBtSxCnProductSellercatModel();
                model.setChannelId(channelId);
                model.setCatId(catId);
                model.setCreater(modifier);
                listInsertData.add(model);
            } else {
                listUpdateData.add(catId);
            }
        }

        if (!listInsertData.isEmpty()) {
            updateCnt += cmsBtSxCnProductSellercatDaoExt.insertByList(listInsertData);
        }
        if (!listUpdateData.isEmpty()) {
            updateCnt += updateProductSellercatUpdFlg(channelId, listUpdateData, "0", modifier);
        }

        $info("cms_bt_sx_cn_product_sellercat更新了%d件!", updateCnt);
        return updateCnt;
    }

    /**
     * 批量更新cms_bt_sx_cn_product_sellercat状态
     *
     * @param channelId 渠道id
     * @param listCatId 类目id列表
     * @param updFlg 更新成的状态  0:未处理, 1:已处理
     * @return 更新件数
     */
    public int updateProductSellercatUpdFlg(String channelId, List<String> listCatId, String updFlg, String modifier) {
        int updateCnt = cmsBtSxCnProductSellercatDaoExt.updateFlgByCatIds(channelId, updFlg, modifier, listCatId);
        $info("cms_bt_sx_cn_product_sellercat状态更新了%d件!", updateCnt);
        return updateCnt;
    }

    /**
     * 检索等待上传的类目列表
     *
     * @param channelId 渠道id
     */
    public List<String> selectListWaitingUpload(String channelId) {
        return cmsBtSxCnProductSellercatDaoExt.selectListWaitingUpload(channelId);
    }
}