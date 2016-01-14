package com.voyageone.ims.model;

import com.voyageone.ims.service.impl.BeatFlg;

/**
 * 任务执行信息
 *
 * Created by Jonas on 8/18/15.
 */
public class ImsBeatInfo {
    private BeatFlg beatFlg;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BeatFlg getBeatFlg() {
        return beatFlg;
    }

    public void setBeatFlg(int beatFlg) {
        this.beatFlg = BeatFlg.valueOf(beatFlg);
    }

    public String getCnName() {
        return getBeatFlg().getCnName();
    }

    public int getFlgValue() {
        return getBeatFlg().value();
    }
}
