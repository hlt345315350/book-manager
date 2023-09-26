package com.hbnx.book.manager.mapper;

import com.hbnx.book.manager.entity.Borrow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author nx
* @description 针对表【borrow】的数据库操作Mapper
* @createDate 2023-09-26 16:11:47
* @Entity com.hbnx.book.manager.entity.Borrow
*/
@Mapper
@Component
public interface BorrowMapper extends BaseMapper<Borrow> {

}




