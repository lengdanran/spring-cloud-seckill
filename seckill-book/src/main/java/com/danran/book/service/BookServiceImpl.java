package com.danran.book.service;

import com.danran.book.mapper.BookMapper;
import com.danran.common.api.book.BookServiceApi;
import com.danran.common.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname BookServiceImpl
 * @Description TODO
 * @Date 2021/8/5 13:59
 * @Created by ASUS
 *
 * 书籍服务接口的实现
 */
@Service
public class BookServiceImpl implements BookServiceApi {

    @Autowired
    private BookMapper bookMapper;

    /***
     * 获取书籍列表
     * @return 书籍的列表
     */
    @Override
    public List<Book> listBook() {
        return bookMapper.getAllBook();
    }

    /***
     * 根据id查询书籍信息
     * @param bookId the id of the book
     * @return book object
     */
    @Override
    public Book getBookById(String bookId) {
        return bookMapper.selectByPrimaryKey(bookId);
    }



    @Override
    public int reduceBookStock(String bookId) {
        Book book = bookMapper.selectByPrimaryKey(bookId);
        if (book.getStock() == 0) return -1;
        book.setStock(book.getStock() - 1);
        bookMapper.updateByPrimaryKeySelective(book);
        return book.getStock();
    }

    /***
     * 扣减书籍的库存
     *
     * !!!!!也许需要加锁同步操作
     *
     * @param bookId the id of the book
     * @return boolean
     */
    @Override
    public boolean reduceBookStockWithVersion(String bookId, int version) {
        Book book = bookMapper.selectByPrimaryKey(bookId);
        if (book.getStock() == 0) return false;// 库存为0
        if (version != book.getVersion()) return false;// 版本不一致
        return bookMapper.reduceBook(bookId) == 1;
    }
}
