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
package fr.paris.lutece.plugins.extend.modules.comment.web.component;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.config.CommentExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentPlugin;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.modules.comment.util.constants.CommentConstants;
import fr.paris.lutece.plugins.extend.service.content.ExtendableContentPostProcessor;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.web.component.AbstractResourceExtenderComponent;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.content.ContentPostProcessor;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedDelegatePaginator;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.IPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * CommentResourceExtenderComponent
 * 
 */
public class CommentResourceExtenderComponent extends AbstractResourceExtenderComponent
{
    // TEMPLATES
    private static final String TEMPLATE_COMMENT = "skin/plugins/extend/modules/comment/comment.html";
    private static final String TEMPLATE_COMMENT_CONFIG = "admin/plugins/extend/modules/comment/comment_config.html";
    private static final String TEMPLATE_MANAGE_COMMENTS = "admin/plugins/extend/modules/comment/comment_info.html";

    private static final String JSP_URL_MANAGE_COMMENTS = "jsp/admin/plugins/extend/ViewExtenderInfo.jsp";

    @Inject
    private ICommentService _commentService;
    @Inject
    @Named( CommentConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;

    private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt(
            CommentConstants.PROPERTY_DEFAULT_LIST_COMMENTS_PER_PAGE, 50 );

    private ContentPostProcessor _contentPostProcessor;

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
        CommentExtenderConfig config = _configService.find( getResourceExtender( ).getKey( ), strIdExtendableResource,
                strExtendableResourceType );
        int nNbComments = 1;
        boolean bAuthorizedsubComments = true;
        boolean bUseBBCodeEditor = false;
        String strAdminBadge = StringUtils.EMPTY;
        if ( config != null )
        {
            nNbComments = config.getNbComments( );
            bAuthorizedsubComments = config.getAuthorizeSubComments( );
            bUseBBCodeEditor = config.getUseBBCodeEditor( );
            strAdminBadge = config.getAdminBadge( );
        }

        List<Comment> listComments = _commentService.findLastComments( strIdExtendableResource,
                strExtendableResourceType, nNbComments, true, true, bAuthorizedsubComments );
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( CommentConstants.MARK_LIST_COMMENTS, listComments );
        model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        model.put( CommentConstants.MARK_USE_BBCODE, bUseBBCodeEditor );
        model.put( CommentConstants.MARK_ADMIN_BADGE, strAdminBadge );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMMENT, request.getLocale( ), model );
        String strContent = template.getHtml( );

        ContentPostProcessor postProcessor = getExtendPostProcessor( );
        if ( postProcessor != null )
        {
            strContent = postProcessor.process( request, strContent );
        }

