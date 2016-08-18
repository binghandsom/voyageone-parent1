package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/2/23.
 */
public class Tmall_010_Jewelry_DictTest {
	String C_TEXT_BR = "<br />";
	String C_TEMPLATE_IMG = "<img src=%s>";

	String C_商品图片 = "PRODUCT_IMAGE";
	String C_包装图片 = "PACKAGE_IMAGE";
	String C_带角度图片 = "ANGLE_IMAGE";
	String C_自定义图片 = "CUSTOM_IMAGE";

	@Test
	public void startupTest() {

//		doCreateJson("详情页描述", false, doDict_详情页描述());
        doCreateJson("无线描述", false, doDict_无线描述());

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
			String html = "<div style=\"margin-left: auto;margin-right: auto;width: 790.0px;\">";
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		{
			{
				// div-001-start
				String html = "<div style=\"background-color: #f6f1ed;margin-bottom: 15.0px;\">";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}

			{

				{
					// 精品描述图片
//					TextWord word = new TextWord(String.format(C_TEMPLATE_IMG, "https://img.alicdn.com/imgextra/i4/2183719539/TB2DfsYeFXXXXagXXXXXXXXXXXX_!!2183719539.gif")); // PA
					TextWord word = new TextWord(String.format(C_TEMPLATE_IMG, "https://img.alicdn.com/imgextra/i4/2641101981/TB2NcXifXXXXXb3XXXXXXXXXXXX_!!2641101981.jpg")); // jewelry
					ruleRoot.addRuleWord(word);
				}

				{
					{
						// div-002-start
						String html = "<div style=\"text-align: center;margin-left: 52.0px;margin-right: 52.0px;line-height: 200.0%;\">";
						TextWord word = new TextWord(html);
						ruleRoot.addRuleWord(word);
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
						// div-002-end
						String html = "</div>";
						TextWord word = new TextWord(html);
						ruleRoot.addRuleWord(word);
					}
				}

				{
					{
						// 商品属性图的前缀
						String html =
								"<div style=\"margin: 15.0px;\">" +
								"<img src=";
						ruleRoot.addRuleWord(new TextWord(html));
					}

					// 商品属性图
					RuleExpression imageTemplate = new RuleExpression();
					String htmlTemplate =
							"http://s7d5.scene7.com/is/image/sneakerhead/JEWERLY_20151014_x760_438x?$760x438$&$JEWERLY-760-438$" +
                                    "&$product=%s" + // 图片
                                    "&$text01=%s" +  // 标题1   文本1
                                    "&$text02=%s" +
                                    "&$text03=%s" +
                                    "&$text04=%s" +
                                    "&$text05=%s" +
                                    "&$text06=%s" +
                                    "&$text07=%s" +
                                    "&$text08=%s";
//					String htmlTemplate = "42";
					imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                    // 参数imageParams
					List<RuleExpression> imageParams = new ArrayList<>();
					// 参数
					{
						{
							// 第一张商品图片
                            // 第一个参数是product_id(GetMainProductImages)
							CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
							RuleExpression img_imageIndex = new RuleExpression();
							img_imageIndex.addRuleWord(new TextWord("0"));
							userParam.setImageIndex(img_imageIndex);
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
                            for (int index = 0; index < 8; index++) {
                                RuleExpression ruleExpression = new RuleExpression();
                                ruleExpression.addRuleWord(new FeedCnWord(true, index));
                                ruleExpression.addRuleWord(new TextWord("   "));
                                ruleExpression.addRuleWord(new FeedCnWord(false, index));
                                imageParams.add(ruleExpression);
                            }
                        }

//						{
//							// 第1个自定义字段
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 0)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 0)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 0));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 0));
//							imageParams.add(ruleExpression);
//
//						}
//						{
//							// 第2个自定义字段
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 1)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 1)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 1));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 1));
//							imageParams.add(ruleExpression);
//
//						}
//						{
//							// 第3个自定义字
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 2)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 2)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 2));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 2));
//							imageParams.add(ruleExpression);
//
//						}
//						{
//							// 第4个自定义字段
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 3)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 3)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 3));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 3));
//							imageParams.add(ruleExpression);
//
//						}
//						{
//							// 第5个自定义字段
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 4)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 4)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 4));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 4));
//							imageParams.add(ruleExpression);
//
//						}
//						{
//							// 第6个自定义字段
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 5)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 5)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 5));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 5));
//							imageParams.add(ruleExpression);
//
//						}
//						{
//							// 第7个自定义字段
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 6)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 6)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 6));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 6));
//							imageParams.add(ruleExpression);
//
//						}
//						{
//							// 第8个自定义字段
////							// 的 标题
////							RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 7)); imageParams.add(ruleExpression_key);
////							// 的 值
////							RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 7)); imageParams.add(ruleExpression_val);
//
//							// 合并
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, 7));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, 7));
//							imageParams.add(ruleExpression);
//
//						}

