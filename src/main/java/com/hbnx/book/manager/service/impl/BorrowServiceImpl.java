package com.hbnx.book.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbnx.book.manager.entity.Book;
import com.hbnx.book.manager.entity.Borrow;
import com.hbnx.book.manager.entity.Users;
import com.hbnx.book.manager.service.BookService;
import com.hbnx.book.manager.service.BorrowService;
import com.hbnx.book.manager.mapper.BorrowMapper;
import com.hbnx.book.manager.service.UsersService;
import com.hbnx.book.manager.util.consts.Constants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author nx
* @description 针对表【borrow】的数据库操作Service实现
* @createDate 2023-09-26 16:11:47
*/
@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService{
    @Resource
    private BorrowMapper borrowMapper;

    //    @Autowired
    @Resource
    private BookService bookService;

    //    @Autowired
    @Resource
    private UsersService userService;

    /**
     * 添加
     * （添加事物）
     */
    @Transactional
    public Integer addBorrow(Borrow borrow) {
        Book book = bookService.findBook(borrow.getBookId());
        Users users = userService.findUserById(borrow.getUserId());

        // 查询是否已经借阅过该图书
        Borrow bor = findBorrowByUserIdAndBookId(users.getId(),book.getId());
        if (bor!=null) {
            Integer ret = bor.getRet();
            if (ret!=null) {
                // 已借阅, 未归还 不可再借
                if (ret == Constants.NO) {
                    return Constants.BOOK_BORROWED;
                }
            }
        }

        // 库存数量减一
        int size = book.getSize();
        if (size>0) {
            size--;
            book.setSize(size);
            bookService.updateBook(book);
        }else {
            return Constants.BOOK_SIZE_NOT_ENOUGH;
        }

        // 用户可借数量减一
        int userSize = users.getSize();
        if (userSize>0) {
            userSize --;
            users.setSize(userSize);
            userService.updateUser(users);
        }else {
            return Constants.USER_SIZE_NOT_ENOUGH;
        }


        // 添加借阅信息, 借阅默认为未归还状态
        borrow.setRet(Constants.NO);
        borrowMapper.insert(borrow);
//        borrowRepository.saveAndFlush(borrow);

        // 一切正常
        return Constants.OK;
    }

    /**
     * user id查询所有借阅信息
     */
    public List<Borrow> findAllBorrowByUserId(Integer userId) {
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Borrow::getUserId,userId);
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);
        return borrows;
//        return borrowRepository.findBorrowByUserId(userId);
    }

    /**
     * user id查询所有 已借阅信息
     */
    public List<Borrow> findBorrowsByUserIdAndRet(Integer userId, Integer ret) {
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Borrow::getUserId,userId).eq(Borrow::getRet,ret);
        return borrowMapper.selectList(queryWrapper);
//        return borrowRepository.findBorrowsByUserIdAndRet(userId,ret);
    }


    /**
     * 详情
     */
    public Borrow findById(Integer id) {
        Borrow borrow = borrowMapper.selectById(id);
        if (null!=borrow){
            return borrow;
        }else {
            return null;
        }
        /*Optional<Borrow> optional = borrowRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;*/
    }

    /**
     * 编辑
     */
    public boolean updateBorrow(Borrow borrow) {
        LambdaUpdateWrapper<Borrow> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Borrow::getUserId,borrow.getUserId())
                .set(Borrow::getBookId,borrow.getBookId())
                .set(Borrow::getUpdateTime,borrow.getUpdateTime())
                .eq(Borrow::getId,borrow.getId());
        int res = borrowMapper.update(null, updateWrapper);
        return res > 0;
//        return borrowMapper.updateBorrow(borrow)>0;
    }


    /**
     * 编辑
     */
    public Borrow updateBorrowByRepo(Borrow borrow) {
        LambdaUpdateWrapper<Borrow> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Borrow::getUserId,borrow.getUserId())
                .set(Borrow::getBookId,borrow.getBookId())
                .set(Borrow::getUpdateTime,borrow.getUpdateTime())
                .eq(Borrow::getId,borrow.getId());
        int res = borrowMapper.update(null, updateWrapper);
//        int res = borrowMapper.updateBorrow(borrow);
        if (res > 0){
            return borrow;
        }else {
            return null;
        }
//        return borrowRepository.saveAndFlush(borrow);
    }

    /**
     * 删除
     */
    public void deleteBorrow(Integer id) {
        borrowMapper.deleteById(id);
//        borrowRepository.deleteById(id);
    }

    /**
     * 查询用户某一条借阅信息
     * @param userId 用户id
     * @param bookId 图书id
     */
    public Borrow findBorrowByUserIdAndBookId(int userId,int bookId) {
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Borrow::getUserId,userId).eq(Borrow::getBookId,bookId);
        Borrow borrow = borrowMapper.selectOne(queryWrapper);
        return borrow;
//        return borrowMapper.findBorrowByUserIdAndBookId(userId,bookId);
    }

    /**
     * 归还书籍, 使用事务保证 ACID
     * @param userId 用户Id
     * @param bookId 书籍id
     */
    @Transactional(rollbackFor = Exception.class)
    public void retBook(int userId,int bookId) {
        // 用户可借数量加1
        Users user = userService.findUserById(userId);
        Integer size = user.getSize();
        size++;
        user.setSize(size);
        userService.updateUser(user);


        // 书籍库存加1
        Book book = bookService.findBook(bookId);
        Integer bookSize = book.getSize();
        bookSize++;
        book.setSize(bookSize);
        bookService.updateBook(book);
        // 借阅记录改为已归还,删除记录
        Borrow borrow = this.findBorrowByUserIdAndBookId(userId, bookId);
//        borrow.setRet(Constants.YES);
//        borrow.setUpdateTime(new Date());
//        borrowMapper.updateBor(BeanUtil.beanToMap(borrow))>0;
        this.deleteBorrow(borrow.getId());
    }
}




