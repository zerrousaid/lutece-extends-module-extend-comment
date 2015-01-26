package fr.paris.lutece.plugins.extend.modules.comment.business;

import java.util.HashMap;
import java.util.Map;


public class AddCommentPosition
{
    
    protected static Map<String					, String> _allPositions;
    
    private static final String NEW_PAGE_STRING = "module.extend.comment.comment_config.position.newPage";
    public static final int NEW_PAGE = 0;
    private static final String TOP_STRING = "module.extend.comment.comment_config.position.top";
    public static final int TOP = 1;
    private static final String BOTTOM_STRING = "module.extend.comment.comment_config.position.bottom";
    public static final int BOTTOM = 2;
    private static final String TOP_BOTTOM_STRING = "module.extend.comment.comment_config.position.topBottom";
    public static final int TOP_BOTTOM = 3;
    private static final String NO_ADD_FORM_STRING = "module.extend.comment.comment_config.position.noAddForm";
    public static final int NO_ADD_FORM = 4;
    /**
     *  @return map of available positions 
     **/
    public static Map<String, String> getAllPositions( )
    {
        if (_allPositions == null)
        {
        	_allPositions = new HashMap<String, String>();
            _allPositions.put( String.valueOf(NEW_PAGE ), NEW_PAGE_STRING);
            _allPositions.put( String.valueOf( TOP ), TOP_STRING);
            _allPositions.put( String.valueOf( BOTTOM ), BOTTOM_STRING );
            _allPositions.put( String.valueOf( TOP_BOTTOM ), TOP_BOTTOM_STRING );
            _allPositions.put( String.valueOf( NO_ADD_FORM ), NO_ADD_FORM_STRING );
        }
        return _allPositions;
    }

}
