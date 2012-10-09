--
-- EXTENDCOMMENT-2 : Add the possibility to notify a mailing list when a comment is added.
--
ALTER TABLE extend_comment_config ADD COLUMN id_mailing_list INT DEFAULT 0 NOT NULL;
