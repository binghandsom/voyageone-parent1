package com.voyageone.synship.formbean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by dell on 2015/7/23.
 */
@XmlRootElement(name = "Result")
public class TrackInfoJuMeiListBean {
    List<TrackInfoJuMeiBean> row;

    public List<TrackInfoJuMeiBean> getRow() {
        return row;
    }

    public void setRow(List<TrackInfoJuMeiBean> row) {
        this.row = row;
    }
}
