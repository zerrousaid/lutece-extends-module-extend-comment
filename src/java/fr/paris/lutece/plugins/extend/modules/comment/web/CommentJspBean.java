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
package fr.paris.lutece.plugins.extend.modules.comment.web;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.modules.comment.util.constants.CommentConstants;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.url.UrlItem;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 *
 * CommentJspBean
 *
 */
public class CommentJspBean extends PluginAdminPageJspBean
{
    // JSP
    private static final String JSP_VIEW_EXTENDER_INFO = "../../ViewExtenderInfo.jsp";
    private static final String JSP_URL_DO_REMOVE_COMMENT = "jsp/admin/plugins/extend/modules/comment/DoRemoveComment.jsp";

	// CONSTANT
	private static final String CONSTANT_AND = "&";
	private static final String CONSTANT_AND_HTML = "%26";

	private ICommentService _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );

	
    /**
     * Do publish unpublish comment.
     *
     * @param request the request
     * @return the string
     */
    public String doPublishUnpublishComment( HttpServletRequest request )
    {
        String strIdComment = request.getParameter( CommentConstants.PARAMETER_ID_COMMENT );

        if ( StringUtils.isNotBlank( strIdComment ) && StringUtils.isNumeric( strIdComment ) )
        {
            int nIdComment = Integer.parseInt( strIdComment );
            Comment comment = _commentService.findByPrimaryKey( nIdComment );

            if ( comment != null )
            {
                try
                {
                    _commentService.updateCommentStatus( comment.getIdComment(  ), !comment.isPublished(  ) );
                }
                catch ( Exception ex )
                {
                    // Something wrong happened... a database check might be needed
                    AppLogService.error( ex.getMessage(  ) + " when updating a comment", ex );

                    return AdminMessageService.getMessageUrl( request, CommentConstants.MESSAGE_ERROR_GENERIC_MESSAGE,
                        AdminMessage.TYPE_ERROR );
                }

                UrlItem url = new UrlItem( JSP_VIEW_EXTENDER_INFO );
                url.addParameter( CommentConstants.PARAMETER_EXTENDER_TYPE,
                    CommentResourceExtender.EXTENDER_TYPE_COMMENT );
                url.addParameter( CommentConstants.PARAMETER_ID_EXTENDABLE_RESOURCE, comment.getIdExtendableResource(  ) );
                url.addParameter( CommentConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE,
                    comment.getExtendableResourceType(  ) );
				url.addParameter( CommentConstants.PARAMETER_FROM_URL, StringUtils.replace( request.getParameter( CommentConstants.PARAMETER_FROM_URL ), CONSTANT_AND, CONSTANT_AND_HTML ) );

                return url.getUrl(  );
            }
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Gets the confirm remove comment.
     *
     * @param request the request
     * @return the confirm remove comment
     */
    public String getConfirmRemoveComment( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_COMMENT );
        url.addParameter( CommentConstants.PARAMETER_ID_COMMENT,
            request.getParameter( CommentConstants.PARAMETER_ID_COMMENT ) );
		url.addParameter( CommentConstants.PARAMETER_FROM_URL, StringUtils.replace( request.getParameter( CommentConstants.PARAMETER_FROM_URL ), CONSTANT_AND, CONSTANT_AND_HTML ) );

        return AdminMessageService.getMessageUrl( request, CommentConstants.MESSAGE_CONFIRM_REMOVE_COMMENT,
            url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Do remove comment.
     *
     * @param request the request
     * @return the string
     */
    public String doRemoveComment( HttpServletRequest request )
    {
        String strIdComment = request.getParameter( CommentConstants.PARAMETER_ID_COMMENT );

        if ( StringUtils.isNotBlank( strIdComment ) && StringUtils.isNumeric( strIdComment ) )
        {
            int nIdComment = Integer.parseInt( strIdComment );
            Comment comment = _commentService.findByPrimaryKey( nIdComment );

            if ( comment != null )
            {
                try
                {
                    _commentService.remove( nIdComment );
                }
                catch ( Exception ex )
                {
                    // Something wrong happened... a database check might be needed
                    AppLogService.error( ex.getMessage(  ) + " when updating a comment", ex );

                    return AdminMessageService.getMessageUrl( request, CommentConstants.MESSAGE_ERROR_GENERIC_MESSAGE,
                        AdminMessage.TYPE_ERROR );
                }

                UrlItem url = new UrlItem( JSP_VIEW_EXTENDER_INFO );
                url.addParameter( CommentConstants.PARAMETER_EXTENDER_TYPE,
                    CommentResourceExtender.EXTENDER_TYPE_COMMENT );
                url.addParameter( CommentConstants.PARAMETER_ID_EXTENDABLE_RESOURCE, comment.getIdExtendableResource(  ) );
                url.addParameter( CommentConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE,
                    comment.getExtendableResourceType(  ) );
				url.addParameter( CommentConstants.PARAMETER_FROM_URL, StringUtils.replace( request.getParameter( CommentConstants.PARAMETER_FROM_URL ), CONSTANT_AND, CONSTANT_AND_HTML ) );

                return url.getUrl(  );
            }
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }
}
