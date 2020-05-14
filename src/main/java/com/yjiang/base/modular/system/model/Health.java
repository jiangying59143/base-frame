package com.yjiang.base.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiangying
 * @since 2020-05-13
 */
@TableName("health")
public class Health extends Model<Health> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String answers;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public List<String> getSelections(){
        if(StringUtils.isNotBlank(answers)){
            return Arrays.asList(answers.substring(2, answers.length()-2).split("\", \""));
        }
        return null;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Health{" +
        ", id=" + id +
        ", title=" + title +
        ", answers=" + answers +
        "}";
    }
}
