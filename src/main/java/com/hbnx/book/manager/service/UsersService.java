package com.hbnx.book.manager.service;

import com.github.pagehelper.PageInfo;
import com.hbnx.book.manager.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbnx.book.manager.util.ro.PageIn;

/**
* @author nx
* @description 针对表【users】的数据库操作Service
* @createDate 2023-09-26 16:11:51
*/
public interface UsersService extends IService<Users> {
    Users addUser(Users users);
    boolean updateUser(Users users);
    Users findUserById(Integer id);
    void deleteUser(Integer id);
    PageInfo<Users> getUserList(PageIn pageIn);
    Users findByUsername(String username);
}
