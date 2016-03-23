package com.voyageone.task2.cms.service.feed;

import com.thoughtworks.xstream.XStream;
import com.voyageone.task2.cms.bean.SuperFeedBcbgBean;

import java.io.File;
import java.util.List;

/**
 * 完整映射 Bcbg feed xml 文件的类定义,用于读取 Bcbg 的 feed xml 文件
 * Created by Jonas on 10/15/15.
 */
class BcbgFeedFile {

    private List<SuperFeedBcbgBean> MATERIALS;

    List<SuperFeedBcbgBean> getMATERIALS() {
        return MATERIALS;
    }

    public void setMATERIALS(List<SuperFeedBcbgBean> MATERIALS) {
        // set 将由 XStream 反序列化时自动调用
        this.MATERIALS = MATERIALS;
    }

    static BcbgFeedFile read(File file) {

        XStream xStream = new XStream();

        // 设定映射关系
        xStream.alias("ROOT", BcbgFeedFile.class);
        xStream.alias("item", SuperFeedBcbgBean.class);

        // 解析文件
        return (BcbgFeedFile) xStream.fromXML(file);
    }
}
