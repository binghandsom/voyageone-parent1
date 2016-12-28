package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

/**
 * Created by zhujiaye on 16/5/20.
 */
public class Tmall_001_Sneakerhead_DictTest extends BaseDictTest {

	@Test
	public void startupTest() {

		doTG(); // 天猫国际
		doTM(); // 天猫
		doJDJG(); // 京东和京东国际
		doJM(); // 聚美

	}

	private void doTG() {
		System.out.println("=====================================");
		System.out.println("==== 天猫国际 ====");
		{
			// 产品图片
			String templateUrl = "http://image.sneakerhead.com/is/image/sneakerhead/tmall-800d?$800$&$img=sneakerhead/%s&layer=2&originN=0,.5&pos=0,170";
			for (int i = 0; i < 5; i++) {
				doCreateJson("产品图片-" + ( i + 1 ), false, doDict_商品图片(templateUrl, String.valueOf(i)));
			}
		}
		{
			// 商品图片
			String templateUrl = "http://image.sneakerhead.com/is/image/sneakerhead/1200templateblack?$1200x1200$&$img=%s&layer=2&originN=0,.5&pos=0,370";
			for (int i = 0; i < 5; i++) {
				doCreateJson("商品图片-" + ( i + 1 ), false, doDict_商品图片(templateUrl, String.valueOf(i)));
			}
		}
		{
			// 透明图片
			String templateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn20160829_800x800toumingBG?$800_800_new_test_file$&$product=%s";
			doCreateJson("透明图片", false, doDict_商品图片(templateUrl, "0"));
		}

		doCreateJson("详情页描述", false, doPC端详情页描述(
			"https://img.alicdn.com/imgextra/i3/1792368114/T2CNnyXb4bXXXXXXXX_!!1792368114.jpg", // 产品信息图片
			"https://img.alicdn.com/imgextra/i4/1792368114/T2ziJXXzRXXXXXXXXX_!!1792368114.jpg", // 固定图片_产品展示_URL
			"https://img.alicdn.com/imgextra/i3/1792368114/T2uJL9XgVaXXXXXXXX_!!1792368114.jpg",	// 固定图片_尺码对照_URL
			"https://img.alicdn.com/imgextra/i2/1792368114/T2h1E2XnFaXXXXXXXX_!!1792368114.jpg", // 固定图片_关于我们_URL
			"https://gdp.alicdn.com/imgextra/i2/1792368114/T24g3.XfRaXXXXXXXX_!!1792368114.jpg", // 固定图片_购物说明_URL
			"https://gdp.alicdn.com/imgextra/i4/1792368114/TB2DjE8sVXXXXXrXFXXXXXXXXXX_!!1792368114.jpg", // 固定图片_价格解释_URL
			"https://gdp.alicdn.com/imgextra/i1/1792368114/TB2tSNXpFXXXXcbXXXXXXXXXXXX_!!1792368114.jpg", // 固定图片_何为sneakerhead_URL
			"https://sneakerhead-usa.tmall.hk/shop/view_shop.htm?search=y&orderType=newOn_desc", // 固定链接_何为sneakerhead_新品上市
			"https://sneakerhead-usa.tmall.hk/shop/view_shop.htm?search=y", // 固定链接_何为sneakerhead_换新推荐
			"https://sneakerhead-usa.tmall.hk/category-922211088.htm?mid=w-3595196461-0&catId=922211088&search=y&orderType=newOn_desc", // 固定链接_何为sneakerhead_高端限量
			"https://sneakerhead-usa.tmall.hk/search.htm?orderType=newOn_desc&keyword=%C4%D0%D0%AC", // 固定链接_何为sneakerhead_全部男鞋
			"https://sneakerhead-usa.tmall.hk/search.htm?orderType=newOn_desc&keyword=ŮЬ", // 固定链接_何为sneakerhead_全部女鞋
			"https://sneakerhead-usa.tmall.hk/p/fuku.htm", // 固定链接_何为sneakerhead_潮流服饰
			"https://gdp.alicdn.com/imgextra/i3/1792368114/T2BeVVXuhaXXXXXXXX-1792368114.jpg", // 固定图片_美国团队_URL
			"http://image.sneakerhead.com/is/image/sneakerhead/tmall790?$790product$&$img=%s" // 图片模板_商品细节图_URL
		));
	}
	private void doTM() {
		System.out.println("=====================================");
		System.out.println("==== 天猫 ====");
		{
			// 产品图片
			String templateUrl = "http://image.sneakerhead.com/is/image/sneakerhead/tmall-800d?$800$&$img=sneakerhead/%s&layer=2&originN=0,.5&pos=0,170";
			for (int i = 0; i < 5; i++) {
				doCreateJson("产品图片-" + ( i + 1 ), false, doDict_商品图片(templateUrl, String.valueOf(i)));
			}
		}
		{
			// 商品图片
			String templateUrl = "http://image.sneakerhead.com/is/image/sneakerhead/tmall-800d?$800$&$img=sneakerhead/%s&layer=2&originN=0,.5&pos=0,170";
			for (int i = 0; i < 5; i++) {
				doCreateJson("商品图片-" + ( i + 1 ), false, doDict_商品图片(templateUrl, String.valueOf(i)));
			}
		}
		{
			// 透明图片
			String templateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn20160829_800x800toumingBG?$800_800_new_test_file$&$product=%s";
			doCreateJson("透明图片", false, doDict_商品图片(templateUrl, "0"));
		}
	}
	private void doJDJG() {
		System.out.println("=====================================");
		System.out.println("==== 京东和京东国际 ====");
		{
			// 京东产品图片
			String templateUrl = "http://image.sneakerhead.com/is/image/sneakerhead/jd_800_black?$800$&$img=%s&layer=2&originN=0,0&pos=0,0";
			for (int i = 1; i <= 5; i++) {
				doCreateJson("京东产品图片-" + i, false, doDict_商品图片(templateUrl, String.valueOf(i + 1)));
			}
		}
	}
	private void doJM() {
		System.out.println("=====================================");
		System.out.println("==== 聚美 ====");

	}

