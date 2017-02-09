package com.voyageone.task2.cms.dictSellerCids;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成店铺内分类字典
 * 放入voyageone_cms2.cms_mt_channel_condition_config表中
 *
 * 关于IF条件结果值里面4个propValue的说明：
 * 1.propValue    (cId    子分类id)          (例："1124130584")
 * 2.propValue_2  (cIds   父分类id,子分类id)  (例："1124130579,1124130584")
 * 3.propValue_3  (cName  父分类id,子分类id)  (例："系列>彩色宝石")
 * 4.propValue_4  (cNames 父分类id,子分类id)  (例："系列,彩色宝石")
 *          ↓   ↓   ↓
 * 1."cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"
 *  例："1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"
 *
 * ※feed->master导入的时候，会读取从表中读取这里生成的字典数据，然后设置到Product.platform.PXX.sellerCarts[]里面
 *
 * @author tom.zhu on 2017/2/9.
 * @version 2.4.0
 * @since 2.4.0
 *
 */
public class Tmall_007_Champion_SellerCids_DictTest extends BaseSellerCidsDictTest {

	@Test
	public void startupTest() {
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "REVERSE WEAVE", new SellerCids("1280718146", "1280718147", "CASUAL", "Reverse Weave")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "Basic", new SellerCids("1280718146", "1280718148", "CASUAL", "Basic")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "Rochester", new SellerCids("1280718146", "1280718150", "CASUAL", "Rochester")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "Campus", new SellerCids("1280718146", "1280718151", "CASUAL", "Campus")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "ACTION STYLE", new SellerCids("1280718146", "1280718149", "CASUAL", "Action Style")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "MADE IN USA", new SellerCids("1280718146", "1280718152", "CASUAL", "Made in USA")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "Sports", new SellerCids("1280695619", "1280695620", "SPORTS", "Training")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "WOMEN’S", new SellerCids("1280718146", "1280718153", "CASUAL", "Women's")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "MEN’S BASIC PERFORMANCE", new SellerCids("1280695619", "1280695620", "SPORTS", "Training")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "ACCESSORIES", new SellerCids("", "1280959563", "", "配饰")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "BASIC ATHLETIC", new SellerCids("1280718146", "1280718148", "CASUAL", "Basic")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "E-MOTION", new SellerCids("1280695619", "1280695621", "SPORTS", "Basketball")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "GAGERS", new SellerCids("1280695619", "1280695621", "SPORTS", "Basketball")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "SMART ATHLETIC", new SellerCids("1280695619", "1280695620", "SPORTS", "Training")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "PLAYGROUND", new SellerCids("1280695619", "1280695621", "SPORTS", "Basketball")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "70's VINTAGE", new SellerCids("1280718146", "1280718150", "CASUAL", "Rochester")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "shortDescription", "CAGERS", new SellerCids("1280695619", "1280695621", "SPORTS", "Basketball")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "T恤", new SellerCids("", "1280959560", "", "T恤")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "卫衣", new SellerCids("", "1280959558", "", "卫衣")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "卫裤", new SellerCids("", "1280959559", "", "卫裤")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "外套", new SellerCids("", "1280959562", "", "外套")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "短裤", new SellerCids("", "1280959561", "", "短裤")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "衬衫", new SellerCids("", "1294093088", "", "衬衫")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "运动裤", new SellerCids("", "1294098952", "", "运动裤")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Like, "category", "衬衫", new SellerCids("", "1294093088", "", "衬衫")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "sizeType", "Women ", new SellerCids("", "1280959564", "", "女装")));
		doCreateJson(doCreateSimpleIf_Feed_Text(CompareType.Eq, "sizeType", "NoSize", new SellerCids("", "1280959563", "", "配饰")));
	}


}