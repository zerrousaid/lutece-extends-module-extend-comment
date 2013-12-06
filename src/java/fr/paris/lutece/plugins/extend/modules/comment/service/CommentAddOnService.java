package fr.paris.lutece.plugins.extend.modules.comment.service;

import fr.paris.lutece.portal.business.resourceenhancer.IResourceDisplayManager;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


/**
 * Manager for add on display
 */
public class CommentAddOnService implements IResourceDisplayManager
{
    public static final String PROPERTY_RESOURCE_TYPE = "document";

    private static final String TAG_NUMBER_COMMENT = "document-number-comment";

    @Inject
    @Named( CommentService.BEAN_SERVICE )
    private ICommentService _commentService;

    @Override
    public void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
        if ( PROPERTY_RESOURCE_TYPE.equals( strResourceType ) )
        {
            // Add on for document type
            int nbComment = _commentService.getCommentNb( String.valueOf( nResourceId ), PROPERTY_RESOURCE_TYPE, false,
                    true );
            XmlUtil.addElement( strXml, TAG_NUMBER_COMMENT, nbComment );
        }
    }

    @Override
    public void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource,
            String strPortletId, HttpServletRequest request )
    {
        // TODO Auto-generated method stub
        return;
    }

}
