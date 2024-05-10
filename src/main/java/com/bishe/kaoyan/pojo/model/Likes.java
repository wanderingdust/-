package com.bishe.kaoyan.pojo.model;

import com.baomidou.mybatisplus.annotation.TableField;

public class Likes {

    @TableField(value = "liker_id")
    private Integer likerId;
    @TableField(value = "target_id")
    private Integer targetId;
    private Integer type;

    public Integer getLikerId() {
        return likerId;
    }

    public void setLikerId(Integer likerId) {
        this.likerId = likerId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
