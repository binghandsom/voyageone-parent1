package com.voyageone.service.impl.cms;

import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItem;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.value.ComplexValue;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.components.tmall.service.TbScItemService;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Taobao ScItem Service
 *
 * @author tom.zhu 2016/10/08
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class TaobaoScItemService extends BaseService {

    @Autowired
    private TbItemService tbItemService;

    @Autowired
    private TbScItemService tbScItemService;

	/**
	 * 创建一个货品， 并初始化库存
	 * @param shopBean shopBean
	 * @param sku_outerId 商家编码
	 * @param title 商品标题
	 * @return 货品id（如果返回为空那就是不需要设置， 如果出错会抛出BusinessError）
	 */
	public String doCreateScItem(ShopBean shopBean, String sku_outerId, String title, String qty) {
		// 判断一下是否需要做货品绑定
		String storeCode = doCheckNeedSetScItem(shopBean);
		if (StringUtils.isEmpty(storeCode)) {
			return null;
		}

		// 检查是否发布过仓储商品
		ScItem scItem;
		try {
			scItem = tbScItemService.getScItemByOuterCode(shopBean, sku_outerId);
		} catch (ApiException e) {
			String errMsg = String.format("自动设置天猫商品全链路库存管理:检查是否发布过仓储商品:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
			throw new BusinessException(errMsg);
		}

		if (scItem == null) {
			// 没有发布过仓储商品的场合， 发布仓储商品
			try {
				scItem = tbScItemService.addScItemSimple(shopBean, title, sku_outerId);
				Thread.sleep(3000); // 一定要睡一会儿， 操作太快后面的操作目测会有70%几率失败
			} catch (ApiException e) {
				String errMsg = String.format("自动设置天猫商品全链路库存管理:发布仓储商品:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
				throw new BusinessException(errMsg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 进行库存初始化
		try {
			String errinfo = tbScItemService.doInitialInventory(shopBean, storeCode, sku_outerId, qty);
			if (!StringUtils.isEmpty(errinfo)) {
				String errMsg = String.format("自动设置天猫商品全链路库存管理:库存初始化:{outerId: %s, err_msg: %s}", sku_outerId, errinfo);
				System.out.println(errMsg);
			}
			Thread.sleep(3000); // 一定要睡一会儿， 操作太快后面的操作目测会有70%几率失败
		} catch (ApiException e) {
			String errMsg = String.format("自动设置天猫商品全链路库存管理:库存初始化:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
			throw new BusinessException(errMsg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return String.valueOf(scItem.getItemId());
	}

    /**
     * 自动设置天猫商品全链路库存管理
     * @param shopBean shopBean
     * @param numIId 天猫id
     */
    public void doSetScItem(ShopBean shopBean, long numIId) {

        // 到天猫获取商品信息
        TbItemSchema tbItemSchema;
        try {
            tbItemSchema = tbItemService.getUpdateSchema(shopBean, numIId);
        } catch (ApiException | TopSchemaException | GetUpdateSchemaFailException e) {
			if (e.toString().equals("com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException: 天猫商品服务异常，请稍后再试")) {
				try {
					tbItemSchema = tbItemService.getUpdateSchema(shopBean, numIId);
				} catch (ApiException | TopSchemaException | GetUpdateSchemaFailException e2) {
					String errMsg = String.format("自动设置天猫商品全链路库存管理:到天猫获取商品信息:调用API失败:{numIId: %s, err_msg: %s}", numIId, e2.toString());
					throw new BusinessException(errMsg);
				}
			} else {
				String errMsg = String.format("自动设置天猫商品全链路库存管理:到天猫获取商品信息:调用API失败:{numIId: %s, err_msg: %s}", numIId, e.toString());
				throw new BusinessException(errMsg);
			}
        }

        Map<String, Field> mapFields = tbItemSchema.getFieldMap();
        if (mapFields == null || mapFields.size() == 0) {
            String errMsg = String.format("自动设置天猫商品全链路库存管理:到天猫获取商品信息:获取属性失败:{numIId: %s, err_msg: %s}", numIId, "所有属性都不存在");
            throw new BusinessException(errMsg);
        }

        // 到天猫获取商品信息 - 获取标题
        String title = null;
        if (mapFields.containsKey("title")) {
            InputField inputField = (InputField) mapFields.get("title");
            title = inputField.getDefaultValue();
        }
        if (StringUtils.isEmpty(title)) {
            String errMsg = String.format("自动设置天猫商品全链路库存管理:到天猫获取商品信息:获取属性失败:{numIId: %s, err_msg: %s}", numIId, "获取标题失败");
            throw new BusinessException(errMsg);
        }

        // 到天猫获取商品信息 - 获取sku（有三种可能：在sku里， 在达尔文sku里， 以外的场合）
        // 需要的信息：sku_outerId（商家编码）、 sku_id（天猫sku id， 可能为空）、 sku_scProductId（天猫仓储产品id， 可能为空）
        boolean blnFound = false;

        {
            // 在sku里的场合
            MultiComplexField skuFields;
            if (mapFields.containsKey("sku")) {
                blnFound = true;
                skuFields = (MultiComplexField) mapFields.get("sku");

                for (ComplexValue skuFieldValue : skuFields.getDefaultComplexValues()) {
                    String outerId = skuFieldValue.getInputFieldValue("sku_outerId");
                    String skuId = skuFieldValue.getInputFieldValue("sku_id");
                    String scProductId = skuFieldValue.getInputFieldValue("sku_scProductId");
					String qty = skuFieldValue.getInputFieldValue("sku_quantity");

                    doSetScItemSku(shopBean, numIId, outerId, skuId, scProductId, title, qty);
                }
            }
        }
        if (!blnFound) {
            // 在达尔文sku里的场合
            MultiComplexField skuFields;
            if (mapFields.containsKey("darwin_sku")) {
                blnFound = true;
                skuFields = (MultiComplexField) mapFields.get("darwin_sku");

                for (ComplexValue skuFieldValue : skuFields.getDefaultComplexValues()) {
                    String outerId = skuFieldValue.getInputFieldValue("sku_outerId");
                    String skuId = skuFieldValue.getInputFieldValue("sku_id");
                    String scProductId = skuFieldValue.getInputFieldValue("sku_scProductId");
					String qty = skuFieldValue.getInputFieldValue("sku_quantity");

                    doSetScItemSku(shopBean, numIId, outerId, skuId, scProductId, title, qty);
                }
            }
        }
        if (!blnFound) {
            // 上记以外的场合
            String outerId = ((InputField)mapFields.get("outer_id")).getDefaultValue();
            String scProductId = ((InputField)mapFields.get("item_sc_product_id")).getDefaultValue();
			String qty = ((InputField)mapFields.get("")).getDefaultValue();

            doSetScItemSku(shopBean, numIId, outerId, null, scProductId, title, qty);
        }


    }

	/**
	 * 判断当前这家店， 是否需要做货品绑定
	 * @param shopBean shopBean
	 * @return 商家仓库编码（如果返回内容为null， 那么就不需要做）
	 */
	public String doCheckNeedSetScItem(ShopBean shopBean) {
		// 获取当前channel的配置
		CmsChannelConfigBean scItemConfig = CmsChannelConfigs.getConfigBean(shopBean.getOrder_channel_id(), CmsConstants.ChannelConfig.SCITEM, shopBean.getCart_id());

		String useScItem = null;
		String storeCode = null;
		if (scItemConfig != null) {
			useScItem = org.apache.commons.lang3.StringUtils.trimToNull(scItemConfig.getConfigValue1());
			storeCode = org.apache.commons.lang3.StringUtils.trimToNull(scItemConfig.getConfigValue2());
		}

		// 如果没有配置： 不需要做货品绑定
		// 如果商家仓库编码为空： 不需要做货品绑定
		if (StringUtils.isEmpty(useScItem) || !"1".equals(useScItem) || StringUtils.isEmpty(storeCode)) {
			return null;
		}

		return storeCode;

//		return "BABCMAXAZRIA001";

	}

    private String doSetScItemSku(ShopBean shopBean, long numIId, String sku_outerId, String sku_id, String sku_scProductId, String title, String qty) {

		// 判断一下是否需要做货品绑定
		String storeCode = doCheckNeedSetScItem(shopBean);
		if (StringUtils.isEmpty(storeCode)) {
			return null;
		}

        // 检查sku是否已经关联
        if (!StringUtils.isEmpty(sku_scProductId)) {
            // 已经关联过了， 无需处理
            return "已经绑定过了:" + sku_outerId;
        }

        // 创建货品（如果没有创建过的话）并初始化库存
		doCreateScItem(shopBean, sku_outerId, title, qty);

		// 创建关联
		String outerCodeResult;
        try {
            if (StringUtils.isEmpty(sku_id)) {
                outerCodeResult = tbScItemService.addScItemMap(shopBean, numIId, null, sku_outerId);
            } else {
                outerCodeResult = tbScItemService.addScItemMap(shopBean, numIId, Long.parseLong(sku_id), sku_outerId);
            }
            System.out.println("关联成功:" + outerCodeResult);
        } catch (ApiException e) {
            String errMsg = String.format("自动设置天猫商品全链路库存管理:创建关联:{numIId: %s, outerId: %s, err_msg: %s}", numIId, sku_outerId, e.toString());
            throw new BusinessException(errMsg);
        }

        if (outerCodeResult == null) {
			return null;
		} else {
			return outerCodeResult;
		}

    }

}
