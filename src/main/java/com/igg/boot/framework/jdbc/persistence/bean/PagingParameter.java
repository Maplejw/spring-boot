package com.igg.boot.framework.jdbc.persistence.bean;

public class PagingParameter {
	private int start = -1;
	private int limit = 0;

	public PagingParameter() {
	}

	public PagingParameter(int start, int limit) {
		this.start = start;
		this.limit = limit;
	}

	public boolean isInvalid() {
		return this.start < 0 || this.limit <= 0;
	}

	public int getStart() {
		return this.start;
	}

	public int getLimit() {
		return this.limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof PagingParameter)) {
			return false;
		} else {
			PagingParameter other = (PagingParameter) o;
			return !other.canEqual(this)
					? false
					: (this.getStart() != other.getStart() ? false : this.getLimit() == other.getLimit());
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof PagingParameter;
	}

	public int hashCode() {
		byte result = 1;
		int result1 = result * 59 + this.getStart();
		result1 = result1 * 59 + this.getLimit();
		return result1;
	}

	public String toString() {
		return "PagingParameter(start=" + this.getStart() + ", limit=" + this.getLimit() + ")";
	}
}