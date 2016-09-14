package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/5/20.
 */
public class Tmall_012_Bcbg_DictTest {
	String C_TEXT_BR = "<br />";
	String C_TEMPLATE_IMG = "<img src=%s>";

	String C_商品图片 = "PRODUCT_IMAGE";
	String C_包装图片 = "PACKAGE_IMAGE";
	String C_带角度图片 = "ANGLE_IMAGE";
	String C_自定义图片 = "CUSTOM_IMAGE";

	@Test
	public void startupTest() {

		doCreateJson("详情页描述", false, doDict_详情页描述());

	}

	/**
	 * 生成json
	 *
	 * @param title    字典名字
	 * @param isUrl    生成出来的内容是否是url(一般是图片的话就是true, 其他有文字的都是false)
	 * @param ruleRoot rule
	 */
	private void doCreateJson(String title, boolean isUrl, RuleExpression ruleRoot) {

		DictWord dictRoot = new DictWord();
		dictRoot.setName(title);
		dictRoot.setExpression(ruleRoot);
		dictRoot.setIsUrl(isUrl);

		RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
		String json = ruleJsonMapper.serializeRuleWord(dictRoot);

		System.out.println("=====================================");
		System.out.println("字典: " + title);
		System.out.println(json);

	}

	private RuleExpression doDict_详情页描述() {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		// 生成内容
		{
			// div-000-start
			String html = "<div style=\"margin-left: auto;margin-right: auto;width: 790px;\">";
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		{
			{
                {
                    // header
                    String html = "<img src=\"https://img.alicdn.com/imgextra/i1/2694857307/TB2DbDBsFXXXXcbXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }

                {
                    // 尺码图
                    RuleExpression htmlTemplate = new RuleExpression();
                    htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("2"));

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));

                    RuleExpression useOriUrl = null;

                    CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }

                {
                    String html = "<img src=\"https://img.alicdn.com/imgextra/i3/2694857307/TB2kCSUgVXXXXcHXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);

                    html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2a3GBgVXXXXXsXpXXXXXXXXXX_!!2694857307.jpg\"/>";
                    word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
			}
			{
				// 英文长描述
				String html = "<div style=\"border-top:none; border-bottom:none; border-left:1px solid #d8d8d8; border-right:1px solid #d8d8d8; padding-left: 5px; padding-right: 5px;margin-bottom:-5px;\">";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);

				String column = "longDesEn";
				CommonWord commonWord = new CommonWord(column);
				ruleRoot.addRuleWord(commonWord);

				html = "</div>";
				word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}
			{
				// 中文长描述
				String html = "<div style=\"border-top:none; border-bottom:none; border-left:1px solid #d8d8d8; border-right:1px solid #d8d8d8; padding-left: 5px; padding-right: 5px;margin-bottom:-5px;\">";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);

				String column = "longDesCn";
				CommonWord commonWord = new CommonWord(column);
				ruleRoot.addRuleWord(commonWord);

				html = "</div>";
				word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}

			{
				// 参数图
				{
					// 前缀
					String html = "<div><img src=\"";
					ruleRoot.addRuleWord(new TextWord(html));
				}

				{
					// useCmsBtImageTemplate
					RuleExpression useCmsBtImageTemplate = new RuleExpression();
					useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

					// 参数imageParams
					List<RuleExpression> imageParams = new ArrayList<>();
					{
						// 第一个参数是product_id(GetMainProductImages)
						CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
						RuleExpression imageIndex = new RuleExpression();
						imageIndex.addRuleWord(new TextWord("0"));
						userParam.setImageIndex(imageIndex);
						RuleExpression img_imageType = new RuleExpression();
						img_imageType.addRuleWord(new TextWord(C_商品图片));
						userParam.setImageType(img_imageType);

						CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
						wordValueGetMainProductImages.setUserParam(userParam);

						RuleExpression imgWord = new RuleExpression();
						imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
						imageParams.add(imgWord);
					}

					{
						// 第二个开始，共八个属性（品牌名称,产品类别,适用年龄,使用体重,固定方式,外形尺寸,材质用料,产品重量）
//						for (int index = 0; index < 8; index++) {
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, index));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, index));
//							imageParams.add(ruleExpression);
//						}
						for (int index = 0; index < 4; index++) {
							// name
							RuleExpression ruleExpression = new RuleExpression();
							ruleExpression.addRuleWord(new FeedCnWord(true, index));
							imageParams.add(ruleExpression);

							// value
							ruleExpression = new RuleExpression();
							ruleExpression.addRuleWord(new FeedCnWord(false, index));
							imageParams.add(ruleExpression);
						}
					}

					CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(null, imageParams, useCmsBtImageTemplate);
					ruleRoot.addRuleWord(new CustomWord(word));
				}

				{
					// 后缀
					String html = "\"></div>";
					ruleRoot.addRuleWord(new TextWord(html));
				}
			}

            // 以前字典里面就有尺码图，但CMS素材管理的共通图片一览里面没有上传尺码图到天猫平台，所以以前相亲页里面没有显示出来尺码图
