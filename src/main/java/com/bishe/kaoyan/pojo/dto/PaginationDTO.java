package com.bishe.kaoyan.pojo.dto;

import java.util.ArrayList;
import java.util.List;

//对返回页码的包装
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;//是否有前按钮
    private boolean showFirstPage;//是否有最前按钮
    private boolean showNext;//是否有后按钮
    private boolean showEndPage;//是否有最后按钮
    private Integer page;//当前页码
    private List<Integer> pages = new ArrayList<>();//显示的页码
    private Integer totalPage;

    public void setPagnation(Integer totalCount, Integer page, Integer size) {
        if (totalCount == 0){
            this.showPrevious = false;
            this.showFirstPage = false;
            this.showNext = false;
            this.showEndPage = false;
        }else {
            Integer totalPage;
            this.page = page;
            if(totalCount % size == 0)
                totalPage = totalCount / size;
            else
                totalPage = totalCount / size + 1;

            pages.add(page);
            //显示页码的多少
            for(int i = 1; i <= 3; i++){
                if(page - i > 0)
                    pages.add(0,page - i);

                if(page + i <= totalPage)
                    pages.add(page + i);
            }

            //是否展示上一页
            if(page == 1)
                showPrevious = false;
            else
                showPrevious = true;

            //是否展示下一页
            if(page == totalPage)
                showNext = false;
            else
                showNext = true;

            //是否展示第一页
            if(pages.contains(1))
                showFirstPage = false;
            else
                showFirstPage = true;

            //是否展示最后一页
            if(pages.contains(totalPage))
                showEndPage = false;
            else
                showEndPage = true;
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public boolean isShowFirstPage() {
        return showFirstPage;
    }

    public void setShowFirstPage(boolean showFirstPage) {
        this.showFirstPage = showFirstPage;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowEndPage() {
        return showEndPage;
    }

    public void setShowEndPage(boolean showEndPage) {
        this.showEndPage = showEndPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
