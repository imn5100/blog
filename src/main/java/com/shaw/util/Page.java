package com.shaw.util;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class Page implements Serializable {
	private static final long serialVersionUID = -5684108796910091767L;
	private Boolean hasPrePage;
	private Boolean hasNextPage;
	private Integer everyPage = 10;
	private Integer totalPage;
	private Integer currentPage = 1;
	private Integer beginIndex;
	private Integer endinIndex;
	private Integer totalCount;
	private Boolean sort;
	private String defaultInfo = "&nbsp;&nbsp;";
	private Integer nextPage = currentPage + 1;
	private Integer previousPage = currentPage - 1 <= 0 ? 0 : currentPage - 1;

	public Integer getNextPage() {
		nextPage = currentPage + 1;
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public Integer getPreviousPage() {
		previousPage = currentPage - 1 <= 0 ? 0 : currentPage - 1;
		return previousPage;
	}

	public void setPreviousPage(Integer previousPage) {
		this.previousPage = previousPage;
	}

	public String getDefaultInfo() {
		return this.defaultInfo;
	}

	public void setDefaultInfo(String defaultInfo) {
		this.defaultInfo = defaultInfo;
	}

	public Page() {

	}

	public Page(Integer totalRecords) {
		this.totalCount = totalRecords;
		setTotalPage(getTotalPage(totalRecords));
	}

	public Page(Integer everyPage, Integer totalRecords) {
		this.everyPage = everyPage;
		this.totalCount = totalRecords;
		setTotalPage(getTotalPage(totalRecords));
	}

	public void pageState(int index, String value) {
		this.sort = Boolean.valueOf(false);
		switch (index) {
		case 0:
			setEveryPage(Integer.valueOf(Integer.getInteger(value)));
			break;
		case 1:
			first();
			break;
		case 2:
			previous();
			break;
		case 3:
			next();
			break;
		case 4:
			last();
			break;
		case 6:
			setCurrentPage(Integer.valueOf(Integer.getInteger(value)));
		}
	}

	private void first() {
		this.currentPage = 1;
	}

	private void previous() {
		this.currentPage = this.currentPage - 1;
	}

	private void next() {
		this.currentPage = this.currentPage + 1;
	}

	private void last() {
		this.currentPage = this.totalPage;
	}

	private Integer getTotalPage(Integer totalRecords) {
		Integer totalPage = 0;
		if (totalRecords.longValue() % this.everyPage.longValue() == 0L)
			totalPage = totalRecords / this.everyPage;
		else {
			totalPage = totalRecords / this.everyPage + 1;
		}
		return totalPage;
	}

	public Integer getBeginIndex() {
		this.beginIndex = ((this.currentPage - 1) * this.everyPage);
		return this.beginIndex;
	}

	public void setBeginIndex(Integer beginIndex) {
		this.beginIndex = beginIndex;
	}

	public Integer getCurrentPage() {
		this.currentPage = this.currentPage == 0 ? 1 : this.currentPage;
		return this.currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		if (0L == currentPage.longValue()) {
			currentPage = 1;
		}
		this.currentPage = currentPage;
	}

	public Integer getEveryPage() {
		this.everyPage = this.everyPage == 0 ? 10 : this.everyPage;
		return this.everyPage;
	}

	public void setEveryPage(Integer everyPage) {
		this.everyPage = everyPage;
	}

	public Boolean getHasNextPage() {
		this.hasNextPage = Boolean.valueOf((this.currentPage != this.totalPage) && (this.totalPage.longValue() != 0L));
		return this.hasNextPage;
	}

	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public Boolean getHasPrePage() {
		this.hasPrePage = Boolean.valueOf(this.currentPage.longValue() != 1L);
		return this.hasPrePage;
	}

	public void setHasPrePage(Boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}

	public Integer getTotalPage() {
		return this.totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		if (this.currentPage.longValue() > totalPage.longValue()) {
			this.currentPage = totalPage;
		}
		this.totalPage = totalPage;
	}

	public Integer getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		setTotalPage(getTotalPage(totalCount));
		this.totalCount = totalCount;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Boolean isSort() {
		return this.sort;
	}

	public void setSort(Boolean sort) {
		this.sort = sort;
	}

	public Integer getEndinIndex() {
		this.endinIndex = this.currentPage * this.everyPage;
		return this.endinIndex;
	}

	public void setEndinIndex(Integer endinIndex) {
		this.endinIndex = endinIndex;
	}
}
