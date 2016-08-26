package com.voyageone.task2.cms.schema;

import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.rule.RequiredRule;
import com.taobao.top.schema.rule.Rule;
import com.taobao.top.schema.rule.TipRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/8/22.
 */
public class SimpleSchemaAdd {
	public static void main(String[] args) {
		doCreateProduct();
//		doCreateItem();
	}

	/**
	 * product: 用于显示在cms画面上
	 */
	private static void doCreateProduct() {
		List<Field> fieldList = new ArrayList<>();

		fieldList.add(createInputField("title", "标题", true, null));

		fieldList.add(createInputField("logistics_weight", "物流重量", true, "单位:千克"));
		fieldList.add(createInputField("logistics_volume", "物流体积", true, "运费是基于重量, 所以这里可以随意填写"));
		fieldList.add(createInputField("logistics_template_id", "物流模板ID", true, null));
		fieldList.add(createInputField("logistics_province", "国家", true, "非港澳台, 直接填写中文的国家即可", "台湾的商家, 填台湾", "香港的商家, 填香港"));
		fieldList.add(createInputField("logistics_city", "城市", true, "非港澳台, 直接填写中文的国家即可", "台湾支持的地区: 台北， 高雄， 台南， 金门， 南投， 基隆， 新竹， 嘉义， 新北， 宜兰， 桃园， 苗栗， 彰化， 云林， 屏东， 台东， 花莲， 澎湖， 连江", "香港支持的地区: 香港岛, 九龙, 新界"));

		fieldList.add(createInputField("extends_nationality", "官网来源代码， 比如US， 用于显示国旗", true, "美国US， 英国UK， 澳大利亚AU， 加拿大CA， 德国DE， 西班牙ES， 法国FR， 香港HK， 意大利IT， 日本JP， 韩国KR， 荷兰NL， 新西兰NZ， 台湾TW， 新加坡SG"));
		fieldList.add(createInputField("extends_currency_type", "币种代码， 比如USD", true, "人民币CNY， 港币HKD， 新台币TWD， 美元USD， 英镑GBP， 日元JPY， 韩元KRW， 欧元EUR， 加拿大元CAD， 新加坡元SGD， 澳元AUD， 新西兰元NZD"));
		fieldList.add(createInputField("extends_translate", "是否需要自动翻译", true, "true, false"));
		fieldList.add(createInputField("extends_source_language", "商品原始语言", true, "中文zh， 繁体zt， 英文en， 韩文ko"));
		fieldList.add(createInputField("extends_website_name", "官网名称", true, "比如sneakerhead美国官网"));
		fieldList.add(createInputField("extends_website_url", "官网商品地址", true, "如果不知道， 可以填官网首页"));
		fieldList.add(createInputField("extends_foreign_origin_price", "参考价格", false, null));
		fieldList.add(createInputField("extends_original_title", "是否自动插入商品关键词", false, "true, false, 默认false， 比如自动插入： X国 官网 直邮 进口"));
		fieldList.add(createInputField("extends_shop_cats", "店铺内分类id", false, null));
		fieldList.add(createInputField("extends_tax_free", "是否包税", false, "true, false， 不填写默认false不包税"));
		fieldList.add(createInputField("extends_international_size_table", "尺码表图片", false, "阿里图片空间里的图片地址"));
		fieldList.add(createInputField("extends_delivery_separate", "是否单独发货", false, "true, false, 默认false， 建议超过1000人民币的商品单独发货， 然后就会自动拆单， 需要付两份运费"));
		fieldList.add(createInputField("extends_support_refund", "支持退货", true, "true, false"));
		fieldList.add(createInputField("extends_hot_sale", "官网是否热卖", false, "true, false"));
		fieldList.add(createInputField("extends_new_goods", "在官网是否是新品", false, "true, false"));
		fieldList.add(createInputField("extends_cross_border_report", "跨境申报/邮关", true, "true跨税申报， false邮关"));

		try {
			String xml = SchemaWriter.writeRuleXmlString(fieldList);
			System.out.println(xml);
		} catch (TopSchemaException e) {
			e.printStackTrace();
		}
	}

	/**
	 * item: 用于上新
	 */
	private static void doCreateItem() {
		List<Field> fieldList = new ArrayList<>();

		fieldList.add(createInputField("title", "标题", true, null));
		fieldList.add(createInputField("category", "类目", true, "可以直接用商家自己的类目, 不同层级的类目, 使用&gt;进行分割", "或直接使用这种方法{\"cat_id\":\"50012036\"}"));
		fieldList.add(createInputField("property", "商家自定义属性", false, "格式:{\"material\":\"cotton\",\"gender\":\"men\",\"color\":\"grey\"}"));
		fieldList.add(createInputField("brand", "品牌", true, null));
		fieldList.add(createInputField("main_images", "主图", true, "1~5张图片, 用逗号分割, 建议800*800"));
		fieldList.add(createInputField("description", "详情描述", true, "描述的html格式, 必须转为xml格式"));
		fieldList.add(createInputField("logistics", "物流", true, "json"));
		fieldList.add(createInputField("skus", "sku", true, "json"));
		fieldList.add(createInputField("extends", "扩展", true, "json"));
		fieldList.add(createInputField("wireless_desc", "无线描述", false, null));

		try {
			String xml = SchemaWriter.writeRuleXmlString(fieldList);
			System.out.println(xml);
		} catch (TopSchemaException e) {
			e.printStackTrace();
		}
	}

	private static Field createInputField(String fId, String fName, boolean require, String... tipInfoList) {
		InputField inputField = new InputField();

		inputField.setId(fId);
		inputField.setName(fName);

		List<Rule> ruleList = new ArrayList<>();
		if (require) {
			Rule rule = new RequiredRule("true");
			ruleList.add(rule);
		}
		if (tipInfoList != null && tipInfoList.length > 0) {
			for (String tipInfo : tipInfoList) {
				Rule rule = new TipRule(tipInfo);
				ruleList.add(rule);
			}
		}
		if (ruleList.size() > 0) {
			inputField.setRules(ruleList);
		}

		return inputField;
	}

}
