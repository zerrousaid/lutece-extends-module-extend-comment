--
-- Structure for table extend_comment
--
DROP TABLE IF EXISTS extend_comment;
CREATE TABLE extend_comment (
	id_comment INT DEFAULT 0 NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	name VARCHAR(255) DEFAULT '' NOT NULL,
	date_comment TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	email VARCHAR(255) NOT NULL,
	ip_address VARCHAR(100) NOT NULL,
	comment LONG VARCHAR NOT NULL,
	is_published SMALLINT default 0 NOT NULL,
	date_last_modif TIMESTAMP,
	id_parent_comment INT DEFAULT 0 NOT NULL,
	is_admin_comment SMALLINT default 0 NOT NULL,
	admin_badge LONG VARCHAR NOT NULL,
	message_comment_created LONG VARCHAR NOT NULL,
	PRIMARY KEY (id_comment)
);

--
-- Structure for table extend_comment_config
--
DROP TABLE IF EXISTS extend_comment_config;
CREATE TABLE extend_comment_config (
	id_extender INT DEFAULT 0 NOT NULL,
	is_moderated SMALLINT default 0 NOT NULL,
	nb_comments INT DEFAULT 1 NOT NULL,
	id_mailing_list INT DEFAULT 0 NOT NULL,
	authorize_sub_comments SMALLINT default 0 NOT NULL,
	use_bbcode SMALLINT default 1 NOT NULL,
	PRIMARY KEY (id_extender)
);
