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
 * @TableName book
 */
@TableName(value ="book")
@Data
public class Book implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String author;

    /**
     * 
     */
    private String isbn;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer pages;

    /**
     * 
     */
    private Double price;

    /**
     * 
     */
    private String publish;

    /**
     * 
     */
    private Date publishTime;

    /**
     * 
     */
    private Integer size;

    /**
     * 
     */
    private String translate;

    /**
     * 
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}