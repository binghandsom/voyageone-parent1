package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.util.JsonUtil;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2015/12/3.
 */

//@RunWith(SpringJUnit4ClassRunner.class)
public class SchemaTest {

        private String docStr1 = "<?xml version=\"1.0\"?>\n" +
                "<itemRule>\n" +
                "    <field id=\"prop_13021751\" name=\"货号\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "    <field id=\"prop_20000\" name=\"品牌\" type=\"singleCheck\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "        </rules>\n" +
                "        <options>\n" +
                "            <option displayName=\"UNDER ARMOUR\" value=\"3721844\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_8560225\" name=\"上市时间\" type=\"singleCheck\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "        </rules>\n" +
                "        <options>\n" +
                "            <option displayName=\"2011年以前\" value=\"95851171\" />\n" +
                "            <option displayName=\"2011年春夏季\" value=\"493280606\" />\n" +
                "            <option displayName=\"2011年秋冬季\" value=\"493280605\" />\n" +
                "            <option displayName=\"2012年春夏季\" value=\"493280602\" />\n" +
                "            <option displayName=\"2012年秋冬季\" value=\"191082002\" />\n" +
                "            <option displayName=\"2013年春夏季\" value=\"236814024\" />\n" +
                "            <option displayName=\"2013年秋冬季\" value=\"305984387\" />\n" +
                "            <option displayName=\"2014年春夏季\" value=\"389166204\" />\n" +
                "            <option displayName=\"2014年秋冬季\" value=\"583724267\" />\n" +
                "            <option displayName=\"2015年\" value=\"14139135\" />\n" +
                "            <option displayName=\"2015年冬季\" value=\"740132938\" />\n" +
                "            <option displayName=\"2015年夏季\" value=\"647672577\" />\n" +
                "            <option displayName=\"2015年春季\" value=\"379874864\" />\n" +
                "            <option displayName=\"2015年秋季\" value=\"715192583\" />\n" +
                "            <option displayName=\"2016年春季\" value=\"854168429\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_148380063\" name=\"销售渠道类型\" type=\"singleCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"纯电商(只在线上销售)\" value=\"852538341\" />\n" +
                "            <option displayName=\"商场同款(线上线下都销售)\" value=\"852424991\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_24477\" name=\"性别\" type=\"singleCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"男\" value=\"20532\" />\n" +
                "            <option displayName=\"女\" value=\"20533\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_122216619\" name=\"折数\" type=\"singleCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"1折\" value=\"11041075\" />\n" +
                "            <option displayName=\"2折\" value=\"11041076\" />\n" +
                "            <option displayName=\"3折\" value=\"11041077\" />\n" +
                "            <option displayName=\"其他\" value=\"20213\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_1627937\" name=\"内部结构\" type=\"multiCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"其他\" value=\"20213\" />\n" +
                "            <option displayName=\"大钞夹\" value=\"3270276\" />\n" +
                "            <option displayName=\"相片位\" value=\"3270277\" />\n" +
                "            <option displayName=\"暗格\" value=\"130141\" />\n" +
                "            <option displayName=\"零钱位\" value=\"3270278\" />\n" +
                "            <option displayName=\"存折位\" value=\"3270279\" />\n" +
                "            <option displayName=\"拉链格\" value=\"3270280\" />\n" +
                "            <option displayName=\"证件位\" value=\"3270281\" />\n" +
                "            <option displayName=\"卡位\" value=\"3270282\" />\n" +
                "            <option displayName=\"钥匙位\" value=\"11038123\" />\n" +
                "            <option displayName=\"支票位\" value=\"11038157\" />\n" +
                "            <option displayName=\"护照位\" value=\"11038383\" />\n" +
                "            <option displayName=\"其它\" value=\"-1\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"in_prop_1627937\" name=\"内部结构\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"disableRule\" value=\"true\">\n" +
                "                <depend-group operator=\"and\">\n" +
                "                    <depend-express fieldId=\"prop_1627937\" value=\"-1\" symbol=\"not contains\" />\n" +
                "                </depend-group>\n" +
                "            </rule>\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "    <field id=\"prop_20021\" name=\"质地\" type=\"singleCheck\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "        </rules>\n" +
                "        <options>\n" +
                "            <option displayName=\"其他\" value=\"20213\" />\n" +
                "            <option displayName=\"珍珠鱼皮\" value=\"6856831\" />\n" +
                "            <option displayName=\"蜥蜴皮\" value=\"3357523\" />\n" +
                "            <option displayName=\"PU\" value=\"3323086\" />\n" +
                "            <option displayName=\"牛皮\" value=\"28397\" />\n" +
                "            <option displayName=\"羊皮\" value=\"28398\" />\n" +
                "            <option displayName=\"猪皮\" value=\"28399\" />\n" +
                "            <option displayName=\"锦纶\" value=\"112997\" />\n" +
                "            <option displayName=\"帆布\" value=\"28401\" />\n" +
                "            <option displayName=\"涤纶\" value=\"28355\" />\n" +
                "            <option displayName=\"牛仔布\" value=\"28343\" />\n" +
                "            <option displayName=\"蛇皮\" value=\"44857\" />\n" +
                "            <option displayName=\"鳄鱼皮\" value=\"44858\" />\n" +
                "            <option displayName=\"灯芯绒\" value=\"28344\" />\n" +
                "            <option displayName=\"无纺布\" value=\"3224556\" />\n" +
                "            <option displayName=\"牛津纺\" value=\"130145\" />\n" +
                "            <option displayName=\"PVC\" value=\"29228\" />\n" +
                "            <option displayName=\"麻\" value=\"3267653\" />\n" +
                "            <option displayName=\"丝绒\" value=\"105253\" />\n" +
                "            <option displayName=\"丝绸\" value=\"130223\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_122216629\" name=\"材质工艺\" type=\"singleCheck\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "            <rule name=\"disableRule\" value=\"true\">\n" +
                "                <depend-group operator=\"and\">\n" +
                "                    <depend-express fieldId=\"prop_20021\" value=\"3323086\" symbol=\"!=\" />\n" +
                "                </depend-group>\n" +
                "            </rule>\n" +
                "        </rules>\n" +
                "        <options>\n" +
                "            <option displayName=\"磨砂\" value=\"90765\" />\n" +
                "            <option displayName=\"软面\" value=\"15354885\" />\n" +
                "            <option displayName=\"压花\" value=\"9405784\" />\n" +
                "            <option displayName=\"亮面\" value=\"19253708\" />\n" +
                "            <option displayName=\"印花\" value=\"129555\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_122276073\" name=\"皮质特征\" type=\"singleCheck\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "            <rule name=\"disableRule\" value=\"true\">\n" +
                "                <depend-group operator=\"and\">\n" +
                "                    <depend-express fieldId=\"prop_20021\" value=\"28397\" symbol=\"!=\" />\n" +
                "                </depend-group>\n" +
                "            </rule>\n" +
                "        </rules>\n" +
                "        <options>\n" +
                "            <option displayName=\"牛剖成移膜革/牛皮革\" value=\"969954999\" />\n" +
                "            <option displayName=\"牛皮剖层移膜革\" value=\"145737304\" />\n" +
                "            <option displayName=\"牛皮革\" value=\"21792004\" />\n" +
                "            <option displayName=\"牛皮革/合成革（超纤）\" value=\"1023386009\" />\n" +
                "            <option displayName=\"牛皮革/牛剖层移膜革\" value=\"699548204\" />\n" +
                "            <option displayName=\"牛二层皮\" value=\"27389350\" />\n" +
                "            <option displayName=\"头层牛皮\" value=\"3880992\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_34272\" name=\"流行元素\" type=\"singleCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"撞色\" value=\"130568\" />\n" +
                "            <option displayName=\"编织\" value=\"30080\" />\n" +
                "            <option displayName=\"花朵\" value=\"99819\" />\n" +
                "            <option displayName=\"荔枝纹\" value=\"4459826\" />\n" +
                "            <option displayName=\"豹纹\" value=\"3255041\" />\n" +
                "            <option displayName=\"链条\" value=\"4015931\" />\n" +
                "            <option displayName=\"锁扣\" value=\"13869498\" />\n" +
                "            <option displayName=\"鳄鱼纹\" value=\"3990926\" />\n" +
                "            <option displayName=\"鳗鱼纹\" value=\"61852687\" />\n" +
                "            <option displayName=\"镂空\" value=\"115771\" />\n" +
                "            <option displayName=\"蝴蝶结\" value=\"115772\" />\n" +
                "            <option displayName=\"亮片\" value=\"29959\" />\n" +
                "            <option displayName=\"蕾丝\" value=\"28386\" />\n" +
                "            <option displayName=\"褶皱\" value=\"112602\" />\n" +
                "            <option displayName=\"皮带装饰\" value=\"115774\" />\n" +
                "            <option displayName=\"车缝线\" value=\"115775\" />\n" +
                "            <option displayName=\"串珠\" value=\"30081\" />\n" +
                "            <option displayName=\"铆钉\" value=\"115776\" />\n" +
                "            <option displayName=\"流苏\" value=\"115777\" />\n" +
                "            <option displayName=\"字母\" value=\"45576\" />\n" +
                "            <option displayName=\"绣花\" value=\"29957\" />\n" +
                "            <option displayName=\"压花\" value=\"9405784\" />\n" +
                "            <option displayName=\"织花\" value=\"11032875\" />\n" +
                "            <option displayName=\"印花\" value=\"129555\" />\n" +
                "            <option displayName=\"其他\" value=\"20213\" />\n" +
                "            <option displayName=\"其它\" value=\"-1\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"in_prop_34272\" name=\"流行元素\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"disableRule\" value=\"true\">\n" +
                "                <depend-group operator=\"and\">\n" +
                "                    <depend-express fieldId=\"prop_34272\" value=\"-1\" symbol=\"!=\" />\n" +
                "                </depend-group>\n" +
                "            </rule>\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "    <field id=\"prop_20603\" name=\"图案\" type=\"singleCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"纯色\" value=\"29454\" />\n" +
                "            <option displayName=\"文字\" value=\"123656\" />\n" +
                "            <option displayName=\"植物花卉\" value=\"104170033\" />\n" +
                "            <option displayName=\"人物\" value=\"46649\" />\n" +
                "            <option displayName=\"动物图案\" value=\"129881\" />\n" +
                "            <option displayName=\"几何图案\" value=\"3222243\" />\n" +
                "            <option displayName=\"卡通动漫\" value=\"14031880\" />\n" +
                "            <option displayName=\"水果\" value=\"98534\" />\n" +
                "            <option displayName=\"风景\" value=\"40793\" />\n" +
                "            <option displayName=\"条纹\" value=\"29452\" />\n" +
                "            <option displayName=\"其他\" value=\"20213\" />\n" +
                "            <option displayName=\"格子\" value=\"29453\" />\n" +
                "            <option displayName=\"其它\" value=\"-1\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"in_prop_20603\" name=\"图案\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"disableRule\" value=\"true\">\n" +
                "                <depend-group operator=\"and\">\n" +
                "                    <depend-express fieldId=\"prop_20603\" value=\"-1\" symbol=\"!=\" />\n" +
                "                </depend-group>\n" +
                "            </rule>\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "    <field id=\"prop_122276315\" name=\"款式\" type=\"singleCheck\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "        </rules>\n" +
                "        <options>\n" +
                "            <option displayName=\"其他\" value=\"20213\" />\n" +
                "            <option displayName=\"硬币包\" value=\"10393599\" />\n" +
                "            <option displayName=\"长款钱包\" value=\"3437640\" />\n" +
                "            <option displayName=\"短款钱包\" value=\"13902669\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_20490\" name=\"闭合方式\" type=\"singleCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"其他\" value=\"20213\" />\n" +
                "            <option displayName=\"拉链\" value=\"115481\" />\n" +
                "            <option displayName=\"搭扣\" value=\"139183\" />\n" +
                "            <option displayName=\"抽带\" value=\"3221906\" />\n" +
                "            <option displayName=\"拉链搭扣\" value=\"3221905\" />\n" +
                "            <option displayName=\"魔术贴\" value=\"3227269\" />\n" +
                "            <option displayName=\"包盖式\" value=\"11031105\" />\n" +
                "            <option displayName=\"敞口\" value=\"11031137\" />\n" +
                "            <option displayName=\"挂钩\" value=\"95742\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_122216587\" name=\"里料材质\" type=\"singleCheck\">\n" +
                "        <options>\n" +
                "            <option displayName=\"尼龙\" value=\"28347\" />\n" +
                "            <option displayName=\"帆布\" value=\"28401\" />\n" +
                "            <option displayName=\"腈纶\" value=\"80664\" />\n" +
                "            <option displayName=\"涤纶\" value=\"28355\" />\n" +
                "            <option displayName=\"合成革\" value=\"30082\" />\n" +
                "            <option displayName=\"棉\" value=\"105255\" />\n" +
                "            <option displayName=\"锦纶\" value=\"112997\" />\n" +
                "            <option displayName=\"涤棉\" value=\"3269748\" />\n" +
                "            <option displayName=\"真皮\" value=\"44660\" />\n" +
                "            <option displayName=\"其它\" value=\"-1\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"in_prop_122216587\" name=\"里料材质\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"disableRule\" value=\"true\">\n" +
                "                <depend-group operator=\"and\">\n" +
                "                    <depend-express fieldId=\"prop_122216587\" value=\"-1\" symbol=\"!=\" />\n" +
                "                </depend-group>\n" +
                "            </rule>\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "    <field id=\"product_images\" name=\"产品图片\" type=\"complex\">\n" +
                "        <fields>\n" +
                "            <field id=\"product_image_0\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                    <rule name=\"requiredRule\" value=\"true\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_1\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_2\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_3\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_4\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "        </fields>\n" +
                "    </field>\n" +
                "    <field id=\"market_price\" name=\"市场价格\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"valueTypeRule\" value=\"decimal\" />\n" +
                "            <rule name=\"minValueRule\" value=\"0.00\" exProperty=\"not include\" />\n" +
                "            <rule name=\"maxValueRule\" value=\"100000000.00\" exProperty=\"not include\" />\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "</itemRule>";


