package com.hbnx.book.manager.service;

import com.hbnx.book.manager.entity.Borrow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author nx
* @description 针对表【borrow】的数据库操作Service
* @createDate 2023-09-26 16:11:47
*/
public interface BorrowService extends IService<Borrow> {
    Integer addBorrow(Borrow borrow);
    List<Borrow> findAllBorrowByUserId(Integer userId);
    List<Borrow> findBorrowsByUserIdAndRet(Integer userId, Integer ret);
    Borrow findById(Integer id);
    boolean updateBorrow(Borrow borrow);
    Borrow updateBorrowByRepo(Borrow borrow);
    void deleteBorrow(Integer id);
    Borrow findBorrowByUserIdAndBookId(int userId,int bookId);
    void retBook(int userId,int bookId);
}
