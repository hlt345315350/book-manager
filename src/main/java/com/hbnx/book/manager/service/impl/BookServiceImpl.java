package com.hbnx.book.manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbnx.book.manager.entity.Book;
import com.hbnx.book.manager.service.BookService;
import com.hbnx.book.manager.mapper.BookMapper;
import com.hbnx.book.manager.util.ro.PageIn;
import com.hbnx.book.manager.util.vo.BookOut;
import com.hbnx.book.manager.util.vo.PageOut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author nx
* @description 针对表【book】的数据库操作Service实现
* @createDate 2023-09-26 16:11:26
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService{
    @Resource
    private BookMapper bookMapper;


    /**
     * 添加图书
     * @param book 图书
     * @return 返回添加的图书
     */
    public Book addBook(Book book) {
        int result = bookMapper.insert(book);
        if (result > 0){
            return book;
        }else {
            return null;
        }
//        return bookRepository.saveAndFlush(book);
    }

    /**
     * 编辑用户
     * @param book 图书对象
     * @return true or false
     */
    public boolean updateBook(Book book) {
        return bookMapper.updateBook(BeanUtil.beanToMap(book))>0;
    }

    /**
     * 图书详情
     * @param id 主键
     * @return 图书详情
     */
    public BookOut findBookById(Integer id) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getId,id);
        Book book = bookMapper.selectOne(queryWrapper);
        if (null != book){
            BookOut out = new BookOut();
            BeanUtil.copyProperties(book,out);
            out.setPublishTime(DateUtil.format(book.getPublishTime(),"yyyy-MM-dd"));
            return out;
        }
        /*Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            Book book = optional.get();
            BookOut out = new BookOut();
            BeanUtil.copyProperties(book,out);
            out.setPublishTime(DateUtil.format(book.getPublishTime(),"yyyy-MM-dd"));
            return out;
        }*/
        return null;
    }

    public Book findBook(Integer id) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getId,id);
        Book book = bookMapper.selectOne(queryWrapper);
        if (null != book){
            return book;
        }else {
            return null;
        }
        /*Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;*/
    }

    /**
     * ISBN查询
     * @param isbn
     * @return
     */
    public BookOut findBookByIsbn(String isbn) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getIsbn,isbn);
        Book book = bookMapper.selectOne(queryWrapper);
//        Book book = bookRepository.findByIsbn(isbn);
        BookOut out = new BookOut();
        if (null == book) {
            return out;
        }
        BeanUtil.copyProperties(book,out);
        out.setPublishTime(DateUtil.format(book.getPublishTime(),"yyyy-MM-dd"));
        return out;
    }

    /**
     * 删除图书
     * @param id 主键
     * @return true or false
     */
    public void deleteBook(Integer id) {
        bookMapper.deleteById(id);
//        bookRepository.deleteById(id);
    }


    /**
     * 图书搜索查询(mybatis 分页)
     * @param pageIn
     * @return
     */
    public PageOut getBookList(PageIn pageIn) {

        PageHelper.startPage(pageIn.getCurrPage(),pageIn.getPageSize());
        List<Book> list = bookMapper.findBookListByLike(pageIn.getKeyword());
        PageInfo<Book> pageInfo = new PageInfo<>(list);

        List<BookOut> bookOuts = new ArrayList<>();
        for (Book book : pageInfo.getList()) {
            BookOut out = new BookOut();
            BeanUtil.copyProperties(book,out);
            out.setPublishTime(DateUtil.format(book.getPublishTime(),"yyyy-MM-dd"));
            bookOuts.add(out);
        }

        // 自定义分页返回对象
        PageOut pageOut = new PageOut();
        pageOut.setList(bookOuts);
        pageOut.setTotal((int)pageInfo.getTotal());
        pageOut.setCurrPage(pageInfo.getPageNum());
        pageOut.setPageSize(pageInfo.getPageSize());
        return pageOut;
    }
}




