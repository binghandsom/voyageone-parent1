package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.SingleCheckField;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.impl.BaseService;
import com.voyageone.task2.cms.bean.platform.SxData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 天猫平台上新用产品相关服务
 *
 * @author desmond on 2016/5/11.
 * @version 2.0.0
 */
public class CmsBuildPlatformProductUploadTmProductService extends BaseService {

    @Autowired
    private TbProductService tbProductService;
    @Autowired
//    private BrandMapDao brandMapDao;

    /**
     * 去天猫(天猫国际)上去寻找是否有存在这个product
     * @param sxData 商品信息
     * @return 返回的天猫productId (如果没有找到, 返回null)
     */
    public Long getProductIdFromTmall(SxData sxData){
        Long productId = 0L;

        // 获取匹配产品规则
        List<Field> fieldList;
        try {
            String strXml = tbProductService.getProductMatchSchema(Long.parseLong(sxData.getPlatformCategoryId()), sxData.getShopBean());

            fieldList = SchemaReader.readXmlForList(strXml);
        } catch (ApiException e) {
            String errMsg = String.format("天猫获取匹配产品规则失败(调用天猫API异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), sxData.getPlatformCategoryId());
            $error(errMsg);
            // 调用API异常
            return productId;
        } catch (TopSchemaException e) {
            String errMsg = String.format("天猫获取匹配产品规则失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId(), sxData.getPlatformCategoryId());
            $error(errMsg);
            // 解析XML异常
            return productId;
        }

        // 设置值
        List<Field> fieldListSearch = new ArrayList<>();
        for (Field field : fieldList) {
            String propId = getPropIdByTmallPropId(field.getId());

            fieldListSearch.add(getPlatformValue(propId, sxData, field));
        }

        // 匹配产品
        try {
            String strPropXml = SchemaWriter.writeParamXmlString(fieldListSearch);
            String[] strProductList = tbProductService.matchProduct(Long.parseLong(sxData.getPlatformCategoryId()), strPropXml, sxData.getShopBean());
            if (strProductList != null && strProductList.length > 0) {
                productId = Long.parseLong(strProductList[0]);
            }
        } catch (TopSchemaException e) {
            // 解析XML异常
            return productId;
        } catch (ApiException e) {
            // 调用API异常
            return productId;
        }

        return productId;
    }

    /**
     * 特殊属性id的匹配
     * TODO: 特殊属性id的匹配, 暂时这里写一下, 之后要移到数据库里
     * @param tmallPropId 天猫上的属性id
     * @return 转换过的共通的属性id (非特殊属性id, 直接原样返回)
     */
    private String getPropIdByTmallPropId(String tmallPropId) {
        switch (tmallPropId) {
            case "prop_13021751": {
                // 款号
                return "SpStyleCode";
            }
            case "prop_20000": {
                // 品牌
                return "SpBrand";
            }
            default: {
                // 非特殊, 直接原样返回
                return tmallPropId;
            }
        }

    }

    /**
     * 获取平台的属性的值的入口
     * @param propId 属性id
     * @param sxData 商品基本信息
     */
    public Field getPlatformValue(String propId, SxData sxData, Field field) {

        if ("SpStyleCode".equals(propId)) {
            // 款号
            InputField inputField = (InputField) field;

            // 非达尔文的商品直接用model
            // TODO: 达尔文商品需要先去这个地方检索一下: https://product.tmall.com/futures/futuresItem.htm
            String styleCode = sxData.getProductList().get(0).getFields().getModel();
            inputField.setValue(styleCode);

            return inputField;

        } else if ("SpBrand".equals(propId)) {
            // 品牌
            SingleCheckField singleCheckField = (SingleCheckField) field;

            singleCheckField.setValue(
                    getBrand(sxData.getChannelId(), sxData.getCartId(), sxData.getProductList().get(0).getFields().getBrand())
            );

            return singleCheckField;

        } else if ("SpSku".equals(propId)) {
            // Sku

            return null;
        } else {
            // 其他属性的场合, 使用mapping

            return null;
        }

    }

    /**
     * 根据主数据的品牌, 获取platform的brand
     * @param channelId channel id
     * @param cartId cart id
     * @param brand brand
     * @return platform的brand
     */
    public String getBrand(String channelId, int cartId, String brand) {
        // TODO 要修改
//        return brandMapDao.cmsBrandToPlatformBrand(channelId, cartId, brand);
        return "";
    }



}
