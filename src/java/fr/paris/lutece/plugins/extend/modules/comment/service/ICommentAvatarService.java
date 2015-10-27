package fr.paris.lutece.plugins.extend.modules.comment.service;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
/**
 * 
 * ICommentAvatarService
 *
 */
public interface ICommentAvatarService {
	
	/**
     * Display the avatar of the comment's author
     * @return The HTML code of the avatar
     */
	String getAvatar(Comment comment);
    /**
     * Display the avatar of the comment's author
     * @return The HTML code of the avatar
     */
    String getAvatarUrl(Comment comment);
    
}
