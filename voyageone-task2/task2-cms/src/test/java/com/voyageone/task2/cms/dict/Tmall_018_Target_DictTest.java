package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/5/20.
 */
public class Tmall_018_Target_DictTest {
	String C_TEXT_BR = "<br />";
	String C_TEMPLATE_IMG = "<img src=%s>";

	String C_商品图片 = "PRODUCT_IMAGE";
	String C_包装图片 = "PACKAGE_IMAGE";
	String C_带角度图片 = "ANGLE_IMAGE";
	String C_自定义图片 = "CUSTOM_IMAGE";

	@Test
	public void startupTest() {

		doCreateJson("详情页描述", false, doDict_详情页描述());
//		doCreateJson("无线描述", false, doDict_无线描述());
//        for (int i = 0; i < 15; i++) {
//            int j = i + 1;
//            doCreateJson("无线自定义图片-" + j, false, doDict_无线自定义图片(String.valueOf(i)));   // index(0~9)
//        }
//        for (int i = 1; i <= 5; i++) {
//            doCreateJson("无线商品图片-" + i, false, doDict_无线商品图片(String.valueOf(i)));   // index(1~5)
//        }
        doCreateJson("无线描述", false, doDict_无线描述2());

        doCreateJson("无线描述-重点商品", false, doDict_无线描述_重点商品());

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
				// div-001-start
				String html = "<div style=\"margin-bottom: 15px;\">";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}

			{
                {
                    // 店铺介绍图(第一张)
                    RuleExpression htmlTemplate = new RuleExpression();
                    htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

                    // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("5"));

                    // 1:PC端 2:APP端
                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));

                    // 图片index 如果是null, 那么就获取全部, index从0开始, 指定但不存在的场合返回""
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));

                    // 1:使用原图 其它或者未设置，使用天猫平台图
                    RuleExpression useOriUrl = null;
//					RuleExpression useOriUrl = new RuleExpression();
//					useOriUrl.addRuleWord(new TextWord("1"));

                    CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                    ruleRoot.addRuleWord(new CustomWord(word));

                }
				{
					// 品牌故事图(第一张)
					RuleExpression htmlTemplate = new RuleExpression();
					htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

                    // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
					RuleExpression imageType = new RuleExpression();
					imageType.addRuleWord(new TextWord("3"));

                    // 1:PC端 2:APP端
					RuleExpression viewType = new RuleExpression();
					viewType.addRuleWord(new TextWord("1"));

                    // 图片index 如果是null, 那么就获取全部, index从0开始, 指定但不存在的场合返回""
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));  // 先设置第一张品牌故事图，后面会再设置一张品牌故事图

                    // 1:使用原图 其它或者未设置，使用天猫平台图
					RuleExpression useOriUrl = null;
//					RuleExpression useOriUrl = new RuleExpression();
//					useOriUrl.addRuleWord(new TextWord("1"));

					CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
					ruleRoot.addRuleWord(new CustomWord(word));

					// 测试用写死url start