//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//						}
					}

					RuleExpression useCmsBtImageTemplate = new RuleExpression();
					useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

					CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate);
					ruleRoot.addRuleWord(new CustomWord(word));

					{
						// 商品属性图的后缀
						String html = ">" +
								"</div>";
						ruleRoot.addRuleWord(new TextWord(html));
					}
				}

				{
					// 所有商品图
					RuleExpression htmlTemplate = new RuleExpression();
					htmlTemplate.addRuleWord(new TextWord("<div style=\"margin:15px\" ><img src=\"%s\" style=\"display:block; width:760px;\" /></div>"));

					RuleExpression imageTemplate = new RuleExpression();
					imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%%5F20150819%%5Fx760%%5F760x?$760x760$&$JEWERLY%%2D760%%2D760$&$proudct=%s"));
//					imageTemplate.addRuleWord(new TextWord("47"));

					RuleExpression imageType = new RuleExpression();
					imageType.addRuleWord(new TextWord(C_商品图片));

					CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null, null);
					ruleRoot.addRuleWord(new CustomWord(word));
				}

				{
					// 所有自定义图
					RuleExpression htmlTemplate = new RuleExpression();
					htmlTemplate.addRuleWord(new TextWord("<div style=\"margin:15px\" ><img src=\"%s\" style=\"display:block; width:760px;\" /></div>"));

					RuleExpression imageTemplate = new RuleExpression();
					imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/JW_20160506_x790_526x?$790x526$&$product=%s"));
//					imageTemplate.addRuleWord(new TextWord("49"));

					RuleExpression imageType = new RuleExpression();
					imageType.addRuleWord(new TextWord(C_自定义图片));

					CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null, null);
					ruleRoot.addRuleWord(new CustomWord(word));
				}

