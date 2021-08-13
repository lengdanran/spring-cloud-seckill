package com.danran.common.domain;



import java.io.Serializable;

/**
 * order
 * @author lengdanran
 */
public class Order implements Serializable {
    private String id;

    private Integer userId;

    private String bookId;

    private Integer amount;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public Order(){}

    public Order(String id, Integer userId, String bookId, Integer amount, Integer status) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.amount = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", bookId='" + bookId + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}