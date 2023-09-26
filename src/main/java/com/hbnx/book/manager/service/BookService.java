package com.hbnx.book.manager.service;

import com.hbnx.book.manager.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbnx.book.manager.util.ro.PageIn;
import com.hbnx.book.manager.util.vo.BookOut;
import com.hbnx.book.manager.util.vo.PageOut;

/**
* @author nx
* @description 针对表【book】的数据库操作Service
* @createDate 2023-09-26 16:11:26
*/
public interface BookService extends IService<Book> {
    Book addBook(Book book);
    boolean updateBook(Book book);
    BookOut findBookById(Integer id);
    Book findBook(Integer id);
    BookOut findBookByIsbn(String isbn);
    void deleteBook(Integer id);
    PageOut getBookList(PageIn pageIn);

}
