package com.igg.boot.framework.jdbc.persistence.bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.igg.boot.framework.jdbc.persistence.Entity;

public class JoinItem {
	private final Class<? extends Entity> clazz;
	private final boolean hasItem;
	private final JoinType joinType;
	private List<String> entityItems;

	public JoinItem(Class<? extends Entity> clazz, String... entityItems) {
		this(clazz, JoinType.JOIN, entityItems);
	}

	public JoinItem(Class<? extends Entity> clazz, JoinType joinType, String... entityItems) {
		this.clazz = clazz;
		this.joinType = joinType;
		if (entityItems.length > 0) {
			this.hasItem = true;
			this.entityItems = Arrays.asList(entityItems);
		} else {
			this.hasItem = false;
			this.entityItems = Collections.emptyList();
		}

	}

	public JoinItem(Class<? extends Entity> clazz, boolean hasItem, String... entityItems) {
		this(clazz, JoinType.JOIN, hasItem, entityItems);
	}

	public JoinItem(Class<? extends Entity> clazz, JoinType joinType, boolean hasItem, String... entityItems) {
		this.clazz = clazz;
		this.hasItem = hasItem;
		this.joinType = joinType;
		if (entityItems.length > 0) {
			this.entityItems = Arrays.asList(entityItems);
		} else {
			this.entityItems = Collections.emptyList();
		}

	}

	public Class<? extends Entity> getClazz() {
		return this.clazz;
	}

	public boolean isHasItem() {
		return this.hasItem;
	}

	public JoinType getJoinType() {
		return this.joinType;
	}

	public List<String> getEntityItems() {
		return this.entityItems;
	}

	public void setEntityItems(List<String> entityItems) {
		this.entityItems = entityItems;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof JoinItem)) {
			return false;
		} else {
			JoinItem other = (JoinItem) o;
			if (!other.canEqual(this)) {
				return false;
			} else {
				Class<?> this$clazz = this.getClazz();
				Class<?> other$clazz = other.getClazz();
				if (this$clazz == null) {
					if (other$clazz != null) {
						return false;
					}
				} else if (!this$clazz.equals(other$clazz)) {
					return false;
				}

				if (this.isHasItem() != other.isHasItem()) {
					return false;
				} else {
					JoinType this$joinType = this.getJoinType();
					JoinType other$joinType = other.getJoinType();
					if (this$joinType == null) {
						if (other$joinType != null) {
							return false;
						}
					} else if (!this$joinType.equals(other$joinType)) {
						return false;
					}

					List<String> this$entityItems = this.getEntityItems();
					List<String> other$entityItems = other.getEntityItems();
					if (this$entityItems == null) {
						if (other$entityItems != null) {
							return false;
						}
					} else if (!this$entityItems.equals(other$entityItems)) {
						return false;
					}

					return true;
				}
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof JoinItem;
	}

	public int hashCode() {
		byte result = 1;
		Class<?> $clazz = this.getClazz();
		int result1 = result * 59 + ($clazz == null ? 43 : $clazz.hashCode());
		result1 = result1 * 59 + (this.isHasItem() ? 79 : 97);
		JoinType $joinType = this.getJoinType();
		result1 = result1 * 59 + ($joinType == null ? 43 : $joinType.hashCode());
		List<String> $entityItems = this.getEntityItems();
		result1 = result1 * 59 + ($entityItems == null ? 43 : $entityItems.hashCode());
		return result1;
	}

	public String toString() {
		return "JoinItem(clazz=" + this.getClazz() + ", hasItem=" + this.isHasItem() + ", joinType="
				+ this.getJoinType() + ", entityItems=" + this.getEntityItems() + ")";
	}
}