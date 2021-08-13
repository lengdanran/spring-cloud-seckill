package com.danran.common.api.mq;

import com.danran.common.domain.Book;
import com.danran.common.domain.User;

import java.io.Serializable;

/**
 * @Classname SkMessage
 * @Description TODO
 * @Date 2021/8/8 14:06
 * @Created by ASUS
 *
 * MQ中传递的秒杀的用户ID和书籍ID
 */
public class SkMessage implements Serializable {
    private User user;
    private Book book;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "SkMessage{" +
                "user=" + user +
                ", book=" + book +
                '}';
    }
}
