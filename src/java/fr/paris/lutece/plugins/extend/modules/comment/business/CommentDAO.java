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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Comment objects.
 */
public class CommentDAO implements ICommentDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_comment ) FROM extend_comment ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_comment ( id_comment, id_resource, resource_type, date_comment, " +
        " name, email, ip_address, comment, is_published ) " + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_comment, id_resource, resource_type, date_comment, name, email, ip_address, comment, is_published " +
        " FROM extend_comment ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE id_comment = ? ";
    private static final String SQL_QUERY_SELECT_BY_RESOURCE = SQL_QUERY_SELECT_ALL +
        " WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_SELECT_NB_COMMENT_BY_RESOURCE = " SELECT count(*) FROM extend_comment WHERE id_resource = ?, resource_type = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_comment WHERE id_comment = ? ";
    private static final String SQL_QUERY_DELETE_BY_ID_RESOURCE = " DELETE FROM extend_comment WHERE resource_type = ? ";
    private static final String SQL_QUERY_FILTER_ID_RESOURCE = " AND id_resource = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE extend_comment SET id_resource = ?, resource_type = ?, date_comment = ?, name = ?, email = ?, " +
        " ip_address = ?, comment = ?, is_published = ? WHERE id_comment = ?  ";
    private static final String SQL_QUERY_UPDATE_COMMENT_PUBLISHED = " UPDATE extend_comment SET is_published = ? WHERE id_comment = ?  ";
    private static final String SQL_ORDER_BY_DATE_COMMENT = " ORDER BY date_comment ";
    private static final String SQL_FILTER_STATUS_PUBLISHED = " is_published = 1 ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_ASC = " ASC ";
    private static final String SQL_DESC = " DESC ";
    private static final String SQL_LIMIT = " LIMIT ";

    /**
     * Generates a new primary key.
     *
     * @param plugin the plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( Comment comment, Plugin plugin )
    {
        int nNewPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        comment.setIdComment( nNewPrimaryKey );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, comment.getIdComment(  ) );
        daoUtil.setString( nIndex++, comment.getIdExtendableResource(  ) );
        daoUtil.setString( nIndex++, comment.getExtendableResourceType(  ) );
        daoUtil.setTimestamp( nIndex++, comment.getDateComment(  ) );
        daoUtil.setString( nIndex++, comment.getName(  ) );
        daoUtil.setString( nIndex++, comment.getEmail(  ) );
        daoUtil.setString( nIndex++, comment.getIpAddress(  ) );
        daoUtil.setString( nIndex++, comment.getComment(  ) );
        daoUtil.setBoolean( nIndex, comment.isPublished(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comment load( int nIdComment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdComment );
        daoUtil.executeQuery(  );

        Comment comment = null;

        if ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            comment = new Comment(  );
            comment.setIdComment( daoUtil.getInt( nIndex++ ) );
            comment.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            comment.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            comment.setDateComment( daoUtil.getTimestamp( nIndex++ ) );
            comment.setName( daoUtil.getString( nIndex++ ) );
            comment.setEmail( daoUtil.getString( nIndex++ ) );
            comment.setIpAddress( daoUtil.getString( nIndex++ ) );
            comment.setComment( daoUtil.getString( nIndex++ ) );
            comment.setPublished( daoUtil.getBoolean( nIndex ) );
        }

        daoUtil.free(  );

        return comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdComment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdComment );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin )
    {
        int nIndex = 1;
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_DELETE_BY_ID_RESOURCE );
        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdExtendableResource ) )
        {
            sbSql.append( SQL_QUERY_FILTER_ID_RESOURCE );
        }
        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
        daoUtil.setString( nIndex++, strExtendableResourceType );
        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdExtendableResource ) )
        {
            daoUtil.setString( nIndex, strIdExtendableResource );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Comment comment, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( nIndex++, comment.getIdExtendableResource(  ) );
        daoUtil.setString( nIndex++, comment.getExtendableResourceType(  ) );
        daoUtil.setTimestamp( nIndex++, comment.getDateComment(  ) );
        daoUtil.setString( nIndex++, comment.getName(  ) );
        daoUtil.setString( nIndex++, comment.getEmail(  ) );
        daoUtil.setString( nIndex++, comment.getIpAddress(  ) );
        daoUtil.setString( nIndex++, comment.getComment(  ) );
        daoUtil.setBoolean( nIndex++, comment.isPublished(  ) );

        daoUtil.setInt( nIndex, comment.getIdComment(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> selectByResource( String strIdExtendableResource, String strExtendableResourceType,
 boolean bPublishedOnly, boolean bAscSort, Plugin plugin )
    {
        List<Comment> listComments = new ArrayList<Comment>(  );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_BY_RESOURCE );

		String strSortOrder;
		if ( bAscSort )
        {
			strSortOrder = SQL_ASC;
        }
        else
        {
			strSortOrder = SQL_DESC;
		}
		if ( bPublishedOnly )
		{
			sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }
		sbSQL.append( SQL_ORDER_BY_DATE_COMMENT ).append( strSortOrder );

        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ), plugin );
        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nIndex = 1;

            Comment comment = new Comment(  );
            comment.setIdComment( daoUtil.getInt( nIndex++ ) );
            comment.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            comment.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            comment.setDateComment( daoUtil.getTimestamp( nIndex++ ) );
            comment.setName( daoUtil.getString( nIndex++ ) );
            comment.setEmail( daoUtil.getString( nIndex++ ) );
            comment.setIpAddress( daoUtil.getString( nIndex++ ) );
            comment.setComment( daoUtil.getString( nIndex++ ) );
            comment.setPublished( daoUtil.getBoolean( nIndex ) );

            listComments.add( comment );
        }

        daoUtil.free(  );

        return listComments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCommentStatus( int nIdComment, boolean bPublished, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_COMMENT_PUBLISHED, plugin );
        daoUtil.setBoolean( nIndex++, bPublished );
        daoUtil.setInt( nIndex, nIdComment );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCommentNb( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NB_COMMENT_BY_RESOURCE, plugin );
        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery(  );

        int nCount = 0;

        if ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> selectLastComments( String strIdExtendableResource, String strExtendableResourceType,
        int nNbComments, boolean bPublishedOnly, Plugin plugin )
    {
        List<Comment> listComments = new ArrayList<Comment>(  );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_BY_RESOURCE );

        if ( bPublishedOnly )
        {
            sbSQL.append( SQL_AND ).append( SQL_FILTER_STATUS_PUBLISHED );
        }

        sbSQL.append( SQL_ORDER_BY_DATE_COMMENT ).append( SQL_DESC );
        sbSQL.append( SQL_LIMIT ).append( nNbComments );

        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ), plugin );
        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nIndex = 1;

            Comment comment = new Comment(  );
            comment.setIdComment( daoUtil.getInt( nIndex++ ) );
            comment.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            comment.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            comment.setDateComment( daoUtil.getTimestamp( nIndex++ ) );
            comment.setName( daoUtil.getString( nIndex++ ) );
            comment.setEmail( daoUtil.getString( nIndex++ ) );
            comment.setIpAddress( daoUtil.getString( nIndex++ ) );
            comment.setComment( daoUtil.getString( nIndex++ ) );
            comment.setPublished( daoUtil.getBoolean( nIndex ) );

            listComments.add( comment );
        }

        daoUtil.free(  );

        return listComments;
    }
}
