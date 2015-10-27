--
-- EXTENDCOMMENT- : Add authentication mod
--
ALTER TABLE extend_comment_config ADD COLUMN is_enabled_auth_mode SMALLINT NOT NULL default 0;
ALTER TABLE extend_comment ADD COLUMN lutece_user_name VARCHAR(255) DEFAULT '' NOT NULL;



