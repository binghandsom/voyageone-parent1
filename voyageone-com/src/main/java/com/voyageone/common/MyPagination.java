package com.voyageone.common;


import java.util.ArrayList;
import java.util.List;


/// <summary>
/// 分页帮助类
/// </summary>
public class MyPagination {
    public MyPagination() {
    }

    //public MyPagination(PaginationParameter pp)
    //{
    //    this.PageIndex = pp.PageIndex;
    //    this.PageRowCount = pp.PageRowCount;
    //}
    public String GoPage = "goPage";
    public String _PaginationObject = "_PO";
    public String PaginationID;
    public String Path = "../Controls/Partial/MyPaginationControl";
    public String AjaxPath = "../Controls/Partial/MyAjaxPaginationControl";
    public String Url = "";
    public boolean IsAjax = false;
    private int _PageCount;
    private int _PageRowCount = 10;
    private int _PageIndex = 1;
    private int _RowCount;
    private List<Object> Args;
    public List<Object> getArgs() {
        return Args;
    }

    public void setArgs(List<Object> Args) {
        this.Args = Args;
    }
    /// <summary>
/// 页数量
/// </summary>
    public int getPageCount() {
        return _PageCount;
    }

    /// <summary>
/// 每页记录数
/// </summary>
    public int getPageRowCount() {
        return _PageRowCount;
    }

    public void setPageRowCount(int value) {
        if (value > 0) {
            _PageRowCount = value;
            SetPageCount();
        }
    }

    /// <summary>
/// 当前页
/// </summary>
    public int getPageIndex() {

        if (_PageIndex == 0) return 1;
        return _PageIndex;
    }

    public void setPageIndex(int value) {
        _PageIndex = value;
    }

    void SetPageCount() {
        _PageCount = (_RowCount - 1) / getPageRowCount() + 1;
    }

    /// <summary>
/// 总记录数
/// </summary>
    public int getRowCount() {
        return _RowCount;
    }

    public void setRowCount(int value) {
        _RowCount = value;
        SetPageCount();
        // _PageCount = (_RowCount - 1) / PageRowCount + 1;
    }

    /// <summary>
/// 开始记录号
/// </summary>
    public int getBeginRowIndex() {
        return _PageRowCount * (getPageIndex() - 1) + 1;
    }

    /// <summary>
/// 是否是当前页
/// </summary>
/// <param name="index"></param>
/// <returns></returns>
    public boolean IsCurrentPage(int index) {
        return getPageIndex() == index;
    }

    /// <summary>
/// 截止记录号
/// </summary>
    public int getEndRowIndex() {
        return _PageRowCount * (getPageIndex());
    }

    /// <summary>
/// 是否显示上一页 (分页控件）
/// </summary>
    public boolean getIsPrePage() {
        return getPageIndex() > 1;
    }

    /// <summary>
/// 是否显示下一页 (分页控件)
/// </summary>
    public boolean getIsNextPage() {
        return getPageIndex() < getPageCount();
    }

    int _ViewPageCount;

    /// <summary>
/// 分页链接数量
/// </summary>
    public int getViewPageCount() {

        if (_ViewPageCount == 0) return 10;
        return _ViewPageCount;
    }

    public void setViewPageCount(int value) {
        _ViewPageCount = value;
    }

    /// <summary>
///  获取分页链接 分页索引列表
/// </summary>
/// <returns></returns>
    public List<Integer> GetPageList() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(getPageIndex());
        int ViewCountNext = getViewPageCount() / 2; //当前页后的 分页数
        for (int i = 0; i < ViewCountNext; i++) {
            if ((int) list.get(i) + 1 <= getPageCount()) {
                list.add((int) list.get(i) + 1);
            } else {
                break;
            }
        }
        int ViewCountPre = getViewPageCount() - list.size();//当前页前的分页数
        for (int i = 0; i < ViewCountPre; i++) {
            if ((int) list.get(0) - 1 > 0) {
                list.add(0, (int) list.get(0) - 1);
            } else {
                break;
            }
        }
        int ViewCountNext1 = getViewPageCount() - list.size();//总分页数不够，有后面补上
        for (int i = list.size() - 1; i < getViewPageCount() - 1; i++) {
            if ((int) list.get(i) + 1 < getPageCount()) {
                list.add((int) list.get(i) + 1);
            } else {
                break;
            }
        }
        return list;
    }


    public String sql;


    public String sqlCount;


    public String OrderBy;


    public boolean IsAll;

    public EnumPaginationType PaginationType;


    public String OrderMinValue;


    public String OrderMaxValue;

    public String GetOrderBy() {
        switch (PaginationType) {
            case Previous:
            case Last:
                return String.format("%1$s asc", OrderBy);
            default:
                return String.format("%1$s desc", OrderBy);
        }
    }

    public String GetOrderByDesc() {
        switch (PaginationType) {
            case Previous:
            case Last:
                return String.format(" Order By %1$s Desc", OrderBy);
            default:
                return ""; //string.Format("{0} desc", OrderBy);
        }
    }

    public int GetBeginRowIndex() {
        switch (PaginationType) {
            case PageInex:
                return this.getBeginRowIndex();
            default:
                return 1;

        }
    }

    public int GetEndRowIndex() {
        switch (PaginationType) {
            case PageInex:
                return this.getEndRowIndex();
            default:
                return this.getPageRowCount();
        }
    }

    public String getOrderWhere() {

        switch (PaginationType) {
            case Top:
                return "";
            case Next:
                return String.format("and %1$s<%2$s", OrderBy, OrderMinValue);
            case Previous:
                return String.format("and %1$s>%2$s", OrderBy, OrderMaxValue);
            case Last:
                return "";
            default:
                return "";
        }
    }


}
