package com.voyageone.service.impl.cms;

import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItem;
import com.taobao.api.domain.ScItemMap;
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
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.components.tmall.service.TbScItemService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
	 * @param sxData sxData
	 * @return 货品id（如果返回为空那就是不需要设置， 如果出错会抛出BusinessError）
	 */
	public String doCreateScItem(ShopBean shopBean, CmsBtProductModel productModel, String sku_outerId, SxData sxData, String qty) {
		// 判断一下是否需要做货品绑定
		String storeCode = doCheckNeedSetScItem(shopBean, productModel);
		if (StringUtils.isEmpty(storeCode)) {
			return null;
		}

		ScItem scItem = sxData.getScItemMap().get(sku_outerId);

		if (scItem == null) {
			// 如果没有创建成功， 不需要做库存初始化， 直接跳出
			return null;
		}
		// 进行库存初始化
		try {
			String errinfo = tbScItemService.doInitialInventory(shopBean, storeCode, sku_outerId, qty);
			if (!StringUtils.isEmpty(errinfo)) {
				String errMsg = String.format("自动设置天猫商品全链路库存管理:库存初始化:{outerId: %s, err_msg: %s}", sku_outerId, errinfo);
				System.out.println(errMsg);
			}
			Thread.sleep(500); // 一定要睡一会儿， 操作太快后面的操作目测会有70%几率失败
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
    public void doSetScItem(ShopBean shopBean, SxData sxData, long numIId) {

		CmsBtProductModel productModel = sxData.getMainProduct();

		// 判断一下是否需要做货品绑定
		String storeCode = doCheckNeedSetScItem(shopBean, productModel);
		if (StringUtils.isEmpty(storeCode)) {
			return;
		}

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
            if (mapFields.containsKey("sku") && mapFields.get("sku") != null) {
                skuFields = (MultiComplexField) mapFields.get("sku");

                for (ComplexValue skuFieldValue : skuFields.getDefaultComplexValues()) {
					blnFound = true;
                    String outerId = skuFieldValue.getInputFieldValue("sku_outerId");
                    String skuId = skuFieldValue.getInputFieldValue("sku_id");
                    String scProductId = skuFieldValue.getInputFieldValue("sku_scProductId");
					String qty = skuFieldValue.getInputFieldValue("sku_quantity");

                    doSetScItemSku(shopBean, productModel, numIId, outerId, skuId, scProductId, sxData, qty);
                }
            }
        }
        if (!blnFound) {
            // 在达尔文sku里的场合
            MultiComplexField skuFields;
            if (mapFields.containsKey("darwin_sku") && mapFields.get("darwin_sku") != null) {
                skuFields = (MultiComplexField) mapFields.get("darwin_sku");

                for (ComplexValue skuFieldValue : skuFields.getDefaultComplexValues()) {
					blnFound = true;
                    String outerId = skuFieldValue.getInputFieldValue("sku_outerId");
                    String skuId = skuFieldValue.getInputFieldValue("sku_id");
                    String scProductId = skuFieldValue.getInputFieldValue("sku_scProductId");
					String qty = skuFieldValue.getInputFieldValue("sku_quantity");

                    doSetScItemSku(shopBean, productModel, numIId, outerId, skuId, scProductId, sxData, qty);
                }
            }
        }
        if (!blnFound) {
            // 上记以外的场合
            String outerId = ((InputField)mapFields.get("outer_id")).getDefaultValue();
            String scProductId = ((InputField)mapFields.get("item_sc_product_id")).getDefaultValue();
			String qty = ((InputField)mapFields.get("quantity")).getDefaultValue();

            doSetScItemSku(shopBean, productModel, numIId, outerId, null, scProductId, sxData, qty);
        }


    }

	/**
	 * 判断当前这家店， 是否需要做货品绑定
	 * @param shopBean shopBean
	 * @return 商家仓库编码（如果返回内容为null， 那么就不需要做）
	 */
	public String doCheckNeedSetScItem(ShopBean shopBean, CmsBtProductModel productModel) {
		int cartId = Integer.valueOf(shopBean.getCart_id());
		if (!Boolean.parseBoolean(productModel.getPlatform(cartId).getFields().getStringAttribute("customsClearanceWay"))) {
			// 入关方案=false(邮关)
			// 不做货品绑定
			return null;
		}
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

	// 返回的是商家编码
    private String doSetScItemSku(ShopBean shopBean, CmsBtProductModel productModel, long numIId, String sku_outerId, String sku_id, String sku_scProductId, SxData sxData, String qty) {

		// 判断一下是否需要做货品绑定
		String storeCode = doCheckNeedSetScItem(shopBean, productModel);
		if (StringUtils.isEmpty(storeCode)) {
			return null;
		}

        // 检查sku是否已经关联
        if (!StringUtils.isEmpty(sku_scProductId)) {
            // 已经关联过了， 无需处理
            return "已经绑定过了:" + sku_outerId;
        }

        // 创建货品（如果没有创建过的话）并初始化库存
		doCreateScItem(shopBean, productModel, sku_outerId, sxData, qty);

		// 创建关联
		String outerCodeResult;
        try {
            if (StringUtils.isEmpty(sku_id) || "null".equals(sku_id)) {
				sku_id = null;
            }
			// 先去看看是否已经创建过关联了
			List<ScItemMap> scItemMapList = tbScItemService.getScItemMap(shopBean, numIId, sku_id);
			if (scItemMapList != null && scItemMapList.size() > 0) {
				// 已经创建过关联了， 跳出
				System.out.println("已经创建过关联了， 不重新关联");
				return String.valueOf(sku_outerId);
			}
			outerCodeResult = tbScItemService.addScItemMap(shopBean, numIId, sku_id, sku_outerId);
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

	/**
	 * 自动设置天猫商品全链路库存管理(Liking)
	 * @param shopBean shopBean
	 * @param sxData sxData
	 * @param skuMap {outer_id: xx, price: xx, quantity: xx, sku_id: xx}
	 */
	public String doSetLikingScItem(ShopBean shopBean, SxData sxData, long numIId, Map<String, Object> skuMap, Map<String, String> skuIdScIdMap) {

		String orgChannelId = sxData.getMainProduct().getOrgChannelId();

		// Liking天猫国际同购店， 获取子店货品绑定的 天猫商家仓库编码
		String storeCode = doGetLikingStoreCode(shopBean, orgChannelId);
		if (StringUtils.isEmpty(storeCode)) {
			return null;
		}

		// 遍历输入的内容， 设置货品
		String outerId = String.valueOf(skuMap.get("outer_id"));
		String skuId = String.valueOf(skuMap.get("sku_id")); // 可能为null
		String qty = String.valueOf(skuMap.get("quantity"));
		if (StringUtils.isNullOrBlank2(skuId)) {
			if (!StringUtils.isEmpty(skuIdScIdMap.get("0"))) {
				return skuIdScIdMap.get("0");
			}
		} else {
			if (!StringUtils.isEmpty(skuIdScIdMap.get(skuId))) {
				return skuIdScIdMap.get(skuId);
			}
		}

		ScItem scItem = sxData.getScItemMap().get(outerId);

		return doSetLikingScItemSku(shopBean, orgChannelId, numIId, outerId, skuId, qty, scItem);

	}

	/**
	 * Liking天猫国际同购店， 获取子店货品绑定的 天猫商家仓库编码
	 * @param shopBean ShopBean
	 * @param orgChannelId orgChannelId
	 * @return 商家仓库编码（如果返回内容为null， 那么就有问题了， 估计是没有配置）
	 */
	public String doGetLikingStoreCode(ShopBean shopBean, String orgChannelId) {
		int cartId = Integer.valueOf(shopBean.getCart_id());

		// 获取当前channel的配置
		CmsChannelConfigBean scItemConfig = CmsChannelConfigs.getConfigBean(shopBean.getOrder_channel_id(), CmsConstants.ChannelConfig.SCITEM, cartId + "_" + orgChannelId);

		String useScItem = null;
		String storeCode = null;
		if (scItemConfig != null) {
			useScItem = org.apache.commons.lang3.StringUtils.trimToNull(scItemConfig.getConfigValue1());
			storeCode = org.apache.commons.lang3.StringUtils.trimToNull(scItemConfig.getConfigValue2());
		}

		// 如果没有配置： 出错
		// 如果商家仓库编码为空： 出错
		if (StringUtils.isEmpty(useScItem) || !"1".equals(useScItem) || StringUtils.isEmpty(storeCode)) {
			return null;
		}

		return storeCode;

	}

	private String doSetLikingScItemSku(
			ShopBean shopBean, String orgChannelId,
			long numIId, String sku_outerId, String sku_id, String qty, ScItem scItem) {

		// Liking天猫国际同购店， 获取子店货品绑定的 天猫商家仓库编码
		String storeCode = doGetLikingStoreCode(shopBean, orgChannelId);
		if (StringUtils.isEmpty(storeCode)) {
			return null;
		}

		// 创建货品（如果没有创建过的话）并初始化库存
		{
			// 进行库存初始化
			if (scItem == null) {
				// 如果没有创建成功， 不需要做库存初始化， 直接跳出
				return null;
			}

			try {
				String errinfo = tbScItemService.doInitialInventory(shopBean, storeCode, sku_outerId, qty);
				if (!StringUtils.isEmpty(errinfo)) {
					String errMsg = String.format("自动设置天猫商品全链路库存管理:库存初始化:{outerId: %s, err_msg: %s}", sku_outerId, errinfo);
					System.out.println(errMsg);
				}
				Thread.sleep(500); // 一定要睡一会儿， 操作太快后面的操作目测会有70%几率失败
			} catch (ApiException e) {
				String errMsg = String.format("自动设置天猫商品全链路库存管理:库存初始化:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
				throw new BusinessException(errMsg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		// 创建关联
		String outerCodeResult;
		try {
			Thread.sleep(1000); // 一定要睡一会儿， 因为如果商品都已经创建成功并初始化过的话， 就会连续创建关联， 可能会导致IC_OPTIMISTIC_LOCKING_CONFLICT
			outerCodeResult = tbScItemService.addScItemMap(shopBean, numIId, sku_id, sku_outerId);

			System.out.println("关联成功:" + outerCodeResult);
		} catch (InterruptedException e) {
			String errMsg = String.format("自动设置天猫商品全链路库存管理:创建关联[之前睡会儿失败]:{numIId: %s, outerId: %s, err_msg: %s}", numIId, sku_outerId, e.toString());
			throw new BusinessException(errMsg);
		} catch (Exception e) {
			if (e.toString().contains("您输入的前端商品已挂靠至该货品上")) {
				return String.valueOf(scItem.getItemId());
			}
            String errMsg = String.format("关联失败:{outerId: %s, err_msg: %s !}", sku_outerId, e.toString());
			throw new BusinessException(errMsg);
		}

		if (outerCodeResult == null) {
			return null;
		} else {
			return String.valueOf(scItem.getItemId());
		}

	}

}
