package com.kdgcsoft.web.common.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 实现mybatisplus分页接口 扩展前台接收分页参数对象
 *
 * @author fyin
 * @date 2021-04-30 9:57
 */
@ApiModel("分页对象")
public class PageRequest<T> implements IPage<T> {
    @ApiModelProperty("当前页数")
    protected long current = 1;
    @ApiModelProperty("记录总数")
    private long total = 0;
    @ApiModelProperty("分页显示条数,默认20")
    private long size = 20;
    @ApiModelProperty("数据列表")
    private List<T> rows = Collections.emptyList();

    @ApiModelProperty(value = "排序字符串", example = "deptName desc,orgName asc")
    private String orders;

    @Override
    public List<OrderItem> orders() {
        List<OrderItem> orderList = new ArrayList<>();
        if (StrUtil.isNotEmpty(this.orders)) {
            List<String> orders = StrUtil.split(this.orders, ",");
            for (String str : orders) {
                if (StrUtil.isNotEmpty(str)) {
                    List<String> ordergroup = StrUtil.split(str, " ");
                    if (ordergroup.size() == 2) {
                        String column = ordergroup.get(0);
                        String dir = ordergroup.get(1);
                        if (StrUtil.equalsAnyIgnoreCase(dir, "desc")) {
                            orderList.add(OrderItem.desc(StrUtil.toUnderlineCase(StrUtil.upperFirst(column))));
                        } else if (StrUtil.equalsAnyIgnoreCase(dir, "asc")) {
                            orderList.add(OrderItem.asc(StrUtil.toUnderlineCase(StrUtil.upperFirst(column))));
                        }
                    }
                }
            }
        }
        return orderList;
    }

    @JsonIgnore
    @Override
    public List<T> getRecords() {
        return this.rows;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.rows = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getOrders() {
        return orders;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
