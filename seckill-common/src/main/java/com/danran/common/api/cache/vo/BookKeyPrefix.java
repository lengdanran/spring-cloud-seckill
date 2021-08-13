package com.danran.common.api.cache.vo;


import java.io.Serializable;

/**
 * redis中，用于商品信息的key
 *
 * @author noodle
 */
public class BookKeyPrefix extends BaseKeyPrefix  implements Serializable {
    public BookKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 缓存在redis中的商品列表页面的key的前缀
    public static BookKeyPrefix bookListKeyPrefix = new BookKeyPrefix(60, "bookList");
    public static BookKeyPrefix BOOK_LIST_HTML = new BookKeyPrefix(60, "bookListHtml");

    // 缓存在redis中的商品详情页面的key的前缀
    public static BookKeyPrefix bookDetailKeyPrefix = new BookKeyPrefix(60, "bookDetail");

    // 缓存在redis中的商品库存的前缀(缓存过期时间为永久)
    public static BookKeyPrefix seckillBookStockPrefix = new BookKeyPrefix(0, "bookStock");
    /**
     * 缓存在redis中的商品库存的前缀(缓存过期时间为永久)
     */
    public static BookKeyPrefix BOOK_STOCK = new BookKeyPrefix(0, "bookStock");
}
