package com.danran.book.controller;

import com.alibaba.fastjson.JSON;
import com.danran.book.mapper.BookMapper;
import com.danran.common.api.seckill.SeckillServiceApi;
import com.danran.common.domain.Book;
import com.danran.common.domain.Order;
import com.danran.common.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname BookController
 * @Description TODO
 * @Date 2021/8/8 19:09
 * @Created by ASUS
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private SeckillServiceApi seckillServiceApi;

    @GetMapping("/get_stock")
    public String getStock(@RequestParam("book_id") String bookId) {
        log.info("[getStock:bookId=" + bookId + "]");
        Book book = bookMapper.selectByPrimaryKey(bookId);
        if (book == null) {
            log.info("书籍[" + bookId + "]在数据库中不存在,返回-1");
            return "-1";
        }
        return String.valueOf(book.getStock());
    }

    @PostMapping("/seckill")
    public Order seckill(@RequestParam("user") String user,
                           @RequestParam("book") String book) {
        return seckillServiceApi.seckill(JSON.parseObject(user, User.class).getId(),
                JSON.parseObject(book, Book.class).getId());
    }

    @GetMapping("/get_book_by_id")
    public Book getBookById(@RequestParam("book_id") String bookId) {
        return bookMapper.selectByPrimaryKey(bookId);
    }
}