	private RuleExpression doPC端详情页描述(
		String str固定图片_产品信息_URL,
		String str固定图片_产品展示_URL,
		String str固定图片_尺码对照_URL,
		String str固定图片_关于我们_URL,
		String str固定图片_购物说明_URL,
		String str固定图片_价格解释_URL,
		String str固定图片_何为sneakerhead_URL,
		String str固定链接_何为sneakerhead_新品上市,
		String str固定链接_何为sneakerhead_换新推荐,
		String str固定链接_何为sneakerhead_高端限量,
		String str固定链接_何为sneakerhead_全部男鞋,
		String str固定链接_何为sneakerhead_全部女鞋,
		String str固定链接_何为sneakerhead_潮流服饰,
		String str固定图片_美国团队_URL,
		String str图片模板_商品细节图_URL
	) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		// 生成内容
		{
			// 固定图片 - 产品信息
			TextWord textWord = new TextWord(String.format(C_TEMPLATE_IMG_790, str固定图片_产品信息_URL));
			ruleRoot.addRuleWord(textWord);
		}

		{
			// 英文长描述
			MasterWord word = new MasterWord("longDesEn");
			ruleRoot.addRuleWord(word);
		}

		{
			// 回车一个
			TextWord word = new TextWord(C_TEXT_BR);
			ruleRoot.addRuleWord(word);
		}

		{
			// 中文长描述
			MasterWord word = new MasterWord("longDesCn");
			ruleRoot.addRuleWord(word);
		}

		{
			// 固定图片 - 产品展示
			TextWord textWord = new TextWord(String.format(C_TEMPLATE_IMG_790, str固定图片_产品展示_URL));
			ruleRoot.addRuleWord(textWord);
		}

		{
//			全部商品（ 带CODE， 英文颜色， 中文颜色， 图片 ）
			// 主商品
			{
				ruleRoot.addRuleWord(new MasterWord("code"));
				ruleRoot.addRuleWord(new TextWord("&nbsp;"));
				ruleRoot.addRuleWord(new MasterWord("codeDiff"));
				ruleRoot.addRuleWord(new TextWord("&nbsp;"));
				ruleRoot.addRuleWord(new MasterWord("color"));
				ruleRoot.addRuleWord(new TextWord("<br />"));

				// 详情页PC图
				for (int j = 0; j < 5; j++) {
					doDict_商品图片(str图片模板_商品细节图_URL, String.valueOf(j));
				}
			}

			// 非主商品
			for (int i = 0; i < 10; i++) {
				ruleRoot.addRuleWord(new SubCodeWord(i, "code"));
				ruleRoot.addRuleWord(new TextWord("&nbsp;"));
				ruleRoot.addRuleWord(new SubCodeWord(i, "codeDiff"));
				ruleRoot.addRuleWord(new TextWord("&nbsp;"));
				ruleRoot.addRuleWord(new SubCodeWord(i, "color"));
				ruleRoot.addRuleWord(new TextWord("<br />"));

				// 详情页PC图
				for (int j = 0; j < 5; j++) {

				}
			}
		}

