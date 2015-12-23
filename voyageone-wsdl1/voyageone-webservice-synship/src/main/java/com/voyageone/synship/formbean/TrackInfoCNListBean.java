package com.voyageone.synship.formbean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by dell on 2015/7/23.
 */
@XmlRootElement(name = "Result")
public class TrackInfoCNListBean {
    List<TrackInfoCNBean> row;

    public List<TrackInfoCNBean> getRow() {
        return row;
    }

    public void setRow(List<TrackInfoCNBean> row) {
        this.row = row;
    }
}