        return strContent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        ReferenceList listIdsMailingList = new ReferenceList( );
        listIdsMailingList
                .addItem( -1, I18nService.getLocalizedString(
                        CommentConstants.PROPERTY_COMMENT_CONFIG_LABEL_NO_MAILING_LIST, locale ) );
        listIdsMailingList.addAll( AdminMailingListService.getMailingLists( AdminUserService.getAdminUser( request ) ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( CommentConstants.MARK_COMMENT_CONFIG, _configService.find( resourceExtender.getIdExtender( ) ) );
        model.put( CommentConstants.MARK_LIST_IDS_MAILING_LIST, listIdsMailingList );
        model.put( CommentConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( CommentConstants.MARK_LOCALE, AdminUserService.getLocale( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMMENT_CONFIG, request.getLocale( ), model );

        return template.getHtml( );
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
            // We save in session the post back URL
            String strPostBackUrl = getPostBackUrl( request );
            request.getSession( ).setAttribute(
                    CommentPlugin.PLUGIN_NAME + CommentConstants.SESSION_COMMENT_ADMIN_POST_BACK_URL, strPostBackUrl );

            CommentExtenderConfig config = _configService.find( getResourceExtender( ).getKey( ),
                    resourceExtender.getIdExtendableResource( ), resourceExtender.getExtendableResourceType( ) );

            // We get the pagination info from the session
            Integer nItemsPerPage = _nDefaultItemsPerPage;
            String strCurrentPageIndex = CommentConstants.CONSTANT_FIRST_PAGE_NUMBER;
            Boolean bIsAscSort = false;
            String strSortedAttributeName = null;
            Object object = request.getSession( ).getAttribute( CommentConstants.SESSION_COMMENT_ADMIN_ITEMS_PER_PAGE );
            if ( object != null )
            {
                nItemsPerPage = (Integer) object;
            }
            object = request.getSession( ).getAttribute( CommentConstants.SESSION_COMMENT_ADMIN_CURRENT_PAGE_INDEX );
            if ( object != null )
            {
                strCurrentPageIndex = (String) object;
            }
            object = request.getSession( ).getAttribute( CommentConstants.SESSION_COMMENT_ADMIN_IS_ASC_SORT );
            if ( object != null )
            {
                bIsAscSort = (Boolean) object;
            }
            object = request.getSession( ).getAttribute( CommentConstants.SESSION_COMMENT_ADMIN_SORTED_ATTRIBUTE_NAME );
            if ( object != null )
            {
                strSortedAttributeName = (String) object;
            }

            int nItemsCount = _commentService.getCommentNb( resourceExtender.getIdExtendableResource( ),
                    resourceExtender.getExtendableResourceType( ), config.getAuthorizeSubComments( ), false );
            String strFromUrl = StringUtils.replace( request.getParameter( CommentConstants.PARAMETER_FROM_URL ),
                    CommentConstants.CONSTANT_AND, CommentConstants.CONSTANT_AND_HTML );
            strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, strCurrentPageIndex );
            int nOldITemsPerPage = nItemsPerPage;
            nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, nItemsPerPage,
                    _nDefaultItemsPerPage );
            if ( nItemsPerPage <= 0 )
            {
                nItemsPerPage = _nDefaultItemsPerPage;
            }
            // If we changed the number of items per page, we go back to the first page
            if ( nItemsPerPage != nOldITemsPerPage )
            {
                strCurrentPageIndex = CommentConstants.CONSTANT_FIRST_PAGE_NUMBER;
            }
            String strNewSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
            if ( strNewSortedAttributeName != null )
            {
                // We update sort properties
                strSortedAttributeName = strNewSortedAttributeName;
                bIsAscSort = Boolean.parseBoolean( request.getParameter( Parameters.SORTED_ASC ) );
            }
            int nItemsOffset = nItemsPerPage * ( Integer.parseInt( strCurrentPageIndex ) - 1 );

            List<Comment> listComments = _commentService.findByResource( resourceExtender.getIdExtendableResource( ),
                    resourceExtender.getExtendableResourceType( ), false, strSortedAttributeName, bIsAscSort,
                    nItemsOffset, nItemsPerPage, config.getAuthorizeSubComments( ) );

            // We generate the base URL for the paginator
            UrlItem url = new UrlItem( strPostBackUrl );
            url.addParameter( CommentConstants.PARAMETER_EXTENDER_TYPE, CommentResourceExtender.EXTENDER_TYPE_COMMENT );
            url.addParameter( CommentConstants.PARAMETER_ID_EXTENDABLE_RESOURCE,
                    resourceExtender.getIdExtendableResource( ) );
            url.addParameter( CommentConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE,
                    resourceExtender.getExtendableResourceType( ) );
            url.addParameter( CommentConstants.PARAMETER_FROM_URL, strFromUrl );

            // We get the paginator
            IPaginator<Comment> paginator = new LocalizedDelegatePaginator<Comment>( listComments, nItemsPerPage,
                    url.getUrl( ), Paginator.PARAMETER_PAGE_INDEX, strCurrentPageIndex, nItemsCount,
                    AdminUserService.getLocale( request ) );

            Map<String, Object> model = new HashMap<String, Object>( );
            model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, resourceExtender.getIdExtendableResource( ) );
            model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, resourceExtender.getExtendableResourceType( ) );
            model.put( CommentConstants.MARK_LIST_COMMENTS, paginator.getPageItems( ) );
            model.put( CommentConstants.PARAMETER_FROM_URL, strFromUrl );
            model.put( CommentConstants.MARK_PAGINATOR, paginator );
            model.put( CommentConstants.MARK_NB_ITEMS_PER_PAGE, Integer.toString( paginator.getItemsPerPage( ) ) );
            model.put( CommentConstants.MARK_ASC_SORT, bIsAscSort );
            model.put( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
            model.put( CommentConstants.PARAMETER_ID_COMMENT,
                    request.getParameter( CommentConstants.PARAMETER_ID_COMMENT ) );
            model.put( CommentConstants.MARK_USE_BBCODE, config.getUseBBCodeEditor( ) );
            model.put( CommentConstants.MARK_ADMIN_BADGE, config.getAdminBadge( ) );
            model.put( CommentConstants.MARK_ALLOW_SUB_COMMENTS, config.getAuthorizeSubComments( ) );
            model.put( CommentConstants.PARAMETER_POST_BACK_URL, strPostBackUrl );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_COMMENTS, request.getLocale( ),
                    model );

