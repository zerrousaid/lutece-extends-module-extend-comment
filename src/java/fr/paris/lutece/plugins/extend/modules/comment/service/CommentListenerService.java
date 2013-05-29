package fr.paris.lutece.plugins.extend.modules.comment.service;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.ICommentDAO;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service to manage listeners over comments
 */
public class CommentListenerService
{
    /**
     * Constant that represents every extendable resource type
     */
    public static final String CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE = "*";

    private static Map<String, List<ICommentListener>> _mapListeners = new HashMap<String, List<ICommentListener>>( );
    private static boolean _bHasListeners;

    private static ICommentDAO _commentDAO;

    /**
     * Private constructor
     */
    private CommentListenerService( )
    {

    }

    /**
     * Register a comment listener.
     * @param strExtendableResourceType The extendable resource type associated
     *            with the listener. Use
     *            {@link #CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE} to associated
     *            the listener with every resource type.
     * @param listener The listener to register
     */
    public static synchronized void registerListener( String strExtendableResourceType, ICommentListener listener )
    {
        List<ICommentListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners == null )
        {
            listListeners = new ArrayList<ICommentListener>( );
            _mapListeners.put( strExtendableResourceType, listListeners );
        }
        listListeners.add( listener );
        _bHasListeners = true;
    }

    /**
     * Check if there is listeners to notify
     * @return True if there is at last one listener, false otherwise
     */
    public static boolean hasListener( )
    {
        return _bHasListeners;
    }

    /**
     * Notify to listeners the creation of a comment. Only listeners associated
     * with the extendable resource type of the comment are notified.
     * @param strExtendableResourceType The extendable resource type of the
     *            created comment
     * @param strIdExtendableResource The extendable resource id of the comment
     * @param bPublished True if the comment is published, false otherwise
     */
    public static void createComment( String strExtendableResourceType, String strIdExtendableResource,
            boolean bPublished )
    {
        List<ICommentListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners != null )
        {
            for ( ICommentListener listener : listListeners )
            {
                listener.createComment( strIdExtendableResource, bPublished );
            }
        }
        listListeners = _mapListeners.get( CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE );
        if ( listListeners != null )
        {
            for ( ICommentListener listener : listListeners )
            {
                listener.createComment( strIdExtendableResource, bPublished );
            }
        }
    }

    /**
     * Notify to listeners the modification of a comment. Only listeners
     * associated with the extendable resource type of the comment are notified.
     * @param nIdComment The id of the updated comment
     * @param bPublished True if the comment was published, false if it was
     *            unpublished
     */
    public static void publishComment( int nIdComment, boolean bPublished )
    {
        if ( _mapListeners.size( ) > 0 )
        {
            Comment comment = getCommentDAO( ).load( nIdComment, CommentPlugin.getPlugin( ) );
            publishComment( comment.getExtendableResourceType( ), comment.getIdExtendableResource( ), bPublished );
        }
    }

    /**
     * Notify to listeners the modification of a comment. Only listeners
     * associated with the extendable resource type of the comment are notified.
     * @param strExtendableResourceType The extendable resource type of the
     *            updated comment
     * @param strIdExtendableResource The extendable resource id of the comment
     * @param bPublished True if the comment was published, false if it was
     *            unpublished
     */
    public static void publishComment( String strExtendableResourceType, String strIdExtendableResource,
            boolean bPublished )
    {
        List<ICommentListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners != null )
        {
            for ( ICommentListener listener : listListeners )
            {
                listener.publishComment( strIdExtendableResource, bPublished );
            }
        }
        listListeners = _mapListeners.get( CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE );
        if ( listListeners != null )
        {
            for ( ICommentListener listener : listListeners )
            {
                listener.publishComment( strIdExtendableResource, bPublished );
            }
        }
    }

    /**
     * Notify to listeners the modification of a comment. Only listeners
     * associated with the extendable resource type of the comment are notified.
     * @param strExtendableResourceType The extendable resource type of the
     *            removed comment
     * @param strIdExtendableResource The extendable resource id of the comment
     * @param listIdRemovedComment The list of ids of removed comments
     */
    public static void deleteComment( String strExtendableResourceType, String strIdExtendableResource,
            List<Integer> listIdRemovedComment )
    {
        try
        {
            List<ICommentListener> listListeners = _mapListeners.get( strExtendableResourceType );
            if ( listListeners != null )
            {
                for ( ICommentListener listener : listListeners )
                {
                    listener.deleteComment( strIdExtendableResource, listIdRemovedComment );
                }
            }
            listListeners = _mapListeners.get( CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE );
            if ( listListeners != null )
            {
                for ( ICommentListener listener : listListeners )
                {
                    listener.deleteComment( strIdExtendableResource, listIdRemovedComment );
                }
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    /**
     * Get the comment DAO
     * @return the comment DAO
     */
    private static ICommentDAO getCommentDAO( )
    {
        if ( _commentDAO == null )
        {
            synchronized ( CommentListenerService.class )
            {
                if ( _commentDAO == null )
                {
                    List<ICommentDAO> listDao = SpringContextService.getBeansOfType( ICommentDAO.class );
                    if ( listDao != null && listDao.size( ) > 0 )
                    {
                        _commentDAO = listDao.get( 0 );
                    }
                }
            }
        }
        return _commentDAO;
    }
}
