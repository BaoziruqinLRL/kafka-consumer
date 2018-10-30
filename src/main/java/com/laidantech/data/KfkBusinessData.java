package com.laidantech.data;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 标准业务对象
 * @Author: lirl
 * @Create: 2018-10-23 23:08
 */
@Data
@EqualsAndHashCode
public class KfkBusinessData {

    private String businessType;

    private String businessValue;

    private Long timestamp;
}
