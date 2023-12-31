package com.hbnx.book.manager.controller;

import cn.hutool.core.date.DateUtil;
import com.hbnx.book.manager.entity.Borrow;
import com.hbnx.book.manager.service.BookService;
import com.hbnx.book.manager.service.BorrowService;
import com.hbnx.book.manager.util.R;
import com.hbnx.book.manager.util.consts.Constants;
import com.hbnx.book.manager.util.http.CodeEnum;
import com.hbnx.book.manager.util.vo.BackOut;
import com.hbnx.book.manager.util.vo.BookOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HBNXhuanglutian
 * @version 1.0
 * @date 2023/9/26 16:45
 * @Description
 */
@RestController
@AllArgsConstructor
@NoArgsConstructor
@Api(tags = "借阅管理")
@RequestMapping("/borrow")
public class BorrowController {
    @Resource
    private BorrowService borrowService;

    //    @Autowired
    @Resource
    private BookService bookService;

    @ApiOperation("借阅列表")
    @GetMapping("/list")
    public R getBorrowList(Integer userId) {
        return R.success(CodeEnum.SUCCESS,borrowService.findAllBorrowByUserId(userId));
    }

    @ApiOperation("借阅图书")
    @PostMapping("/add")
    public R addBorrow(@RequestBody Borrow borrow) {
        Integer result = borrowService.addBorrow(borrow);
        if (result == Constants.BOOK_BORROWED) {
            return R.success(CodeEnum.BOOK_BORROWED);
        }else if (result == Constants.USER_SIZE_NOT_ENOUGH) {
            return R.success(CodeEnum.USER_NOT_ENOUGH);
        }else if (result == Constants.BOOK_SIZE_NOT_ENOUGH) {
            return R.success(CodeEnum.BOOK_NOT_ENOUGH);
        }
        return R.success(CodeEnum.SUCCESS,Constants.OK);
    }

    @ApiOperation("编辑借阅")
    @PostMapping("/update")
    public R modifyBorrow(@RequestBody Borrow borrow) {
        return R.success(CodeEnum.SUCCESS,borrowService.updateBorrow(borrow));
    }


    @ApiOperation("借阅详情")
    @GetMapping("/detail")
    public R borrowDetail(Integer id) {
        return R.success(CodeEnum.SUCCESS,borrowService.findById(id));
    }

    @ApiOperation("删除归还记录")
    @GetMapping("/delete")
    public R delBorrow(Integer id) {
        borrowService.deleteBorrow(id);
        return R.success(CodeEnum.SUCCESS);
    }


    @ApiOperation("已借阅列表")
    @GetMapping("/borrowed")
    public R borrowedList(Integer userId) {
        List<BackOut> outs = new ArrayList<>();
        if (userId!=null&&userId>0) {
            // 获取所有 已借阅 未归还书籍
            List<Borrow> borrows = borrowService.findBorrowsByUserIdAndRet(userId, Constants.NO);
            for (Borrow borrow : borrows) {
                BackOut backOut = new BackOut();
                BookOut out = bookService.findBookById(borrow.getBookId());
                BeanUtils.copyProperties(out,backOut);

                backOut.setBorrowTime(DateUtil.format(borrow.getCreateTime(),Constants.DATE_FORMAT));

                String endTimeStr = DateUtil.format(borrow.getEndTime(), Constants.DATE_FORMAT);
                backOut.setEndTime(endTimeStr);
                // 判断是否逾期
                String toDay = DateUtil.format(new Date(), Constants.DATE_FORMAT);
                int i = toDay.compareTo(endTimeStr);
                if (i>0) {
                    backOut.setLate(Constants.YES_STR);
                }else {
                    backOut.setLate(Constants.NO_STR);
                }

                outs.add(backOut);
            }
        }

        return R.success(CodeEnum.SUCCESS,outs);
    }

    @ApiOperation("归还书籍")
    @PostMapping("/ret")
    public R retBook(Integer userId, Integer bookId) {
        // 归还图书
        borrowService.retBook(userId,bookId);
        return R.success(CodeEnum.SUCCESS);
    }
}
