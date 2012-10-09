--
-- Structure for table socialhub_comment
--
DROP TABLE IF EXISTS socialhub_comment;
CREATE TABLE socialhub_comment (
	id_comment INT DEFAULT 0 NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	name VARCHAR(255) DEFAULT '' NOT NULL,
	date_comment TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	email VARCHAR(255) NOT NULL,
	ip_address VARCHAR(100) NOT NULL,
	comment LONG VARCHAR NOT NULL,
	is_published SMALLINT default 0 NOT NULL,
	PRIMARY KEY (id_comment)
);

--
-- Structure for table socialhub_comment_config
--
DROP TABLE IF EXISTS socialhub_comment_config;
CREATE TABLE socialhub_comment_config (
	id_extender INT DEFAULT 0 NOT NULL,
	is_moderated SMALLINT default 0 NOT NULL,
	nb_comments INT DEFAULT 1 NOT NULL,
	id_mailing_list INT DEFAULT 0 NOT NULL,
	PRIMARY KEY (id_extender)
);
