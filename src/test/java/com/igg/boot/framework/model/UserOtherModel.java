package com.igg.boot.framework.model;

import com.igg.boot.framework.jdbc.persistence.Entity;
import com.igg.boot.framework.jdbc.persistence.annotation.Column;
import com.igg.boot.framework.jdbc.persistence.annotation.Id;
import com.igg.boot.framework.jdbc.persistence.annotation.Reference;
import com.igg.boot.framework.jdbc.persistence.annotation.Table;

import lombok.Getter;
import lombok.Setter;

@Table("user_other")
@Setter
@Getter
public class UserOtherModel extends Entity{
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	private Long id;
	
	@Column
	@Reference(value = UserModel.class,column = "id")
	private Long uid;
	
	private String username;
}
