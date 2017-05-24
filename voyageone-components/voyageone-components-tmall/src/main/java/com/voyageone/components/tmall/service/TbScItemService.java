package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.InventorySum;
import com.taobao.api.domain.ScItem;
import com.taobao.api.domain.ScItemMap;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.TbBase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tom.zhu on 2016-10-08.
 */
@Component
public class TbScItemService extends TbBase {
	 // 小知识:
	 //  天猫前台商品: ic商品
	 //  仓储商品: sc商品

	/**
	 * 菜鸟新规定， 后端货品的sku， 仅支持数字、字母、下划线、横杠, 25个字符, 所以我们就转换一下
	 * @param sku
	 * @return
	 */
	private String getRealScOuterCodeBySku(String sku) {
//		String regExTmp = "[-_a-zA-Z0-9]";
//
		if (StringUtils.isEmpty(sku)) {
			return "";
		}
//
//		StringBuilder sb = new StringBuilder();
//		String result;
//
//		for (int i = 0; i < sku.length(); i ++) {
//			String c = sku.substring(i, i + 1);
//
//			Pattern pattern = Pattern.compile(regExTmp, Pattern.CASE_INSENSITIVE);
//			Matcher matcher = pattern.matcher(c);
//
//			if (matcher.find()) {
//				// OK的字符
//				sb.append(c);
//			}
//
//		}
//
//		result = sb.toString();
//
//		if (result.length() > 25) {
//			result = result.substring(0, 25);
//		}
//
//		return result;

		return MD5.getMd5_16(sku);
	}

	/**
	 * taobao.scitem.outercode.get (根据outerCode查询仓储商品)
	 *
	 * @param shopBean  shopBean
	 * @param outerCode 商家编码
	 * @return sc商品(仓储商品) (如果发生异常, 返回null)
	 * @throws ApiException
	 */
	public ScItem getScItemByOuterCode(ShopBean shopBean, String outerCode) throws ApiException {

		ScitemOutercodeGetRequest request = new ScitemOutercodeGetRequest();

		request.setOuterCode(getRealScOuterCodeBySku(outerCode));

		ScitemOutercodeGetResponse res = reqTaobaoApi(shopBean, request);

//		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
//			request.setOuterCode(outerCode);
//
//			res = reqTaobaoApi(shopBean, request);
//		}

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			return null;
		}