    @Test
    public void testReadXmlForList1() throws Exception {
        List<Field> result1 = SchemaReader.readXmlForList(docStr1);
        String resultStr1 = JsonUtil.getJsonString(result1);
        System.out.println(resultStr1);

        List<com.taobao.top.schema.field.Field> result2 = com.taobao.top.schema.factory.SchemaReader.readXmlForList(docStr1);
        String resultStr2 = JsonUtil.getJsonString(result2);
        System.out.println(resultStr2);
        if (resultStr1.equals(resultStr2)) {
                System.out.println("equal");
        }
    }


        private String docStr2 = "<?xml version=\"1.0\"?>\n" +
                "<itemRule>\n" +
                "    <field id=\"prop_20000\" name=\"品牌\" type=\"singleCheck\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" >12313213</rule>\n" +
                "        </rules>\n" +
                "        <options>\n" +
                "            <option displayName=\"AIR JORDAN\" value=\"5003369\" />\n" +
                "            <option displayName=\"Adidas/阿迪达斯\" value=\"20579\" />\n" +
                "            <option displayName=\"Asics/亚瑟士\" value=\"3290067\" />\n" +
                "            <option displayName=\"ASICS Onitsuka tiger\" value=\"21070527\" />\n" +
                "            <option displayName=\"Atelier Arthur\" value=\"302314339\" />\n" +
                "            <option displayName=\"clarks\" value=\"3276787\" />\n" +
                "            <option displayName=\"Converse/匡威\" value=\"20582\" />\n" +
                "            <option displayName=\"Creative Recreation\" value=\"5918968\" />\n" +
                "            <option displayName=\"DC life collection\" value=\"302310466\" />\n" +
                "            <option displayName=\"GENERIC SURPLUS\" value=\"68458571\" />\n" +
                "            <option displayName=\"Gourmet\" value=\"10575437\" />\n" +
                "            <option displayName=\"k swiss\" value=\"6619613\" />\n" +
                "            <option displayName=\"Keep\" value=\"3528997\" />\n" +
                "            <option displayName=\"Lacoste/拉科斯特\" value=\"27145\" />\n" +
                "            <option displayName=\"LRG\" value=\"3436650\" />\n" +
                "            <option displayName=\"native shoes\" value=\"86657463\" />\n" +
                "            <option displayName=\"NEW BALANCE\" value=\"20584\" />\n" +
                "            <option displayName=\"PALLADIUM\" value=\"6203401\" />\n" +
                "            <option displayName=\"PF FLYERS\" value=\"6792478\" />\n" +
                "            <option displayName=\"PRO－Keds\" value=\"93491009\" />\n" +
                "            <option displayName=\"Puma/彪马\" value=\"20581\" />\n" +
                "            <option displayName=\"radii\" value=\"3986091\" />\n" +
                "            <option displayName=\"Reebok/锐步\" value=\"20580\" />\n" +
                "            <option displayName=\"Saucony/圣康尼\" value=\"227462273\" />\n" +
                "            <option displayName=\"Sebago/仕品高\" value=\"50769648\" />\n" +
                "            <option displayName=\"Supra\" value=\"3271712\" />\n" +
                "            <option displayName=\"SWIMS\" value=\"96332285\" />\n" +
                "            <option displayName=\"Thorocraft\" value=\"87728920\" />\n" +
                "            <option displayName=\"TIMBERLAND/添柏岚\" value=\"28005\" />\n" +
                "            <option displayName=\"VANS\" value=\"29529\" />\n" +
                "            <option displayName=\"Columbia/哥伦比亚\" value=\"27976\" />\n" +
                "            <option displayName=\"Levi’s/李维斯\" value=\"3271216\" />\n" +
                "            <option displayName=\"SOREL\" value=\"9285047\" />\n" +
                "            <option displayName=\"Casio/卡西欧\" value=\"21459\" />\n" +
                "            <option displayName=\"MEISTER\" value=\"22966158\" />\n" +
                "            <option displayName=\"PRDGY\" value=\"302328108\" />\n" +
                "            <option displayName=\"O Clock\" value=\"88244072\" />\n" +
                "            <option displayName=\"MOSLEY TRIBES\" value=\"4272396\" />\n" +
                "            <option displayName=\"STANCE\" value=\"36005150\" />\n" +
                "            <option displayName=\"Herschel supply\" value=\"113523991\" />\n" +
                "            <option displayName=\"INCASE\" value=\"7164819\" />\n" +
                "            <option displayName=\"TOPO\" value=\"3310474\" />\n" +
                "            <option displayName=\"sneakerhead\" value=\"208238204\" />\n" +
                "            <option displayName=\"VICTORINOX/维氏\" value=\"30219\" />\n" +
                "            <option displayName=\"Bearpaw\" value=\"8165945\" />\n" +
                "            <option displayName=\"TOMS\" value=\"3360656\" />\n" +
                "            <option displayName=\"G－SHOCK\" value=\"3221209\" />\n" +
                "            <option displayName=\"the hundreds\" value=\"11824735\" />\n" +
                "            <option displayName=\"huf\" value=\"6038950\" />\n" +
                "            <option displayName=\"Nixon\" value=\"3248446\" />\n" +
                "            <option displayName=\"GLOBE\" value=\"3275551\" />\n" +
                "            <option displayName=\"Jansport\" value=\"30573\" />\n" +
                "            <option displayName=\"KENNEDY\" value=\"6576288\" />\n" +
                "            <option displayName=\"New Era\" value=\"3368976\" />\n" +
                "            <option displayName=\"Diamond\" value=\"35808\" />\n" +
                "            <option displayName=\"Oakley/奥克利\" value=\"3804630\" />\n" +
                "            <option displayName=\"staple\" value=\"8239828\" />\n" +
                "            <option displayName=\"Fila/斐乐\" value=\"3224828\" />\n" +
                "            <option displayName=\"MITCHELL AND NESS\" value=\"41677515\" />\n" +
                "            <option displayName=\"Stussy\" value=\"30218\" />\n" +
                "            <option displayName=\"ivi\" value=\"7727214\" />\n" +
                "            <option displayName=\"10DEEP\" value=\"7527566\" />\n" +
                "            <option displayName=\"DWINDLE\" value=\"365926897\" />\n" +
                "            <option displayName=\"primitive\" value=\"14165293\" />\n" +
                "            <option displayName=\"BLITZ\" value=\"3568426\" />\n" +
                "            <option displayName=\"bucketfeet\" value=\"113629963\" />\n" +
                "            <option displayName=\"VOLLEY\" value=\"32376705\" />\n" +
                "            <option displayName=\"ATHLETIC RECON\" value=\"322914939\" />\n" +
                "            <option displayName=\"akomplice\" value=\"70182252\" />\n" +
                "            <option displayName=\"FLEX WATCHES\" value=\"365976819\" />\n" +
                "            <option displayName=\"9five\" value=\"68905260\" />\n" +
                "            <option displayName=\"Ambig\" value=\"131992585\" />\n" +
                "            <option displayName=\"Super/超级\" value=\"3283425\" />\n" +
                "            <option displayName=\"visual\" value=\"5524480\" />\n" +
                "            <option displayName=\"PRODIGY\" value=\"140201810\" />\n" +
                "            <option displayName=\"RASTACLAT\" value=\"105578634\" />\n" +
                "            <option displayName=\"DNA\" value=\"3511348\" />\n" +
                "            <option displayName=\"UNDEFEATED\" value=\"3481604\" />\n" +
                "            <option displayName=\"KOMONO\" value=\"9464671\" />\n" +
                "            <option displayName=\"Girl\" value=\"3496180\" />\n" +
                "            <option displayName=\"CHUCKS\" value=\"149011560\" />\n" +
                "            <option displayName=\"MARSHALL\" value=\"3345347\" />\n" +
                "            <option displayName=\"PROOF EYEWEAR\" value=\"365936857\" />\n" +
                "            <option displayName=\"rook\" value=\"19884744\" />\n" +
                "            <option displayName=\"Rustic Dime\" value=\"287678466\" />\n" +
                "            <option displayName=\"X LARGE\" value=\"9998798\" />\n" +
                "            <option displayName=\"House of Marley\" value=\"174614316\" />\n" +
                "            <option displayName=\"SUPERDRY\" value=\"4368806\" />\n" +
                "            <option displayName=\"IMKING\" value=\"113236099\" />\n" +
                "            <option displayName=\"SUNSET SKATEBOARD\" value=\"365870975\" />\n" +
                "            <option displayName=\"FIVE FOUR\" value=\"10467730\" />\n" +
                "            <option displayName=\"LAKAI\" value=\"3286615\" />\n" +
                "            <option displayName=\"Benny Gold\" value=\"76200409\" />\n" +
                "            <option displayName=\"9FIGURES\" value=\"365890977\" />\n" +
                "            <option displayName=\"jason mark\" value=\"313944520\" />\n" +
                "            <option displayName=\"PATRICK EWING\" value=\"88553680\" />\n" +
                "            <option displayName=\"NV EURO\" value=\"333692458\" />\n" +
                "            <option displayName=\"ESOTERIC\" value=\"7906605\" />\n" +
                "            <option displayName=\"THE NORTH FACE/北面\" value=\"9865041\" />\n" +
                "            <option displayName=\"Pointer\" value=\"9420643\" />\n" +
                "            <option displayName=\"Original Penguin\" value=\"3300580\" />\n" +
                "            <option displayName=\"publish\" value=\"26865067\" />\n" +
                "            <option displayName=\"Dr．Martens\" value=\"27668612\" />\n" +
                "            <option displayName=\"Pony\" value=\"31342\" />\n" +
                "            <option displayName=\"icons\" value=\"15348429\" />\n" +
                "            <option displayName=\"polo ralph lauren\" value=\"3222776\" />\n" +
                "            <option displayName=\"Dr.Martens\" value=\"3369267\" />\n" +
                "            <option displayName=\"Nike/耐克\" value=\"20578\" />\n" +
                "            <option displayName=\"nike Air Jordan/乔丹\" value=\"129717904\" />\n" +
                "            <option displayName=\"Diadora/迪亚多纳\" value=\"40823\" />\n" +
                "            <option displayName=\"ransom\" value=\"10111386\" />\n" +
                "            <option displayName=\"UNDER ARMOUR\" value=\"3721844\" />\n" +
                "        </options>\n" +
                "    </field>\n" +
                "    <field id=\"prop_13021751\" name=\"货号\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"requiredRule\" value=\"true\" />\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "    <field id=\"product_images\" name=\"产品图片\" type=\"complex\">\n" +
                "        <fields>\n" +
                "            <field id=\"product_image_0\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                    <rule name=\"requiredRule\" value=\"true\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_1\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_2\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_3\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "            <field id=\"product_image_4\" name=\"产品图片\" type=\"input\">\n" +
                "                <rules>\n" +
                "                    <rule name=\"valueTypeRule\" value=\"url\" />\n" +
                "                </rules>\n" +
                "            </field>\n" +
                "        </fields>\n" +
                "    </field>\n" +
                "    <field id=\"market_price\" name=\"市场价格\" type=\"input\">\n" +
                "        <rules>\n" +
                "            <rule name=\"valueTypeRule\" value=\"decimal\" />\n" +
                "            <rule name=\"minValueRule\" value=\"0.00\" exProperty=\"not include\" />\n" +
                "            <rule name=\"maxValueRule\" value=\"100000000.00\" exProperty=\"not include\" />\n" +
                "        </rules>\n" +
                "    </field>\n" +
                "</itemRule>";



