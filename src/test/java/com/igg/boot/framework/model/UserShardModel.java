package com.igg.boot.framework.model;

import com.igg.boot.framework.jdbc.persistence.Entity;
import com.igg.boot.framework.jdbc.persistence.annotation.Column;
import com.igg.boot.framework.jdbc.persistence.annotation.Id;
import com.igg.boot.framework.jdbc.persistence.annotation.Table;

import lombok.Getter;
import lombok.Setter;

@Table(value = "users")
@Setter
@Getter
public class UserShardModel extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@Id
	private Long id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private int status;
}
