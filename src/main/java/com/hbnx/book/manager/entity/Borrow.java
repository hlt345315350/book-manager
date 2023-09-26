package com.hbnx.book.manager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName borrow
 */
@TableName(value ="borrow")
@Data
public class Borrow implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer bookId;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Date endTime;

    /**
     * 
     */
    private Integer ret;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}