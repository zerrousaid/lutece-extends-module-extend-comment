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
package fr.paris.lutece.plugins.extend.modules.comment.business.config;

import fr.paris.lutece.plugins.extend.business.extender.config.ExtenderConfig;


/**
 * 
 * CommentExtenderConfig
 * 
 */
public class CommentExtenderConfig extends ExtenderConfig
{
    private boolean _bIsModerated;
    private int _nNbComments;
    private int _nIdMailingList = -1;
    private boolean _bAuthorizeSubComments;
    private boolean _bUseBBCodeEditor;
    private String _strAdminBadge;
    private String _strMessageCommentCreated;
    private int _nAddCommentPosition;
    private boolean _bEnabledAuthMode;
    private boolean _bDisplaySubComments;
    private boolean _bDeleteComments;


	/**
     * Checks if is moderated.
     * 
     * @return true, if is moderated
     */
    public boolean isModerated( )
    {
        return _bIsModerated;
    }

    /**
     * Sets the moderated.
     * 
     * @param bModerated the new moderated
     */
    public void setModerated( boolean bModerated )
    {
        _bIsModerated = bModerated;
    }

    /**
     * @return the nNbComments
     */
    public int getNbComments( )
    {
        return _nNbComments;
    }

    /**
     * @param nNbComments the nNbComments to set
     */
    public void setNbComments( int nNbComments )
    {
        _nNbComments = nNbComments;
    }

    /**
     * @return the nIdMailingList
     */
    public int getIdMailingList( )
    {
        return _nIdMailingList;
    }

    /**
     * @param nIdMailingList the nIdMailingList to set
     */
    public void setIdMailingList( int nIdMailingList )
    {
        _nIdMailingList = nIdMailingList;
    }

    /**
     * Check if sub comments are authorized for this extender
     * @return True if sub comments are authorized, false otherwise
     */
    public boolean getAuthorizeSubComments( )
    {
        return _bAuthorizeSubComments;
    }

    /**
     * Authorize or unauthorize sub comments for this extender
     * @param bAuthorizeSubComments True if sub comments are authorized, false
     *            otherwise
     */
    public void setAuthorizeSubComments( boolean bAuthorizeSubComments )
    {
        this._bAuthorizeSubComments = bAuthorizeSubComments;
    }

    /**
     * Check if a BBCode editor should be used to compose comments
     * @return True if a BBCode should be used to compose comments, false
     *         otherwise
     */
    public boolean getUseBBCodeEditor( )
    {
        return _bUseBBCodeEditor;
    }

    /**
     * Set the usage of a BBCode editor compose comments
     * @param bUseBBCodeEditor True to use a BBCode editor to compose comments,
     *            false otherwise
     */
    public void setUseBBCodeEditor( boolean bUseBBCodeEditor )
    {
        this._bUseBBCodeEditor = bUseBBCodeEditor;
    }

    /**
     * Get the badge of comments posted by admin users
     * @return The badge of comments posted by admin users
     */
    public String getAdminBadge( )
    {
        return _strAdminBadge;
    }

    /**
     * Set the badge of comments posted by admin users
     * @param strAdminAnswerBadge The badge of comments posted by admin users
     */
    public void setAdminBadge( String strAdminAnswerBadge )
    {
        this._strAdminBadge = strAdminAnswerBadge;
    }

    /**
     * Get the message to display to users when a comment is created
     * @return The message to display
     */
    public String getMessageCommentCreated( )
    {
        return _strMessageCommentCreated;
    }

    /**
     * Set the message to display to users when a comment is created
     * @param strMessageCommentCreated The message to display
     */
    public void setMessageCommentCreated( String strMessageCommentCreated )
    {
        this._strMessageCommentCreated = strMessageCommentCreated;
    }

    /**
     * @returnthe position number of the add comment form
     */
    public int getAddCommentPosition()
    {
    	return _nAddCommentPosition;
    }
    
    /**
     * * @param the position number
     * @return void
     */
    public void setAddCommentPosition (int nPosition)
    {
    	_nAddCommentPosition = nPosition;
    }
    
    /**
     * 
     * @return isEnableAuthMode true if the authentication mode is enable
     */
	public boolean isEnabledAuthMode() {
		return _bEnabledAuthMode;
	}
	
	/**
	 * 
	 * @param bEnableAuthMode  true if the authentication mode is enable
	 */
	public void setEnabledAuthMode(boolean bEnableAuthMode) {
		this._bEnabledAuthMode = bEnableAuthMode;
	}
	
	/**
	 * 
	 * @return isDisplaySubComments true if the display sub comments mode is enable 
	 */
	public boolean isDisplaySubComments( )
	{
		return _bDisplaySubComments;
	}
	
	/**
	 * 
	 * @param bDisplaySubComments true if the display sub comments mode is enable
	 */
	public void setDisplaySubComments( boolean bDisplaySubComments )
	{
		this._bDisplaySubComments = bDisplaySubComments;
	}

	/**
	 * Returns the DeleteComments option
	 * @return True if the the option is enabled
	 */
    public boolean getDeleteComments()
    {
        return _bDeleteComments;
    }

    /**
     * Sets the DeleteComments option
     * @param bDeleteComments The DeleteComments option
     */
    public void setDeleteComments( boolean bDeleteComments )
    {
        _bDeleteComments = bDeleteComments;
    }
}
