package com.voyageone.task2.cms.dictSellerCids;

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
public class Tmall_001_Sneakerhead_SellerCids_DictTest extends BaseSellerCidsDictTest {

	private String C_BRAND = "brand";
	private String C_PRODUCT_TYPE = "productType";
	private String C_SIZE_TYPE = "sizeType";
	private String C_SHORT_DESC = "shortDescription";

	@Test
	public void startupTest() {
		System.out.println("天猫国际");
		do天猫国际_男鞋();
		do天猫国际_女鞋("Women");
		do天猫国际_女鞋("Big Kids");
		do天猫国际_童鞋和服饰配件();

		System.out.println("京东");
		do京东_各种品牌();
		do京东_各种鞋类();
		do京东_童鞋();
		do京东_男鞋();
		do京东_女鞋("Women");
		do京东_女鞋("Big Kids");
		do京东_服饰配件();

		System.out.println("京东国际");
		do京东国际_各种品牌();
		do京东国际_各种鞋类();
		do京东国际_童鞋();
		do京东国际_男鞋();
		do京东国际_女鞋("Women");
		do京东国际_女鞋("Big Kids");
		do京东国际_服饰配件();


	}

	private void do天猫国际_男鞋() {

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385125", "1264026237", "男鞋", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385125", "1264026238", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385125", "1264026238", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385125", "1264026238", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385125", "1264026239", "男鞋", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385125", "1264026240", "男鞋", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385125", "1264078763", "男鞋", "其他")));
		}

	}

	private void do天猫国际_女鞋(String sizeType) {

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385995", "1264026242", "女鞋", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385995", "1264026243", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385995", "1264026243", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385995", "1264026243", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385995", "1264026244", "女鞋", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385995", "1264026245", "女鞋", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1140385995", "1264078764", "女鞋", "其他")));
		}

	}

	private void do天猫国际_童鞋和服饰配件() {

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Big Kids"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1168809246", "1264026247", "童鞋", "Big Kids（大童）")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Preschool"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1168809246", "1264026248", "童鞋", "Preschool（幼童）")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Toddler"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("1168809246", "1264026249", "童鞋", "Toddler（婴童）")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Neq, "1", C_PRODUCT_TYPE, "Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "1140387718", "", "服饰配件")));
		}

	}

	private void do各种品牌(String brand, SellerCids value) {
		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, brand));

			doCreateJson(doCreateSimpleIf(simpleCaseList, value));
		}
	}

	private void do京东_各种品牌() {
		do各种品牌("Sneakerhead", new SellerCids("", "1414125", "", "Sneakerhead"));
		do各种品牌("Nike", new SellerCids("", "1099945", "", "Nike/耐克"));
		do各种品牌("Jordan", new SellerCids("", "1100006", "", "AJ乔丹 Air Jordan"));
		do各种品牌("New Balance", new SellerCids("", "1099970", "", "New Balance"));
		do各种品牌("Converse", new SellerCids("", "1099968", "", "Converse/匡威"));
		do各种品牌("Reebok", new SellerCids("", "1099969", "", "Reebok/锐步"));
		do各种品牌("Supra", new SellerCids("", "1099974", "", "Supra"));
		do各种品牌("Puma", new SellerCids("", "1100022", "", "Puma"));
		do各种品牌("Toms", new SellerCids("", "1099975", "", "TOMS"));
		do各种品牌("Saucony", new SellerCids("", "1414206", "", "Saucony/索康尼"));
		do各种品牌("Vans", new SellerCids("", "1414207", "", "VANS/万斯"));
		do各种品牌("Creative Recreation", new SellerCids("", "1414208", "", "Creative Recreation"));
		do各种品牌("Levi's", new SellerCids("", "1414210", "", "Levi’s/李维斯"));
		do各种品牌("Timberland", new SellerCids("", "1414211", "", "Timberland/添柏岚"));
		do各种品牌("Columbia", new SellerCids("", "1414215", "", "Columbia"));
		do各种品牌("Herschel Supply", new SellerCids("", "1414213", "", "Herschel"));
		do各种品牌("Generic Surplus", new SellerCids("", "1414217", "", "Generic Surplus"));
		do各种品牌("K Swiss", new SellerCids("", "1414219", "", "K-SWISS"));
		do各种品牌("Topo Designs", new SellerCids("", "4128370", "", "TOPO"));
		do各种品牌("Asics", new SellerCids("", "5510044", "", "Asics/亚瑟士"));
		do各种品牌("Asics Onitsuka Tiger", new SellerCids("", "5903944", "", "Onitsuka Tiger"));

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "stan smith"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5499656", "5499657", "Adidas", "stan smith")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "superstar"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5499656", "5499658", "Adidas", "superstar")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "tubular"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5499656", "5499659", "Adidas", "tubular")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "boost"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5499656", "5499660", "Adidas", "boost")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "stan smith"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "superstar"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "tubular"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "boost"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5499656", "5499661", "Adidas", "adias更多")));
		}
	}

	private void do京东_各种鞋类() {
		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5842939", "", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5842980", "", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5842984", "", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5842981", "5842983", "板鞋/休闲鞋", "休闲鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5842981", "5842983", "板鞋/休闲鞋", "休闲鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5842981", "5842982", "板鞋/休闲鞋", "板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5842985", "", "其他鞋类")));
		}

	}

	private void do京东_童鞋() {
		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Big Kids"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("3849384", "5287603", "童鞋", "Big Kids")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Preschool"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("3849384", "5287604", "童鞋", "Preschool")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Toddler"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("3849384", "5287605", "童鞋", "Toddler")));
		}

	}

	private void do京东_男鞋() {

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313717", "5287866", "男鞋", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313717", "5287867", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313717", "5287867", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313717", "5287867", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313717", "5287868", "男鞋", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313717", "5287869", "男鞋", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313717", "5287870", "男鞋", "其他")));
		}

	}

	private void do京东_女鞋(String sizeType) {

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313718", "5287871", "女鞋", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313718", "5287872", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313718", "5287872", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313718", "5287872", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313718", "5287873", "女鞋", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313718", "5287874", "女鞋", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("4313718", "5287875", "女鞋", "其他")));
		}

	}

	private void do京东_服饰配件() {
		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "T Shirt"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843027", "服饰配件", "T恤")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Hoodie"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843028", "服饰配件", "卫衣/套头衫")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Crewneck"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843028", "服饰配件", "卫衣/套头衫")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Jacket"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843029", "服饰配件", "夹克外套")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Windbreaker"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843029", "服饰配件", "夹克外套")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Parka"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843029", "服饰配件", "夹克外套")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Pants"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843030", "服饰配件", "运动裤")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Shorts"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843030", "服饰配件", "运动裤")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Bags"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843031", "服饰配件", "运动包")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Hats"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843032", "服饰配件", "配件")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Sunglasses"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843032", "服饰配件", "配件")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Watches"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843032", "服饰配件", "配件")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Accessories"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843033", "服饰配件", "其他")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Knives"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5843026", "5843033", "服饰配件", "其他")));
		}

	}

	private void do京东国际_各种品牌() {
		do各种品牌("Nike", new SellerCids("", "2392238", "", "Nike耐克"));
		do各种品牌("Jordan", new SellerCids("", "2392300", "", "AJ乔丹 Air Jordan"));
		do各种品牌("Sneakerhead", new SellerCids("", "2392298", "", "sneakerhead"));
		do各种品牌("New Balance", new SellerCids("", "2392409", "", "New Balance"));
		do各种品牌("Asics", new SellerCids("", "2392410", "", "Asics"));
		do各种品牌("Asics Onitsuka Tiger", new SellerCids("", "5903704", "", "Onitsuka tiger"));
		do各种品牌("Saucony", new SellerCids("", "2392353", "", "Saucony"));
		do各种品牌("Reebok", new SellerCids("", "2392355", "", "Reebok"));
		do各种品牌("Puma", new SellerCids("", "2392378", "", "Puma"));
		do各种品牌("Vans", new SellerCids("", "2392354", "", "Vans"));
		do各种品牌("Supra", new SellerCids("", "2392447", "", "Supra"));
		do各种品牌("Levi's", new SellerCids("", "2392299", "", "Levi’s"));
		do各种品牌("Converse", new SellerCids("", "2392351", "", "Converse"));
		do各种品牌("Timberland", new SellerCids("", "2392350", "", "Timberland"));
		do各种品牌("Creative Recreation", new SellerCids("", "2392411", "", "Creative Recreation"));
		do各种品牌("Palladium", new SellerCids("", "2392414", "", "Palladium"));
		do各种品牌("Native", new SellerCids("", "2392364", "", "Native"));
		do各种品牌("Swims", new SellerCids("", "2392448", "", "Swims"));
		do各种品牌("Toms", new SellerCids("", "2392450", "", "Toms"));
		do各种品牌("Herschel Supply", new SellerCids("", "2392456", "", "Herschel"));
		do各种品牌("Radii", new SellerCids("", "2392459", "", "Radii"));
		do各种品牌("Clarks", new SellerCids("", "2392463", "", "Clarks"));
		do各种品牌("Dr. Martens", new SellerCids("", "2392488", "", "Dr. Martens"));
		do各种品牌("Bucketfeet", new SellerCids("", "4127963", "", "BucketFeet"));
		do各种品牌("Diadora", new SellerCids("", "4227748", "", "Diadora"));

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "stan smith"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5496352", "5496353", "Adidas", "stan smith")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "superstar"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5496352", "5496354", "Adidas", "superstar")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "tubular"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5496352", "5496355", "Adidas", "tubular")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "boost"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5496352", "5496356", "Adidas", "boost")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_BRAND, "Adidas"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "stan smith"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "superstar"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "tubular"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "boost"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5496352", "5496357", "Adidas", "Adidas更多")));
		}
	}

	private void do京东国际_各种鞋类() {
		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5829030", "", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5829031", "", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5829151", "", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829032", "5829035", "板鞋/休闲鞋", "休闲鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829032", "5829035", "板鞋/休闲鞋", "休闲鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829032", "5829034", "板鞋/休闲鞋", "板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("", "5829306", "", "其他鞋类")));
		}

	}

	private void do京东国际_童鞋() {
		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Big Kids"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333906", "5333907", "童鞋", "Big Kids")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Preschool"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333906", "5333908", "童鞋", "Preschool")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Toddler"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333906", "5333909", "童鞋", "Toddler")));
		}

	}

	private void do京东国际_男鞋() {

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333894", "5333895", "男鞋", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333894", "5333896", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333894", "5333896", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333894", "5333896", "男鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333894", "5333897", "男鞋", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333894", "5333898", "男鞋", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, "Men"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333894", "5333899", "男鞋", "其他")));
		}

	}

	private void do京东国际_女鞋(String sizeType) {

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Running Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333900", "5333901", "女鞋", "跑步鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Lifestyle Shoes"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333900", "5333902", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Retro"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333900", "5333902", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Skateborad"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333900", "5333902", "女鞋", "休闲鞋/板鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Basketball"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333900", "5333903", "女鞋", "篮球鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333900", "5333904", "女鞋", "训练鞋")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_SIZE_TYPE, sizeType));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Running Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Lifestyle Shoes"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Retro"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Skateborad"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Basketball"));
			simpleCaseList.add(new SimpleCase(CompareType.NLike, "1", C_SHORT_DESC, "Training"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5333900", "5333905", "女鞋", "其他")));
		}

	}

	private void do京东国际_服饰配件() {
		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "T Shirt"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829300", "服饰配件", "T恤")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Hoodie"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829301", "服饰配件", "卫衣/套头衫")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Crewneck"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829301", "服饰配件", "卫衣/套头衫")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Jacket"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829302", "服饰配件", "夹克外套")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Windbreaker"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829302", "服饰配件", "夹克外套")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Parka"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829302", "服饰配件", "夹克外套")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Pants"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829303", "服饰配件", "裤子")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Apparel"));
			simpleCaseList.add(new SimpleCase(CompareType.Like, "1", C_SHORT_DESC, "Shorts"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829303", "服饰配件", "裤子")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Bags"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829304", "服饰配件", "运动包")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Hats"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829305", "服饰配件", "配件")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Sunglasses"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829305", "服饰配件", "配件")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Watches"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829305", "服饰配件", "配件")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Accessories"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829386", "服饰配件", "其他")));
		}

		{
			List<SimpleCase> simpleCaseList = new ArrayList<>();
			simpleCaseList.add(new SimpleCase(CompareType.Eq, "1", C_PRODUCT_TYPE, "Knives"));

			doCreateJson(doCreateSimpleIf(simpleCaseList, new SellerCids("5829333", "5829386", "服饰配件", "其他")));
		}

	}

}