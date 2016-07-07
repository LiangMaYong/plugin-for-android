package com.liangmayong.androidplugin.management.db;

/**
 * APDatabasePager
 * 
 * @author LiangMaYong
 * @version 1.0
 */
public class APDatabasePager {
	private int count = 0;
	private int pagerid = 0;
	private int start = 0;
	private int total = 0;

	/**
	 * init APDatabasePager
	 * 
	 * @param start
	 *            start
	 * @param count
	 *            count
	 * @param pagerid
	 *            pagerid
	 */
	public APDatabasePager(int start, int count, int pagerid) {
		this.count = count;
		this.pagerid = pagerid;
		this.start = start;
	}

	/**
	 * get total pager
	 * 
	 * @return total pager
	 */
	public int getTotalPager() {
		return (total - start) / count > 0 ? (total - start) / count : 1;
	}

	/**
	 * get total rows
	 * 
	 * @return total rows
	 */
	public int getTotalRows() {
		return total;
	}

	/**
	 * get now pager
	 * 
	 * @return now pager
	 */
	public int getNowPager() {
		return pagerid;
	}

	/**
	 * get list rows
	 * 
	 * @return list rows
	 */
	public int getListRows() {
		return count;
	}

	void setTotal(int total) {
		this.total = total;
	}

	String getLimit() {
		if (pagerid < 1) {
			pagerid = 1;
		}
		return ((pagerid - 1) * count + start) + "," + (pagerid * count + start);
	}

	/**
	 * next
	 */
	public void next() {
		pagerid++;
	}

	/**
	 * previous
	 */
	public void previous() {
		pagerid--;
	}
}
