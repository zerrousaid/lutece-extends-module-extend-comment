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
package fr.paris.lutece.plugins.extend.modules.comment.web;

import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.config.CommentExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentPlugin;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.modules.comment.util.constants.CommentConstants;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.service.captcha.CaptchaSecurityService;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.util.LocalizedDelegatePaginator;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.IPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 
 * CommentApp
 * 
 */
public class CommentApp implements XPageApplication
{
    // MESSAGES
    private static final String MESSAGE_STOP_GENERIC_MESSAGE = "module.extend.comment.message.stop.genericMessage";
    private static final String MESSAGE_ERROR_BAD_JCAPTCHA = "module.extend.comment.message.error.badJcaptcha";

    // PROPERTIES
    private static final String PROPERTY_USE_CAPTCHA = "module.extend.comment.useCaptcha";

    // MARKS
    private static final String MARK_CAPTCHA = "captcha";
    private static final String MARK_IS_ACTIVE_CAPTCHA = "is_active_captcha";

    // TEMPLATES
    private static final String TEMPLATE_XPAGE_VIEW_COMMENTS = "skin/plugins/extend/modules/comment/view_comments.html";
    private static final String TEMPLATE_XPAGE_ADD_COMMENT = "skin/plugins/extend/modules/comment/add_comment.html";
    private static final String TEMPLATE_XPAGE_MESSAGE_COMMENT_CREATED = "skin/plugins/extend/modules/comment/message_comment_created.html";
    private static final String TEMPLATE_COMMENT_NOTIFY_MESSAGE = "skin/plugins/extend/modules/comment/comment_notify_message.html";

    private static final String JSP_URL_DEFAULT_POST_BACK = "jsp/site/Portal.jsp?page=extend-comment";

    // CONSTANTS
    private static final String JCAPTCHA_PLUGIN = "jcaptcha";
    private static final String HTML_BR = "<br />";
    private static final String FROM_SESSION = "from_session";
    private static final String CONSTANT_AND = "&";
    private static final String CONSTANT_AND_HTML = "%26";

    // VARIABLES
    private static ICommentService _commentService;
    private static IResourceExtenderConfigService _configService;
    private static IResourceExtenderService _resourceExtenderService;
    private static IResourceExtenderHistoryService _resourceHistoryService;

    private static int _nDefaultItemsPerPage;

    private final boolean _bIsCaptchaEnabled = PluginService.isPluginEnable( JCAPTCHA_PLUGIN )
            && Boolean
                    .parseBoolean( AppPropertiesService.getProperty( PROPERTY_USE_CAPTCHA, Boolean.TRUE.toString( ) ) );

