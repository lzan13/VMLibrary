package com.vmloft.develop.library.simple.widget.details;

/**
 * Created by lzan13 on 2017/5/15.
 * 可折叠对象内容实体类，记录展示内容
 */
public class VMDetailsEntity {
    private String content;
    private boolean isFold;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFold() {
        return isFold;
    }

    public void setFold(boolean fold) {
        isFold = fold;
    }
}