        @Test
        public void testReadXmlForList2() throws Exception {
                List<Field> result1 = SchemaReader.readXmlForList(docStr2);
                String resultStr1 = JsonUtil.getJsonString(result1);
                System.out.println(resultStr1);

                List<com.taobao.top.schema.field.Field> result2 = com.taobao.top.schema.factory.SchemaReader.readXmlForList(docStr2);
                String resultStr2 = JsonUtil.getJsonString(result2);
                System.out.println(resultStr2);
                if (resultStr1.equals(resultStr2)) {
                        System.out.println("equal");
                }
        }

        @Test
        public void testReadWriteXmlForList2() throws Exception {
                List<Field> result1 = SchemaReader.readXmlForList(docStr2);
                String docStr1After = SchemaWriter.writeParamXmlString(result1);

                List<com.taobao.top.schema.field.Field> result2 = com.taobao.top.schema.factory.SchemaReader.readXmlForList(docStr2);
                String docStr2After = com.taobao.top.schema.factory.SchemaWriter.writeParamXmlString(result2);
                System.out.println(docStr1After);
                System.out.println(docStr2After);
                if (docStr1After.equals(docStr2After)) {
                        System.out.println("equal");
                }
        }

        @Test
        public void testReadJsonForList() throws Exception {
                List<Field> result1 = SchemaReader.readXmlForList(docStr2);
                for (Field field:result1) {
                        if (field instanceof InputField) {
                                ((InputField)field).setDefaultValue("fsfsdfddfsdf");
                        }
                }
                String resultStr1 = JsonUtil.bean2Json(result1);
                List<Map<String, Object>> rootList = JsonUtil.jsonToMapList(resultStr1);
                System.out.println(resultStr1);
                //SchemaReader.readJsonForList(resultStr1);
        }


//        public static List<Map<String, Object>> json2MapList(String jsonStr)  {
//                ObjectMapper mapper = new ObjectMapper();
//                List<Map<String, Object>> list = mapper.readValue(jsonStr,
//                        TypeFactory.defaultInstance().constructCollectionType(List.class, Map.class));
//                return list;
//        }


}