		return res.getScItem();

	}

	/**
	 * taobao.scitem.add (发布后端商品) (简易模式, 只需传入商品标题和商家编码)
	 * @param shopBean shopBean
	 * @param itemName 商品标题
	 * @param outerCode 商家编码
	 * @return sc商品(仓储商品) (如果发生异常, 返回null, 如果正常创建, 返回正常创建好的商品)
	 * @throws ApiException
	 */
	public ScItem addScItemSimple(ShopBean shopBean, String itemName, String outerCode) throws ApiException {

		ScitemAddRequest request = new ScitemAddRequest();

		request.setItemName(itemName);
		request.setOuterCode(getRealScOuterCodeBySku(outerCode));
		request.setWeight(1L);

		ScitemAddResponse res = reqTaobaoApi(shopBean, request);

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			return null;
		}

		return res.getScItem();

	}

	/**
	 * taobao.scitem.map.add (创建IC商品与后端商品的映射关系)
	 * @param shopBean shopBean
	 * @param numIId 天猫前台商品id
	 * @param skuId 天猫前台商品的sku id
	 * @param outerCode 商家编码
	 * @return 商家编码(如果发生异常, 返回null)
	 * @throws ApiException
	 */
	public String addScItemMap(ShopBean shopBean, long numIId, Long skuId, String outerCode) throws ApiException {

		ScitemMapAddRequest request = new ScitemMapAddRequest();

		request.setItemId(numIId);
		if (skuId != null) {
			request.setSkuId(skuId);
		}
		request.setOuterCode(getRealScOuterCodeBySku(outerCode));

		ScitemMapAddResponse res = reqTaobaoApi(shopBean, request);

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			throw new BusinessException(res.getSubCode() + ":" + res.getSubMsg());
		}

		return res.getOuterCode();

	}

	/**
	 * taobao.scitem.map.delete (失效指定用户的商品与后端商品的映射关系)
	 * @param shopBean shopBean
	 * @param sc_item_id 	后台商品ID
	 * @return
	 * @throws ApiException
	 */
	public void deleteScItemMap(ShopBean shopBean, long sc_item_id) throws ApiException {

		ScitemMapDeleteRequest request = new ScitemMapDeleteRequest();

		request.setScItemId(sc_item_id);

		ScitemMapDeleteResponse res = reqTaobaoApi(shopBean, request);

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			throw new BusinessException(res.getSubCode() + ":" + res.getSubMsg());
		}

	}

	/**
	 * taobao.inventory.initial (库存初始化)
	 * @param shopBean shopBean
	 * @param store_code 商家仓库编码
	 * @param scItemCode 商家编码
	 * @param quantity 库存
	 * @return
	 * @throws ApiException
	 */
	public String doInitialInventory(ShopBean shopBean, String store_code, String scItemCode, String quantity) throws ApiException {

		InventoryInitialRequest request = new InventoryInitialRequest();

		request.setStoreCode(store_code);
		String items = "[{\"scItemId\":\"0\",\"scItemCode\":\"%s\",\"inventoryType\":\"1\",\"quantity\":\"%s\"}]";
		items = String.format(items, getRealScOuterCodeBySku(scItemCode), quantity);
		request.setItems(items);

		InventoryInitialResponse res = reqTaobaoApi(shopBean, request);

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			if (res.getSubMsg() == null || !res.getSubMsg().startsWith("商品已初始化")) {
				throw new BusinessException(res.getSubCode() + ":" + res.getSubMsg());
			}
		}

		return null;
	}

	/**
	 * taobao.scitem.map.query (查找IC商品或分销商品与后端商品的关联信息)
	 * @param shopBean shopBean
	 * @param numIId 天猫id
	 * @param skuId skuId（如果不知道， 就传null）
	 * @return （符合条件的sku的）关联信息列表
	 * @throws ApiException
	 */
	public List<ScItemMap> getScItemMap(ShopBean shopBean, long numIId, String skuId) throws ApiException {
		ScitemMapQueryRequest request = new ScitemMapQueryRequest();

		request.setItemId(numIId);
		if (!StringUtils.isEmpty(skuId)) {
			request.setSkuId(Long.parseLong(skuId));
		}

		ScitemMapQueryResponse res = reqTaobaoApi(shopBean, request);

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			return null;
		}

		return res.getScItemMaps();
	}

	/**
	 * taobao.inventory.query (查询商品库存信息)
	 * @param shopBean shopBean
	 * @param scItemId 后端货品id
	 * @return （符合条件的sku的）关联信息列表
	 * @throws ApiException
	 */
	public List<InventorySum> getInventoryByScItemId(ShopBean shopBean, long scItemId) throws ApiException {
		InventoryQueryRequest request = new InventoryQueryRequest();

		request.setScItemIds(String.valueOf(scItemId));

		InventoryQueryResponse res = reqTaobaoApi(shopBean, request);

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			return null;
		}

		return res.getItemInventorys();
	}

	/**
	 * taobao.scitem.get (根据id查询商品)
	 * @param shopBean shopBean
	 * @param numIId 天猫id
	 * @return （符合条件的sku的）关联信息列表
	 * @throws ApiException
	 */
	public ScItem getScitemByNumIId(ShopBean shopBean, long numIId) throws ApiException {
		ScitemGetRequest request = new ScitemGetRequest();

		request.setItemId(numIId);

		ScitemGetResponse res = reqTaobaoApi(shopBean, request);

		if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) {
			return null;
		}

		return res.getScItem();
	}

}
