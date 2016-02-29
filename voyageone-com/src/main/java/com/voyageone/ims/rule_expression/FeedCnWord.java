package com.voyageone.ims.rule_expression;

import java.util.Map;

/**
 * Created by Leo on 15-8-26.
 */
public class FeedCnWord extends RuleWord{
    private String value;
    private Map<String, String> extra;
    private boolean isTitle = false; // 当value为空的时候, 这个字段起作用
    private int feedIndex; // 当value为空的时候, 这个字段起作用

    public FeedCnWord() {
        setWordType(WordType.FEED_CN);
    }

    public FeedCnWord(String value) {
        this();
        this.value = value;
    }

    public FeedCnWord(boolean isTitle, int feedIndex) {
        this();
        this.value = null;

        this.setTitle(isTitle);
        this.feedIndex = feedIndex;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public int getFeedIndex() {
        return feedIndex;
    }

    public void setFeedIndex(int feedIndex) {
        this.feedIndex = feedIndex;
    }
}
