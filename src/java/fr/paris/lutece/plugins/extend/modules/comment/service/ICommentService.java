/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import java.util.List;

import org.springframework.transaction.annotation.Transactional;


/**
 * ICommentService.
 */
public interface ICommentService
{
    /**
     * Delete.
     *
     * @param nIdComment the n id comment
     */
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    void remove( int nIdComment );

    /**
     * Delete by resource
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     */
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    void removeByResource( String strIdExtendableResource, String strExtendableResourceType );

    /**
     * Insert.
     *
     * @param comment the comment
     */
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    void create( Comment comment );

    /**
     * Store.
     *
     * @param comment the comment
     */
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    void update( Comment comment );

    /**
     * Update comment status.
     *
     * @param nIdComment the n id comment
     * @param bPublished the b published
     */
    @Transactional( CommentPlugin.TRANSACTION_MANAGER )
    void updateCommentStatus( int nIdComment, boolean bPublished );

    /**
     * Load.
     *
     * @param nIdComment the n id comment
     * @return the comment
     */
    Comment findByPrimaryKey( int nIdComment );

	/**
	 * Select by resource.
	 * 
	 * @param strIdExtendableResource the str id extendable resource
	 * @param strExtendableResourceType the str extendable resource type
	 * @param bPublishedOnly the b only published
	 * @param bAscSort True if comments should be sorted ascendantly, false otherwise
	 * @return the list
	 */
    List<Comment> findByResource( String strIdExtendableResource, String strExtendableResourceType,
 boolean bPublishedOnly, boolean bAscSort );

    /**
     * Check comment nb.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the int
     */
    int getCommentNb( String strIdExtendableResource, String strExtendableResourceType );

    /**
     * Load last comments.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param nNbComments the n nb commentss
     * @param bPublishedOnly the b published only
     * @return the list
     */
    List<Comment> findLastComments( String strIdExtendableResource, String strExtendableResourceType, int nNbComments,
        boolean bPublishedOnly );
}
