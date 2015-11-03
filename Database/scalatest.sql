CREATE TABLE users
(
	id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	name varchar(255) NOT NULL COMMENT '名前',
	gender enum('male', 'female') NOT NULL COMMENT '性別',
	created datetime DEFAULT '0000-00-00 00:00:00' NOT NULL COMMENT '登録時間',
	modified datetime DEFAULT '0000-00-00 00:00:00' NOT NULL COMMENT '更新時間',
	is_deleted tinyint(1) unsigned DEFAULT 0 NOT NULL COMMENT '削除フラグ : true: 1, false: 0',
	deleted datetime DEFAULT '0000-00-00 00:00:00' COMMENT '削除時間',
	PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT = 'ユーザー' DEFAULT CHARACTER SET utf8;
