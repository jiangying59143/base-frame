package com.yjiang.base.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 支付宝订单表
 * </p>
 *
 * @author jiangying
 * @since 2023-02-21
 */
@TableName("alipay_order")
public class AlipayOrder extends Model<AlipayOrder> {

    private static final long serialVersionUID = 1L;

    public AlipayOrder() {
    }

    public AlipayOrder(String orderNo, BigDecimal totalAmount, String subject, String body, int status, Date createTime) {
        this.orderNo = orderNo;
        this.totalAmount = totalAmount;
        this.subject = subject;
        this.body = body;
        this.status = status;
        this.createTime = createTime;
    }

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户订单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 订单金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;
    /**
     * 订单标题
     */
    private String subject;
    /**
     * 订单描述
     */
    private String body;
    /**
     * 订单状态
     */
    private int status;
    /**
     * 订单创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 订单最后更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AlipayOrder{" +
        ", id=" + id +
        ", orderNo=" + orderNo +
        ", totalAmount=" + totalAmount +
        ", subject=" + subject +
        ", body=" + body +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