//					String html = "<div><img src=\"%s\" /></div>";
//					html = String.format(html, "https://img.alicdn.com/imgextra/i4/2854639042/TB2_H8OpVXXXXa5XpXXXXXXXXXX_!!2854639042.jpg");
//					TextWord word = new TextWord(html);
//					ruleRoot.addRuleWord(word);
					// 测试用写死url end
				}

				{
					// 商品参数图
					{
						// 前缀
						String html = "<div><img src=\"";
						ruleRoot.addRuleWord(new TextWord(html));
					}

					{
						// imageTemplate
						RuleExpression imageTemplate = new RuleExpression();
						String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x_PC?$bbbbbbbb790x500bbbbbbbb$&$product=%s&$text01=%s&$text02=%s&$text03=%s&$text04=%s&$text05=%s&$text06=%s&$text07=%s&$text08=%s";
						imageTemplate.addRuleWord(new TextWord(htmlTemplate));

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
							for (int index = 0; index < 8; index++) {
								RuleExpression ruleExpression = new RuleExpression();
								ruleExpression.addRuleWord(new FeedCnWord(true, index));
								ruleExpression.addRuleWord(new TextWord("   "));
								ruleExpression.addRuleWord(new FeedCnWord(false, index));
								imageParams.add(ruleExpression);
							}
						}

						CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
						ruleRoot.addRuleWord(new CustomWord(word));
					}

					{
						// 后缀
						String html = "\"></div>";
						ruleRoot.addRuleWord(new TextWord(html));
					}
				}

				{
					// 商品自定义图
					RuleExpression htmlTemplate = new RuleExpression();
					htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

					RuleExpression imageTemplate = new RuleExpression();
					imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x?$bbbbbbbb790x500bbbbbbbb$&$product=%s"));

					RuleExpression imageType = new RuleExpression();
					imageType.addRuleWord(new TextWord(C_自定义图片));

					RuleExpression useOriUrl = new RuleExpression();
					useOriUrl.addRuleWord(new TextWord("1"));

					CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
					ruleRoot.addRuleWord(new CustomWord(word));
				}

                // add by desmond 2016/10/18 start
                {
                    // 品牌故事图（第二张）
                    RuleExpression htmlTemplate = new RuleExpression();
                    htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

                    // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("3"));

                    // 1:PC端 2:APP端
                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));

                    // 图片index 如果是null, 那么就获取全部, index从0开始, 指定但不存在的场合返回""
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("1"));     // 再设置第二张品牌故事图

                    // 1:使用原图 其它或者未设置，使用天猫平台图
                    RuleExpression useOriUrl = null;
//					RuleExpression useOriUrl = new RuleExpression();
//					useOriUrl.addRuleWord(new TextWord("1"));

                    CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }
                // add by desmond 2016/10/18 end

				{
					// 商品图片
					{
						// 商品描述（假的）：
						String html = "<div><img src=\"%s\"/></div>";
						html = String.format(html, "https://img.alicdn.com/imgextra/i3/2854639042/TB2XKHVpVXXXXcsXpXXXXXXXXXX_!!2854639042.jpg");
						TextWord word = new TextWord(html);
						ruleRoot.addRuleWord(word);
					}

					{
						RuleExpression htmlTemplate = new RuleExpression();
						htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

						RuleExpression imageTemplate = new RuleExpression();
						imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Target_20161213_x790_500x-1?$790x500$&$790_500$&$product=%s"));

						RuleExpression imageType = new RuleExpression();
						imageType.addRuleWord(new TextWord(C_商品图片));

						RuleExpression useOriUrl = null;

                        RuleExpression imageIndex = new RuleExpression();
                        imageIndex.addRuleWord(new TextWord("0"));
                        imageIndex.addRuleWord(new TextWord("1"));
                        imageIndex.addRuleWord(new TextWord("2"));
                        imageIndex.addRuleWord(new TextWord("3"));
                        imageIndex.addRuleWord(new TextWord("4"));

						CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, imageIndex);
						ruleRoot.addRuleWord(new CustomWord(word));
					}
				}

//				{
//					// 购物流程图
//					String html = "<div><img src=\"%s\" /></div>";
//					html = String.format(html, "https://img.alicdn.com/imgextra/i2/2854639042/TB2zCaipVXXXXaZXXXXXXXXXXXX_!!2854639042.jpg");
//					TextWord word = new TextWord(html);
//					ruleRoot.addRuleWord(word);
//				}
//
//				{
//					// 店铺介绍图
//					String html = "<div><img src=\"%s\" /></div>";
//					html = String.format(html, "https://img.alicdn.com/imgextra/i2/2854639042/TB2PpSdpVXXXXbcXXXXXXXXXXXX_!!2854639042.jpg");
//					TextWord word = new TextWord(html);
//					ruleRoot.addRuleWord(word);
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
			// div-000-end
			String html = "</div>";
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		return ruleRoot;
	}

