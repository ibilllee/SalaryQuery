package com.bill.domain;

import java.util.List;

public class PageBean<T>
{
	private int totalCount;//总记录数
	private int totalPage;//总页码
	private List<T> list;//数据表
	private int currentPage;//当前页数
	private int rows;//一页行数

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "PageBean{" +
				"totalCount=" + totalCount +
				", totalPage=" + totalPage +
				", list=" + list +
				", currentPage=" + currentPage +
				", rows=" + rows +
				'}';
	}
}
