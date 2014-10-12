package com.hecj.smsclock.util;

import java.io.Serializable;

/**
 * 分页实体类
 * 
 * @author HECJ
 * 
 */
public class Pagination implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer pageSize = 5;

	private Integer currPage = 0;

	private Integer countSize = 0;

	public Pagination() {
		super();
	}

	/**
	 * 初始化每页条数
	 * 
	 * @param pPageSize
	 */
	public Pagination(Integer pPageSize) {
		super();
		this.pageSize = pPageSize;
	}

	public Pagination(Integer pageSize, Integer currPage, Integer countSize) {
		super();
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.countSize = countSize;
	}

	/**
	 * 每页多少条
	 * 
	 * @return
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 当前页
	 * 
	 * @return
	 */
	public Integer getCurrPage() {
		return this.currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	/**
	 * 共多少条
	 * 
	 * @return
	 */
	public Integer getCountSize() {
		return this.countSize;
	}

	public void setCountSize(Integer countSize) {
		this.countSize = countSize;
	}

	/**
	 * 共多少页
	 * 
	 * @return
	 */
	public Integer getPageCount() {

		return this.getCountSize() % this.getPageSize() == 0 ? this.getCountSize() / this.getPageSize() : this.getCountSize()
				/ this.getPageSize() + 1;
	}

	/**
	 * 游标开始位置
	 * 
	 * @return
	 */
	public Integer startCursor() {

		return this.getPageSize() * (this.getCurrPage() - 1);
	}


}
