package com.igg.boot.framework.jdbc.persistence.bean;

public enum JoinType {
	JOIN {
		@Override
		public String fetchType() {
			return " join ";
		}
	}, LEFT_JOIN {
		@Override
		public String fetchType() {
			return " left join ";
		}
	}, RIGHT_JOIN {
		@Override
		public String fetchType() {
			return " right join ";
		}
	};

	private JoinType() {
	}

	public abstract String fetchType();
}