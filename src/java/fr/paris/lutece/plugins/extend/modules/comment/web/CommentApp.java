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
import fr.paris.lutece.plugins.extend.modules.comment.business.config.CommentExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentPlugin;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.modules.comment.util.constants.CommentConstants;
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
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
    private static final String TEMPLATE_COMMENT_NOTIFY_MESSAGE = "skin/plugins/extend/modules/comment/comment_notify_message.html";

	private static final String PARAMETER_PAGE = "page";

    // CONSTANTS
    private static final String JCAPTCHA_PLUGIN = "jcaptcha";
    private static final String HTML_BR = "<br />";
    private static final String FROM_SESSION = "from_session";

    // VARIABLES
    private static final boolean _bIsCaptchaEnabled = PluginService.isPluginEnable( JCAPTCHA_PLUGIN ) &&
        Boolean.parseBoolean( AppPropertiesService.getProperty( PROPERTY_USE_CAPTCHA, Boolean.TRUE.toString(  ) ) );
    private ICommentService _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );
    private IResourceExtenderConfigService _configService = SpringContextService.getBean( CommentConstants.BEAN_CONFIG_SERVICE );
    private IResourceExtenderService _resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );
    private IResourceExtenderHistoryService _resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

	private String _strCurrentPageIndex;
	private int _nItemsPerPage;
	private int _nDefautlItemsPerPage = 50;

    /**
     * {@inheritDoc}
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws UserNotSignedException, SiteMessageException
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
                doAddComment( request, strIdExtendableResource, strExtendableResourceType );
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
     * @return the view comment page
     */
    private XPage getViewCommentPage( HttpServletRequest request, String strIdExtendableResource,
        String strExtendableResourceType )
    {
        XPage page = new XPage(  );
        page.setTitle( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_VIEW_COMMENTS_PAGE_TITLE,
                request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_VIEW_COMMENTS_PATH_LABEL,
                request.getLocale(  ) ) );

        boolean bAscSort;
		String strSort = request.getParameter( CommentConstants.MARK_ASC_SORT );
        if ( StringUtils.isEmpty( strSort ) )	
        {
			bAscSort = false;
        }
        else
        {
			bAscSort = Boolean.parseBoolean( strSort );
        }
        
        String strFromUrl = request.getParameter( CommentConstants.PARAMETER_FROM_URL );
        if ( FROM_SESSION.equals( strFromUrl ) )
        {
            strFromUrl = (String) request.getSession( ).getAttribute(
                    CommentPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL );
        }
        if ( StringUtils.isEmpty( strFromUrl ) )
        {
            strFromUrl = request.getHeader( CommentConstants.PARAMETER_REFERER );
        }
        if ( strFromUrl != null )
        {
            strFromUrl = strFromUrl.replace( "&", "%26" );
        }
        request.getSession( )
                .setAttribute( CommentPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL, strFromUrl );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
		_nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefautlItemsPerPage );
		UrlItem urlSort = new UrlItem( AppPathService.getPortalUrl( ) );
		urlSort.addParameter( PARAMETER_PAGE, CommentPlugin.PLUGIN_NAME );
		urlSort.addParameter( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
		urlSort.addParameter( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
		urlSort.addParameter( CommentConstants.MARK_ASC_SORT, strSort );
        if ( StringUtils.isNotEmpty( strFromUrl ) )
        {
            urlSort.addParameter( CommentConstants.PARAMETER_FROM_URL, FROM_SESSION );
        }
		List<Comment> listItems = _commentService.findByResource( strIdExtendableResource, strExtendableResourceType, true, bAscSort );

		Paginator<Comment> paginator = new LocalizedPaginator<Comment>( listItems, _nItemsPerPage, urlSort.getUrl( ), Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, request
				.getLocale( ) );
        
        Map<String, Object> model = new HashMap<String, Object>(  );
		// model.put( CommentConstants.MARK_LIST_COMMENTS, paginator.getPageItems( ) );
        model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
		model.put( CommentConstants.MARK_PAGINATOR, paginator );
		model.put( CommentConstants.MARK_ASC_SORT, strSort );
        model.put( CommentConstants.PARAMETER_FROM_URL, strFromUrl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_VIEW_COMMENTS, request.getLocale(  ),
                model );

        page.setContent( template.getHtml(  ) );

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
        XPage page = new XPage(  );

        page.setTitle( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_ADD_COMMENT_PAGE_TITLE,
                request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( CommentConstants.PROPERTY_XPAGE_ADD_COMMENT_PAGE_LABEL,
                request.getLocale(  ) ) );

        CommentExtenderConfig config = _configService.find( CommentResourceExtender.EXTENDER_TYPE_COMMENT,
                strIdExtendableResource, strExtendableResourceType );

        String strFromUrl = request.getParameter( CommentConstants.PARAMETER_FROM_URL );
        if ( FROM_SESSION.equals( strFromUrl ) )
        {
            strFromUrl = (String) request.getSession( ).getAttribute(
                    CommentPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL );
        }
        if ( StringUtils.isEmpty( strFromUrl ) )
        {
            strFromUrl = request.getHeader( CommentConstants.PARAMETER_REFERER );
        }
        if ( strFromUrl != null )
        {
            strFromUrl = strFromUrl.replace( "&", "%26" );
        }
        request.getSession( )
                .setAttribute( CommentPlugin.PLUGIN_NAME + CommentConstants.PARAMETER_FROM_URL, strFromUrl );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( CommentConstants.MARK_COMMENT_CONFIG, config );
        model.put( CommentConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( CommentConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        model.put( CommentConstants.PARAMETER_FROM_URL, strFromUrl );

        // Add Captcha
        model.put( MARK_IS_ACTIVE_CAPTCHA, _bIsCaptchaEnabled );

        if ( _bIsCaptchaEnabled )
        {
            CaptchaSecurityService captchaService = new CaptchaSecurityService(  );
            model.put( MARK_CAPTCHA, captchaService.getHtmlCode(  ) );
        }

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( user != null )
            {
                model.put( CommentConstants.MARK_MYLUTECE_USER, user );
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_ADD_COMMENT, request.getLocale(  ), model );

        page.setContent( template.getHtml(  ) );

        return page;
    }

    /**
     * Do add comment.
     *
     * @param request the request
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @throws SiteMessageException the site message exception
     */
    private void doAddComment( HttpServletRequest request, String strIdExtendableResource,
        String strExtendableResourceType ) throws SiteMessageException
    {
        Comment comment = new Comment(  );

        try
        {
            BeanUtils.populate( comment, request.getParameterMap(  ) );
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

        if ( constraintViolations.size(  ) > 0 )
        {
            Object[] params = { buildStopMessage( constraintViolations ) };

            SiteMessageService.setMessage( request, MESSAGE_STOP_GENERIC_MESSAGE, params, SiteMessage.TYPE_STOP );
        }

        if ( StringUtils.isBlank( comment.getName(  ) ) || StringUtils.isBlank( comment.getComment(  ) ) ||
                StringUtils.isBlank( comment.getEmail(  ) ) )
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, SiteMessage.TYPE_STOP );
        }

        // Test the captcha
        if ( _bIsCaptchaEnabled )
        {
            CaptchaSecurityService captchaService = new CaptchaSecurityService(  );

            if ( !captchaService.validate( request ) )
            {
                SiteMessageService.setMessage( request, MESSAGE_ERROR_BAD_JCAPTCHA, SiteMessage.TYPE_STOP );
            }
        }

        CommentExtenderConfig config = _configService.find( CommentResourceExtender.EXTENDER_TYPE_COMMENT,
                strIdExtendableResource, strExtendableResourceType );

        if ( config != null )
        {
            comment.setIpAddress( request.getRemoteAddr(  ) );
            comment.setIdExtendableResource( strIdExtendableResource );
            comment.setExtendableResourceType( strExtendableResourceType );
            comment.setPublished( !config.isModerated(  ) );

            boolean bIsCreated = false;

            try
            {
                _commentService.create( comment );
                bIsCreated = true;
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when creating a comment ", ex );
                // Revert
                _commentService.remove( comment.getIdComment(  ) );

                SiteMessageService.setMessage( request, CommentConstants.MESSAGE_ERROR_GENERIC_MESSAGE,
                    SiteMessage.TYPE_ERROR );
            }

            if ( bIsCreated )
            {
                // Add to the resource extender history
                _resourceHistoryService.create( CommentResourceExtender.EXTENDER_TYPE_COMMENT, strIdExtendableResource,
                    strExtendableResourceType, request );

                // Notify the mailing list
                sendCommentNotification( request, comment, config );
            }
        }
        else
        {
            SiteMessageService.setMessage( request, CommentConstants.MESSAGE_ERROR_GENERIC_MESSAGE,
                SiteMessage.TYPE_ERROR );
        }
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
        StringBuilder sbError = new StringBuilder(  );

        if ( ( listErrors != null ) && !listErrors.isEmpty(  ) )
        {
            for ( ConstraintViolation<A> error : listErrors )
            {
                sbError.append( error.getMessage(  ) );
                sbError.append( HTML_BR );
            }
        }

        return sbError.toString(  );
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
        int nMailingListId = config.getIdMailingList(  );
        Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( nMailingListId );

        for ( Recipient recipient : listRecipients )
        {
            Map<String, Object> model = new HashMap<String, Object>(  );

            String strSenderEmail = comment.getEmail(  );
            String strSenderName = comment.getName(  );
            String strResourceName = _resourceExtenderService.getExtendableResourceName( comment.getIdExtendableResource(  ),
                    comment.getExtendableResourceType(  ) );

            Object[] params = { strResourceName };
            String strSubject = I18nService.getLocalizedString( CommentConstants.MESSAGE_NOTIFY_SUBJECT, params,
                    request.getLocale(  ) );

            UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) );
            url.addParameter( XPageAppService.PARAM_XPAGE_APP, CommentPlugin.PLUGIN_NAME );
            url.addParameter( CommentConstants.PARAMETER_ID_EXTENDABLE_RESOURCE, comment.getIdExtendableResource(  ) );
            url.addParameter( CommentConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE, comment.getExtendableResourceType(  ) );

            model.put( CommentConstants.MARK_RESOURCE_EXTENDER_NAME, strResourceName );
            model.put( CommentConstants.MARK_RESOURCE_EXTENDER_URL, url.getUrl(  ) );
            model.put( CommentConstants.MARK_COMMENT, comment );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMMENT_NOTIFY_MESSAGE,
                    request.getLocale(  ), model );
            String strBody = template.getHtml(  );

            MailService.sendMailHtml( recipient.getEmail(  ), strSenderName, strSenderEmail, strSubject, strBody );
        }
    }
}