		{
			// 尺码图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(
					String.format(C_TEMPLATE_IMG_790, str固定图片_尺码对照_URL) + // 固定图片
					C_TEMPLATE_IMG_790)); // 尺码图

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("2"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			RuleExpression useOriUrl = null;

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 固定图片 - 关于我们
			TextWord textWord = new TextWord(String.format(C_TEMPLATE_IMG_790, str固定图片_关于我们_URL));
			ruleRoot.addRuleWord(textWord);
		}
		{
			// 固定图片 - 购物说明
			TextWord textWord = new TextWord(String.format(C_TEMPLATE_IMG_790, str固定图片_购物说明_URL));
			ruleRoot.addRuleWord(textWord);
		}
		{
			// 固定图片 - 价格解释
			TextWord textWord = new TextWord(String.format(C_TEMPLATE_IMG_790, str固定图片_价格解释_URL));
			ruleRoot.addRuleWord(textWord);
		}
		{
			// 固定图片 - 何为sneakerhead
//	<p>
//		<img src="%s" usemap="#Map1" height="587" border="0" width="790" class="img-ks-lazyload">
//		<map name="Map1">
//			<area coords="1,271,261,586" href="%s" target="_blank" style="outline:none;"> <!-- 新品上市 -->
//			<area coords="266,272,527,452" href="%s" target="_blank" style="outline:none;"> <!-- 换新推荐 -->
//			<area coords="531,271,785,451" href="%s" target="_blank" style="outline:none;"> <!-- 高端限量 -->
//			<area coords="267,455,437,583" href="%s" target="_blank" style="outline:none;"> <!-- 全部男鞋 -->
//			<area coords="443,453,616,585" href="%s" target="_blank" style="outline:none;"> <!-- 全部女鞋 -->
//			<area coords="617,453,790,584" href="%s" target="_blank" style="outline:none;"> <!-- 潮流服饰 -->
//		</map>
//	</p>
//			新品上市->按照新品降序排列
//			换新推荐->
//			高端限量->类目【高端限量】按照新品降序排列
//			全部男鞋->搜索【男鞋】按照新品降序排列
//			全部女鞋->搜索【女鞋】按照新品降序排列
//			潮流服饰->https://sneakerhead-usa.tmall.hk/p/fuku.htm

			String html = "<p>\n" +
					"\t<img src=\"%s\" usemap=\"#Map1\" height=\"587\" border=\"0\" width=\"790\" class=\"img-ks-lazyload\">\n" +
					"\t<map name=\"Map1\">\n" +
					"\t\t<area coords=\"1,271,261,586\" href=\"%s\" target=\"_blank\" style=\"outline:none;\"> <!-- 新品上市 -->\n" +
					"\t\t<area coords=\"266,272,527,452\" href=\"%s\" target=\"_blank\" style=\"outline:none;\"> <!-- 换新推荐 -->\n" +
					"\t\t<area coords=\"531,271,785,451\" href=\"%s\" target=\"_blank\" style=\"outline:none;\"> <!-- 高端限量 -->\n" +
					"\t\t<area coords=\"267,455,437,583\" href=\"%s\" target=\"_blank\" style=\"outline:none;\"> <!-- 全部男鞋 -->\n" +
					"\t\t<area coords=\"443,453,616,585\" href=\"%s\" target=\"_blank\" style=\"outline:none;\"> <!-- 全部女鞋 -->\n" +
					"\t\t<area coords=\"617,453,790,584\" href=\"%s\" target=\"_blank\" style=\"outline:none;\"> <!-- 潮流服饰 -->\n" +
					"\t</map>\n" +
					"</p>\n";
			TextWord textWord = new TextWord(String.format(html,
					str固定图片_何为sneakerhead_URL,
					str固定链接_何为sneakerhead_新品上市,
					str固定链接_何为sneakerhead_换新推荐,
					str固定链接_何为sneakerhead_高端限量,
					str固定链接_何为sneakerhead_全部男鞋,
					str固定链接_何为sneakerhead_全部女鞋,
					str固定链接_何为sneakerhead_潮流服饰
			));
			ruleRoot.addRuleWord(textWord);
		}
		{
			// 固定图片 - 美国团队
			TextWord textWord = new TextWord(String.format(C_TEMPLATE_IMG_790, str固定图片_美国团队_URL));
			ruleRoot.addRuleWord(textWord);
		}

		return ruleRoot;
	}

	/**
	 * 根据index生成天猫商品图
	 *
	 * @param templateUrl 模板url
	 * @param index String  自定义图片index
	 * @return RuleExpression 无线商品图片字典
	 */
	private RuleExpression doDict_商品图片(String templateUrl, String index) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		{
			// 商品图
			CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord("%s"));
			userParam.setHtmlTemplate(htmlTemplate);
			RuleExpression imageTemplate = new RuleExpression();
			// %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
			imageTemplate.addRuleWord(new TextWord(templateUrl));
			userParam.setImageTemplate(imageTemplate);
			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord(String.valueOf(index)));
			userParam.setImageIndex(imageIndex);
			RuleExpression img_imageType = new RuleExpression();
			img_imageType.addRuleWord(new TextWord(C_商品图片));
			userParam.setImageType(img_imageType);

			CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
			wordValueGetMainProductImages.setUserParam(userParam);
			ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));
		}

		return ruleRoot;
	}


}