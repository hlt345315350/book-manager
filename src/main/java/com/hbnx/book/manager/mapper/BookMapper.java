package com.hbnx.book.manager.mapper;

import com.hbnx.book.manager.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
* @author nx
* @description 针对表【book】的数据库操作Mapper
* @createDate 2023-09-26 16:11:26
* @Entity com.hbnx.book.manager.entity.Book
*/
@Mapper
@Component
public interface BookMapper extends BaseMapper<Book> {
    /**
     * 模糊分页查询用户
     * @param keyword 关键字
     * @return
     */
    List<Book> findBookListByLike(String keyword);

    /**
     * 编辑用户
     * @param map
     * @return
     */
    int updateBook(Map<String, Object> map);
}




