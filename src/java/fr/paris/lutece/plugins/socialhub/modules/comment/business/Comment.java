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
package fr.paris.lutece.plugins.socialhub.modules.comment.business;

import fr.paris.lutece.plugins.socialhub.modules.comment.util.annotation.Email;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;


/**
 *
 * Comment
 *
 */
public class Comment
{
    private int _nIdComment;
    @NotNull
    private String _strIdExtendableResource;
    @NotNull
    private String _strExtendableResourceType;
    private Timestamp _dateComment;
    private String _strName;
    @Email
    private String _strEmail;
    private String _strIpAddress;
    private String _strComment;
    private boolean _bIsPublished;

    /**
     * @return the strIdExtendableResource
     */
    public String getIdExtendableResource(  )
    {
        return _strIdExtendableResource;
    }

    /**
     * @param strIdExtendableResource the strIdExtendableResource to set
     */
    public void setIdExtendableResource( String strIdExtendableResource )
    {
        _strIdExtendableResource = strIdExtendableResource;
    }

    /**
     * @return the extendableResourceType
     */
    public String getExtendableResourceType(  )
    {
        return _strExtendableResourceType;
    }

    /**
     * @param strExtendableResourceType the extendableResourceType to set
     */
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
    * Returns the date of the comment
    * @return the date of the comment
    */
    public Timestamp getDateComment(  )
    {
        return _dateComment;
    }

    /**
     * Sets the date of the comment
     * @param dateComment the new date
     */
    public void setDateComment( Timestamp dateComment )
    {
        _dateComment = dateComment;
    }

    /**
    * Returns the identifier of the object
     * @return The identifier of the object
     */
    public int getIdComment(  )
    {
        return _nIdComment;
    }

    /**
     * Sets the identifier of the object
     * @param nIdComment the new identifier
     */
    public void setIdComment( int nIdComment )
    {
        _nIdComment = nIdComment;
    }

    /**
     * Gets the checks if is published.
     *
     * @return the checks if is published
     */
    public boolean isPublished(  )
    {
        return _bIsPublished;
    }

    /**
     * Sets the published.
     *
     * @param bIsPublished the new published
     */
    public void setPublished( boolean bIsPublished )
    {
        _bIsPublished = bIsPublished;
    }

    /**
     * Returns the comment
     * @return the comment
     */
    public String getComment(  )
    {
        return _strComment;
    }

    /**
     * Sets the comment
     * @param strComment the new comment
     */
    public void setComment( String strComment )
    {
        _strComment = strComment;
    }

    /**
     * Returns the email of the user
     * @return the email of the user
     */
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Sets the email of the user
     * @param strEmail the new email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * Returns the IP address of the user
     * @return the IP address of the user
     */
    public String getIpAddress(  )
    {
        return _strIpAddress;
    }

    /**
     * Sets the IP address of the user
     * @param strIpAddress the new IP address
     */
    public void setIpAddress( String strIpAddress )
    {
        _strIpAddress = strIpAddress;
    }

    /**
     * Returns the name of the user
     * @return the name of the user
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the user
     * @param strName the new name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }
}
