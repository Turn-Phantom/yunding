package com.li.cloud.common.basecurd.entity;

import com.github.pagehelper.PageHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc: 分页对象
 * @date: 2019-07-27
 */
public class Pagination<T> {

    // 页码
    private int pageNo = 1;

    // 页数
    private int pageSize = 30;

    // 总页数
    private int pageTotal;

    // 结果集总数
    private int resultTotal;

    // 当前查询结果集
    private List<T> resultDatas;

    // 分页参数
    private Map<String, Object> params = new HashMap<>();

    /**
     * @desc: 初始化分页插件
     * @date: 2019-07-28
     */
    public void initPageHelper(){
        PageHelper.startPage(pageNo, pageSize);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        // 若页码小于1，则默认设置页面为1
        if(pageNo < 1){
            pageNo = 1;
        }
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getResultTotal() {
        return resultTotal;
    }

    public void setResultTotal(int resultTotal) {
        this.resultTotal = resultTotal;
        this.pageTotal = resultTotal % pageSize == 0 ? resultTotal / pageSize : resultTotal / pageSize + 1;
    }

    public List<T> getResultDatas() {
        return resultDatas;
    }

    public void setResultDatas(List<T> resultDatas) {
        this.resultDatas = resultDatas;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
