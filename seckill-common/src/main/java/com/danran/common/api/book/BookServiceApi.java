package com.danran.common.api.book;

import com.danran.common.domain.Book;

import java.util.List;

/**
 * @Classname BookServiceApi
 * @Description TODO
 * @Date 2021/8/5 13:54
 * @Created by ASUS
 * 书籍服务接口
 */
public interface BookServiceApi {
    /***
     * 获取书籍列表
     * @return 书籍的列表
     */
    List<Book> listBook();

    /***
     * 根据id查询书籍信息
     * @param bookId the id of the book
     * @return book object
     */
    Book getBookById(String bookId);

    /***
     * 扣减书籍的库存
     * @param bookId the id of the book
     * @return boolean
     */
    boolean reduceBookStockWithVersion(String bookId, int version);

    int reduceBookStock(String bookId);
}
