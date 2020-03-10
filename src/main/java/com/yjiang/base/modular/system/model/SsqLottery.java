package com.yjiang.base.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiangying
 * @since 2020-03-08
 */
@TableName("ssq_lottery")
public class SsqLottery extends Model<SsqLottery> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("create_by")
    private Long createBy;
    @TableField("create_date")
    private Date createDate;
    @TableField("update_by")
    private Long updateBy;
    @TableField("update_date")
    private Date updateDate;
    private Integer version;
    private String number;
    @TableField("open_date")
    private Date openDate;
    @TableField("red_ball_1")
    private Integer redBall1;
    @TableField("red_ball_2")
    private Integer redBall2;
    @TableField("red_ball_3")
    private Integer redBall3;
    @TableField("red_ball_4")
    private Integer redBall4;
    @TableField("red_ball_5")
    private Integer redBall5;
    @TableField("red_ball_6")
    private Integer redBall6;
    @TableField("blue_ball_1")
    private Integer blueBall1;
    @TableField("totalSale")
    private BigDecimal totalSale;
    @TableField("prizePond")
    private BigDecimal prizePond;
    @TableField("firstCount")
    private Integer firstCount;
    @TableField("secondCount")
    private Integer secondCount;
    @TableField("thirdCount")
    private Integer thirdCount;
    @TableField("firstAmountMoney")
    private BigDecimal firstAmountMoney;
    @TableField("secondAmountMoney")
    private BigDecimal secondAmountMoney;
    @TableField("thirdAmountMoney")
    private BigDecimal thirdAmountMoney;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Integer getRedBall1() {
        return redBall1;
    }

    public void setRedBall1(Integer redBall1) {
        this.redBall1 = redBall1;
    }

    public Integer getRedBall2() {
        return redBall2;
    }

    public void setRedBall2(Integer redBall2) {
        this.redBall2 = redBall2;
    }

    public Integer getRedBall3() {
        return redBall3;
    }

    public void setRedBall3(Integer redBall3) {
        this.redBall3 = redBall3;
    }

    public Integer getRedBall4() {
        return redBall4;
    }

    public void setRedBall4(Integer redBall4) {
        this.redBall4 = redBall4;
    }

    public Integer getRedBall5() {
        return redBall5;
    }

    public void setRedBall5(Integer redBall5) {
        this.redBall5 = redBall5;
    }

    public Integer getRedBall6() {
        return redBall6;
    }

    public void setRedBall6(Integer redBall6) {
        this.redBall6 = redBall6;
    }

    public Integer getBlueBall1() {
        return blueBall1;
    }

    public void setBlueBall1(Integer blueBall1) {
        this.blueBall1 = blueBall1;
    }

    public BigDecimal getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(BigDecimal totalSale) {
        this.totalSale = totalSale;
    }

    public BigDecimal getPrizePond() {
        return prizePond;
    }

    public void setPrizePond(BigDecimal prizePond) {
        this.prizePond = prizePond;
    }

    public Integer getFirstCount() {
        return firstCount;
    }

    public void setFirstCount(Integer firstCount) {
        this.firstCount = firstCount;
    }

    public Integer getSecondCount() {
        return secondCount;
    }

    public void setSecondCount(Integer secondCount) {
        this.secondCount = secondCount;
    }

    public Integer getThirdCount() {
        return thirdCount;
    }

    public void setThirdCount(Integer thirdCount) {
        this.thirdCount = thirdCount;
    }

    public BigDecimal getFirstAmountMoney() {
        return firstAmountMoney;
    }

    public void setFirstAmountMoney(BigDecimal firstAmountMoney) {
        this.firstAmountMoney = firstAmountMoney;
    }

    public BigDecimal getSecondAmountMoney() {
        return secondAmountMoney;
    }

    public void setSecondAmountMoney(BigDecimal secondAmountMoney) {
        this.secondAmountMoney = secondAmountMoney;
    }

    public BigDecimal getThirdAmountMoney() {
        return thirdAmountMoney;
    }

    public void setThirdAmountMoney(BigDecimal thirdAmountMoney) {
        this.thirdAmountMoney = thirdAmountMoney;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SsqLottery{" +
        ", id=" + id +
        ", createBy=" + createBy +
        ", createDate=" + createDate +
        ", updateBy=" + updateBy +
        ", updateDate=" + updateDate +
        ", version=" + version +
        ", number=" + number +
        ", openDate=" + openDate +
        ", redBall1=" + redBall1 +
        ", redBall2=" + redBall2 +
        ", redBall3=" + redBall3 +
        ", redBall4=" + redBall4 +
        ", redBall5=" + redBall5 +
        ", redBall6=" + redBall6 +
        ", blueBall1=" + blueBall1 +
        ", totalSale=" + totalSale +
        ", prizePond=" + prizePond +
        ", firstCount=" + firstCount +
        ", secondCount=" + secondCount +
        ", thirdCount=" + thirdCount +
        ", firstAmountMoney=" + firstAmountMoney +
        ", secondAmountMoney=" + secondAmountMoney +
        ", thirdAmountMoney=" + thirdAmountMoney +
        "}";
    }
}
