package com.yjiang.base.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yjiang
 * @since 2021-11-20
 */
@TableName("health_score")
public class HealthScore extends Model<HealthScore> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("health_user_id")
    private Integer healthUserId;
    @TableField("timeCount")
    private Integer timeCount;
    private Integer score;


    public HealthScore(Integer healthUserId, Integer timeCount, Integer score) {
        this.healthUserId = healthUserId;
        this.timeCount = timeCount;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHealthUserId() {
        return healthUserId;
    }

    public void setHealthUserId(Integer healthUserId) {
        this.healthUserId = healthUserId;
    }

    public Integer getTime() {
        return timeCount;
    }

    public void setTime(Integer time) {
        this.timeCount = time;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "HealthScore{" +
                "id=" + id +
                ", healthUserId=" + healthUserId +
                ", time=" + timeCount +
                ", score=" + score +
                '}';
    }
}
