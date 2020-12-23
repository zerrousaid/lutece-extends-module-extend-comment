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

import java.util.HashMap;
import java.util.Map;

public class AddCommentPosition
{

    protected static Map<String, String> _allPositions;

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
     * @return map of available positions
     **/
    public static Map<String, String> getAllPositions( )
    {
        if ( _allPositions == null )
        {
            _allPositions = new HashMap<String, String>( );
            _allPositions.put( String.valueOf( NEW_PAGE ), NEW_PAGE_STRING );
            _allPositions.put( String.valueOf( TOP ), TOP_STRING );
            _allPositions.put( String.valueOf( BOTTOM ), BOTTOM_STRING );
            _allPositions.put( String.valueOf( TOP_BOTTOM ), TOP_BOTTOM_STRING );
            _allPositions.put( String.valueOf( NO_ADD_FORM ), NO_ADD_FORM_STRING );
        }
        return _allPositions;
    }

}
