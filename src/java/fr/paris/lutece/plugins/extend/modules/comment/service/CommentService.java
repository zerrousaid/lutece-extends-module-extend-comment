/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.ICommentDAO;

import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;


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
        _commentDAO.insert( comment, CommentPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void update( Comment comment )
    {
        _commentDAO.store( comment, CommentPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void updateCommentStatus( int nIdComment, boolean bPublished )
    {
        _commentDAO.updateCommentStatus( nIdComment, bPublished, CommentPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void remove( int nIdComment )
    {
        _commentDAO.delete( nIdComment, CommentPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    public void removeByResource( String strIdExtendableResource, String strExtendableResourceType )
    {
        _commentDAO.deleteByResource( strIdExtendableResource, strExtendableResourceType, CommentPlugin.getPlugin(  ) );
    }

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment findByPrimaryKey( int nIdComment )
    {
        return _commentDAO.load( nIdComment, CommentPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findByResource( String strIdExtendableResource, String strExtendableResourceType,
 boolean bPublishedOnly, boolean bAscSort )
    {
		return _commentDAO.selectByResource( strIdExtendableResource, strExtendableResourceType, bPublishedOnly, bAscSort,
            CommentPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCommentNb( String strIdExtendableResource, String strExtendableResourceType )
    {
        return _commentDAO.getCommentNb( strIdExtendableResource, strExtendableResourceType, CommentPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findLastComments( String strIdExtendableResource, String strExtendableResourceType,
        int nNbComments, boolean bPublishedOnly )
    {
        return _commentDAO.selectLastComments( strIdExtendableResource, strExtendableResourceType, nNbComments,
            bPublishedOnly, CommentPlugin.getPlugin(  ) );
    }
}