    /**
     * {@inheritDoc}
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws UserNotSignedException,
            SiteMessageException
    {
        XPage page = null;

        // Check if the extender is indeed in the parameters
        String strIdExtendableResource = request.getParameter( CommentConstants.PARAMETER_ID_EXTENDABLE_RESOURCE );
        String strExtendableResourceType = request.getParameter( CommentConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE );

        if ( StringUtils.isBlank( strIdExtendableResource ) || StringUtils.isBlank( strExtendableResourceType ) )
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_STOP );
        }

        String strAction = request.getParameter( CommentConstants.PARAMETER_ACTION );

        if ( StringUtils.isNotBlank( strAction ) )
        {
            if ( CommentConstants.ACTION_ADD_COMMENT.equals( strAction ) )
            {
                page = getAddCommentPage( request, strIdExtendableResource, strExtendableResourceType );
            }
            else if ( CommentConstants.ACTION_DO_ADD_COMMENT.equals( strAction ) )
            {
                page = doAddComment( request, strIdExtendableResource, strExtendableResourceType );
            }
        }

        if ( page == null )
        {
            page = getViewCommentPage( request, strIdExtendableResource, strExtendableResourceType );
        }

        return page;
    }

    /**
     * Gets the view comment page.
     * 
     * @param request the request
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param strPostBackUrl The URL to use for post backs.
     * @return the view comment page
     */
    public static String getViewCommentPageContent( HttpServletRequest request, String strIdExtendableResource,
            String strExtendableResourceType, String strPostBackUrl )
    {
        request.getSession( ).setAttribute( ExtendPlugin.PLUGIN_NAME + CommentConstants.SESSION_COMMENT_POST_BACK_URL,
                strPostBackUrl );

        Integer nItemsPerPage = getDefaultItemsPerPage( );
        String strCurrentPageIndex = CommentConstants.CONSTANT_FIRST_PAGE_NUMBER;
        Boolean bIsAscSort = false;
        Object object = request.getSession( ).getAttribute( CommentConstants.SESSION_COMMENT_ITEMS_PER_PAGE );
        if ( object != null )
        {
            nItemsPerPage = (Integer) object;
        }
        object = request.getSession( ).getAttribute( CommentConstants.SESSION_COMMENT_CURRENT_PAGE_INDEX );
        if ( object != null )
        {
            strCurrentPageIndex = (String) object;
        }
        object = request.getSession( ).getAttribute( CommentConstants.SESSION_COMMENT_IS_ASC_SORT );
        if ( object != null )
        {
            bIsAscSort = (Boolean) object;
        }

        String strSort = request.getParameter( CommentConstants.MARK_ASC_SORT );
        if ( !StringUtils.isEmpty( strSort ) )
        {
            bIsAscSort = Boolean.parseBoolean( strSort );
        }

        String strFromUrl = request.getParameter( CommentConstants.PARAMETER_FROM_URL );
        if ( FROM_SESSION.equals( strFromUrl ) )
        {
            strFromUrl = (String) request.getSession( ).getAttribute(
                    ExtendPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL );
        }
        if ( StringUtils.isEmpty( strFromUrl ) )
        {
            strFromUrl = request.getHeader( CommentConstants.PARAMETER_REFERER );
        }
        if ( strFromUrl != null )
        {
            strFromUrl = strFromUrl.replace( CONSTANT_AND, CONSTANT_AND_HTML );
        }
        request.getSession( ).setAttribute( ExtendPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL, strFromUrl );

        strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, strCurrentPageIndex );
        int nOldITemsPerPage = nItemsPerPage;
        nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, nItemsPerPage,
                getDefaultItemsPerPage( ) );
        if ( nItemsPerPage <= 0 )
        {
            nItemsPerPage = getDefaultItemsPerPage( );
        }
        // If we changed the number of items per page, we go back to the first page
        if ( nItemsPerPage != nOldITemsPerPage )
        {
            strCurrentPageIndex = CommentConstants.CONSTANT_FIRST_PAGE_NUMBER;
        }
        UrlItem urlSort = new UrlItem( strPostBackUrl );
        urlSort.addParameter( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        urlSort.addParameter( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        urlSort.addParameter( CommentConstants.MARK_ASC_SORT, strSort );
        if ( StringUtils.isNotEmpty( strFromUrl ) )
        {
            urlSort.addParameter( CommentConstants.PARAMETER_FROM_URL, FROM_SESSION );
        }
        boolean bGetSubComments = false;
        boolean bUseBBCodeEditor = false;
        boolean bAllowSubComments = false;
        String strAdminBadge = StringUtils.EMPTY;
        CommentExtenderConfig config = getConfigService( ).find( CommentResourceExtender.EXTENDER_TYPE_COMMENT,
                strIdExtendableResource, strExtendableResourceType );
        if ( config != null )
        {
            bGetSubComments = config.getAuthorizeSubComments( );
            bUseBBCodeEditor = config.getUseBBCodeEditor( );
            bAllowSubComments = config.getAuthorizeSubComments( );
            strAdminBadge = config.getAdminBadge( );
        }
        int nItemsOffset = nItemsPerPage * ( Integer.parseInt( strCurrentPageIndex ) - 1 );

        List<Comment> listItems = getCommentService( ).findByResource( strIdExtendableResource,
                strExtendableResourceType, true, null, bIsAscSort, nItemsOffset, nItemsPerPage, bGetSubComments );

        int nItemsCount = getCommentService( ).getCommentNb( strIdExtendableResource, strExtendableResourceType, true,
                true );

        IPaginator<Comment> paginator = new LocalizedDelegatePaginator<Comment>( listItems, nItemsPerPage,
                urlSort.getUrl( ), Paginator.PARAMETER_PAGE_INDEX, strCurrentPageIndex, nItemsCount,
                request.getLocale( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        model.put( CommentConstants.MARK_PAGINATOR, paginator );
        model.put( CommentConstants.MARK_ASC_SORT, strSort );
        model.put( CommentConstants.PARAMETER_FROM_URL, strFromUrl );
        model.put( CommentConstants.PARAMETER_ID_COMMENT, request.getParameter( CommentConstants.PARAMETER_ID_COMMENT ) );
        model.put( CommentConstants.MARK_USE_BBCODE, bUseBBCodeEditor );
        model.put( CommentConstants.MARK_ALLOW_SUB_COMMENTS, bAllowSubComments );
        model.put( CommentConstants.MARK_ADMIN_BADGE, strAdminBadge );
        model.put( CommentConstants.PARAMETER_POST_BACK_URL, strPostBackUrl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_VIEW_COMMENTS, request.getLocale( ),
                model );

        HttpSession session = request.getSession( );
        session.setAttribute( CommentConstants.SESSION_COMMENT_ITEMS_PER_PAGE, nItemsPerPage );
        session.setAttribute( CommentConstants.SESSION_COMMENT_CURRENT_PAGE_INDEX, strCurrentPageIndex );
        session.setAttribute( CommentConstants.SESSION_COMMENT_IS_ASC_SORT, bIsAscSort );

        return template.getHtml( );
    }

    /**
     * Gets the view comment page.
     * 
     * @param request the request
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the view comment page
     */
    public XPage getViewCommentPage( HttpServletRequest request, String strIdExtendableResource,
            String strExtendableResourceType )
    {
        XPage page = new XPage( );
        page.setTitle( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_VIEW_COMMENTS_PAGE_TITLE,
                request.getLocale( ) ) );
        page.setPathLabel( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_VIEW_COMMENTS_PATH_LABEL,
                request.getLocale( ) ) );

        page.setContent( getViewCommentPageContent( request, strIdExtendableResource, strExtendableResourceType,
                JSP_URL_DEFAULT_POST_BACK ) );
        return page;
    }

    /**
     * Gets the adds the comment page.
     * 
     * @param request the request
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the adds the comment page
     */
    private XPage getAddCommentPage( HttpServletRequest request, String strIdExtendableResource,
            String strExtendableResourceType )
    {
        XPage page = new XPage( );

        page.setTitle( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_ADD_COMMENT_PAGE_TITLE,
                request.getLocale( ) ) );
        page.setPathLabel( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_ADD_COMMENT_PAGE_LABEL,
                request.getLocale( ) ) );

        CommentExtenderConfig config = getConfigService( ).find( CommentResourceExtender.EXTENDER_TYPE_COMMENT,
                strIdExtendableResource, strExtendableResourceType );

        String strFromUrl = request.getParameter( CommentConstants.PARAMETER_FROM_URL );
        if ( FROM_SESSION.equals( strFromUrl ) )
        {
            strFromUrl = (String) request.getSession( ).getAttribute(
                    ExtendPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL );
        }
        if ( StringUtils.isEmpty( strFromUrl ) )
        {
            strFromUrl = request.getHeader( CommentConstants.PARAMETER_REFERER );
        }
        if ( strFromUrl != null )
        {
            strFromUrl = strFromUrl.replace( CONSTANT_AND, CONSTANT_AND_HTML );
        }
        request.getSession( ).setAttribute( ExtendPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL, strFromUrl );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( CommentConstants.MARK_COMMENT_CONFIG, config );
        model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        model.put( CommentConstants.PARAMETER_FROM_URL, strFromUrl );
        model.put( CommentConstants.MARK_RETURN_TO_COMMENT_LIST,
                Boolean.parseBoolean( request.getParameter( CommentConstants.MARK_RETURN_TO_COMMENT_LIST ) ) );
        model.put( CommentConstants.PARAMETER_ID_COMMENT, request.getParameter( CommentConstants.PARAMETER_ID_COMMENT ) );
        model.put( CommentConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( CommentConstants.MARK_LOCALE, Locale.getDefault( ) );

        // Add Captcha
        model.put( MARK_IS_ACTIVE_CAPTCHA, _bIsCaptchaEnabled );

        if ( _bIsCaptchaEnabled )
        {
            CaptchaSecurityService captchaService = new CaptchaSecurityService( );
            model.put( MARK_CAPTCHA, captchaService.getHtmlCode( ) );
        }

        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

            if ( user != null )
            {
                model.put( CommentConstants.MARK_MYLUTECE_USER, user );
            }
        }

        HtmlTemplate template = AppTemplateService
                .getTemplate( TEMPLATE_XPAGE_ADD_COMMENT, request.getLocale( ), model );

        page.setContent( template.getHtml( ) );

        return page;
    }

    /**
     * Do add comment.
     * 
     * @param request the request
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return The page to display, or null to display the default page
     * @throws SiteMessageException the site message exception
     */
    private XPage doAddComment( HttpServletRequest request, String strIdExtendableResource,
            String strExtendableResourceType ) throws SiteMessageException
    {
        Comment comment = new Comment( );

        try
        {
            BeanUtils.populate( comment, request.getParameterMap( ) );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( "Unable to fetch data from request", e );
        }
        catch ( InvocationTargetException e )
        {
            AppLogService.error( "Unable to fetch data from request", e );
        }

        // Check mandatory fields
        Set<ConstraintViolation<Comment>> constraintViolations = BeanValidationUtil.validate( comment );

        if ( constraintViolations.size( ) > 0 )
        {
            Object[] params = { buildStopMessage( constraintViolations ) };

            SiteMessageService.setMessage( request, MESSAGE_STOP_GENERIC_MESSAGE, params, SiteMessage.TYPE_STOP );
        }

        if ( StringUtils.isBlank( comment.getName( ) ) || StringUtils.isBlank( comment.getComment( ) )
                || StringUtils.isBlank( comment.getEmail( ) ) )
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_STOP );
        }

        // Test the captcha
        if ( _bIsCaptchaEnabled )
        {
            CaptchaSecurityService captchaService = new CaptchaSecurityService( );

            if ( !captchaService.validate( request ) )
            {
                SiteMessageService.setMessage( request, MESSAGE_ERROR_BAD_JCAPTCHA, SiteMessage.TYPE_STOP );
            }
        }

        CommentExtenderConfig config = getConfigService( ).find( CommentResourceExtender.EXTENDER_TYPE_COMMENT,
                strIdExtendableResource, strExtendableResourceType );

        if ( config != null )
        {
            comment.setIpAddress( SecurityUtil.getRealIp( request ) );
            comment.setIdExtendableResource( strIdExtendableResource );
            comment.setExtendableResourceType( strExtendableResourceType );
            comment.setPublished( !config.isModerated( ) );
            if ( !config.getAuthorizeSubComments( ) )
            {
                comment.setIdParentComment( 0 );
            }
            boolean bIsCreated = false;

            try
            {
                getCommentService( ).create( comment );
                bIsCreated = true;
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage( ) + " when creating a comment ", ex );
                // Revert
                getCommentService( ).remove( comment.getIdComment( ) );

                SiteMessageService.setMessage( request, CommentConstants.MESSAGE_ERROR_GENERIC_MESSAGE,
                        SiteMessage.TYPE_ERROR );
            }

            if ( bIsCreated )
            {
                // Add to the resource extender history
                getResourceExtenderHistoryService( ).create( CommentResourceExtender.EXTENDER_TYPE_COMMENT,
                        strIdExtendableResource, strExtendableResourceType, request );

                // Notify the mailing list
                sendCommentNotification( request, comment, config );

                XPage page = new XPage( );
                page.setTitle( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_ADD_COMMENT_PAGE_TITLE,
                        request.getLocale( ) ) );
                page.setPathLabel( I18nService.getLocalizedString(
                        CommentConstants.PROPERTY_XPAGE_ADD_COMMENT_PAGE_LABEL, request.getLocale( ) ) );

                String strPostBackUrl = (String) request.getSession( ).getAttribute(
                        ExtendPlugin.PLUGIN_NAME + CommentConstants.SESSION_COMMENT_POST_BACK_URL );
                if ( strPostBackUrl == null )
                {
                    strPostBackUrl = JSP_URL_DEFAULT_POST_BACK;
                }
                String strFromUrl = request.getParameter( CommentConstants.PARAMETER_FROM_URL );
                if ( FROM_SESSION.equals( strFromUrl ) )
                {
                    strFromUrl = (String) request.getSession( ).getAttribute(
                            ExtendPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL );
                }
                Map<String, Object> model = new HashMap<String, Object>( );
                model.put( CommentConstants.MARK_MESSAGE_COMMENT_CREATED, config.getMessageCommentCreated( ) );
                model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
                model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
                model.put( CommentConstants.PARAMETER_ID_COMMENT,

                comment.getIdParentComment( ) == 0 ? comment.getIdComment( ) : comment.getIdParentComment( ) );
                model.put( CommentConstants.PARAMETER_POST_BACK_URL, strPostBackUrl );
                model.put( CommentConstants.MARK_RETURN_TO_COMMENT_LIST,
                        Boolean.parseBoolean( request.getParameter( CommentConstants.MARK_RETURN_TO_COMMENT_LIST ) ) );
                model.put( CommentConstants.PARAMETER_FROM_URL, strFromUrl );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_MESSAGE_COMMENT_CREATED,
                        request.getLocale( ), model );

                page.setContent( template.getHtml( ) );

                return page;
            }
            return null;
        }
        SiteMessageService.setMessage( request, CommentConstants.MESSAGE_ERROR_GENERIC_MESSAGE, SiteMessage.TYPE_ERROR );
        return null;
    }

    /**
     * Builds the stop message.
     * 
     * @param <A> the generic type
     * @param listErrors the list errors
     * @return the string
     */
    private static <A> String buildStopMessage( Set<ConstraintViolation<A>> listErrors )
    {
        StringBuilder sbError = new StringBuilder( );

        if ( ( listErrors != null ) && !listErrors.isEmpty( ) )
        {
            for ( ConstraintViolation<A> error : listErrors )
            {
                sbError.append( error.getMessage( ) );
                sbError.append( HTML_BR );
            }
        }

        return sbError.toString( );
    }

    /**
     * Send comment notification.
     * 
     * @param request the request
     * @param comment the document comment
     * @param config the config
     */
    private void sendCommentNotification( HttpServletRequest request, Comment comment, CommentExtenderConfig config )
    {
        int nMailingListId = config.getIdMailingList( );
        Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( nMailingListId );

        for ( Recipient recipient : listRecipients )
        {
            Map<String, Object> model = new HashMap<String, Object>( );

            String strSenderEmail = comment.getEmail( );
            String strSenderName = comment.getName( );
            String strResourceName = getResourceExtenderService( ).getExtendableResourceName(
                    comment.getIdExtendableResource( ), comment.getExtendableResourceType( ) );

            Object[] params = { strResourceName };
            String strSubject = I18nService.getLocalizedString( CommentConstants.MESSAGE_NOTIFY_SUBJECT, params,
                    request.getLocale( ) );

            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl( ) );
            url.addParameter( XPageAppService.PARAM_XPAGE_APP, CommentPlugin.PLUGIN_NAME );
            url.addParameter( CommentConstants.PARAMETER_ID_EXTENDABLE_RESOURCE, comment.getIdExtendableResource( ) );
            url.addParameter( CommentConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE, comment.getExtendableResourceType( ) );

            model.put( CommentConstants.MARK_RESOURCE_EXTENDER_NAME, strResourceName );
            model.put( CommentConstants.MARK_RESOURCE_EXTENDER_URL, url.getUrl( ) );
            model.put( CommentConstants.MARK_COMMENT, comment );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMMENT_NOTIFY_MESSAGE,
                    request.getLocale( ), model );
            String strBody = template.getHtml( );

            MailService.sendMailHtml( recipient.getEmail( ), strSenderName, strSenderEmail, strSubject, strBody );
        }
    }

    /**
     * Get the default number of items per page
     * @return the default number of items per page
     */
    private static int getDefaultItemsPerPage( )
    {
        if ( _nDefaultItemsPerPage == 0 )
        {
            _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt(
                    CommentConstants.PROPERTY_DEFAULT_LIST_COMMENTS_PER_PAGE, 50 );
        }
        return _nDefaultItemsPerPage;
    }

    /**
     * Get the comment service
     * @return the comment service
     */
    private static ICommentService getCommentService( )
    {
        if ( _commentService == null )
        {
            _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );
        }
        return _commentService;
    }

    /**
     * Get the config service
     * @return the config service
     */
    private static IResourceExtenderConfigService getConfigService( )
    {
        if ( _configService == null )
        {
            _configService = SpringContextService.getBean( CommentConstants.BEAN_CONFIG_SERVICE );
        }
        return _configService;
    }

    /**
     * Get the resource extender service
     * @return the resource extender service
     */
    private static IResourceExtenderService getResourceExtenderService( )
    {
        if ( _resourceExtenderService == null )
        {
            _resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );
        }
        return _resourceExtenderService;
    }

    /**
     * Get the resource extender history service
     * @return the resource extender history service
     */
    private static IResourceExtenderHistoryService getResourceExtenderHistoryService( )
    {
        if ( _resourceHistoryService == null )
        {
            _resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
        }
        return _resourceHistoryService;
    }
}
