/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.extend.modules.comment.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.CommentFilter;
import fr.paris.lutece.plugins.extend.modules.comment.business.ICommentDAO;
import fr.paris.lutece.plugins.extend.modules.comment.util.constants.CommentConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;


/**
 * 
 * CommentService
 * 
 */
public class CommentService implements ICommentService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend-comment.commentService";
      
    @Inject
    private ICommentDAO _commentDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void create( Comment comment )
    {
        Timestamp currentTimestamp = new Timestamp( new Date( ).getTime( ) );
        comment.setDateComment( currentTimestamp );
        comment.setDateLastModif( currentTimestamp );
        _commentDAO.insert( comment, CommentPlugin.getPlugin( ) );
        if ( comment.getIdParentComment( ) > 0 )
        {
            Comment parentComment = findByPrimaryKey( comment.getIdParentComment( ) );
            parentComment.setDateLastModif( currentTimestamp );
            update( parentComment );
        }
        CommentListenerService.createComment( comment.getExtendableResourceType( ), comment.getIdExtendableResource( ),
                comment.isPublished( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void update( Comment comment )
    {
        Comment oldComment = findByPrimaryKey( comment.getIdComment( ) );
        comment.setDateLastModif( new Timestamp( new Date( ).getTime( ) ) );
        _commentDAO.store( comment, CommentPlugin.getPlugin( ) );
        if ( oldComment.isPublished( ) ^ comment.isPublished( ) )
        {
            CommentListenerService.publishComment( comment.getExtendableResourceType( ),
                    comment.getIdExtendableResource( ), comment.isPublished( ) );
        }
        //        if ( ( oldComment.isPublished( ) && !comment.isPublished( ) ) || !oldComment.isPublished( ) && comment.isPublished( ) ) )
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void updateCommentStatus( int nIdComment, boolean bPublished )
    {
        _commentDAO.updateCommentStatus( nIdComment, bPublished, CommentPlugin.getPlugin( ) );
        CommentListenerService.publishComment( nIdComment, bPublished );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void remove( int nIdComment )
    {
        if ( CommentListenerService.hasListener( ) )
        {
            Comment comment = findByPrimaryKey( nIdComment );
            List<Integer> listIdRemovedComments = new ArrayList<Integer>( );
            listIdRemovedComments.add( nIdComment );
            CommentListenerService.deleteComment( comment.getExtendableResourceType( ),
                    comment.getIdExtendableResource( ), listIdRemovedComments );
        }
        _commentDAO.delete( nIdComment, CommentPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void removeByResource( String strIdExtendableResource, String strExtendableResourceType )
    {
        if ( CommentListenerService.hasListener( ) )
        {
            List<Integer> listRemovedComments = findIdsByResource( strIdExtendableResource, strExtendableResourceType,
                    false );
            CommentListenerService.deleteComment( strExtendableResourceType, strIdExtendableResource,
                    listRemovedComments );
        }
        _commentDAO.deleteByResource( strIdExtendableResource, strExtendableResourceType, CommentPlugin.getPlugin( ) );
    }

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment findByPrimaryKey( int nIdComment )
    {
        return _commentDAO.load( nIdComment, CommentPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdsByResource( String strIdExtendableResource, String strExtendableResourceType,
            boolean bPublishedOnly )
    {
        return _commentDAO.findIdsByResource( strIdExtendableResource, strExtendableResourceType, bPublishedOnly,
                CommentPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findByResource( String strIdExtendableResource, String strExtendableResourceType,
            boolean bPublishedOnly, boolean bAscSort )
    {
    	
    	CommentFilter commentFilter=new CommentFilter();
    	if(bPublishedOnly)
    	{
    		commentFilter.setCommentState(Comment.COMMENT_STATE_PUBLISHED);
    	}
    
    	commentFilter.setAscSort(bAscSort);
    	
    	return _commentDAO.findParentCommentsByResource( strIdExtendableResource, strExtendableResourceType,
        		commentFilter, 0, 0, CommentPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCommentNb( String strIdExtendableResource, String strExtendableResourceType, boolean bParentsOnly,
            boolean bPublishedOnly )
    {
        return _commentDAO.getCommentNb( strIdExtendableResource, strExtendableResourceType, bParentsOnly,
                bPublishedOnly, CommentPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findLastComments( String strIdExtendableResource, String strExtendableResourceType,
            int nNbComments, boolean bPublishedOnly, boolean bParentsOnly, boolean bGetNumberSubComments )
    {
        Plugin plugin = CommentPlugin.getPlugin( );
        List<Comment> listComments = _commentDAO.selectLastComments( strIdExtendableResource,
                strExtendableResourceType, nNbComments, bPublishedOnly, bParentsOnly, plugin );
        if ( bGetNumberSubComments )
        {
            for ( Comment comment : listComments )
            {
                comment.setNumberSubComments( _commentDAO.countByIdParent( comment.getIdComment( ), bPublishedOnly,
                        plugin ) );
            }
        }
        return listComments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findByResource( String strIdExtendableResource, String strExtendableResourceType,
            boolean bPublishedOnly, String strSortedAttributeName, boolean bAscSort, int nItemsOffset,
            int nMaxItemsNumber, boolean bLoadSubComments )
    {
    	
    	CommentFilter commentFilter=new CommentFilter();
    	if(bPublishedOnly)
    	{
    		commentFilter.setCommentState(Comment.COMMENT_STATE_PUBLISHED);
    	}
    	commentFilter.setSortedAttributeName(strSortedAttributeName);
    	commentFilter.setAscSort(bAscSort);
        return findByResource(strIdExtendableResource, strExtendableResourceType, commentFilter, nItemsOffset, nMaxItemsNumber, bLoadSubComments);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findByResource( String strIdExtendableResource, String strExtendableResourceType,
    		CommentFilter commentFilter, int nItemsOffset,
            int nMaxItemsNumber, boolean bLoadSubComments )
    {
    	
    	
        List<Comment> listComments = _commentDAO.findParentCommentsByResource( strIdExtendableResource,
                strExtendableResourceType, commentFilter, nItemsOffset,
                nMaxItemsNumber, CommentPlugin.getPlugin( ) );
        if ( bLoadSubComments )
        {
            for ( Comment comment : listComments )
            {
                comment.setListSubComments( this.findByIdParent( comment.getIdComment( ), commentFilter.getCommentState()!= null && commentFilter.getCommentState().equals(Comment.COMMENT_STATE_PUBLISHED),
                        commentFilter.getSortedAttributeName(), commentFilter.getAscSort() ) );
            }
        }

        return listComments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findByIdParent( int nIdParent, boolean bPublishedOnly )
    {
        return this.findByIdParent( nIdParent, bPublishedOnly, null, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findByIdParent( int nIdParent, boolean bPublishedOnly, String strSortedAttributeName,
            boolean bAscSort )
    {
    	CommentFilter commentFilter=new CommentFilter();
    	
    	if(bPublishedOnly)
    	{
    		commentFilter.setCommentState(Comment.COMMENT_STATE_PUBLISHED);
    	}
    	commentFilter.setSortedAttributeName(strSortedAttributeName);
    	commentFilter.setAscSort(bAscSort);
       
        return _commentDAO.findByIdParent( nIdParent,commentFilter,
                CommentPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByIdParent( int nIdParent, boolean bPublishedOnly )
    {
        return _commentDAO.countByIdParent( nIdParent, bPublishedOnly, CommentPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdMostCommentedResources( String strExtendableResourceType, boolean bPublishedOnly,
            int nItemsOffset, int nMaxItemsNumber )
    {
        return _commentDAO.findIdMostCommentedResources( strExtendableResourceType, bPublishedOnly, nItemsOffset,
                nMaxItemsNumber, CommentPlugin.getPlugin( ) );
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getRefListCommentStates(Locale locale )
    {
        ReferenceList refListDiggSubmitState = new ReferenceList(  );
        refListDiggSubmitState.addItem(  "", I18nService.getLocalizedString(CommentConstants.PROPERTY_COMMENT_ALL_STATE ,locale) );
        refListDiggSubmitState.addItem( Comment.COMMENT_STATE_PUBLISHED, I18nService.getLocalizedString(CommentConstants.PROPERTY_COMMENT_STATE_PUBLISHED ,locale));
        refListDiggSubmitState.addItem( Comment.COMMENT_STATE_UN_PUBLISHED, I18nService.getLocalizedString(CommentConstants.PROPERTY_COMMENT_STATE_UN_PUBLISHED,locale ));
        return refListDiggSubmitState;
    }
}
