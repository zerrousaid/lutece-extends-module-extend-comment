/*
 * Copyright (c) 2002-2020, City of Paris
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

/**
 * 
 * CommentFilter
 *
 */
public class CommentFilter
{

    private Integer _nCommentState;
    private String _strSortedAttributeName;
    private Boolean _bAscSort;
    private Boolean _bPinned;
    private Boolean _bImportant;
    private String _strLuteceUserName;
    private String _strExtendableResourceType;

    /**
     * *
     * 
     * @return the comment state set if the results must contain only the comments which are in the state specify in parameters
     */
    public Integer getCommentState( )
    {
        return _nCommentState;
    }

    /**
     * 
     * @param nCommentState
     *            set if the results must contain only the comments which are in the state specify in parameters
     */
    public void setCommentState( Integer nCommentState )
    {
        this._nCommentState = nCommentState;
    }

    /**
     * 
     * @return the sorted attribute name
     */
    public String getSortedAttributeName( )
    {
        return _strSortedAttributeName;
    }

    /**
     * 
     * @param _strSortedAttributeName
     *            the sorted attribute name
     */
    public void setSortedAttributeName( String _strSortedAttributeName )
    {
        this._strSortedAttributeName = _strSortedAttributeName;
    }

    /**
     * 
     * @return true if the result must be sorted by asc sort
     */
    public Boolean getAscSort( )
    {
        return _bAscSort;
    }

    /**
     * 
     * @param _bAscSort
     *            true if the result must be sorted by asc sort
     */
    public void setAscSort( Boolean _bAscSort )
    {
        this._bAscSort = _bAscSort;
    }

    /**
     * 
     * @return return true if the results must contain only the comments pinned false if the results must contain only the comments not pinned
     * 
     */
    public Boolean getPinned( )
    {
        return _bPinned;
    }

    /**
     * 
     * @param _bPinned
     *            true if the results must contain only the comments pinned false if the results must contain only the comments not pinned
     */
    public void setPinned( Boolean _bPinned )
    {
        this._bPinned = _bPinned;
    }

    /**
     * 
     * @return true if the results must contain only the comments Important false if the results must contain only the comments not Important
     */
    public Boolean getImportant( )
    {
        return _bImportant;
    }

    /**
     * 
     * @param _bImportant
     *            true if the results must contain only the comments Important false if the results must contain only the comments not Important
     */
    public void setImportant( Boolean _bImportant )
    {
        this._bImportant = _bImportant;
    }

    /**
     * 
     * @return the lutece user name
     */
    public String getLuteceUserName( )
    {
        return _strLuteceUserName;
    }

    /**
     * set the lutece user name
     * 
     * @param _strLuteceUserName
     *            the lutece user name
     */
    public void setLuteceUserName( String _strLuteceUserName )
    {
        this._strLuteceUserName = _strLuteceUserName;
    }

    /**
     * @return the extendableResourceType
     */
    public String getExtendableResourceType( )
    {
        return _strExtendableResourceType;
    }

    /**
     * @param strExtendableResourceType
     *            the extendableResourceType to set
     */
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

}
