package com.voyageone.batch.ims.modelbean;

/**
 * Created by Leo on 15-6-2.
 */
public class PropValueBean {
   private String uuid;
   private String channel_id;
   private int level;
   private String level_value;
   private int prop_id;
   private String prop_value;
   private String parent;

   public int getLevel() {
      return level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public String getLevel_value() {
      return level_value;
   }

   public void setLevel_value(String level_value) {
      this.level_value = level_value;
   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getParent() {
      return parent;
   }

   public void setParent(String parent) {
      this.parent = parent;
   }

   public String getChannel_id() {
      return channel_id;
   }

   public void setChannel_id(String channel_id) {
      this.channel_id = channel_id;
   }

   public int getProp_id() {
      return prop_id;
   }

   public void setProp_id(int prop_id) {
      this.prop_id = prop_id;
   }

   public String getProp_value() {
      return prop_value;
   }

   public void setProp_value(String prop_value) {
      this.prop_value = prop_value;
   }
}
