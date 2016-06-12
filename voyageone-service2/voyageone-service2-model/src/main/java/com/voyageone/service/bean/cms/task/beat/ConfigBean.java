package com.voyageone.service.bean.cms.task.beat;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by jonasvlag on 16/3/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class ConfigBean {

    private Boolean need_vimage;

    private String beat_template;

    private String revert_template;

    private String beat_vtemplate;

    private String revert_vtemplate;

    public Boolean isNeed_vimage() {
        return need_vimage;
    }

    public void setNeed_vimage(Boolean need_vimage) {
        this.need_vimage = need_vimage;
    }

    public String getBeat_template() {
        return beat_template;
    }

    public void setBeat_template(String beat_template) {
        this.beat_template = beat_template;
    }

    public String getRevert_template() {
        return revert_template;
    }

    public void setRevert_template(String revert_template) {
        this.revert_template = revert_template;
    }

    public String getBeat_vtemplate() {
        return beat_vtemplate;
    }

    public void setBeat_vtemplate(String beat_vtemplate) {
        this.beat_vtemplate = beat_vtemplate;
    }

    public String getRevert_vtemplate() {
        return revert_vtemplate;
    }

    public void setRevert_vtemplate(String revert_vtemplate) {
        this.revert_vtemplate = revert_vtemplate;
    }

    @JsonIgnore
    public boolean isValid() {

        // 检查必须的三个值, 是否都存在
        for (Object value : new Object[]{
                isNeed_vimage(), getBeat_template(), getRevert_template()
        }) {
            if (value == null)
                return false;
        }

        // 如果需要竖图, 继续检查竖图的两个值是否存在
        if (isNeed_vimage()) {
            for (Object value : new Object[]{
                    getBeat_vtemplate(), getRevert_vtemplate()
            }) {
                if (value == null)
                    return false;
            }
        }

        return true;
    }
}
