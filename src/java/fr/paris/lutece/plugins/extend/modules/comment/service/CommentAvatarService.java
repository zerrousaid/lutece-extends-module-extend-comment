package fr.paris.lutece.plugins.extend.modules.comment.service;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.avatar.service.AvatarService;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.util.constants.CommentConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * CommentAvatarService
 *
 */
public class CommentAvatarService implements ICommentAvatarService{

	
	private static ICommentAvatarService _singleton; 
	private static boolean _bUseLuteceUserNameAsAvatarKey;
	
	
    /**
     * {@inheritDoc}
     */
    @Override
    public String getAvatar(Comment comment)
    {
    	if(_bUseLuteceUserNameAsAvatarKey && !StringUtils.isEmpty(comment.getLuteceUserName()))
    	{
    		return AvatarService.getAvatar( comment.getLuteceUserName() );
    	}
    	return AvatarService.getAvatar( comment.getEmail() );
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getAvatarUrl(Comment comment)
    {
    	if(_bUseLuteceUserNameAsAvatarKey && !StringUtils.isEmpty(comment.getLuteceUserName()))
    	{
    		return AvatarService.getAvatarUrl( comment.getLuteceUserName() );
    	}
        return AvatarService.getAvatarUrl( comment.getEmail()  );
    }
    
    /**
     * 
     * @return singleton
     */
    public static ICommentAvatarService getInstance()
    {
    	
    	if(_singleton == null)
    	{
    		
    		_singleton=new CommentAvatarService();
    		_bUseLuteceUserNameAsAvatarKey=AppPropertiesService.getPropertyBoolean(CommentConstants.PROPERTY_USE_LUTECE_USER_NAME_AS_AVATAR_KEY, false);
    	}
    	
    	return _singleton;
    	
    	
    }

}
