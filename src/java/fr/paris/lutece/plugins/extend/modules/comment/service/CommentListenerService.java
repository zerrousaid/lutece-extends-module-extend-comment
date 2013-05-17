package fr.paris.lutece.plugins.extend.modules.comment.service;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.ICommentDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * Service to manage listeners over comments
 */
public class CommentListenerService
{
    private static Map<String, List<ICommentListener>> _mapListeners = new HashMap<String, List<ICommentListener>>( );

    @Inject
    private static ICommentDAO _commentDAO;

    /**
     * Register a comment listener.
     * @param strExtendableResourceType The extendable resource type associated
     *            with the listener.
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
    }

    /**
     * Notify to listeners the creation of a comment. Only listeners associated
     * with the extendable resource type of the comment are notified.
     * @param strExtendableResourceType The extendable resource type of the
     *            created comment
     * @param strIdExtendableResource The extendable resource id of the comment
     */
    public static void createComment( String strExtendableResourceType, String strIdExtendableResource )
    {
        List<ICommentListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners != null )
        {
            for ( ICommentListener listener : listListeners )
            {
                listener.createComment( strIdExtendableResource );
            }
        }
    }

    /**
     * Notify to listeners the modification of a comment. Only listeners
     * associated with the extendable resource type of the comment are notified.
     * @param nIdComment The id of the updated comment
     */
    public static void updateComment( int nIdComment )
    {
        if ( _mapListeners.size( ) > 0 )
        {
            Comment comment = _commentDAO.load( nIdComment, CommentPlugin.getPlugin( ) );
            updateComment( comment.getExtendableResourceType( ), comment.getIdExtendableResource( ) );
        }
    }

    /**
     * Notify to listeners the modification of a comment. Only listeners
     * associated with the extendable resource type of the comment are notified.
     * @param strExtendableResourceType The extendable resource type of the
     *            updated comment
     * @param strIdExtendableResource The extendable resource id of the comment
     */
    public static void updateComment( String strExtendableResourceType, String strIdExtendableResource )
    {
        List<ICommentListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners != null )
        {
            for ( ICommentListener listener : listListeners )
            {
                listener.updateComment( strIdExtendableResource );
            }
        }
    }

    /**
     * Notify to listeners the removal of a comment. Only listeners
     * associated with the extendable resource type of the comment are notified.
     * @param nIdComment The id of the removed comment
     */
    public static void deleteComment( int nIdComment )
    {
        if ( _mapListeners.size( ) > 0 )
        {
            Comment comment = _commentDAO.load( nIdComment, CommentPlugin.getPlugin( ) );
            deleteComment( comment.getExtendableResourceType( ), comment.getIdExtendableResource( ) );
        }
    }

    /**
     * Notify to listeners the modification of a comment. Only listeners
     * associated with the extendable resource type of the comment are notified.
     * @param strExtendableResourceType The extendable resource type of the
     *            removed comment
     * @param strIdExtendableResource The extendable resource id of the comment
     */
    public static void deleteComment( String strExtendableResourceType, String strIdExtendableResource )
    {
        List<ICommentListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners != null )
        {
            for ( ICommentListener listener : listListeners )
            {
                listener.deleteComment( strIdExtendableResource );
            }
        }
    }
}
