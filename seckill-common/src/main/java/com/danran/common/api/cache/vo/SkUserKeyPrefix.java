package com.danran.common.api.cache.vo;

import java.io.Serializable;

/**
 * 秒杀用户信息的key前缀
 */

public class SkUserKeyPrefix extends BaseKeyPrefix  implements Serializable {

    public static final int TOKEN_EXPIRE = 30*60;// 缓存有效时间为30min

    public SkUserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SkUserKeyPrefix token = new SkUserKeyPrefix(TOKEN_EXPIRE, "token");
    /**
     * 用户cookie
     */
    public static SkUserKeyPrefix TOKEN = new SkUserKeyPrefix(TOKEN_EXPIRE, "token");
    /**
     * 用于存储用户对象到redis的key前缀
     */
    public static SkUserKeyPrefix getSeckillUserById = new SkUserKeyPrefix(0, "id");
    public static SkUserKeyPrefix SK_USER_PHONE = new SkUserKeyPrefix(0, "id");

}
