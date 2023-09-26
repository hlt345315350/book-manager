package com.hbnx.book.manager.mapper;

import com.hbnx.book.manager.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
* @author nx
* @description 针对表【users】的数据库操作Mapper
* @createDate 2023-09-26 16:11:51
* @Entity com.hbnx.book.manager.entity.Users
*/
@Mapper
@Component
public interface UsersMapper extends BaseMapper<Users> {
    /**
     * 模糊分页查询用户
     * @param keyword 关键字
     * @return
     */
    List<Users> findListByLike(String keyword);

    /**
     * 编辑用户
     * @param map
     * @return
     */
    int updateUsers(Map<String,Object> map);
}




