package com.igg.boot.framework.jdbc.persistence.bean;

import java.util.List;

public class DataStore<T> {
	private int records;
	private List<T> datas;

	public DataStore() {
	}

	public DataStore(int records, List<T> datas) {
		this.records = records;
		this.datas = datas;
	}

	public DataStore(PagingParameter paging, List<T> datas) {
		if (datas != null) {
			this.records = datas.size();
			if (paging != null && !paging.isInvalid()) {
				int end = paging.getStart() + paging.getLimit();
				if (end > this.records) {
					end = this.records;
				}

				this.datas = datas.subList(paging.getStart(), end);
			} else {
				this.datas = datas;
			}

		}
	}

	public int getRecords() {
		return this.records;
	}

	public List<T> getDatas() {
		return this.datas;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof DataStore)) {
			return false;
		} else {
			DataStore<?> other = (DataStore<?>) o;
			if (!other.canEqual(this)) {
				return false;
			} else if (this.getRecords() != other.getRecords()) {
				return false;
			} else {
				List this$datas = this.getDatas();
				List other$datas = other.getDatas();
				if (this$datas == null) {
					if (other$datas != null) {
						return false;
					}
				} else if (!this$datas.equals(other$datas)) {
					return false;
				}

				return true;
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof DataStore;
	}

	public int hashCode() {
		byte result = 1;
		int result1 = result * 59 + this.getRecords();
		List<T> $datas = this.getDatas();
		result1 = result1 * 59 + ($datas == null ? 43 : $datas.hashCode());
		return result1;
	}

	public String toString() {
		return "DataStore(records=" + this.getRecords() + ", datas=" + this.getDatas() + ")";
	}
}