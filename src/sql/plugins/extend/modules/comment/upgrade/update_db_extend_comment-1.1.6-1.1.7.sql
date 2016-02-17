--
-- EXTENDCOMMENT- : Add display sub comments mod
--
ALTER TABLE extend_comment_config ADD COLUMN is_enabled_display_sub_comments SMALLINT default 0 NOT NULL;
ALTER TABLE extend_comment_config ADD COLUMN is_enabled_delete_comments SMALLINT default 0 NOT NULL;




