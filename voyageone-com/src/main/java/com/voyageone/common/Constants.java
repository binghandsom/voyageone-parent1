package com.voyageone.common;

import com.voyageone.common.components.issueLog.enums.SubSystem;

import java.util.Random;

public final class Constants {
    public static final String SCOPE_PROTOTYPE = "prototype";

    public static final String DAO_NAME_SPACE_CORE = "com.voyageone.core.sql.";
    public static final String DAO_NAME_SPACE_OMS = "com.voyageone.oms.sql.";
    public static final String DAO_NAME_SPACE_WMS = "com.voyageone.wms.sql.";
    public static final String DAO_NAME_SPACE_IMS = "com.voyageone.ims.sql.";
    public static final String DAO_NAME_SPACE_COMMON = "com.voyageone.common.sql.";
    public static final String DAO_NAME_SPACE_CMS = "com.voyageone.cms.sql.";
    public static final String DAO_NAME_SPACE_SYNSHIP = "com.voyageone.batch.synship.sql.";
    public static final String DAO_READ = "read";
    public static final String DAO_WRITE = "write";
    // exception消息分隔符
    public static final String EXCEPTION_MESSAGE_PREFIX = "; cause is ";
    // exception消息描述最大值
    public static final int EXCEPTION_MESSAGE_LENGTH = 200;
    // 随机数
    public static final Random RANDOM = new Random();
    // controll的后缀
    public static final String CONTROLLER_SUFFIX = ".html";
    // Ajax 请求返回值
    public static final String AJAX_RESULT_OK = "OK";
    public static final String AJAX_RESULT_NG = "NG";
    // 公共公告类型
    public static final int ANNOUNCEMENT_PUBLIC = 0;
    // 密码加密固定盐值
    public static final String MD5_FIX_SALT = "crypto.voyageone.la";
    // 密码加密散列加密次数
    public static final int MD5_HASHITERATIONS = 4;
    public static final String EmptyString = "";
    // 手机号码不符合规定
    public static final int PHONE_NUM_ERR = -3;

    public static String getDaoNameSpace(SubSystem subSystem) {

        String ns;

        switch (subSystem) {
            case COM:
                ns = Constants.DAO_NAME_SPACE_COMMON;
                break;
            case OMS:
                ns = Constants.DAO_NAME_SPACE_OMS;
                break;
            case WMS:
                ns = Constants.DAO_NAME_SPACE_WMS;
                break;
            case CMS:
                ns = Constants.DAO_NAME_SPACE_CMS;
                break;
            case CORE:
                ns = Constants.DAO_NAME_SPACE_CORE;
                break;
            case IMS:
                ns = Constants.DAO_NAME_SPACE_IMS;
                break;
            case SYNSHIP:
                ns = Constants.DAO_NAME_SPACE_SYNSHIP;
                break;
            default:
                return null;
        }

        if (!ns.endsWith(".")) return ns;

        return ns.substring(0, ns.length() - 1);
    }

    /**
     * 邮件类专用字符串存储
     */
    public final static class MAIL {
        public static final String EMAIL_RECEIVER = "EMAIL_RECEIVER";
        public static final String EMAIL_CONFIG = "VOYAGEONE_EMAIL_CONFIG";
        public static final String SMTP_HOST = "SMTP_HOST";
        public static final String SENDER_PWD = "SENDER_PWD";
        public static final String SENDER_ACCOUNT = "SENDER_ACCOUNT";
        public final static String SUBJECT_PREFIX = "[VoyageOne]";
        public final static String SUBJECT_REQUIRED = "[ActionRequired]";
    }

    /**
     * 语言类
     */
    public final static class LANGUAGE {
        public static final String EN = "en";
        public static final String CN = "cn";
        public static final String JP = "jp";
        public static final String[] ALL = {"en", "cn", "jp"};
    }

    /**
     * 语言类
     */
    public final static class PATH {
        //产品规则属性文件路径
        public final static String PATH_PRODUCT_RULE_FILE = "ims.path.product.rule.file";
        //商品规则属性文件路径
        public final static String PATH_ITEM_RULE_FILE = "ims.path.item.rule.file";
    }

    //第三方库存更新方式 FULL: 全量； INCREACE: 增量
    public final static class updateClientInventoryType {
        public final static String FULL = "FULL";
        public final static String INCREACE = "INCREACE";
    }

    //SMS情报
    public final static class smsInfo {
        public static final String SMS_INFO = "SMS_INFO";
        public static final String WORDS = "WORDS";
        public static final String COST = "COST";
        public static final String URL = "URL";
        public static final String NEED_ADD_SERIAL = "1";
        public static final String NO_NEED_ADD_SERIAL = "0";
        public static final String SMS_TYPE_MARKETING = "1";
        public static final String SMS_TYPE_LOGISTICS = "0";
    }

    public static final class comMtTypeChannel {
        public static final String COUNTRY_10 = "country";
        public static final String BRAND_41 = "brand";
        public static final String HS_CODE_CROP_42 = "hsCodeCrop";
        public static final String HS_CODE_CROP_43 = "hsCodePrivate";
        public static final String SKU_CARTS_53 = "skuCarts";
        public static final String SKU_CARTS_53_D = "D";
        public static final String SKU_CARTS_53_A = "A";
        public static final String SKU_CARTS_53_O = "O";
        public static final String PROUDCT_TYPE_57 = "productType";
        public static final String PROUDCT_TYPE_58 = "sizeType";
        public static final String SORT_ATTRIBUTES_61 = "sortAttributes";
        public static final String DYNAMIC_PROPERTY = "dynamicProperty";
        public static final String IMAGE_TEMPLATE_TYPE = "imageTemplateType";
        public static final String material_TYPE_103 = "materialEn";
        public static final String origin_TYPE_104 = "origin";
        public static final String colorMap_TYPE_105 = "colorMap";
    }

    public static final class comMtTypeValue {

        public static final String IMAGE_TYPE = "imageType";

    }

    public static final class productForOtherSystemInfo {
        public static final String IMG_URL = "http://image.voyageone.com.cn/is/image/sneakerhead/";

        public static final String IMG_URL_WITH_PARAMENTER = "http://image.voyageone.com.cn/is/image/sneakerhead/%s-%s";

        public static final String TMALL_NUM_IID = "http://detail.tmall.com/item.htm?id=";

        public static final String JINDONG_NUM_IID = "https://item.jd.com/%s.html";

        public static final String JUMEI_NUM_IID = "http://item.jumeiglobal.com/%s.html";

        public static final String HS_CODE_CROP = "hsCodeCrop";

        public static final String HS_CODE_PRIVATE = "hsCodePrivate";

    }


    //发短信时替换亿美KEY
    public final static class smsChange {
        public static final String CHANGE_ON = "1";
        public static final String CHANGE_OFF = "0";
    }

}
