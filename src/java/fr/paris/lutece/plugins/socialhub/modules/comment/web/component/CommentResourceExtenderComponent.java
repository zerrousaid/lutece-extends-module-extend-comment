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
package fr.paris.lutece.plugins.socialhub.modules.comment.web.component;

import fr.paris.lutece.plugins.socialhub.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.socialhub.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.socialhub.modules.comment.business.Comment;
import fr.paris.lutece.plugins.socialhub.modules.comment.business.config.CommentExtenderConfig;
import fr.paris.lutece.plugins.socialhub.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.socialhub.modules.comment.util.constants.CommentConstants;
import fr.paris.lutece.plugins.socialhub.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.socialhub.util.SocialHubErrorException;
import fr.paris.lutece.plugins.socialhub.web.component.AbstractResourceExtenderComponent;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * CommentResourceExtenderComponent
 *
 */
public class CommentResourceExtenderComponent extends AbstractResourceExtenderComponent
{
    // TEMPLATES
    private static final String TEMPLATE_COMMENT = "skin/plugins/socialhub/modules/comment/comment.html";
    private static final String TEMPLATE_COMMENT_CONFIG = "admin/plugins/socialhub/modules/comment/comment_config.html";
    private static final String TEMPLATE_MANAGE_COMMENTS = "admin/plugins/socialhub/modules/comment/comment_info.html";
    @Inject
    private ICommentService _commentService;
    @Inject
    @Named( CommentConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildXmlAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        StringBuffer strXml )
    {
        // Nothing yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        HttpServletRequest request )
    {
        CommentExtenderConfig config = _configService.find( getResourceExtender(  ).getKey(  ),
                strIdExtendableResource, strExtendableResourceType );
        int nNbComments = 1;

        if ( config != null )
        {
            nNbComments = config.getNbComments(  );
        }

        List<Comment> listComments = _commentService.findLastComments( strIdExtendableResource,
                strExtendableResourceType, nNbComments, true );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( CommentConstants.MARK_LIST_COMMENTS, listComments );
        model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMMENT, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        ReferenceList listIdsMailingList = new ReferenceList(  );
        listIdsMailingList.addItem( -1,
            I18nService.getLocalizedString( CommentConstants.PROPERTY_COMMENT_CONFIG_LABEL_NO_MAILING_LIST, locale ) );
        listIdsMailingList.addAll( AdminMailingListService.getMailingLists( AdminUserService.getAdminUser( request ) ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( CommentConstants.MARK_COMMENT_CONFIG, _configService.find( resourceExtender.getIdExtender(  ) ) );
        model.put( CommentConstants.MARK_LIST_IDS_MAILING_LIST, listIdsMailingList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMMENT_CONFIG, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IExtenderConfig getConfig( int nIdExtender )
    {
        return _configService.find( nIdExtender );
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public String getInfoHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        if ( resourceExtender != null )
        {
            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( CommentConstants.MARK_LIST_COMMENTS,
                _commentService.findByResource( resourceExtender.getIdExtendableResource(  ),
                    resourceExtender.getExtendableResourceType(  ), false ) );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_COMMENTS, request.getLocale(  ),
                    model );

            return template.getHtml(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveConfig( HttpServletRequest request, IExtenderConfig config )
        throws SocialHubErrorException
    {
        _configService.update( config );
    }
}