//    private RuleExpression doDict_无线描述() {
//        // 根字典
//        RuleExpression ruleRoot = new RuleExpression();
//        {
//            // start
//            String kv = "{\"wireless_desc\":{";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
////        {
////            // K-V 模板
////            String kv = "\"k1\":{\"k1-1\":\"v1\",\"k1-2\":\"v2\"},";
////            TextWord word = new TextWord(kv);
////            ruleRoot.addRuleWord(word);
////
////            <field id="xxx_enable" name="是否启用" type="singleCheck">
////                <options>
////                <option displayName="启用" value="true"/>
////                <option displayName="不启用" value="false"/>
////                </options>
////            </field>
////        }
//
//        {
//            // item_info 商品信息
//            String kv = "\"item_info\":{\"item_info_enable\":\"true\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // coupon 优惠
//            String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"xxx\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // hot_recommanded 同店推荐
////            <field id="hot_recommanded_id" name="选择模板" type="singleCheck">
////                <options>
////                <option displayName="商品推荐" value="520277"/>
////                </options>
////            </field>
//            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"520277\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // shop_discount 店铺活动
//            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"xxx\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // user_define 自定义
//            String kv = "\"user_define\":{\"user_define_enable\":\"true\",\"user_define_name\":\"xxx\",\"user_define_image_0\":\"xxx\",\"user_define_image_1\":\"xxx\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // item_picture 商品图片
//            String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//
//            for (int i = 0; i < 5; i++) {
//                // 5张产品图片
//                int j = i + 1;
//                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
//                TextWord imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//
//                DictWord dictRoot = new DictWord();
//                dictRoot.setName("产品图片-" + j);
//                ruleRoot.addRuleWord(dictRoot);
//
//                imageStr = "\"}";
//                imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//            }
//            for (int i = 5; i < 10; i++) {
//                // 5张商品图片
//                int j = i - 4;
//                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
//                TextWord imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//
//                DictWord dictRoot = new DictWord();
//                dictRoot.setName("商品图片-" + j);
//                ruleRoot.addRuleWord(dictRoot);
//
//                imageStr = "\"}";
//                imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//            }
//            for (int i = 10; i < 15; i++) {
//                // 5张无线商品图片
//                int j = i - 9;
//                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
//                TextWord imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//
//                DictWord dictRoot = new DictWord();
//                dictRoot.setName("无线商品图片-" + j);
//                ruleRoot.addRuleWord(dictRoot);
//
//                imageStr = "\"}";
//                imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//            }
//            for (int i = 15; i < 20; i++) {
//                // 5张无线自定义图片
//                int j = i - 14;
//                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
//                TextWord imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//
//                DictWord dictRoot = new DictWord();
//                dictRoot.setName("无线自定义图片-" + j);
//                ruleRoot.addRuleWord(dictRoot);
//
//                imageStr = "\"}";
//                imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//            }
//
//            // end
//            String endStr = "}";
//            TextWord endWord = new TextWord(endStr);
//            ruleRoot.addRuleWord(endWord);
//        }
//
//        {
//            // end
//            String kv = "}}";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        return ruleRoot;
//    }

    private RuleExpression doDict_无线描述2() {
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
            String kv = "\"item_info\":{\"item_info_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // coupon 优惠
//            String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"xxx\"},";
            String kv = "\"coupon\":{\"coupon_enable\":\"false\"},";
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
             // modified by morse.lu 2016/08/05 start
            // 不写死了，从画面上取
//            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"626083\"},";
////            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"false\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);

            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);

            // 同店推荐的field_id
            MasterWord masterWord = new MasterWord("hot_recommanded_id");
            ruleRoot.addRuleWord(masterWord);

            kv = "\"},";
            word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
            // modified by morse.lu 2016/08/05 end
        }

        {
            // shop_discount 店铺活动
//            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"xxx\"},";
            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // user_define 自定义
//            String kv = "\"user_define\":{\"user_define_enable\":\"true\",\"user_define_name\":\"xxx\",\"user_define_image_0\":\"xxx\",\"user_define_image_1\":\"xxx\"},";
            String kv = "\"user_define\":{\"user_define_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // item_picture 商品图片
            String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);

            // Target无线端一共20张商品图片
            // No.1     共通图片-店铺简介        (1张无线图片)
            // No.2     共通图片-品牌图          (1张无线图片)
            // No.3     参数图 (模板*1)         (1张无线图片)
            // No.4~13  自定义图                (10张无线图片)
            // No.14~18 商品图（图2~图6）(模板*2) (5张无线图片)
            // No.19~20 共通图片-物流图          (2张无线图片)
            //
            // 注：
            // 模板*1：http://s7d5.scene7.com/is/image/sneakerhead/Target%5F20160527%5Fx790%5F500x%5FPC?$790%5F500$&$text08=8888888888&$text07=7777777777777777&$text06=666666666666&$text05=555555555555555&$text04=44444444444444444&$text03=3333333333333&$text02=22222222222222&$text01=1111111111&$product=018%2D13130411%2D1
            // 模板*2：http://s7d5.scene7.com/is/image/sneakerhead/Target%5F20160527%5Fx790%5F500x?$790%5F500$&$product=018%2D13130411%2D1

            // No.1 1张共通图片-店铺简介图片
            for (int i = 0; i < 1; i++) {
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 店铺图
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression useOriUrl = null;

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
                ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.2 1张共通图片-品牌图
            for (int i = 1; i < 2; i++) {
                // 5张商品图片
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 品牌图
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression useOriUrl = null;

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.3 1张参数图 (模板*1)
            for (int i = 2; i < 3; i++) {
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 参数图
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate =
                        "http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x_PC?$790_500$&$" +
                                "&$product=%s" + // 图片
                                "&$text01=%s" +  // 标题1   文本1
                                "&$text02=%s" +
                                "&$text03=%s" +
                                "&$text04=%s" +
                                "&$text05=%s" +
                                "&$text06=%s" +
                                "&$text07=%s" +
                                "&$text08=%s";
//                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x_PC?$bbbbbbbb790x500bbbbbbbb$&$product=%s&$text01=%s&$text02=%s&$text03=%s&$text04=%s&$text05=%s&$text06=%s&$text07=%s&$text08=%s";
                imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                // 设置参数imageParams的值
                List<RuleExpression> imageParams = new ArrayList<>();
                {
                    // 第一个参数是product_id(GetMainProductImages)
                    CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));   // 第一张商品图片
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
                    for (int index = 0; index < 8; index++) {
                        RuleExpression ruleExpression = new RuleExpression();
                        ruleExpression.addRuleWord(new FeedCnWord(true, index));
                        ruleExpression.addRuleWord(new TextWord("   "));
                        ruleExpression.addRuleWord(new FeedCnWord(false, index));
                        imageParams.add(ruleExpression);
                    }
                }

                CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                ruleRoot.addRuleWord(new CustomWord(imagesWithParamWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.4~13  10张无线自定义图
            for (int i = 3; i < 13; i++) {
                int j = i - 2;
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 无线自定义图字典
                DictWord dictRoot = new DictWord();
                dictRoot.setName("无线自定义图片-" + j);   // 1~10
                ruleRoot.addRuleWord(dictRoot);

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.14~18 商品图（图2~图6）(模板*2) (5张无线图片)
            for (int i = 13; i < 18; i++) {
                int j = i - 12;
                // 5张商品图片
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 无线商品图（图2~图6）字典
                DictWord dictRoot = new DictWord();
                dictRoot.setName("无线商品图片-" + j);   // 1~5
                ruleRoot.addRuleWord(dictRoot);

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.19~20 共通图片-物流图  (2张无线图片)
            for (int i = 18; i < 20; i++) {
                int j = i - 18;
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 物流图
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord(String.valueOf(j)));   // 图片index(0,1)

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression useOriUrl = null;

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

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

    private RuleExpression doDict_无线描述_重点商品() {
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
            String kv = "\"item_info\":{\"item_info_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // coupon 优惠
//            String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"xxx\"},";
            String kv = "\"coupon\":{\"coupon_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);

            // 同店推荐的field_id
            MasterWord masterWord = new MasterWord("hot_recommanded_id");
            ruleRoot.addRuleWord(masterWord);

            kv = "\"},";
            word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
            // modified by morse.lu 2016/08/05 end
        }

        {
            // shop_discount 店铺活动
//            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"xxx\"},";
            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // user_define 自定义
//            String kv = "\"user_define\":{\"user_define_enable\":\"true\",\"user_define_name\":\"xxx\",\"user_define_image_0\":\"xxx\",\"user_define_image_1\":\"xxx\"},";
            String kv = "\"user_define\":{\"user_define_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // item_picture 商品图片
            String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);

            // Target无线端一共20张商品图片
            // No.1     共通图片-店铺简介        (1张无线图片)
            // No.2     共通图片-品牌故事        (1张品牌故事图片)
            // No.3~17  自定义                   (15张无线自定义图)
            // No.18    共通图片-品牌故事        (1张品牌故事图片)
            // No.19~20 共通图片-物流图          (2张物流图片)

			for (int i = 0; i < 1; i++) {
				String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
				TextWord imageWord = new TextWord(imageStr);
				ruleRoot.addRuleWord(imageWord);

				// 无线自定义图字典
				CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));         // 自定义图片index(0~9)
				userParam.setImageIndex(imageIndex);
				RuleExpression img_imageType = new RuleExpression();
				img_imageType.addRuleWord(new TextWord(C_带角度图片));
				userParam.setImageType(img_imageType);

				RuleExpression useOriUrl = new RuleExpression();
				// 自定义图因为长宽不确定，所以只能使用原图
				useOriUrl.addRuleWord(new TextWord("1"));  // 使用原图
				userParam.setUseOriUrl(useOriUrl);

				CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
				wordValueGetMainProductImages.setUserParam(userParam);
				ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));

				imageStr = "\"}";
				imageWord = new TextWord(imageStr);
				ruleRoot.addRuleWord(imageWord);
			}

			// No.1 1张共通图片-店铺简介图片
            for (int i = 1; i < 2; i++) {
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 店铺图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0")); // 第一张图

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.2 1张共通图片-品牌图
            for (int i = 2; i < 3; i++) {
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 品牌图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0")); // 第一张图

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.3~17  自定义
            for (int i = 3; i < 17; i++) {
                int j = i - 2;
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 无线自定义图字典
                DictWord dictRoot = new DictWord();
                dictRoot.setName("无线自定义图片-" + j);   // 1~15
                ruleRoot.addRuleWord(dictRoot);

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.18 1张共通图片-品牌图
            for (int i = 17; i < 18; i++) {
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 品牌图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("1")); // 第二张图

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

            // No.19~20 共通图片-物流图  (2张无线图片)
            for (int i = 18; i < 20; i++) {
                int j = i - 18;
                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
                TextWord imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);

                // 物流图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord(String.valueOf(j)));   // 图片index(0,1)

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));

                imageStr = "\"}";
                imageWord = new TextWord(imageStr);
                ruleRoot.addRuleWord(imageWord);
            }

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
     * 根据index生成天猫无线自定义图片字典
     *
     * @param index String  无线自定义图片index
     * @return RuleExpression 无线自定义图片字典
     */
    private RuleExpression doDict_无线自定义图片(String index) {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord(index));         // 自定义图片index(0~9)
            userParam.setImageIndex(imageIndex);
            RuleExpression img_imageType = new RuleExpression();
            img_imageType.addRuleWord(new TextWord(C_自定义图片));
            userParam.setImageType(img_imageType);

            RuleExpression useOriUrl = new RuleExpression();
            // 自定义图因为长宽不确定，所以只能使用原图
            useOriUrl.addRuleWord(new TextWord("1"));  // 使用原图
            userParam.setUseOriUrl(useOriUrl);

            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
            wordValueGetMainProductImages.setUserParam(userParam);
            ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));
        }

        return ruleRoot;
    }

    /**
     * 根据index生成天猫无线商品图2-6的图片字典
     *
     * @param index String  自定义图片index
     * @return RuleExpression 无线商品图片字典
     */
    private RuleExpression doDict_无线商品图片(String index) {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // 商品图（图2~图6）
            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("%s"));
            userParam.setHtmlTemplate(htmlTemplate);
            RuleExpression imageTemplate = new RuleExpression();
            // %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x?$790_500$&$product=%s"));
            userParam.setImageTemplate(imageTemplate);
            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord(String.valueOf(index)));   // 商品图片(图2~图6)index(1~5)
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

//	/**
//	 * 获取手绘图的默认图
//	 * 当手绘图不存在的场合, 用这张共通的图片替换掉
//	 *
//	 * @param paddingImageIndex 使用第几张替换的图
//	 * @return 默认图
//	 */
//
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