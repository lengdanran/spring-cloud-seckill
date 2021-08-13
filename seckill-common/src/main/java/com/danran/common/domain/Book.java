package com.danran.common.domain;



import java.io.Serializable;

/**
 * book
 */

public class Book implements Serializable {
    private String id;

    private String bookName;

    private Integer stock;

    private Integer version;

    private static final long serialVersionUID = 1L;


    public void reduceStack() {
        this.stock = this.stock - 1;
        this.version = this.version + 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", bookName='" + bookName + '\'' +
                ", stock=" + stock +
                ", version=" + version +
                '}';
    }
}