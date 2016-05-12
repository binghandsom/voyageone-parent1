package com.voyageone.common.util;

import com.taobao.top.schema.exception.TopSchemaException;
import org.junit.Test;

import java.util.List;

public class SchemaConvertUtilTest {

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
    public void testMasterConvertToTaobo() {
        List<com.voyageone.common.masterdate.schema.field.Field> fieldList = com.voyageone.common.masterdate.schema.factory.SchemaReader.readXmlForList(docStr1);

        System.out.println(SchemaConvertUtil.masterConvertToTaobo(fieldList));
    }

    @Test
    public void testTaoboConvertToMaster() throws TopSchemaException {
        List<com.taobao.top.schema.field.Field> fieldList = com.taobao.top.schema.factory.SchemaReader.readXmlForList(docStr1);

        System.out.println(SchemaConvertUtil.taoboConvertToMaster(fieldList));
    }
}
