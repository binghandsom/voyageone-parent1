package com.voyageone.task2.cms.model;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
public class CmsBtFeedInfoSummerGuruModel extends CmsBtFeedInfoModel {

  private String material;
  private String bodyMeasurements;
  private String relationshipName;
  private String condition;
  private String conditionNotes;

  public String getMaterial() {
    return material;
  }

  public void setMaterial(String material) {
    this.material = material;
  }

  public String getBodyMeasurements() {
    return bodyMeasurements;
  }

  public void setBodyMeasurements(String bodyMeasurements) {
    this.bodyMeasurements = bodyMeasurements;
  }

  public String getRelationshipName() {
    return relationshipName;
  }

  public void setRelationshipName(String relationshipName) {
    this.relationshipName = relationshipName;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public String getConditionNotes() {
    return conditionNotes;
  }

  public void setConditionNotes(String conditionNotes) {
    this.conditionNotes = conditionNotes;
  }
}
