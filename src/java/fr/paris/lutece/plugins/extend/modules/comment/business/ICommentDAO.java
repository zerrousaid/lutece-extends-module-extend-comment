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
package fr.paris.lutece.plugins.extend.modules.comment.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * ICommentDAO.
 */
public interface ICommentDAO
{
    /**
     * Delete.
     *
     * @param nIdComment the n id comment
     * @param plugin the plugin
     */
    void delete( int nIdComment, Plugin plugin );

    /**
     * Delete by id hub resource.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param plugin the plugin
     */
    void deleteByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );

    /**
     * Insert.
     *
     * @param comment the comment
     * @param plugin the plugin
     */
    void insert( Comment comment, Plugin plugin );

    /**
     * Load.
     *
     * @param nIdComment the n id comment
     * @param plugin the plugin
     * @return the comment
     */
    Comment load( int nIdComment, Plugin plugin );

    /**
     * Store.
     *
     * @param comment the comment
     * @param plugin the plugin
     */
    void store( Comment comment, Plugin plugin );

    /**
     * Select by id hub resource.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param bPublishedOnly the b published
     * @param plugin the plugin
     * @return the list
     */
    List<Comment> selectByResource( String strIdExtendableResource, String strExtendableResourceType,
        boolean bPublishedOnly, Plugin plugin );

    /**
     * Update comment status.
     *
     * @param nIdComment the n id comment
     * @param bPublished the b published
     * @param plugin the plugin
     */
    void updateCommentStatus( int nIdComment, boolean bPublished, Plugin plugin );

    /**
     * Check comment nb.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param plugin the plugin
     * @return the int
     */
    int getCommentNb( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );

    /**
     * Load last comments.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param nNbComments the n nb comments
     * @param bPublishedOnly the b published only
     * @param plugin the plugin
     * @return the list
     */
    List<Comment> selectLastComments( String strIdExtendableResource, String strExtendableResourceType,
        int nNbComments, boolean bPublishedOnly, Plugin plugin );
}