//			{
//				// 尺码图 getCommonImages
//				RuleExpression htmlTemplate = new RuleExpression();
//				htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\"/>"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord("2")); // 尺码图
//
//				RuleExpression viewType = new RuleExpression();
//				viewType.addRuleWord(new TextWord("1")); // pc
//
//				RuleExpression useOriUrl = null;
//	//			RuleExpression useOriUrl = new RuleExpression();
//	//			useOriUrl.addRuleWord(new TextWord("1"));
//
//				CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
//				ruleRoot.addRuleWord(new CustomWord(word));
//			}

			{
				// 分割线 ----- detail -----
				String html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2VcOHgVXXXXcmXXXXXXXXXXXX_!!2694857307.jpg\"/>";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}

			{
				// 商品详情图getAllImages (FieldImageType.PRODUCT_IMAGE, useCmsBtImageTemplate = true)
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\"/>"));

				RuleExpression imageTemplate = null;
	//			RuleExpression imageTemplate = new RuleExpression();
	//			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x?$bbbbbbbb790x500bbbbbbbb$&$product=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression useOriUrl = null;
	//			RuleExpression useOriUrl = new RuleExpression();
	//			useOriUrl.addRuleWord(new TextWord("1"));

				// useCmsBtImageTemplate
				RuleExpression useCmsBtImageTemplate = new RuleExpression();
				useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

				CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, useCmsBtImageTemplate);
				ruleRoot.addRuleWord(new CustomWord(word));
			}

			{
				// 商品自定义图getAllImages (FieldImageType.CUSTOM_IMAGE, useOriUrlStr = 1 使用原图)
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\"/>"));

				RuleExpression imageTemplate = null;
	//			RuleExpression imageTemplate = new RuleExpression();
	//			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x?$bbbbbbbb790x500bbbbbbbb$&$product=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_自定义图片));

				RuleExpression useOriUrl = new RuleExpression();
				useOriUrl.addRuleWord(new TextWord("1"));

				CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null);
				ruleRoot.addRuleWord(new CustomWord(word));
			}

			{
				// end
				String html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2dRqWgVXXXXaXXXXXXXXXXXXX_!!2694857307.jpg\"/>";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);

				html = "<a href=\"https://bcbgmaxazria.tmall.hk/p/rd397317.htm?spm=a1z10.1-b.w10842440-12684248065.9.IvnNih\">";
				word = new TextWord(html);
				ruleRoot.addRuleWord(word);

				html = "<img src=\"https://img.alicdn.com/imgextra/i4/2694857307/TB2ZtJfhXXXXXcmXXXXXXXXXXXX_!!2694857307.jpg\"/>";
				word = new TextWord(html);
				ruleRoot.addRuleWord(word);

				html = "</a>";
				word = new TextWord(html);
				ruleRoot.addRuleWord(word);

				html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2gdFdhXXXXXcLXXXXXXXXXXXX_!!2694857307.jpg\"/>";
				word = new TextWord(html);
				ruleRoot.addRuleWord(word);

				html = "<img src=\"https://img.alicdn.com/imgextra/i3/2694857307/TB261bEsFXXXXbKXXXXXXXXXXXX_!!2694857307.jpg\"/>";
				word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}
		}

		{
			// div-000-end
			String html = "</div>";
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		return ruleRoot;
	}


}