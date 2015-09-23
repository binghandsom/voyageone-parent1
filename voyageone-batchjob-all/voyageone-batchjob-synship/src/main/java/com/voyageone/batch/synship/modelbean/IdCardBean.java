package com.voyageone.batch.synship.modelbean;

/**
 * 从 Synship CloudClient 迁移
 * 表 tt_idcard 的映射
 *
 * Created by Jonas on 9/22/15.
 */
public class IdCardBean {
    // ID
    private String id_no;

    // 手机号码
    private String phone;

    // 收件人
    private String receive_name;

    // 身份证
    private String id_card;

    // 图片路径
    private String image_path;

    // 批准
    private String approved;

    private String comment;

    // 注释
    private String comments;

    // 订单ID
    private String source_order_id;

    // 压缩
    private String compress_flg;

    // 来源类型
    private String source_type;

    private String force_approved_flg;

    private String audit_type;

    private String automatic_type;

    // 创建时间
    private String create_time;

    // 更新时间
    private String update_time;

    // 创建者
    private String create_person;

    // 更新者
    private String update_person;

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String Idno) {
        this.id_no = Idno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receiveName) {
        this.receive_name = receiveName;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String idCard) {
        this.id_card = idCard;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String imagePath) {
        this.image_path = imagePath;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String sourceOrderId) {
        source_order_id = sourceOrderId;
    }

    public String getCompress_flg() {
        return compress_flg;
    }

    public void setCompress_flg(String compressFlg) {
        this.compress_flg = compressFlg;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String sourceType) {
        source_type = sourceType;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String createTime) {
        this.create_time = createTime;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String updateTime) {
        this.update_time = updateTime;
    }

    public String getCreate_person() {
        return create_person;
    }

    public void setCreate_person(String createPerson) {
        this.create_person = createPerson;
    }

    public String getUpdate_person() {
        return update_person;
    }

    public void setUpdate_person(String updatePerson) {
        this.update_person = updatePerson;
    }

    public String getComment() {
        return comment;
    }

    public String getForce_approved_flg() {
        return force_approved_flg;
    }

    public String getAudit_type() {
        return audit_type;
    }

    public String getAutomatic_type() {
        return automatic_type;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setForce_approved_flg(String forceApprovedFlg) {
        force_approved_flg = forceApprovedFlg;
    }

    public void setAudit_type(String auditType) {
        audit_type = auditType;
    }

    public void setAutomatic_type(String automaticType) {
        automatic_type = automaticType;
    }
}
