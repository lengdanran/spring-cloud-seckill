package com.danran.order.mapper;


import com.danran.common.domain.Book;
import com.danran.common.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    @Select("select * from `order` where user_id=#{userId} and book_id=#{bookId}")
    Order getOrderByUserIDAndBookID(@Param("userId") int userID, @Param("bookId") String bookID);
}