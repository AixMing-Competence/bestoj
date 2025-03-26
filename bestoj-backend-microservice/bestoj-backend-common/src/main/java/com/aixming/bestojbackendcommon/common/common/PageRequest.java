package com.aixming.bestojbackendcommon.common.common;

import com.aixming.bestojbackendcommon.common.constant.CommonConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author AixMing
 */
@Data
public class PageRequest implements Serializable {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;

    private static final long serialVersionUID = 3385807682750140172L;
    
}
