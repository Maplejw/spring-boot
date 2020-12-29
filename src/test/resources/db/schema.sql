
create table if not exists users (
	id BIGINT NOT NULL AUTO_INCREMENT,
	username varchar(100),
	password varchar(100),
	status int,
	PRIMARY KEY(id)
);

create table if not exists users_0 (id BIGINT not null primary key auto_increment,username varchar(100),password varchar(100),status int);

create table if not exists users_1 (id BIGINT not null primary key auto_increment,username varchar(100),password varchar(100),status int);

create table if not exists user_email_0 (
	id BIGINT not null  auto_increment,
	username varchar(100),
	password varchar(100),
	status int,
	primary key(id)
);

create table if not exists user_email_1 (
	id BIGINT not null  auto_increment,
	username varchar(100),
	password varchar(100),
	status int,
	primary key(id)
);

create table if not exists user_other(
	id BIGINT not null  auto_increment,
	uid BIGINT not null,
	primary key(id)
);

create table if not exists user_others_0(
	id BIGINT not null  auto_increment,
	uid BIGINT not null,
	primary key(id)
);

create table if not exists user_others_1(
	id BIGINT not null  auto_increment,
	uid BIGINT not null,
	primary key(id)
);