//				{
//					// 四张手绘图
//
//					// html
//					String html_01 =
//							"<div style=\"font-size: 0;padding: 0.0px 15.0px 15.0px 15.0px;\">" +
//									"    <div style=\"display: inline-block;width: 380.0px;border: solid 0.0px;\">" +
//									"        <img style=\"display: block;width: 380.0px;height: 251.0px;\" src=\"";
//					ruleRoot.addRuleWord(new TextWord(html_01));
//
//					// 左上角图片
//					// TODO:添加自定义图片 CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams);
//					ruleRoot.addRuleWord(new TextWord("https://img.alicdn.com/imgextra/i2/2183719539/TB2x8C5gpXXXXaNXpXXXXXXXXXX_!!2183719539.jpg"));
//
//					// html
//					String html_02 =
//							"\">";
//					ruleRoot.addRuleWord(new TextWord(html_02));
//
//					// 左下角图片 - 手绘图 - 1
//					{
//						RuleExpression htmlTemplate = new RuleExpression();
//						htmlTemplate.addRuleWord(new TextWord("<img style=\"display: block;width: 380.0px;height: 509.0px;\" src=\"%s\">"));
//						RuleExpression imageTemplate = new RuleExpression();
//						imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Jewerly_20150907_x380_509x?$308x509$&$Jewerly_380-509$&$product=%s"));
//						RuleExpression imageIndex = new RuleExpression();
//						imageIndex.addRuleWord(new TextWord("1")); // 手绘图中的第一张
//						RuleExpression imageType = new RuleExpression();
//						imageType.addRuleWord(new TextWord(C_自定义图片)); // 手绘图
//
//						RuleExpression paddingExpression = get获取手绘图的默认图("1"); // 第一张替换图
//
//						CustomWordValueGetMainProductImages word = new CustomWordValueGetMainProductImages(htmlTemplate, imageTemplate, imageIndex, imageType, paddingExpression);
//						ruleRoot.addRuleWord(new CustomWord(word));
//					}
//
//					// html
//					String html_03 =
//							"    </div>" +
//									"    <div style=\"display: inline-block;width: 380.0px;border: solid 0.0px;\">";
//					ruleRoot.addRuleWord(new TextWord(html_03));
//
//					// 右上角图片 - 手绘图 - 2
//					{
//						RuleExpression htmlTemplate = new RuleExpression();
//						htmlTemplate.addRuleWord(new TextWord("<img style=\"display: block;width: 380.0px;height: 509.0px;\" src=\"%s\">"));
//						RuleExpression imageTemplate = new RuleExpression();
//						imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Jewerly_20150907_x380_509x?$308x509$&$Jewerly_380-509$&$product=%s"));
//						RuleExpression imageIndex = new RuleExpression();
//						imageIndex.addRuleWord(new TextWord("2")); // 手绘图中的第二张
//						RuleExpression imageType = new RuleExpression();
//						imageType.addRuleWord(new TextWord(C_自定义图片)); // 手绘图
//
//						RuleExpression paddingExpression = get获取手绘图的默认图("2"); // 第二张替换图
//
//						CustomWordValueGetMainProductImages word = new CustomWordValueGetMainProductImages(htmlTemplate, imageTemplate, imageIndex, imageType, paddingExpression);
//						ruleRoot.addRuleWord(new CustomWord(word));
//					}
//
//					// html
//					String html_04 =
//							"        <img style=\"display: block;width: 380.0px;height: 251.0px;\" src=\"";
//					ruleRoot.addRuleWord(new TextWord(html_04));
//
//					// 右下角图片 - 固定图片
//					String html_05 = "https://img.alicdn.com/imgextra/i2/2183719539/TB2x8C5gpXXXXaNXpXXXXXXXXXX_!!2183719539.jpg";
//					ruleRoot.addRuleWord(new TextWord(html_05));
//
//					// html
//					String html_06 =
//							"\">" +
//									"    </div>" +
//									"</div>";
//					ruleRoot.addRuleWord(new TextWord(html_06));
//
//				}

			}

			{
				// div-001-end
				String html = "</div>";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}

		}

		{
			// 尺寸参考, 关于我们, 巴菲特图
//			String html = "<img src=\"https://img.alicdn.com/imgextra/i3/2183719539/TB246e8gpXXXXaSXpXXXXXXXXXX_!!2183719539.jpg\">"; // PA
			String html = "<img src=\"https://img.alicdn.com/imgextra/i4/2641101981/TB2k3RyfpXXXXbbXpXXXXXXXXXX_!!2641101981.jpg\">"; // jewelry
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		{
			// 珠宝知识图
			String html =
					"<a href=\"http://jewelry.tmall.com/p/rd294659.htm\">" +
//							"<img src=\"https://img.alicdn.com/imgextra/i3/2183719539/TB279TdgpXXXXXgXpXXXXXXXXXX_!!2183719539.jpg\">" + // PA
							"<img src=\"https://img.alicdn.com/imgextra/i1/2641101981/TB2YG4hfpXXXXbtXpXXXXXXXXXX_!!2641101981.jpg\">" + // jewelry
							"</a>";
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		{
			// 购买须知, 购买流程, 商品包装, 退货政策图
//			String html = "<img src=\"https://img.alicdn.com/imgextra/i2/2183719539/TB20izkgpXXXXbfXXXXXXXXXXXX_!!2183719539.jpg\">"; // PA
			String html = "<img src=\"https://img.alicdn.com/imgextra/i1/2641101981/TB2KYhQfpXXXXbYXXXXXXXXXXXX_!!2641101981.jpg\">"; // jewelry
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		{
			// div-000-end
			String html = "</div>";
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}


		return ruleRoot;
	}

    private RuleExpression doDict_无线描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();
        {
            // start
            String kv = "{\"wireless_desc\":{";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

//        {
//            // K-V 模板
//            String kv = "\"k1\":{\"k1-1\":\"v1\",\"k1-2\":\"v2\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//
//            <field id="xxx_enable" name="是否启用" type="singleCheck">
//                <options>
//                <option displayName="启用" value="true"/>
//                <option displayName="不启用" value="false"/>
//                </options>
//            </field>
//        }

        {
            // item_info 商品信息
            String kv = "\"item_info\":{\"item_info_enable\":\"true\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // coupon 优惠
            String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"342115\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // hot_recommanded 同店推荐
//            <field id="hot_recommanded_id" name="选择模板" type="singleCheck">
//                <options>
//                <option displayName="商品推荐" value="520277"/>
//                </options>
//            </field>
            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"341911\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // shop_discount 店铺活动
            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"342160\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

//        {
//            // user_define 自定义
//            String kv = "\"user_define\":{\"user_define_enable\":\"true\",\"user_define_name\":\"xxx\",\"user_define_image_0\":\"xxx\",\"user_define_image_1\":\"xxx\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }

        // 图片全是DictWord(总共20张图片,但最后5张不用设置图片)
        {
            // item_picture 商品图片
            String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);

            // 5张无线商品图片
            for (int i = 0; i < 5; i++) {
                int j = i + 1;
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                DictWord dictRoot = new DictWord();
                dictRoot.setName("无线商品图片-" + j);
                ruleRoot.addRuleWord(dictRoot);

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // 5张无线自定义图片
            for (int i = 5; i < 10; i++) {

                int j = i - 4;
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                DictWord dictRoot = new DictWord();
                dictRoot.setName("无线自定义图片-" + j);
                ruleRoot.addRuleWord(dictRoot);

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // 5张无线固定图
            for (int i = 10; i < 15; i++) {
                // 5张无线商品图片
                int j = i - 9;
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                DictWord dictRoot = new DictWord();
                dictRoot.setName("无线固定图-" + j);
                ruleRoot.addRuleWord(dictRoot);

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // item_picture_image_15 ~ item_picture_image_19不用设置

            // end
            String endStr = "}";
            TextWord endWord = new TextWord(endStr);
            ruleRoot.addRuleWord(endWord);
        }

        {
            // end
            String kv = "}}";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        return ruleRoot;
    }

	/**
	 * 获取手绘图的默认图
	 * 当手绘图不存在的场合, 用这张共通的图片替换掉
	 *
	 * @param paddingImageIndex 使用第几张替换的图
	 * @return 默认图
	 */

//	private RuleExpression get获取手绘图的默认图(String paddingImageIndex) {
//		RuleExpression paddingExpression = new RuleExpression();
//
//		RuleExpression paddingPropName = new RuleExpression();
//		paddingPropName.addRuleWord(new TextWord("Jewwery_handMade_image"));
//		RuleExpression imageIndex = new RuleExpression();
//		imageIndex.addRuleWord(new TextWord(paddingImageIndex));
//		CustomWordValueGetPaddingImageKey paddingImage = new CustomWordValueGetPaddingImageKey(paddingPropName, imageIndex);
//
//		paddingExpression.addRuleWord(new CustomWord(paddingImage));
//
//		return paddingExpression;
//
//	}

}