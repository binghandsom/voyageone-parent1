package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.configs.Codes;

public class ImageConfig {
    //阿里OSS
    public static String getAliYunEndpoint() {
        return getValue("AliYun_OSS_Confige", "endpoint");
    }

    public static String getAliYunAccessKeyId() {
        return getValue("AliYun_OSS_Confige", "accessKeyId");
    }

    public static String getAliYunAccessKeySecret() {
        return getValue("AliYun_OSS_Confige", "accessKeySecret");
    }

    public static String getAliYunBucketName() {
        return getValue("AliYun_OSS_Confige", "bucketName");
    }

    public static String getValue(String id, String code) {
        return Codes.getCodeName(id, code);
    }

    public static String getLiquidFireUrl() {
        return getValue("LiquidFire_Confige", "url");
    }

    public static String getLiquidFireImageSavePath() {
        return getValue("LiquidFire_Confige", "imageSavePath");
    }

    public static String getImageProxyIP() {
        return getValue("LiquidFire_Confige", "imageProxyIP");
    }

    public static String getImageProxyPort() {
        return getValue("LiquidFire_Confige", "imageProxyPort");
    }


    public static String getUSCDNUrl() {
        return getValue("US_CDN_Confige", "url");
    }

    public static String getUSCDNUserName() {
        return getValue("US_CDN_Confige", "userName");
    }

    public static String getUSCDNPassword() {
        return getValue("US_CDN_Confige", "password");
    }

    public static String getUSCDNWorkingDirectory() {
        return getValue("US_CDN_Confige", "workingDirectory");
    }


//    -- USCDN配置
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('US_CDN_Confige','url','ftp.vny.FB70.omicroncdn.net','','',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('US_CDN_Confige','userName','admin@voyageone.com','','',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('US_CDN_Confige','password','Voyage1#','','',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('US_CDN_Confige','workingDirectory','/sneakerhead/images','','',NOW(),'system','system',1);

//    -- 阿里云OSS配置
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('AliYun_OSS_Confige','endpoint','http://oss-cn-hangzhou.aliyuncs.com','','阿里云oss endpoint',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('AliYun_OSS_Confige','accessKeyId','v5l02zcFVl6rBKXg','','阿里云oss accessKeyId',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('AliYun_OSS_Confige','accessKeySecret','c6VxvbVsDLZI4vAePHn6PxsEAuQGuq','','阿里云oss accessKeySecret',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('AliYun_OSS_Confige','bucketName','shenzhen-vo','','阿里云bucketName',NOW(),'system','system',1);
//
//    -- LiquidFire 生成图片配置
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('LiquidFire_Confige','url','http://voyageone.ma.liquifire.com/voyageone','','LiquidFire 生成图片地址',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('LiquidFire_Confige','imageSavePath','/usr/createImages','','LiquidFire 生成图片缓存路径',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('LiquidFire_Confige','imageProxyIP','47.89.48.208','','LiquidFire 生成图片proxyIP',NOW(),'system','system',1);
//    INSERT `tm_code`(`id`,`code`,`name`,`name1`,`des`,`created`,`creater`,`modifier`,`status`)
//    VALUE('LiquidFire_Confige','imageProxyPort','7656','','LiquidFire 生成图片ProxyPort',NOW(),'system','system',1);

//    `cms_mt_image_create_file`
//            `cms_mt_image_create_template`
}
