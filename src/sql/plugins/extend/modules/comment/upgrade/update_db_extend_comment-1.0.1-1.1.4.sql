--
-- EXTENDCOMMENT- : Add possibility to choose position of comment form
--
ALTER TABLE extend_comment_config ADD COLUMN add_comment_position SMALLINT NOT NULL default 0;