            // We save the pagination info in the session
            HttpSession session = request.getSession( );
            session.setAttribute( CommentConstants.SESSION_COMMENT_ADMIN_ITEMS_PER_PAGE, nItemsPerPage );
            session.setAttribute( CommentConstants.SESSION_COMMENT_ADMIN_CURRENT_PAGE_INDEX, strCurrentPageIndex );
            session.setAttribute( CommentConstants.SESSION_COMMENT_ADMIN_IS_ASC_SORT, bIsAscSort );
            session.setAttribute( CommentConstants.SESSION_COMMENT_ADMIN_SORTED_ATTRIBUTE_NAME, strSortedAttributeName );

            String strContent = template.getHtml( );

            ContentPostProcessor postProcessor = getExtendPostProcessor( );
            if ( postProcessor != null )
            {
                strContent = postProcessor.process( request, strContent );
            }

            return strContent;
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveConfig( HttpServletRequest request, IExtenderConfig config ) throws ExtendErrorException
    {
        _configService.update( config );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPostBackUrl( HttpServletRequest request )
    {
        UrlItem urlItem = new UrlItem( request.getRequestURI( ) );
        Map<String, String[]> mapParameters = new HashMap<String, String[]>( request.getParameterMap( ) );
        // We ignore some parameters : those parameter will be set regardlessly of the post pack URL
        mapParameters.remove( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE );
        mapParameters.remove( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE );
        mapParameters.remove( CommentConstants.PARAMETER_EXTENDER_TYPE );
        mapParameters.remove( CommentConstants.PARAMETER_FROM_URL );
        mapParameters.remove( CommentConstants.PARAMETER_ID_COMMENT );
        mapParameters.remove( Paginator.PARAMETER_PAGE_INDEX );
        mapParameters.remove( Paginator.PARAMETER_ITEMS_PER_PAGE );
        mapParameters.remove( Parameters.SORTED_ATTRIBUTE_NAME );
        mapParameters.remove( Parameters.SORTED_ASC );
        for ( Entry<String, String[]> entry : mapParameters.entrySet( ) )
        {
            urlItem.addParameter( entry.getKey( ), entry.getValue( )[0] );
        }
        return urlItem.getUrl( );
    }

    /**
     * Get the content post processor of plugin extend
     * @return the content post processor of plugin extend
     */
    private ContentPostProcessor getExtendPostProcessor( )
    {
        if ( _contentPostProcessor == null )
        {
            synchronized ( this )
            {
                if ( _contentPostProcessor == null )
                {
                    _contentPostProcessor = SpringContextService.getBean( ExtendableContentPostProcessor.BEAN_NAME );
                }
            }
        }
        return _contentPostProcessor;
    }
}
