package com.yjiang.base.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author jiangying
 * @since 2020-05-30
 */
@TableName("health_users")
public class HealthUsers extends Model<HealthUsers> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String age;
    private String sex;
    private String education;
    private String job;
    @TableField("org_name")
    private String orgName;
    private Integer count;
    private Integer nutrition;
    @TableField(value="create_date", fill = FieldFill.INSERT)
    private Date createDate;
    @TableField(value="update_date", fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    public HealthUsers(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public HealthUsers(Integer id, String name, String age, String sex, String education, String job, String orgName, Integer count, Integer nutrition) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.education = education;
        this.job = job;
        this.orgName = orgName;
        this.count = count;
        this.nutrition = nutrition;
    }

    public HealthUsers(Integer id, String name, String age, String sex, String education, String job, String orgName, Integer count, Integer nutrition, java.sql.Timestamp createDate, java.sql.Timestamp updateDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.education = education;
        this.job = job;
        this.orgName = orgName;
        this.count = count;
        this.nutrition = nutrition;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getNutrition() {
        return nutrition;
    }

    public void setNutrition(Integer nutrition) {
        this.nutrition = nutrition;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "HealthUsers{" +
        ", id=" + id +
        ", name=" + name +
        ", age=" + age +
        ", sex=" + sex +
        ", education=" + education +
        ", job=" + job +
        ", orgName=" + orgName +
        ", count=" + count +
        ", nutrition=" + nutrition +
        "}";
    }
}
