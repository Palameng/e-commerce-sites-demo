package com.mymall.pojo;

import lombok.*;

import java.util.Date;

/**
 * Data 注解默认包含了get set方法, 但不单纯是只有get set方法，同时还包含了equal hashcode方法 canEqual方法,但是
 * 全参构造器和无参构造器是没有的
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;


}